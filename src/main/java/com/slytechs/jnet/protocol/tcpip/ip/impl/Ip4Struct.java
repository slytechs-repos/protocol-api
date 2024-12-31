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
package com.slytechs.jnet.protocol.tcpip.ip.impl;

import static com.slytechs.jnet.platform.api.memory.layout.BinaryLayout.*;

import com.slytechs.jnet.platform.api.memory.layout.ArrayField;
import com.slytechs.jnet.platform.api.memory.layout.BinaryLayout;
import com.slytechs.jnet.platform.api.memory.layout.BitField;
import com.slytechs.jnet.platform.api.memory.layout.EnumBitField;
import com.slytechs.jnet.platform.api.memory.layout.FormattedBitField.BitFieldFormatter;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int16be;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int32be;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int8;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Padding;

/**
 * The Enum Ip4Layout.
 */
public enum Ip4Struct implements EnumBitField<Ip4Struct> {

	/** The version. */
	VERSION(Struct.IP4_STRUCT, "ip.version"),

	/** The hdr len. */
	HDR_LEN(Struct.IP4_STRUCT, "ip.hdr_len"),

	/** The dsfield. */
	DSFIELD(Struct.IP4_STRUCT, "ip.dsfield"),

	/** The dsfield dscp. */
	DSFIELD_DSCP(Struct.IP4_STRUCT, "ip.dsfield.dscp"),

	/** The dsfield dscp select. */
	DSFIELD_DSCP_SELECT(Struct.IP4_STRUCT, "ip.dsfield.dscp.select"),

	/** The dsfield dscp code. */
	DSFIELD_DSCP_CODE(Struct.IP4_STRUCT, "ip.dsfield.dscp.code"),

	/** The dsfield ecn. */
	DSFIELD_ECN(Struct.IP4_STRUCT, "ip.dsfield.ecn"),

	/** The total length. */
	TOTAL_LENGTH(Struct.IP4_STRUCT, "ip.len"),

	/** The id. */
	ID(Struct.IP4_STRUCT, "ip.id", "0x%x (%1$d)"),

	/** The flags. */
	FLAGS(Struct.IP4_STRUCT, "ip.flags"),

	/** The flags nibble. */
	FLAGS_NIBBLE(Struct.IP4_STRUCT, "ip.flags_nibble"),

	/** The flags byte. */
	FLAGS_BYTE(Struct.IP4_STRUCT, "ip.flags_byte"),

	/** The flags rb. */
	FLAGS_RB(Struct.IP4_STRUCT, "ip.flags.rb"),

	/** The flags df. */
	FLAGS_DF(Struct.IP4_STRUCT, "ip.flags.df"),

	/** The flags mf. */
	FLAGS_MF(Struct.IP4_STRUCT, "ip.flags.mf"),

	/** The frag offset. */
	FRAG_OFFSET(Struct.IP4_STRUCT, "ip.frag_offset"),

	/** The ttl. */
	TTL(Struct.IP4_STRUCT, "ip.ttl"),

	/** The proto. */
	PROTO(Struct.IP4_STRUCT, "ip.proto"),

	/** The checksum. */
	CHECKSUM(Struct.IP4_STRUCT, "ip.checksum"),

	/** The src. */
	SRC(Struct.IP4_STRUCT, "ip.src"),

	/** The dst. */
	DST(Struct.IP4_STRUCT, "ip.dst"),

	/** The header word0. */
	HEADER_WORD0(Struct.IP4_STRUCT, "ip.word0", "%08X"),

	/** The header word1. */
	HEADER_WORD1(Struct.IP4_STRUCT, "ip.word1", "%08X"),

	/** The header word2. */
	HEADER_WORD2(Struct.IP4_STRUCT, "ip.word2", "%08X"),

	/** The header word3. */
	HEADER_WORD3(Struct.IP4_STRUCT, "ip.word3", "%08X"),

	/** The header word4. */
	HEADER_WORD4(Struct.IP4_STRUCT, "ip.word4", "%08X");

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant IP4_STRUCT. */
		private static final BinaryLayout IP4_STRUCT = structLayout(

				/* Word0 - 31:00 */
				structLayout( /* 31:00 */
						structLayout( /* 07:00 */
								Int8.BITS_04.withName("ip.hdr_len"), /* 03:00 */
								Int8.BITS_04.withName("ip.version") /* 07:04 */
						),

						/* 15:08 Detailed DS or TOS field */
						unionLayout(
								/* 15:08 - ds */
								unionLayout(
										Int8.BITS_08.withName("ip.dsfield"), /* 15:08 */
										sequenceLayout(8, Int8.BITS_01).withName("ip.dsfield.bits"), /* 15:08 */
										/* 15:08 */
										structLayout(
												Int8.BITS_00,
												/* 13:08 */
												unionLayout(
														Int8.BITS_06.withName("ip.dsfield.dscp"),
														/* 13:08 */
														structLayout(
																Int8.BITS_03.withName("ip.dsfield.dscp.select"),
																Int8.BITS_03.withName("ip.dsfield.dscp.code"))),
												/* 15:14 */
												Int8.BITS_02.withName("ip.dsfield.ecn"))),

								/* 15:08 - tos */
								unionLayout(
										Int8.BITS_08.withName("ip.tos"),
										sequenceLayout(8, Int8.BITS_01).withName("ip.tos.bits"),
										structLayout(
												Int8.BITS_03.withName("ip.tos.precedence"),
												Int8.BITS_01.withName("ip.tos.delay"),
												Int8.BITS_01.withName("ip.tos.throughput"),
												Int8.BITS_01.withName("ip.tos.reliability"),
												Int8.BITS_01.withName("ip.tos.cost"),
												Padding.BITS_01))),
						/* 31:16 - total len */
						Int16be.BITS_16.withName("ip.len")

				),

				/* Word1 */
				structLayout(
						Int16be.BITS_16.withName("ip.id"), /* 15:00 47:32 */

						/* 31:16 63:48 */
						unionLayout(
								unionLayout(
										Int16be.BITS_03.withName("ip.flags"),
										Int16be.BITS_04.withName("ip.flags_nibble"),
										Int16be.BITS_08.withName("ip.flags_byte"),
										Int8.BITS_00,
										structLayout(
												Int16be.BITS_01.withName("ip.flags.rb"),
												Int16be.BITS_01.withName("ip.flags.df"),
												Int16be.BITS_01.withName("ip.flags.mf"))),
								/* 31:16 63:48 */
								structLayout(
										Int16be.BITS_00, // Reset as 16-bit carrier
										Padding.BITS_03,
										Int16be.BITS_13.withName("ip.frag_offset")))

				),

				/* Word2 */
				structLayout(
						Int32be.BITS_08.withName("ip.ttl"),
						Int32be.BITS_08.withName("ip.proto"),
						Int32be.BITS_16.withName("ip.checksum")),

				/* Word3 - src */
				unionLayout(
						Int32be.BITS_32.withName("ip.src"),
						sequenceLayout(4, Int8.BITS_08).withName("ip.src.bytes"),
						sequenceLayout(32, Int8.BITS_01).withName("ip.src.bits")),

				/* Word4 - dst */
				unionLayout(
						Int32be.BITS_32.withName("ip.dst"),
						sequenceLayout(4, Int8.BITS_08).withName("ip.dst.bytes"),
						sequenceLayout(32, Int8.BITS_01).withName("ip.dst.bits")),

				structLayout(
						Int32be.BITS_32.withName("ip.word0"),
						Int32be.BITS_32.withName("ip.word1"),
						Int32be.BITS_32.withName("ip.word2"),
						Int32be.BITS_32.withName("ip.word3"),
						Int32be.BITS_32.withName("ip.word4")));

	}

	/** The Constant SRC_BYTES. */
	public static final ArrayField SRC_BYTES = Struct.IP4_STRUCT.arrayField("ip.src.bytes");

	/** The Constant DST_BYTES. */
	public static final ArrayField DST_BYTES = Struct.IP4_STRUCT.arrayField("ip.dst.bytes");

	/** The bits. */
	private final BitField bits;

	/**
	 * Instantiates a new ip 4 layout.
	 *
	 * @param layout    the layout
	 * @param path      the path
	 * @param formatter the formatter
	 */
	private Ip4Struct(BinaryLayout layout, String path, BitFieldFormatter formatter) {
		this.bits = layout.bitField(path)
				.formatted()
				.formatter(formatter);
	}

	/**
	 * Instantiates a new ip 4 layout.
	 *
	 * @param layout the layout
	 * @param path   the path
	 */
	private Ip4Struct(BinaryLayout layout, String path) {
		this.bits = layout.bitField(path)
				.formatted()
				.format("%d");
	}

	/**
	 * Instantiates a new ip 4 layout.
	 *
	 * @param layout the layout
	 * @param path   the path
	 * @param format the format
	 */
	private Ip4Struct(BinaryLayout layout, String path, String format) {
		this.bits = layout.bitField(path)
				.formatted()
				.format("%d");
	}

	/**
	 * Proxy bit field.
	 *
	 * @return the bit field
	 * @see com.slytechs.jnetpcap.pro.PacketDescriptorProxy.BitField.Proxy#proxyBitField()
	 */
	@Override
	public BitField proxyBitField() {
		return bits;
	}

}