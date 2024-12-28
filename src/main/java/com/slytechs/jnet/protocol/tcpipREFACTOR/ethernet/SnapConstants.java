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
 * Interface defining constants for Subnetwork Access Protocol (SNAP). SNAP is a
 * protocol used in the Logical Link Control (LLC) sublayer to provide
 * extensibility and support for various network layer protocols.
 * 
 * <p>
 * These constants include field offsets, header lengths, and specific SNAP
 * types used in network data processing.
 * </p>
 * 
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface SnapConstants {

	/**
	 * Length of the SNAP header in bytes.
	 */
	int SNAP_HEADER_LEN = 5;

	/**
	 * Offset of the Type field in the SNAP header.
	 */
	int SNAP_FIELD_TYPE = 3;

	/**
	 * Offset of the Organizationally Unique Identifier (OUI) field.
	 */
	int SNAP_FIELD_OUI = 0;

	/**
	 * EtherType value for Ethernet SNAP frames.
	 */
	int SNAP_TYPE_ETHER = 4;

}
