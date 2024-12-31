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
package com.slytechs.jnet.protocol.api.meta.impl;

import java.lang.reflect.AnnotatedElement;

import com.slytechs.jnet.platform.api.util.json.JsonArray;
import com.slytechs.jnet.protocol.api.meta.MetaValue.ValueResolver;
import com.slytechs.jnet.protocol.api.meta.Resolver;
import com.slytechs.jnet.protocol.api.meta.Resolvers;
import com.slytechs.jnet.protocol.api.meta.spi.ValueResolverService;

/**
 * The Class ResolverInfo.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class ResolverInfo implements MetaInfoType {

	/**
	 * Parses the.
	 *
	 * @param element   the element
	 * @param name      the name
	 * @param jsonArray the json array
	 * @return the resolver info
	 */
	static ResolverInfo parse(AnnotatedElement element, String name, JsonArray jsonArray) {
		if (jsonArray != null)
			return parseJson(jsonArray);

		return parseAnnotations(element);
	}

	/**
	 * Parses the annotations.
	 *
	 * @param element the element
	 * @return the resolver info
	 */
	private static ResolverInfo parseAnnotations(AnnotatedElement element) {
		Resolvers multiple = element.getAnnotation(Resolvers.class);
		Resolver single = element.getAnnotation(Resolver.class);
		var resolverMap = ValueResolverService.cached().getResolvers();

		if (multiple != null) {
			Resolver[] resolversAnnotation = multiple.value();
			ValueResolver[] resolvers = new ValueResolver[resolversAnnotation.length];

			for (int i = 0; i < resolversAnnotation.length; i++)
				resolvers[i] = resolverMap.get(resolversAnnotation[i].value());

			return new ResolverInfo(resolvers);

		} else if (single != null)
			return new ResolverInfo(resolverMap.get(single.value()));

		return new ResolverInfo();
	}

	/**
	 * Parses the json.
	 *
	 * @param jsonArray the json array
	 * @return the resolver info
	 */
	private static ResolverInfo parseJson(JsonArray jsonArray) {
		ValueResolver[] resolvers = new ValueResolver[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {

		}

		return new ResolverInfo(resolvers);
	}

	/** The resolvers. */
	private final ValueResolver[] resolvers;

	/**
	 * Instantiates a new resolver info.
	 *
	 * @param resolvers the resolvers
	 */
	private ResolverInfo(ValueResolver... resolvers) {
		this.resolvers = resolvers;
	}

	/**
	 * Gets the value resolvers.
	 *
	 * @return the value resolvers
	 */
	public ValueResolver[] getValueResolvers() {
		return resolvers;
	}

}
