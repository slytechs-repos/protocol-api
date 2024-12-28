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
package com.slytechs.jnet.protocol.tcpipREFACTOR.sctp;

/**
 * Interface defining constants for the Stream Control Transmission Protocol
 * (SCTP). SCTP is a transport-layer protocol used for message-oriented
 * communication with support for multihoming and multistreaming.
 * 
 * <p>
 * These constants include header lengths and field offsets specific to SCTP
 * packet headers.
 * </p>
 * 
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface SctpConstants {

	/**
	 * Length of the SCTP header in bytes.
	 */
	int SCTP_HEADER_LEN = 12;

	/**
	 * Offset of the Source Port field in the SCTP header.
	 */
	int SCTP_FIELD_SRC_PORT = 0;

	/**
	 * Offset of the Destination Port field in the SCTP header.
	 */
	int SCTP_FIELD_DST_PORT = 2;

	/**
	 * Offset of the Verification Tag field in the SCTP header.
	 */
	int SCTP_FIELD_VERIFICATION_TAG = 4;

	/**
	 * Offset of the Checksum field in the SCTP header.
	 */
	int SCTP_FIELD_CHECKSUM = 8;

}
