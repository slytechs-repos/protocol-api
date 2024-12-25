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

import com.slytechs.jnet.platform.api.memory.layout.BinaryLayout;
import com.slytechs.jnet.platform.api.memory.layout.BitField;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int16;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int32;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int64;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int8;

/**
 * The Enum IpfLayout structure for a IpfDescriptor type.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public enum IpfReassemblyLayout implements BitField.Proxy {

	FLAGS("flags"),
	IP_TYPE("ip_type"),
	IP_IS_REASSEMBLED("ip_is_reassembled"),
	IP_IS_COMPLETE("ip_is_complete"),
	IP_IS_TIMEOUT("ip_is_timeout"),
	IP_IS_HOLE("ip_is_hole"),
	IP_IS_OVERLAP("ip_is_overlap"),
	TABLE_SIZE("table_size"),
	REASSEMBLED_BYTES("reassembled_bytes"),
	REASSEMBLED_MILLI("reassembled_milli"),
	HOLE_BYTES("hole_bytes"),
	OVERLAP_BYTES("overlap_bytes"),

	FRAG_PKT_INDEX("frag_pkt_index"),
	FRAG_OFFSET("frag_offset"),
	FRAG_LENGTH("frag_length"),
	FRAG_OVERLAY_BYTES("frag_overlay_bytes"),
	;

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant TYPE2_STRUCT. */
		private static final BinaryLayout STRUCT = structLayout(

				/* Word0 */
				unionLayout(
						structLayout(
								Int8.BITS_01.withName("ip_type"),
								Int8.BITS_01.withName("ip_is_reassembled"),
								Int8.BITS_01.withName("ip_is_complete"),
								Int8.BITS_01.withName("ip_is_timeout"),
								Int8.BITS_01.withName("ip_is_hole"),
								Int8.BITS_01.withName("ip_is_overlap"),
								Int8.BITS_02),
						Int8.BITS_08.withName("flags")),

				Int8.BITS_08.withName("table_size"),
				Int16.BITS_16.withName("reassembled_bytes"),

				/* Word1 */
				Int16.BITS_16.withName("hole_bytes"),
				Int16.BITS_16.withName("overlap_bytes"),

				/* Word2 */
				Int32.BITS_32.withName("reassembled_milli"),

				/* Word3&4 */
				PredefinedLayout.ADDRESS.withName("buffer"),

				/* Word5+ */
				sequenceLayout(32, structLayout(

						Int64.BITS_64.withName("frag_pkt_index"),
						Int16.BITS_16.withName("frag_offset"),
						Int16.BITS_16.withName("frag_length"),

						Int16.BITS_16.withName("frag_overlay_bytes"),
						Int16.BITS_16

				).withName("frag_table"))

		);
	}

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new type 2 layout.
	 *
	 * @param path the path
	 */
	IpfReassemblyLayout(String path) {
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
