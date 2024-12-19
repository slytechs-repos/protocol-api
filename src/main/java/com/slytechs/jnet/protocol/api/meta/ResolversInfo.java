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
package com.slytechs.jnet.protocol.api.meta;

import java.lang.reflect.AnnotatedElement;

import com.slytechs.jnet.jnetruntime.internal.json.JsonArray;
import com.slytechs.jnet.jnetruntime.internal.json.JsonArrayBuilder;
import com.slytechs.jnet.jnetruntime.internal.json.JsonObject;
import com.slytechs.jnet.jnetruntime.internal.json.JsonValue;
import com.slytechs.jnet.jnetruntime.internal.json.JsonValue.ValueType;
import com.slytechs.jnet.protocol.api.meta.MetaValue.ValueResolver;

/**
 * The ResolversInfo.
 *
 * @param resolvers a list of value resolvers
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public record ResolversInfo(ValueResolver... resolvers) implements MetaInfoType {

	/**
	 * Parses the.
	 *
	 * @param element      the element
	 * @param name         the name
	 * @param jsonDefaults the json defaults
	 * @return the resolvers info
	 */
	static ResolversInfo parse(AnnotatedElement element, String name, JsonObject jsonDefaults) {
		JsonValue jsonValue = null;
		if (jsonDefaults != null)
			jsonValue = jsonDefaults.get("resolver");

		if (jsonValue != null)
			return parseJson(jsonValue);

		return parseAnnotations(element);
	}

	/**
	 * Parses the annotations.
	 *
	 * @param element the element
	 * @return the resolvers info
	 */
	private static ResolversInfo parseAnnotations(AnnotatedElement element) {
		Resolvers multiple = element.getAnnotation(Resolvers.class);
		Resolver single = element.getAnnotation(Resolver.class);

		if (multiple != null) {
			Resolver[] resolversAnnotation = multiple.value();
			ValueResolver[] resolvers = new ValueResolver[resolversAnnotation.length];

			for (int i = 0; i < resolversAnnotation.length; i++)
				resolvers[i] = resolversAnnotation[i].value().getResolver();

			return new ResolversInfo(resolvers);

		} else if (single != null)
			return new ResolversInfo(single.value().getResolver());

		return new ResolversInfo();
	}

	/**
	 * Parses the json.
	 *
	 * @param jsonValue the json value
	 * @return the resolvers info
	 */
	private static ResolversInfo parseJson(JsonValue jsonValue) {

		JsonArray jsonArray;
		if (jsonValue.getValueType() == ValueType.ARRAY) {
			jsonArray = (JsonArray) jsonValue;
		} else {
			jsonArray = new JsonArrayBuilder()
					.add(jsonValue)
					.build();
		}

		ValueResolver[] resolvers = new ValueResolver[jsonArray.size()];

		for (int i = 0; i < jsonArray.size(); i++) {
			resolvers[i] = Resolver.ResolverType
					.valueOf(jsonArray.getString(i))
					.getResolver();
		}

		return new ResolversInfo(resolvers);

	}
}
