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
package com.slytechs.jnet.protocol.tcpip.icmp;

/**
 * Interface defining constants for the Internet Control Message Protocol
 * (ICMP), including both ICMPv4 and ICMPv6. ICMP is used by network devices to
 * send error messages and operational information.
 * 
 * <p>
 * This interface includes header lengths, field offsets, and ICMP message types
 * for both IPv4 and IPv6 implementations.
 * </p>
 * 
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface IcmpConstants {

	/* ICMPv4 Constants */

	/**
	 * Length of the ICMPv4 header in bytes.
	 */
	int ICMPv4_HEADER_LEN = 4;

	/**
	 * Offset of the ICMPv4 Type field.
	 */
	int ICMPv4_FIELD_TYPE = 0;

	/**
	 * Offset of the ICMPv4 Code field.
	 */
	int ICMPv4_FIELD_CODE = 1;

	/**
	 * Offset of the ICMPv4 Checksum field.
	 */
	int ICMPv4_FIELD_CHECKSUM = 2;

	/* ICMPv4 Types */

	/** ICMPv4 Echo Request message type. */
	int ICMPv4_TYPE_ECHO_REQUEST = 8;

	/** ICMPv4 Echo Reply message type. */
	int ICMPv4_TYPE_ECHO_REPLY = 0;

	/** ICMPv4 Destination Unreachable message type. */
	int ICMP_TYPE_UNREACHABLE = 1;

	/* ICMPv4 Echo Constants */

	/**
	 * Length of the ICMPv4 Echo header in bytes.
	 */
	int ICMPv4_ECHO_HEADER_LEN = 8;

	/**
	 * Offset of the Identifier field in ICMPv4 Echo messages.
	 */
	int ICMPv4_ECHO_FIELD_IDENTIFIER = 4;

	/**
	 * Offset of the Sequence Number field in ICMPv4 Echo messages.
	 */
	int ICMPv4_ECHO_FIELD_SEQUENCE = 6;

	/* ICMPv6 Constants */

	/**
	 * Length of the ICMPv6 header in bytes.
	 */
	int ICMPv6_HEADER_LEN = 4;

	/* ICMPv6 Types */

	/** ICMPv6 Packet Too Big message type. */
	int ICMPv6_TYPE_PACKET_TOO_BIG = 2;

	/** ICMPv6 Time Exceeded message type. */
	int ICMPv6_TYPE_TIME_EXEEDED = 3;

	/** ICMPv6 Parameter Problem message type. */
	int ICMPv6_TYPE_PARAMETER_PROBLEM = 4;

	/** ICMPv6 Echo Request message type. */
	int ICMPv6_TYPE_ECHO_REQUEST = 128;

	/** ICMPv6 Echo Reply message type. */
	int ICMPv6_TYPE_ECHO_REPLY = 129;

	/** ICMPv6 Multicast Listener Query message type. */
	int ICMPv6_TYPE_MULTICAST_LISTENER_QUERY = 130;

	/** ICMPv6 Multicast Listener Report message type. */
	int ICMPv6_TYPE_MULTICAST_LISTENER_REPORT = 131;

	/** ICMPv6 Multicast Listener Done message type. */
	int ICMPv6_TYPE_MULTICAST_LISTENER_DONE = 132;

	/** ICMPv6 Router Solicitation message type. */
	int ICMPv6_TYPE_ROUTER_SOLICITATION = 133;

	/** ICMPv6 Router Advertisement message type. */
	int ICMPv6_TYPE_ROUTER_ADVERTISEMENT = 134;

	/** ICMPv6 Neighbor Solicitation message type. */
	int ICMPv6_TYPE_NEIGHBOR_SOLICITATION = 135;

	/** ICMPv6 Neighbor Advertisement message type. */
	int ICMPv6_TYPE_NEIGHBOR_ADVERTISEMENT = 136;

	/** ICMPv6 Redirect message type. */
	int ICMPv6_TYPE_REDIRECT = 137;

	/** ICMPv6 Router Renumbering message type. */
	int ICMPv6_TYPE_ROUTER_RENUMBER = 138;

	/** ICMPv6 Item Information Query message type. */
	int ICMPv6_TYPE_NODE_INFO_QUERY = 139;

	/** ICMPv6 Item Information Response message type. */
	int ICMPv6_TYPE_NODE_INFO_RESPONSE = 140;

	/** ICMPv6 Inverse Neighbor Solicitation message type. */
	int ICMPv6_TYPE_INVERSE_NEIGHBOR_SOLICITATION = 141;

	/** ICMPv6 Inverse Neighbor Advertisement message type. */
	int ICMPv6_TYPE_INVERSE_NEIGHBOR_ADVERTISEMENT = 142;

	/** ICMPv6 Multicast Listener Report Version 2 message type. */
	int ICMPv6_TYPE_MULTICAST_LISTENER_REPORTv2 = 143;

	/** ICMPv6 Home Agent Address Discovery Request message type. */
	int ICMPv6_TYPE_HOME_AGENT_REQUEST = 144;

	/** ICMPv6 Home Agent Address Discovery Reply message type. */
	int ICMPv6_TYPE_HOME_AGENT_REPLY = 145;

	/** ICMPv6 Mobile Prefix Solicitation message type. */
	int ICMPv6_TYPE_MOBILE_PREFIX_SOLICITATION = 146;

	/** ICMPv6 Mobile Prefix Advertisement message type. */
	int ICMPv6_TYPE_MOBILE_PREFIX_ADVERTISEMENT = 147;

}
