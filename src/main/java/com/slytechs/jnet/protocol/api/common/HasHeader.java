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
 * Interface for accessing and verifying the presence of protocol headers.
 * 
 * <p>
 * This interface provides methods to check for the availability of protocol
 * headers and perform operations such as memory binding and peeking into the
 * headers of encapsulated protocols. It supports handling of nested (tunneled)
 * protocols through a depth parameter.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 * <li>Retrieve a protocol header and bind memory to it.</li>
 * <li>Check the existence of a header by ID or instance.</li>
 * <li>Support for protocol headers in nested tunneling scenarios.</li>
 * <li>Perform peek operations for non-binding header checks.</li>
 * </ul>
 * 
 * <h2>Implemented By:</h2>
 * <ul>
 * <li>{@link Packet}</li>
 * </ul>
 * 
 * @see Header
 * @see HeaderNotFound
 * @see Packet
 * @author Sly Technologies
 * @version 2023
 */
public sealed interface HasHeader permits Packet {

	/**
	 * Retrieves a protocol header and binds memory to it. If the header is not
	 * found, this method throws a {@link HeaderNotFound} exception.
	 * 
	 * @param <T>    the protocol header type
	 * @param header the header instance to be retrieved and bound
	 * @return the memory-bound header instance (never null)
	 * @throws HeaderNotFound if the protocol header is not found
	 */
	default <T extends Header> T getHeader(T header) throws HeaderNotFound {
		return getHeader(header, 0);
	}

	/**
	 * Retrieves a protocol header at a specified tunnel depth and binds memory to
	 * it. If the header is not found, this method throws a {@link HeaderNotFound}
	 * exception.
	 * 
	 * @param <T>    the protocol header type
	 * @param header the header instance to be retrieved and bound
	 * @param depth  the tunnel depth (0 for outermost header)
	 * @return the memory-bound header instance (never null)
	 * @throws HeaderNotFound if the protocol header is not found
	 */
	<T extends Header> T getHeader(T header, int depth) throws HeaderNotFound;

	/**
	 * Checks if a protocol header is available by its ID.
	 * 
	 * @param headerId the ID of the protocol header to check
	 * @return true if the header is available, false otherwise
	 */
	default boolean hasHeader(int headerId) {
		return hasHeader(headerId, 0);
	}

	/**
	 * Checks if a protocol header is available by its ID and depth.
	 * 
	 * @param headerId the ID of the protocol header to check
	 * @param depth    the tunnel depth (0 for outermost header)
	 * @return true if the header is available, false otherwise
	 */
	boolean hasHeader(int headerId, int depth);

	/**
	 * Checks if a protocol header is available and binds memory to it if available.
	 * 
	 * @param <T>    the protocol header type
	 * @param header the header instance to check and bind
	 * @return true if the header is available and binding was successful, false
	 *         otherwise
	 */
	default <T extends Header> boolean hasHeader(T header) {
		return peekHeader(header, 0) != null;
	}

	/**
	 * Checks if a protocol header is available at a specified depth and binds
	 * memory to it if available.
	 * 
	 * @param <T>    the protocol header type
	 * @param header the header instance to check and bind
	 * @param depth  the tunnel depth (0 for outermost header)
	 * @return true if the header is available and binding was successful, false
	 *         otherwise
	 */
	default <T extends Header> boolean hasHeader(T header, int depth) {
		return peekHeader(header, depth) != null;
	}

	/**
	 * Performs a peek operation to check if a protocol header is available. If
	 * available, binds memory to it.
	 * 
	 * @param <T>    the protocol header type
	 * @param header the header instance to peek
	 * @return the memory-bound header instance if available, otherwise null
	 */
	default <T extends Header> T peekHeader(T header) {
		return peekHeader(header, 0);
	}

	/**
	 * Performs a peek operation at a specified depth to check if a protocol header
	 * is available. If available, binds memory to it.
	 * 
	 * @param <T>    the protocol header type
	 * @param header the header instance to peek
	 * @param depth  the tunnel depth (0 for outermost header)
	 * @return the memory-bound header instance if available, otherwise null
	 */
	<T extends Header> T peekHeader(T header, int depth);
}
