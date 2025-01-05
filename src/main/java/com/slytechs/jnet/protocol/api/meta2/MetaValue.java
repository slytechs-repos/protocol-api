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

import com.slytechs.jnet.protocol.api.meta.spi.ValueResolverService;

/**
 * The Class MetaValue.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class MetaValue {

	/**
	 * The Interface ValueFormatter.
	 */
	public interface ValueFormatter {

		/**
		 * Format.
		 *
		 * @param value the value
		 * @return the string
		 */
		String format(Object value);
	}

	/**
	 * The Interface ValueResolver.
	 */
	public interface ValueResolver {

		public class ValueResolverType {

			public static ValueResolverType valueOf(String name) {
				var cache = ValueResolverService.cached().getResolvers();

				return new ValueResolverType(name, cache.get(name));
			}

			public static ValueResolverType[] values() {
				var array = ValueResolverService.cached().getResolverTypeArray();

				return array;
			}

			private final String name;
			private final ValueResolver resolver;

			/**
			 * @param name
			 * @param resolver
			 */
			public ValueResolverType(String name, ValueResolver resolver) {
				super();
				this.name = name;
				this.resolver = resolver;
			}

			public String name() {
				return name;
			}

			public ValueResolver getResolver() {
				return resolver;
			}

		}

		ValueResolver DEFAULT_RESOLVER = String::valueOf;

		static ValueResolver of(ValueResolver resolver) {
			return resolver;
		}

		static ValueResolver of(ValueResolverTuple2 resolver) {
			return resolver;
		}

		/**
		 * Checks if is default to formatted.
		 *
		 * @return true, if is default to formatted
		 */
		default boolean isDefaultToFormatted() {
			return true;
		}

		/**
		 * Or else.
		 *
		 * @param alternative the alternative
		 * @return the value resolver
		 */
		default ValueResolver orElse(ValueResolver alternative) {
			return v -> {
				String r = resolveValue(v);
				if (r != null)
					return r;

				return alternative.resolveValue(v);
			};
		}

		/**
		 * Resolve value.
		 *
		 * @param value the value
		 * @return the string
		 */
		String resolveValue(Object value);

		default String resolveValue(MetaField field, Object value) {
			return resolveValue(value);
		}
	}

	public interface ValueResolverTuple2 extends ValueResolver {
		@Override
		String resolveValue(MetaField field, Object value);

		/**
		 * Resolve value.
		 *
		 * @param value the value
		 * @return the string
		 */
		@Override
		default String resolveValue(Object value) {
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * Instantiates a new meta value.
	 */
	public MetaValue() {}

	/**
	 * Sets the field value.
	 *
	 * @param <T>      the generic type
	 * @param target   the target
	 * @param newValue the new value
	 */
	public <T> void setFieldValue(Object target, T newValue) {

	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @return the t
	 */
	public <T> T get() {
		return null;
	}

	/**
	 * Gets the.
	 *
	 * @param <T>  the generic type
	 * @param type the type
	 * @return the t
	 */
	public final <T> T get(Class<T> type) {
		return get();
	}
}
