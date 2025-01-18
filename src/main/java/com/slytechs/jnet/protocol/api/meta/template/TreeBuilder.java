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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import com.slytechs.jnet.platform.api.util.Named;
import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.platform.api.util.format.Printable;
import com.slytechs.jnet.platform.api.util.function.Pair;
import com.slytechs.jnet.platform.api.util.function.Tuple.Tuple4;
import com.slytechs.jnet.platform.api.util.function.TypeLiteral;

/**
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public abstract class TreeBuilder<T> {

	@FunctionalInterface
	public interface Builder<T> {

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

	/**
	 * A hierarchical context implementation that maintains key-value pairs in a
	 * parent-child relationship. Each Context instance can have a parent context,
	 * forming a chain up to a root context. Values can be stored locally in each
	 * context instance and retrieved either directly or through the parent chain.
	 *
	 * <p>
	 * The context hierarchy is immutable once created, though the key-value pairs
	 * within each context instance can be modified. Thread safety is provided for
	 * context creation through atomic indexing, but individual context instances
	 * are not thread-safe for modifications.
	 *
	 * <p>
	 * This class implements Iterable&lt;Pair&lt;String, Object&gt;&gt;, allowing
	 * iteration over all key-value pairs in the context hierarchy (from this
	 * context up to the root). When iterating:
	 * <ul>
	 * <li>Each key appears exactly once in the iteration
	 * <li>For keys that exist in multiple contexts, the value from the closest
	 * context to the current one is used
	 * <li>The root context's pairs are not included as it never stores values
	 * </ul>
	 *
	 * <p>
	 * Key features:
	 * <ul>
	 * <li>Hierarchical value lookup through parent contexts
	 * <li>Type-safe value retrieval with default values
	 * <li>Protected root context with special behavior
	 * <li>Built-in trace verification capabilities
	 * <li>Iterable interface for accessing all unique key-value pairs in the
	 * hierarchy
	 * </ul>
	 *
	 * <p>
	 * Example usage:
	 * 
	 * <pre>{@code
	 * Context root = Context.root();
	 * Context child = new Context(root);
	 * child.put("key", "value");
	 * String value = child.getSubField("key", String.class); // Retrieves from child
	 * String parentValue = child.getSubField("parentKey", String.class); // Checks parent if not in child
	 * 
	 * // Iterate over all unique key-value pairs in the hierarchy
	 * for (Pair<String, Object> pair : child) {
	 * 	System.out.println(pair.getKey() + ": " + pair.getValue());
	 * }
	 * }</pre>
	 */
	public static class Context implements Iterable<Pair<String, Object>>, Named, Printable {

		/**
		 * Special implementation of Context that serves as the root of any context
		 * hierarchy. The RootContext has unique behavior:
		 * <ul>
		 * <li>All field lookups return null (except for the special trace key)
		 * <li>Attempts to put values throw UnsupportedOperationException
		 * <li>Default values are returned as-is without storage
		 * </ul>
		 * 
		 * <p>
		 * This implementation ensures that field lookups have a well-defined
		 * termination point when traversing up the context hierarchy.
		 */
		private static class RootContext extends Context {

			/**
			 * Creates a new RootContext instance. Initializes using the parent Context's
			 * no-arg constructor.
			 */
			public RootContext() {
				super();
			}

			/**
			 * Always returns null for any key lookup. This provides a terminal case for
			 * field lookups in the context hierarchy.
			 *
			 * @param key the key to look up (ignored)
			 * @return null in all cases
			 */
			@Override
			public final Object getField(String key) {
				return null;
			}

			/**
			 * Returns the special trace key value if requested, null otherwise. This method
			 * specifically handles the trace key for validation purposes while maintaining
			 * null returns for all other keys.
			 *
			 * @param key the key to look up
			 * @return TRACE_KEY if the key matches TRACE_KEY, null otherwise
			 */
			@Override
			public final Object getSubField(String key) {
				if (key.equals(TRACE_KEY))
					return TRACE_KEY;

				return null;
			}

			/**
			 * Returns the provided default value without storing or looking up anything.
			 * This ensures that default values are properly handled at the root level.
			 *
			 * @param <T>          the type of the default value
			 * @param key          the key to look up (ignored)
			 * @param defaultValue the default value to return
			 * @return the defaultValue parameter as-is
			 */
			@Override
			public final <T> T getSubField(String key, T defaultValue) {
				return defaultValue;
			}

			/**
			 * Prevents any modifications to the root context.
			 * 
			 * @param key   the key to store (ignored)
			 * @param value the value to store (ignored)
			 * @throws UnsupportedOperationException in all cases
			 */
			@Override
			public final void put(String key, Object value) {
				throw new UnsupportedOperationException();
			}

			@Override
			public Stream<Context> streamHierarchy() {
				return Stream.of(this);
			}

			/**
			 * @see com.slytechs.jnet.protocol.api.meta.template.TreeBuilder.Context#streamKeys()
			 */
			@Override
			public Stream<String> streamKeys() {
				return Stream.of();
			}

			@Override
			protected Stream<Tuple4<String, Object, Integer, String>> streamWithDepth(int level) {
				return Stream.of();
			}
		}

		private static final AtomicInteger COUNTER = new AtomicInteger();

		public static final String TRACE_KEY = "<trace>"; // Reserved

		private static final Context ROOT = new RootContext();

		/**
		 * Creates a new root context instance. The root context serves as the top-level
		 * parent in the context hierarchy and has special behavior for field access and
		 * modifications.
		 *
		 * @return a new root context instance
		 */
		public static Context root() {
			return new Context(ROOT);
		}

		private final Map<String, Object> map = new HashMap<>();

		private final Context parent;

		private final Context root;
		private final int index;
		private String name;

		/**
		 * Creates a new context with the specified parent. The new context inherits the
		 * ability to look up values from its parent when they are not found locally.
		 *
		 * @param parent the parent context, must not be null unless creating the root
		 *               context
		 * @throws AssertionError if parent is null (when assertions are enabled)
		 */
		private Context() {
			this.index = COUNTER.getAndIncrement();
			this.parent = null;
			this.root = null;
			this.name = "RootContext";
//			System.out.println("Context::init " + toString());
		}

		/**
		 * Creates a new context with the specified parent context. The new context
		 * instance maintains a reference to its parent for hierarchical field lookups
		 * and receives a unique index for identification purposes.
		 * 
		 * <p>
		 * During construction, this method verifies the integrity of the context
		 * hierarchy by traversing up to the root context. If any parent in the chain is
		 * null (except for the special ROOT context case), an assertion error will be
		 * thrown when assertions are enabled.
		 * 
		 * <p>
		 * The constructor also logs the initialization using System.out.println, which
		 * can be useful for debugging context creation.
		 *
		 * @param parent the parent context to establish the hierarchical relationship.
		 *               Must not be null unless this is the special ROOT context
		 *               instance
		 * @throws AssertionError if the parent is null or if any ancestor context in
		 *                        the chain up to root is null (when assertions are
		 *                        enabled)
		 * @see #isRoot()
		 * @see #getRoot()
		 */
		public Context(Context parent) {
			this.index = COUNTER.getAndIncrement();
			this.parent = parent;
			this.name = "Context:" + index;
//			System.out.println("Context::init " + toString());

			var root = parent;
			if (this != ROOT) {
				assert root != null : "parent is null";

				while (!root.isRoot()) {
					root = root.parent;
					assert root != null;
				}
			}

			this.root = null;
		}

		/**
		 * Removes all key-value pairs from this context instance. This operation does
		 * not affect the parent context.
		 */
		public final void clear() {
			map.clear();
		}

		/**
		 * Retrieves a value from this context's local storage. Does not check parent
		 * contexts.
		 *
		 * @param key the key to look up
		 * @return the value associated with the key, or null if not found
		 */
		public Object getField(String key) {
			return map.get(key);
		}

		/**
		 * Retrieves a typed value from this context's local storage. Does not check
		 * parent contexts.
		 *
		 * @param <T>        the expected type of the value
		 * @param key        the key to look up
		 * @param valueClass the class representing the expected type
		 * @return the value associated with the key, cast to the specified type, or
		 *         null if not found
		 */
		@SuppressWarnings("unchecked")
		public final <T> T getField(String key, Class<T> valueClass) {
			return (T) map.get(key);
		}

		/**
		 * Retrieves a typed value with a default from this context's local storage.
		 * Does not check parent contexts.
		 *
		 * @param <T>          the expected type of the value
		 * @param key          the key to look up
		 * @param defaultValue the value to return if the key is not found
		 * @return the value associated with the key, cast to the specified type, or
		 *         defaultValue if not found
		 */
		@SuppressWarnings("unchecked")
		public final <T> T getField(String key, T defaultValue) {
			return map.containsKey(key) ? (T) map.get(key) : defaultValue;
		}

		@SuppressWarnings("unchecked")
		public final <T> T getField(String key, TypeLiteral<T> typeLiteral) {
			return (T) map.get(key);
		}

		/**
		 * Returns the root context in this context's hierarchy.
		 *
		 * @return the root context instance
		 */
		public final Context getRoot() {
			return root;
		}

		/**
		 * Retrieves a value, checking parent contexts if not found locally. Searches
		 * through the context hierarchy starting from this context up through parent
		 * contexts until the value is found or the root is reached.
		 *
		 * @param key the key to look up
		 * @return the value associated with the key, or null if not found in this or
		 *         any parent context
		 */
		public Object getSubField(String key) {
			return map.containsKey(key) ? map.get(key) : parent.getSubField(key);
		}

		/**
		 * Retrieves a typed value, checking parent contexts if not found locally.
		 *
		 * @param <T>        the expected type of the value
		 * @param key        the key to look up
		 * @param valueClass the class representing the expected type
		 * @return the value associated with the key, cast to the specified type, or
		 *         null if not found
		 */
		@SuppressWarnings("unchecked")
		public final <T> T getSubField(String key, Class<T> valueClass) {
			return map.containsKey(key) ? (T) map.get(key) : (T) parent.getSubField(key);
		}

		/**
		 * Retrieves a typed value with a default, checking parent contexts if not found
		 * locally.
		 *
		 * @param <T>          the expected type of the value
		 * @param key          the key to look up
		 * @param defaultValue the value to return if the key is not found in this or
		 *                     any parent context
		 * @return the value associated with the key, cast to the specified type, or
		 *         defaultValue if not found
		 */
		@SuppressWarnings("unchecked")
		public <T> T getSubField(String key, T defaultValue) {
			return map.containsKey(key) ? (T) map.get(key) : parent.getSubField(key, defaultValue);
		}

		@SuppressWarnings("unchecked")
		public final <T> T getSubField(String key, TypeLiteral<T> typeLiteral) {
			return map.containsKey(key) ? (T) map.get(key) : (T) parent.getSubField(key);
		}

		/**
		 * Checks if this context's local storage is empty.
		 *
		 * @return true if this context contains no key-value pairs, false otherwise
		 */
		public final boolean isEmpty() {
			return map.isEmpty();
		}

		/**
		 * Checks if this context is a root context. A context is considered root if it
		 * either has no parent or its parent is the special ROOT instance.
		 *
		 * @return true if this is a root context, false otherwise
		 */
		public final boolean isRoot() {
			return parent == null || this.parent == ROOT;
		}

		@Override
		public Iterator<Pair<String, Object>> iterator() {
			return stream().iterator();
		}

		/**
		 * @see com.slytechs.jnet.platform.api.util.Named#name()
		 */
		@Override
		public String name() {
			return name;
		}

		/**
		 * @see com.slytechs.jnet.platform.api.util.format.Printable#printTo(java.lang.Appendable,
		 *      com.slytechs.jnet.platform.api.util.format.Detail)
		 */
		@Override
		public void printTo(Appendable out, Detail detail) throws IOException {
			switch (detail) {
			case HIGH -> out.append((" ".repeat(index)) + name() + " " + map.toString());
			default -> out.append(name() + " " + map.toString());
			};
		}

		/**
		 * Stores a value in this context's local storage.
		 *
		 * @param key   the key under which to store the value
		 * @param value the value to store
		 * @throws IllegalArgumentException if the key is the reserved trace key
		 */
		public void put(String key, Object value) {
			if (key.equals(TRACE_KEY))
				throw new IllegalArgumentException("reseved key " + TRACE_KEY);

			if (value == null)
				throw new IllegalArgumentException("can not set null value for \"%s\"".formatted(key));

			map.put(key, value);
		}

		/**
		 * Retrieves a required value from this context or its parents, throwing an
		 * exception if null. The value will be unchecked cast to type T.
		 *
		 * @param <T> the expected type of the value
		 * @param key the key to look up
		 * @return the non-null value associated with the key, cast to type T
		 * @throws NullPointerException if the value is null
		 * @throws ClassCastException   if the value cannot be cast to type T
		 */
		public final <T> T requireNonNull(String key) {
			@SuppressWarnings("unchecked")
			T value = (T) getSubField(key);
			if (value == null)
				throw new NullPointerException("required field \"%s\" is null".formatted(key));

			return value;
		}

		/**
		 * Retrieves a required value of a specific class from this context or its
		 * parents. The value must be non-null and must be assignable to the specified
		 * class.
		 *
		 * @param <T>        the expected type of the value
		 * @param key        the key to look up
		 * @param valueClass the Class object representing the required type
		 * @return the non-null value associated with the key, verified to be of type T
		 * @throws NullPointerException if the value is null
		 * @throws ClassCastException   if the value is not assignable to the specified
		 *                              class
		 */
		public final <T> T requireNonNull(String key, Class<T> valueClass) {
			T value = getSubField(key, valueClass);
			if (value == null)
				throw new NullPointerException("required field \"%s\" is null".formatted(key));

			return value;
		}

		/**
		 * Retrieves a required value from this context or its parents, using the type
		 * of defaultValue. Despite taking a default value parameter, this method will
		 * still throw an exception if the value is null - the defaultValue parameter is
		 * only used for type inference.
		 *
		 * @param <T>          the expected type of the value
		 * @param key          the key to look up
		 * @param defaultValue the default value, used only for type inference (not
		 *                     returned if key not found)
		 * @return the non-null value associated with the key
		 * @throws NullPointerException if the value is null
		 * @throws ClassCastException   if the value cannot be cast to the same type as
		 *                              defaultValue
		 */
		public <T> T requireNonNull(String key, T defaultValue) {
			T value = getSubField(key, defaultValue);
			if (value == null)
				throw new NullPointerException("required field \"%s\" is null".formatted(key));

			return value;
		}

		/**
		 * Retrieves a required value with generic type information from this context or
		 * its parents. This method supports complex generic types through TypeLiteral,
		 * allowing for proper type checking of generic parameters that would normally
		 * be erased at runtime.
		 * 
		 * <p>
		 * For example, this method can properly handle types like List&lt;String&gt; or
		 * Map&lt;Integer, List&lt;String&gt;&gt; where simple Class objects would lose
		 * the generic type information.
		 *
		 * <p>
		 * Example usage:
		 * 
		 * <pre>{@code
		 * // Retrieving a List<String> with proper generic type checking
		 * List<String> list = context.requireNonNull("key", new TypeLiteral<List<String>>() {});
		 * }</pre>
		 *
		 * @param <T>         the expected type of the value, potentially including
		 *                    generic parameters
		 * @param key         the key to look up
		 * @param typeLiteral a TypeLiteral instance capturing the complete generic type
		 *                    information
		 * @return the non-null value associated with the key, with verified generic
		 *         type information
		 * @throws NullPointerException if the value is null
		 * @throws ClassCastException   if the value cannot be cast to the type
		 *                              represented by the TypeLiteral
		 * @see TypeLiteral
		 */
		public final <T> T requireNonNull(String key, TypeLiteral<T> typeLiteral) {
			T value = getSubField(key, typeLiteral);
			if (value == null)
				throw new NullPointerException("required field \"%s\" is null".formatted(key));

			return value;
		}

		/**
		 * Verifies the trace program by checking if the special trace key exists and
		 * contains the expected value throughout the context hierarchy.
		 *
		 * @return this context instance
		 * @throws IllegalStateException if the trace verification fails
		 */
		public Context runTraceProgram() {
			String result = (String) getSubField(TRACE_KEY);
			if (result == null || !result.equals(TRACE_KEY))
				throw new IllegalStateException("trace from bottom to root failed");

			return this;
		}

		/**
		 * Returns the number of key-value pairs stored in this context's local storage.
		 *
		 * @return the number of entries in this context
		 */
		public int size() {
			return map.size();
		}

		public Stream<Pair<String, Object>> stream() {
			return streamKeys()
					.map(key -> Pair.of(key, getSubField(key)));
		}

		public Stream<Context> streamHierarchy() {
			return Stream.concat(parent.streamHierarchy(), Stream.of(this));
		}

		public Stream<String> streamKeys() {
			return Stream.concat(
					map.keySet().stream(),
					parent.streamKeys()).distinct();
		}

		/**
		 * Returns a stream of tuples containing the key, value, and depth level for
		 * each field, where depth indicates how many parent levels above the current
		 * object the field was found. A depth of 0 indicates the field is from the
		 * current object, 1 means from immediate parent, and so on.
		 *
		 * @return a stream of Tuple3 containing key, value and depth level for each
		 *         field
		 */
		public Stream<Tuple4<String, Object, Integer, String>> streamWithDepth() {
			return streamWithDepth(0);
		}

		/**
		 * Returns a stream of tuples containing the key, value, and depth level for
		 * each field, where depth indicates how many parent levels above the current
		 * object the field was found. A depth of 0 indicates the field is from the
		 * current object, 1 means from immediate parent, and so on.
		 *
		 * @param level the current depth level being processed
		 * @return a stream of Tuple3 containing key, value and depth level for each
		 *         field
		 */
		protected Stream<Tuple4<String, Object, Integer, String>> streamWithDepth(int level) {
			return Stream.concat(
					// Current level fields with depth 'level'
					map.keySet().stream()
							.map(key -> Tuple4.of(key, getSubField(key), level, name())),
					// Parent fields with incremented depth
					parent.streamWithDepth(level + 1)).distinct();
		}

		@Override
		public String toString() {
			return printToString();
		}
	}

	public static abstract class ListBuilder<T, E> implements Builder<T> {

		public static final String BUILT_ENTRIES = "<built-entries>";
		private final Context context;
		private Builder<E> elementBuilder;

		private int minSize = 0;
		private Function<Object, Object> mapper;

		public ListBuilder(Context parent) {
			this.context = new Context(parent);

			configure(this);

			context.streamWithDepth()
					.forEach(System.out::println);

		}

		public ListBuilder(Context parent, Builder<E> elementBuilder) {
			this.context = new Context(parent);
			this.elementBuilder = elementBuilder;

			configure(this);

			context.streamWithDepth()
					.forEach(System.out::println);

		}

		public ListBuilder(Context parent, BuilderFactory<E> elementBuilderFactory) {
			this.context = new Context(parent);
			this.elementBuilder = elementBuilderFactory.newBuilder(context);

			configure(this);

			context.streamWithDepth()
					.forEach(System.out::println);

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

		public ListBuilder<T, E> elementBuilder(BuilderFactory<E> elementBuilder) {
			this.elementBuilder = elementBuilder.newBuilder(context);

			return this;
		};

		public ListBuilder<T, E> map(Function<Object, Object> mapper) {
			this.mapper = mapper;
			return this;
		}

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
		protected final Context context;

		/**
		 * @param parent
		 */
		public MapBuilder(Context parent) {
			this.context = new Context(parent);

			configure(this);

			context.streamWithDepth()
					.forEach(System.out::println);
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
