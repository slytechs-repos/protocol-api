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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet;

import java.nio.ByteBuffer;
import java.util.Optional;

import com.slytechs.jnet.protocol.api.address.NetAddress;
import com.slytechs.jnet.protocol.api.address.NetAddressType;
import com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.impl.MacOuiAssignments;
import com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.impl.MacOuiAssignments.OuiEntry;

/**
 * A MAC address.
 * <p>
 * A MAC address, or Media Access Control address, is a unique identifier
 * assigned to a network interface controller (NIC) for use as a network address
 * in communications within a network segment. This use is common in most IEEE
 * 802 networking technologies, including Ethernet, Wi-Fi, and Bluetooth. Within
 * the Open Systems Interconnection (OSI) network model, MAC addresses are used
 * in the medium access control protocol sublayer of the data link layer. As
 * typically represented, MAC addresses are recognizable as six groups of two
 * hexadecimal digits, separated by hyphens, colons, or without a separator.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public class MacAddress implements NetAddress {
	
	public static long getAsLong(int index, ByteBuffer buffer) {
		return (buffer.getLong(index) >> 16) & 0x0000_FFFF_FFFF_FFFFL;
	}
	
	public static MacAddress get(int index, ByteBuffer buffer) {
		byte[] addr = new byte[MacAddress.MAC_ADDRESS_SIZE];

		buffer.get(index, addr);

		return new MacAddress(addr);
	}

	/** The Constant MAC_ADDRESS_SIZE. */
	public static final int MAC_ADDRESS_SIZE = 6;

	/** The Constant MAC_ADDR_FIELD_SEPARATOR. */
	private static final char MAC_ADDR_FIELD_SEPARATOR = ':';

	/** The Constant MAC_OUI_FIELD_SEPARATOR. */
	private static final char MAC_OUI_FIELD_SEPARATOR = '_';

	/**
	 * Read oui prefix array.
	 *
	 * @param src    the src
	 * @param dst    the dst
	 * @param offset the offset
	 * @return the int
	 */
	static int parseOuiPrefixIntoArray(String src, byte[] dst, int offset) {
		int indx = src.indexOf(MAC_OUI_FIELD_SEPARATOR);
		if (indx == -1)
			return 0;

		String ouiName = src.substring(0, indx);

		Optional<OuiEntry> ouiOptional = MacOuiAssignments.reverseLookupOuiName(ouiName);
		if (ouiOptional.isEmpty())
			return 0;

		OuiEntry oui = ouiOptional.get();
		var prefix = oui.getPrefix();
		int prefixLen = oui.getMask() / 8;

		for (int i = 0; i < prefixLen; i++)
			dst[offset + i] = prefix[i];

		return prefixLen;
	}

	/**
	 * Write oui prefix string.
	 *
	 * @param src    the src
	 * @param offset the offset
	 * @param dst    the dst
	 * @return the int
	 */
	static int lookupOuiPrefixString(byte[] src, int offset, StringBuilder dst) {
		if (offset != 0)
			throw new UnsupportedOperationException("only offset of 0 is currently supported");

		Optional<OuiEntry> ouiOptional = MacOuiAssignments.lookupManufacturerOui(src);
		if (ouiOptional.isEmpty())
			return 0;

		OuiEntry oui = ouiOptional.get();
		var prefixLen = oui.getMask() / 8;

		dst.append(oui.getName());

		return prefixLen;
	}

	/**
	 * Parses the oui mac address.
	 *
	 * @param s the s
	 * @return the byte[]
	 */
	public static byte[] parseOuiMacAddress(String s) {
		byte[] dst = new byte[MAC_ADDRESS_SIZE];

		int j = parseOuiPrefixIntoArray(s, dst, 0);
		int idx = j > 0 ? s.indexOf(MAC_OUI_FIELD_SEPARATOR) + 1 : 0;

		String[] c = s.substring(idx).split(":");

		for (int i = 0; (i + j) < MAC_ADDRESS_SIZE; i++)
			dst[j + i] = (byte) Integer.parseInt(c[i], 16);

		return dst;
	}

	/**
	 * Parses the mac address.
	 *
	 * @param s the s
	 * @return the byte[]
	 */
	public static byte[] parseMacAddress(String s) {
		String[] c = s.strip().split(":");
		if (c.length != MAC_ADDRESS_SIZE)
			throw new IllegalArgumentException("invalid MAC address format in string [%s]"
					.formatted(s));

		byte[] dst = new byte[MAC_ADDRESS_SIZE];

		for (int i = 0; i < MAC_ADDRESS_SIZE; i++)
			dst[i] = (byte) Integer.parseInt(c[i], 16);

		return dst;
	}

	/**
	 * Append hex.
	 *
	 * @param v the v
	 * @param b the b
	 * @return the string builder
	 */
	private static StringBuilder appendHex(int v, StringBuilder b) {
		if (v < 0x10)
			b.append('0');

		b.append(Integer.toHexString(v));

		return b;
	}

	/**
	 * To mac address string.
	 *
	 * @param src the src
	 * @return the string
	 */
	public static String toMacAddressString(byte[] src) {
		return toMacAddressString(src, 0);
	}

	/**
	 * To mac address string.
	 *
	 * @param src    the src
	 * @param offset the offset
	 * @return the string
	 */
	public static String toMacAddressString(byte[] src, int offset) {
		if ((src.length - offset) < MAC_ADDRESS_SIZE)
			throw new IllegalArgumentException("src array [%d] at offset [%d] too small for MAC address"
					.formatted(src.length, offset));

		StringBuilder toStringBuffer = new StringBuilder();

		appendHex(src[offset + 0] & 0xFF, toStringBuffer)
				.append(MAC_ADDR_FIELD_SEPARATOR);

		appendHex(src[offset + 1] & 0xFF, toStringBuffer)
				.append(MAC_ADDR_FIELD_SEPARATOR);

		appendHex(src[offset + 2] & 0xFF, toStringBuffer)
				.append(MAC_ADDR_FIELD_SEPARATOR);

		appendHex(src[offset + 3] & 0xFF, toStringBuffer)
				.append(MAC_ADDR_FIELD_SEPARATOR);

		appendHex(src[offset + 4] & 0xFF, toStringBuffer)
				.append(MAC_ADDR_FIELD_SEPARATOR);

		appendHex(src[offset + 5] & 0xFF, toStringBuffer);

		return toStringBuffer.toString();
	}

	/**
	 * To oui mac address string.
	 *
	 * @param src the src
	 * @return the string
	 */
	public static String toOuiMacAddressString(byte[] src) {
		return toOuiMacAddressString(src, 0);
	}

	/**
	 * To oui mac address string.
	 *
	 * @param src    the src
	 * @param offset the offset
	 * @return the string
	 */
	public static String toOuiMacAddressString(byte[] src, int offset) {
		StringBuilder toStringBuffer = new StringBuilder();

		int i = lookupOuiPrefixString(src, offset, toStringBuffer);
		if (i > 0)
			toStringBuffer.append(MAC_OUI_FIELD_SEPARATOR);

		for (; i < MAC_ADDRESS_SIZE; i++) {
			appendHex(src[offset + i] & 0xFF, toStringBuffer);

			if (i < (MAC_ADDRESS_SIZE - 1))
				toStringBuffer.append(MAC_ADDR_FIELD_SEPARATOR);
		}

		return toStringBuffer.toString();
	}

	/** The src. */
	private final byte[] src;

	/**
	 * Instantiates a new mac address.
	 */
	public MacAddress() {
		this(new byte[MAC_ADDRESS_SIZE]);
	}

	/**
	 * Instantiates a new mac address.
	 *
	 * @param src the destination
	 */
	public MacAddress(byte[] src) {
		this.src = src;
	}

	/**
	 * To array.
	 *
	 * @return the byte[]
	 * @see com.slytechs.jnet.protocol.api.address.NetAddress#toArray()
	 */
	@Override
	public byte[] toArray() {
		return src;
	}

	/**
	 * To oui string.
	 *
	 * @return the string
	 */
	public String toOuiString() {
		return toOuiMacAddressString(src, 0);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return toMacAddressString(src);
	}

	/**
	 * Type.
	 *
	 * @return the address type
	 * @see com.slytechs.jnet.protocol.api.address.NetAddress#type()
	 */
	@Override
	public NetAddressType type() {
		return NetAddressType.MAC;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.address.NetAddress#byteSize()
	 */
	@Override
	public int byteSize() {
		return MAC_ADDRESS_SIZE;
	}

}
