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

import static java.lang.invoke.MethodHandles.*;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

import com.slytechs.jnet.platform.api.domain.value.Value;

/**
 * The Class MetaValue.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public record MetaValue(String name, MethodHandle getter, MethodHandle setter) implements Value {

	public MetaValue(String name, Method getter) throws IllegalAccessException {
		this(name, lookup().unreflect(getter), null);
	}

	public MetaValue(String name, Method getter, Method setter) throws IllegalAccessException {
		this(name, lookup().unreflect(getter), lookup().unreflect(setter));
	}

	public MetaValue bindTo(Object target) {
		return new MetaValue(

				name,
				getter.bindTo(target),
				setter == null ? null : setter.bindTo(target)

		);
	}

	/**
	 * @see com.slytechs.jnet.platform.api.domain.value.Value#get()
	 */
	@Override
	public Object get() {
		try {
			return getter.invoke();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getFormatted() {
		return String.valueOf(get());
	}

	/**
	 * @see com.slytechs.jnet.platform.api.domain.value.Value#set(java.lang.Object)
	 */
	@Override
	public void set(Object newValue) {
		if (setter != null)
			try {
				setter.invoke(newValue);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
	}

	/**
	 * @see com.slytechs.jnet.platform.api.domain.value.Value#compareAndSet(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public boolean compareAndSet(Object expectedValue, Object newValue) {
		Object old = get();
		if (expectedValue.equals(old)) {
			set(newValue);

			return true;
		}

		return false;
	}

	/**
	 * @see com.slytechs.jnet.platform.api.domain.value.Value#getAndSet(java.lang.Object)
	 */
	@Override
	public Object getAndSet(Object newValue) {
		Object old = get();
		set(newValue);

		return old;
	}
}
