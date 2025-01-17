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

import com.slytechs.jnet.protocol.api.meta.FormatRegistry;

/**
 * Represents a compiled placeholderPattern for processing templates, combining
 * static fragments and dynamic arguments.
 * <p>
 * A {@code PlaceholderPattern} provides methods to access:
 * <ul>
 * <li>Static fragments of the template that do not change during
 * evaluation.</li>
 * <li>Dynamic arguments that are resolved or formatted at runtime.</li>
 * </ul>
 * Implementations of this interface are used in template engines to parse,
 * compile, and evaluate templates.
 * </p>
 *
 * <p>
 * Example Usage:
 * 
 * <pre>{@code
 * // Compile a template into a PlaceholderPattern
 * PlaceholderPattern placeholderPattern = TemplateResource.compile("Hello, ${name}!", macros);
 * 
 * // Retrieve static fragments
 * String[] fragments = placeholderPattern.fragments();
 * 
 * // Retrieve dynamic arguments
 * Placeholder[] args = placeholderPattern.args();
 * 
 * // Process the placeholderPattern with resolved arguments
 * String result = ...; // Some logic to combine fragments and resolved arguments
 * System.out.println(result); // Outputs: "Hello, John!" (example resolution)
 * }</pre>
 * </p>
 */
public interface PlaceholderPattern {

	/**
	 * Represents an argument that can hold an expression, a reference name, and an
	 * optional format. This interface provides methods to check for format
	 * presence, manipulate expressions, and apply formatting.
	 */
	public interface Placeholder {

		/**
		 * Checks whether the argument's expression is empty.
		 * 
		 * @return {@code true} if the expression is empty, {@code false} otherwise.
		 */
		default boolean isEmpty() {
			return expression().isEmpty();
		}

		/**
		 * Checks whether the argument's expression starts with the specified string.
		 * 
		 * @param str the string to check as a prefix.
		 * @return {@code true} if the expression starts with the specified string,
		 *         {@code false} otherwise.
		 */
		default boolean startsWith(String str) {
			return expression().startsWith(str);
		}

		/**
		 * Determines whether a format is present for this argument.
		 * 
		 * @return {@code true} if a format is defined for this argument, {@code false}
		 *         otherwise.
		 */
		boolean isFormatPresent();

		/**
		 * Returns the expression associated with this argument.
		 * 
		 * @return the argument's expression, as a {@link String}.
		 */
		String expression();

		/**
		 * Returns the reference name associated with this argument.
		 * <p>
		 * The reference name is typically used to identify a variable or an external
		 * value that the argument refers to during evaluation.
		 * </p>
		 * 
		 * @return the reference name, or an empty string if no reference name is
		 *         defined.
		 */
		String referenceName();

		/**
		 * Returns the name of the format associated with this argument.
		 * <p>
		 * The format name is used to apply specific formatting logic to the argument's
		 * value during evaluation.
		 * </p>
		 * 
		 * @return the format name, or an empty string if no format is defined.
		 */
		String formatLine();

		/**
		 * Applies the format associated with this argument to the given value.
		 * <p>
		 * If no format is defined or the format cannot be applied, this method returns
		 * {@code null}.
		 * </p>
		 * 
		 * @param value the value to format.
		 * @return the formatted value as a {@link String}, or {@code null} if no format
		 *         is applied.
		 */
		default String applyFormat(Object value) {
			return null;
		}

		/**
		 * Applies the argument's format to the given value, or falls back to the
		 * specified {@link FormatRegistry} if the argument's format cannot be applied.
		 * <p>
		 * This method first attempts to apply the argument's own format to the value.
		 * If the format cannot be applied (e.g., no format is defined or it returns
		 * {@code null}), the specified {@link FormatRegistry} is used as a fallback to
		 * apply formatting based on the argument's format name.
		 * </p>
		 * 
		 * @param value    the value to format.
		 * @param registry the {@link FormatRegistry} to use as a fallback for
		 *                 formatting.
		 * @return the formatted value as a {@link String}, either from the argument's
		 *         format or the registry.
		 */
		default String applyFormatOrElse(Object value, FormatRegistry registry) {
			var formattedValue = applyFormat(value);
			if (formattedValue == null) {
				return registry.applyFormat(value, formatLine());
			}

			return formattedValue;
		}
	}

	/**
	 * Compiles the given template string into a {@link PlaceholderPattern} using
	 * the specified macros.
	 * <p>
	 * The {@code compile} method parses the input template and processes any macros
	 * within it, returning a {@link PlaceholderPattern} object that separates
	 * static fragments from dynamic arguments. This allows for efficient evaluation
	 * of the template at runtime.
	 * </p>
	 *
	 * <p>
	 * Example:
	 * 
	 * <pre>{@code
	 * Macros macros = Macros.root();
	 * PlaceholderPattern placeholderPattern = TemplateResource.compile("Hello, ${name}!", macros);
	 *
	 * // Retrieve static fragments
	 * String[] fragments = placeholderPattern.fragments(); // ["Hello, ", "!"]
	 *
	 * // Retrieve dynamic arguments
	 * Placeholder[] args = placeholderPattern.args(); // [Placeholder representing ${name}]
	 * }</pre>
	 * </p>
	 *
	 * @param template the template string to compile, which may contain static and
	 *                 dynamic components.
	 * @param macros   the {@link Macros} instance used to resolve any macros in the
	 *                 template.
	 * @return a {@link PlaceholderPattern} representing the compiled template.
	 * @throws IllegalArgumentException if the template is invalid or cannot be
	 *                                  parsed.
	 * @see PlaceholderPattern
	 * @see Macros
	 */
	static PlaceholderPattern compile(String template, Macros macros) {
		return new DefaultTemplatePattern(template, macros);
	}

	/**
	 * Returns the static fragments of the compiled template.
	 * <p>
	 * Fragments represent the parts of the template that remain unchanged during
	 * evaluation. For example, in the template {@code "Hello, ${name}!"}, the
	 * fragments would include the static parts: {@code "Hello, "} and {@code "!"}.
	 * </p>
	 *
	 * @return an array of static fragments in the template.
	 */
	String[] fragments();

	/**
	 * Returns the dynamic placeholders (arguments) defined in the compiled
	 * template.
	 * <p>
	 * Arguments represent the variable or dynamic components of the template that
	 * are resolved or formatted at runtime. For example, in the template
	 * {@code "Hello, ${name}!"}, the argument would be the dynamic part:
	 * {@code ${name}}.
	 * </p>
	 *
	 * @return an array of {@link Placeholder} objects representing the dynamic
	 *         components of the template.
	 */
	Placeholder[] placeholders();
}