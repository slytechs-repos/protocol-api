/*
 * Sly Technologies Free License
 * 
 * Copyright 2024 Sly Technologies Inc.
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
package com.slytechs.jnet.protocol.tcpip.link;

import com.slytechs.jnet.jnetruntime.internal.layout.BinaryLayout;
import com.slytechs.jnet.jnetruntime.internal.layout.BitField;
import com.slytechs.jnet.jnetruntime.internal.layout.PredefinedLayout.Int64be;
import com.slytechs.jnet.jnetruntime.internal.layout.PredefinedLayout.Padding;

/**
 * PPPoE header format layout definition.
 * 
 * @author Mark Bednarczyk
 */
public /**
		 * PPP header format layout definition.
		 */
enum PppLayout implements BitField.Proxy {

	/** Flag field (8 bits) */
	FLAG("flag"),

	/** Address field (8 bits) */
	ADDRESS("address"),

	/** Control field (8 bits) */
	CONTROL("control"),

	/** Protocol field (16 bits) */
	PROTOCOL("protocol");

	/**
	 * The PPP header structure layout definition.
	 */
	private static class Struct {

		private final static BinaryLayout STRUCT = BinaryLayout.structLayout(

				/* Word0 - Flag (8 bits) + Address (8 bits) + Control (8 bits) */
				Int64be.BITS_08.withName("flag"),
				Int64be.BITS_08.withName("address"),
				Int64be.BITS_08.withName("control"),

				/* Word1 - Protocol (16 bits) */
				Int64be.BITS_16.withName("protocol"),

				Padding.BITS_00);
	}

	private final BitField field;

	/**
	 * Instantiates a new PPP layout field.
	 *
	 * @param path the field path name
	 */
	PppLayout(String path) {
		this.field = Struct.STRUCT.bitField(path);
	}

	/**
	 * Gets the proxy bit field.
	 *
	 * @return the bit field
	 */
	@Override
	public BitField proxyBitField() {
		return field;
	}
}
