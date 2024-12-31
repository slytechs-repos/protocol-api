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
import java.util.function.IntFunction;

import com.slytechs.jnet.protocol.api.meta.MetaDomain;
import com.slytechs.jnet.protocol.api.meta.MetaField;

/**
 * The Interface MetaContext.
 */
public interface MetaContext extends MetaDomain {

	/**
	 * The Interface MetaIndexed.
	 */
	interface MetaIndexed extends MetaContext {

		/**
		 * Append.
		 *
		 * @param <V>   the value type
		 * @param value the value
		 */
		<V> void append(V value);

		/**
		 * Capacity.
		 *
		 * @return the int
		 */
		int capacity();

		/**
		 * Gets the at index.
		 *
		 * @param <V>   the value type
		 * @param index the index
		 * @return the at index
		 */
		<V> V getAtIndex(int index);

		/**
		 * Gets the at index or compute.
		 *
		 * @param <V>   the value type
		 * @param index the index
		 * @param func  the func
		 * @return the at index or compute
		 */
		default <V> V getAtIndexOrCompute(int index, IntFunction<V> func) {
			V v = getAtIndex(index);

			if (v == null) {
				v = func.apply(index);
				setAtIndex(index, v);
			}

			return v;
		}

		/**
		 * Limit.
		 *
		 * @return the int
		 */
		int limit();

		/**
		 * Limit.
		 *
		 * @param <V>      the value type
		 * @param newLimit the new limit
		 * @param func     the func
		 */
		<V> void limit(int newLimit, IntFunction<V> func);

		/**
		 * Remaining.
		 *
		 * @return the int
		 */
		default int remaining() {
			return capacity() - limit();
		}

		/**
		 * Sets the all.
		 *
		 * @param <V>  the value type
		 * @param func the new all
		 */
		default <V> void setAll(IntFunction<V> func) {
			int count = limit();
			for (int i = 0; i < count; i++)
				setAtIndex(i, func.apply(i));
		}

		/**
		 * Sets the at index.
		 *
		 * @param <V>   the value type
		 * @param index the index
		 * @param value the value
		 */
		<V> void setAtIndex(int index, V value);
	}

	/**
	 * The Interface MetaMapped.
	 */
	interface MetaMapped extends MetaContext {

		/**
		 * The Interface Proxy.
		 */
		public interface Proxy extends MetaMapped {

			/**
			 * Of.
			 *
			 * @param parent the parent
			 * @param proxy  the proxy
			 * @return the meta mapped
			 */
			static MetaMapped of(MetaDomain parent, MetaMapped proxy) {
				return new Proxy() {

					@Override
					public MetaMapped getProxy() {
						return proxy;
					}

					@Override
					public String name() {
						return getProxy().name();
					}

					@Override
					public MetaDomain parent() {
						return parent;
					}

					@Override
					public <K, V> Optional<V> findKey(K key) {
						return getProxy().findKey(key);
					}

					@Override
					public MetaDomain findDomain(String name) {
						return getProxy().findDomain(name);
					}
				};
			}

			/**
			 * Gets the proxy.
			 *
			 * @return the proxy
			 */
			MetaMapped getProxy();

			/**
			 * Capacity.
			 *
			 * @return the int
			 * @see com.slytechs.jnet.protocol.api.meta.impl.MetaContext.MetaMapped#capacity()
			 */
			@Override
			default int capacity() {
				return getProxy().capacity();
			}

			/**
			 * Gets the.
			 *
			 * @param <K> the key type
			 * @param <V> the value type
			 * @param key the key
			 * @return the v
			 * @see com.slytechs.jnet.protocol.api.meta.impl.MetaContext.MetaMapped#get(java.lang.Object)
			 */
			@Override
			default <K, V> V get(K key) {
				return getProxy().get(key);
			}

			/**
			 * Gets the field.
			 *
			 * @param name the name
			 * @return the field
			 * @see com.slytechs.jnet.protocol.api.meta.impl.MetaContext.MetaMapped#getField(java.lang.String)
			 */
			@Override
			default MetaField getField(String name) {
				return getProxy().getField(name);
			}

			/**
			 * Gets the or compute.
			 *
			 * @param <K>  the key type
			 * @param <V>  the value type
			 * @param key  the key
			 * @param func the func
			 * @return the or compute
			 * @see com.slytechs.jnet.protocol.api.meta.impl.MetaContext.MetaMapped#getOrCompute(java.lang.Object,
			 *      java.util.function.Function)
			 */
			@Override
			default <K, V> V getOrCompute(K key, Function<K, V> func) {
				return getProxy().getOrCompute(key, func);
			}

			/**
			 * Remaining.
			 *
			 * @return the int
			 * @see com.slytechs.jnet.protocol.api.meta.impl.MetaContext.MetaMapped#remaining()
			 */
			@Override
			default int remaining() {
				return getProxy().remaining();
			}

			/**
			 * Sets the.
			 *
			 * @param <K>   the key type
			 * @param <V>   the value type
			 * @param key   the key
			 * @param value the value
			 * @see com.slytechs.jnet.protocol.api.meta.impl.MetaContext.MetaMapped#set(java.lang.Object,
			 *      java.lang.Object)
			 */
			@Override
			default <K, V> void set(K key, V value) {
				getProxy().set(key, value);
			}

			/**
			 * Size.
			 *
			 * @return the int
			 * @see com.slytechs.jnet.protocol.api.meta.impl.MetaContext.MetaMapped#size()
			 */
			@Override
			default int size() {
				return getProxy().size();
			}

		}

		/**
		 * Capacity.
		 *
		 * @return the int
		 */
		default int capacity() {
			return size();
		}

		/**
		 * Gets the.
		 *
		 * @param <K> the key type
		 * @param <V> the value type
		 * @param key the key
		 * @return the v
		 */
		<K, V> V get(K key);

		/**
		 * Gets the field.
		 *
		 * @param name the name
		 * @return the field
		 */
		default MetaField getField(String name) {
			return get(name);
		}

		/**
		 * Find field.
		 *
		 * @param name the name
		 * @return the optional
		 */
		default Optional<MetaField> findField(String name) {
			return Optional.ofNullable(getField(name));
		}

		/**
		 * Gets the or compute.
		 *
		 * @param <K>  the key type
		 * @param <V>  the value type
		 * @param key  the key
		 * @param func the func
		 * @return the or compute
		 */
		default <K, V> V getOrCompute(K key, Function<K, V> func) {
			V v = get(key);

			if (v == null) {
				v = func.apply(key);
				set(key, v);
			}

			return v;
		}

		default <K, V> V orElse(K key, V defaultValue) {
			V v = get(key);
			if (v == null)
				return defaultValue;

			return v;
		}

		/**
		 * Remaining.
		 *
		 * @return the int
		 */
		default int remaining() {
			return capacity() - size();
		}

		/**
		 * Sets the.
		 *
		 * @param <K>   the key type
		 * @param <V>   the value type
		 * @param key   the key
		 * @param value the value
		 */
		default <K, V> void set(K key, V value) {
			throw new UnsupportedOperationException("setters not supported for this domain type");
		}

		/**
		 * Size.
		 *
		 * @return the int
		 */
		int size();
	}

	/**
	 * New root.
	 *
	 * @return the meta context
	 */
	static MetaContext newRoot() {
		return new MapMetaContext(GlobalContext.get(), MetaPath.ROOT_NAME, 1);
	}

	/**
	 * Cast.
	 *
	 * @param <T> the generic type
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	default <T extends MetaContext> T cast() {
		return (T) this;
	}

}
