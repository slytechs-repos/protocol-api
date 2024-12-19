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

import com.slytechs.jnet.protocol.api.packet.Packet;

/**
 * Defines methods for checking and accessing protocol headers.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public sealed interface HasHeader permits Packet { 

	/**
	 * Gets a protocol header. A header with numerical id {@link Header#id()} is
	 * located and memory bound to parameter {@code header}. This method does not
	 * return null and will throw an exception if header is not present.
	 *
	 * @param <T>    the generic protocol header type
	 * @param header the protocol header instance
	 * @return the header, this method does not return null
	 * @throws HeaderNotFound if the protocol header is not found
	 */
	default <T extends Header> T getHeader(T header) throws HeaderNotFound {
		return getHeader(header, 0);
	}

	/**
	 * Gets a protocol header. A header with numerical id {@link Header#id()} is
	 * located and memory bound to parameter {@code header}. This method does not
	 * return null and will throw an exception if header is not present.
	 *
	 * @param <T>    the generic protocol header type
	 * @param header the protocol header instance
	 * @param depth  the inner tunnel depth for this protocol where a value of 0
	 *               always accesses the outer most protocol header if protocol
	 *               header at least one level deep exists
	 * @return the header, this method does not return null
	 * @throws HeaderNotFound if the protocol header is not found
	 */
	<T extends Header> T getHeader(T header, int depth) throws HeaderNotFound;

	/**
	 * Checks if a particular protocol header is available.
	 *
	 * @param headerId the numerical header id for the requested header
	 * @return true, if successful, otherwise false
	 */
	default boolean hasHeader(int headerId) {
		return hasHeader(headerId, 0);
	}

	/**
	 * Checks if a particular protocol header is available.
	 *
	 * @param headerId the numerical header id for the requested header
	 * @param depth    the inner tunnel depth for this protocol where a value of 0
	 *                 always accesses the outer most protocol header if protocol
	 *                 header at least one level deep exists
	 * @return true, if successful, otherwise false
	 */
	boolean hasHeader(int headerId, int depth);

	/**
	 * Checks and performs memory binding if a particular protocol header is
	 * available. If a header is not available, no memory binding to protocol header
	 * is performed and false is returned.
	 *
	 * @param <T>    the generic protocol header type
	 * @param header the unbound protocol header instance
	 * @return true, if header binding was successful, otherwise false
	 */
	default <T extends Header> boolean hasHeader(T header) {
		return peekHeader(header, 0) != null;
	}

	/**
	 * Checks and performs memory binding if a particular protocol header is
	 * available. If a header is not available, no memory binding to protocol header
	 * is performed and false is returned.
	 *
	 * @param <T>    the generic protocol header type
	 * @param header the unbound protocol header instance
	 * @param depth  the inner tunnel depth for this protocol where a value of 0
	 *               always accesses the outer most protocol header if protocol
	 *               header at least one level deep exists
	 * @return true, if header binding was successful, otherwise false
	 */
	default <T extends Header> boolean hasHeader(T header, int depth) {
		return peekHeader(header, depth) != null;
	}

	/**
	 * Performs a peek and memory binding operation if a particular protocol header
	 * is available. If a header is not available, no memory binding to protocol
	 * header is performed and null is returned.
	 *
	 * @param <T>    the generic protocol header type
	 * @param header the unbound protocol header instance
	 * @return the header, if header binding was successful, otherwise null
	 */
	default <T extends Header> T peekHeader(T header) {
		return peekHeader(header, 0);
	}

	/**
	 * Performs a peek and memory binding operation if a particular protocol header
	 * is available. If a header is not available, no memory binding to protocol
	 * header is performed and null is returned.
	 *
	 * @param <T>    the generic protocol header type
	 * @param header the unbound protocol header instance
	 * @param depth  the inner tunnel depth for this protocol where a value of 0
	 *               always accesses the outer most protocol header if protocol
	 *               header at least one level deep exists
	 * @return the header, if header binding was successful, otherwise null
	 */
	<T extends Header> T peekHeader(T header, int depth);

}
