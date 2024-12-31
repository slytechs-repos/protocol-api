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
package com.slytechs.jnet.protocol.tcpip.dissector;

import static com.slytechs.jnet.protocol.tcpip.ip.IpConstants.*;
import static com.slytechs.jnet.protocol.tcpip.ipx.IpxConstants.*;
import static com.slytechs.jnet.protocol.tcpip.tcp.TcpConstants.*;

import java.nio.ByteBuffer;

import com.slytechs.jnet.platform.api.util.Bits;
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.core.L3FrameType;
import com.slytechs.jnet.protocol.api.core.L4FrameType;

/**
 * The Class JavaDissector.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public abstract class PacketL3DissectorJava extends PacketL2DissectorJava {

	protected int l3Type;
	protected int l3Offset;
	protected int l3Size;
	protected boolean l3IsFrag;
	protected boolean l3LastFrag;
	protected int l4Type;
	protected int l4Size;

	/**
	 * Instantiates a new java dissector.
	 */
	protected PacketL3DissectorJava() {
		super();
	}

	protected final int calcIpVersion(byte versionField) {
		return (versionField >> 4) & Bits.BITS_04;
	}

	protected abstract void dissectExtensionPorts(ByteBuffer buf, int offset, int id, int src, int dst);

	protected abstract void dissectGre(int offset);

	protected abstract void dissectIcmp4(int offset);

	protected abstract void dissectIcmp6(int offset);

	@Override
	protected void dissectIpx(int offset) {
		if (!hasRemaining(offset, IPX_HEADER_LEN))
			return;

		this.l3Type = L3FrameType.L3_FRAME_TYPE_IPX;
		this.l3Offset = offset;
		this.l3Size = IPX_HEADER_LEN;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissector#reset()
	 */
	@Override
	public void reset() {
		super.reset();

		l3Type = l3Offset = l3Size = 0;
		l3IsFrag = l3LastFrag = false;

		l4Type = l4Size = 0;
	}

	/**
	 * Dissect ip.
	 *
	 * @param l3Offset the offset
	 */
	@Override
	protected final void dissectIp(int offset) {
		if (!hasRemaining(offset, IPv4_HEADER_LEN))
			return;

		this.l3Offset = offset;

		int r0 = Byte.toUnsignedInt(buf.get(l3Offset + IPv4_FIELD_VER)); // 07:00 IP header len & version
		int ver = calcIpVersion((byte) r0); // Common to IPv4 and IPv6

		int nextHeader = NO_NEXT_HEADER;

		if (ver == 4) {
			this.l3Type = L3FrameType.L3_FRAME_TYPE_IPv4;
			this.l3Size = ((r0 >> 0) & Bits.BITS_04);
			int len = l3Size << 2;
			nextHeader = buf.get(l3Offset + IPv4_FIELD_PROTOCOL);

			if (!addRecord(CoreId.CORE_ID_IPv4, l3Offset, len))
				return;

			int sword3 = buf.getShort(l3Offset + IPv4_FIELD_FLAGS);
			boolean mf = (sword3 & IPv4_FLAG16_MF) > 0;
			int fragOff = (sword3 & IPv4_MASK16_FRAGOFF);

			this.l3IsFrag = mf || (fragOff > 0);
			this.l3LastFrag = !mf && (fragOff > 0);

			dissectIp4Options(l3Offset, len, nextHeader);

			if (fragOff == 0)
				dissectIpType(offset + len, nextHeader);

		} else if (hasRemaining(l3Offset, IPv6_HEADER_LEN)) {
			this.l3Type = L3FrameType.L3_FRAME_TYPE_IPv6;
			this.l3Size = IPv6_HEADER_LEN >> 2;

			nextHeader = Byte.toUnsignedInt(buf.get(l3Offset + IPv6_FIELD_NEXT_HOP));

			dissectIp6Options(l3Offset, nextHeader);
		}
	}

	protected abstract void dissectIp4Options(int offset, int hlen, int nextHeader);

	protected abstract void dissectIp6Options(int offset, int nextHeader);

	/**
	 * Dissect ip type.
	 *
	 * @param offset     the offset
	 * @param nextHeader the next header
	 */
	protected void dissectIpType(int offset, int nextHeader) {

		EXIT: while (nextHeader != NO_NEXT_HEADER) {
			switch (nextHeader) {
			case IP_TYPE_ICMPv4:
				dissectIcmp4(offset);

				break EXIT;

			case IP_TYPE_IPv4_IN_IP:
				dissectIp(offset);

				break EXIT;

			case IP_TYPE_TCP:
				dissectTcp(offset);

				break EXIT;

			case IP_TYPE_UDP:
				dissectUdp(offset);

				break EXIT;

			case IP_TYPE_IPv6_IN_IP: // IPv6
				dissectIp(offset);

				break EXIT;

			case IP_TYPE_GRE:
				dissectGre(offset);

				break EXIT;

			case IP_TYPE_SCTP:
				dissectSctp(offset);

				break EXIT;

			case IP_TYPE_ICMPv6:
				dissectIcmp6(offset);

				break EXIT;

			case IP_TYPE_NO_NEXT:
				dissectSctp(offset);

				return;

			default:
				dissectExtensionType(buf, offset, CoreId.CORE_ID_IPv4, nextHeader);
				break EXIT;
			}
		}

	}

	protected abstract void dissectSctp(int offset);

	protected final void dissectTcp(int offset) {
		if (!hasRemaining(offset, TCP_HEADER_LEN))
			return;

		this.l4Type = L4FrameType.L4_FRAME_TYPE_TCP;

		int r0 = buf.get(offset + TCP_FIELD_IHL);
		this.l4Size = ((r0 >> 4) & Bits.BITS_04);
		int len = l4Size << 2;

		addRecord(CoreId.CORE_ID_TCP, offset, len);

		dissectTcpOptions(offset, len);

		int src = Short.toUnsignedInt(buf.getShort(offset + TCP_FIELD_SRC));
		int dst = Short.toUnsignedInt(buf.getShort(offset + TCP_FIELD_DST));

		offset += len;

		dissectExtensionPorts(buf, offset, CoreId.CORE_ID_TCP, src, dst);
	}

	protected abstract void dissectTcpOptions(int offset, int tcpHeaderLenth);

	protected abstract void dissectUdp(int offset);

}
