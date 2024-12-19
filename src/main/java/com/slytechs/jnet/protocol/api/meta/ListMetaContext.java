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

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.IntFunction;

import com.slytechs.jnet.protocol.api.meta.MetaContext.MetaIndexed;

/**
 * The Class ListMetaContext.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class ListMetaContext extends AbstractMetaContext implements MetaIndexed {

	/** The list. */
	private final ArrayList<Object> list;

	/** The capacity. */
	private final int capacity;

	/**
	 * Instantiates a new list meta context.
	 *
	 * @param parent   the parent
	 * @param name     the name
	 * @param capacity the capacity
	 */
	public ListMetaContext(MetaContext parent, String name, int capacity) {
		super(parent, name);
		this.capacity = capacity;
		this.list = new ArrayList<>(capacity);
	}

	/**
	 * Instantiates a new list meta context.
	 *
	 * @param name     the name
	 * @param capacity the capacity
	 */
	public ListMetaContext(String name, int capacity) {
		super(name);
		this.capacity = capacity;
		this.list = new ArrayList<>(capacity);
	}

	/**
	 * Append.
	 *
	 * @param <V>   the value type
	 * @param value the value
	 * @see com.slytechs.jnet.protocol.api.meta.MetaContext.MetaIndexed#append(java.lang.Object)
	 */
	@Override
	public <V> void append(V value) {
		list.add(value);
	}

	/**
	 * Capacity.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.protocol.api.meta.MetaContext.MetaIndexed#capacity()
	 */
	@Override
	public int capacity() {
		return capacity;
	}

	/**
	 * Gets the at index.
	 *
	 * @param <V>   the value type
	 * @param index the index
	 * @return the at index
	 * @see com.slytechs.jnet.protocol.api.meta.MetaContext.MetaIndexed#getAtIndex(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <V> V getAtIndex(int index) {
		return (V) list.get(index);
	}

	/**
	 * Limit.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.protocol.api.meta.MetaContext.MetaIndexed#limit()
	 */
	@Override
	public int limit() {
		return list.size();
	}

	/**
	 * Sets the at index.
	 *
	 * @param <V>   the value type
	 * @param index the index
	 * @param value the value
	 * @see com.slytechs.jnet.protocol.api.meta.MetaContext.MetaIndexed#setAtIndex(int,
	 *      java.lang.Object)
	 */
	@Override
	public <V> void setAtIndex(int index, V value) {
		if (index >= limit())
			limit(index, i -> null);

		list.set(index, value);
	}

	/**
	 * Limit.
	 *
	 * @param <V>      the value type
	 * @param newLimit the new limit
	 * @param func     the func
	 * @see com.slytechs.jnet.protocol.api.meta.MetaContext.MetaIndexed#limit(int,
	 *      java.util.function.IntFunction)
	 */
	@Override
	public <V> void limit(int newLimit, IntFunction<V> func) {
		if (newLimit > capacity)
			throw new IllegalArgumentException("new limit is over capacity [limit=%d, capacity=%d]"
					.formatted(newLimit, capacity));

		int remaining = remaining();
		int start = limit();

		for (int i = 0; i < remaining; i++)
			append(func.apply(start + i));
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
	@SuppressWarnings("unchecked")
	@Override
	public <K, V> Optional<V> findKey(K key) {
		if (key instanceof Number index)
			return Optional.ofNullable((V) list.get(index.intValue()));

		return Optional.empty();
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
		return list.stream()
				.filter(v -> v instanceof MetaDomain domain && domain.name().equals(name))
				.map(v -> (MetaDomain) v)
				.findAny()
				.orElse(null);
	}

}
