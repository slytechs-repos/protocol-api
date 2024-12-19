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

import com.slytechs.jnet.protocol.api.meta.MetaContext.MetaMapped;

/**
 * The Class AbstractMetaMapped.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public abstract class AbstractMetaMapped extends AbstractMetaContext implements MetaMapped {

	/** The lookup. */
	private LookupField lookup;

	/**
	 * Instantiates a new abstract meta mapped.
	 *
	 * @param parent the parent
	 * @param name   the name
	 * @param lookup the lookup
	 */
	public AbstractMetaMapped(MetaContext parent, String name, LookupField lookup) {
		super(parent, name);
		this.lookup = lookup;
	}

	/**
	 * Instantiates a new abstract meta mapped.
	 *
	 * @param name the name
	 */
	public AbstractMetaMapped(String name) {
		super(name);
		// TODO Auto-generated constructor stub
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
		return (V) lookup.lookup((String) key);
	}

	/**
	 * Size.
	 *
	 * @return the int
	 * @see com.slytechs.jnet.protocol.api.meta.MetaContext.MetaMapped#size()
	 */
	@Override
	public int size() {
		throw new UnsupportedOperationException("not implemented yet");
	}

}
