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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.slytechs.jnet.protocol.api.meta.MetaContext.MetaMapped;

/**
 * Map based meta context.
 */
public class MapMetaContext
		extends AbstractMetaContext
		implements MetaMapped {

	/** The capacity. */
	private int capacity = Integer.MAX_VALUE;

	/**
	 * Root domain.
	 *
	 * @param name     the name
	 * @param capacity the capacity
	 */
	public MapMetaContext(String name, int capacity) {
		super(name);
		this.capacity = capacity;
	}

	/**
	 * Anchored domain.
	 *
	 * @param parent   the parent
	 * @param name     the name
	 * @param capacity the capacity
	 */
	public MapMetaContext(MetaContext parent, String name, int capacity) {
		super(parent, name);
		this.capacity = capacity;
	}

	/** The map. */
	private final Map<Object, Object> map = new HashMap<>();

	/**
	 * Gets the.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param key the key
	 * @return the v
	 * @see com.slytechs.jnet.protocol.api.meta.MetaContext.MetaMapped#get(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <K, V> V get(K key) {
		return (V) map.get(key);
	}

	/**
	 * Sets the new value for key.
	 *
	 * @param <K>   the key type
	 * @param <V>   the value type
	 * @param key   the key
	 * @param value the value
	 */
	@Override
	public <K, V> void set(K key, V value) {
		if (size() == capacity)
			throw new IllegalStateException("context over capacity [%d]"
					.formatted(capacity));

		map.put(key, value);
	}

	/**
	 * Capacity.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.protocol.api.meta.MetaContext.MetaMapped#capacity()
	 */
	@Override
	public int capacity() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Sets the capacity.
	 *
	 * @param newCapacity the new capacity
	 */
	public void setCapacity(int newCapacity) {
		if (newCapacity < size())
			throw new IllegalStateException("new capacity is less than current size [size=%d, newCapacity=%d]"
					.formatted(size(), newCapacity));

		this.capacity = newCapacity;
	}

	/**
	 * Clear.
	 */
	public void clear() {
		map.clear();
	}

	/**
	 * Size.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.protocol.api.meta.MetaContext.MetaMapped#size()
	 */
	@Override
	public int size() {
		return map.size();
	}

	/**
	 * Find key.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param key the key
	 * @return the optional
	 * @see com.slytechs.jnet.protocol.api.meta.MetaDomain#findKey(java.lang.Object)
	 */
	@Override
	public <K, V> Optional<V> findKey(K key) {
		return Optional.ofNullable(get(key));
	}

	/**
	 * Find domain.
	 *
	 * @param name the name
	 * @return the meta domain
	 * @see com.slytechs.jnet.protocol.api.meta.MetaDomain#findDomain(java.lang.String)
	 */
	@Override
	public MetaDomain findDomain(String name) {
		Object obj = get(name);
		if (obj instanceof MetaDomain domain)
			return domain;

		return null;
	}
}
