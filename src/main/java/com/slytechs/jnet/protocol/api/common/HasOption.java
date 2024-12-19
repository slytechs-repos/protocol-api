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
 * Defines methods for checking and accessing protocol header options.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 * @param <T> the generic type
 */
public interface HasOption<T extends Header> {

	/**
	 * Gets a protocol header extension. A header extension with numerical id
	 * {@link Header#id()} is located and memory bound to parameter
	 * {@code extension}. This method does not return null and will throw an
	 * exception if header extension is not present.
	 *
	 * @param <E>    the element type
	 * @param option the protocol header extension instance
	 * @return the header extension, this method does not return null
	 * @throws HeaderNotFound if the protocol header is not found
	 */
	default <E extends T> E getOption(E option) throws HeaderNotFound {
		return getOption(option, 0);
	}

	/**
	 * Gets a protocol header extension. A header extension with numerical id
	 * {@link Header#id()} is located and memory bound to parameter
	 * {@code extension}. This method does not return null and will throw an
	 * exception if header extension is not present.
	 *
	 * @param <E>    the element type
	 * @param option the protocol header extension instance
	 * @param depth  the depth
	 * @return the header extension, this method does not return null
	 * @throws HeaderNotFound if the protocol header is not found
	 */
	<E extends T> E getOption(E option, int depth) throws HeaderNotFound;

	/**
	 * Checks if a particular protocol header extension is available.
	 *
	 * @param optionId the extension id
	 * @return true, if successful, otherwise false
	 */
	default boolean hasOption(int optionId) {
		return hasOption(optionId, 0);
	}

	/**
	 * Checks if a particular header option is available.
	 *
	 * @param optionId the option id
	 * @param depth    the inner tunnel depth for this protocol where a value of 0
	 *                 always accesses the outer most protocol header if protocol
	 *                 header at least one level deep exists
	 * @return true, if successful, otherwise false
	 */
	boolean hasOption(int optionId, int depth);

	/**
	 * Checks and performs memory binding if a particular protocol header extension
	 * is available. If a header extension is not available, no memory binding to
	 * protocol header extension is performed and false is returned.
	 *
	 * @param option the extension
	 * @return true, if header extension binding was successful, otherwise false
	 */
	default boolean hasOption(T option) {
		return peekOption(option, 0) != null;
	}

	/**
	 * Checks and performs memory binding if a particular protocol header extension
	 * is available. If a header extension is not available, no memory binding to
	 * protocol header extension is performed and false is returned.
	 *
	 * @param option the extension
	 * @param depth  the inner tunnel depth for this protocol where a value of 0
	 *               always accesses the outer most protocol header if protocol
	 *               header extension at least one level deep exists
	 * @return true, if header binding was successful, otherwise false
	 */
	default boolean hasOption(T option, int depth) {
		return peekOption(option, depth) != null;
	}

	/**
	 * Performs a peek and memory binding operation if a particular protocol header
	 * extension is available. If a header is not available, no memory binding to
	 * protocol header extension is performed and null is returned.
	 *
	 * @param <E>    the element type
	 * @param option the extension
	 * @return the header, if header binding was successful, otherwise null
	 */
	default <E extends T> E peekOption(E option) {
		return peekOption(option, 0);
	}

	/**
	 * Performs a peek and memory binding operation if a particular protocol header
	 * extension is available. If a header is not available, no memory binding to
	 * protocol header extension is performed and null is returned.
	 *
	 * @param <E>    the element type
	 * @param option the extension
	 * @param depth  the inner tunnel depth for this protocol where a value of 0
	 *               always accesses the outer most protocol header if protocol
	 *               header extension at least one level deep exists
	 * @return the header extension, if header binding was successful, otherwise
	 *         null
	 */
	<E extends T> E peekOption(E option, int depth);

}
