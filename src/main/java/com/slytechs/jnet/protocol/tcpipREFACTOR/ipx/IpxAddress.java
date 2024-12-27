/*
 * Sly Technologies Free License
 * 
 * Copyright 2024 Sly Technologies Inc.
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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ipx;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */

import java.nio.ByteBuffer;

import com.slytechs.jnet.protocol.api.address.NetAddress;
import com.slytechs.jnet.protocol.api.address.NetAddressType;

/**
 * An ETH_IPX_SPX address.
 * <p>
 * An ETH_IPX_SPX address is a network addressing scheme used in Novell NetWare
 * networks. It consists of a network number (4 bytes) and a node number (6
 * bytes, typically a MAC address).
 * </p>
 */
public class IpxAddress implements NetAddress {

	public static long getAsLong(int index, ByteBuffer buffer) {
		return buffer.getLong(index) & 0xFFFF_FFFF_FFFF_FFFFL;
	}

	public static IpxAddress get(int index, ByteBuffer buffer) {
		byte[] addr = new byte[IpxAddress.IPX_ADDRESS_SIZE];
		buffer.get(index, addr);
		return new IpxAddress(addr);
	}

	/** The constant IPX_ADDRESS_SIZE. */
	public static final int IPX_ADDRESS_SIZE = 10; // 4 bytes network + 6 bytes node

	/** The constant IPX_ADDR_FIELD_SEPARATOR. */
	private static final char IPX_ADDR_FIELD_SEPARATOR = ':';

	private static StringBuilder appendHex(int v, StringBuilder b) {
		if (v < 0x10)
			b.append('0');

		b.append(Integer.toHexString(v));
		return b;
	}

	public static String toIpxAddressString(byte[] src) {
		return toIpxAddressString(src, 0);
	}

	public static String toIpxAddressString(byte[] src, int offset) {
		if ((src.length - offset) < IPX_ADDRESS_SIZE)
			throw new IllegalArgumentException("src array [%d] at offset [%d] too small for ETH_IPX_SPX address"
					.formatted(src.length, offset));

		StringBuilder b = new StringBuilder();

		// Network number (4 bytes)
		for (int i = 0; i < 4; i++) {
			appendHex(src[offset + i] & 0xFF, b);
		}

		b.append(IPX_ADDR_FIELD_SEPARATOR);

		// Node number (6 bytes)
		for (int i = 4; i < IPX_ADDRESS_SIZE; i++) {
			appendHex(src[offset + i] & 0xFF, b);
			if (i < IPX_ADDRESS_SIZE - 1)
				b.append(IPX_ADDR_FIELD_SEPARATOR);
		}

		return b.toString();
	}

	private final byte[] src;

	public IpxAddress() {
		this(new byte[IPX_ADDRESS_SIZE]);
	}

	public IpxAddress(byte[] src) {
		if (src.length != IPX_ADDRESS_SIZE)
			throw new IllegalArgumentException("Invalid ETH_IPX_SPX address size: " + src.length);
		this.src = src;
	}

	@Override
	public byte[] toArray() {
		return src;
	}

	@Override
	public String toString() {
		return toIpxAddressString(src);
	}

	@Override
	public NetAddressType type() {
		return NetAddressType.IPX;
	}

	@Override
	public int byteSize() {
		return IPX_ADDRESS_SIZE;
	}
}