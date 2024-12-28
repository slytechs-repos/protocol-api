/*
 * Sly Technologies Free License
 * 
 * Copyright 2024 Sly Technologies Inc.
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

/**
 * Interface defining IP protocol constants and associated field values. These
 * constants represent common protocol types, header fields, options, and other
 * IP-specific attributes for IPv4 and IPv6.
 * 
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface IpConstants {

	/** Common IP Protocol Types */

	/** Internet Control Message Protocol for IPv4 */
	int IP_TYPE_ICMPv4 = 1;
	/** IPv4 encapsulated within IPv4 */
	int IP_TYPE_IPv4_IN_IP = 4;
	/** Transmission Control Protocol */
	int IP_TYPE_TCP = 6;
	/** User Datagram Protocol */
	int IP_TYPE_UDP = 17;
	/** IPv6 encapsulated within IPv4 */
	int IP_TYPE_IPv6_IN_IP = 41;
	/** Generic Routing Encapsulation */
	int IP_TYPE_GRE = 47;
	/** Stream Control Transmission Protocol */
	int IP_TYPE_SCTP = 132;

	/** IPv6 Special Protocol Types */

	/** IPv6 Hop-by-Hop Option */
	int IP_TYPE_IPv6_HOP_BY_HOP = 0;
	/** IPv6 Routing Header */
	int IP_TYPE_IPv6_ROUTING_HEADER = 43;
	/** IPv6 Fragment Header */
	int IP_TYPE_IPv6_FRAGMENT_HEADER = 44;
	/** Encapsulating Security Payload */
	int IP_TYPE_IPv6_ENCAPSULATING_SECURITY_PAYLOAD = 50;
	/** Authentication Header */
	int IP_TYPE_IPv6_AUTHENTICATION_HEADER = 51;
	/** Internet Control Message Protocol for IPv6 */
	int IP_TYPE_ICMPv6 = 58;
	/** No Next Header */
	int IP_TYPE_NO_NEXT = 59;
	/** Destination Options Header */
	int IP_TYPE_IPv6_DESTINATION_OPTIONS = 60;
	/** Mobility Header */
	int IP_TYPE_IPv6_MOBILITY_HEADER = 135;
	/** Host Identity Protocol */
	int IP_TYPE_IPv6_HOST_IDENTITY_PROTOCOL = 139;
	/** Shim6 Protocol */
	int IP_TYPE_IPv6_SHIM6_PROTOCOL = 140;

	/** IPv4 Header Constants */

	/** Minimum IPv4 header length in bytes */
	int IPv4_HEADER_LEN = 20;
	/** Maximum IPv4 header length in bytes */
	int IPv4_HEADER_MAX_LEN = 60;
	/** IPv4 version field offset */
	int IPv4_FIELD_VER = 0;
	/** Protocol field offset in IPv4 header */
	int IPv4_FIELD_PROTOCOL = 9;
	/** Destination address length in IPv4 */
	int IPv4_FIELD_DST_LEN = 4;
	/** Source address offset in IPv4 header */
	int IPv4_FIELD_SRC = 12;
	/** Destination address offset in IPv4 header */
	int IPv4_FIELD_DST = 16;
	/** Source address length in IPv4 */
	int IPv4_FIELD_SRC_LEN = 4;
	/** Flags field offset in IPv4 header */
	int IPv4_FIELD_FLAGS = 6;
	/** Identification field offset in IPv4 header */
	int IPv4_FIELD_IDENT = 4;
	/** Total length field offset in IPv4 header */
	int IPv4_FIELD_TOTAL_LEN = 2;

	/** IPv4 Fragmentation Constants */

	/** Fragment offset mask for IPv4 */
	int IPv4_MASK16_FRAGOFF = 0x1FFF;
	/** Don't Fragment flag in IPv4 */
	int IPv4_FLAG16_DF = 0x4000;
	/** More Fragments flag in IPv4 */
	int IPv4_FLAG16_MF = 0x2000;

	/** IPv4 Options */

	/** End of Option List */
	int IPv4_OPTION_TYPE_EOOL = 0x00;
	/** No Operation */
	int IPv4_OPTION_TYPE_NOP = 0x01;
	/** Security */
	int IPv4_OPTION_TYPE_SEC_DEF = 0x02;
	/** Record Route */
	int IPv4_OPTION_TYPE_RR = 0x07;
	/** MTU Probe */
	int IPv4_OPTION_TYPE_MTUP = 0x0B;
	/** MTU Reply */
	int IPv4_OPTION_TYPE_MTUR = 0x0C;
	/** Extended Internet Protocol */
	int IPv4_OPTION_TYPE_ENCODE = 0x0F;
	/** Quick-Start */
	int IPv4_OPTION_TYPE_QS = 0x19;
	/** Timestamp */
	int IPv4_OPTION_TYPE_TS = 0x44;
	/** Traceroute */
	int IPv4_OPTION_TYPE_TR = 0x52;
	/** Basic Security Option */
	int IPv4_OPTION_TYPE_SEC = 0x82;
	/** Router Alert */
	int IPv4_OPTION_TYPE_RTRALT = 0x94;

	/** IPv6 Header Constants */

	/** IPv6 header length in bytes */
	int IPv6_HEADER_LEN = 40;
	/** Next header field offset in IPv6 */
	int IPv6_FIELD_NEXT_HOP = 6;
	/** Source address offset in IPv6 header */
	int IPv6_FIELD_SRC = 8;
	/** Destination address offset in IPv6 header */
	int IPv6_FIELD_DST = 24;
	/** Source address length in IPv6 */
	int IPv6_FIELD_SRC_LEN = 16;
	/** Destination address length in IPv6 */
	int IPv6_FIELD_DST_LEN = 16;

	/** IPv6 Fragmentation Constants */

	/** Fragment offset field in IPv6 */
	int IPv6_FIELD_FRAG_OFFSET = 2;
	/** Identification field in IPv6 */
	int IPv6_FIELD_IDENTIFICATION = 4;
	/** More Fragments flag in IPv6 */
	int IPv6_FLAG16_MF = 0x8000;
	/** Fragment offset mask in IPv6 */
	int IPv6_MASK16_FRAGOFF = 0x1FFF;

	/** IPv6 Extension Header Types */

	/** Hop-by-Hop Option header */
	int IPv6_OPTION_TYPE_HOP_BY_HOP = 0;
	/** Fragment header */
	int IPv6_OPTION_TYPE_FRAGMENT = 44;
	/** Destination options header */
	int IPv6_OPTION_TYPE_DESTINATION = 60;
	/** Routing header */
	int IPv6_OPTION_TYPE_ROUTING = 43;
	/** Encapsulating Security Payload header */
	int IPv6_OPTION_TYPE_SECURITY = 50;
	/** Authentication header */
	int IPv6_OPTION_TYPE_AUTHENTICATION = 51;
	/** No Next Header */
	int IPv6_OPTION_TYPE_NO_NEXT = 59;
	/** Mobility header */
	int IPv6_OPTION_TYPE_MOBILITY = 135;
	/** Host Identity Protocol header */
	int IPv6_OPTION_TYPE_IDENTITY = 139;
	/** Shim6 Protocol header */
	int IPv6_OPTION_TYPE_SHIMv6 = 140;

	/** IPv6 Extension Header Sizes */

	/** Length of Fragment header in IPv6 */
	int IPv6_OPTION_TYPE_FRAGMENT_LEN = 8;
}
