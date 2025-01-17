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
package com.slytechs.jnet.protocol.api.meta.spi.impl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.slytechs.jnet.platform.api.incubator.StableValue;
import com.slytechs.jnet.protocol.api.meta.spi.HeaderTemplateService;
import com.slytechs.jnet.protocol.api.meta.template.HeaderTemplate;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class CachedHeaderTemplateService implements HeaderTemplateService {

	public static final StableValue<CachedHeaderTemplateService> CACHED = StableValue
			.ofSupplier(CachedHeaderTemplateService::new);

	private final static Map<String, Reference<HeaderTemplate>> cache = new HashMap<>();

	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.spi.HeaderTemplateService#loadHeaderTemplate(java.lang.String)
	 */
	@Override
	public HeaderTemplate loadHeaderTemplate(String resource, String name) {
		rwLock.readLock().lock();

		try {
			String key = makeKey(resource, name);
			if (cache.containsKey(key)) {
				var template = cache.get(key).get();
				if (template != null)
					return template;
			}
		} finally {
			rwLock.readLock().unlock();
		}

		return loadFromAnyProvider(resource, name);
	}

	private String makeKey(String resource, String name) {
		if (name == null)
			return resource;

		StringBuilder sb = new StringBuilder(resource);
		sb.append('#').append(name);

		return sb.toString();
	}

	private HeaderTemplate loadFromAnyProvider(String resource, String name) {

		var template = ServiceLoader.load(HeaderTemplateService.class).stream()
				.map(s -> s.get().loadHeaderTemplate(resource, name))
				.filter(t -> t != null)
				.findAny()
				.orElse(null);

		if (template != null) {
			rwLock.writeLock().lock();;

			try {
				String key = makeKey(resource, name);
				cache.remove(key);
				cache.put(key, new WeakReference<>(template));

			} finally {
				rwLock.writeLock().unlock();
			}
		}

		return template;
	}

}
