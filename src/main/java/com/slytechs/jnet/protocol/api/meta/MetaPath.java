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

import java.util.Iterator;
import java.util.Optional;

/**
 * The Class MetaPath.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public final class MetaPath implements Iterable<String> {

	/** The Constant GLOBAL_DOMAINNAME. */
	public static final String GLOBAL_DOMAINNAME = "$$";

	/** The Constant ROOT_NAME. */
	public static final String ROOT_NAME = "$";

	/** The Constant UP_NAME. */
	public static final String UP_NAME = "..";

	private static final boolean DEBUG = false;

	/** The offset. */
	private final int offset;

	/** The path. */
	private final String path;

	/** The stack. */
	private final String[] stack;

	/**
	 * Instantiates a new meta path.
	 *
	 * @param path the path
	 */
	public MetaPath(String path) {
		this.path = path;
		this.stack = path.split("\\.");
		this.offset = 0;
	}

	/**
	 * Instantiates a new meta path.
	 *
	 * @param path   the path
	 * @param stack  the stack
	 * @param offset the offset
	 */
	@SuppressWarnings("unused")
	private MetaPath(String path, String[] stack, int offset) {
		this.path = path;
		this.stack = stack;
		this.offset = offset;
	}

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<String> iterator() {
		return new Iterator<String>() {
			int i = 0;

			@Override
			public boolean hasNext() {
				return i < stack.length;
			}

			@Override
			public String next() {
				return stack[i++];
			}

		};
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return path;
	}

	/**
	 * Stack.
	 *
	 * @return the string[]
	 */
	public String[] stack() {
		return stack;
	}

	/**
	 * Stack.
	 *
	 * @param index the index
	 * @return the string
	 */
	public String stack(int index) {
		return stack[index];
	}

	/**
	 * Match.
	 *
	 * @param name   the name
	 * @param offset the offset
	 * @return true, if successful
	 */
	public boolean match(String name, int offset) {
		return stack[offset].equals(name);
	}

	/**
	 * Match.
	 *
	 * @param index the index
	 * @param name  the name
	 * @return true, if successful
	 */
	public boolean match(int index, String name) {
		if (index < 0 || index >= (stack.length - offset))
			return false;

		return stack[offset + index].equals(name);
	}

	/**
	 * Match last.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	public boolean matchLast(String name) {
		return match(stack.length - 1, name);
	}

	/**
	 * Checks if is up.
	 *
	 * @param offset the offset
	 * @return true, if is up
	 */
	private boolean isUp(int offset) {
		return match(offset, UP_NAME);
	}

	/**
	 * Checks if is last.
	 *
	 * @param offset the offset
	 * @return true, if is last
	 */
	private boolean isLast(int offset) {
		return ((stack.length - 1) == offset);
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return stack.length - offset;
	}

	/**
	 * Last.
	 *
	 * @return the string
	 */
	private String last() {
		return stack[stack.length - 1];
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Search for field.
	 *
	 * @param domain the domain
	 * @return the optional
	 */
	public Optional<MetaField> searchForField(MetaDomain domain) {
		return search(domain);
	}

	/**
	 * Search.
	 *
	 * @param <V>    the value type
	 * @param domain the domain
	 * @return the optional
	 */
	public <V> Optional<V> search(MetaDomain domain) {
		return search(domain, this.offset);
	}

	/**
	 * Search.
	 *
	 * @param <K>    the key type
	 * @param <V>    the value type
	 * @param domain the domain
	 * @param offset the offset
	 * @return the optional
	 */
	private <K, V> Optional<V> search(MetaDomain domain, int offset) {
		assert domain != null : "null domain [%s]".formatted(toString());
		assert domain != domain.parent() : "infinite recursion [%s]".formatted(toString());

		if (offset >= stack.length)
			return Optional.empty();

		if (domain == null)
			return Optional.empty();

		if (match(GLOBAL_DOMAINNAME, offset))
			return search(MetaDomain.getGlobalDomain(), offset + 1);

		if (isUp(offset))
			return search(domain.parent(), offset + 1);

		if (isLast(offset)) {
			Optional<V> field = domain.findKey(last());
			if (field.isPresent()) {
				if (Global.get().orElse("MetaPath.DEBUG", DEBUG))
					System.out.printf("MetaPath::search(%s,%d) FOUND=[%s] FOUND!%n",
							domain.name(),
							offset,
							stack[offset]);

				return field;
			}

			if (domain.parent() == null) {
				if (Global.get().orElse("MetaPath.DEBUG", DEBUG))
					System.out.printf("MetaPath::search(%s,%d) NOT FOUND! [%s]%n",
							domain.name(),
							offset,
							stack[offset]);

				return Optional.empty();
			}

			if (Global.get().orElse("MetaPath.DEBUG", DEBUG))
				System.out.printf("MetaPath::search(%s,%d) UP [%s]%n",
						domain.name(),
						offset,
						stack[offset]);

			return search(domain.parent(), offset);
		}

		MetaDomain childDomain = domain.findDomain(stack[offset]);
		if (childDomain == null)
			return Optional.empty();

		return search(childDomain, offset + 1);

	}
}
