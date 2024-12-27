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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet;

import static com.slytechs.jnet.platform.api.memory.layout.BinaryLayout.*;

import com.slytechs.jnet.platform.api.memory.layout.BinaryLayout;
import com.slytechs.jnet.platform.api.memory.layout.BitField;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int8;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Padding;

/**
 * VLAN header format layout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
enum LlcLayout implements BitField.Proxy {

	/** The dsap. */
	DSAP("dsap"),
	
	/** The ssap. */
	SSAP("ssap"),
	
	/** The control. */
	CONTROL("control"),

	;

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant HEADER. */
		private final static BinaryLayout STRUCT = structLayout(

				/* Word0 */
				Int8.BITS_08.withName("dsap"),
				Int8.BITS_08.withName("ssap"),
				Int8.BITS_08.withName("control"),

				Padding.BITS_00);
	}

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new ip 6 layout.
	 *
	 * @param path the path
	 */
	LlcLayout(String path) {
		this.field = Struct.STRUCT.bitField(path);
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
