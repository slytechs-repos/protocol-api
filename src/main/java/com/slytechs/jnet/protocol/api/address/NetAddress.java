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
package com.slytechs.jnet.protocol.api.address;

/**
 * Interface representing a network address. This interface is implemented by
 * all core-protocol-pack network addresses, providing uniform methods to access
 * address details such as type, binary representation, and size.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface NetAddress {

	/**
	 * Retrieves the type of this network address.
	 *
	 * @return the address type constant
	 */
	NetAddressType type();

	/**
	 * Returns a binary representation of this address.
	 * <p>
	 * The length of the returned array depends on the type of address implementing
	 * this interface and its native size in bytes.
	 *
	 * @return an array containing this address
	 */
	byte[] toArray();

	/**
	 * Retrieves the size of this address in bits.
	 *
	 * @return the number of bits in the address
	 */
	default int bitSize() {
		return byteSize() << 3;
	}

	/**
	 * Retrieves the size of this address in bytes.
	 *
	 * @return the number of bytes in the address
	 */
	int byteSize();
}
