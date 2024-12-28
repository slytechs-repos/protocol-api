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

import java.util.function.Function;

/**
 * The Class Global.
 */
public class Global extends MapMetaContext {

	/** The Constant GLOBAL_STATIC_CTX. */
	public static final Global GLOBAL_STATIC_CTX = new Global();

	/**
	 * Instantiates a new global.
	 */
	public Global() {
		super("Global", 100);
	}

	/**
	 * Gets the.
	 *
	 * @return the global
	 */
	public static Global get() {
		return GLOBAL_STATIC_CTX;
	}

	/**
	 * Compute.
	 *
	 * @param <K>  the key type
	 * @param <V>  the value type
	 * @param key  the key
	 * @param func the func
	 * @return the v
	 */
	public synchronized static <K, V> V compute(K key, Function<K, V> func) {
		return get().getOrCompute(key, func);
	}
}