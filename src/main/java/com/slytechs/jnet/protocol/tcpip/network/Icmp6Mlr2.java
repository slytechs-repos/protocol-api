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
package com.slytechs.jnet.protocol.tcpip.network;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Objects;

import com.slytechs.jnet.platform.api.util.Enums;
import com.slytechs.jnet.protocol.api.common.HasIndexedRecord;
import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.common.HeaderNotFound;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.tcpip.constants.CoreId;
import com.slytechs.jnet.protocol.tcpip.constants.Icmp6Mlr2RecordType;

/**
 * ICMPv6 Multicast Listener Report Message v2 header.
 * 
 * <p>
 * ICMPv6 Multicast Listener Report v2 (MLRv2) records are used by IPv6 hosts to
 * report their interest in receiving multicast traffic to multicast routers.
 * MLRv2 records are sent in response to ICMPv6 General Queries (GQs) or
 * Multicast Router Solicitations (MRSs).
 * </p>
 * 
 * <p>
 * An MLRv2 record contains the following information:
 * </p>
 * <ul>
 * <li>The multicast address that the host is interested in receiving traffic
 * for.</li>
 * <li>The interface on which the host is listening for multicast traffic.</li>
 * <li>The filter mode for the multicast address.</li>
 * <li>The source list for the multicast address.</li>
 * </ul>
 * 
 * <p>
 * The filter mode indicates how the host wants to receive multicast traffic for
 * the specified multicast address. The following are the possible filter modes:
 * </p>
 * 
 * <ul>
 * <li>All: The host wants to receive all multicast traffic for the specified
 * multicast address.</li>
 * <li>Some: The host wants to receive only multicast traffic from specific
 * sources for the specified multicast address.</li>
 * <li>None: The host does not want to receive any multicast traffic for the
 * specified multicast address.</li>
 * </ul>
 * 
 * <p>
 * The source list is a list of IPv6 addresses that the host wants to receive
 * multicast traffic from for the specified multicast address. The source list
 * is only used when the filter mode is set to "Some".
 * </p>
 * 
 * <p>
 * MLRv2 records are used by multicast routers to build multicast forwarding
 * tables. The multicast routers use the information in the MLRv2 records to
 * determine which hosts are interested in receiving multicast traffic for a
 * particular multicast address.
 * </p>
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
@MetaResource("icmp6-mlr2-meta.json")
public final class Icmp6Mlr2
		extends Icmp6
		implements Iterable<Icmp6Mlr2.MulticastAddressRecord>, HasIndexedRecord<Icmp6Mlr2.MulticastAddressRecord> {

	/**
	 * An ICMPv6 Multicast Listener Report v2 (MLRv2) with Allow New Sources record.
	 */
	@MetaResource("icmp6-mlr2-allow-new-sources-meta.json")
	public static class AllowNewSourcesRecord extends MulticastAddressRecord {

		/** The Constant ID. */
		public static final int ID = Icmp6Mlr2RecordType.ICMPv6_ID_OPT_MLRv2_ALLOW_NEW_SOURCES;

		/**
		 * Instantiates a new change include record.
		 */
		public AllowNewSourcesRecord() {
			super(ID);
		}
	}

	/**
	 * An ICMPv6 Multicast Listener Report v2 (MLRv2) with Block Old Sources record.
	 */
	@MetaResource("icmp6-mlr2-block-old-sources-meta.json")
	public static class BlockOldSourcesRecord extends MulticastAddressRecord {

		/** The Constant ID. */
		public static final int ID = Icmp6Mlr2RecordType.ICMPv6_ID_OPT_MLRv2_BLOCK_OLD_SOURCES;

		/**
		 * Instantiates a new change include record.
		 */
		public BlockOldSourcesRecord() {
			super(ID);
		}
	}

	/**
	 * An ICMPv6 Multicast Listener Report v2 (MLRv2) Change Exclude record.
	 */
	@MetaResource("icmp6-mlr2-change-ex-meta.json")
	public static class ChangeExcludeRecord extends MulticastAddressRecord {

		/** The Constant ID. */
		public static final int ID = Icmp6Mlr2RecordType.ICMPv6_ID_OPT_MLRv2_CHANGE_TO_EXCLUDE;

		/**
		 * Instantiates a new change exclude record.
		 */
		public ChangeExcludeRecord() {
			super(ID);
		}
	}

	/**
	 * An ICMPv6 Multicast Listener Report v2 (MLRv2) Change Include record.
	 */
	@MetaResource("icmp6-mlr2-change-in-meta.json")
	public static class ChangeIncludeRecord extends MulticastAddressRecord {

		/** The Constant ID. */
		public static final int ID = Icmp6Mlr2RecordType.ICMPv6_ID_OPT_MLRv2_CHANGE_TO_INCLUDE;

		/**
		 * Instantiates a new change include record.
		 */
		public ChangeIncludeRecord() {
			super(ID);
		}
	}

	/**
	 * An ICMPv6 Multicast Listener Report v2 (MLRv2) with Mode Exclude record.
	 */
	@MetaResource("icmp6-mlr2-mode-ex-meta.json")
	public static class ModeExcludeRecord extends MulticastAddressRecord {

		/** The Constant ID. */
		public static final int ID = Icmp6Mlr2RecordType.ICMPv6_ID_OPT_MLRv2_MODE_IS_EXCLUDE;

		/**
		 * Instantiates a new change include record.
		 */
		public ModeExcludeRecord() {
			super(ID);
		}
	}

	/**
	 * An ICMPv6 Multicast Listener Report v2 (MLRv2) with Mode Include record.
	 */
	@MetaResource("icmp6-mlr2-mode-in-meta.json")
	public static class ModeIncludeRecord extends MulticastAddressRecord {

		/** The Constant ID. */
		public static final int ID = Icmp6Mlr2RecordType.ICMPv6_ID_OPT_MLRv2_MODE_IS_INCLUDE;

		/**
		 * Instantiates a new change include record.
		 */
		public ModeIncludeRecord() {
			super(ID);
		}
	}

	/**
	 * Base class for all MLRv2 records.
	 */
	@MetaResource("icmp6-mlr2-record-meta.json")
	public static class MulticastAddressRecord extends Header implements Iterable<Ip6Address> {

		/** The MLRv2 generic record ID. */
		public static final int ID = Icmp6Mlr2RecordType.ICMPv6_ID_OPT_MLRv2_RECORD;

		/**
		 * Calculate record length directly from data buffer and record fields.
		 *
		 * @param offset the offset into the data buffer
		 * @param buffer the data buffer
		 * @return the int length in bytes of the entire record
		 */
		private static int calculateRecordLength(int offset, ByteBuffer buffer) {
			int auxDataLen = Byte.toUnsignedInt(buffer.get(offset + 1));
			int numberOfSources = Short.toUnsignedInt(buffer.getShort(offset + 2));

			return ICMPv6_MLRv2_RECORD_HEADER_LEN
					+ IpAddress.IPv6_ADDRESS_SIZE
					+ (numberOfSources * IpAddress.IPv6_ADDRESS_SIZE)
					+ (auxDataLen * 4);
		}

		/**
		 * Calculate record offset.
		 *
		 * @param index      the index of the record
		 * @param buffer     the buffer MLRv2 message buffer
		 * @param numRecords number of records in total
		 * @return the offset of the record in bytes
		 */
		private static int calculateRecordOffset(int index, ByteBuffer buffer) {

			int offset = ICMPv6_MLRv2_HEADER_LEN;

			while (index-- > 0) {
				int len = MulticastAddressRecord.calculateRecordLength(offset, buffer);

				offset += len;
			}

			return offset;
		}

		/**
		 * Calculate record type directly from data buffer.
		 *
		 * @param offset the offset into the data buffer
		 * @param buffer the data buffer
		 * @return the int
		 */
		private static Icmp6Mlr2RecordType calculateType(int offset, ByteBuffer buffer) {
			Icmp6Mlr2RecordType type = Enums.valueOf(Byte.toUnsignedInt(buffer.get(offset + 0)),
					Icmp6Mlr2RecordType.class);

			return type;
		}

		/** The record type enum constant looked up onBind memory action. */
		private Icmp6Mlr2RecordType type;

		/**
		 * Instantiates a new MLRv2 generic record header.
		 */
		public MulticastAddressRecord() {
			super(ID);
		}

		/**
		 * Instantiates a new subclass of MLRv2 record header of specific subclass.
		 *
		 * @param id protocol ID of the subclass
		 */
		protected MulticastAddressRecord(int id) {
			super(id);
		}

		/**
		 * Aux data length (in units of 32-bits).
		 *
		 * @return the number of 32-bit words in the AUX data field at the end of the
		 *         record
		 */
		@Meta
		public int auxDataLen() {
			return Byte.toUnsignedInt(buffer().get(1));
		}

		/**
		 * Aux data length (in units of bytes).
		 *
		 * @return the number of bytes in the AUX data field at the end of the record
		 */
		@Meta(MetaType.ATTRIBUTE)
		public int auxDataLenBytes() {
			return auxDataLen() * 4;
		}

		/**
		 * Gets the source address from the source tables within this record.
		 *
		 * @param index the index of the source address within this record
		 * @return the IPv6 source address
		 */
		public Ip6Address getSourceAddress(int index) {
			Objects.checkIndex(index, numberOfSources());

			int offset = RECORD_SOURCE_ADDRESS_OFFSET + (index * IpAddress.IPv6_ADDRESS_SIZE);

			return Ip6Address.get(offset, buffer());
		}

		/**
		 * Iterates over all multicast source addresses within this record.
		 * 
		 * @see java.lang.Iterable#iterator()
		 */
		@Override
		public Iterator<Ip6Address> iterator() {
			return new Iterator<Ip6Address>() {
				final int count = numberOfSources();
				int index = 0;

				@Override
				public boolean hasNext() {
					return index < count;
				}

				@Override
				public Ip6Address next() {
					return getSourceAddress(index++);
				}

			};
		}

		/**
		 * MLRv2 Multicast address of the interface being configured.
		 *
		 * @return the IPv6 multicast address field value
		 */
		@Meta
		public Ip6Address multicastAddress() {
			return Ip6Address.get(4, buffer());
		}

		/**
		 * Number of source multicast addresses within this record.
		 *
		 * @return the number of source addresses
		 */
		@Meta
		public int numberOfSources() {
			return Short.toUnsignedInt(buffer().getShort(2));
		}

		@Override
		protected void onBind() {
			super.onBind();

			this.type = Enums.valueOf(recordType(), Icmp6Mlr2RecordType.class);
		}

		/**
		 * MLRv2 Record type.
		 *
		 * @return the 8-bit record type field value
		 */
		@Meta
		public int recordType() {
			return Byte.toUnsignedInt(buffer().get(0));
		}

		/**
		 * Record type as enum.
		 *
		 * @return the icmp 6 mlr 2 record type
		 */
		public Icmp6Mlr2RecordType recordTypeAsEnum() {
			return type;
		}

	}

	/** The Constant ICMPv6_MLRv2_HEADER_LEN. */
	private static final int ICMPv6_MLRv2_HEADER_LEN = 8;

	/** The Constant ICMPv6_MLRv2_RECORD_HEADER_LEN. */
	private static final int ICMPv6_MLRv2_RECORD_HEADER_LEN = 4;

	/** The Constant RECORD_START_OFFSET. */
	private static final int RECORD_START_OFFSET = ICMPv6_MLRv2_HEADER_LEN;

	/** The Constant RECORD_SOURCE_ADDRESS_OFFSET. */
	private static final int RECORD_SOURCE_ADDRESS_OFFSET = 4 + IpAddress.IPv6_ADDRESS_SIZE;

	/** A constant ID for all generic MLRv2 records. */
	public static final int ID = CoreId.CORE_ID_ICMPv6_MULTICAST_LISTENER_REPORTv2;

	/**
	 * New ICMPv6 Multicast Listener Report Message v2.
	 */
	public Icmp6Mlr2() {
		super(ID);
	}

	/**
	 * The multicast address record at specified record index.
	 *
	 * @param index the record index.
	 * @return the record at specified index
	 * @throws IndexOutOfBoundsException if index is greater than number of records
	 *                                   or negative
	 */
	@Override
	public MulticastAddressRecord getRecord(int index) throws IndexOutOfBoundsException {
		Objects.checkIndex(index, numberOfRecords());

		ByteBuffer buffer = buffer();
		int offset = MulticastAddressRecord.calculateRecordOffset(index, buffer);
		int len = MulticastAddressRecord.calculateRecordLength(offset, buffer);

		Icmp6Mlr2RecordType type = MulticastAddressRecord.calculateType(offset, buffer());

		MulticastAddressRecord multicastAddressRecord = type.newHeaderInstance();
		multicastAddressRecord.bind(buffer().slice(offset, len));

		return multicastAddressRecord;
	}

	/**
	 * Gets a multicast address record if present at the specified record index.
	 *
	 * @param <R>   the record type
	 * @param rec   the record header to bind to
	 * @param index the index of the multicast address record
	 * @return The supplied and bound record object. This method never returns null.
	 * @throws HeaderNotFound if the record is not found
	 */
	@Override
	public <R extends MulticastAddressRecord> R getRecord(int index, R rec) throws HeaderNotFound {
		R result = peekRecord(index, rec);
		if (result == null)
			throw new HeaderNotFound(rec.headerName());

		return result;
	}

	/**
	 * Gets a mutlicast address record at a specific byte offset into the MLRv2
	 * message.
	 *
	 * @param byteOffset the offset into the databuffer from the start of the MLRv2
	 *                   header.
	 * @return the multicast address record at the specified offset
	 */
	private MulticastAddressRecord getRecordAtOffset(int byteOffset) {
		ByteBuffer buffer = buffer();
		Icmp6Mlr2RecordType type = MulticastAddressRecord.calculateType(byteOffset, buffer);
		int len = MulticastAddressRecord.calculateRecordLength(byteOffset, buffer);

		MulticastAddressRecord multicastAddressRecord = type.newHeaderInstance();
		multicastAddressRecord.bind(buffer().slice(byteOffset, len));

		return multicastAddressRecord;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HasIndexedRecord#hasRecord(int, int)
	 */
	@Override
	public boolean hasRecord(int index, int recordId) {
		if (index < 0 || index >= numberOfRecords())
			return false;

		ByteBuffer buffer = buffer();
		int offset = MulticastAddressRecord.calculateRecordOffset(index, buffer);

		Icmp6Mlr2RecordType type = MulticastAddressRecord.calculateType(offset, buffer());

		return recordId != type.id();
	}

	/**
	 * Checks and binds a multicast address record if present at the specified
	 * record index.
	 *
	 * @param <R>   the record type
	 * @param rec   the record header to bind to
	 * @param index the index of the multicast address record
	 * @return true, if binding was successful, otherwise false if the record not
	 *         found at specified index
	 */
	public <R extends MulticastAddressRecord> boolean hasRecord(R rec, int index) {
		R result = peekRecord(index, rec);

		return result != null;
	}

	/**
	 * Iterates over all present multicast address records.
	 *
	 * @return the record iterator
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<MulticastAddressRecord> iterator() {
		return new Iterator<Icmp6Mlr2.MulticastAddressRecord>() {
			final int num = numberOfRecords();
			int index = 0;
			int offset = RECORD_START_OFFSET;

			@Override
			public boolean hasNext() {
				return index < num;
			}

			@Override
			public MulticastAddressRecord next() {
				MulticastAddressRecord multicastAddressRecord = getRecordAtOffset(offset);
				offset += multicastAddressRecord.headerLength();

				return multicastAddressRecord;
			}
		};
	}

	/**
	 * Number of multicast address records.
	 *
	 * @return the int
	 */
	@Override
	@Meta
	public int numberOfRecords() {
		return Short.toUnsignedInt(buffer().getShort(6));
	}

	/**
	 * Binds a multicast address record if present at the specified record index or
	 * null.
	 *
	 * @param <R>   the record type
	 * @param rec   the record header to bind to
	 * @param index the index of the multicast address record
	 * @return the supplied record if successfully bound or null if record not found
	 */
	@Override
	public <R extends MulticastAddressRecord> R peekRecord(int index, R rec) {
		if (index < 0 || index >= numberOfRecords())
			return null;

		ByteBuffer buffer = buffer();
		int offset = MulticastAddressRecord.calculateRecordOffset(index, buffer);
		int len = MulticastAddressRecord.calculateRecordLength(offset, buffer());
		Icmp6Mlr2RecordType type = MulticastAddressRecord.calculateType(offset, buffer());

		if (rec.id() != type.id())
			return null;

		rec.bind(buffer().slice(offset, len));

		return rec;
	}

	/**
	 * The first record.
	 *
	 * @return the multicast address record
	 */
	@Meta
	public MulticastAddressRecord record() {
		return getRecordAtOffset(RECORD_START_OFFSET);
	}

	/**
	 * MLRv2 reserved field.
	 *
	 * @return the reserved field value (should be 0).
	 */
	@Meta
	public int reserved() {
		return Short.toUnsignedInt(buffer().getShort(4));
	}
}
