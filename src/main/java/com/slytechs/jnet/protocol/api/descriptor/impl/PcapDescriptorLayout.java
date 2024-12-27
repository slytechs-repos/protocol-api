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
import com.slytechs.jnet.platform.api.memory.layout.BitField;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int32;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int64;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Padding;

/**
 * The Enum PcapLayout.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public enum PcapDescriptorLayout implements BitField.Proxy {

	/** The timestamp. */
	TIMESTAMP("timestamp"),
	
	/** The caplen. */
	CAPLEN("caplen"),
	
	/** The wirelen. */
	WIRELEN("wirelen"),

	;

	/**
	 * The Class Struct.
	 */
	private static class Struct {
		
		/** The Constant PCAP_STRUCT. */
		private static final BinaryLayout PCAP_STRUCT = structLayout(
				Int64.BITS_64.withName("timestamp"),
				Int32.BITS_16.withName("caplen"),
				Padding.BITS_16,
				Int32.BITS_16.withName("wirelen"),
				Padding.BITS_16);
	}

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new pcap layout.
	 *
	 * @param path the path
	 */
	PcapDescriptorLayout(String path) {
		this.field = Struct.PCAP_STRUCT.bitField(path);
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
