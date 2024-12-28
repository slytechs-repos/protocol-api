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

import com.slytechs.jnet.protocol.api.common.impl.HeaderFactoryAllocator;
import com.slytechs.jnet.protocol.api.common.impl.HeaderFactorySyncLocalImpl;

/**
 * A factory for creating Header objects based on numerical IDs.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public interface HeaderFactory {

	/**
	 * A header factory which allocates a new header each time requested. Performs
	 * no caching.
	 *
	 * @return the header factory
	 */
	static HeaderFactory newInstance() {
		return new HeaderFactoryAllocator();
	}

	/**
	 * A reusable instance of each header will be created and cached. A thread
	 * synchronization will be performed on each header, allowing multiple headers
	 * to be gotten at the same time, but for locked headers, a requesting thread
	 * will wait until that particular header is released.
	 *
	 * @return the header factory
	 */
	static HeaderFactory syncLocal() {
		return new HeaderFactorySyncLocalImpl();
	}

	/**
	 * Gets the header.
	 *
	 * @param <H>    the generic type
	 * @param header the header
	 * @return the header
	 */
	@SuppressWarnings("unchecked")
	default <H extends Header> H getHeader(HeaderInfo header) {
		return (H) get(header.id());
	}

	/**
	 * Gets the exension.
	 *
	 * @param <H>       the generic type
	 * @param primary   the primary
	 * @param extension the extension
	 * @return the exension
	 */
	@SuppressWarnings("unchecked")
	default <H extends Header> H getExension(HeaderInfo primary, HeaderOptionInfo extension) {
		return (H) getExtension(primary.id(), extension.id());
	}

	/**
	 * Gets the.
	 *
	 * @param id the id
	 * @return the header
	 */
	Header get(int id);

	/**
	 * Gets the header.
	 *
	 * @param <H>  the generic type
	 * @param id   the id
	 * @param type the type
	 * @return the header
	 */
	@SuppressWarnings("unchecked")
	default <H extends Header> H getHeader(int id, Class<H> type) {
		return (H) get(id);
	}

	/**
	 * Gets the extension.
	 *
	 * @param primaryId   the primary id
	 * @param extensionId the extension id
	 * @return the extension
	 */
	default Header getExtension(int primaryId, int extensionId) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Gets the.
	 *
	 * @param <H>         the generic type
	 * @param primaryId   the primary id
	 * @param extensionId the extension id
	 * @param type        the type
	 * @return the h
	 */
	@SuppressWarnings("unchecked")
	default <H extends Header> H get(int primaryId, int extensionId, Class<H> type) {
		return (H) getExtension(primaryId, extensionId);
	}

	/**
	 * Checks if is release supported.
	 *
	 * @return true, if is release supported
	 */
	default boolean isReleaseSupported() {
		return false;
	}

	/**
	 * Release.
	 *
	 * @param header the header
	 */
	default void release(Header header) {
		throw new UnsupportedOperationException();
	}

}
