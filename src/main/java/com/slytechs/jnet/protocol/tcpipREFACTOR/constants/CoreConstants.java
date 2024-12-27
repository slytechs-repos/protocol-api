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
package com.slytechs.jnet.protocol.tcpipREFACTOR.constants;

/**
 * Various core protocol pack constants.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public final class CoreConstants {
	/* @formatter:off - Descriptor type2 constants */

	/**
	 * A flag which specifies if CRC field at the end of Ether II frame has been
	 * captured. Depends on capture configuration and hardware used.
	 */
	public static final int DESC_PKT_FLAG_CRC           = 0x0001;

	/**
	 * A flag which specifies if preamble preceding the Ether II frame has been
	 * captured. Depends on capture configuration and hardware used.
	 */
	public static final int DESC_PKT_FLAG_PREAMBLE      = 0x0002;

	/** The Constant DESC_TYPE1_BYTE_SIZE. */
	public static final int DESC_TYPE1_BYTE_SIZE        = 16;

	/** The Constant DESC_TYPE2_BYTE_SIZE_MIN. */
	public static final int DESC_TYPE2_BYTE_SIZE_MIN    = 28;
	
	/** The Constant DESC_TYPE2_RECORD_BYTE_SIZE. */
	public static final int DESC_TYPE2_RECORD_BYTE_SIZE = 8;
	
	/** The Constant DESC_TYPE2_RECORD_MAX_COUNT. */
	public static final int DESC_TYPE2_RECORD_MAX_COUNT = 16;
	
	/** The Constant DESC_IPF_FRAG_KEY. */
	public static final int DESC_IPF_FRAG_KEY = 12;
	
	/** The Constant DESC_IPF_FRAG_IPv4_KEY_BYTE_SIZE. */
	public static final int DESC_IPF_FRAG_IPv4_KEY_BYTE_SIZE = 12;
	
	/** The Constant DESC_IPF_FRAG_IPv6_KEY_BYTE_SIZE. */
	public static final int DESC_IPF_FRAG_IPv6_KEY_BYTE_SIZE = 36;
	
	/** The Constant DESC_IPF_FRAG_BYTE_SIZE. */
	public static final int DESC_IPF_FRAG_BYTE_SIZE = 0 
			+ DESC_IPF_FRAG_KEY + DESC_IPF_FRAG_IPv6_KEY_BYTE_SIZE;
	
	/** The Constant DESC_IPF_REASSEMBLY_BYTE_MIN_SIZE. */
	public static final int DESC_IPF_REASSEMBLY_BYTE_MIN_SIZE = 20 ;
	
	/** The Constant DESC_IPF_REASSEMBLY_RECORD_SIZE. */
	public static final int DESC_IPF_REASSEMBLY_RECORD_SIZE = 16 ;
	
	/** The Constant DESC_IPF_REASSEMBLY_BYTE_SIZE. */
	public static final int DESC_IPF_REASSEMBLY_BYTE_SIZE = 0 
			+ DESC_IPF_REASSEMBLY_BYTE_MIN_SIZE + DESC_IPF_REASSEMBLY_RECORD_SIZE * 32;
	
	/** The Constant DESC_TYPE2_BYTE_SIZE_MAX. */
	public static final int DESC_TYPE2_BYTE_SIZE_MAX    = 0
			+ DESC_TYPE2_BYTE_SIZE_MIN
			+ (DESC_TYPE2_RECORD_MAX_COUNT * DESC_TYPE2_RECORD_BYTE_SIZE);
	/* @formatter:on - Descriptor type2 constants */

	/* @formatter:off - Descriptor Ethernet II constants */
	/** The Constant ETHER_HEADER_LEN. */
	public static final int ETHER_HEADER_LEN           = 14;
	
	/** The Constant ETHER_FIELD_TYPE. */
	public static final int ETHER_FIELD_TYPE           = 12;
	
	/** The Constant ETHER_FIELD_DST. */
	public static final int ETHER_FIELD_DST            = 0;
	
	/** The Constant ETHER_FIELD_SRC. */
	public static final int ETHER_FIELD_SRC            = 6;
	
	/** The Constant ETHER_FIELD_PREAMBLE. */
	public static final int ETHER_FIELD_PREAMBLE       = 0;
	
	/** The Constant ETHER_FIELD_LEN_PREAMBLE. */
	public static final int ETHER_FIELD_LEN_PREAMBLE   = 8;
	
	/** The Constant ETHER_FIELD_LEN_CRC. */
	public static final int ETHER_FIELD_LEN_CRC        = 4;

	/** The Constant ETHER_FIELD_DST_LEN. */
	public static final int ETHER_FIELD_DST_LEN        = 6;
	
	/** The Constant ETHER_FIELD_SRC_LEN. */
	public static final int ETHER_FIELD_SRC_LEN        = 6;
	
	/** The Constant ETHER_FIELD_LEN_TYPE. */
	public static final int ETHER_FIELD_LEN_TYPE       = 2;
	
	/** The Constant ETHER_MIN_VALUE_FOR_TYPE. */
	public static final int ETHER_MIN_VALUE_FOR_TYPE   = 0x600;
	
	/** The Constant ETHER_TYPE_IPv4. */
	public static final int ETHER_TYPE_IPv4            = 0x0800;
	
	/** The Constant ETHER_TYPE_IPv6. */
	public static final int ETHER_TYPE_IPv6            = 0x86DD;
	
	/** The Constant ETHER_TYPE_VLAN. */
	public static final int ETHER_TYPE_VLAN            = 0x8100;
	
	/** The Constant ETHER_TYPE_IPX. */
	public static final int ETHER_TYPE_IPX             = 0x8137;
	
	/** The Constant ETHER_TYPE_MPLS. */
	public static final int ETHER_TYPE_MPLS            = 0x8847;
	
	/** The Constant ETHER_TYPE_ARP. */
	public static final int ETHER_TYPE_ARP            = 0x0806;
	
	/** The Constant ETHER_TYPE_ARP. */
	public static final int ETHER_TYPE_RARP           = 0x8035;
	
	/** The Constant ETHER_TYPE_MPLS_UPSTREAM. */
	public static final int ETHER_TYPE_MPLS_UPSTREAM   = 0x8848;
	
	/** The Constant ETHER_TYPE_PPPoE_DISCOVERY. */
	public static final int ETHER_TYPE_PPPoE_DISCOVERY = 0x8848;
	
	/** The Constant ETHER_TYPE_PPPoE_SESSION. */
	public static final int ETHER_TYPE_PPPoE_SESSION   = 0x8848;
	/* @formatter:on - Descriptor Ethernet II constants */

	/** The Constant ARP_HEADER_LEN. */
	/* @formatter:off - ARP constants */
	public static final int ARP_HEADER_LEN             = 28;
	
	/** The Constant ARP_FIELD_SHA. */
	public static final int ARP_FIELD_SHA              = 8;
	
	/** The Constant ARP_FIELD_SPA. */
	public static final int ARP_FIELD_SPA              = 14;
	
	/** The Constant ARP_FIELD_THA. */
	public static final int ARP_FIELD_THA              = 18;
	
	/** The Constant ARP_FIELD_TPA. */
	public static final int ARP_FIELD_TPA              = 24;
	
	/** The Constant ARP_LEN_HALEN. */
	public static final int ARP_LEN_HALEN              = 6;
	
	/** The Constant ARP_LEN_PALEN. */
	public static final int ARP_LEN_PALEN              = 4;
	/* @formatter:on - ARP constants */

	/* @formatter:off - Descriptor VLAN constants */
	/** The Constant VLAN_HEADER_LEN. */
	public static final int VLAN_HEADER_LEN    = 4;
	
	/** The Constant VLAN_FIELD_TCI. */
	public static final int VLAN_FIELD_TYPE = 2;
	
	/** The Constant VLAN_FIELD_LEN_TCI. */
	public static final int VLAN_FIELD_LEN_TCI = 2;
	
	/* @formatter:on - Descriptor VLAN constants */

	/* Descriptor LLC constants */
	/** The Constant LLC_HEADER_LEN. */
	public static final int LLC_HEADER_LEN = 3;

	/** The Constant LLC_FIELD_DSAP. */
	public static final int LLC_FIELD_DSAP = 0;

	/** The Constant LLC_FIELD_SSAP. */
	public static final int LLC_FIELD_SSAP = 1;

	/** The Constant LLC_FIELD_CONTROL. */
	public static final int LLC_FIELD_CONTROL = 2;

	/** The Constant LLC_TYPE_FRAME. */
	public static final int LLC_TYPE_FRAME = 3;

	/** The Constant LLC_TYPE_SNAP. */
	public static final int LLC_TYPE_SNAP = 0xAA;

	/** The Constant LLC_TYPE_NETWARE. */
	public static final int LLC_TYPE_NETWARE = 0xFF;

	/** The Constant LLC_TYPE_STP. */
	public static final int LLC_TYPE_STP = 0x42;

	/** The Constant SNAP_HEADER_LEN. */
	/* Descriptor SNAP constants */
	public static final int SNAP_HEADER_LEN = 5;

	/** The Constant SNAP_FIELD_TYPE. */
	public static final int SNAP_FIELD_TYPE = 3;

	/** The Constant SNAP_FIELD_OUI. */
	public static final int SNAP_FIELD_OUI = 0;

	/** The Constant SNAP_TYPE_ETHER. */
	public static final int SNAP_TYPE_ETHER = 4;

	/** The Constant MPLS_HEADER_LEN. */
	/* Descriptor MPLS constants */
	public static final int MPLS_HEADER_LEN = 0;

	/** The Constant MPLS_BITMASK_BOTTOM. */
	public static final int MPLS_BITMASK_BOTTOM = 0;

	/** The Constant STP_HEADER_LEN. */
	public static final int STP_HEADER_LEN = 35;

	/** The Constant IPX_HEADER_LEN. */
	/* Descriptor IPX constants */
	public static final int IPX_HEADER_LEN = 30;

	/** The Constant IPX_FIELD_VALUE_CHECKSUM. */
	public static final int IPX_FIELD_VALUE_CHECKSUM = 0;

	/** The Constant ICMPv4_HEADER_LEN. */
	/* Descriptor ICMP constants */
	public static final int ICMPv4_HEADER_LEN = 4;
	
	/** The Constant ICMPv4_FIELD_TYPE. */
	public static final int ICMPv4_FIELD_TYPE = 0;
	
	/** The Constant ICMPv4_FIELD_CODE. */
	public static final int ICMPv4_FIELD_CODE = 1;
	
	/** The Constant ICMPv4_FIELD_CHECKSUM. */
	public static final int ICMPv4_FIELD_CHECKSUM = 2;
	
	/** The Constant ICMPv4_TYPE_ECHO_REQUEST. */
	public static final int ICMPv4_TYPE_ECHO_REQUEST = 8;
	
	/** The Constant ICMPv4_TYPE_ECHO_REPLY. */
	public static final int ICMPv4_TYPE_ECHO_REPLY = 0;

	/** The Constant ICMPv4_ECHO_HEADER_LEN. */
	public static final int ICMPv4_ECHO_HEADER_LEN = 8;
	
	/** The Constant ICMPv4_ECHO_FIELD_IDENTIFIER. */
	public static final int ICMPv4_ECHO_FIELD_IDENTIFIER = 4;
	
	/** The Constant ICMPv4_ECHO_FIELD_SEQUENCE. */
	public static final int ICMPv4_ECHO_FIELD_SEQUENCE = 6;

	/** The Constant ICMPv6_HEADER_LEN. */
	public static final int ICMPv6_HEADER_LEN = 4;

	/** The Constant ICMPv6_TYPE_RESERVED. */
	public static final int ICMPv6_TYPE_RESERVED = 0;

	/** The Constant ICMP_TYPE_UNREACHABLE. */
	public static final int ICMP_TYPE_UNREACHABLE = 1;

	/** The Constant ICMPv6_TYPE_PACKET_TOO_BIG. */
	public static final int ICMPv6_TYPE_PACKET_TOO_BIG = 2;

	/** The Constant ICMPv6_TYPE_TIME_EXEEDED. */
	public static final int ICMPv6_TYPE_TIME_EXEEDED = 3;

	/** The Constant ICMPv6_TYPE_PARAMETER_PROBLEM. */
	public static final int ICMPv6_TYPE_PARAMETER_PROBLEM = 4;

	/** The Constant ICMPv6_TYPE_ECHO_REQUEST. */
	public static final int ICMPv6_TYPE_ECHO_REQUEST = 128;

	/** The Constant ICMPv6_TYPE_ECHO_REPLY. */
	public static final int ICMPv6_TYPE_ECHO_REPLY = 129;

	/** The Constant ICMPv6_TYPE_MULTICAST_LISTENER_QUERY. */
	public static final int ICMPv6_TYPE_MULTICAST_LISTENER_QUERY = 130;

	/** The Constant ICMPv6_TYPE_MULTICAST_LISTENER_REPORT. */
	public static final int ICMPv6_TYPE_MULTICAST_LISTENER_REPORT = 131;

	/** The Constant ICMPv6_TYPE_MULTICAST_LISTENER_DONE. */
	public static final int ICMPv6_TYPE_MULTICAST_LISTENER_DONE = 132;

	/** The Constant ICMPv6_TYPE_ROUTER_SOLICITATION. */
	public static final int ICMPv6_TYPE_ROUTER_SOLICITATION = 133;

	/** The Constant ICMPv6_TYPE_ROUTER_ADVERTISEMENT. */
	public static final int ICMPv6_TYPE_ROUTER_ADVERTISEMENT = 134;

	/** The Constant ICMPv6_TYPE_NEIGHBOR_SOLICITATION. */
	public static final int ICMPv6_TYPE_NEIGHBOR_SOLICITATION = 135;

	/** The Constant ICMPv6_TYPE_NEIGHBOR_ADVERTISEMENT. */
	public static final int ICMPv6_TYPE_NEIGHBOR_ADVERTISEMENT = 136;

	/** The Constant ICMPv6_TYPE_REDIRECT. */
	public static final int ICMPv6_TYPE_REDIRECT = 137;

	/** The Constant ICMPv6_TYPE_ROUTER_RENUMBER. */
	public static final int ICMPv6_TYPE_ROUTER_RENUMBER = 138;

	/** The Constant ICMPv6_TYPE_NODE_INFO_QUERY. */
	public static final int ICMPv6_TYPE_NODE_INFO_QUERY = 139;

	/** The Constant ICMPv6_TYPE_NODE_INFO_RESPONSE. */
	public static final int ICMPv6_TYPE_NODE_INFO_RESPONSE = 140;

	/** The Constant ICMPv6_TYPE_INVERSE_NEIGHBOR_SOLICITATION. */
	public static final int ICMPv6_TYPE_INVERSE_NEIGHBOR_SOLICITATION = 141;

	/** The Constant ICMPv6_TYPE_INVERSE_NEIGHBOR_ADVERTISEMENT. */
	public static final int ICMPv6_TYPE_INVERSE_NEIGHBOR_ADVERTISEMENT = 142;
	
	/** The Constant ICMPv6_TYPE_MULTICAST_LISTENER_REPORTv2. */
	public static final int ICMPv6_TYPE_MULTICAST_LISTENER_REPORTv2 = 143 ;

	/** The Constant ICMPv6_TYPE_HOME_AGENT_REQUEST. */
	public static final int ICMPv6_TYPE_HOME_AGENT_REQUEST = 144;

	/** The Constant ICMPv6_TYPE_HOME_AGENT_REPLY. */
	public static final int ICMPv6_TYPE_HOME_AGENT_REPLY = 145;

	/** The Constant ICMPv6_TYPE_MOBILE_PREFIX_SOLICITATION. */
	public static final int ICMPv6_TYPE_MOBILE_PREFIX_SOLICITATION = 146;

	/** The Constant ICMPv6_TYPE_MOBILE_PREFIX_ADVERTISEMENT. */
	public static final int ICMPv6_TYPE_MOBILE_PREFIX_ADVERTISEMENT = 147;

	/** The Constant ICMPv6_TYPE_FMIPv6_MESSAGE. */
	public static final int ICMPv6_TYPE_FMIPv6_MESSAGE = 154;

	/** The Constant ICMPv6_TYPE_RPL_CONTROL_MESSAGE. */
	public static final int ICMPv6_TYPE_RPL_CONTROL_MESSAGE = 155;

	/** The Constant ICMPv6_TYPE_ILNPv6_LOCATOR_UPDATE_MESSAGE. */
	public static final int ICMPv6_TYPE_ILNPv6_LOCATOR_UPDATE_MESSAGE = 156;

	/** The Constant ICMPv6_TYPE_DUPLICATE_ADDRESS_REQUEST_CODE_SUFFIX. */
	public static final int ICMPv6_TYPE_DUPLICATE_ADDRESS_REQUEST_CODE_SUFFIX = 157;

	/** The Constant ICMPv6_TYPE_DUPLICATE_ADDRESS_CONFIRMATION_CODE_SUFFIX. */
	public static final int ICMPv6_TYPE_DUPLICATE_ADDRESS_CONFIRMATION_CODE_SUFFIX = 158;

	/** The Constant ICMPv6_TYPE_MPL_CONTROL_MESSAGE. */
	public static final int ICMPv6_TYPE_MPL_CONTROL_MESSAGE = 159;

	/** The Constant ICMPv6_TYPE_EXTENDED_ECHO_REQUEST. */
	public static final int ICMPv6_TYPE_EXTENDED_ECHO_REQUEST = 160;

	/** The Constant ICMPv6_TYPE_EXTENDED_ECHO_REPLY. */
	public static final int ICMPv6_TYPE_EXTENDED_ECHO_REPLY = 161;

	/** The Constant IPv4_HEADER_LEN. */
	/* Descriptor IP constants */
	public static final int IPv4_HEADER_LEN = 20;

	/**
	 * The longest Internet Header (IP header) size can be 15*32 Bits = 480 Bits =
	 * 60 Bytes.
	 */
	public static final int IPv4_HEADER_MAX_LEN = 60;

	/** The Constant IPv6_HEADER_LEN. */
	public static final int IPv6_HEADER_LEN = 40;

	/** The Constant IPv4_FIELD_VER. */
	public static final int IPv4_FIELD_VER = 0;

	/** The Constant IPv4_FIELD_PROTOCOL. */
	public static final int IPv4_FIELD_PROTOCOL = 9;

	/** The Constant IPv6_FIELD_NEXT_HOP. */
	public static final int IPv6_FIELD_NEXT_HOP = 6;

	/** The Constant IPv4_FIELD_DST_LEN. */
	public static final int IPv4_FIELD_DST_LEN = 4;

	/** The Constant IPv4_FIELD_SRC. */
	public static final int IPv4_FIELD_SRC = 12;

	/** The Constant IPv4_FIELD_DST. */
	public static final int IPv4_FIELD_DST = 16;

	/** The Constant IPv4_FIELD_SRC_LEN. */
	public static final int IPv4_FIELD_SRC_LEN = 4;

	/** The Constant IPv4_FIELD_FLAGS. */
	public static final int IPv4_FIELD_FLAGS = 6;
	
	/** The Constant IPv4_FIELD_IDENT. */
	public static final int IPv4_FIELD_IDENT = 4;
	
	/** The Constant IPv4_FIELD_TOTAL_LEN. */
	public static final int IPv4_FIELD_TOTAL_LEN = 2;

	/** The Constant IPv4_MASK16_FRAGOFF. */
	public static final int IPv4_MASK16_FRAGOFF = 0x1FFF;

	/** The Constant IPv4_FLAG16_DF. */
	public static final int IPv4_FLAG16_DF = 0x4000;

	/** The Constant IPv4_FLAG16_MF. */
	public static final int IPv4_FLAG16_MF = 0x2000;

	/** The Constant IPv6_FIELD_FRAG_OFFSET. */
	public static final int IPv6_FIELD_FRAG_OFFSET = 2;
	
	/** The Constant IPv6_FIELD_IDENTIFICATION. */
	public static final int IPv6_FIELD_IDENTIFICATION = 4;
	
	/** The Constant IPv6_FLAG16_MF. */
	public static final int IPv6_FLAG16_MF = 0x8000;
	
	/** The Constant IPv6_MASK16_FRAGOFF. */
	public static final int IPv6_MASK16_FRAGOFF = 0x1FFF;

	/** The Constant IPv4_FIELD_SRC. */
	public static final int IPv6_FIELD_SRC = 8;
	
	/** The Constant IPv6_FIELD_SRC_LEN. */
	public static final int IPv6_FIELD_SRC_LEN = 16;

	/** The Constant IPv4_FIELD_DST. */
	public static final int IPv6_FIELD_DST = 24;
	
	/** The Constant IPv6_FIELD_DST_LEN. */
	public static final int IPv6_FIELD_DST_LEN = 16;

	/** The Constant IP_TYPE_ICMPv4. */
	public static final int IP_TYPE_ICMPv4 = 1;

	/** The Constant IP_TYPE_IPv4_IN_IP. */
	public static final int IP_TYPE_IPv4_IN_IP = 4;

	/** The Constant IP_TYPE_TCP. */
	public static final int IP_TYPE_TCP = 6;

	/** The Constant IP_TYPE_UDP. */
	public static final int IP_TYPE_UDP = 17;

	/** The Constant IP_TYPE_IPv6_IN_IP. */
	public static final int IP_TYPE_IPv6_IN_IP = 41;

	/** The Constant IP_TYPE_GRE. */
	public static final int IP_TYPE_GRE = 47;

	/** The Constant IP_TYPE_SCTP. */
	public static final int IP_TYPE_SCTP = 132;

	/** The Constant IP_TYPE_IPv6_HOP_BY_HOP. */
	public static final int IP_TYPE_IPv6_HOP_BY_HOP = 0;

	/** The Constant IP_TYPE_IPv6_ROUTING_HEADER. */
	public static final int IP_TYPE_IPv6_ROUTING_HEADER = 43;

	/** The Constant IP_TYPE_IPv6_FRAGMENT_HEADER. */
	public static final int IP_TYPE_IPv6_FRAGMENT_HEADER = 44;

	/** The Constant IP_TYPE_IPv6_ENCAPSULATING_SECURITY_PAYLOAD. */
	public static final int IP_TYPE_IPv6_ENCAPSULATING_SECURITY_PAYLOAD = 50;

	/** The Constant IP_TYPE_IPv6_AUTHENTICATION_HEADER. */
	public static final int IP_TYPE_IPv6_AUTHENTICATION_HEADER = 51;

	/** The Constant IP_TYPE_ICMPv6. */
	public static final int IP_TYPE_ICMPv6 = 58;

	/** The Constant IP_TYPE_NO_NEXT. */
	public static final int IP_TYPE_NO_NEXT = 59;

	/** The Constant IP_TYPE_IPv6_DESTINATION_OPTIONS. */
	public static final int IP_TYPE_IPv6_DESTINATION_OPTIONS = 60;

	/** The Constant IP_TYPE_IPv6_MOBILITY_HEADER. */
	public static final int IP_TYPE_IPv6_MOBILITY_HEADER = 135;

	/** The Constant IP_TYPE_IPv6_HOST_IDENTITY_PROTOCOL. */
	public static final int IP_TYPE_IPv6_HOST_IDENTITY_PROTOCOL = 139;

	/** The Constant IP_TYPE_IPv6_SHIM6_PROTOCOL. */
	public static final int IP_TYPE_IPv6_SHIM6_PROTOCOL = 140;

	/** The Constant IPv4_OPTION_TYPE_EOOL. */
	// @formatter:off
	public static final int IPv4_OPTION_TYPE_EOOL      = 0x00;
	
	/** The Constant IPv4_OPTION_TYPE_NOP. */
	public static final int IPv4_OPTION_TYPE_NOP       = 0x01;
	
	/** The Constant IPv4_OPTION_TYPE_SEC_DEF. */
	public static final int IPv4_OPTION_TYPE_SEC_DEF   = 0x02;
	
	/** The Constant IPv4_OPTION_TYPE_RR. */
	public static final int IPv4_OPTION_TYPE_RR        = 0x07;
	
	/** The Constant IPv4_OPTION_TYPE_EXP1_ZSU. */
	public static final int IPv4_OPTION_TYPE_EXP1_ZSU  = 0x0A;
	
	/** The Constant IPv4_OPTION_TYPE_MTUP. */
	public static final int IPv4_OPTION_TYPE_MTUP      = 0x0B;
	
	/** The Constant IPv4_OPTION_TYPE_MTUR. */
	public static final int IPv4_OPTION_TYPE_MTUR      = 0x0C;
	
	/** The Constant IPv4_OPTION_TYPE_ENCODE. */
	public static final int IPv4_OPTION_TYPE_ENCODE    = 0x0F;
	
	/** The Constant IPv4_OPTION_TYPE_QS. */
	public static final int IPv4_OPTION_TYPE_QS        = 0x19;
	
	/** The Constant IPv4_OPTION_TYPE_EXP1. */
	public static final int IPv4_OPTION_TYPE_EXP1      = 0x1E;
	
	/** The Constant IPv4_OPTION_TYPE_TS. */
	public static final int IPv4_OPTION_TYPE_TS        = 0x44;
	
	/** The Constant IPv4_OPTION_TYPE_RT. */
	public static final int IPv4_OPTION_TYPE_RT        = 0x52;
	
	/** The Constant IPv4_OPTION_TYPE_EXP2. */
	public static final int IPv4_OPTION_TYPE_EXP2      = 0x5E;
	
	/** The Constant IPv4_OPTION_TYPE_SEC. */
	public static final int IPv4_OPTION_TYPE_SEC       = 0x82;
	
	/** The Constant IPv4_OPTION_TYPE_LSR. */
	public static final int IPv4_OPTION_TYPE_LSR       = 0x83;
	
	/** The Constant IPv4_OPTION_TYPE_E_SEC. */
	public static final int IPv4_OPTION_TYPE_E_SEC     = 0x85;
	
	/** The Constant IPv4_OPTION_TYPE_CIPSO. */
	public static final int IPv4_OPTION_TYPE_CIPSO     = 0x86;
	
	/** The Constant IPv4_OPTION_TYPE_SID. */
	public static final int IPv4_OPTION_TYPE_SID       = 0x88;
	
	/** The Constant IPv4_OPTION_TYPE_SSR. */
	public static final int IPv4_OPTION_TYPE_SSR       = 0x89;
	
	/** The Constant IPv4_OPTION_TYPE_EXP3_VISA. */
	public static final int IPv4_OPTION_TYPE_EXP3_VISA = 0x8E;
	
	/** The Constant IPv4_OPTION_TYPE_IMITD. */
	public static final int IPv4_OPTION_TYPE_IMITD     = 0x90;
	
	/** The Constant IPv4_OPTION_TYPE_E_IP. */
	public static final int IPv4_OPTION_TYPE_E_IP      = 0x91;
	
	/** The Constant IPv4_OPTION_TYPE_E_ADDR. */
	public static final int IPv4_OPTION_TYPE_E_ADDR    = 0x93;
	
	/** The Constant IPv4_OPTION_TYPE_RTRALT. */
	public static final int IPv4_OPTION_TYPE_RTRALT    = 0x94;
	
	/** The Constant IPv4_OPTION_TYPE_SBD. */
	public static final int IPv4_OPTION_TYPE_SBD       = 0x95;
	
	/** The Constant IPv4_OPTION_TYPE_DPS. */
	public static final int IPv4_OPTION_TYPE_DPS       = 0x97;
	
	/** The Constant IPv4_OPTION_TYPE_UMP. */
	public static final int IPv4_OPTION_TYPE_UMP       = 0x98;
	
	/** The Constant IPv4_OPTION_TYPE_EXP4. */
	public static final int IPv4_OPTION_TYPE_EXP4      = 0x9E;
	
	/** The Constant IPv4_OPTION_TYPE_EXP5. */
	public static final int IPv4_OPTION_TYPE_EXP5      = 0xCD;
	
	/** The Constant IPv4_OPTION_TYPE_EXP6. */
	public static final int IPv4_OPTION_TYPE_EXP6      = 0xDE;
	// @formatter:on

	/** The Constant IPv6_OPTION_TYPE_HOP_BY_HOP. */
	// @formatter:off
	public static final int IPv6_OPTION_TYPE_HOP_BY_HOP     = 0;
	
	/** The Constant IPv6_OPTION_TYPE_FRAGMENT. */
	public static final int IPv6_OPTION_TYPE_FRAGMENT       = 44;
	
	/** The Constant IPv6_OPTION_TYPE_DESTINATION. */
	public static final int IPv6_OPTION_TYPE_DESTINATION    = 60;
	
	/** The Constant IPv6_OPTION_TYPE_ROUTING. */
	public static final int IPv6_OPTION_TYPE_ROUTING        = 43;
	
	/** The Constant IPv6_OPTION_TYPE_SECURITY. */
	public static final int IPv6_OPTION_TYPE_SECURITY       = 50;
	
	/** The Constant IPv6_OPTION_TYPE_AUTHENTICATION. */
	public static final int IPv6_OPTION_TYPE_AUTHENTICATION = 51;
	
	/** The Constant IPv6_OPTION_TYPE_NO_NEXT. */
	public static final int IPv6_OPTION_TYPE_NO_NEXT        = 59;
	
	/** The Constant IPv6_OPTION_TYPE_MOBILITY. */
	public static final int IPv6_OPTION_TYPE_MOBILITY       = 135;
	
	/** The Constant IPv6_OPTION_TYPE_IDENTITY. */
	public static final int IPv6_OPTION_TYPE_IDENTITY       = 139;
	
	/** The Constant IPv6_OPTION_TYPE_SHIMv6. */
	public static final int IPv6_OPTION_TYPE_SHIMv6         = 140;
	
	/** The Constant IPv6_OPTION_TYPE_FRAGMENT_LEN. */
	public static final int IPv6_OPTION_TYPE_FRAGMENT_LEN   = 8;
	// @formatter:on

	/* TCP constants */

	/** The Constant TCP_HEADER_LEN. */
	public static final int TCP_HEADER_LEN = 20;

	/** The Constant TCP_FIELD_IHL. */
	public static final int TCP_FIELD_IHL = 12;

	/** The Constant TCP_FIELD_SRC. */
	public static final int TCP_FIELD_SRC = 0;

	/** The Constant TCP_FIELD_DST. */
	public static final int TCP_FIELD_DST = 2;

	/** The Constant TCP_OPTION_FIELD_KIND. */
	public static final int TCP_OPTION_FIELD_KIND = 0;

	/** The Constant TCP_OPTION_FIELD_LENGTH. */
	public static final int TCP_OPTION_FIELD_LENGTH = 1;

	/** The Constant TCP_OPTION_FIELD_DATA. */
	public static final int TCP_OPTION_FIELD_DATA = 2;

	/** The Constant TCP_OPTION_KIND_EOL. */
	public static final int TCP_OPTION_KIND_EOL = 0;

	/** The Constant TCP_OPTION_KIND_NOP. */
	public static final int TCP_OPTION_KIND_NOP = 1;

	/** The Constant TCP_OPTION_KIND_MSS. */
	public static final int TCP_OPTION_KIND_MSS = 2;

	/** The Constant TCP_OPTION_KIND_WIN_SCALE. */
	public static final int TCP_OPTION_KIND_WIN_SCALE = 3;

	/** The Constant TCP_OPTION_KIND_SACK_PERMITTED. */
	public static final int TCP_OPTION_KIND_SACK_PERMITTED = 4;
	
	/** The Constant TCP_OPTION_KIND_SACK. */
	public static final int TCP_OPTION_KIND_SACK = 5;

	/** The Constant TCP_OPTION_KIND_TIMESTAMP. */
	public static final int TCP_OPTION_KIND_TIMESTAMP = 8;

	/** The Constant TCP_OPTION_KIND_FASTOPEN. */
	public static final int TCP_OPTION_KIND_FASTOPEN = 34;

	/** The Constant TCP_OPTION_LEN_MSS. */
	public static final int TCP_OPTION_LEN_MSS = 4;

	/** The Constant TCP_OPTION_LEN_TIMESTAMP. */
	public static final int TCP_OPTION_LEN_TIMESTAMP = 10;

	/** The Constant TCP_OPTION_LEN_WIN_SCALE. */
	public static final int TCP_OPTION_LEN_WIN_SCALE = 3;

	/** The Constant TCP_OPTION_LEN_FASTOPEN. */
	public static final int TCP_OPTION_LEN_FASTOPEN = 18;

	/** The Constant TCP_OPTION_LEN_NOP. */
	public static final int TCP_OPTION_LEN_NOP = 1;

	/** The Constant TCP_OPTION_LEN_EOL. */
	public static final int TCP_OPTION_LEN_EOL = 1;

	/** The Constant FLAG_ACK. */
	public static final int TCP_FLAG_ACK = 0x10;

	/** The Constant FLAG_CONG. */
	public static final int TCP_FLAG_CONG = 0x80;

	/** The Constant FLAG_CWR. */
	public static final int TCP_FLAG_CWR = 0x80;

	/** The Constant FLAG_ECE. */
	public static final int TCP_FLAG_ECE = 0x40;

	/** The Constant FLAG_ECN. */
	public static final int TCP_FLAG_ECN = 0x40;

	/** The Constant FLAG_FIN. */
	public static final int TCP_FLAG_FIN = 0x01;

	/** The Constant FLAG_PSH. */
	public static final int TCP_FLAG_PSH = 0x08;

	/** The Constant FLAG_RST. */
	public static final int TCP_FLAG_RST = 0x04;

	/** The Constant FLAG_SYN. */
	public static final int TCP_FLAG_SYN = 0x02;

	/** The Constant FLAG_URG. */
	public static final int TCP_FLAG_URG = 0x20;

	/** The Constant UDP_HEADER_LEN. */
	/* Descriptor UDP constants */
	public static final int UDP_HEADER_LEN = 8;

	/** The Constant SCTP_HEADER_LEN. */
	/* Descriptor SCTP constants */
	public static final int SCTP_HEADER_LEN = 0;

	/** The Constant GRE_HEADER_LEN. */
	public static final int GRE_HEADER_LEN = 2;

	/** The Constant GRE_BITMASK_CHKSUM_FLAG. */
	/* Descriptor GRE constants */
	public static final int GRE_BITMASK_CHKSUM_FLAG = 0;

	/** The Constant GRE_BITMASK_KEY_FLAG. */
	public static final int GRE_BITMASK_KEY_FLAG = 0;

	/** The Constant GRE_BITMASK_SEQ_FLAG. */
	public static final int GRE_BITMASK_SEQ_FLAG = 0;

	/**
	 * Instantiates a new core constants.
	 */
	private CoreConstants() {
	}

}
