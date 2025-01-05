/*
 * Sly Technologies Free License
 * 
 * Copyright 2024 Sly Technologies Inc.
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

/**
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface ValueResolver {

	interface ValueResolver2 {
		String toString(MetaField field, Object value);
	}

	String toString(Object value);

	default String toString(MetaField field, Object value) {
		return toString(value);
	}

	static ValueResolver of(ValueResolver resolver) {
		return resolver;
	}

	static ValueResolver of(ValueResolver2 resolver) {
		return new ValueResolver() {

			@Override
			public String toString(MetaField field, Object value) {
				return resolver.toString(field, value);
			}

			@Override
			public String toString(Object value) {
				throw new UnsupportedOperationException("not implemented yet");
			}

		};
	}

}
