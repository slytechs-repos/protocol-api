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
package com.slytechs.jnet.protocol.meta;

import java.io.InputStream;

import com.slytechs.jnet.jnetruntime.internal.json.Json;
import com.slytechs.jnet.jnetruntime.internal.json.JsonException;
import com.slytechs.jnet.jnetruntime.internal.json.JsonObject;
import com.slytechs.jnet.jnetruntime.internal.json.JsonReader;

/**
 * The Class MetaResourceReader.
 *
 * @author Mark Bednarczyk
 */
public class MetaResourceReader {

	/** The json obj. */
	private final JsonObject jsonObj;

	/**
	 * Instantiates a new meta resource reader.
	 *
	 * @param resourceName the resource name
	 * @throws JsonException the json exception
	 */
	public MetaResourceReader(String resourceName) throws JsonException {
		InputStream in = ReflectedClass.class.getResourceAsStream("/" + resourceName);
		if (in == null) {
			this.jsonObj = null;
		} else {

			try (JsonReader reader = Json.createReader(in)) {
				JsonObject obj = reader.readObject();
				reader.close();

				this.jsonObj = process(obj);
			}
		}
	}

	/**
	 * Process.
	 *
	 * @param sourceJsonObj the source json obj
	 * @return the json object
	 */
	protected JsonObject process(JsonObject sourceJsonObj) {
		return sourceJsonObj;
	}

	/**
	 * To json obj.
	 *
	 * @return the json object
	 */
	public JsonObject toJsonObj() {
		return this.jsonObj;
	}
}
