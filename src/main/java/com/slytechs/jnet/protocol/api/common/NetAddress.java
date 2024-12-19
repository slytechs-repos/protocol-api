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
package com.slytechs.jnet.protocol.api.common;

/**
 * An address interface implemented by all defined core-protocol-pack network
 * addresses.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface NetAddress {

	/**
	 * A type of address.
	 *
	 * @return the address type constant
	 */
	NetAddressType type();

	/**
	 * A binary representation of this address.
	 * <p>
	 * The length of the returned array depends on type of of address implementing
	 * this interface and its native address size in bytes.
	 *
	 * @return an array containing this address
	 */
	byte[] toArray();

	/**
	 * Address size in bits.
	 *
	 * @return number of bits
	 */
	default int bitSize() {
		return byteSize() << 3;
	}

	/**
	 * Address size in bytes.
	 *
	 * @return the number of bytes
	 */
	int byteSize();
}
