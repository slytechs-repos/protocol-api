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
package com.slytechs.jnet.protocol.api.descriptor;

import static com.slytechs.jnet.protocol.api.descriptor.Type2DescriptorLayout.*;
import static com.slytechs.jnet.protocol.tcpip.constants.CoreConstants.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.function.IntConsumer;

import com.slytechs.jnet.jnetruntime.util.Bits;
import com.slytechs.jnet.jnetruntime.util.Enums;
import com.slytechs.jnet.protocol.api.common.HeaderOptionInfo;
import com.slytechs.jnet.protocol.api.pack.Pack;
import com.slytechs.jnet.protocol.api.pack.PackId;
import com.slytechs.jnet.protocol.tcpip.constants.CoreConstants;
import com.slytechs.jnet.protocol.tcpip.constants.CoreId;
import com.slytechs.jnet.protocol.tcpip.constants.Icmp6IdNsOptions;
import com.slytechs.jnet.protocol.tcpip.constants.Icmp6Mlr2RecordType;
import com.slytechs.jnet.protocol.tcpip.constants.Ip4IdOptions;
import com.slytechs.jnet.protocol.tcpip.constants.Ip6ExtType;
import com.slytechs.jnet.protocol.tcpip.constants.Ip6IdOption;
import com.slytechs.jnet.protocol.tcpip.constants.L2FrameType;
import com.slytechs.jnet.protocol.tcpip.constants.PacketDescriptorType;
import com.slytechs.jnet.protocol.tcpip.constants.TcpOptionId;
import com.slytechs.jnet.protocol.tcpip.network.Icmp6NeighborAdvertisement;
import com.slytechs.jnet.protocol.tcpip.network.Icmp6NeighborSolicitation;

/**
 * Descriptor type 2 java based packet dissector.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
class Type2DissectorJavaImpl extends PacketL3DissectorJava implements PacketDissectorExtension {

	/** The Constant RECORD_START. */
	private static final int RECORD_START = CoreConstants.DESC_TYPE2_BYTE_SIZE_MIN;

	/** The Constant WORD0_1. */
	private static final int WORD0_1 = 0;

	/** The Constant WORD2. */
	private static final int WORD2 = 8;

	/** The Constant WORD3. */
	private static final int WORD3 = 12;

	/** The Constant WORD5. */
	private static final int WORD5 = 20;

	/** The extensions. */
	private final PacketDissectorExtension extensions;

	/** The record extensions. */
	private boolean recordExtensions = true;

	/** The ip 4 disable bitmask. */
	private long ip4DisableBitmask = 0;

	/** The ip 6 disable bitmask. */
	private long ip6DisableBitmask = 0;

	/** The tcp disable bitmask. */
	private long tcpDisableBitmask = 0;

	/** The default bitmask. */
	private long defaultBitmask = Bits.BITS_00;

	/** The rx port. */
	private int rxPort;

	/** The tx port. */
	private int txPort;

	/** The tx now. */
	private int txNow;

	/** The tx ignore. */
	private int txIgnore;

	/** The tx crc override. */
	private int txCrcOverride;

	/** The tx set clock. */
	private int txSetClock;

	/** The hash type. */
	private int hashType;

	/** The record count. */
	private int recordCount;

	/** The hash. */
	private int hash;

	/** The bitmask. */
	private long bitmask;

	/** The record. */
	private final long[] record = new long[DESC_TYPE2_RECORD_MAX_COUNT];

	/**
	 * Instantiates a new java dissector type 2.
	 */
	Type2DissectorJavaImpl() {
		reset();

		this.extensions = Pack.wrapAllExtensions(PacketDescriptorType.TYPE2, this);
		this.l2Type = L2FrameType.L2_FRAME_TYPE_ETHER;
	}

	/**
	 * Adds the record.
	 *
	 * @param id     the id
	 * @param offset the offset
	 * @param length the length
	 * @return true, if successful
	 */
	@Override
	protected boolean addRecord(int id, int offset, int length) {
		if ((recordCount == DESC_TYPE2_RECORD_MAX_COUNT) || ((offset + length) > captureLength))
			return false;

		record[recordCount++] = PackId.encodeRecord(id, offset, length);
		bitmask = PackId.bitmaskSet(bitmask, id);

		return true;
	}

	/**
	 * Calculate gre header length.
	 *
	 * @param flags the flags
	 * @return the int
	 */
	private int calculateGreHeaderLength(short flags) {
		int size = 4;

		if ((flags & GRE_BITMASK_CHKSUM_FLAG) != 0)
			size += 4;

		if ((flags & GRE_BITMASK_KEY_FLAG) != 0)
			size += 4;

		if ((flags & GRE_BITMASK_SEQ_FLAG) != 0)
			size += 4;

		return size;
	}

	/**
	 * Calculate ip 6 options length and next hop.
	 *
	 * @param offset     the offset
	 * @param nextHeader the next header
	 * @return the int
	 */
	private int calculateIp6OptionsLengthAndNextHop(int offset, int nextHeader) {
		int len = 0;
		offset += 40;

		// Calculate IPv6 header size including options
		LOOP: while (hasRemaining(offset + 2)) {

			// IPv6 options
			switch (nextHeader) {
			case IP_TYPE_IPv6_FRAGMENT_HEADER:
				nextHeader = buf.get(offset + len + 0);
				len += 8;
				break;

			case IP_TYPE_IPv6_HOP_BY_HOP:
			case IP_TYPE_IPv6_DESTINATION_OPTIONS:
			case IP_TYPE_IPv6_ROUTING_HEADER:
			case IP_TYPE_IPv6_ENCAPSULATING_SECURITY_PAYLOAD:
			case IP_TYPE_IPv6_AUTHENTICATION_HEADER: // Authentication header
			case IP_TYPE_IPv6_MOBILITY_HEADER: // Mobility header
			case IP_TYPE_IPv6_HOST_IDENTITY_PROTOCOL: // Host identity
			case IP_TYPE_IPv6_SHIM6_PROTOCOL: // Shim6 protocol
				nextHeader = buf.get(offset + len + 0);
				len += (buf.get(offset + len + 1) << 3); // (in units of 8 bytes)
				break;

			default:
				break LOOP;
			}

		} // End LOOP:

		return (nextHeader << 16) | len;
	}

	/**
	 * Check bitmask.
	 *
	 * @param mask the mask
	 * @param id   the id
	 * @return true, if successful
	 */
	private boolean checkBitmask(long mask, int id) {
		int index = PackId.decodeRecordOrdinal(id);

		return (mask & (1L << index)) != 0;
	}

	/**
	 * Descriptor length.
	 *
	 * @return the int
	 */
	private int descriptorLength() {
		return CoreConstants.DESC_TYPE2_BYTE_SIZE_MAX;
	}

	/**
	 * Destroy dissector.
	 *
	 * @see com.slytechs.jnet.protocol.api.descriptor.PacketL3DissectorJava#destroyDissector()
	 */
	@Override
	protected void destroyDissector() {
		// Nothing to destroy with type1
	}

	/**
	 * Disable bitmask recording.
	 *
	 * @return the java dissector type 2
	 */
	public Type2DissectorJavaImpl disableBitmaskRecording() {
		// Turning on all bits, effectively disables bitmask checks
		bitmask = defaultBitmask = Bits.BITS_32;

		return this;
	}

	/**
	 * Disable extension recording for.
	 *
	 * @param protocolId   the protocol id
	 * @param extentionIds the extention ids
	 * @return the java dissector type 2
	 */
	public Type2DissectorJavaImpl disableExtensionRecordingFor(
			CoreId protocolId, HeaderOptionInfo... extentionIds) {

		return disableExtensionRecordingInCoreProtocol(protocolId.id(),
				Arrays.stream(extentionIds)
						.mapToInt(HeaderOptionInfo::id)
						.toArray());
	}

	/**
	 * Disable extension recording for all.
	 *
	 * @return the java dissector type 2
	 */
	public Type2DissectorJavaImpl disableExtensionRecordingForAll() {
		this.recordExtensions = false;

		return this;
	}

	/**
	 * Disable extension recording in core protocol.
	 *
	 * @param protocolId   the protocol id
	 * @param extensionIds the extension ids
	 * @return the java dissector type 2
	 */
	public Type2DissectorJavaImpl disableExtensionRecordingInCoreProtocol(int protocolId, int... extensionIds) {

		if (extensionIds.length > 0) {
			IntConsumer bitmaskSet = switch (protocolId) {
			case CoreId.CORE_ID_IPv4 -> id -> ip4DisableBitmask = PackId.bitmaskSet(ip4DisableBitmask, id);
			case CoreId.CORE_ID_IPv6 -> id -> ip6DisableBitmask = PackId.bitmaskSet(ip6DisableBitmask, id);
			case CoreId.CORE_ID_TCP -> id -> tcpDisableBitmask = PackId.bitmaskSet(tcpDisableBitmask, id);
			default -> id -> {};
			};

			for (int extId : extensionIds)
				bitmaskSet.accept(extId);

		} else {
			/* If no specifics extensions specified, disable them all */
			switch (protocolId) {
			case CoreId.CORE_ID_IPv4 -> ip4DisableBitmask = Bits.BITS_32;
			case CoreId.CORE_ID_IPv6 -> ip6DisableBitmask = Bits.BITS_32;
			case CoreId.CORE_ID_TCP -> tcpDisableBitmask = Bits.BITS_32;
			};
		}

		return this;
	}

	@Override
	protected void dissectExtensionPorts(ByteBuffer buf, int offset, int id, int src, int dst) {
		extensions.dissectPorts(buf, offset, id, src, dst);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.descriptor.PacketL3DissectorJava#dissectExtensionType(java.nio.ByteBuffer,
	 *      int, int, int)
	 */
	@Override
	protected void dissectExtensionType(ByteBuffer buf, int offset, int id, int nextHeader) {
		extensions.dissectType(buf, offset, id, nextHeader);
	}

	@Override
	protected void dissectGre(int offset) {
		if (!hasRemaining(offset, GRE_HEADER_LEN))
			return;

		short r0 = buf.getShort(offset + 0);
		int len = calculateGreHeaderLength(r0);

		addRecord(CoreId.CORE_ID_GRE, offset, len);
	}

	@Override
	protected void dissectIcmp4(int offset) {
		if (!hasRemaining(offset, ICMPv4_HEADER_LEN))
			return;

		int code = Byte.toUnsignedInt(buf.get(offset + ICMPv4_FIELD_CODE));
		int type = Byte.toUnsignedInt(buf.get(offset + ICMPv4_FIELD_TYPE));

		switch (type) {
		case ICMPv4_TYPE_ECHO_REQUEST -> {
//			addRecord(CoreId.CORE_ID_ICMPv4, offset, ICMPv4_ECHO_HEADER_LEN);
			addRecord(CoreId.CORE_ID_ICMPv4_ECHO_REQUEST, offset, ICMPv4_ECHO_HEADER_LEN);
		}
		case ICMPv4_TYPE_ECHO_REPLY -> {
//			addRecord(CoreId.CORE_ID_ICMPv4, offset, ICMPv4_ECHO_HEADER_LEN);
			addRecord(CoreId.CORE_ID_ICMPv4_ECHO_REPLY, offset, ICMPv4_ECHO_HEADER_LEN);
		}
		default -> addRecord(CoreId.CORE_ID_ICMPv4, offset, ICMPv4_HEADER_LEN);
		};

	}

	/**
	 * Dissect icmp 6.
	 *
	 * @param offset the offset
	 */
	@Override
	protected void dissectIcmp6(int offset) {
		if (!hasRemaining(offset, ICMPv6_HEADER_LEN))
			return;

		int type = Byte.toUnsignedInt(buf.get(offset)); // type field
		int len = 0;

		switch (type) {
		case ICMP_TYPE_UNREACHABLE:
		case ICMPv6_TYPE_TIME_EXEEDED:
		case ICMPv6_TYPE_PARAMETER_PROBLEM:
			len = 8;
			if (!addRecord(CoreId.CORE_ID_ICMPv6, offset, len))
				return;
			offset += len;
			dissectIp(offset);
			return;

		case ICMPv6_TYPE_ECHO_REQUEST:
			len = 8;
			addRecord(CoreId.CORE_ID_ICMPv6_ECHO_REQUEST, offset, len);
			return;

		case ICMPv6_TYPE_ECHO_REPLY:
			len = 8;
			addRecord(CoreId.CORE_ID_ICMPv6_ECHO_REPLY, offset, len);
			return;

		case ICMPv6_TYPE_ROUTER_SOLICITATION:
			len = 8;
			addRecord(CoreId.CORE_ID_ICMPv6, offset, len);
			break;

		case ICMPv6_TYPE_ROUTER_ADVERTISEMENT:
			len = 16;
			addRecord(CoreId.CORE_ID_ICMPv6, offset, len);
			break;

		case ICMPv6_TYPE_NEIGHBOR_SOLICITATION:
			len = Icmp6NeighborSolicitation.HEADER_LEN;
			addRecord(CoreId.CORE_ID_ICMPv6_NEIGHBOR_SOLICITATION, offset, len);

			dissectIcmp6NsOptions(offset + len);
			break;

		case ICMPv6_TYPE_NEIGHBOR_ADVERTISEMENT:
			len = Icmp6NeighborAdvertisement.HEADER_LEN;
			addRecord(CoreId.CORE_ID_ICMPv6_NEIGHBOR_ADVERTISEMENT, offset, len);
			break;

		case ICMPv6_TYPE_REDIRECT:
			len = 40;
			addRecord(CoreId.CORE_ID_ICMPv6, offset, len);
			break;

		case ICMPv6_TYPE_MULTICAST_LISTENER_REPORTv2:
			dissectIcmp6MulticastListenerReportV2(offset);
			break;

		case ICMPv6_TYPE_PACKET_TOO_BIG:
		case ICMPv6_TYPE_MULTICAST_LISTENER_QUERY:
		case ICMPv6_TYPE_MULTICAST_LISTENER_REPORT:
		case ICMPv6_TYPE_MULTICAST_LISTENER_DONE:

		case ICMPv6_TYPE_ROUTER_RENUMBER:
		case ICMPv6_TYPE_NODE_INFO_QUERY:
		case ICMPv6_TYPE_NODE_INFO_RESPONSE:
		case ICMPv6_TYPE_INVERSE_NEIGHBOR_SOLICITATION:
		case ICMPv6_TYPE_INVERSE_NEIGHBOR_ADVERTISEMENT:
		case ICMPv6_TYPE_HOME_AGENT_REQUEST:
		case ICMPv6_TYPE_HOME_AGENT_REPLY:
		case ICMPv6_TYPE_MOBILE_PREFIX_SOLICITATION:
		case ICMPv6_TYPE_MOBILE_PREFIX_ADVERTISEMENT:
			len = 4;
			addRecord(CoreId.CORE_ID_ICMPv6, offset, len);
			return;
		}
	}

	private void dissectIcmp6NsOptions(int offset) {
		int type = (buf.get(offset + 0) & 0xFF);
		int len = (buf.get(offset + 1) & 0xFF) * 8;

		if (type == Icmp6IdNsOptions.ICMPv6_OPTION_TYPE_SOURCE_LINK_ADDRESS)
			addRecord(Icmp6IdNsOptions.ICMPv6_ID_OPT_SOURCE_LINK_ADDRESS, offset, len);
	}

	private void dissectIcmp6MulticastListenerReportV2(int offset) {
		final int noOfRecords = Short.toUnsignedInt(buf.getShort(offset + 6));
		final int mainRecordIndex = this.recordCount;
		final int start = offset;

		if (!addRecord(CoreId.CORE_ID_ICMPv6_MULTICAST_LISTENER_REPORTv2, offset, 0))
			return;

		offset += 8; // Start of 1st record
		int mainLen = 8;
		for (int i = 0; i < noOfRecords; i++) {
			int type = buf.get(offset + 0) & 0xff;
			var typeEnum = Enums.valueOf(type, Icmp6Mlr2RecordType.class);
			int auxLen = (buf.get(offset + 1) & 0xff) * 4;
			int numSrc = (buf.getShort(offset + 2) * 0xffff);
			int len = auxLen + numSrc * 16 + 16 + 4;

			mainLen += len;

			if (!addRecord(typeEnum.id(), offset, len))
				return;
		}

		// Now update the entire MLRv2 header with computed length
		updateRecord(mainRecordIndex,
				CoreId.CORE_ID_ICMPv6_MULTICAST_LISTENER_REPORTv2,
				start, mainLen);
	}

	/**
	 * Dissect ip 4 options.
	 *
	 * @param offset     the offset
	 * @param hlen       the hlen
	 * @param nextHeader the next header
	 */
	@Override
	protected void dissectIp4Options(int offset, int hlen, int nextHeader) {
		int l4Offset = offset + hlen;

		/* check if any options are present */
		if (hlen > IPv4_HEADER_LEN) {
			offset += IPv4_HEADER_LEN; // Align at start of Ip4 options

			while (offset < l4Offset) {
				int type = Byte.toUnsignedInt(buf.get(offset + 0)); // option type
				int len = buf.get(offset + 1); // option length

				int id = 0;
				switch (type) {
				case Ip4IdOptions.IPv4_OPTION_TYPE_EOOL:
				case Ip4IdOptions.IPv4_OPTION_TYPE_NOP:
					len = 1;

					// fall through for all options
				default:
					id = Ip4IdOptions.mapTypeToId(type);
				}

				if (recordExtensions && !checkBitmask(ip4DisableBitmask, id))
					addRecord(id, offset, len);
				offset += len;
			}
		}

	}

	protected void dissectIp6OptHopByHop(int offset, int totLen) {

		int end = offset + totLen;

		while (offset < end) {
			int type = Byte.toUnsignedInt(buf.get(offset));

			switch (type) {

			/* PAD1 is special case, only takes up 1 bytes total */
			case Ip6IdOption.IPv6_OPTION_PAD1: {
				if (!addRecord(Ip6IdOption.IPv6_ID_OPT_PAD1, offset, 1))
					break;

				offset += 1;
				break;
			}

			case Ip6IdOption.IPv6_OPTION_PADN:
			case Ip6IdOption.IPv6_OPTION_ROUTER_ALERT:
			case Ip6IdOption.IPv6_OPTION_RPL:
			case Ip6IdOption.IPv6_OPTION_QUICK_START:
			case Ip6IdOption.IPv6_OPTION_MTU:
			case Ip6IdOption.IPv6_OPTION_MPL:
			case Ip6IdOption.IPv6_OPTION_JUMBO_PAYLOAD:
			case Ip6IdOption.IPv6_OPTION_TUNNEL_ENCAPS_LIMIT:
			default: {
				int len = Byte.toUnsignedInt(buf.get(offset + 1)) + 2;
				int id = Ip6IdOption.mapTypeToId(type);
				if (!addRecord(id, offset, len))
					break;

				offset += len;
				break;
			}

			}
		}
	}

	/**
	 * Dissect ip 6 options.
	 *
	 * @param offset     the offset
	 * @param nextHeader the next header
	 */
	@Override
	protected void dissectIp6Options(int offset, int nextHeader) {
		final int ip6RecordIndex = recordCount;
		final int ip6OffsetStart = offset;

		if (!addRecord(CoreId.CORE_ID_IPv6, offset, IPv6_HEADER_LEN))
			return;

		int extLen = 0;
		offset += IPv6_HEADER_LEN;

		// Calculate IPv6 header size including options
		while (hasRemaining(offset + 2)) {
			if (nextHeader == Ip6ExtType.IPv6_EXT_TYPE_NO_NEXT)
				return; // No upper layer header

			int id = Ip6ExtType.mapTypeToId(nextHeader);
			if (id == -1)
				break; // Not an extension header

			int len = (Byte.toUnsignedInt(buf.get(offset + extLen + 1)) << 3) + 8; // (in units of 8 bytes)

			if (recordExtensions && !checkBitmask(ip4DisableBitmask, id))
				addRecord(id, offset, len);

			if (nextHeader == Ip6ExtType.IPv6_EXT_TYPE_HOP_BY_HOP_OPTIONS)
				dissectIp6OptHopByHop(offset + 2, len - 2);

			nextHeader = Byte.toUnsignedInt(buf.get(offset));

			extLen += len;
			offset += len;

		} // End LOOP:

		if ((extLen > 0) && !updateRecord(ip6RecordIndex, CoreId.CORE_ID_IPv6,
				ip6OffsetStart, IPv6_HEADER_LEN + extLen))
			return;

		dissectIpType(offset, nextHeader);
	}

	/**
	 * Dissect ipx.
	 *
	 * @param offset the offset
	 */
	@Override
	protected void dissectIpx(int offset) {
		if (!hasRemaining(offset, IPX_HEADER_LEN))
			return;

		addRecord(CoreId.CORE_ID_IPX, offset, IPX_HEADER_LEN);
	}

	@Override
	protected void dissectSctp(int offset) {
		if (!hasRemaining(offset, SCTP_HEADER_LEN))
			return;

		addRecord(CoreId.CORE_ID_SCTP, offset, SCTP_HEADER_LEN);
	}

	/**
	 * Dissect tcp options.
	 *
	 * @param offset         the offset
	 * @param tcpHeaderLenth the tcp header lenth
	 */
	@Override
	protected void dissectTcpOptions(int offset, int tcpHeaderLenth) {

		int limit = offset + tcpHeaderLenth;

		// Advance to option start
		offset += TCP_HEADER_LEN;

		while (offset < limit) {
			int kind = Byte.toUnsignedInt(buf.get(offset + TCP_OPTION_FIELD_KIND));

			switch (kind) {

			case TCP_OPTION_KIND_EOL:
			case TCP_OPTION_KIND_NOP: {
				int len = 1;
				int id = TcpOptionId.mapKindToId(kind);

				addRecord(id, offset, len);
				offset += 1;
				break;
			}

			case TCP_OPTION_KIND_MSS:
			case TCP_OPTION_KIND_WIN_SCALE:
			case TCP_OPTION_KIND_SACK_PERMITTED:
			case TCP_OPTION_KIND_SACK:
			case TCP_OPTION_KIND_TIMESTAMP:
			case TCP_OPTION_KIND_FASTOPEN: {
				int len = Byte.toUnsignedInt(buf.get(offset + TCP_OPTION_FIELD_LENGTH));
				int id = TcpOptionId.mapKindToId(kind);

				addRecord(id, offset, len);
				offset += len;
				break;
			}

			default: {
				int len = Byte.toUnsignedInt(buf.get(offset + TCP_OPTION_FIELD_LENGTH));
				offset += len;
			}

			}
		}
	}

	@Override
	protected void dissectUdp(int offset) {
		if (!hasRemaining(offset, UDP_HEADER_LEN))
			return;

		addRecord(CoreId.CORE_ID_UDP, offset, UDP_HEADER_LEN);

		int src = Short.toUnsignedInt(buf.getShort(offset + TCP_FIELD_SRC));
		int dst = Short.toUnsignedInt(buf.getShort(offset + TCP_FIELD_DST));

		offset += UDP_HEADER_LEN;

		extensions.dissectPorts(buf, offset, CoreId.CORE_ID_UDP, src, dst);
	}

	/**
	 * Reset.
	 *
	 * @see com.slytechs.jnet.protocol.api.descriptor.PacketDissector#reset()
	 */
	@Override
	public void reset() {
		super.reset();

		hash = hashType = 0;
		rxPort = txPort = 0;
		txNow = txIgnore = txCrcOverride = txSetClock = 0;

		recordCount = 0;
		bitmask = defaultBitmask;
	}

	/**
	 * Sets the extensions.
	 *
	 * @param ext the new extensions
	 * @see com.slytechs.jnet.protocol.api.descriptor.PacketDissectorExtension#setExtensions(com.slytechs.jnet.protocol.api.descriptor.PacketDissectorExtension)
	 */
	@Override
	public void setExtensions(PacketDissectorExtension ext) {
	}

	/**
	 * Sets the hash.
	 *
	 * @param hash     the hash
	 * @param hashType the hash type
	 * @return the java dissector type 2
	 */
	public Type2DissectorJavaImpl setHash(int hash, int hashType) {
		this.hashType = hashType;
		this.hash = hash;

		return this;
	}

	/**
	 * Sets the recorder.
	 *
	 * @param recorder the new recorder
	 * @see com.slytechs.jnet.protocol.api.descriptor.PacketDissectorExtension#setRecorder(com.slytechs.jnet.protocol.api.descriptor.PacketDissector.RecordRecorder)
	 */
	@Override
	public void setRecorder(RecordRecorder recorder) {
		// We are the recorder see addRecord(...)
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JavaDissectorType2 [dltType=" + dltType + ", timestamp=" + timestamp + ", captureLength="
				+ captureLength + ", wireLength=" + wireLength + ", rxPort=" + rxPort + ", txPort=" + txPort
				+ ", txNow=" + txNow + ", txIgnore=" + txIgnore + ", txCrcOverride=" + txCrcOverride + ", txSetClock="
				+ txSetClock + ", l2Type=" + l2Type + ", hashType=" + hashType + ", recordCount=" + recordCount
				+ ", hash=" + hash + ", bitmask=" + bitmask + "]";
	}

	/**
	 * Update record.
	 *
	 * @param recordIndex the record index
	 * @param id          the id
	 * @param offset      the offset
	 * @param length      the length
	 * @return true, if successful
	 */
	private boolean updateRecord(int recordIndex, int id, int offset, int length) {
		if ((recordIndex >= recordCount) || ((offset + length) > captureLength))
			return false;

		record[recordIndex] = PackId.encodeRecord(id, offset, length);

		return true;
	}

	/**
	 * Write descriptor.
	 *
	 * @param desc the desc
	 * @return the int
	 * @see com.slytechs.jnet.protocol.api.descriptor.PacketDissector#writeDescriptor(java.nio.ByteBuffer)
	 */
	@Override
	public int writeDescriptor(ByteBuffer desc) {
		int len = writeDescriptorUsingLayout(desc);

		desc.position(desc.position() + len);

		return len;
	}

	/**
	 * Write descriptor fast path.
	 *
	 * @param desc the desc
	 * @return the int
	 */
	public final int writeDescriptorFastPath(ByteBuffer desc) {
		final boolean big = (desc.order() == ByteOrder.BIG_ENDIAN);

		// Struct/Layout class has the private encoders we utilize here
		int word2 = big
				? Type2DescriptorLayout
						.encodeWord2BE(captureLength, rxPort, txPort)
				: Type2DescriptorLayout
						.encodeWord2LE(captureLength, rxPort, txPort);

		int word3 = big
				? Type2DescriptorLayout
						.encodeWord3BE(wireLength, txNow, txIgnore, txCrcOverride, txSetClock, l2Type, 0, recordCount)
				: Type2DescriptorLayout
						.encodeWord3LE(wireLength, txNow, txIgnore, txCrcOverride, txSetClock, l2Type, 0, recordCount);

		// @formatter:off
		desc.putLong(WORD0_1, timestamp) // 07-00 Word0&1
				.putInt(WORD2, word2)    // 11-08 Word2
				.putInt(WORD3, word3)    // 15-12 Word3
				                         // 19-16 Word4 hash/color2 which is not set by the dissector
				.putLong(WORD5, bitmask); // 23-20 Word5 recorded protocol bitmask (1 bit per proto)
		// @formatter:on

		for (int i = 0, j = RECORD_START; i < recordCount; i++, j += DESC_TYPE2_RECORD_BYTE_SIZE)
			desc.putLong(j, record[i]); // 152-24 (up to 152 bytes eq. (32 * 4) + 24)

		return descriptorLength();
	}

	/**
	 * Write descriptor using layout.
	 *
	 * @param desc the desc
	 * @return the int
	 */
	public final int writeDescriptorUsingLayout(ByteBuffer desc) {
		TIMESTAMP.setLong(timestamp, desc);

		CAPLEN.setInt(captureLength, desc);
		RX_PORT.setInt(rxPort, desc);
		TX_PORT.setInt(txPort, desc);

		WIRELEN.setInt(wireLength, desc);
		TX_NOW.setInt(txNow, desc);
		TX_IGNORE.setInt(txIgnore, desc);
		TX_CRC_OVERRIDE.setInt(txCrcOverride, desc);
		TX_SET_CLOCK.setInt(txSetClock, desc);
		L2_TYPE.setInt(l2Type, desc);
		HASH_TYPE.setInt(hashType, desc);
		RECORD_COUNT.setInt(recordCount, desc);

		L3_IS_FRAG.setShort((short) (l3IsFrag ? 1 : 0), desc);
		L3_LAST_FRAG.setShort((short) (l3LastFrag ? 1 : 0), desc);

		HASH24.setInt(hash, desc);
		BITMASK.setLong(bitmask, desc);

		for (int i = 0, j = DESC_TYPE2_BYTE_SIZE_MIN; i < recordCount; i++, j += DESC_TYPE2_RECORD_BYTE_SIZE)
			desc.putLong(j, record[i]);

		return descriptorLength();
	}

}
