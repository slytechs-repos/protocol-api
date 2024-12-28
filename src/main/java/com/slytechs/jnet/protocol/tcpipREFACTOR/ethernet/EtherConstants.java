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
 * Interface defining constants for Ethernet frame structure and associated
 * fields. These constants include field offsets, lengths, and other attributes
 * for Ethernet, Logical Link Control (LLC), and Subnetwork Access Protocol
 * (SNAP).
 * 
 * <p>
 * This class cannot be instantiated.
 * </p>
 * 
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface EtherConstants {

	/** Ethernet Header Constants */

	/** Length of the Ethernet header in bytes */
	int ETHER_HEADER_LEN = 14;
	/** Offset of the Ethernet Type field */
	int ETHER_FIELD_TYPE = 12;
	/** Offset of the destination MAC address */
	int ETHER_FIELD_DST = 0;
	/** Offset of the source MAC address */
	int ETHER_FIELD_SRC = 6;
	/** Offset of the preamble */
	int ETHER_FIELD_PREAMBLE = 0;
	/** Length of the preamble in bytes */
	int ETHER_FIELD_LEN_PREAMBLE = 8;
	/** Length of the CRC in bytes */
	int ETHER_FIELD_LEN_CRC = 4;
	/** Length of the destination MAC address in bytes */
	int ETHER_FIELD_DST_LEN = 6;
	/** Length of the source MAC address in bytes */
	int ETHER_FIELD_SRC_LEN = 6;
	/** Length of the Type/Length field in bytes */
	int ETHER_FIELD_LEN_TYPE = 2;
	/** Minimum value to distinguish Type from Length */
	int ETHER_MIN_VALUE_FOR_TYPE = 0x600;

	/** Logical Link Control (LLC) Header Constants */

	/** Length of the LLC header in bytes */
	int LLC_HEADER_LEN = 3;
	/** Offset of the Destination Service Access Point (DSAP) */
	int LLC_FIELD_DSAP = 0;
	/** Offset of the Source Service Access Point (SSAP) */
	int LLC_FIELD_SSAP = 1;
	/** Offset of the Control field */
	int LLC_FIELD_CONTROL = 2;
	/** Frame type for LLC */
	int LLC_TYPE_FRAME = 3;
	/** SNAP frame type */
	int LLC_TYPE_SNAP = 0xAA;
	/** NetWare frame type */
	int LLC_TYPE_NETWARE = 0xFF;
	/** Spanning Tree Protocol frame type */
	int LLC_TYPE_STP = 0x42;

	/** Subnetwork Access Protocol (SNAP) Header Constants */

	/** Length of the SNAP header in bytes */
	int SNAP_HEADER_LEN = 5;
	/** Offset of the Type field in SNAP header */
	int SNAP_FIELD_TYPE = 3;
	/** Offset of the Organizationally Unique Identifier (OUI) */
	int SNAP_FIELD_OUI = 0;
	/** EtherType value for SNAP frames */
	int SNAP_TYPE_ETHER = 4;

}
