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

import com.slytechs.jnet.platform.api.util.Named;

/**
 * The Class MetaField.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public record MetaAttribute(MetaParent parent, String name, MetaValue value) implements Named, MetaElement {

	public MetaAttribute(String name, MetaValue value) {
		this(new MetaParent(), name, value);
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get() {
		return (T) value.get();
	}

	public MetaAttribute bindTo(Object target) {
		return new MetaAttribute(new MetaParent(), name, value.bindTo(target));
	}

}
