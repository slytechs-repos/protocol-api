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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ip;

import com.slytechs.jnet.protocol.api.core.CoreId;

/**
 * IPv6 shim6 (IPv6-in-IPv4) extension header.
 * <p>
 * The IPv6 shim header (also known as the Shim6 header) is an extension header
 * that is used to tunnel IPv6 packets over IPv4 networks. The shim6 header is
 * used to encapsulate an IPv6 packet in an IPv4 packet. This allows IPv6
 * packets to be routed over IPv4 networks, which are still the most common type
 * of network in the world.
 * </p>
 * <p>
 * The Shim6 extension header is a valuable tool for providing multihoming
 * settingsSupport for IPv6 hosts. It allows hosts to have multiple IP addresses and to
 * use any of those addresses to send or receive datagrams. This can improve the
 * reliability and performance of IPv6 networks.
 * </p>
 */
public final class Ip6Shim6Extension extends Ip6ExtensionHeader {

	/** IPv6 shim6 option ID constant */
	public static final int ID = CoreId.CORE_ID_IPv6_EXT_SHIMv6;

	/**
	 * IPv6 shim6 extension header.
	 */
	public Ip6Shim6Extension() {
		super(ID);
	}

	public int contextTag() {
		return Short.toUnsignedInt(buffer().getShort(2));
	}

	/**
	 * This field contains the locators for the source of the datagram. The locators
	 * are 128-bit IPv6 addresses.
	 *
	 * @return the IPv6 16-byte address
	 */
	public byte[] locatorSource() {
		byte[] dst = new byte[IpAddress.IPv6_ADDRESS_SIZE];

		buffer().get(4, dst);

		return dst;
	}

	/**
	 * This field contains the locators for the destination of the datagram. The
	 * locators are 128-bit IPv6 addresses.
	 *
	 * @return the IPv6 16-byte address
	 */
	public byte[] locatorDestination() {
		byte[] dst = new byte[IpAddress.IPv6_ADDRESS_SIZE];

		buffer().get(12, dst);

		return dst;
	}

	/**
	 * This field is used to identify a forked instance of the Shim6 session. The
	 * value of this field is a 32-bit identifier that is assigned by the sender of
	 * the datagram.
	 * 
	 * @return the unsigned 32-bit field value
	 */
	public long forkedInstanceId() {
		return Integer.toUnsignedLong(buffer().getInt(20));
	}
}