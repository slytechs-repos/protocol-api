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

import java.util.Objects;
import java.util.function.IntUnaryOperator;

import com.slytechs.jnet.protocol.api.address.NetAddress;
import com.slytechs.jnet.protocol.api.address.NetAddressType;

/**
 * Base class for IPv4 and IPv6 addresses.
 * <p>
 * IP address is a unique identifier assigned to every device that is connected
 * to a network. It is used to route data between devices on the network and to
 * identify the device on the network.
 * </p>
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public abstract sealed class IpAddress implements NetAddress
		permits Ip4Address, Ip6Address {

	/** The Constant IPv4_ADDRESS_SIZE. */
	public static final int IPv4_ADDRESS_SIZE = 4;
	
	/** The Constant IPv6_ADDRESS_SIZE. */
	public static final int IPv6_ADDRESS_SIZE = 16;
	
	/** The Constant IPv6_ADDRESS_STRING_SIZE. */
	public static final int IPv6_ADDRESS_STRING_SIZE = 39;
	
	/** The Constant IP6_ADDR_FIELD_SEPARATOR. */
	private static final char IP6_ADDR_FIELD_SEPARATOR = ':';
	
	/** The Constant IP4_ADDR_FIELD_SEPARATOR. */
	private static final char IP4_ADDR_FIELD_SEPARATOR = '.';

	/**
	 * Converts IPv4 binary address into a string suitable for presentation.
	 *
	 * @param src a byte array representing an IPv4 numeric address
	 * @return a String representing the IPv4 address in textual representation
	 *         format
	 */
	public static String toIp4AddressString(byte[] src) {
		return toIp4AddressString(src, 0);
	}

	/**
	 * Converts IPv4 binary address into a string suitable for presentation.
	 *
	 * @param src    a byte array representing an IPv4 numeric address
	 * @param offset the offset
	 * @return a String representing the IPv4 address in textual representation
	 *         format
	 */
	private static String toIp4AddressString(byte[] src, int offset) {
		if ((src.length - offset) < IPv4_ADDRESS_SIZE)
			throw new IllegalArgumentException("src array [%d] at offset [%d] too small for IPv4 address"
					.formatted(src.length, offset));

		StringBuilder b = new StringBuilder();

		b.append(src[offset + 0] & 0xff)
				.append(IP4_ADDR_FIELD_SEPARATOR)
				.append(src[offset + 1] & 0xff)
				.append(IP4_ADDR_FIELD_SEPARATOR)
				.append(src[offset + 2] & 0xff)
				.append(IP4_ADDR_FIELD_SEPARATOR)
				.append(src[offset + 3] & 0xff);

		return b.toString();
	}

	/**
	 * Convert IPv6 binary address into presentation (printable) format.
	 *
	 * @param src a byte array representing the IPv6 numeric address
	 * @return a String representing an IPv6 address in textual representation
	 *         format
	 */
	public static String toIp6AddressString(byte[] src) {
		if (src.length < IPv6_ADDRESS_SIZE)
			throw new IllegalArgumentException("src array [%d] too small for IPv6 address"
					.formatted(src.length));

		return toIp6AddressString(src, 0);
	}

	/**
	 * To uncompressed ip 6 address string.
	 *
	 * @param src the src
	 * @return the string
	 */
	public static String toUncompressedIp6AddressString(byte[] src) {
		return toUncompressedIp6AddressString(src, 0);
	}

	/**
	 * To uncompressed ip 6 address string.
	 *
	 * @param src    the src
	 * @param offset the offset
	 * @return the string
	 */
	public static String toUncompressedIp6AddressString(byte[] src, int offset) {
		if ((src.length - offset) < IPv6_ADDRESS_SIZE)
			throw new IllegalArgumentException("src array [%d] at offset [%d] too small for IPv6 address"
					.formatted(src.length, offset));

		final IntUnaryOperator USHORT = i -> {
			return Byte.toUnsignedInt(src[offset + i]) << 8 | Byte.toUnsignedInt(src[offset + i + 1]);
		};
		StringBuilder toStringBuffer = new StringBuilder();

		toStringBuffer

				.append(Integer.toHexString(USHORT.applyAsInt(offset + 0)))
				.append(IP6_ADDR_FIELD_SEPARATOR)
				.append(Integer.toHexString(USHORT.applyAsInt(offset + 2)))
				.append(IP6_ADDR_FIELD_SEPARATOR)
				.append(Integer.toHexString(USHORT.applyAsInt(offset + 4)))
				.append(IP6_ADDR_FIELD_SEPARATOR)
				.append(Integer.toHexString(USHORT.applyAsInt(offset + 6)))
				.append(IP6_ADDR_FIELD_SEPARATOR)
				.append(Integer.toHexString(USHORT.applyAsInt(offset + 8)))
				.append(IP6_ADDR_FIELD_SEPARATOR)
				.append(Integer.toHexString(USHORT.applyAsInt(offset + 10)))
				.append(IP6_ADDR_FIELD_SEPARATOR)
				.append(Integer.toHexString(USHORT.applyAsInt(offset + 12)))
				.append(IP6_ADDR_FIELD_SEPARATOR)
				.append(Integer.toHexString(USHORT.applyAsInt(offset + 14)))

		;

		return toStringBuffer.toString();
	}

	/**
	 * To ip 6 address string.
	 *
	 * @param src    the src
	 * @param offset the offset
	 * @return the string
	 */
	public static String toIp6AddressString(byte[] src, int offset) {
		if ((src.length - offset) < 16)
			throw new IllegalArgumentException("src array [%d] at offset [%d] too small for IPv6 address"
					.formatted(src.length, offset));

		final IntUnaryOperator USHORT = i -> {
			return Byte.toUnsignedInt(src[offset + i]) << 8 | Byte.toUnsignedInt(src[offset + i + 1]);
		};

		StringBuilder toStringBuffer = new StringBuilder();

		/* Do compression only once on the first series of zeros */
		int i;
		for (i = 0; i < 16; i += 2) {
			int f = USHORT.applyAsInt(i);

			if (i != 0 && toStringBuffer.charAt(toStringBuffer.length() - 2) != ':')
				toStringBuffer.append(IP6_ADDR_FIELD_SEPARATOR);

			boolean compressed = (i != 0) && (toStringBuffer.charAt(toStringBuffer.length() - 2) == ':');

			if (f != 0)
				toStringBuffer.append(Integer.toHexString(f));

			if (f != 0 && compressed) {
				i += 2;
				break;
			}
		}

		/* Finish the remainder with no compression */
		for (; i < 16; i += 2) {
			toStringBuffer
					.append(IP6_ADDR_FIELD_SEPARATOR)
					.append(Integer.toHexString(USHORT.applyAsInt(i)));
		}

		return toStringBuffer.toString();
	}

	/**
	 * Parses the ip 4 address string.
	 *
	 * @param ipAddress the ip address
	 * @return the byte[]
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	private static byte[] parseIp4AddressString(String ipAddress) throws IllegalArgumentException {
		ipAddress = stripNetmask(ipAddress);

		String[] c = ipAddress.split("\\.");
		if (c.length != 4)
			throw new IllegalArgumentException("invalid IPv4 address format " + ipAddress);

		byte[] result = new byte[4];
		result[0] = (byte) Integer.parseInt(c[0]);
		result[1] = (byte) Integer.parseInt(c[1]);
		result[2] = (byte) Integer.parseInt(c[2]);
		result[3] = (byte) Integer.parseInt(c[3]);

		return result;
	}

	/**
	 * Parses the ip 6 address string.
	 *
	 * @param ipAddress the ip address
	 * @return the byte[]
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	private static byte[] parseIp6AddressString(String ipAddress) throws IllegalArgumentException {
		ipAddress = stripNetmask(ipAddress);

		/**
		 * Check for short hand notation:
		 * 
		 * <pre>
		 * ::
		 * ::1
		 * 64:ff9b::255.255.255.255
		 * </pre>
		 */
		@SuppressWarnings("unused")
		String[] z = ipAddress.split("::");
		String[] c = ipAddress.split("[:-]");
		if (c.length != 4)
			throw new IllegalArgumentException("invalid IPv4 address format " + ipAddress);

		byte[] result = new byte[4];
		result[0] = (byte) Integer.parseInt(c[0]);
		result[1] = (byte) Integer.parseInt(c[1]);
		result[2] = (byte) Integer.parseInt(c[2]);
		result[3] = (byte) Integer.parseInt(c[3]);

		return result;
	}

	/**
	 * Parses the ip address string.
	 *
	 * @param ipAddress the ip address
	 * @return the byte[]
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public static byte[] parseIpAddressString(String ipAddress) throws IllegalArgumentException {
		Objects.requireNonNull(ipAddress, "ipAddress");

		if (ipAddress.contains("."))
			return parseIp4AddressString(ipAddress);
		else
			return parseIp6AddressString(ipAddress);
	}

	/**
	 * Strip netmask.
	 *
	 * @param address the address
	 * @return the string
	 */
	private static String stripNetmask(String address) {
		if (address.indexOf('/') != -1)
			address = address.replaceFirst("^(.+)/.+$", "$1"); // Strip netmask

		return address;
	}

	/**
	 * To string.
	 *
	 * @param src the ip address
	 * @return the string builder
	 */
	public static String toIpAddressString(byte[] src) {
		if ((src.length != IPv4_ADDRESS_SIZE)
				&& (src.length != IPv6_ADDRESS_SIZE))
			throw new IllegalArgumentException("invalid IP address length, must be either 4 or 16");

		return (src.length == 4)
				? toIp4AddressString(src)
				: toIp6AddressString(src);
	}

	/** The type. */
	private final NetAddressType type;
	
	/** The byte size. */
	private final int byteSize;

	/**
	 * Instantiates a new ip address.
	 *
	 * @param byteSize the byte size
	 * @param type     the type
	 */
	protected IpAddress(int byteSize, NetAddressType type) {
		super();
		this.byteSize = byteSize;
		this.type = type;
	}

	/**
	 * Type.
	 *
	 * @return the address type
	 * @see com.slytechs.jnet.protocol.api.address.NetAddress#type()
	 */
	@Override
	public NetAddressType type() {
		return this.type;
	}

	/**
	 * Get IP address as an array.
	 *
	 * @return the IP address
	 */
	@Override
	public abstract byte[] toArray();

	/**
	 * Gets a string with formatted IP address of either IPv4 and IPv6 type.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public abstract String toString();

	/**
	 * Byte size.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.protocol.api.address.NetAddress#byteSize()
	 */
	@Override
	public final int byteSize() {
		return byteSize;
	}
}
