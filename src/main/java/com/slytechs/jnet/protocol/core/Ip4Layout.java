/*
 * Apache License, Version 2.0
 * 
 * Copyright 2013-2022 Sly Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slytechs.jnet.protocol.core;

import static com.slytechs.jnet.runtime.internal.layout.BinaryLayout.*;

import com.slytechs.jnet.runtime.internal.layout.ArrayField;
import com.slytechs.jnet.runtime.internal.layout.BinaryLayout;
import com.slytechs.jnet.runtime.internal.layout.BitField;
import com.slytechs.jnet.runtime.internal.layout.EnumBitField;
import com.slytechs.jnet.runtime.internal.layout.FormattedBitField.BitFieldFormatter;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int16be;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int32be;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Int8;
import com.slytechs.jnet.runtime.internal.layout.PredefinedLayout.Padding;

public enum Ip4Layout implements EnumBitField<Ip4Layout> {
	VERSION(Struct.IP4_STRUCT, "ip.version"),
	HDR_LEN(Struct.IP4_STRUCT, "ip.hdr_len"),
	DSFIELD(Struct.IP4_STRUCT, "ip.dsfield"),
	DSFIELD_DSCP(Struct.IP4_STRUCT, "ip.dsfield.dscp"),
	DSFIELD_DSCP_SELECT(Struct.IP4_STRUCT, "ip.dsfield.dscp.select"),
	DSFIELD_DSCP_CODE(Struct.IP4_STRUCT, "ip.dsfield.dscp.code"),
	DSFIELD_ECN(Struct.IP4_STRUCT, "ip.dsfield.ecn"),
	TOTAL_LENGTH(Struct.IP4_STRUCT, "ip.len"),
	ID(Struct.IP4_STRUCT, "ip.id", "0x%x (%1$d)"),
	FLAGS(Struct.IP4_STRUCT, "ip.flags"),
	FLAGS_NIBBLE(Struct.IP4_STRUCT, "ip.flags_nibble"),
	FLAGS_BYTE(Struct.IP4_STRUCT, "ip.flags_byte"),
	FLAGS_RB(Struct.IP4_STRUCT, "ip.flags.rb"),
	FLAGS_DF(Struct.IP4_STRUCT, "ip.flags.df"),
	FLAGS_MF(Struct.IP4_STRUCT, "ip.flags.mf"),
	FRAG_OFFSET(Struct.IP4_STRUCT, "ip.frag_offset"),
	TTL(Struct.IP4_STRUCT, "ip.ttl"),
	PROTO(Struct.IP4_STRUCT, "ip.proto"),
	CHECKSUM(Struct.IP4_STRUCT, "ip.checksum"),
	SRC(Struct.IP4_STRUCT, "ip.src"),
	DST(Struct.IP4_STRUCT, "ip.dst"),

	HEADER_WORD0(Struct.IP4_STRUCT, "ip.word0", "%08X"),
	HEADER_WORD1(Struct.IP4_STRUCT, "ip.word1", "%08X"),
	HEADER_WORD2(Struct.IP4_STRUCT, "ip.word2", "%08X"),
	HEADER_WORD3(Struct.IP4_STRUCT, "ip.word3", "%08X"),
	HEADER_WORD4(Struct.IP4_STRUCT, "ip.word4", "%08X");

	private static class Struct {

		private static final BinaryLayout IP4_STRUCT = structLayout(

				/* Word0 - 31:00 */
				structLayout( /* 31:00 */
						structLayout( /* 07:00 */
								Int8.BITS_04.withName("ip.version"), /* 03:00 */
								Int8.BITS_04.withName("ip.hdr_len")), /* 07:04 */

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

	public static final ArrayField SRC_BYTES = Struct.IP4_STRUCT.arrayField("ip.src.bytes");
	public static final ArrayField DST_BYTES = Struct.IP4_STRUCT.arrayField("ip.dst.bytes");

	private final BitField bits;

	private Ip4Layout(BinaryLayout layout, String path, BitFieldFormatter formatter) {
		this.bits = layout.bitField(path)
				.formatted()
				.formatter(formatter);
	}

	private Ip4Layout(BinaryLayout layout, String path) {
		this.bits = layout.bitField(path)
				.formatted()
				.format("%d");
	}

	private Ip4Layout(BinaryLayout layout, String path, String format) {
		this.bits = layout.bitField(path)
				.formatted()
				.format("%d");
	}

	/**
	 * @see com.slytechs.jnet.layout.BitField.Proxy#proxyBitField()
	 */
	@Override
	public BitField proxyBitField() {
		return bits;
	}

}