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

import java.util.Objects;

import com.slytechs.jnet.protocol.api.meta.MetaDomain;

/**
 * The Class AbstractMetaContext.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public abstract class AbstractMetaContext implements MetaDomain, MetaContext {

	/** The parent. */
	private final MetaContext parent;

	/** The name. */
	private final String name;

	/**
	 * Instantiates a new abstract meta context.
	 *
	 * @param parent the parent
	 * @param name   the name
	 */
	protected AbstractMetaContext(MetaContext parent, String name) {
		this.parent = Objects.requireNonNull(parent, "parent domain");
		this.name = name;

		if (name.equals(MetaPath.GLOBAL_DOMAINNAME))
			throw new IllegalArgumentException("reserved context name [%s]"
					.formatted(name));
	}

	/**
	 * Instantiates a new abstract meta context.
	 *
	 * @param name the name
	 */
	protected AbstractMetaContext(String name) {

		this.name = name;
		this.parent = name.equals(MetaPath.GLOBAL_DOMAINNAME)
				? null
				: GlobalContext.get();

		if ((getClass() != GlobalContext.class) && (name.equals(MetaPath.GLOBAL_DOMAINNAME)))
			throw new IllegalArgumentException("reserved context name [%s]"
					.formatted(name));
	}

	/**
	 * Name.
	 *
	 * @return the string
	 * @see com.slytechs.jnet.protocol.api.meta.MetaDomain#name()
	 */
	@Override
	public final String name() {
		return name;
	}

	/**
	 * Parent.
	 *
	 * @return the meta domain
	 * @see com.slytechs.jnet.protocol.api.meta.MetaDomain#parent()
	 */
	@Override
	public final MetaDomain parent() {
		return parent;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String flags = null;

		return ""
				+ getClass().getSimpleName()
				+ " ["
				+ "name=" + name
				+ (flags != null ? " flags=" + flags : "")
				+ "]";
	}

}
