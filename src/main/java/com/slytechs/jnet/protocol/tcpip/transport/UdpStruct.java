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
package com.slytechs.jnet.protocol.tcpip.transport;

import static com.slytechs.jnet.platform.api.memory.layout.BinaryLayout.*;

import com.slytechs.jnet.platform.api.memory.layout.ArrayField;
import com.slytechs.jnet.platform.api.memory.layout.BinaryLayout;
import com.slytechs.jnet.platform.api.memory.layout.BitField;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int16be;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int8;
import com.slytechs.jnet.protocol.tcpip.constants.CoreConstants;

/**
 * The Enum UdpStruct.
 */
public enum UdpStruct implements BitField.Proxy {
	
	/** The src port. */
	SRC_PORT("udp.srcport"),
	
	/** The dst port. */
	DST_PORT("udp.dstport"),
	
	/** The length. */
	LENGTH("udp.length"),
	
	/** The checksum. */
	CHECKSUM("udp.checksum");

	/**
	 * The Class Struct.
	 */
	private static class Struct {
		
		/** The Constant UDP_STRUCT. */
		private static final BinaryLayout UDP_STRUCT = unionLayout(
				structLayout(
						Int16be.BITS_16.withName("udp.srcport"),
						Int16be.BITS_16.withName("udp.dstport"),
						Int16be.BITS_16.withName("udp.length"),
						Int16be.BITS_16.withName("udp.checksum")),
				sequenceLayout(CoreConstants.UDP_HEADER_LEN, Int8.BITS_08).withName("udp.bytes"));
	}

	/** The Constant HEADER_BYTES. */
	public static final ArrayField HEADER_BYTES = Struct.UDP_STRUCT.arrayField("udp.bytes");

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new udp struct.
	 *
	 * @param path the path
	 */
	UdpStruct(String path) {
		this.field = Struct.UDP_STRUCT.bitField(path);
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
