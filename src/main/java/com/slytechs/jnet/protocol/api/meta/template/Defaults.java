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

/**
 * Represents a hierarchical configuration of default values, supporting
 * inheritance from a parent instance.
 * <p>
 * This class provides default settings for various layout-related properties,
 * such as:
 * <ul>
 * <li>{@code indent}: The indentation level (integer).</li>
 * <li>{@code width}: The width of the layout (integer).</li>
 * <li>{@code align}: Text alignment (e.g., {@code LEFT}, {@code RIGHT},
 * {@code CENTER}, {@code JUSTIFY}).</li>
 * <li>{@code prefix}: A string prefix to prepend to output.</li>
 * </ul>
 * Each property can either be explicitly defined or inherited from a parent
 * {@code Defaults} instance, enabling a hierarchical configuration of defaults.
 * </p>
 * <p>
 * Example Usage:
 * 
 * <pre>{@code
 * Defaults root = Defaults.root();
 * Defaults child = Defaults.fromMap(root, Map.of(
 * 		"indent", 4,
 * 		"width", 100,
 * 		"align", "center",
 * 		"prefix", ">>"));
 *
 * System.out.println(child.indent()); // Output: 4
 * System.out.println(child.width()); // Output: 100
 * System.out.println(child.align()); // Output: CENTER
 * System.out.println(child.prefix()); // Output: >>
 * }</pre>
 * </p>
 */
public record Defaults(Defaults parent, int indent, int width, Align align) {
	public static class Builder {
		private final Defaults parent;
		private int indent;
		private int width;
		private Align align;

		public Builder(Defaults parent) {
			this.parent = parent;
		}

		public Builder align(Align align) {
			this.align = align;
			return this;
		}

		public Defaults build() {
			return new Defaults(parent, indent, width, align);
		}

		public Builder indent(int indent) {
			this.indent = indent;
			return this;
		}

		public Builder width(int width) {
			this.width = width;
			return this;
		}
	}

	public static Builder builder(Defaults parent) {
		return new Builder(parent);
	}

	/**
	 * Returns a string representation of this {@code Defaults} instance, including
	 * its properties.
	 *
	 * @return a string representation of this instance.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Defaults ["
				+ "indent=" + indent()
				+ ", width=" + width()
				+ ", align=" + align().toString().toLowerCase()
				+ "]";
	}

	/**
	 * Enum representing text alignment options.
	 */
	public enum Align {
		LEFT,
		RIGHT,
		CENTER,
		JUSTIFY,
	}

	/**
	 * A constant representing an uninitialized integer value.
	 */
	public static final int EMPTY_INT = Integer.MIN_VALUE;

	/**
	 * The default width for layout.
	 */
	public static final int DEFAULT_WIDTH = 50;

	/**
	 * The default indentation level.
	 */
	public static final int DEFAULT_INDENT = 50;

	/**
	 * The default text alignment.
	 */
	public static final Align DEFAULT_ALIGN = Align.LEFT;

	/**
	 * Creates a root-level {@code Defaults} instance with predefined values.
	 * <p>
	 * The root instance has no parent and provides the following default settings:
	 * <ul>
	 * <li>{@code indent} → {@value #DEFAULT_INDENT}</li>
	 * <li>{@code width} → {@value #DEFAULT_WIDTH}</li>
	 * <li>{@code align} → {@link Align#LEFT}</li>
	 * <li>{@code prefix} → an empty string</li>
	 * </ul>
	 * </p>
	 *
	 * @return a root-level {@code Defaults} instance.
	 */
	public static Defaults root() {
		return new Defaults(null, DEFAULT_INDENT, DEFAULT_WIDTH, DEFAULT_ALIGN);
	}

	/**
	 * Returns the indentation level, inheriting from the parent if uninitialized.
	 *
	 * @return the resolved indentation level.
	 */
	public int indent() {
		return indent == EMPTY_INT ? parent.indent() : this.indent;
	}

	/**
	 * Returns the layout width, inheriting from the parent if uninitialized.
	 *
	 * @return the resolved layout width.
	 */
	public int width() {
		return width == EMPTY_INT ? parent.width() : this.width;
	}

	/**
	 * Returns the text alignment, inheriting from the parent if uninitialized.
	 *
	 * @return the resolved text alignment.
	 */
	public Align align() {
		return align == null ? parent.align() : this.align;
	}
}