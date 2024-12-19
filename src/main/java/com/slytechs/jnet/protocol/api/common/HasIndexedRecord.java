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
 * Defines methods for checking and accessing indexed record headers.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 * @param <T> the generic type
 */
public interface HasIndexedRecord<T extends Header> {

	/**
	 * Gets a record header. A header extension with numerical id
	 * {@link Header#id()} is located and memory bound to parameter
	 * {@code extension}. This method does not return null and will throw an
	 * exception if header extension is not present.
	 *
	 * @param <E>    the record type
	 * @param index  record index
	 * @param record the record header instance
	 * @return the record header, this method does not return null
	 * @throws HeaderNotFound if the record header is not found
	 */
	<E extends T> E getRecord(int index, E record) throws HeaderNotFound;

	/**
	 * Gets a record header.
	 *
	 * @param index record index
	 * @return the record found at specified index, this method never returns null
	 * @throws IndexOutOfBoundsException thrown if index is out of bounds
	 */
	T getRecord(int index) throws IndexOutOfBoundsException;

	/**
	 * Checks if a particular record header is available.
	 * 
	 * @param index    record index
	 * @param recordId the extension id
	 *
	 * @return true, if successful, otherwise false
	 */
	boolean hasRecord(int index, int recordId);

	/**
	 * Checks and performs memory binding if a particular record header is
	 * available. If a record header is not available, no memory binding to record
	 * header is performed and false is returned.
	 *
	 * @param index  record index
	 * @param record the record header
	 * @return true, if record binding was successful, otherwise false
	 */
	default boolean hasRecord(int index, T record) {
		return peekRecord(index, record) != null;
	}

	/**
	 * Performs a peek and memory binding operation if a particular protocol header
	 * extension is available. If a header is not available, no memory binding to
	 * protocol header extension is performed and null is returned.
	 *
	 * @param <E>    the record type
	 * @param index  record index
	 * @param record the record header
	 * @return the record header, if record binding was successful, otherwise null
	 */
	<E extends T> E peekRecord(int index, E record);

	/**
	 * Gets the number of records present.
	 *
	 * @return the number of records present
	 */
	int numberOfRecords();
}
