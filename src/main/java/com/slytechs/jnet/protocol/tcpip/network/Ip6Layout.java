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

import static com.slytechs.jnet.platform.api.memory.layout.BinaryLayout.*;

import com.slytechs.jnet.platform.api.memory.layout.ArrayField;
import com.slytechs.jnet.platform.api.memory.layout.BinaryLayout;
import com.slytechs.jnet.platform.api.memory.layout.BitField;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int16be;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int32be;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int64be;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int8;

/**
 * The Enum Ip6Layout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
enum Ip6Layout implements BitField.Proxy {
	
	/** The version. */
	VERSION("ip.version"),
	
	/** The dsfield. */
	DSFIELD("ip.dsfield"),
	
	/** The flow. */
	FLOW("ip.flow"),
	
	/** The payload length. */
	PAYLOAD_LENGTH("ip.plen"),
	
	/** The next. */
	NEXT("ip.nxt"),
	
	/** The hop limit. */
	HOP_LIMIT("ip.hlim"),
	
	/** The src as int. */
	SRC_AS_INT("ip.src.ints"),
	
	/** The dst as int. */
	DST_AS_INT("ip.dst.ints"),
	
	/** The src as long. */
	SRC_AS_LONG("ip.src.longs"),
	
	/** The dst as long. */
	DST_AS_LONG("ip.dst.longs");

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant HEADER. */
		private final static BinaryLayout IP6_STRUCT = structLayout(

				/* Word0 */
				unionLayout(
						Int32be.BITS_04.withName("ip.version"),
						unionLayout(
								structLayout(
										Int32be.BITS_04, // ignore
										Int32be.BITS_08.withName("ip.dsfield"),
										Int32be.BITS_20.withName("ip.flow")),
								structLayout(
										Int32be.BITS_04, // ignore
										Int32be.BITS_06.withName("ip.dsfield.dscp"),
										Int32be.BITS_03.withName("ip.dsfield.ecn")),
								structLayout(
										Int32be.BITS_04, // ignore
										Int32be.BITS_03.withName("ip.dsfield.dscp.select"),
										Int32be.BITS_03.withName("ip.dsfield.dscp.code")))),

				/* Word1 */
				Int16be.BITS_16.withName("ip.plen"),
				Int8.BITS_08.withName("ip.nxt"),
				Int8.BITS_08.withName("ip.hlim"),

				/* Word2,3,4,5 */
				unionLayout( /* Alternative address layouts */
						sequenceLayout(16, Int8.BITS_08).withName("ip.src"),
						sequenceLayout(128, Int8.BITS_01).withName("ip.src.bits"),
						sequenceLayout(4, Int32be.BITS_32).withName("ip.src.ints"),
						sequenceLayout(2, Int64be.BITS_64).withName("ip.src.longs")),

				/* Word6,7,8,9 */
				unionLayout( /* Alternative address layouts */
						sequenceLayout(16, Int8.BITS_08).withName("ip.dst"),
						sequenceLayout(128, Int8.BITS_01).withName("ip.dst.bits"),
						sequenceLayout(4, Int32be.BITS_32).withName("ip.dst.ints"),
						sequenceLayout(2, Int64be.BITS_64).withName("ip.dst.longs")));
	}

	/** The Constant SRC_BYTES. */
	public static final ArrayField SRC_BYTES = Struct.IP6_STRUCT.arrayField("ip.src");
	
	/** The Constant DST_BYTES. */
	public static final ArrayField DST_BYTES = Struct.IP6_STRUCT.arrayField("ip.dst");

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new ip 6 layout.
	 *
	 * @param path the path
	 */
	Ip6Layout(String path) {
		this.field = Struct.IP6_STRUCT.bitField(path);
	}

	/**
	 * Proxy bit field.
	 *
	 * @return the bit field
	 * @see com.slytechs.jnetpcap.pro.PacketDescriptorProxy.BitField.Proxy#proxyBitField()
	 */
	@Override
	public BitField proxyBitField() {
		return field;
	}
}
