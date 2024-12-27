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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ip;

import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.tcpipREFACTOR.constants.CoreId;

/**
 * Internet Protocol Version 6 (IPv6).
 * <p>
 * An IPv6 packet is the smallest unit of message exchanged using Internet
 * Protocol version 6 (IPv6). It is comprised of control information for
 * addressing and routing, as well as a payload of user data. The control
 * information in IPv6 packets consists of a mandatory fixed header and optional
 * extension headers. The payload of an IPv6 packet is typically a datagram or
 * segment of the higher-level transport layer protocol, although it could also
 * be data for an internet layer (e.g., ICMPv6) or link layer (e.g., OSPF).
 * </p>
 * <p>
 * IPv6 packets are typically transmitted over the link layer (e.g., Ethernet or
 * Wi-Fi), which encapsulates each packet in a frame. Alternatively, packets may
 * be transported over a higher-layer tunneling protocol such as IPv4 when using
 * 6to4 or Teredo transition technologies.
 * </p>
 * <p>
 * Unlike IPv4, routers do not fragment IPv6 packets larger than the maximum
 * transmission unit (MTU), as this is the sole responsibility of the
 * originating node. IPv6 mandates a minimum MTU of 1,280 octets, but hosts are
 * "strongly recommended" to use Path MTU Discovery to take advantage of MTUs
 * greater than the minimum.
 * </p>
 * <p>
 * Since July 2017, the Internet Assigned Numbers Authority (IANA) has been
 * responsible for registering all IPv6 parameters used in IPv6 packet headers.
 * *
 * </p>
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("ip6-experimental-meta.json")
public final class Ip6 extends Ip implements DiffServ  {

	/** The Constant ID. */
	public static final int ID = CoreId.CORE_ID_IPv6;

	public static final int FLOW_LABEL_MASK = 0x0FFF_FFFF;

	public static final int TRAFFIC_CLASS_MASK = 0b0000_1111_1111_0000_0000_0000_0000_0000;

	public static final int TRAFFIC_DSCP_MASK = 0b0000_1111_1100_0000_0000_0000_0000_0000;

	public static final int TRAFFIC_ECN_MASK = 0b0000_0000_0011_0000_0000_0000_0000_0000;

	/**
	 * Instantiates a new ip 6.
	 */
	public Ip6() {
		super(ID);
	}

	/**
	 * Dst.
	 *
	 * @return the byte[]
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.Ip#dst()
	 */
	@Override
	@Meta
	public byte[] dst() {
		return Ip6Layout.DST_BYTES.getByteArray(buffer());
	}

	/**
	 * Dst address.
	 *
	 * @return the ip address
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.Ip#dstAsAddress()
	 */
	@Override
	public IpAddress dstAsAddress() {
		return new Ip6Address(dst());
	}

	/**
	 * Dst as int.
	 *
	 * @param index the index
	 * @return the int
	 */
	public int dstAsInt(int index) {
		return Ip6Layout.DST_AS_INT.getInt(buffer(), index);
	}

	/**
	 * Dst as int.
	 *
	 * @param dst   the dst
	 * @param index the index
	 */
	public void dstAsInt(int dst, int index) {
		Ip6Layout.DST_AS_INT.setInt(dst, buffer(), index);
	}

	/**
	 * Dst as long.
	 *
	 * @param index the index
	 * @return the long
	 */
	public long dstAsLong(int index) {
		return Ip6Layout.DST_AS_LONG.getInt(buffer(), index);
	}

	/**
	 * Dst as long.
	 *
	 * @param dst   the dst
	 * @param index the index
	 */
	public void dstAsLong(long dst, int index) {
		Ip6Layout.DST_AS_LONG.setLong(dst, buffer(), index);
	}

	/**
	 * Flow.
	 *
	 * @return the int
	 */
	@Meta(abbr = "flow")
	public int flowLabel() {
		return Ip6Layout.FLOW.getInt(buffer()) & FLOW_LABEL_MASK;
	}

	/**
	 * Flow.
	 *
	 * @param flow the flow
	 */
	public void flowLabel(int flow) {
		Ip6Layout.FLOW.setInt(flow, buffer());
	}

	/**
	 * Hop limit.
	 *
	 * @return the int
	 */
	@Meta(abbr = "limit")
	public int hopLimit() {
		return Ip6Layout.HOP_LIMIT.getInt(buffer()) & 0xFF;
	}

	/**
	 * Hop limit.
	 *
	 * @param hopLimit the hop limit
	 */
	public void hopLimit(int hopLimit) {
		Ip6Layout.HOP_LIMIT.setInt(hopLimit, buffer());
	}

	/**
	 * Next.
	 *
	 * @return the int
	 */
	@Meta(abbr = "proto")
	public int nextHeader() {
		return Ip6Layout.NEXT.getInt(buffer());
	}

	/**
	 * Next.
	 *
	 * @param next the next
	 */
	public void nextHeader(int next) {
		Ip6Layout.NEXT.setInt(next, buffer());
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.Ip#payloadLength()
	 */
	@Override
	@Meta(abbr = "len")
	public int payloadLength() {
		return Ip6Layout.PAYLOAD_LENGTH.getInt(buffer());
	}

	/**
	 * Payload length.
	 *
	 * @param payloadLength the payload length
	 */
	public void payloadLength(int payloadLength) {
		Ip6Layout.PAYLOAD_LENGTH.setInt(payloadLength, buffer());
	}

	@Override
	public int protocol() {
		return super.protocol();
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.Ip#src()
	 */
	@Override
	@Meta
	public byte[] src() {
		return Ip6Layout.SRC_BYTES.getByteArray(buffer());
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.Ip#srcAsAddress()
	 */
	@Override
	public IpAddress srcAsAddress() {
		return new Ip6Address(src());
	}

	/**
	 * Src as int.
	 *
	 * @param index the index
	 * @return the int
	 */
	public int srcAsInt(int index) {
		return Ip6Layout.SRC_AS_INT.getInt(buffer(), index);
	}

	/**
	 * Src as int.
	 *
	 * @param src   the src
	 * @param index the index
	 */
	public void srcAsInt(int src, int index) {
		Ip6Layout.DST_AS_INT.setInt(src, buffer(), index);
	}

	/**
	 * Src as long.
	 *
	 * @param index the index
	 * @return the long
	 */
	public long srcAsLong(int index) {
		return Ip6Layout.SRC_AS_LONG.getLong(buffer(), index);
	}

	/**
	 * Src as long.
	 *
	 * @param src   the src
	 * @param index the index
	 */
	public void srcAsLong(long src, int index) {
		Ip6Layout.DST_AS_LONG.setLong(src, buffer(), index);
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.DiffServ#trafficClass()
	 */
	@Override
	@Meta
	public int trafficClass() {
		return flowLabel() & TRAFFIC_CLASS_MASK;
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.DiffServ#trafficDscp()
	 */
	@Override
	@Meta(value = MetaType.ATTRIBUTE, abbr = "dscp")
	public int trafficDscp() {
		return flowLabel() & TRAFFIC_DSCP_MASK;
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.DiffServ#trafficDscpAbbr()
	 */
	@Override
	@Meta(MetaType.ATTRIBUTE)
	public String trafficDscpAbbr() {
		return DiffServEcn.resolveAbbr(trafficEcn() >> 20);
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.DiffServ#trafficDscpDescription()
	 */
	@Override
	@Meta(MetaType.ATTRIBUTE)
	public String trafficDscpDescription() {
		return DiffServDscp.resolveDescription(trafficDscp() >> 22);
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.DiffServ#trafficDscpName()
	 */
	@Override
	@Meta(MetaType.ATTRIBUTE)
	public String trafficDscpName() {
		return DiffServDscp.resolveName(trafficDscp() >> 22);
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.DiffServ#trafficEcn()
	 */
	@Override
	@Meta(MetaType.ATTRIBUTE)
	public int trafficEcn() {
		return flowLabel() & TRAFFIC_ECN_MASK;
	}

	/**
	 * Version.
	 *
	 * @return the int
	 */
	@Override
	@Meta(abbr = "ver")
	public int version() {
		return Ip6Layout.VERSION.getInt(buffer());
	}

	@Meta(MetaType.ATTRIBUTE)
	public int word0() {
		return buffer().getInt(0);
	}
}
