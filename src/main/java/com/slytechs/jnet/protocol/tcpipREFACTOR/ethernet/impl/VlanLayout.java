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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.impl;

import static com.slytechs.jnet.platform.api.memory.layout.BinaryLayout.*;

import com.slytechs.jnet.platform.api.memory.layout.BinaryLayout;
import com.slytechs.jnet.platform.api.memory.layout.BitField;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int16be;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Padding;

/**
 * ARP header format layout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public enum VlanLayout implements BitField.Proxy {

	/** Hardware type. */
	PRI("pri"),
	
	/** The cfi. */
	CFI("cfi"),
	
	/** The vid. */
	VID("vid"),
	
	/** The type. */
	TYPE("type")

	;

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant HEADER. */
		private final static BinaryLayout STRUCT = structLayout(

				/* Word0 */
				Int16be.BITS_03.withName("pri"),
				Int16be.BITS_01.withName("cfi"),
				Int16be.BITS_12.withName("vid"),
				Int16be.BITS_16.withName("type"),

				Padding.BITS_00);
	}

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new ip 6 layout.
	 *
	 * @param path the path
	 */
	VlanLayout(String path) {
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
