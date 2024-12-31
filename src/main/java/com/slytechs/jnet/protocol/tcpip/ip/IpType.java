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

import java.util.function.IntSupplier;

import com.slytechs.jnet.platform.api.util.Enums;

/**
 * IEEE Ethernet type field values table.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public enum IpType implements IntSupplier {

	/** The ICM pv 4. */
	ICMPv4(IpType.IP_TYPE_ICMPv4),
	
	/** The I pv 4 I N IP. */
	IPv4_IN_IP(IpType.IP_TYPE_IPv4_IN_IP),
	
	/** The tcp. */
	TCP(IpType.IP_TYPE_TCP),
	
	/** The udp. */
	UDP(IpType.IP_TYPE_UDP),
	
	/** The I pv 6 I N IP. */
	IPv6_IN_IP(IpType.IP_TYPE_IPv6_IN_IP),
	
	/** The gre. */
	GRE(IpType.IP_TYPE_GRE),
	
	/** The sctp. */
	SCTP(IpType.IP_TYPE_SCTP),
	
	/** The I pv 6 HO P B Y HOP. */
	IPv6_HOP_BY_HOP(IpType.IP_TYPE_IPv6_HOP_BY_HOP),
	
	/** The I pv 6 ROUTIN G HEADER. */
	IPv6_ROUTING_HEADER(IpType.IP_TYPE_IPv6_ROUTING_HEADER),
	
	/** The I pv 6 FRAGMEN T HEADER. */
	IPv6_FRAGMENT_HEADER(IpType.IP_TYPE_IPv6_FRAGMENT_HEADER),
	
	/** The I pv 6 ENCAPSULATIN G SECURIT Y PAYLOAD. */
	IPv6_ENCAPSULATING_SECURITY_PAYLOAD(IpType.IP_TYPE_IPv6_ENCAPSULATING_SECURITY_PAYLOAD),
	
	/** The I pv 6 AUTHENTICATIO N HEADER. */
	IPv6_AUTHENTICATION_HEADER(IpType.IP_TYPE_IPv6_AUTHENTICATION_HEADER),
	
	/** The ICM pv 6. */
	ICMPv6(IpType.IP_TYPE_ICMPv6),
	
	/** The no next. */
	NO_NEXT(IpType.IP_TYPE_NO_NEXT),
	
	/** The I pv 6 DESTINATIO N OPTIONS. */
	IPv6_DESTINATION_OPTIONS(IpType.IP_TYPE_IPv6_DESTINATION_OPTIONS),
	
	/** The I pv 6 MOBILIT Y HEADER. */
	IPv6_MOBILITY_HEADER(IpType.IP_TYPE_IPv6_MOBILITY_HEADER),
	
	/** The I pv 6 HOS T IDENTIT Y PROTOCOL. */
	IPv6_HOST_IDENTITY_PROTOCOL(IpType.IP_TYPE_IPv6_HOST_IDENTITY_PROTOCOL),
	
	/** The I pv 6 SHIM 6 PROTOCOL. */
	IPv6_SHIM6_PROTOCOL(IpType.IP_TYPE_IPv6_SHIM6_PROTOCOL),

	;

	/** The Constant IP_TYPE_ICMPv4. */
	// @formatter:off
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
	// @formatter:on

	/**
	 * Resolve.
	 *
	 * @param type the type
	 * @return the string
	 */
	public static String resolve(Object type) {
		return Enums.resolve(type, IpType.class);
	}

	/**
	 * Value of ip type.
	 *
	 * @param type the type
	 * @return the ip type
	 */
	public static IpType valueOfIpType(int type) {
		return Enums.valueOf(type, IpType.class);
	}

	/** The type. */
	private final int type;

	/**
	 * Instantiates a new ip type.
	 *
	 * @param type the type
	 */
	IpType(int type) {
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
