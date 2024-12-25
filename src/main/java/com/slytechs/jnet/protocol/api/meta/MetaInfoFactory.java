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

import com.slytechs.jnet.platform.api.util.json.JsonObject;

/**
 * A factory for creating MetaInfo objects.
 */
public class MetaInfoFactory {
	
	/** The element. */
	private final AnnotatedElement element;
	
	/** The json defaults. */
	private final JsonObject jsonDefaults;

	/**
	 * Instantiates a new meta info factory.
	 *
	 * @param element      the element
	 * @param name         the name
	 * @param jsonDefaults the json defaults
	 */
	public MetaInfoFactory(AnnotatedElement element, String name, JsonObject jsonDefaults) {
		this.element = element;
		this.jsonDefaults = jsonDefaults;
	}

	/**
	 * Gets the meta info.
	 *
	 * @param <T>        the generic type
	 * @param lookupType the lookup type
	 * @return the meta info
	 */
	public <T extends MetaInfoType> T getMetaInfo(Class<T> lookupType) {
		return null;
	}
}
