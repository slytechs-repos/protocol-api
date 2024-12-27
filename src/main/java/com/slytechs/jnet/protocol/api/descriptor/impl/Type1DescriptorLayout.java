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
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int16;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int64;

/**
 * Type2 struct/layout definition.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public enum Type1DescriptorLayout implements BitField.Proxy {

	/** The timestamp. */
	TIMESTAMP("timestamp"),

	/** The capture length or at snaplen */
	CAPLEN("caplen"),
	L2_FRAME_TYPE("l2_frame_type"),
	L3_OFFSET("l3_offset"),
	L3_SIZE("l3_size"),

	/** The length as seen on the wire */
	WIRELEN("wirelen"),
	VLAN_COUNT("vlan_count"),
	MPLS_COUNT("mpls_count"),
	L3_FRAME_TYPE("l3_frame_type"),
	L4_FRAME_TYPE("l4_frame_type"),
	L4_SIZE("l4_size"),

	;

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant STRUCT. */
		private static final BinaryLayout STRUCT = structLayout(

				/* Word0&1 */
				Int64.BITS_64.withName("timestamp"),

				/* Word2 */
				Int16.BITS_16.withName("caplen"),
				Int16.BITS_02.withName("l2_frame_type"),
				Int16.BITS_07.withName("l3_offset"),
				Int16.BITS_07.withName("l3_size"),

				/* Word3 */
				Int16.BITS_16.withName("wirelen"),
				Int16.BITS_02.withName("vlan_count"),
				Int16.BITS_03.withName("mpls_count"),
				Int16.BITS_03.withName("l3_frame_type"),
				Int16.BITS_04.withName("l4_frame_type"),
				Int16.BITS_04.withName("l4_size")

		);
	}

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new type 2 layout.
	 *
	 * @param path the path
	 */
	Type1DescriptorLayout(String path) {
		this.field = Struct.STRUCT.bitField(path);
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
