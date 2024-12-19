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
package com.slytechs.jnet.protocol.tcpip.network;

/**
 * DiffServ, or Differentiated Services.
 * 
 * </p>
 * DiffServ, or Differentiated Services, is a network architecture that
 * specifies a mechanism for classifying and managing network traffic and
 * providing quality of service (QoS) on modern IP networks. DiffServ can, for
 * example, be used to provide low-latency to critical network traffic such as
 * voice or streaming media while providing best-effort service to non-critical
 * services such as web traffic or file transfers.
 * </p>
 * <p>
 * DiffServ uses a 6-bit differentiated services code point (DSCP) in the 8-bit
 * differentiated services field (DS field) in the IP header for packet
 * classification purposes. The DS field replaces the outdated IPv4 TOS field.
 * </p>
 * <p>
 * The DSCP value can range from 0 to 63, and each value corresponds to a
 * specific type of traffic or a set of traffic characteristics. For example,
 * the DSCP value of 0 is used for best-effort traffic, while the DSCP value of
 * 1 is used for voice traffic.
 * </p>
 * <p>
 * Once a packet has been classified, it is then assigned a per-hop behavior
 * (PHB). The PHB specifies how the packet should be treated by routers and
 * other network devices. For example, a packet with a PHB of EF (which stands
 * for Expedited Forwarding) will be given priority over packets with other
 * PHBs.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface DiffServ {

	/** A traffic class/DiffServ bitmask. */
	int TRAFFIC_CLASS = 0xFF;

	/** A bitmask for the DSCP (code-point) field. */
	int DIFF_SERV_DSCP = 0xFE;

	/** A bitmask for the ECN (explicit congestion notification) field. */
	int DIFF_SERV_ECN = 0x03;

	/**
	 * </p>
	 * IP traffic class is a way of classifying IP packets for Quality of Service
	 * (QoS) treatment. It is used to mark packets with different levels of
	 * importance, such as voice traffic, video traffic, or bulk data traffic.
	 * Routers can then use the IP traffic class markings to decide how to handle
	 * the packets, such as giving priority to voice traffic or dropping bulk data
	 * traffic during periods of congestion.
	 * </p>
	 * <p>
	 * The IP traffic class is a 8-bit field in the IPv4 header that is divided into
	 * two parts: the first 3 bits are used for IP precedence, and the last 5 bits
	 * are used for Differentiated Services (DiffServ). IP precedence is an older
	 * way of classifying IP traffic, and it is not widely used anymore. DiffServ is
	 * the newer way of classifying IP traffic, and it is used by most networks
	 * today.
	 * </p>
	 * <p>
	 * The DiffServ bits in the IP traffic class field can be used to mark packets
	 * with one of 32 different values using {@link #trafficDscp()} method. These
	 * values are called Differentiated Services Code Points (DSCPs), and they are
	 * defined by the Internet Assigned Numbers Authority (IANA). The IANA has
	 * assigned specific meanings to some of the DSCPs, such as Expedited Forwarding
	 * (EF) for voice traffic and Assured Forwarding (AF) for video traffic.
	 * </p>
	 * <p>
	 * Network operators can also define their own DSCPs to meet the specific needs
	 * of their network. For example, a network operator might define a DSCP for
	 * bulk data traffic that is guaranteed to be delivered within a certain amount
	 * of time.
	 * </p>
	 * <p>
	 *
	 * 
	 * @return the int
	 */
	int trafficClass();

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
	 * @return numerical code point field value
	 */
	int trafficDscp();

	/**
	 * An abbreviated name of the Differentiated Services Code Point (DSCP) field.
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
	 * @return A string abbreviated name for the code-point value
	 */
	String trafficDscpAbbr();

	/**
	 * A description of the Differentiated Services Code Point (DSCP) field.
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
	 * @return A string abbreviated name for the code-point value
	 */
	String trafficDscpDescription();

	/**
	 * A descriptive name of the Differentiated Services Code Point (DSCP) field.
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
	 * @return A string abbreviated name for the code-point value
	 */
	String trafficDscpName();

	/**
	 * A 2-bit DiffServ Explicit Congestion Notification (ECN) field.
	 * <p>
	 * The DiffServ ECN field is a 2-bit field in the IPv4 header that is used to
	 * settingsSupport Explicit Congestion Notification (ECN). ECN is a way of notifying
	 * routers that a network is becoming congested. This allows routers to take
	 * steps to avoid congestion, such as dropping packets or slowing down the
	 * transmission of packets.
	 * </p>
	 * <p>
	 * The DiffServ ECN field can be used to mark packets in one of two ways:
	 * </p>
	 * <ul>
	 * <li>ECN-Capable (00): This indicates that the packet is ECN-capable, but it
	 * does not indicate whether or not the packet has already been marked with
	 * ECN.</li>
	 * <li>ECN-CE (11): This indicates that the packet has already been marked with
	 * ECN.</li>
	 * </ul>
	 * <p>
	 * When a router receives a packet that is marked with ECN-CE, the router will
	 * take steps to avoid congestion, such as dropping packets or slowing down the
	 * transmission of packets. This helps to prevent the congestion from getting
	 * worse and affecting other packets in the network. *
	 * </p>
	 * <p>
	 * 
	 * @return numerical value of the ECN field
	 */
	int trafficEcn();

}