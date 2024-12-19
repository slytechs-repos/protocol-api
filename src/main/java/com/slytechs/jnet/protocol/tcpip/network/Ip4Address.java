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

import com.slytechs.jnet.protocol.api.common.NetAddressType;

/**
 * IP version 4 address.
 * <p>
 * IPv4 addresses are used to identify devices on a network. They are 32-bit
 * numbers that are written in dotted-decimal format, such as 192.168.1.1. The
 * first two numbers identify the network, and the last two numbers identify the
 * device on the network.
 * </p>
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public final class Ip4Address extends IpAddress {

	/** The bytes. */
	private final byte[] bytes;

	/**
	 * Instantiates a new IP v4 address with pre-allocated array to store the
	 * address.
	 */
	public Ip4Address() {
		this(new byte[IPv4_ADDRESS_SIZE]);
	}

	/**
	 * Instantiates a new IP v4 address with supplied storage array.
	 *
	 * @param address the address
	 */
	public Ip4Address(byte[] address) {
		super(IPv4_ADDRESS_SIZE, NetAddressType.IPv4);
		this.bytes = address;

		if (address.length != 4)
			throw new IllegalArgumentException("invalid IPv4 address length " + address.length);

	}

	/**
	 * Get IP address as an array.
	 *
	 * @return the IP address
	 */
	@Override
	public byte[] toArray() {
		return bytes;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toIp4AddressString(bytes);
	}
}
