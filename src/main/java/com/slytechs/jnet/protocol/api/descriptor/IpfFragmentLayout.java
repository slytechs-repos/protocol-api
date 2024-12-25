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
package com.slytechs.jnet.protocol.api.descriptor;

import static com.slytechs.jnet.platform.api.memory.layout.BinaryLayout.*;
import static com.slytechs.jnet.protocol.tcpip.constants.CoreConstants.*;

import com.slytechs.jnet.platform.api.memory.layout.ArrayField;
import com.slytechs.jnet.platform.api.memory.layout.BinaryLayout;
import com.slytechs.jnet.platform.api.memory.layout.BitField;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int16;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int8;
import com.slytechs.jnet.protocol.tcpip.network.IpAddress;

/**
 * The Enum IpfLayout structure for a IpfDescriptor type.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
enum IpfFragmentLayout implements BitField.Proxy {

	/* Word0 */
	IP_TYPE("ip_type"),
	IP_IS_FRAG("ip_is_frag"),
	IP_IS_LAST("ip_is_last"),
	IP_IS_OVERLAP("ip_is_overlap"),
	IP_IS_DUPLICATE("ip_is_duplicate"),
	IP_HDR_OFFSET("ip_hdr_offset"),
	IP_HDR_LEN("ip_hdr_len"),
	IP_NEXT("next_header"),

	/* Word1 */
	FIELD_FRAG_OFFSET("field_frag_offset"),
	FIELD_IDENTIFIER("identifier"),

	/* Word2 */
	FRAG_DATA_OFFSET("frag_data_offset"),
	FRAG_DATA_LEN("frag_data_len"),

	;

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant TYPE2_STRUCT. */
		private static final BinaryLayout STRUCT = structLayout(

				/* Word0 */
				Int8.BITS_01.withName("ip_type"), // 0 = IPv4, 1 = IPv6
				Int8.BITS_01.withName("ip_is_frag"),
				Int8.BITS_01.withName("ip_is_last"),
				Int8.BITS_01.withName("ip_is_overlap"),
				Int8.BITS_01.withName("ip_is_duplicate"),
				Int8.BITS_03,
				Int8.BITS_08.withName("ip_hdr_offset"), // Start of IP hdr
				Int8.BITS_08.withName("ip_hdr_len"), // Len of hdr + required opts
				Int8.BITS_08,

				/* Word1 */
				Int16.BITS_16.withName("field_frag_offset"), // Hdr field/opt value
				Int16.BITS_16, // Hdr field value

				/* Word2 */
				Int16.BITS_16.withName("frag_data_offset"), // Frag data calculated start
				Int16.BITS_16.withName("frag_data_len"), // Frag data calculated length

				unionLayout(
						structLayout(
								Int16.BITS_16.withName("identifier"),
								Int16.BITS_16.withName("next_header"),
								unionLayout(
										structLayout(
												sequenceLayout(IpAddress.IPv4_ADDRESS_SIZE, Int8.BITS_08).withName("ip4_src"),
												sequenceLayout(IpAddress.IPv4_ADDRESS_SIZE, Int8.BITS_08).withName("ip4_dst")),
										structLayout(
												sequenceLayout(IpAddress.IPv6_ADDRESS_SIZE, Int8.BITS_08).withName("ip6_src"),
												sequenceLayout(IpAddress.IPv6_ADDRESS_SIZE, Int8.BITS_08).withName("ip6_dst"))

								))),
				sequenceLayout(DESC_IPF_FRAG_IPv6_KEY_BYTE_SIZE, Int8.BITS_08).withName("key_bytes")

		);
	}

	public static final ArrayField KEY_IPv4_SRC = Struct.STRUCT.arrayField("ip4_src");
	public static final ArrayField KEY_IPv4_DST = Struct.STRUCT.arrayField("ip4_dst");
	public static final ArrayField KEY_IPv6_SRC = Struct.STRUCT.arrayField("ip6_src");
	public static final ArrayField KEY_IPv6_DST = Struct.STRUCT.arrayField("ip6_dst");
	public static final ArrayField KEY_BYTES = Struct.STRUCT.arrayField("key_bytes");

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new type 2 layout.
	 *
	 * @param path the path
	 */
	IpfFragmentLayout(String path) {
		this.field = Struct.STRUCT.bitField(path);
	}

	/**
	 * Proxy bit field.
	 *
	 * @return the bit field
	 * @see com.slytechs.jnet.jnetruntime.internal.layout.BitField.Proxy#proxyBitField()
	 */
	@Override
	public BitField proxyBitField() {
		return field;
	}

}
