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

import com.slytechs.jnet.platform.api.memory.layout.BinaryLayout;
import com.slytechs.jnet.platform.api.memory.layout.BitField;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int16;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int32;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int64;

/**
 * Type2 struct/layout definition.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
enum Type2DescriptorLayout implements BitField.Proxy {

	/** The timestamp. */
	TIMESTAMP("timestamp"),

	/** The caplen. */
	CAPLEN("caplen"),

	/** is l3 a fragment. */
	L3_IS_FRAG("l3_is_frag"),

	/** is l3 the last fragment. */
	L3_LAST_FRAG("l3_last_frag"),

	/** The color. */
	COLOR("color"),

	/** The hash24. */
	HASH24("hash24"),

	/** The wirelen. */
	WIRELEN("wirelen"),

	/** The l2 type. */
	L2_TYPE("l2_type"),

	/** The rx port. */
	RX_PORT("rx_port"),

	/** The tx port. */
	TX_PORT("tx_port"),

	/** The hash type. */
	HASH_TYPE("hash_type"),

	/** The tx now. */
	TX_NOW("tx_now"),

	/** The tx ignore. */
	TX_IGNORE("tx_ignore"),

	/** The tx crc override. */
	TX_CRC_OVERRIDE("tx_crc_override"),

	/** The tx set clock. */
	TX_SET_CLOCK("tx_set_clock"),

	/** The record count. */
	RECORD_COUNT("record_count"),

	/** The bitmask. */
	BITMASK("bitmask"),

	/** The record. */
	RECORD("record"),

	/** The hash32. */
	HASH32("hash32"),

	/** The array. */
	ARRAY("array"),

	;

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant TYPE2_STRUCT. */
		private static final BinaryLayout TYPE2_STRUCT = unionLayout(
				/* length: 24-184 bytes */
				structLayout(

						/* Word0&1 */
						Int64.BITS_64.withName("timestamp"),

						/* Word2 */
						Int16.BITS_16.withName("caplen"),
						Int16.BITS_08.withName("rx_port"),
						Int16.BITS_08.withName("tx_port"),

						/* Word3 */
						Int16.BITS_16.withName("wirelen"),
						Int16.BITS_01.withName("tx_now"),
						Int16.BITS_01.withName("tx_ignore"),
						Int16.BITS_01.withName("tx_crc_override"),
						Int16.BITS_01.withName("tx_set_clock"),
						Int16.BITS_04.withName("l2_type"),
						Int16.BITS_01.withName("l3_is_frag"),
						Int16.BITS_01.withName("l3_last_frag"),
						Int16.BITS_01.withName("reserved"),
						Int16.BITS_05.withName("record_count"),

						/* Word4 */
						unionLayout(
								Int32.BITS_32.withName("hash32"),
								Int32.BITS_32.withName("color"),
								structLayout(
										Int32.BITS_24.withName("hash24"),
										Int32.BITS_05.withName("hash_type"))),

						/* Word5 */
						Int64.BITS_64.withName("bitmask"),

						/* Word6-38 */
						sequenceLayout(DESC_TYPE2_RECORD_MAX_COUNT, Int64.BITS_64).withName("record")

				),
				sequenceLayout(32 + 6, Int32.BITS_32).withName("array")

		);
	}

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new type 2 layout.
	 *
	 * @param path the path
	 */
	Type2DescriptorLayout(String path) {
		this.field = Struct.TYPE2_STRUCT.bitField(path);
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

	/**
	 * Encode word 2 BE.
	 *
	 * @param captureLength the capture length
	 * @param rxPort        the rx port
	 * @param txPort        the tx port
	 * @return the int
	 */
	public static int encodeWord2BE(int captureLength, int rxPort, int txPort) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Encode word 2 LE.
	 *
	 * @param captureLength the capture length
	 * @param rxPort        the rx port
	 * @param txPort        the tx port
	 * @return the int
	 */
	public static int encodeWord2LE(int captureLength, int rxPort, int txPort) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Encode word 3 BE.
	 *
	 * @param wireLength    the wire length
	 * @param txNow         the tx now
	 * @param txIgnore      the tx ignore
	 * @param txCrcOverride the tx crc override
	 * @param txSetClock    the tx set clock
	 * @param l2Type        the l 2 type
	 * @param i             the i
	 * @param recordCount   the record count
	 * @return the int
	 */
	public static int encodeWord3BE(int wireLength, int txNow, int txIgnore, int txCrcOverride, int txSetClock,
			int l2Type, int i,
			int recordCount) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Encode word 3 LE.
	 *
	 * @param wireLength    the wire length
	 * @param txNow         the tx now
	 * @param txIgnore      the tx ignore
	 * @param txCrcOverride the tx crc override
	 * @param txSetClock    the tx set clock
	 * @param l2Type        the l 2 type
	 * @param i             the i
	 * @param recordCount   the record count
	 * @return the int
	 */
	public static int encodeWord3LE(int wireLength, int txNow, int txIgnore, int txCrcOverride, int txSetClock,
			int l2Type, int i,
			int recordCount) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}
