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
package com.slytechs.jnet.protocol.api.pack;

import java.util.Optional;

import com.slytechs.jnet.platform.api.NotFound;
import com.slytechs.jnet.protocol.api.descriptor.CompactDescriptor;

/**
 * A unique 32-bit protocol ID or a 32-bit record and a box class. A protocol
 * pack ID is unique ID across all protocol packs and identifies specific
 * headers and protocol packs.
 * 
 * <p>
 * This class provides numerous static utility methods for encoding and decoding
 * a 4-bit pack-id and a 6-bit header/protocol id as well as bit-wise compatible
 * 32-bit records.
 * </p>
 * 
 * <h2>32-bit header 'ID' encoding</h2>
 * <p>
 * In unique ID use case, the header and pack ID are encoded in the 1st 12-bits.
 * The ID is a combination of a pack-ID (6-bits) and header-ID (6-bits). The
 * combination of pack-ID + header-ID provide a unique ID for each header across
 * all protocol packs.
 * </p>
 * 
 * <pre>
 * struct pack_id_s {
 * 	 uint16_t
 * 		ordinal:8,  // Index within the protocol pack
 * 		pack:8;     // Protocol pack unique number
 * 
 * 	 uint16_t
 *      	class_mask;
 * </pre>
 * 
 * <h2>32-bit 'record' encoding</h2>
 * <p>
 * In record use case, records are used to associate header offset and length
 * with a particular ID. The {@code Type2Descriptor} uses an array of 32-bit
 * records to record header offsets/length after packet dissection.
 * </p>
 * 
 * <pre>
 * struct pack_record_s {
 * 	uint64_t
 * 		ordinal:8,    // Index within the protocol pack
 * 		pack:8,       // Protocol pack unique number
 *      class_mask:16;// Classification mask
 * 
 * 		size:16,     // (Optional) Size of the protocol header (in units of 8-bits)
 * 		offset:16;   // (Optional) Offset into the packet (in units of 8-bit bytes)
 * }
 * </pre>
 * <p>
 * For an ID, offset and size fields are both set to 0.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public interface PackId {

	/** The pack mask ordinal. */
	// @formatter:off
	
	/** RECORD not found in the record table */
	long RECORD_NOT_FOUND = -1;
	
	/** The pack id not found. */
	int ID_NOT_FOUND = -1;
	
	/** The id class ordinal. */
	int ID_ORDINAL_CLASS = 0xFF;
	
	/** The pack mask ordinal. */
	int PACK_MASK_ORDINAL  = 0x000000FF;  // 07:00 - 8 bits, protocol number
	
	/** The pack mask pack. */
	int PACK_MASK_PACK     = 0x0000FF00;  // 15:08 - 8 bits, protocol pack number
	
	/** The pack mask pack. */
	int PACK_MASK_CLASSBITMASK = 0xFFFF_0000;  // 31:16 - 16 bits, classification bitmask
	
	/** The pack mask unpack. */
	int PACK_MASK_ID   = 0xFFFFFFFF;  // 09:00 - Pack + Ordinal
	
	/** The pack mask unpack. */
	int PACK_MASK_PACK_ORDINAL   = 0x0000FFFF;  // 09:00 - Pack + Ordinal
	
	/** The record mask ordinal. */
	long RECORD_MASK_ORDINAL = 0x00000000_000000FFL; // 05:00 - 6 bits, protocol number
	
	/** The record mask classbitmask. */
	long RECORD_MASK_CLASSBITMASK = 0x00000000_FFFF000FL; // 05:00 - 6 bits, protocol number
	
	/** The record mask pack. */
	long RECORD_MASK_PACK    = 0x00000000_0000FF00L; // 09:06 - 4 bits, protocol pack number
	
	/** The record mask unpack. */
	long RECORD_MASK_ID     = 0x00000000_FFFFFFFFL; // 09:00 - Pack + Ordinal
	
	/** The record mask pack ordinal. */
	long RECORD_MASK_PACK_ORDINAL  = 0x00000000_0000FFFFL; // 09:00 - Pack + Ordinal
	
	/** The record mask size. */
	long RECORD_MASK_SIZE    = 0x0000FFFF_00000000L; // 20:10 - 11 bits (in units of 32 bits)
	
	/** The record mask offset. */
	long RECORD_MASK_OFFSET  = 0xFFFF0000_00000000L; // 31:21 - 11 bits (in units of 8 bits)
	// @formatter:on

	/** The pack shift ordinal. */
	// @formatter:off
	int PACK_SHIFT_ORDINAL  = 0;
	
	/** The pack shift classbitmask. */
	int PACK_SHIFT_CLASSBITMASK = 16;
	
	/** The pack shift pack. */
	int PACK_SHIFT_PACK     = 8;
	
	/** The record shift ordinal. */
	int RECORD_SHIFT_ORDINAL = 0;
	
	/** The record shift pack. */
	int RECORD_SHIFT_PACK    = 8;
	
	/** The record shift classification mask. */
	int RECORD_SHIFT_CLASSBITMASK    = 16;
	
	/** The record shift size. */
	int RECORD_SHIFT_SIZE    = 32;
	
	/** The record shift offset. */
	int RECORD_SHIFT_OFFSET  = 48;
	// @formatter:on

	/** The Constant PACK_MAXCOUNT_PACKS. */
	int PACK_MAXCOUNT_PACKS = 256;

	/** The Constant PACK_MAXCOUNT_ORDINALS. */
	int PACK_MAXCOUNT_ORDINALS = 256;

	/** The pack maxcount class mask bits. */
	int PACK_MAXCOUNT_CLASS_MASK_BITS = 16;

	/**
	 * Bitmask check.
	 *
	 * @param mask the mask
	 * @param id   the id
	 * @return true, if successful
	 */
	static boolean bitmaskCheck(long mask, int id) {
		return ((1L << decodeIdOrdinal(id)) & mask) != 0;
	}

	/**
	 * Bitmask set.
	 *
	 * @param mask the mask
	 * @param id   the id
	 * @return the int
	 */
	static long bitmaskSet(long mask, int id) {
		return (1L << decodeIdOrdinal(id)) | mask;
	}

	/**
	 * Class bitmask is empty.
	 *
	 * @param idSrcMask the id src mask
	 * @return true, if successful
	 */
	static boolean classBitmaskIsEmpty(int idSrcMask) {
		return (idSrcMask & PACK_MASK_CLASSBITMASK) == 0;
	}

	/**
	 * Class bitmask is present.
	 *
	 * @param idSrcMask the id src mask
	 * @return true, if successful
	 */
	static boolean classBitmaskIsPresent(int idSrcMask) {
		return (idSrcMask & PACK_MASK_CLASSBITMASK) > 0;
	}

	/**
	 * Classmask check.
	 *
	 * @param classBitmask the class bitmask
	 * @param id           the id
	 * @return true, if successful
	 */
	static boolean classmaskCheck(int classBitmask, int id) {
		int classbits = (classBitmask & PACK_MASK_CLASSBITMASK);
		int ordinalbits = (classBitmask & PACK_MASK_ORDINAL);

		return (ordinalbits == ID_ORDINAL_CLASS)
				&& (classbits != 0)
				&& (id & classbits) == classbits;
	}

	/**
	 * Decode id ordinal.
	 *
	 * @param id the id
	 * @return the int
	 */
	static int decodeIdOrdinal(int id) {
		return (id & PACK_MASK_ORDINAL) >> PACK_SHIFT_ORDINAL;
	}

	/**
	 * Decode pack id.
	 *
	 * @param id the id
	 * @return the int
	 */
	static int decodePackId(int id) {
		return (id & PACK_MASK_PACK);
	}

	/**
	 * Decode pack class bitmask.
	 *
	 * @param id the id
	 * @return the int
	 */
	static int decodePackClassBitmask(int id) {
		return (id & PACK_MASK_CLASSBITMASK);
	}

	/**
	 * Decode pack id.
	 *
	 * @param id the id
	 * @return the int
	 */
	static int decodeRecordId(int id) {
		return (id & PACK_MASK_ID);
	}

	/**
	 * Decode pack ordinal.
	 *
	 * @param id the id
	 * @return the int
	 */
	static int decodePackOrdinal(int id) {
		return (id & PACK_MASK_PACK) >> PACK_SHIFT_PACK;
	}

	/**
	 * Decode record offset.
	 *
	 * @param record the record
	 * @return the int
	 */
	static int decodeRecordOffset(long record) {
		return (int) ((record & RECORD_MASK_OFFSET) >> RECORD_SHIFT_OFFSET);
	}

	/**
	 * Decode record ordinal.
	 *
	 * @param record the record
	 * @return the int
	 */
	static int decodeRecordOrdinal(long record) {
		return (int) ((record & RECORD_MASK_ORDINAL) >> RECORD_SHIFT_ORDINAL);
	}

	/**
	 * Decode record pack ordinal.
	 *
	 * @param record the record
	 * @return the int
	 */
	static int decodeRecordPackOrdinal(long record) {
		return (int) ((record & RECORD_MASK_PACK) >> RECORD_SHIFT_PACK);
	}

	/**
	 * Decode record pack id.
	 *
	 * @param record the record
	 * @return the int
	 */
	static int decodeRecordPackId(long record) {
		return (int) (record & RECORD_MASK_PACK);
	}

	/**
	 * Decode record class bitmask.
	 *
	 * @param record the record
	 * @return the int
	 */
	static int decodeRecordClassBitmask(long record) {
		return (int) (record & RECORD_MASK_CLASSBITMASK) >> RECORD_SHIFT_CLASSBITMASK;
	}

	/**
	 * Decode record size.
	 *
	 * @param record the record
	 * @return the int
	 */
	static int decodeRecordSize(long record) {
		return (int) ((record & RECORD_MASK_SIZE) >> RECORD_SHIFT_SIZE);
	}

	/**
	 * Encode id.
	 *
	 * @param packOrdinal the pack ordinal
	 * @param ordinal     the ordinal
	 * @return the int
	 */
	static int encodeId(int packOrdinal, int ordinal) {
		return encodeId(packOrdinal, ordinal, 0);
	}

	/**
	 * Encode id.
	 *
	 * @param packOrdinal  the pack ordinal
	 * @param ordinal      the ordinal
	 * @param classBitmask the class bitmask
	 * @return the int
	 */
	static int encodeId(int packOrdinal, int ordinal, int classBitmask) {
		return 0
				| ((ordinal << PACK_SHIFT_ORDINAL) & PACK_MASK_ORDINAL)
				| ((packOrdinal << PACK_SHIFT_PACK) & PACK_MASK_PACK)
				| classBitmask;
	}

	/**
	 * Encode id.
	 *
	 * @param pack    the pack
	 * @param ordinal the ordinal
	 * @return the int
	 */
	static int encodeId(ProtocolPackTable pack, int ordinal) {
		return PackId.encodeId(pack.ordinal(), ordinal);
	}

	/**
	 * Encode id.
	 *
	 * @param pack         the pack
	 * @param ordinal      the ordinal
	 * @param classBitmask the class bitmask
	 * @return the int
	 */
	static int encodeId(ProtocolPackTable pack, int ordinal, int classBitmask) {
		return PackId.encodeId(pack.ordinal(), ordinal, classBitmask);
	}

	/**
	 * Encode record.
	 *
	 * @param id     the id
	 * @param offset the offset
	 * @param size   the size
	 * @return the int
	 */
	static long encodeRecord(int id, int offset, int size) {
		return (id) |
				(((long) size << RECORD_SHIFT_SIZE) & RECORD_MASK_SIZE) |
				(((long) offset << RECORD_SHIFT_OFFSET) & RECORD_MASK_OFFSET);
	}

	/**
	 * Encode record id.
	 *
	 * @param record the record
	 * @return the int
	 */
	static int decodeRecordId(long record) {
		return (int) (record & RECORD_MASK_ID);
	}

	/**
	 * Compares the protocol ID portions of id and encoded parameters to be equal.
	 * 
	 * @param record id which has offset/length encoded
	 * @param id     clean protocol id which does not have offset/length encoded
	 *
	 * @return true, if protocol IDs of both id and encoded are equals
	 */
	static boolean recordEqualsId(long record, int id) {
		int recordId = (int) (record & RECORD_MASK_ID);

		return false
				|| (recordId == id)
				|| classmaskCheck(id, recordId);
	}

	/**
	 * Record equals pack.
	 *
	 * @param record the record
	 * @param pack   the pack
	 * @return true, if successful
	 */
	static boolean recordEqualsPack(int record, int pack) {
		return (record & PACK_MASK_PACK) == pack;
	}

	/**
	 * Record to compact descriptor.
	 *
	 * @param record the record
	 * @return the long
	 */
	static long recordToCompactDescriptor(long record) {
		int offset = decodeRecordOffset(record);
		int length = decodeRecordSize(record);
		int id = decodeRecordId(record);

		return CompactDescriptor.encode(id, offset, length);
	}

	/**
	 * Record to compact descriptor.
	 *
	 * @param record      the record
	 * @param id          the id
	 * @param recordIndex the record index
	 * @return the long
	 */
	static long recordToCompactDescriptor(long record, int id, int recordIndex) {
		int offset = decodeRecordOffset(record);
		int length = decodeRecordSize(record);

		return CompactDescriptor.encode(id, offset, length, recordIndex);
	}

	/**
	 * Gets the.
	 *
	 * @param id the id
	 * @return the pack id
	 * @throws NotFound the not found
	 */
	static PackId get(int id) throws NotFound {
		PackId pid = peek(id);
		if (pid == null)
			throw new NotFound("0x04X".formatted(id));

		return pid;
	}

	/**
	 * Peek.
	 *
	 * @param id the id
	 * @return the pack id
	 */
	static PackId peek(int id) {
		int packComponent = decodePackId(id);
		Pack<?> pack = Pack.getLoadedPack(packComponent);
		if (pack == null)
			return null;

		var hdr = pack.findHeader(id);
		if (hdr.isPresent() && hdr.get() instanceof PackId packId)
			return packId;

		return null;
	}

	/**
	 * Find.
	 *
	 * @param id the id
	 * @return the optional
	 */
	static Optional<PackId> find(int id) {
		return Optional.ofNullable(peek(id));
	}

	/**
	 * To string.
	 *
	 * @param id the id
	 * @return the string
	 */
	static String toString(int id) {
		return find(id)
				.map(PackId::toString)
				.orElse("0x04X".formatted(id));
	}

	/**
	 * Decode 32-bit pack id from the full id.
	 *
	 * @return pack only id
	 */
	default int packId() {
		return decodePackId(id());
	}

	/**
	 * Decode 32-bit pack ordinal or zero-based index.
	 *
	 * @return the pack ordinal index
	 */
	default int packOrdinal() {
		return decodePackOrdinal(id());
	}

	/**
	 * Decode 32-bit id ordinal or index.
	 *
	 * @return the ordinal index
	 */
	default int idOrdinal() {
		return decodeIdOrdinal(id());
	}

	/**
	 * Decode offset in bytes if id is a record.
	 *
	 * @return offset in units of bytes
	 */
	default int offset() {
		return decodeRecordOffset(id());
	}

	/**
	 * Decode size in bytes if id is a record.
	 *
	 * @return size in units of bytes
	 */
	default int size() {
		return decodeRecordSize(id());
	}

	/**
	 * Gets a 32-bit pack id for this object.
	 *
	 * @return the id
	 */
	int id();
}
