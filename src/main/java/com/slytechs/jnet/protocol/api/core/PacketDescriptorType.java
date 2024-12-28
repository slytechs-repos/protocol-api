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
package com.slytechs.jnet.protocol.api.core;

import java.util.function.Supplier;

import com.slytechs.jnet.protocol.api.descriptor.DescriptorType;
import com.slytechs.jnet.protocol.api.descriptor.PacketDescriptor;
import com.slytechs.jnet.protocol.api.descriptor.PcapDescriptor;
import com.slytechs.jnet.protocol.api.descriptor.Type1Descriptor;
import com.slytechs.jnet.protocol.api.descriptor.Type2Descriptor;

/**
 * A constant table of packet descriptor types supported by jNet modules.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public enum PacketDescriptorType implements DescriptorType<PacketDescriptor> {

	/** 16 bytes PCAP descriptor. */
	PCAP(DESCRIPTOR_TYPE_PCAP, PcapDescriptor::new),

	/** 16 byte descriptor, providing basic l2, l3 and l4 information. */
	TYPE1(DESCRIPTOR_TYPE_TYPE1, Type1Descriptor::new),

	/**
	 * 24-152 byte descriptor, providing RX and TX capabilities, as well as protocol
	 * dissection records.
	 */
	TYPE2(DESCRIPTOR_TYPE_TYPE2, Type2Descriptor::new),

	; // EOF Constant table

	/** The type. */
	private final int type;

	/** The factory. */
	private final Supplier<PacketDescriptor> factory;

	/**
	 * Instantiates a new packet descriptor type.
	 *
	 * @param type    the type
	 * @param factory the factory
	 */
	PacketDescriptorType(int type, Supplier<PacketDescriptor> factory) {
		this.factory = factory;
		this.type = type;
	}

	/**
	 * Gets an equivalent numerical constant for this descriptor type. Suitable in
	 * passing to native jNet native library functions.
	 *
	 * @return numerical equivalent
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return type;
	}

	/**
	 * New descriptor.
	 *
	 * @return the packet descriptor
	 */
	@Override
	public PacketDescriptor newDescriptor() {
		return factory.get();
	}
}
