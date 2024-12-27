/*
 * Sly Technologies Free License
 * 
 * Copyright 2023 Sly Technologies Inc.
 *
 * Licensed under the Sly Technologies Free License (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.slytechs.com/free-license-text
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.slytechs.jnet.protocol.api.descriptor.impl;

import static com.slytechs.jnet.protocol.tcpipREFACTOR.arp.ArpConstants.*;
import static com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.EtherConstants.*;
import static com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.EtherType.*;
import static com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.VlanConstants.*;
import static com.slytechs.jnet.protocol.tcpipREFACTOR.ipx.IpxConstants.*;
import static com.slytechs.jnet.protocol.tcpipREFACTOR.mpls.MplsConstants.*;
import static com.slytechs.jnet.protocol.tcpipREFACTOR.stp.StpConstants.*;

import java.net.ProtocolException;
import java.nio.ByteBuffer;

import com.slytechs.jnet.platform.api.util.time.TimestampUnit;
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.core.L2FrameType;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public abstract class PacketL2DissectorJava extends AbstractPacketDissector {

	protected static final int NO_NEXT_HEADER = -1;
	/** The Constant DEFAULT_L2_TYPE. */
	protected static final L2FrameType DEFAULT_L2_TYPE = L2FrameType.ETHER;
	/** The timestamp unit. */
	protected TimestampUnit timestampUnit = TimestampUnit.PCAP_MICRO;
	protected ByteBuffer buf;
	protected long timestamp;
	protected int captureLength;
	protected int wireLength;
	protected int dltType = DEFAULT_L2_TYPE.getAsInt();
	protected int l2Type = L2FrameType.L2_FRAME_TYPE_ETHER;
	protected int vlanCount;
	protected int mplsCount;

	/**
	 * 
	 */
	public PacketL2DissectorJava() {
		super();
		
	}

	protected abstract boolean addRecord(int id, int offset, int length);

	/**
	 * Destroy dissector.
	 */
	protected abstract void destroyDissector();

	protected final void dissectEthernet(int offset) {

		if (!hasRemaining(offset, ETHER_HEADER_LEN))
			return;

		int type = Short.toUnsignedInt(buf.getShort(offset + ETHER_FIELD_TYPE));

		if (type > ETHER_MIN_VALUE_FOR_TYPE) {
			// Ethernet2 frame type

			addRecord(CoreId.CORE_ID_ETHER, offset, ETHER_HEADER_LEN);
			l2Type = L2FrameType.L2_FRAME_TYPE_ETHER;
			offset += ETHER_HEADER_LEN;

			dissectEthType(offset, type);

		} else if (hasRemaining(offset, LLC_HEADER_LEN)) { // Nope 802.3 frame
			// THE 802.2 LOGICAL LINK CONTROL (LLC) HEADER

			offset += ETHER_HEADER_LEN;
			addRecord(CoreId.CORE_ID_LLC, offset, LLC_HEADER_LEN);
			l2Type = L2FrameType.L2_FRAME_TYPE_LLC;

			int dsap = Byte.toUnsignedInt(buf.get(offset + LLC_FIELD_DSAP));
			int ssap = Byte.toUnsignedInt(buf.get(offset + LLC_FIELD_SSAP));
			int control = Byte.toUnsignedInt(buf.get(offset + LLC_FIELD_CONTROL));

			offset += LLC_HEADER_LEN;

			if ((control == LLC_TYPE_FRAME)
					&& (ssap == LLC_TYPE_SNAP)
					&& (dsap == LLC_TYPE_SNAP)
					&& hasRemaining(offset, SNAP_HEADER_LEN)) { // Snap + FrameHeader = SNAP
				// THE SUB-NETWORK ACCESS PROTOCOL (SNAP) HEADER

				addRecord(CoreId.CORE_ID_SNAP, offset, SNAP_HEADER_LEN);
				l2Type = L2FrameType.L2_FRAME_TYPE_SNAP;
				type = buf.getShort(offset + SNAP_FIELD_TYPE);

				offset += SNAP_HEADER_LEN;

				dissectEthType(offset, type);

			} else if ((control == LLC_TYPE_FRAME)
					&& (dsap == LLC_TYPE_NETWARE)
					&& (ssap == LLC_TYPE_NETWARE)
					&& hasRemaining(offset, IPX_HEADER_LEN)) {

				// Internetwork Packet Exchange
				dissectIpx(offset);

			} else if ((control == LLC_TYPE_FRAME)
					&& (dsap == LLC_TYPE_STP)
					&& (ssap == LLC_TYPE_STP)
					&& hasRemaining(offset, STP_HEADER_LEN)) {

				addRecord(CoreId.CORE_ID_STP, offset, STP_HEADER_LEN);
			} else {
				return; // Its a pure LLC frame, no protocols

			}
		}
	}

	protected abstract void dissectIp(int offset);

	/**
	 * Dissect eth type.
	 *
	 * @param offset the offset
	 * @param type   the type
	 */
	protected final void dissectEthType(int offset, int type) {

		switch (type) {
		case ETHER_TYPE_IPv4:
		case ETHER_TYPE_IPv6:
			dissectIp(offset);

			break;

		case ETHER_TYPE_VLAN:
			if (addRecord(CoreId.CORE_ID_VLAN, offset, VLAN_HEADER_LEN)) {
				this.vlanCount++;

				type = Short.toUnsignedInt(buf.getShort(offset + VLAN_FIELD_TYPE));
				offset += VLAN_HEADER_LEN;
				dissectEthType(offset, type);
			}

			break;

		case ETHER_TYPE_IPX:
			dissectIpx(offset);

			break;

		case ETHER_TYPE_MPLS:
		case ETHER_TYPE_MPLS_UPSTREAM:

			boolean bottomOfstack = false;
			do {
				this.mplsCount++;

				int label = buf.getInt(offset);
				bottomOfstack = (label & MPLS_BITMASK_BOTTOM) != 0;

				if (!addRecord(CoreId.CORE_ID_MPLS, offset, MPLS_HEADER_LEN))
					return;

				offset += MPLS_HEADER_LEN; // MPLS header length
			} while (!bottomOfstack);

			dissectIp(offset);

			break;

		case ETHER_TYPE_ARP:
		case ETHER_TYPE_RARP:
			addRecord(CoreId.CORE_ID_ARP, offset, ARP_HEADER_LEN);
			break;

		default:
			dissectExtensionType(buf, offset, CoreId.CORE_ID_ETHER, type);
			break;
		}
	}

	protected abstract void dissectExtensionType(ByteBuffer buf, int offset, int id, int nextHeader);

	protected abstract void dissectIpx(int offset);

	/**
	 * Dissect L 2.
	 *
	 * @param dlt    the dlt
	 * @param buf    the buf
	 * @param offset the offset
	 * @return the int
	 */
	protected final int dissectL2(int dlt, ByteBuffer buf, int offset) {
		int l2Type = L2FrameType.L2_FRAME_TYPE_OTHER;

		switch (dlt) { // L2 Datalink Type
		case L2FrameType.L2_FRAME_TYPE_ETHER:
			if (hasRemaining(offset, ETHER_HEADER_LEN)) {
				l2Type = L2FrameType.L2_FRAME_TYPE_ETHER;
				dissectEthernet(offset);
			}
			break;

		case L2FrameType.L2_FRAME_TYPE_NOVELL_RAW:
			if (hasRemaining(offset, ETHER_HEADER_LEN)) {
				l2Type = L2FrameType.L2_FRAME_TYPE_ETHER;
				int first2bytes = buf.getShort(ETHER_HEADER_LEN);

				/*
				 * In raw mode, ETH_IPX_SPX follows immediately 802.3 header instead of LLC/SNAP but 1st
				 * two bytes must be 0xFFFF
				 */
				if (first2bytes == IPX_FIELD_VALUE_CHECKSUM)
					dissectIpx(ETHER_HEADER_LEN); // Sets L3Type to ETH_IPX_SPX...
			}
			break;

		}

		return l2Type;
	}

	/**
	 * Dissect packet.
	 *
	 * @param buffer    the buffer
	 * @param timestamp the timestamp
	 * @param caplen    the caplen
	 * @param wirelen   the wirelen
	 * @return the int
	 * @see com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissector#dissectPacket(java.nio.ByteBuffer,
	 *      long, int, int)
	 */
	@Override
	public int dissectPacket(ByteBuffer buffer, long timestamp, int caplen, int wirelen) {
		this.buf = buffer;
		this.timestamp = timestamp;
		this.captureLength = caplen;
		this.wireLength = wirelen;

		this.l2Type = dissectL2(dltType, buf, 0);

		if (!isDissectionSuccess())
			return 0;

		/*
		 * Advance the position manually, since we only reference the buffer data in
		 * absolute mode.
		 */
		buffer.position(buffer.position() + captureLength);

		return captureLength;
	}

	protected boolean isDissectionSuccess() {
		return true;
	}

	/**
	 * Checks for remaining.
	 *
	 * @param offset the offset
	 * @return true, if successful
	 */
	protected final boolean hasRemaining(int offset) {
		return offset <= captureLength;
	}

	/**
	 * Checks for remaining.
	 *
	 * @param offset the offset
	 * @param length the length
	 * @return true, if successful
	 */
	protected final boolean hasRemaining(int offset, int length) {
		return (offset + length) <= captureLength;
	}

	/**
	 * Checks if is native.
	 *
	 * @return true, if is native
	 * @see com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissector#isNative()
	 */
	@Override
	public boolean isNative() {
		return false;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissector#reset()
	 */
	@Override
	public void reset() {
		timestamp = captureLength = wireLength = 0;

		l2Type = 0;

		vlanCount = mplsCount = 0;
	}

	/**
	 * Sets the datalink type.
	 *
	 * @param l2Type the l 2 type
	 * @return the packet dissector
	 * @throws ProtocolException the protocol exception
	 * @see com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissector#setDatalinkType(com.slytechs.jnet.protocol.api.core.L2FrameType)
	 */
	@Override
	public final PacketDissector setDatalinkType(L2FrameType l2Type) throws ProtocolException {
		this.dltType = l2Type.getL2FrameTypeAsInt();

		return this;
	}

}