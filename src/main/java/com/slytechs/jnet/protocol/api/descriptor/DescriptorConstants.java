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
package com.slytechs.jnet.protocol.api.descriptor;

/**
 * Interface defining constants for binary descriptors used in network data
 * processing. Descriptors are data structures that precede network packets,
 * frames, streams, and other protocol-generated data. They encode metadata
 * about the data they describe, such as protocol headers, fragmentation
 * details, and more.
 * 
 * <p>
 * This interface provides constants for three main types of descriptors:
 * </p>
 * <ul>
 * <li><b>Type 1 Descriptor:</b> A constant-length descriptor providing basic
 * Layer 2 (L2) and Layer 3 (L3) information about packets.</li>
 * <li><b>Type 2 Descriptor:</b> A variable-length descriptor containing a full
 * packet dissection where all protocol headers within a packet are
 * recorded.</li>
 * <li><b>IPF Descriptor:</b> A descriptor encoding information about IP
 * fragmentation and reassembly processes.</li>
 * </ul>
 * 
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface DescriptorConstants {

	/** Descriptor Flags */

	/** Packet includes a CRC field */
	int DESC_PKT_FLAG_CRC = 0x0001;
	/** Packet includes a preamble field */
	int DESC_PKT_FLAG_PREAMBLE = 0x0002;

	/** Type 1 Descriptor Constants */

	/** Fixed byte size of a Type 1 descriptor */
	int DESC_TYPE1_BYTE_SIZE = 16;

	/** Type 2 Descriptor Constants */

	/** Minimum byte size of a Type 2 descriptor */
	int DESC_TYPE2_BYTE_SIZE_MIN = 28;
	/** Byte size of a single record within a Type 2 descriptor */
	int DESC_TYPE2_RECORD_BYTE_SIZE = 8;
	/** Maximum number of records within a Type 2 descriptor */
	int DESC_TYPE2_RECORD_MAX_COUNT = 16;
	/** Maximum byte size of a Type 2 descriptor */
	int DESC_TYPE2_BYTE_SIZE_MAX = DESC_TYPE2_BYTE_SIZE_MIN
			+ (DESC_TYPE2_RECORD_MAX_COUNT * DESC_TYPE2_RECORD_BYTE_SIZE);

	/** IP Fragmentation and Reassembly (IPF) Descriptor Constants */

	/** Offset for the fragmentation key */
	int DESC_IPF_FRAG_KEY = 12;
	/** Byte size of an IPv4 fragmentation key */
	int DESC_IPF_FRAG_IPv4_KEY_BYTE_SIZE = 12;
	/** Byte size of an IPv6 fragmentation key */
	int DESC_IPF_FRAG_IPv6_KEY_BYTE_SIZE = 36;
	/** Total byte size of an IPF fragment descriptor */
	int DESC_IPF_FRAG_BYTE_SIZE = DESC_IPF_FRAG_KEY + DESC_IPF_FRAG_IPv6_KEY_BYTE_SIZE;
	/** Minimum byte size for IP reassembly metadata */
	int DESC_IPF_REASSEMBLY_BYTE_MIN_SIZE = 20;
	/** Byte size of a single reassembly record */
	int DESC_IPF_REASSEMBLY_RECORD_SIZE = 16;
	/** Total byte size of an IP reassembly descriptor */
	int DESC_IPF_REASSEMBLY_BYTE_SIZE = DESC_IPF_REASSEMBLY_BYTE_MIN_SIZE
			+ DESC_IPF_REASSEMBLY_RECORD_SIZE * 32;

}
