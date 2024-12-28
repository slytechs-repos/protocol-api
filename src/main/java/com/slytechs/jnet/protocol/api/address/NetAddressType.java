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
 * Enumeration defining various types of network addresses. This enum provides
 * constants representing common address types such as IPv4, IPv6, MAC, and
 * more. Each type can be represented as an integer value corresponding to its
 * ordinal position.
 * 
 * <p>
 * Implements {@link java.util.function.IntSupplier} to supply the ordinal value
 * as an integer.
 * </p>
 * 
 * @author Sly Technologies Inc.
 */
public enum NetAddressType implements java.util.function.IntSupplier {

	/** IPv4 address type. */
	IPv4,

	/** IPv6 address type. */
	IPv6,

	/** MAC (Media Access Control) address type. */
	MAC,

	/** MAC-64 (64-bit MAC) address type. */
	MAC64,

	/** IPX (Internetwork Packet Exchange) address type. */
	IPX,

	/** AppleTalk address type. */
	APPLETALK;

	/**
	 * Returns the ordinal value of the address type as an integer.
	 *
	 * @return the ordinal value of this address type
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return ordinal();
	}
}
