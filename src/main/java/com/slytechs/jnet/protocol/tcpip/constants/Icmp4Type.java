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

import com.slytechs.jnet.jnetruntime.util.Enums;

/**
 * The Enum Icmp4Type.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public enum Icmp4Type implements IntSupplier {

	/** The echo request. */
	ECHO_REQUEST(Icmp4Type.ICMPv4_TYPE_ECHO_REQUEST),

	/** The echo reply. */
	ECHO_REPLY(Icmp4Type.ICMPv4_TYPE_ECHO_REPLY),

	/** The destination unreachable. */
	DESTINATION_UNREACHABLE(Icmp4Type.ICMPv4_TYPE_DESTINATION_UNREACHABLE),

	/** The source quence. */
	SOURCE_QUENCE(Icmp4Type.ICMPv4_TYPE_SOURCE_QUENCE),

	/** The redirect. */
	REDIRECT(Icmp4Type.ICMPv4_TYPE_REDIRECT),

	/** The router advertisement. */
	ROUTER_ADVERTISEMENT(Icmp4Type.ICMPv4_TYPE_ROUTER_ADVERTISEMENT),

	/** The router solicitation. */
	ROUTER_SOLICITATION(Icmp4Type.ICMPv4_TYPE_ROUTER_SOLICITATION),

	/** The time exceeded. */
	TIME_EXCEEDED(Icmp4Type.ICMPv4_TYPE_TIME_EXCEEDED),

	/** The paramter problem bad ip. */
	PARAMTER_PROBLEM_BAD_IP(Icmp4Type.ICMPv4_TYPE_PARAMTER_PROBLEM_BAD_IP),

	/** The timestamp. */
	TIMESTAMP(Icmp4Type.ICMPv4_TYPE_TIMESTAMP),

	/** The timestamp reply. */
	TIMESTAMP_REPLY(Icmp4Type.ICMPv4_TYPE_TIMESTAMP_REPLY),

	/** The info request. */
	INFO_REQUEST(Icmp4Type.ICMPv4_TYPE_INFO_REQUEST),

	/** The info reply. */
	INFO_REPLY(Icmp4Type.ICMPv4_TYPE_INFO_REPLY),

	/** The address mask request. */
	ADDRESS_MASK_REQUEST(Icmp4Type.ICMPv4_TYPE_ADDRESS_MASK_REQUEST),

	/** The address mask reply. */
	ADDRESS_MASK_REPLY(Icmp4Type.ICMPv4_TYPE_ADDRESS_MASK_REPLY),

	/** The traceroute. */
	TRACEROUTE(Icmp4Type.ICMPv4_TYPE_TRACEROUTE),

	/** The extended request. */
	EXTENDED_REQUEST(Icmp4Type.ICMPv4_TYPE_EXTENDED_REQUEST),

	/** The extended reply. */
	EXTENDED_REPLY(Icmp4Type.ICMPv4_TYPE_EXTENDED_REPLY),

	;

	/** The Constant ICMPv4_TYPE_ECHO_REPLY. */
	// @formatter:off
	public static final int ICMPv4_TYPE_ECHO_REPLY              = 0;
	
	/** The Constant ICMPv4_TYPE_DESTINATION_UNREACHABLE. */
	public static final int ICMPv4_TYPE_DESTINATION_UNREACHABLE = 3;
	
	/** The Constant ICMPv4_TYPE_SOURCE_QUENCE. */
	public static final int ICMPv4_TYPE_SOURCE_QUENCE           = 4;
	
	/** The Constant ICMPv4_TYPE_REDIRECT. */
	public static final int ICMPv4_TYPE_REDIRECT                = 5;
	
	/** The Constant ICMPv4_TYPE_ECHO_REQUEST. */
	public static final int ICMPv4_TYPE_ECHO_REQUEST            = 8;
	
	/** The Constant ICMPv4_TYPE_ROUTER_ADVERTISEMENT. */
	public static final int ICMPv4_TYPE_ROUTER_ADVERTISEMENT    = 9;
	
	/** The Constant ICMPv4_TYPE_ROUTER_SOLICITATION. */
	public static final int ICMPv4_TYPE_ROUTER_SOLICITATION     = 10;
	
	/** The Constant ICMPv4_TYPE_TIME_EXCEEDED. */
	public static final int ICMPv4_TYPE_TIME_EXCEEDED           = 11;
	
	/** The Constant ICMPv4_TYPE_PARAMTER_PROBLEM_BAD_IP. */
	public static final int ICMPv4_TYPE_PARAMTER_PROBLEM_BAD_IP = 12;
	
	/** The Constant ICMPv4_TYPE_TIMESTAMP. */
	public static final int ICMPv4_TYPE_TIMESTAMP               = 13;
	
	/** The Constant ICMPv4_TYPE_TIMESTAMP_REPLY. */
	public static final int ICMPv4_TYPE_TIMESTAMP_REPLY         = 14;
	
	/** The Constant ICMPv4_TYPE_INFO_REQUEST. */
	public static final int ICMPv4_TYPE_INFO_REQUEST            = 15;
	
	/** The Constant ICMPv4_TYPE_INFO_REPLY. */
	public static final int ICMPv4_TYPE_INFO_REPLY              = 16;
	
	/** The Constant ICMPv4_TYPE_ADDRESS_MASK_REQUEST. */
	public static final int ICMPv4_TYPE_ADDRESS_MASK_REQUEST    = 17;
	
	/** The Constant ICMPv4_TYPE_ADDRESS_MASK_REPLY. */
	public static final int ICMPv4_TYPE_ADDRESS_MASK_REPLY      = 18;
	
	/** The Constant ICMPv4_TYPE_TRACEROUTE. */
	public static final int ICMPv4_TYPE_TRACEROUTE              = 30;
	
	/** The Constant ICMPv4_TYPE_EXTENDED_REQUEST. */
	public static final int ICMPv4_TYPE_EXTENDED_REQUEST        = 42;
	
	/** The Constant ICMPv4_TYPE_EXTENDED_REPLY. */
	public static final int ICMPv4_TYPE_EXTENDED_REPLY          = 43;

	// @formatter:on

	/**
	 * Resolve.
	 *
	 * @param type the type
	 * @return the string
	 */
	public static String resolve(Object type) {
		return Enums.resolve(type, Icmp4Type.class);
	}

	/** The type. */
	private final int type;

	/**
	 * Instantiates a new icmp 6 type.
	 *
	 * @param type the type
	 */
	Icmp4Type(int type) {
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

}
