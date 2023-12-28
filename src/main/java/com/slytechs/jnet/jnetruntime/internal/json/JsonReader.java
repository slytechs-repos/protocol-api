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
package com.slytechs.jnet.jnetruntime.internal.json;

/**
 * The Interface JsonReader.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public interface JsonReader extends AutoCloseable {

	/**
	 * Close.
	 *
	 * @throws JsonException the json exception
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	void close() throws JsonException;

	/**
	 * Read.
	 *
	 * @return the json structure
	 * @throws JsonException the json exception
	 */
	JsonStructure read() throws JsonException;

	/**
	 * Read array.
	 *
	 * @return the json array
	 * @throws JsonException the json exception
	 */
	JsonArray readArray() throws JsonException;

	/**
	 * Read object.
	 *
	 * @return the json object
	 * @throws JsonException the json exception
	 */
	JsonObject readObject() throws JsonException;

}
