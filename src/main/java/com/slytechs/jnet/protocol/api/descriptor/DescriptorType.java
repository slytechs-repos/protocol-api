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
package com.slytechs.jnet.protocol.api.descriptor;

/**
 * The Interface DescriptorType.
 *
 * @param <T> the generic type
 */
public interface DescriptorType<T extends Descriptor> {

	/** Either 16 bytes. */
	int DESCRIPTOR_TYPE_PCAP = 0;

	/** 16 byte CORE_PROTOCOLS descriptor. */
	int DESCRIPTOR_TYPE_TYPE1 = 1;

	/** The Constant DESCRIPTOR_TYPE_TYPE2. */
	int DESCRIPTOR_TYPE_TYPE2 = 2;

	/** IP fragmentation reassembly descriptor. */
	int DESCRIPTOR_TYPE_IPF_FRAG = 20;

	/** IP Fragmentation tracking descriptor. */
	int DESCRIPTOR_TYPE_IPF_TRACKING = 21;

	/** IP Fragmentation reassembly descriptor. */
	int DESCRIPTOR_TYPE_IPF_REASSEMBLY = 22;

	/** The descriptor type ipf buffer. */
	int DESCRIPTOR_TYPE_IPF_BUFFER = 23;

	/** 16 byte NT STD descriptor. */
	int DESCRIPTOR_TYPE_NT_STD = 100;

	/**
	 * Gets the as int.
	 *
	 * @return the as int
	 */
	int getAsInt();

	/**
	 * New descriptor.
	 *
	 * @return the t
	 */
	T newDescriptor();

}