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
 * Interface representing a descriptor type, defining various constants for
 * descriptor identification and providing methods for working with descriptor
 * types.
 * 
 * <p>
 * A descriptor type specifies the format and purpose of a descriptor used to
 * represent network data and protocol metadata. Implementations of this
 * interface can generate new descriptors of the specified type and retrieve
 * their integer representation.
 * </p>
 * 
 * @param <T> the type of descriptor this descriptor type generates
 * @author Sly Technologies Inc.
 */
public interface DescriptorType<T extends Descriptor> {

	/**
	 * PCAP Descriptor Type: A 16-byte descriptor for packet capture.
	 */
	int DESCRIPTOR_TYPE_PCAP = 0;

	/**
	 * Type 1 Descriptor: A 16-byte descriptor for core protocol metadata.
	 */
	int DESCRIPTOR_TYPE_TYPE1 = 1;

	/**
	 * Type 2 Descriptor: A variable-length descriptor for full packet dissection.
	 */
	int DESCRIPTOR_TYPE_TYPE2 = 2;

	/**
	 * IP Fragmentation Descriptor: Describes IP fragmentation and reassembly data.
	 */
	int DESCRIPTOR_TYPE_IPF_FRAG = 20;

	/**
	 * IP Fragmentation Tracking Descriptor: Tracks IP fragmentation progress.
	 */
	int DESCRIPTOR_TYPE_IPF_TRACKING = 21;

	/**
	 * IP Reassembly Descriptor: Represents IP fragmentation reassembly information.
	 */
	int DESCRIPTOR_TYPE_IPF_REASSEMBLY = 22;

	/**
	 * IP Buffer Descriptor: Represents an IP buffer for fragmentation and
	 * reassembly.
	 */
	int DESCRIPTOR_TYPE_IPF_BUFFER = 23;

	/**
	 * NT Standard Descriptor: A 16-byte descriptor for NT standard protocols.
	 */
	int DESCRIPTOR_TYPE_NT_STD = 100;

	/**
	 * Retrieves the integer representation of the descriptor type.
	 *
	 * @return the integer value representing this descriptor type
	 */
	int getAsInt();

	/**
	 * Creates a new descriptor instance of this type.
	 *
	 * @return a new instance of the descriptor corresponding to this type
	 */
	T newDescriptor();
}
