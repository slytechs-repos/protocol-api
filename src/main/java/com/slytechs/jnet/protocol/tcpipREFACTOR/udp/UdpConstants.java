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
package com.slytechs.jnet.protocol.tcpipREFACTOR.udp;

/**
 * Interface defining constants for the User Datagram Protocol (UDP). UDP is a
 * core protocol of the Internet Protocol Suite, enabling connectionless
 * communication between applications.
 * 
 * <p>
 * This interface includes constants for UDP header fields and their offsets.
 * </p>
 * 
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface UdpConstants {

	/**
	 * Length of the UDP header in bytes.
	 */
	int UDP_HEADER_LEN = 8;

	/**
	 * Offset of the Source Port field in the UDP header.
	 */
	int UDP_FIELD_SRC_PORT = 0;

	/**
	 * Offset of the Destination Port field in the UDP header.
	 */
	int UDP_FIELD_DST_PORT = 2;

	/**
	 * Offset of the Length field in the UDP header.
	 */
	int UDP_FIELD_LENGTH = 4;

	/**
	 * Offset of the Checksum field in the UDP header.
	 */
	int UDP_FIELD_CHECKSUM = 6;

}
