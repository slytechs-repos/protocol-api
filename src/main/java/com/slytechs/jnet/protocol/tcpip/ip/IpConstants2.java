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
package com.slytechs.jnet.protocol.tcpip.ip;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public sealed interface IpConstants2 permits IpConstants2.Private {
	final class Private implements IpConstants2 {}

	/* Common IP Protocol Types */
	int IP_TYPE_ICMPv4 = 1;
	int IP_TYPE_IPv4_IN_IP = 4;
	int IP_TYPE_TCP = 6;
	int IP_TYPE_UDP = 17;
	int IP_TYPE_IPv6_IN_IP = 41;
	int IP_TYPE_GRE = 47;
	int IP_TYPE_SCTP = 132;

	/* IPv6 Special Protocol Types */
	int IP_TYPE_IPv6_HOP_BY_HOP = 0;
	int IP_TYPE_IPv6_ROUTING_HEADER = 43;
	int IP_TYPE_IPv6_FRAGMENT_HEADER = 44;
	int IP_TYPE_IPv6_ENCAPSULATING_SECURITY_PAYLOAD = 50;
	int IP_TYPE_IPv6_AUTHENTICATION_HEADER = 51;
	int IP_TYPE_ICMPv6 = 58;
	int IP_TYPE_NO_NEXT = 59;
	int IP_TYPE_IPv6_DESTINATION_OPTIONS = 60;
	int IP_TYPE_IPv6_MOBILITY_HEADER = 135;
	int IP_TYPE_IPv6_HOST_IDENTITY_PROTOCOL = 139;
	int IP_TYPE_IPv6_SHIM6_PROTOCOL = 140;

	/* Header constants */
	int IPv4_HEADER_LEN = 20;
	int IPv4_HEADER_MAX_LEN = 60;
	int IPv4_FIELD_VER = 0;
	int IPv4_FIELD_PROTOCOL = 9;
	int IPv4_FIELD_DST_LEN = 4;
	int IPv4_FIELD_SRC = 12;
	int IPv4_FIELD_DST = 16;
	int IPv4_FIELD_SRC_LEN = 4;
	int IPv4_FIELD_FLAGS = 6;
	int IPv4_FIELD_IDENT = 4;
	int IPv4_FIELD_TOTAL_LEN = 2;

	/* Fragmentation constants */
	int IPv4_MASK16_FRAGOFF = 0x1FFF;
	int IPv4_FLAG16_DF = 0x4000;
	int IPv4_FLAG16_MF = 0x2000;

	/* Options */
	int IPv4_OPTION_TYPE_EOOL = 0x00;
	int IPv4_OPTION_TYPE_NOP = 0x01;
	int IPv4_OPTION_TYPE_SEC_DEF = 0x02;
	int IPv4_OPTION_TYPE_RR = 0x07;
	int IPv4_OPTION_TYPE_MTUP = 0x0B;
	int IPv4_OPTION_TYPE_MTUR = 0x0C;
	int IPv4_OPTION_TYPE_ENCODE = 0x0F;
	int IPv4_OPTION_TYPE_QS = 0x19;
	int IPv4_OPTION_TYPE_TS = 0x44;
	int IPv4_OPTION_TYPE_TR = 0x52;
	int IPv4_OPTION_TYPE_SEC = 0x82;
	int IPv4_OPTION_TYPE_RTRALT = 0x94;

	/* Header constants */
	int IPv6_HEADER_LEN = 40;
	int IPv6_FIELD_NEXT_HOP = 6;
	int IPv6_FIELD_SRC = 8;
	int IPv6_FIELD_DST = 24;
	int IPv6_FIELD_SRC_LEN = 16;
	int IPv6_FIELD_DST_LEN = 16;

	/* Fragment constants */
	int IPv6_FIELD_FRAG_OFFSET = 2;
	int IPv6_FIELD_IDENTIFICATION = 4;
	int IPv6_FLAG16_MF = 0x8000;
	int IPv6_MASK16_FRAGOFF = 0x1FFF;

	/* Extension header types */
	int IPv6_OPTION_TYPE_HOP_BY_HOP = 0;
	int IPv6_OPTION_TYPE_FRAGMENT = 44;
	int IPv6_OPTION_TYPE_DESTINATION = 60;
	int IPv6_OPTION_TYPE_ROUTING = 43;
	int IPv6_OPTION_TYPE_SECURITY = 50;
	int IPv6_OPTION_TYPE_AUTHENTICATION = 51;
	int IPv6_OPTION_TYPE_NO_NEXT = 59;
	int IPv6_OPTION_TYPE_MOBILITY = 135;
	int IPv6_OPTION_TYPE_IDENTITY = 139;
	int IPv6_OPTION_TYPE_SHIMv6 = 140;

	/* Extension header sizes */
	int IPv6_OPTION_TYPE_FRAGMENT_LEN = 8;
}