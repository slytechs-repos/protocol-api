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

import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.api.meta.MetaContext.MetaMapped;

/**
 * The Class MetaElement.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public abstract class MetaElement
		implements Comparable<MetaElement>, MetaDomain {

	/** The meta. */
	protected final ReflectedComponent meta;

	/** The meta type. */
	private final MetaType metaType;

	/** The parent. */
	private final MetaDomain parent;

	/**
	 * Instantiates a new meta element.
	 *
	 * @param parent        the parent
	 * @param metaContainer the meta container
	 */
	protected MetaElement(MetaDomain parent, ReflectedComponent metaContainer) {
		this.meta = metaContainer;
		this.metaType = metaContainer.getMetaType(MetaInfo.class).metaType();

		if (this instanceof MetaMapped mapped) {
			this.parent = MetaMapped.Proxy.of(parent, mapped);
		} else
			this.parent = parent;

		assert this != parent() : "invalid domain parent";
	}

	/**
	 * Abbr.
	 *
	 * @return the string
	 */
	public final String abbr() {
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
	public final int compareTo(MetaElement o) {
		return ordinal() - o.ordinal();
	}

	/**
	 * Gets the meta.
	 *
	 * @param <T>  the generic type
	 * @param type the type
	 * @return the meta
	 */
	public final <T extends MetaInfoType> T getMeta(Class<T> type) {
		return meta.getMetaType(type);
	}

	/**
	 * Checks if is attribute.
	 *
	 * @return true, if is attribute
	 */
	public final boolean isAttribute() {
		return (metaType == MetaType.ATTRIBUTE);
	}

	/**
	 * Checks if is field.
	 *
	 * @return true, if is field
	 */
	public final boolean isField() {
		return (metaType == MetaType.FIELD);
	}

	/**
	 * Meta type.
	 *
	 * @return the meta type
	 */
	public final MetaType metaType() {
		return metaType;
	}

	/**
	 * Name.
	 *
	 * @return the string
	 * @see com.slytechs.jnet.protocol.api.meta.MetaDomain#name()
	 */
	@Override
	public final String name() {
		return meta.name();
	}

	/**
	 * Note.
	 *
	 * @return the string
	 */
	public final String note() {
		return meta.note();
	}

	/**
	 * Ordinal.
	 *
	 * @return the int
	 */
	public final int ordinal() {
		return meta.ordinal();
	}

	/**
	 * Parent.
	 *
	 * @return the meta domain
	 * @see com.slytechs.jnet.protocol.api.meta.MetaDomain#parent()
	 */
	@Override
	public MetaDomain parent() {
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
		String className = getClass().getSimpleName();
		return className
				+ " [name=" + name() + "]";
	}

	void setIntReferenceResolver(IntMetaResolver intRefResolver) {
		var meta = this.meta.getMetaType(MetaInfo.class);

		meta.linkIntReferenceResolver(intRefResolver);
	}
}
