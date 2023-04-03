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
package com.slytechs.jnet.runtime.util.json;

import java.util.ArrayList;
import java.util.List;

public final class JsonArrayBuilder {

	private final List<JsonValue> list = new ArrayList<>();

	public JsonArrayBuilder add(String value) {
		list.add(new StringImpl(value));

		return this;
	}

	public JsonArrayBuilder add(Number value) {
		list.add(new NumberImpl(value));

		return this;
	}

	public JsonArrayBuilder add(JsonValue value) {
		list.add(value);

		return this;
	}

	public JsonArray build() {
		return new ArrayImpl(list);
	}
}