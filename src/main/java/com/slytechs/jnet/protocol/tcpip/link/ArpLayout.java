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
package com.slytechs.jnet.protocol.tcpip.link;

import static com.slytechs.jnet.platform.api.memory.layout.BinaryLayout.*;

import com.slytechs.jnet.platform.api.memory.layout.ArrayField;
import com.slytechs.jnet.platform.api.memory.layout.BinaryLayout;
import com.slytechs.jnet.platform.api.memory.layout.BitField;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int32be;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Int8;
import com.slytechs.jnet.platform.api.memory.layout.PredefinedLayout.Padding;

/**
 * ARP header format layout.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
enum ArpLayout implements BitField.Proxy {

	/** Hardware type. */
	HTYPE("hardware_type"),

	/** Protocol type. */
	PTYPE("protocol_type"),

	/** Hardware address size. */
	HSIZE("hardware_size"),

	/** Protocol address size. */
	PSIZE("protocol_size"),

	/** Operation. */
	OPCODE("opcode"),

	;

	/**
	 * The Class Struct.
	 */
	private static class Struct {

		/** The Constant HEADER. */
		private final static BinaryLayout STRUCT = structLayout(

				/* Word0 */
				Int32be.BITS_16.withName("hardware_type"),
				Int32be.BITS_16.withName("protocol_type"),

				/* Word1 */
				Int32be.BITS_08.withName("hardware_size"),
				Int32be.BITS_08.withName("protocol_size"),
				Int32be.BITS_16.withName("opcode"),

				sequenceLayout(6, Int8.BITS_08).withName("sender_ha"),
				sequenceLayout(4, Int8.BITS_08).withName("sender_pa"),
				sequenceLayout(6, Int8.BITS_08).withName("target_ha"),
				sequenceLayout(4, Int8.BITS_08).withName("target_pa"),

				Padding.BITS_00);
	}

	/** Sender hardware address. */
	public static final ArrayField SHA = Struct.STRUCT.arrayField("sender_ha");

	/** Sender protocol address. */
	public static final ArrayField SPA = Struct.STRUCT.arrayField("sender_pa");

	/** Target hardware address. */
	public static final ArrayField THA = Struct.STRUCT.arrayField("target_ha");

	/** Target protocol address. */
	public static final ArrayField TPA = Struct.STRUCT.arrayField("target_pa");

	/** The field. */
	private final BitField field;

	/**
	 * Instantiates a new ip 6 layout.
	 *
	 * @param path the path
	 */
	ArpLayout(String path) {
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
