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

import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;

/**
 * IPv4 record route option header.
 * <p>
 * The IPv4 record route option is used to record the IP addresses of the
 * routers that a packet has passed through. The record route option is a 3-byte
 * option with a type of 7. The first byte of the option is the pointer, which
 * specifies the byte position where the IP address of the current router should
 * be recorded. The second byte of the option is the length, which specifies the
 * length of the option in bytes. The third byte of the option is the IP address
 * of the current router.
 * </p>
 */
public final class Ip4RecordRouteOption extends Ip4Option {

	/** The IPv4 record route option header ID constant. */
	public static final int ID = Ip4IdOptions.IPv4_ID_OPT_RR;

	/**
	 * IPv4 record route option header.
	 */
	public Ip4RecordRouteOption() {
		super(ID);
	}

	/**
	 * The pointer field is a 1-byte field that specifies the byte position where
	 * the IP address of the current router should be recorded.
	 * 
	 * @return byte index to the next IP address to be recorded
	 */
	public int pointer() {
		return Byte.toUnsignedInt(buffer().get(2));
	}

	/**
	 * Calculates the number of IP for route IP address recorded in this option
	 * header.
	 *
	 * @return number of router IP addresses stored
	 */
	@Meta(MetaType.ATTRIBUTE)
	public int ipAddressCount() {
		int count = (optionDataLength() - 3) / 4;

		return count;
	}

	/**
	 * The IP address field is a 4-byte field that contains the IP address of the
	 * current router.
	 *
	 * @param index the route index
	 * @return the IPv4 4-byte addresses
	 */
	public byte[] ipAddress(int index) {
		byte[] dst = new byte[IpAddress.IPv4_ADDRESS_SIZE];

		ipAddress(index, dst, 0);

		return dst;
	}

	/**
	 * The IP address field is a 4-byte field that contains the IP address of the
	 * current router.
	 *
	 * @param index the route index
	 * @param dst   the destination array where to store IP route addresses
	 * @return the IPv4 4-byte addresses
	 */
	public int ipAddress(int index, byte[] dst) {
		return ipAddress(index, dst, 0);
	}

	/**
	 * The IP address field is a 4-byte field that contains the IP address of the
	 * current router.
	 *
	 * @param index  the route index
	 * @param dst    the destination array where to store IP route addresses
	 * @param offset the offset into the destination array
	 * @return the IPv4 4-byte addresses
	 */
	public int ipAddress(int index, byte[] dst, int offset) {
		int doff = index * 4 + 3;
		if (doff < 3 || doff > optionDataLength())
			throw new IndexOutOfBoundsException(index);

		buffer().get(doff, dst, offset, IpAddress.IPv4_ADDRESS_SIZE);

		return IpAddress.IPv4_ADDRESS_SIZE;
	}

	/**
	 * The IP address field is a 4-byte field that contains the IP address of the
	 * current router.
	 *
	 * @param index the route index
	 * @return the IPv4 ip address object
	 */
	public Ip4Address ipAddressAsIpAddress(int index) {
		return new Ip4Address(ipAddress(index));
	}
}