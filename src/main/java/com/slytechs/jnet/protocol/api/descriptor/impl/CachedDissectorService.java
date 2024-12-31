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
package com.slytechs.jnet.protocol.api.descriptor.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.slytechs.jnet.platform.api.incubator.StableValue;
import com.slytechs.jnet.protocol.api.descriptor.Dissector;
import com.slytechs.jnet.protocol.api.descriptor.spi.DissectorService;

/**
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class CachedDissectorService implements DissectorService {

	public static final StableValue<CachedDissectorService> CACHED_SERVICE = StableValue
			.ofSupplier(CachedDissectorService::new);

	private final List<Dissector> cache;
	private final Map<Class<? extends Dissector>, List<? extends Dissector>> cacheByType = new HashMap<>();
	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	private final Lock readLock = rwLock.readLock();
	private final Lock writeLock = rwLock.writeLock();

	/**
	 * 
	 */
	private CachedDissectorService() {
		this.cache = Collections.unmodifiableList(DissectorService.mergeAllServices());
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.descriptor.spi.DissectorService#listDissectors()
	 */
	@Override
	public List<Dissector> listDissectors() {
		return cache;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.descriptor.spi.DissectorService#listByType(java.lang.Class)
	 */
	@Override
	public <T extends Dissector> List<T> listByType(Class<T> dissectorClass) {

		readLock.lock();

		try {
			@SuppressWarnings("unchecked")
			List<T> dissectors = (List<T>) cacheByType.get(dissectorClass);
			if (dissectors != null)
				return dissectors;
		} finally {
			readLock.unlock();
		}

		writeLock.lock();

		try {
			var dissectors = DissectorService.super.listByType(dissectorClass);
			cacheByType.put(dissectorClass, dissectors);

			return dissectors;
		} finally {
			writeLock.unlock();
		}
	}

}
