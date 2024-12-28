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

import java.util.Optional;
import java.util.function.Function;

import com.slytechs.jnet.protocol.api.meta.MetaField;

/**
 * A meta domain. A domain is a namespace which may contains ATV pairs. A
 * Packet's domain space contains packet attributes and its protocol headers. A
 * header's domain contains header attributes and header's fields. Even a
 * header's field is a domain containing various attributes and access to the
 * field's value.
 * <p>
 * On the other end there are domains that are pure maps or list based. Finally
 * domains are linked in a hierarchy which can be queried for values either
 * using a {@code MetaPath} or in cannonical dot notation ie
 * {@code "Ip4.version"}.
 * </p>
 * <p>
 * Other usage for domains is to attach analysis information. Since domains are
 * implemented by meta contexts, the context can be used to group information
 * across may packets, streams and other analysis collections. A GUI based
 * application can use domain/contexts to create complex data models to be
 * displayed in a GUI across many packets.
 * </p>
 */
public interface MetaDomain {

	/**
	 * Gets the global domain.
	 *
	 * @return the global domain
	 */
	public static MapMetaContext getGlobalDomain() {
		return Global.GLOBAL_STATIC_CTX;
	}

	/**
	 * Global.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param key the key
	 * @return the v
	 */
	static <K, V> V global(K key) {
		return getGlobalDomain().get(key);
	}

	/**
	 * Global.
	 *
	 * @param <K>  the key type
	 * @param <V>  the value type
	 * @param key  the key
	 * @param func the func
	 * @return the v
	 */
	static <K, V> V global(K key, Function<K, V> func) {
		return getGlobalDomain().getOrCompute(key, func);
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	String name();

	/**
	 * Parent.
	 *
	 * @return the meta domain
	 */
	MetaDomain parent();

	/**
	 * Find key.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param key the key
	 * @return the optional
	 */
	<K, V> Optional<V> findKey(K key);

	/**
	 * Find domain.
	 *
	 * @param name the name
	 * @return the meta domain
	 */
	MetaDomain findDomain(String name);

	/**
	 * Search for field.
	 *
	 * @param path the path
	 * @return the optional
	 */
	default Optional<MetaField> searchForField(MetaPath path) {
		return path.searchForField(this);
	}

}
