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
package com.slytechs.jnet.protocol.tcpip.ip;

import java.util.Iterator;
import java.util.Set;

import com.slytechs.jnet.platform.api.util.format.BitFormat;
import com.slytechs.jnet.protocol.api.common.HasOption;
import com.slytechs.jnet.protocol.api.common.HeaderNotFound;
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.tcpip.ip.impl.Ip4Struct;

/**
 * Internet Protocol Version 4 (IPv4).
 * <p>
 * Internet Protocol Version 4 (IPv4) is a widely used protocol in data
 * communication over various types of networks, particularly in packet-switched
 * layer networks like Ethernet. It provides a logical connection between
 * network devices by assigning identification to each device. IPv4 uses a best
 * effort delivery model and is a connectionless protocol, meaning that neither
 * delivery nor proper sequencing or avoidance of duplicate delivery is
 * guaranteed.
 * </p>
 * <p>
 * IPv4 employs 32-bit addressing, which allows for 232 unique addresses. These
 * addresses are divided into five classes: A, B, C, D, and E. Classes A, B, and
 * C differ in the bit length used to address the network host. While Class D
 * addresses are reserved for military purposes, Class E addresses are set aside
 * for future use. IPv4 addresses are typically represented in dot-decimal
 * notation, where the address is divided into four octets and expressed in
 * decimal format, separated by periods. RFC 791 is the publication that defines
 * and specifies IPv4. There are many ways to configure IPv4 with various
 * devices, including both manual and automatic configurations, depending on the
 * network type.
 * </p>
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("/tcpip/ip4.yaml")
public final class Ip4
		extends Ip
		implements DiffServ, HasOption<Ip4Option>, Iterable<Ip4Option> {

	/** The Constant ID. */
	public static final int ID = CoreId.CORE_ID_IPv4;
	private static final String FLAGS_FORMAT = ".DM";
	private static final BitFormat FLAGS_FORMATTER = new BitFormat(FLAGS_FORMAT);

	/**
	 * Instantiates a new IPv4 header object.
	 */
	public Ip4() {
		super(ID);
	}

	public int diffServ() {
		return trafficClass();
	}

	@Meta(abbr = "crc", formatter = Meta.Formatter.HEX_LOWERCASE_0x)
	public int checksum() {
		return Ip4Struct.CHECKSUM.getUnsignedShort(buffer());
	}

	/**
	 * The Differentiated Services Code Point (DSCP) field.
	 * <p>
	 * The Differentiated Services Code Point (DSCP) field is a 6-bit field in the
	 * IPv4 header that is used to classify IP packets for Quality of Service (QoS)
	 * treatment. The DSCP field is part of the Differentiated Services (DiffServ)
	 * architecture, which is a way of classifying and managing IP traffic in a
	 * network.
	 * </p>
	 * <p>
	 * The DSCP field can be used to mark packets with different levels of
	 * importance, such as voice traffic, video traffic, or bulk data traffic.
	 * Routers can then use the DSCP markings to decide how to handle the packets,
	 * such as giving priority to voice traffic or dropping bulk data traffic during
	 * periods of congestion.
	 * </p>
	 * <p>
	 * The DSCP field is divided into two parts: the first 3 bits are used for IP
	 * Precedence, and the last 3 bits are used for Differentiated Services
	 * (DiffServ). IP Precedence is an older way of classifying IP traffic, and it
	 * is not widely used anymore. DiffServ is the newer way of classifying IP
	 * traffic, and it is used by most networks today.
	 * </p>
	 * <p>
	 * The DiffServ bits in the DSCP field can be used to mark packets with one of
	 * 64 different values. These values are called DSCP codepoints, and they are
	 * defined by the Internet Assigned Numbers Authority (IANA). The IANA has
	 * assigned specific meanings to some of the DSCP codepoints, such as Expedited
	 * Forwarding (EF) for voice traffic and Assured Forwarding (AF) for video
	 * traffic.
	 * </p>
	 * <p>
	 * Network operators can also define their own DSCP codepoints to meet the
	 * specific needs of their network. For example, a network operator might define
	 * a DSCP codepoint for bulk data traffic that is guaranteed to be delivered
	 * within a certain amount of time. *
	 * </p>
	 * 
	 * @return the int
	 */
	public int dsfield() {
		return Ip4Struct.DSFIELD.getInt(buffer());
	}

	/**
	 * Dsfield dscp.
	 *
	 * @return the int
	 */
	public int dsfieldDscp() {
		return Ip4Struct.DSFIELD_DSCP.getInt(buffer());
	}

	/**
	 * Dsfield dscp code.
	 *
	 * @return the int
	 */
	public int dsfieldDscpCode() {
		return Ip4Struct.DSFIELD_DSCP_CODE.getInt(buffer());
	}

	public boolean isFragmented() {
		return flags_MF() != 0 || fragOffset() > 0;
	}

	/**
	 * Dsfield dscp info.
	 *
	 * @return the string
	 */
	String dsfieldDscpInfo() {
		StringBuilder b = new StringBuilder();

		if (dsfieldDscp() == 0) {
			b.append("Default (")
					.append(dsfieldDscp())
					.append(")");

		} else {
			b.append(dsfieldDscp());
		}

		return b.toString();
	}

	/**
	 * Dsfield dscp select.
	 *
	 * @return the int
	 */
	int dsfieldDscpSelect() {
		return Ip4Struct.DSFIELD_DSCP_SELECT.getInt(buffer());
	}

	/**
	 * Dsfield ecn.
	 *
	 * @return the int
	 */
	int dsfieldEcn() {
		return Ip4Struct.DSFIELD_ECN.getInt(buffer());
	}

	/**
	 * Dsfield info.
	 *
	 * @return the string
	 */
	String dsfieldInfo() {
		StringBuilder b = new StringBuilder();

		if (dsfieldDscpSelect() == 0)
			b.append("DSCP: CS").append(dsfieldDscpCode());

		else if (dsfieldDscpSelect() == 1)
			b.append("DSCP: EF").append(dsfieldDscpCode());

		else if (dsfieldDscpSelect() == 2)
			b.append("DSCP: AF").append(dsfieldDscpCode());

		if (dsfieldEcn() == 0)
			b.append(", ENC: Not-ECT");

		return b.toString();
	}

	/**
	 * Dst.
	 *
	 * @return the byte[]
	 * @see com.slytechs.jnet.protocol.tcpip.ip.Ip#dst()
	 */
	@Meta
	@Override
	public byte[] dst() {
		return dst(new byte[IpAddress.IPv4_ADDRESS_SIZE], 0);
	}

	/**
	 * Dst.
	 *
	 * @param dst the dst
	 * @return the byte[]
	 */
	@Override
	public byte[] dst(byte[] dst) {
		return dst(dst, 0);
	}

	/**
	 * Dst.
	 *
	 * @param dst    the dst
	 * @param offset the offset
	 * @return the byte[]
	 */
	@Override
	public byte[] dst(byte[] dst, int offset) {
		buffer().get(IpConstants.IPv4_FIELD_DST, dst, offset, IpConstants.IPv4_FIELD_DST_LEN);

		return dst;
	}

	/**
	 * Dst address.
	 *
	 * @return the ip 4 address
	 * @see com.slytechs.jnet.protocol.tcpip.ip.Ip#dstAsAddress()
	 */
	@Override
	public Ip4Address dstAsAddress() {
		return new Ip4Address(dst());
	}

	/**
	 * Dst as int.
	 *
	 * @return the int
	 */
	public int dstGetAsInt() {
		return Ip4Struct.DST.getInt(buffer());
	}

	/**
	 * Flags.
	 *
	 * @return the int
	 */
	@Meta(abbr = "flg")
	public int flags() {
		return Ip4Struct.FLAGS.getInt(buffer());
	}

	/**
	 * Flags df.
	 *
	 * @return the int
	 */
	public int flags_DF() {
		return Ip4Struct.FLAGS_DF.getInt(buffer());
	}

	/**
	 * Flags mf.
	 *
	 * @return the int
	 */
	public int flags_MF() {
		return Ip4Struct.FLAGS_MF.getInt(buffer());
	}

	/**
	 * Flags reserved.
	 *
	 * @return the int
	 */
	public int flags_Reserved() {
		return Ip4Struct.FLAGS_RB.getInt(buffer());
	}

	@Meta(value = MetaType.ATTRIBUTE)
	public String flagsAsString() {
		return flagsEnum().toString();
	}

	/**
	 * Flags set.
	 *
	 * @return the sets the
	 */
	@Meta(value = MetaType.ATTRIBUTE, abbr = "flagsEnum")
	public Set<Ip4Flag> flagsEnum() {
		return Ip4Flag.valueOfInt03(flags());
	}

	@Meta(value = MetaType.ATTRIBUTE)
	public String flagsFormatted() {
		return FLAGS_FORMATTER.format(flags());
	}

	/**
	 * Flags info.
	 *
	 * @return the string
	 */
	@Meta(value = MetaType.ATTRIBUTE)
	public String flagsInfo() {
		if (flags_DF() != 0)
			return "Don't fragment";

		if (flags_MF() != 0)
			return "More fragments";

		return "No flags";
	}

	/**
	 * Frag offset.
	 *
	 * @return the int
	 */
	@Meta(abbr = "off")
	public int fragOffset() {
		return Ip4Struct.FRAG_OFFSET.getInt(buffer());
	}

	@Meta(value = MetaType.ATTRIBUTE, abbr = "offBytes")
	public int fragOffsetBytes() {
		return fragOffset() << 3;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HasOption#getOption(com.slytechs.jnet.protocol.Header,
	 *      int)
	 */
	@Override
	public <E extends Ip4Option> E getOption(E extension, int depth) throws HeaderNotFound {
		return super.getOptionHeader(extension, depth);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HasOption#hasOption(int, int)
	 */
	@Override
	public boolean hasOption(int extensionId, int depth) {
		return super.hasOptionHeader(extensionId, depth);
	}

	/**
	 * Hdr len.
	 *
	 * @return the int
	 */
	@Meta(abbr = "ihl")
	public int hdrLen() {
		return Ip4Struct.HDR_LEN.getInt(buffer());
	}

	/**
	 * Hdr len bytes.
	 *
	 * @return the int
	 */
	@Meta(value = MetaType.ATTRIBUTE, abbr = "ihlBytes")
	public int hdrLenBytes() {
		return hdrLen() * 4;
	}

	/**
	 * Identification.
	 *
	 * @return the int
	 */
	@Meta(abbr = "ident", formatter = Meta.Formatter.HEX_LOWERCASE_0x)
	public int identification() {
		return Ip4Struct.ID.getUnsignedShort(buffer());
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Ip4Option> iterator() {

		final int optLen = headerLength();

		return new Iterator<Ip4Option>() {
			int off = IpConstants.IPv4_HEADER_LEN;

			@Override
			public boolean hasNext() {
				return off < optLen;
			}

			@Override
			public Ip4Option next() {
				throw new UnsupportedOperationException("not implemented yet");
			}

		};
	}

	/**
	 * Payload length.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.protocol.tcpip.ip.Ip#payloadLength()
	 */
	@Override
	public int payloadLength() {
		return totalLength() - hdrLenBytes();
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HasOption#peekOption(com.slytechs.jnet.protocol.Header,
	 *      int)
	 */
	@Override
	public <E extends Ip4Option> E peekOption(E extension, int depth) {
		return super.peekOptionHeader(extension, depth);
	}

	/**
	 * Protocol.
	 *
	 * @return the int
	 */
	@Meta(abbr = "proto")
	@Override
	public int protocol() {
		return Ip4Struct.PROTO.getInt(buffer());
	}

	/**
	 * Protocol info.
	 *
	 * @return the string
	 */
	public String protocolInfo() {
		return IpType.valueOfIpType(protocol()).name();
	}

	/**
	 * Src.
	 *
	 * @return the byte[]
	 * @see com.slytechs.jnet.protocol.tcpip.ip.Ip#src()
	 */
	@Override
	@Meta
	public byte[] src() {
		return src(new byte[IpAddress.IPv4_ADDRESS_SIZE], 0);
	}

	/**
	 * Src.
	 *
	 * @param dst the dst
	 * @return the byte[]
	 */
	@Override
	public byte[] src(byte[] dst) {
		return src(dst, 0);
	}

	/**
	 * Src.
	 *
	 * @param dst    the dst
	 * @param offset the offset
	 * @return the byte[]
	 */
	@Override
	public byte[] src(byte[] dst, int offset) {
		byte[] debug = new byte[20];
		buffer().get(0, debug);
		buffer().get(IpConstants.IPv4_FIELD_SRC, dst, offset, IpConstants.IPv4_FIELD_SRC_LEN);

		return dst;
	}

	/**
	 * Src get as address.
	 *
	 * @return the ip 4 address
	 * @see com.slytechs.jnet.protocol.tcpip.ip.Ip#srcAsAddress()
	 */
	@Override
	public Ip4Address srcAsAddress() {
		return new Ip4Address(src());
	}

	/**
	 * Src get as int.
	 *
	 * @return the int
	 */
	public int srcAsInt() {
		return Ip4Struct.SRC.getInt(buffer());
	}

	/**
	 * Total length.
	 *
	 * @return the int
	 */
	@Meta(abbr = "len")
	public int totalLength() {
		return Ip4Struct.TOTAL_LENGTH.getUnsignedShort(buffer());
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpip.ip.DiffServ#trafficClass()
	 */
	@Meta(abbr = "tos")
	@Override
	public int trafficClass() {
		return buffer().get(1) & DiffServ.TRAFFIC_CLASS;
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpip.ip.DiffServ#trafficDscp()
	 */
	@Meta(value = MetaType.ATTRIBUTE, abbr = "dscp")
	@Override
	public int trafficDscp() {
		return (trafficClass() & DiffServ.DIFF_SERV_DSCP) >> 2;
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpip.ip.DiffServ#trafficDscpAbbr()
	 */
	@Meta(value = MetaType.ATTRIBUTE, abbr = "dscpAbbr")
	@Override
	public String trafficDscpAbbr() {
		return DiffServEcn.resolveAbbr(trafficDscp());
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpip.ip.DiffServ#trafficDscpDescription()
	 */
	@Meta(value = MetaType.ATTRIBUTE, abbr = "dscpDesc")
	@Override
	public String trafficDscpDescription() {
		return DiffServDscp.resolveDescription(trafficDscp());
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpip.ip.DiffServ#trafficDscpName()
	 */
	@Meta(value = MetaType.ATTRIBUTE, abbr = "dscpName")
	@Override
	public String trafficDscpName() {
		return DiffServDscp.resolveName(trafficDscp());
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpip.ip.DiffServ#trafficEcn()
	 */
	@Meta(value = MetaType.ATTRIBUTE, abbr = "ecn")
	@Override
	public int trafficEcn() {
		return (trafficClass() & DiffServ.DIFF_SERV_ECN);
	}

	/**
	 * Ttl.
	 *
	 * @return the int
	 */
	@Meta
	public int ttl() {
		return Ip4Struct.TTL.getUnsignedByte(buffer());
	}

	/**
	 * Version.
	 *
	 * @return the int
	 */
	@Meta(abbr = "ver")
	@Override
	public int version() {
		return Ip4Struct.VERSION.getUnsignedByte(buffer());
	}

}
