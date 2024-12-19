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

import static com.slytechs.jnet.jnetruntime.internal.layout.BinaryLayout.*;

import com.slytechs.jnet.jnetruntime.internal.layout.BinaryLayout;
import com.slytechs.jnet.jnetruntime.internal.layout.BitField;
import com.slytechs.jnet.jnetruntime.internal.layout.PredefinedLayout.Int16;
import com.slytechs.jnet.jnetruntime.internal.layout.PredefinedLayout.Int32;
import com.slytechs.jnet.jnetruntime.internal.layout.PredefinedLayout.Int64;
import com.slytechs.jnet.jnetruntime.internal.layout.PredefinedLayout.Int8;

/**
 * IPF tracking descriptor attached to IP fragment packets. Provides information
 * about each IP fragment and reassembled data-gram information.
 * <p>
 * All the flags and counters are snapshots at the time the IP fragment was
 * processed and dispatched.
 * </p>
 * <p>
 * The constant {@code FRAG_PKT_INDEX} is an index into the packet stream of
 * where the fully reassembled IP data-gram resides. The frameNo of the
 * reassembled datagram may not be known during the reassembly process until all
 * of the fragments are processed, for this reason the value of the packet index
 * will be set to 0 initially and should be set post fragment reassembly in user
 * handler, the IP fragment packets are retained.
 * </p>
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public enum IpfTrackingLayout implements BitField.Proxy {

	FLAGS("flags"),
	IP_IS_REASSEMBLED("ip_is_reassembled"),
	IP_IS_COMPLETE("ip_is_complete"),
	IP_IS_TIMEOUT("ip_is_timeout"),
	IP_IS_HOLE("ip_is_hole"),
	IP_IS_OVERLAP("ip_is_overlap"),
	TABLE_SIZE("table_size"),
	REASSEMBLED_BYTES("reassembled_bytes"),
	HOLE_BYTES("hole_bytes"),
	OVERLAP_BYTES("overlap_bytes"),
	REASSEMBLED_MILLI("reassembled_milli"),

	FRAG_PKT_INDEX("frag_pkt_index"),
	FRAG_OFFSET("frag_offset"),
	FRAG_LENGTH("frag_length"),

	;

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant TYPE2_STRUCT. */
		private static final BinaryLayout STRUCT = structLayout(

				/* Word0&1 */
				Int64.BITS_64.withName("reassembled_milli"),

				/* Word2 */
				Int16.BITS_16.withName("hole_bytes"),
				Int16.BITS_16.withName("overlap_bytes"),

				/* Word2 */
				Int16.BITS_16.withName("reassembled_bytes"),
				unionLayout(
						structLayout(
								Int8.BITS_01.withName("ip_is_reassembled"),
								Int8.BITS_01.withName("ip_is_complete"),
								Int8.BITS_01.withName("ip_is_timeout"),
								Int8.BITS_01.withName("ip_is_hole"),
								Int8.BITS_01.withName("ip_is_overlap"),
								Int8.BITS_03),
						Int8.BITS_08.withName("flags")),
				Int8.BITS_08.withName("table_size"),

				/* Word3+ */
				sequenceLayout(32, structLayout(

						Int64.BITS_64.withName("frag_pkt_index"),
						Int16.BITS_16.withName("frag_offset"),
						Int16.BITS_16.withName("frag_length"),

						Int32.BITS_32

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
	IpfTrackingLayout(String path) {
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
