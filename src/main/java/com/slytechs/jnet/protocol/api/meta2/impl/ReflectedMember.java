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

import java.lang.reflect.Member;

/**
 * The Class ReflectedMember.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public abstract class ReflectedMember extends ReflectedComponent {


	/**
	 * Instantiates a new reflected member.
	 *
	 * @param meta the meta
	 */
	protected ReflectedMember(MetaInfo meta) {
		super(meta);
	}

	/**
	 * Gets the value.
	 *
	 * @param <T>    the generic type
	 * @param target the target
	 * @param args   the args
	 * @return the value
	 */
	public abstract <T> T getValue(Object target, Object... args);

	/**
	 * Sets the value.
	 *
	 * @param target the target
	 * @param args   the args
	 */
	public abstract void setValue(Object target, Object... args);

	/**
	 * Gets the value type.
	 *
	 * @return the value type
	 */
	public abstract Class<?> getValueType();

	/**
	 * Gets the member.
	 *
	 * @return the member
	 */
	protected abstract Member getMember();

	public abstract boolean isClassMethod();

	public boolean isClassField() {
		return !isClassMethod();
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getMember().toString();
	}

}
