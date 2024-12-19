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
 * EtherType is a two-octet field in an Ethernet frame. It is used to indicate
 * which protocol is encapsulated in the payload of the frame and is used at the
 * receiving end by the data link layer to determine how the payload is
 * processed. The same field is also used to indicate the size of some Ethernet
 * frames.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
public enum EtherType implements IntSupplier {

	/** The I pv 4. */
	IPv4(EtherType.ETHER_TYPE_IPv4),

	/** The I pv 6. */
	IPv6(EtherType.ETHER_TYPE_IPv6),

	/** The vlan. */
	VLAN(EtherType.ETHER_TYPE_VLAN),

	/** The ipx. */
	IPX(EtherType.ETHER_TYPE_IPX),

	/** The mpls uni. */
	MPLS_UNI(EtherType.ETHER_TYPE_MPLS),

	/** The mpls multi. */
	MPLS_MULTI(EtherType.ETHER_TYPE_MPLS_UPSTREAM),

	/** The ARP. */
	ARP(EtherType.ETHER_TYPE_ARP),

	/** The RARP. */
	RARP(EtherType.ETHER_TYPE_RARP),

	;

	/** The Constant ETHER_TYPE_IPv4. */
	// @formatter:off
	public static final int ETHER_TYPE_IPv4            = 0x0800;
	
	/** The Constant ETHER_TYPE_IPv6. */
	public static final int ETHER_TYPE_IPv6            = 0x86DD;
	
	/** The Constant ETHER_TYPE_VLAN. */
	public static final int ETHER_TYPE_VLAN            = 0x8100;
	
	/** The Constant ETHER_TYPE_IPX. */
	public static final int ETHER_TYPE_IPX             = 0x8137;
	
	/** The Constant ETHER_TYPE_MPLS. */
	public static final int ETHER_TYPE_MPLS            = 0x8847;
	
	/** The Constant ETHER_TYPE_MPLS_UPSTREAM. */
	public static final int ETHER_TYPE_MPLS_UPSTREAM   = 0x8848;
	
	/** The Constant ETHER_TYPE_PPPoE_DISCOVERY. */
	public static final int ETHER_TYPE_PPPoE_DISCOVERY = 0x8848;
	
	/** The Constant ETHER_TYPE_PPPoE_SESSION. */
	public static final int ETHER_TYPE_PPPoE_SESSION   = 0x8848;
	
	/** The Constant ETHER_TYPE_ARP. */
	public static final int ETHER_TYPE_ARP            = 0x0806;
	
	/** The Constant ETHER_TYPE_RARP. */
	public static final int ETHER_TYPE_RARP           = 0x8035;
	// @formatter:on

	/**
	 * Resolve.
	 *
	 * @param obj the obj
	 * @return the string
	 */
	public static String resolve(Object obj) {
		return Enums.resolve(obj, EtherType.class);
	}

	/**
	 * Value of ether type.
	 *
	 * @param type the type
	 * @return the ether type
	 */
	public static EtherType valueOfEtherType(int type) {
		return Enums.valueOf(type, EtherType.class);
	}

	/** The type. */
	private final int type;

	/**
	 * Instantiates a new ether type.
	 *
	 * @param type the type
	 */
	EtherType(int type) {
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
