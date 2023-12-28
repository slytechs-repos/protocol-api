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
package com.slytechs.jnet.jnetruntime.internal.util.format;

import java.util.Optional;

import com.slytechs.jnet.jnetruntime.internal.util.ByteArraySlice;

/**
 * The Interface FormatPropertyGetter.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author mark
 * @param <T> the generic type
 */
public interface FormatPropertyGetter<T> {

	/**
	 * Gets the format property.
	 *
	 * @param target the target
	 * @param slice  the slice
	 * @return the format property
	 */
	FormatProperty getFormatProperty(T target, Optional<ByteArraySlice> slice);

	/**
	 * Gets the attribute property.
	 *
	 * @return the attribute property
	 */
	default FormatProperty getAttributeProperty() {
		throw new UnsupportedOperationException();
	}
}
