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
package com.slytechs.jnet.protocol.tcpip.mpls;

/**
 * Interface defining constants for the Multiprotocol Label Switching (MPLS)
 * protocol. MPLS is a data-carrying technique for high-performance
 * telecommunications networks that directs data from one network node to the
 * next based on short path labels rather than long network addresses.
 * 
 * <p>
 * These constants define header lengths, field offsets, and label operations
 * specific to MPLS processing.
 * </p>
 * 
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface MplsConstants {

	/**
	 * Length of the MPLS header in bytes.
	 */
	int MPLS_HEADER_LEN = 4;

	/**
	 * Offset of the MPLS Label field in the MPLS header.
	 */
	int MPLS_FIELD_LABEL = 0;

	/**
	 * Offset of the MPLS Experimental (EXP) field in the MPLS header.
	 */
	int MPLS_FIELD_EXP = 20;

	/**
	 * Offset of the MPLS Bottom of Stack (BoS) field in the MPLS header.
	 */
	int MPLS_FIELD_BOS = 23;

	/**
	 * Offset of the MPLS Time-to-Live (TTL) field in the MPLS header.
	 */
	int MPLS_FIELD_TTL = 24;

	/**
	 * Bitmask for the MPLS Bottom of Stack (BoS) flag.
	 */
	int MPLS_BITMASK_BOTTOM = 0x01;

}
