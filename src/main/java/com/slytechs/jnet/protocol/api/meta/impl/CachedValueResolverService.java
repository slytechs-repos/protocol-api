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
package com.slytechs.jnet.protocol.api.meta.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.slytechs.jnet.platform.api.incubator.StableValue;
import com.slytechs.jnet.protocol.api.meta.MetaValue.ValueResolver;
import com.slytechs.jnet.protocol.api.meta.MetaValue.ValueResolver.ValueResolverType;
import com.slytechs.jnet.protocol.api.meta.spi.ValueResolverService;

/**
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class CachedValueResolverService implements ValueResolverService {

	public static final StableValue<CachedValueResolverService> CACHED_SERVICE = StableValue
			.ofSupplier(CachedValueResolverService::new);

	private final Map<String, ValueResolver> cacheMap;

	private final List<ValueResolverType> cacheList;
	private final ValueResolverType[] cacheArray;

	public CachedValueResolverService() {

		this.cacheMap = Collections.unmodifiableMap(new HashMap<>(ValueResolverService.mergeAllServices()));

		var list = getResolvers().entrySet().stream()
				.map(e -> new ValueResolverType(e.getKey(), e.getValue()))
				.toList();
		this.cacheList = Collections.unmodifiableList(list);

		this.cacheArray = getResolverTypeList().toArray(ValueResolverType[]::new);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.spi.ValueResolverService#getResolvers()
	 */
	@Override
	public Map<String, ValueResolver> getResolvers() {
		return cacheMap;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.spi.ValueResolverService#getResolverTypes()
	 */
	@Override
	public List<ValueResolverType> getResolverTypeList() {
		return cacheList;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.spi.ValueResolverService#getResolverTypeArray()
	 */
	@Override
	public ValueResolverType[] getResolverTypeArray() {
		return cacheArray;
	}

	@Override
	public String toString() {
		return getResolvers().entrySet().stream()
				.map(e -> "ValueResolver: %s".formatted(e.getKey()))
				.collect(Collectors.joining("\n"));
	}
}