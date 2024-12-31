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
package com.slytechs.jnet.protocol.tcpip.udp;

import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.meta.Meta;

/**
 * User Datagram Protocol (UDP).
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@Meta
public final class Udp extends Header {

	/** The Constant ID. */
	public static final int ID = CoreId.CORE_ID_UDP;

	/**
	 * Instantiates a new udp.
	 */
	public Udp() {
		super(ID);
	}

	/**
	 * The checksum field may be used for error-checking of the header and data.
	 * This field is optional in IPv4, and mandatory in most cases in IPv6. The
	 * field carries all-zeros if unused.
	 *
	 * @return 16-bit checksum of the header and data or zero if not set
	 */
	public int checksum() {
		return UdpStruct.CHECKSUM.getUnsignedShort(buffer());
	}

	/**
	 * Sets a new checksum value.
	 *
	 * @param newChecksum new CRC16 checksum
	 */
	public void checksum(int newChecksum) {
		UdpStruct.CHECKSUM.setInt(newChecksum, buffer());
	}

	/**
	 * Destination port number.This field identifies the receiver's port and is
	 * required. Similar to source port number, if the client is the destination
	 * host then the port number will likely be an ephemeral port number and if the
	 * destination host is the server then the port number will likely be a
	 * well-known port number.
	 *
	 * @return port number
	 */
	@Meta
	public int dstPort() {
		return UdpStruct.DST_PORT.getUnsignedShort(buffer());
	}

	/**
	 * Sets a new port number.
	 *
	 * @param dstPort the dst port
	 */
	public void dstPort(int dstPort) {
		UdpStruct.DST_PORT.setInt(dstPort, buffer());
	}

	/**
	 * Length field. This field specifies the length in bytes of the UDP header and
	 * UDP data. The minimum length is 8 bytes, the length of the header. The field
	 * size sets a theoretical limit of 65,535 bytes (8-byte header + 65,527 bytes
	 * of data) for a UDP datagram. However the actual limit for the data length,
	 * which is imposed by the underlying IPv4 protocol, is 65,507 bytes (65,535
	 * bytes − 8-byte UDP header − 20-byte IP header). Using IPv6 jumbograms it is
	 * possible to have UDP datagrams of size greater than 65,535 bytes. RFC 2675
	 * specifies that the length field is set to zero if the length of the UDP
	 * header plus UDP data is greater than 65,535.
	 *
	 * @return the int
	 */
	@Meta
	public int length() {
		return UdpStruct.LENGTH.getUnsignedShort(buffer());
	}

	/**
	 * Set a new length field.
	 *
	 * @param length the length
	 */
	public void length(int length) {
		UdpStruct.LENGTH.setInt(length, buffer());
	}

	/**
	 * Source port number. This field identifies the sender's port, when used, and
	 * should be assumed to be the port to reply to if needed. If not used, it
	 * should be zero. If the source host is the client, the port number is likely
	 * to be an ephemeral port. If the source host is the server, the port number is
	 * likely to be a well-known port number from 0 to 1023
	 *
	 * @return the int
	 */
	@Meta
	public int srcPort() {
		return UdpStruct.SRC_PORT.getUnsignedShort(buffer());
	}

	/**
	 * Set a new port number.
	 *
	 * @param srcPort the src port
	 */
	public void srcPort(int srcPort) {
		UdpStruct.SRC_PORT.setInt(srcPort, buffer());
	}

}
