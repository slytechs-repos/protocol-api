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

import java.util.Arrays;
import java.util.function.IntSupplier;

import com.slytechs.jnet.jnetruntime.util.Enums;

/**
 * IPv6 extension header type.
 * 
 * <p>
 * Extension headers carry optional internet layer information and are placed
 * between the fixed header and the upper-layer protocol header.[1] Extension
 * headers form a chain, using the Next Header fields. The Next Header field in
 * the fixed header indicates the type of the first extension header; the Next
 * Header field of the last extension header indicates the type of the
 * upper-layer protocol header in the payload of the packet. All extension
 * headers are a multiple of 8 octets in size; some extension headers require
 * internal padding to meet this requirement.
 * </p>
 */
public enum Ip6ExtType implements IntSupplier {

	/**
	 * Hop-by-Hop Options. Options that need to be examined by all devices on the
	 * path.
	 */
	HOP_BY_HOP_OPTIONS(Ip6ExtType.IPv6_EXT_TYPE_HOP_BY_HOP_OPTIONS, CoreId.CORE_ID_IPv6_EXT_HOP_BY_HOP_OPTIONS),

	/** The fragment. Contains parameters for fragmentation of datagrams. */
	FRAGMENT(Ip6ExtType.IPv6_EXT_TYPE_FRAGMENT, CoreId.CORE_ID_IPv6_EXT_FRAGMENT),

	/**
	 * Destination Options (before upper-layer header). Options that need to be
	 * examined only by the destination of the packet.
	 */
	DEST_OPTIONS(Ip6ExtType.IPv6_EXT_TYPE_DEST_OPTIONS, CoreId.CORE_ID_IPv6_EXT_DEST_OPTIONS),

	/**
	 * The routing. Methods to specify the route for a datagram (used with Mobile
	 * IPv6).
	 */
	ROUTING(Ip6ExtType.IPv6_EXT_TYPE_ROUTING, CoreId.CORE_ID_IPv6_EXT_ROUTING),

	/**
	 * Encapsulating Security Payload (ESP). Carries encrypted data for secure
	 * communication.
	 */
	ECAPS_SEC_HEADER(Ip6ExtType.IPv6_EXT_TYPE_ENCAPS_SEC_HEADER, CoreId.CORE_ID_IPv6_EXT_ENCAPS_SEC_PAYLOAD),

	/**
	 * The authentication.header (AH). Contains information used to verify the
	 * authenticity of most parts of the packet.
	 */
	AUTH_HEADER(Ip6ExtType.IPv6_EXT_TYPE_AUTH_HEADER, CoreId.CORE_ID_IPv6_EXT_AUTH_HEADER),

	/**
	 * Mobility (currently without upper-layer header). Parameters used with Mobile
	 * IPv6.
	 */
	MOBILITY(Ip6ExtType.IPv6_EXT_TYPE_MOBILITY, CoreId.CORE_ID_IPv6_EXT_MOBILITY),

	/**
	 * Host Identity Protocol (HIPv2). Used for Host Identity Protocol version 2
	 * (HIPv2).
	 */
	HOST_IDENTITY(Ip6ExtType.IPv6_EXT_TYPE_HOST_IDENTITY, CoreId.CORE_ID_IPv6_EXT_HOST_IDENTITY),

	/** The SHIMv6 protocol. Used for Shim6. */
	SHIMv6(Ip6ExtType.IPv6_EXT_TYPE_SHIMv6, CoreId.CORE_ID_IPv6_EXT_SHIMv6),

	;

	// @formatter:off - types
	
	/**
	 * Hop-by-Hop Options. Options that need to be examined by all devices on the
	 * path.
	 */
	public static final int IPv6_EXT_TYPE_HOP_BY_HOP_OPTIONS = 0;
	
	/** The fragment. Contains parameters for fragmentation of datagrams. */
	public static final int IPv6_EXT_TYPE_FRAGMENT           = 44;
	
	/**
	 * Destination Options (before upper-layer header). Options that need to be
	 * examined only by the destination of the packet.
	 */
	public static final int IPv6_EXT_TYPE_DEST_OPTIONS       = 60;
	
	/**
	 * The routing. Methods to specify the route for a datagram (used with Mobile
	 * IPv6).
	 */
	public static final int IPv6_EXT_TYPE_ROUTING            = 43;
	
	/** The Constant IPv6_EXT_TYPE_AUTHENTICATION. */
	public static final int IPv6_EXT_TYPE_AUTH_HEADER        = 51;
	
	/**
	 * Encapsulating Security Payload (ESP). Carries encrypted data for secure
	 * communication.
	 */
	public static final int IPv6_EXT_TYPE_ENCAPS_SEC_HEADER  = 50;
	
	/**
	 * The authentication.header (AH). Contains information used to verify the
	 * authenticity of most parts of the packet.
	 */
	public static final int IPv6_EXT_TYPE_NO_NEXT            = 59;
	
	/**
	 * Mobility (currently without upper-layer header). Parameters used with Mobile
	 * IPv6.
	 */
	public static final int IPv6_EXT_TYPE_MOBILITY           = 135;
	
	/**
	 * Host Identity Protocol (HIPv2). Used for Host Identity Protocol version 2
	 * (HIPv2).
	 */
	public static final int IPv6_EXT_TYPE_HOST_IDENTITY      = 139;
	
	/** The SHIMv6 protocol. Used for Shim6. */
	public static final int IPv6_EXT_TYPE_SHIMv6             = 140;
	// @formatter:on

	private class Mapper {
		/** The Constant MAP_TABLE. */
		private static final int[] MAP_TABLE = new int[256];

		static {
			Arrays.fill(MAP_TABLE, -1);
		}
	}

	/**
	 * Map type to id.
	 *
	 * @param type the type
	 * @return the int
	 */
	public static int mapTypeToId(int type) {
		return Mapper.MAP_TABLE[type & 0xff];
	}

	/**
	 * Resolve.
	 *
	 * @param type the type
	 * @return the string
	 */
	public static String resolve(Object type) {
		return Enums.resolve(type, Ip6ExtType.class);
	}

	/** The type. */
	private final int type;

	/**
	 * Instantiates a new IPv6 extension next-header type constant.
	 *
	 * @param type the next-header type
	 * @param id   the protocol pack ID assigned
	 */
	Ip6ExtType(int type, int id) {
		this.type = type;

		Mapper.MAP_TABLE[type] = id;
	}

	/**
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return type;
	}

}