/*
 * Sly Technologies Free License
 * 
 * Copyright 2025 Sly Technologies Inc.
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
package com.slytechs.jnet.protocol.api.meta.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.slytechs.jnet.platform.api.util.function.TypeLiteral;

/**
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public abstract class TreeBuilder<T> {

	@FunctionalInterface
	public interface Builder<T> {

		@SafeVarargs
		static <T> Builder<T> ofAnyBuilder(Builder<T> first, Builder<T>... builders) {
			return ofFirstBuilder(first, builders);
		}

		@SafeVarargs
		static <T> Builder<T> ofFirstBuilder(Builder<T> first, Builder<T>... builders) {
			if (builders.length == 0)
				return first;

			return new Builder<>() {

				@Override
				public T build(Object value) throws TemplateException {
					T builtValue = null;

					builtValue = first.build(builtValue);
					if (builtValue != null)
						return builtValue;

					for (var b : builders) {
						builtValue = b.build(value);
						if (builtValue == null)
							continue;
					}

					return builtValue;
				}

			};
		}

		static Builder<Boolean> buildBoolean() {
			return value -> (value instanceof Boolean b) ? b : null;
		}

		static <E extends Enum<E>> Builder<E> buildEnum(Class<E> enumClass) {
			return value -> {
				if (!(value instanceof String e))
					return null;

				var arr = enumClass.getEnumConstants();
				for (var c : arr)
					if (c.name().equalsIgnoreCase(e))
						return c;

				return null;
			};
		}

		static Builder<Integer> buildInt() {
			return value -> (value instanceof Number num) ? num.intValue() : null;
		}

		static Builder<Number> buildNumber() {
			return value -> (value instanceof Number num) ? num : null;
		}

		static Builder<String> buildString() {
			return value -> (value instanceof String str) ? str : null;
		}

		T build(Object value) throws TemplateException;
	}

	public interface BuilderFactory<T> {

		@SafeVarargs
		static <T> BuilderFactory<T> ofAnyFactory(BuilderFactory<T> first, BuilderFactory<T>... builderFactories) {
			return ofFirstFactory(first, builderFactories);
		}

		@SafeVarargs
		static <T> BuilderFactory<T> ofFirstFactory(BuilderFactory<T> first, BuilderFactory<T>... builderFactories) {
			if (builderFactories.length == 0)
				return first;

			return new BuilderFactory<T>() {

				@Override
				public Builder<T> newBuilder(Context context) {
					@SuppressWarnings("unchecked")
					Builder<T>[] builders = new Builder[builderFactories.length];

					for (int i = 0; i < builders.length; i++)
						builders[i] = builderFactories[i].newBuilder(context);

					return Builder.ofFirstBuilder(first.newBuilder(context), builders);
				}
			};
		}

		Builder<T> newBuilder(Context context);
	}

	public static class Context {

		private static class RootContext extends Context {
			public RootContext() {
				super(null);
			}

			@Override
			public Object getField(String key) {
				return null;
			}

			@Override
			public Object getSubField(String key) {
				return null;
			}

			@Override
			public <T> T getSubField(String key, T defaultValue) {
				return defaultValue;
			}

		}

		static Context root() {
			return new RootContext();
		}

		private final Map<String, Object> map = new HashMap<>();
		private final Context parent;

		public Context(Context parent) {
			this.parent = parent;
		}

		public void clear() {
			map.clear();
		}

		public Object getField(String key) {
			return map.get(key);
		}

		@SuppressWarnings("unchecked")
		public <T> T getField(String key, Class<T> valueType) {
			return (T) map.get(key);
		}

		@SuppressWarnings("unchecked")
		public <T> T getField(String key, T defaultValue) {
			return map.containsKey(key) ? (T) map.get(key) : defaultValue;
		}

		@SuppressWarnings("unchecked")
		public <T> T getField(String key, TypeLiteral<T> typeLiteral) {
			return (T) map.get(key);
		}

		public Object getSubField(String key) {
			return map.containsKey(key) ? map.get(key) : parent.getSubField(key);
		}

		@SuppressWarnings("unchecked")
		public <T> T getSubField(String key, Class<T> valueClass) {
			return map.containsKey(key) ? (T) map.get(key) : (T) parent.getSubField(key);
		}

		@SuppressWarnings("unchecked")
		public <T> T getSubField(String key, T defaultValue) {
			return map.containsKey(key) ? (T) map.get(key) : parent.getSubField(key, defaultValue);
		}

		@SuppressWarnings("unchecked")
		public <T> T getSubField(String key, TypeLiteral<T> typeLiteral) {
			return map.containsKey(key) ? (T) map.get(key) : (T) parent.getSubField(key);
		}

		public boolean isEmpty() {
			return map.isEmpty();
		}

		/**
		 * @param key
		 * @param object
		 */
		public void put(String key, Object value) {
			map.put(key, value);
		}

		public int size() {
			return map.size();
		}
	}

	public static abstract class ListBuilder<T, E> implements Builder<T> {

		public static final String BUILT_ENTRIES = "<built-entries>";
		private final Context context;
		private Builder<E> elementBuilder;

		private int minSize = 0;
		private Function<Object, Object> mapper;

		public ListBuilder(Context parent, Builder<E> elementBuilder) {
			this.context = new Context(parent);
			this.elementBuilder = elementBuilder;

			configure(this);
		}

		public ListBuilder(Context parent, BuilderFactory<E> elementBuilderFactory) {
			this.context = new Context(parent);
			this.elementBuilder = elementBuilderFactory.newBuilder(context);

			configure(this);
		}

		public T build(List<?> list) {
			if (list == null || list.size() < minSize)
				return null;

			final List<E> newList = new ArrayList<>();

			if (mapper != null) {

				@SuppressWarnings("unchecked")
				final List<Object> lst = (List<Object>) list;
				for (int i = 0; i < list.size(); i++) {
					Object mappedValue = mapper.apply(list.get(i));

					lst.set(i, mappedValue);
				}
			}

			if (elementBuilder != null) {
				for (int i = 0; i < list.size(); i++) {
					Object element = list.get(i);

					newList.add(elementBuilder.build(element));
				}

				context.put(BUILT_ENTRIES, newList);
			}

			T value = newInstance(context, newList);

			return value;
		}

		/**
		 * @see com.slytechs.jnet.protocol.api.meta.template.TreeBuilder.Builder#build(com.slytechs.jnet.protocol.api.meta.template.TreeBuilder.Builder,
		 *      java.lang.Object)
		 */
		@Override
		public T build(Object value) {
			return (value instanceof List<?> list) ? build(list) : null;
		}

		protected void configure(ListBuilder<T, E> builder) {}

		public ListBuilder<T, E> map(Function<Object, Object> mapper) {
			this.mapper = mapper;
			return this;
		};

		public ListBuilder<T, E> minSize(int size) {
			this.minSize = size;
			return this;
		}

		protected abstract T newInstance(Context context, List<E> newList);
	}

	public static abstract class MapBuilder<T> implements Builder<T> {

		public interface ValueFactory<T> {
			T newInstance(Context context);
		}

		private final Map<String, Builder<?>> requiredFields = new HashMap<>();
		private final Map<String, Builder<?>> optionalFields = new HashMap<>();
		private final Map<String, Builder<?>> customBuilders = new HashMap<>();
		private int minSize = 0;
		private final Context context;

		/**
		 * @param parent
		 */
		public MapBuilder(Context parent) {
			this.context = new Context(parent);
		}

		public T build(Map<?, ?> map) {
			this.context.clear();

			if (map == null || map.size() < minSize)
				return null;

			// Build required fields
			for (Map.Entry<String, Builder<?>> entry : requiredFields.entrySet()) {
				String key = entry.getKey();
				Object val = map.get(key);

				if (val == null)
					return null;

				context.put(key, entry.getValue().build(val));
			}

			// Build optional fields
			for (Map.Entry<String, Builder<?>> entry : optionalFields.entrySet()) {
				String key = entry.getKey();
				Object val = map.get(key);

				if (val != null)
					context.put(key, entry.getValue().build(val));
			}

			if (customBuilders != null && !customBuilders.isEmpty()) {
				// Build optional fields
				for (Map.Entry<String, Builder<?>> entry : customBuilders.entrySet()) {
					String key = entry.getKey();
					Object val = map.get(key);

					if (val != null)
						context.put(key, entry.getValue().build(val));
				}
			}

			T value = newInstance(context);

			return value;
		}

		/**
		 * @see com.slytechs.jnet.protocol.api.meta.template.TreeBuilder.Builder#build(com.slytechs.jnet.protocol.api.meta.template.TreeBuilder.Builder,
		 *      java.lang.Object)
		 */
		@Override
		public T build(Object value) {
			return (value instanceof Map<?, ?> map) ? build(map) : null;
		}

		protected abstract void configure(MapBuilder<T> builder);

		public MapBuilder<T> customBuilder(String key, Builder<Map<String, Object>> builder) {
			this.customBuilders.put(key, builder);

			return this;
		}

		public MapBuilder<T> minSize(int size) {
			this.minSize = size;
			return this;
		}

		protected abstract T newInstance(Context context);

		public MapBuilder<T> optionalField(String key, Builder<?> builder) {
			optionalFields.put(key, builder);
			return this;
		}

		public MapBuilder<T> optionalField(String key, BuilderFactory<?> builder) {
			optionalFields.put(key, builder.newBuilder(context));
			return this;
		}

		public MapBuilder<T> requireField(String key, Builder<?> builder) {
			requiredFields.put(key, builder);
			return this;
		}

		public MapBuilder<T> requireField(String key, BuilderFactory<?> builder) {
			requiredFields.put(key, builder.newBuilder(context));
			return this;
		}
	}

	public static abstract class StringBuilder<T> implements Builder<T> {

		public static <T> StringBuilder<T> of(Function<String, T> factory) {
			return new StringBuilder<>() {

				@Override
				public T newInstance(String string) {
					return factory.apply(string);
				}

			};
		}

		public StringBuilder() {}

		/**
		 * @see com.slytechs.jnet.protocol.api.meta.template.TreeBuilder.Builder#build(java.lang.Object)
		 */
		@Override
		public T build(Object value) throws TemplateException {
			return (value instanceof String str) ? newInstance(str) : null;
		}

		public abstract T newInstance(String string);

	}

	public TreeBuilder() {}

	public abstract T build(Object target);
}
