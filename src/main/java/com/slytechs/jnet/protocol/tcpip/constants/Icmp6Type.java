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
package com.slytechs.jnet.protocol.tcpip.constants;

import java.util.function.IntSupplier;

import com.slytechs.jnet.platform.api.util.Enums;

/**
 * The Enum Icmp6Type.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public enum Icmp6Type implements IntSupplier {

	/** The echo request. */
	ECHO_REQUEST(Icmp6Type.ICMPv6_TYPE_ECHO_REQUEST),

	/** The echo. */
	ECHO_REPLY(Icmp6Type.ICMPv6_TYPE_ECHO_REPLY),

	/** Type: Multicast Listener Report Message v2 (143). */
	MLRv2(Icmp6Type.ICMPv6_TYPE_MULTICAST_LISTENER_REPORTv2),

	/** The reserved. */
	RESERVED(Icmp6Type.ICMPv6_TYPE_RESERVED),

	/** The unreachable. */
	UNREACHABLE(Icmp6Type.ICMPv6_TYPE_UNREACHABLE),

	/** The packet too big. */
	PACKET_TOO_BIG(Icmp6Type.ICMPv6_TYPE_PACKET_TOO_BIG),

	/** The time exeeded. */
	TIME_EXEEDED(Icmp6Type.ICMPv6_TYPE_TIME_EXEEDED),

	/** The parameter problem. */
	PARAMETER_PROBLEM(Icmp6Type.ICMPv6_TYPE_PARAMETER_PROBLEM),

	/** The multicast listener query. */
	MLQ(Icmp6Type.ICMPv6_TYPE_MULTICAST_LISTENER_QUERY),

	/** Type: Multicast Listener Report Message v1 (131). */
	MLRv1(Icmp6Type.ICMPv6_TYPE_MULTICAST_LISTENER_REPORT),

	/** The multicast listener done. */
	MLD(Icmp6Type.ICMPv6_TYPE_MULTICAST_LISTENER_DONE),

	/** The router solicitation. */
	ROUTER_SOLICITATION(Icmp6Type.ICMPv6_TYPE_ROUTER_SOLICITATION),

	/** The router advertisement. */
	ROUTER_ADVERTISEMENT(Icmp6Type.ICMPv6_TYPE_ROUTER_ADVERTISEMENT),

	/** The neighbor solicitation. */
	NEIGHBOR_SOLICITATION(Icmp6Type.ICMPv6_TYPE_NEIGHBOR_SOLICITATION),

	/** The neighbor advertisement. */
	NEIGHBOR_ADVERTISEMENT(Icmp6Type.ICMPv6_TYPE_NEIGHBOR_ADVERTISEMENT),

	/** The redirect. */
	REDIRECT(Icmp6Type.ICMPv6_TYPE_REDIRECT),

	/** The router renumber. */
	ROUTER_RENUMBER(Icmp6Type.ICMPv6_TYPE_ROUTER_RENUMBER),

	/** The node info query. */
	NODE_INFO_QUERY(Icmp6Type.ICMPv6_TYPE_NODE_INFO_QUERY),

	/** The node info response. */
	NODE_INFO_RESPONSE(Icmp6Type.ICMPv6_TYPE_NODE_INFO_RESPONSE),

	/** The inverse neighbor solicitation. */
	INVERSE_NEIGHBOR_SOLICITATION(Icmp6Type.ICMPv6_TYPE_INVERSE_NEIGHBOR_SOLICITATION),

	/** The inverse neighbor advertisement. */
	INVERSE_NEIGHBOR_ADVERTISEMENT(Icmp6Type.ICMPv6_TYPE_INVERSE_NEIGHBOR_ADVERTISEMENT),

	/** The home agent request. */
	HOME_AGENT_REQUEST(Icmp6Type.ICMPv6_TYPE_HOME_AGENT_REQUEST),

	/** The home agent reply. */
	HOME_AGENT_REPLY(Icmp6Type.ICMPv6_TYPE_HOME_AGENT_REPLY),

	/** The mobile prefix solicitation. */
	MOBILE_PREFIX_SOLICITATION(Icmp6Type.ICMPv6_TYPE_MOBILE_PREFIX_SOLICITATION),

	/** The mobile prefix advertisement. */
	MOBILE_PREFIX_ADVERTISEMENT(Icmp6Type.ICMPv6_TYPE_MOBILE_PREFIX_ADVERTISEMENT),

	/** The FMI pv 6 MESSAGE. */
	FMIPv6_MESSAGE(Icmp6Type.ICMPv6_TYPE_FMIPv6_MESSAGE),

	/** The rpl control message. */
	RPL_CONTROL_MESSAGE(Icmp6Type.ICMPv6_TYPE_RPL_CONTROL_MESSAGE),

	/** The ILN pv 6 LOCATO R UPDAT E MESSAGE. */
	ILNPv6_LOCATOR_UPDATE_MESSAGE(Icmp6Type.ICMPv6_TYPE_ILNPv6_LOCATOR_UPDATE_MESSAGE),

	/** The duplicate address request code suffix. */
	DUPLICATE_ADDRESS_REQUEST_CODE_SUFFIX(Icmp6Type.ICMPv6_TYPE_DUPLICATE_ADDRESS_REQUEST_CODE_SUFFIX),

	/** The duplicate address confirmation code suffix. */
	DUPLICATE_ADDRESS_CONFIRMATION_CODE_SUFFIX(Icmp6Type.ICMPv6_TYPE_DUPLICATE_ADDRESS_CONFIRMATION_CODE_SUFFIX),

	/** The mpl control message. */
	MPL_CONTROL_MESSAGE(Icmp6Type.ICMPv6_TYPE_MPL_CONTROL_MESSAGE),

	/** The extended echo request. */
	EXTENDED_ECHO_REQUEST(Icmp6Type.ICMPv6_TYPE_EXTENDED_ECHO_REQUEST),

	/** The extended echo reply. */
	EXTENDED_ECHO_REPLY(Icmp6Type.ICMPv6_TYPE_EXTENDED_ECHO_REPLY),

	;

	/** The Constant ICMPv6_TYPE_RESERVED. */
	public static final int ICMPv6_TYPE_RESERVED = 0;

	/** The Constant ICMPv6_TYPE_UNREACHABLE. */
	public static final int ICMPv6_TYPE_UNREACHABLE = 1;

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
	public static final int ICMPv6_TYPE_MULTICAST_LISTENER_REPORTv2 = 143;

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

	/**
	 * Resolve.
	 *
	 * @param type the type
	 * @return the string
	 */
	public static String resolve(Object type) {
		return Enums.resolve(type, Icmp6Type.class);
	}

	/** The type. */
	private final int type;

	/**
	 * Instantiates a new icmp 6 type.
	 *
	 * @param type the type
	 */
	Icmp6Type(int type) {
		this.type = type;
	}

	/**
	 * Gets the as int.
	 *
	 * @return the as int
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return type;
	}

	/**
	 * Value of.
	 *
	 * @param intValue the int value
	 * @return the icmp 6 type
	 */
	public static Icmp6Type valueOf(int intValue) {
		return values()[intValue];
	}

}
