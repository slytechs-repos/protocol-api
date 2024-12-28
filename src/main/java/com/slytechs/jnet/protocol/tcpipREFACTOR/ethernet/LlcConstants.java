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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet;

/**
 * Interface defining constants for Logical Link Control (LLC) protocol. LLC is
 * a sublayer of the Data Link Layer (Layer 2) in the OSI model, responsible for
 * framing and error checking for various network protocols.
 * 
 * <p>
 * These constants include field offsets, header lengths, and specific LLC frame
 * types.
 * </p>
 * 
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface LlcConstants {

	/**
	 * Length of the LLC header in bytes.
	 */
	int LLC_HEADER_LEN = 3;

	/**
	 * Offset of the Destination Service Access Point (DSAP) field.
	 */
	int LLC_FIELD_DSAP = 0;

	/**
	 * Offset of the Source Service Access Point (SSAP) field.
	 */
	int LLC_FIELD_SSAP = 1;

	/**
	 * Offset of the Control field in the LLC header.
	 */
	int LLC_FIELD_CONTROL = 2;

	/**
	 * Frame type for standard LLC frames.
	 */
	int LLC_TYPE_FRAME = 3;

	/**
	 * SNAP frame type indicating Subnetwork Access Protocol.
	 */
	int LLC_TYPE_SNAP = 0xAA;

	/**
	 * NetWare frame type for Novell NetWare protocols.
	 */
	int LLC_TYPE_NETWARE = 0xFF;

	/**
	 * Spanning Tree Protocol (STP) frame type.
	 */
	int LLC_TYPE_STP = 0x42;

}
