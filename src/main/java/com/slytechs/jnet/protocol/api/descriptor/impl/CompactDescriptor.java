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
package com.slytechs.jnet.protocol.api.descriptor.impl;

import static com.slytechs.jnet.platform.api.memory.layout.BinaryLayout.*;

import com.slytechs.jnet.platform.api.memory.layout.BinaryLayout;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int64;
import com.slytechs.jnet.protocol.api.pack.PackId;

/**
 * A compact descriptor that encodes a 32-bit ID, a 16-bit offset and 16-length
 * fields into a single 64-bit (8-byte) value. Typical usage is to encode in
 * some kind descriptors, a table made up of 64-bit entries, a list of headers
 * and offsets into a packet.
 */
public interface CompactDescriptor {

	/**
	 * The Interface DecodeConsumer.
	 */
	@FunctionalInterface
	public interface DecodeConsumer {

		/**
		 * Accept.
		 *
		 * @param id     the id
		 * @param offset the offset
		 * @param length the length
		 */
		void accept(int id, int offset, int length);
	}

	/**
	 * The Interface EncodeConsumer.
	 */
	@FunctionalInterface
	public interface EncodeConsumer {

		/**
		 * Accept.
		 *
		 * @param compact the compact
		 */
		void accept(long compact);
	}

	/** The Constant ID_NOT_FOUND. */
	public static final long ID_NOT_FOUND = -1L;

	/** The Constant COMPACT_ID_MASK. */
	public static final long COMPACT_ID_MASK = 0x0000_0000_0000FFFFL;

	/** The Constant COMPACT_META_MASK. */
	public static final long COMPACT_META_MASK = 0x0000_0000_FFFF0000L;

	/** The Constant COMPACT_OFF_MASK. */
	public static final long COMPACT_OFF_MASK = 0x0000_FFFF_00000000L;

	/** The Constant COMPACT_LEN_MASK. */
	public static final long COMPACT_LEN_MASK = 0xFFFF_0000_00000000L;

	/** The Constant COMPACT_ID_SHIFT. */
	public static final int COMPACT_ID_SHIFT = 0;

	/** The Constant COMPACT_META_SHIFT. */
	public static final int COMPACT_META_SHIFT = 16;

	/** The Constant COMPACT_OFF_SHIFT. */
	public static final int COMPACT_OFF_SHIFT = 32;

	/** The Constant COMPACT_LEN_SHIFT. */
	public static final int COMPACT_LEN_SHIFT = 48;

	/** The Constant COMPACT_LAYOUT. */
	public static final BinaryLayout COMPACT_LAYOUT = structLayout(
			Int64.BITS_16.withName("length"),
			Int64.BITS_16.withName("offset"),
			Int64.BITS_32.withName("id"));

	/**
	 * Decode.
	 *
	 * @param compact  the compact
	 * @param consumer the consumer
	 */
	public static void decode(CompactDescriptor compact, DecodeConsumer consumer) {
		consumer.accept(compact.id(), compact.offset(), compact.length());
	}

	/**
	 * Decode.
	 *
	 * @param compact  the compact
	 * @param consumer the consumer
	 */
	public static void decode(long compact, DecodeConsumer consumer) {
		consumer.accept(decodeId(compact), decodeOffset(compact), decodeLength(compact));
	}

	/**
	 * Decode id.
	 *
	 * @param compact the compact
	 * @return the int
	 */
	public static int decodeId(long compact) {
		return (int) ((compact & COMPACT_ID_MASK) >> COMPACT_ID_SHIFT);
	}

	/**
	 * Decode length.
	 *
	 * @param compact the compact
	 * @return the int
	 */
	public static int decodeLength(long compact) {
		return (int) ((compact & COMPACT_LEN_MASK) >> COMPACT_LEN_SHIFT);
	}

	/**
	 * Decode meta.
	 *
	 * @param compact the compact
	 * @return the int
	 */
	public static int decodeMeta(long compact) {
		return (int) ((compact & COMPACT_META_MASK) >> COMPACT_META_SHIFT);
	}

	/**
	 * Decode offset.
	 *
	 * @param compact the compact
	 * @return the int
	 */
	public static int decodeOffset(long compact) {
		return (int) ((compact & COMPACT_OFF_MASK) >> COMPACT_OFF_SHIFT);
	}

	/**
	 * Encode.
	 *
	 * @param id     the id
	 * @param offset the offset
	 * @param length the length
	 * @return the long
	 */
	public static long encode(int id, int offset, int length) {
		return 0l
				| (((long) id << COMPACT_ID_SHIFT) & COMPACT_ID_MASK)
				| (((long) offset << COMPACT_OFF_SHIFT) & COMPACT_OFF_MASK)
				| (((long) length << COMPACT_LEN_SHIFT) & COMPACT_LEN_MASK);
	}

	/**
	 * Encode.
	 *
	 * @param id       the id
	 * @param offset   the offset
	 * @param length   the length
	 * @param consumer the consumer
	 */
	public static void encode(int id, int offset, int length, EncodeConsumer consumer) {
		consumer.accept(encode(id, offset, length));
	}

	/**
	 * Encode.
	 *
	 * @param id     the id
	 * @param offset the offset
	 * @param length the length
	 * @param meta   the meta
	 * @return the long
	 */
	public static long encode(int id, int offset, int length, int meta) {
		return 0l
				| (((long) id << COMPACT_ID_SHIFT) & COMPACT_ID_MASK)
				| (((long) meta << COMPACT_META_SHIFT) & COMPACT_META_MASK)
				| (((long) offset << COMPACT_OFF_SHIFT) & COMPACT_OFF_MASK)
				| (((long) length << COMPACT_LEN_SHIFT) & COMPACT_LEN_MASK);
	}

	/**
	 * Index of in table.
	 *
	 * @param id    the id
	 * @param table the table
	 * @param limit the limit
	 * @return the int
	 */
	public static int indexOfInTable(int id, long[] table, int limit) {

		for (int i = 0; i < limit; i++) {
			long ent = (table[i] & CompactDescriptor.COMPACT_ID_MASK);
			if (ent == id)
				return i;
		}

		return -1;
	}

	/**
	 * Lookup id in table.
	 *
	 * @param id    the id
	 * @param table the table
	 * @param limit the limit
	 * @return the long
	 */
	public static long lookupIdInTable(int id, long[] table, int limit) {

		for (int i = 0; i < limit; i++) {
			long ent = (table[i] & CompactDescriptor.COMPACT_ID_MASK);
			if (ent == id)
				return table[i];
		}

		return CompactDescriptor.ID_NOT_FOUND;
	}

	/**
	 * Of.
	 *
	 * @param id     the id
	 * @param offset the offset
	 * @param length the length
	 * @return the compact descriptor
	 */
	public static CompactDescriptor of(int id, int offset, int length) {
		final long compact = encode(id, offset, length);
		return new CompactDescriptor() {

			@Override
			public long compact() {
				return compact;
			}

			@Override
			public int id() {
				return id;
			}

			@Override
			public int length() {
				return length;
			}

			@Override
			public int offset() {
				return offset;
			}

			@Override
			public String toString() {
				return String.format("%08x off=%d len=%d", id, offset, length);
			}
		};
	}

	/**
	 * Of.
	 *
	 * @param compact the compact
	 * @return the compact descriptor
	 */
	public static CompactDescriptor of(long compact) {
		int id = decodeId(compact);
		int offset = decodeOffset(compact);
		int length = decodeLength(compact);

		return new CompactDescriptor() {

			@Override
			public long compact() {
				return compact;
			}

			@Override
			public int id() {
				return id;
			}

			@Override
			public int length() {
				return length;
			}

			@Override
			public int offset() {
				return offset;
			}

		};
	}

	/**
	 * To string.
	 *
	 * @param encoded the encoded
	 * @return the string
	 */
	public static String toString(long encoded) {
		return "CompactDescriptor"
				+ "[id=" + String.format("%s", PackId.toString(decodeId(encoded)))
				+ " off=" + decodeOffset(encoded)
				+ " len=" + decodeLength(encoded)
//				+ " compact=" + String.format("0x%016X", encoded)
				+ "]";
	}

	/**
	 * Compact.
	 *
	 * @return the long
	 */
	long compact();

	/**
	 * Id.
	 *
	 * @return the int
	 */
	int id();

	/**
	 * Length.
	 *
	 * @return the int
	 */
	int length();

	/**
	 * Offset.
	 *
	 * @return the int
	 */
	int offset();
}
