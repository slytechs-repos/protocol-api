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

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * The Class ReflectedComponent.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public abstract class ReflectedComponent implements Comparable<ReflectedComponent> {

	/** The meta. */
	private final MetaInfo meta;

	/**
	 * Instantiates a new reflected component.
	 *
	 * @param meta the meta
	 */
	public ReflectedComponent(MetaInfo meta) {
		this.meta = meta;
	}

	/**
	 * Abbr.
	 *
	 * @return the string
	 */
	public String abbr() {
		return meta.abbr();
	}

	/**
	 * Compare to.
	 *
	 * @param o the o
	 * @return the int
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ReflectedComponent o) {
		return ordinal() - o.ordinal();
	}

	/**
	 * Gets the meta type.
	 *
	 * @param <T>  the generic type
	 * @param type the type
	 * @return the meta type
	 */
	public <T extends MetaInfoType> T getMetaType(Class<T> type) {
		return meta.getMetaType(type);
	}

	/**
	 * Checks if is static.
	 *
	 * @param type the type
	 * @return true, if is static
	 */
	protected final boolean isStatic(Member type) {
		return (type.getModifiers() & Modifier.STATIC) != 0;
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	public String name() {
		return meta.name();
	}

	/**
	 * Note.
	 *
	 * @return the string
	 */
	public String note() {
		return meta.note();
	}

	/**
	 * Ordinal.
	 *
	 * @return the int
	 */
	public int ordinal() {
		return meta.ordinal();
	}

	/**
	 * Unwind cause exception.
	 *
	 * @param e the e
	 * @return the throwable
	 */
	protected final Throwable unwindCauseException(Throwable e) {
		while (e.getCause() != null)
			e = e.getCause();

		return e;
	}
}
