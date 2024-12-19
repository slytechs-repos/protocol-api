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
package com.slytechs.jnet.protocol.tcpip.network;

import java.nio.ByteBuffer;

import com.slytechs.jnet.protocol.api.common.NetAddressType;

/**
 * IP version 6 address.
 * <p>
 * IPv6 (Internet Protocol version 6) addresses are the next generation of IP
 * addresses designed to replace IPv4 addresses. IPv6 addresses are 128 bits
 * long and are represented in hexadecimal format. *
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public final class Ip6Address extends IpAddress {

	public static Ip6Address get(int index, ByteBuffer buffer) {
		byte[] addr = new byte[IpAddress.IPv6_ADDRESS_SIZE];

		buffer.get(index, addr);

		return new Ip6Address(addr);
	}

	/** The bytes. */
	private final byte[] bytes;

	/**
	 * Instantiates a new ip 6 address.
	 */
	public Ip6Address() {
		this(new byte[IPv6_ADDRESS_SIZE]);
	}

	/**
	 * Instantiates a new ip 6 address.
	 *
	 * @param address the address
	 */
	public Ip6Address(byte[] address) {
		super(IPv6_ADDRESS_SIZE, NetAddressType.IPv6);
		bytes = address;
	}

	/**
	 * To array.
	 *
	 * @return the byte[]
	 */
	@Override
	public byte[] toArray() {
		return bytes;
	}

	/**
	 * To compressed IPv6 address string.
	 * <p>
	 * For example the IPv6 address {@code 2001:1234:0000:0000:1A12:0000:0000:1A13}
	 * in compressed form looks like this {@code 2001:1234::1A12:0:0:1A13}.
	 * According to IPv6 address spec, both of these addresses representations
	 * encapsulate the same address. The compressed form is easier to read and
	 * remember if neccessary for humans.
	 * </p>
	 *
	 * @return The IPv6 textual address representation (in compressed format).
	 */
	@Override
	public String toString() {
		return toIp6AddressString(bytes);
	}

	/**
	 * To uncompressed IPv6 address string.
	 * <p>
	 * For example this is the IPv6 uncompressed address
	 * {@code 2001:1234:0000:0000:1A12:0000:0000:1A13} consisting of 8 groups of 2
	 * bytes sets of addresses for a total of 16 bytes or 128 bits. When a group is
	 * all zeros then a single zero will be used. In the case where the number
	 * within a group is led with zeros, those leading zeros may be dropped, such as
	 * {@code 2001:1234:0:0:1A12:0:0:1A13}. In either case, there will be 8 groups
	 * separated by the ':' character.
	 * </p>
	 * * @return The IPv6 textual address representation (in uncompressed format).
	 */
	public String toUncompressedString() {
		return toUncompressedIp6AddressString(bytes);
	}

}
