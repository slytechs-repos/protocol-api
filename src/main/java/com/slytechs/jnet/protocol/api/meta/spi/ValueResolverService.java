/*
 * Sly Technologies Free License
 * 
 * Copyright 2024 Sly Technologies Inc.
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
package com.slytechs.jnet.protocol.api.meta.spi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import com.slytechs.jnet.protocol.api.meta.MetaValue.ValueResolver;
import com.slytechs.jnet.protocol.api.meta.MetaValue.ValueResolver.ValueResolverType;
import com.slytechs.jnet.protocol.api.meta.impl.CachedValueResolverService;

/**
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface ValueResolverService {

	static ValueResolverService cached() {
		return CachedValueResolverService.CACHED_SERVICE.get();
	}

	static Map<String, ValueResolver> mergeAllServices() {
		var map = new HashMap<String, ValueResolver>();

		ServiceLoader.load(ValueResolverService.class)
				.stream()
				.forEach(s -> map.putAll(s.get().getResolvers()));

		return map;
	}

	Map<String, ValueResolver> getResolvers();

	default List<ValueResolverType> getResolverTypeList() {
		return getResolvers().entrySet().stream()
				.map(e -> new ValueResolverType(e.getKey(), e.getValue()))
				.toList();
	}

	default ValueResolverType[] getResolverTypeArray() {
		return getResolverTypeList().toArray(ValueResolverType[]::new);
	}

	default ValueResolverType valueTypeOf(String name) {
		for (var ar : getResolverTypeArray()) {
			if (ar.name().endsWith(name))
				return ar;
		}

		return null;
	}
}
