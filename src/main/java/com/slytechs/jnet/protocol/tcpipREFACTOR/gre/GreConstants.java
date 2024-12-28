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
package com.slytechs.jnet.protocol.tcpipREFACTOR.gre;

/**
 * Interface defining constants for Generic Routing Encapsulation (GRE). GRE is
 * a tunneling protocol that encapsulates various network layer protocols within
 * virtual point-to-point connections.
 * 
 * <p>
 * These constants include header lengths and bitmasks for various GRE features,
 * such as checksums, keys, and sequence numbers.
 * </p>
 * 
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface GreConstants {

	/**
	 * Length of the GRE header in bytes.
	 */
	int GRE_HEADER_LEN = 2;

	/**
	 * Bitmask for the GRE checksum flag.
	 */
	int GRE_BITMASK_CHKSUM_FLAG = 0;

	/**
	 * Bitmask for the GRE key flag.
	 */
	int GRE_BITMASK_KEY_FLAG = 0;

	/**
	 * Bitmask for the GRE sequence number flag.
	 */
	int GRE_BITMASK_SEQ_FLAG = 0;

}
