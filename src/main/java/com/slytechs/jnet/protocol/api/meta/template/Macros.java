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

import java.util.Map;

/**
 * Represents a hierarchical structure of macro definitions, allowing for
 * resolution of macros with support for parent-child relationships. Macros are
 * key-value pairs, where the key is a macro name (e.g., {@code $bits}), and the
 * value is its corresponding substitution.
 * <p>
 * This class enables macro resolution in a nested (hierarchical) context, where
 * macros defined in the current instance take precedence over those in its
 * parent.
 * </p>
 * <p>
 * Example:
 * 
 * <pre>{@code
 * Macros root = Macros.root();
 * Macros child = Macros.fromMap(root, Map.of("$size", "1024"));
 *
 * System.out.println(child.resolveIfPresent("$size")); // Output: 1024
 * System.out.println(child.resolveIfPresent("$bits")); // Output: bits
 * }</pre>
 * </p>
 */
public record Macros(Macros parent, Map<String, String> macroMap) {

	/**
	 * Returns a string representation of the macro map.
	 *
	 * @return a string containing the macro map entries.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Macros ["
				+ (macroMap == null ? "" : "macroMap=" + macroMap)
				+ "]";
	}

	/**
	 * Creates a root-level {@code Macros} instance with predefined macros.
	 * <p>
	 * The root instance does not have a parent and provides the following default
	 * macros:
	 * <ul>
	 * <li>{@code $bits} → {@code bits}</li>
	 * <li>{@code $bytes} → {@code bytes}</li>
	 * </ul>
	 * </p>
	 *
	 * @return the root-level {@code Macros} instance with default macros.
	 */
	public static Macros root() {
		return new Macros(null, Map.of(

				"$bits", "bits",
				"$bytes", "bytes"

		));
	}

	public static Macros fromContainer(Macros parent, Map<String, Object> container) {
		if (container == null)
			return parent;

		@SuppressWarnings("unchecked")
		Map<String, String> macroMap = (Map<String, String>) container.get("macros");

		return fromMap(parent, macroMap);
	}

	/**
	 * Creates a {@code Macros} instance from a given map, with an optional parent.
	 * <p>
	 * If the provided map is {@code null} or empty, the parent instance is returned
	 * directly. Otherwise, a new {@code Macros} instance is created with the given
	 * map as its macro definitions and the specified parent for hierarchical
	 * resolution.
	 * </p>
	 *
	 * @param parent   the parent {@code Macros} instance, or {@code null} if no
	 *                 parent exists.
	 * @param macroMap the macro definitions for the current instance.
	 * @return a new {@code Macros} instance, or the parent if the map is
	 *         {@code null} or empty.
	 */
	public static Macros fromMap(Macros parent, Map<String, String> macroMap) {
		if (macroMap == null || macroMap.isEmpty())
			return parent;

		return new Macros(parent, macroMap);
	}

	/**
	 * Checks if the specified macro name is defined in the current instance or any
	 * parent.
	 *
	 * @param name the macro name to check.
	 * @return {@code true} if the macro is defined in the current instance or any
	 *         parent; {@code false} otherwise.
	 */
	public boolean contains(String name) {
		if (macroMap.containsKey(name))
			return true;

		return (parent == null) ? false : parent.contains(name);
	}

	/**
	 * Resolves the value of a macro by its name, searching the current instance
	 * first, and then recursively in its parent hierarchy if not found.
	 * <p>
	 * If the macro is not defined in any context, the original name is returned.
	 * </p>
	 *
	 * @param name the macro name to resolve.
	 * @return the resolved value of the macro, or the original name if not found.
	 */
	private String resolveIfPresent(String name) {
		return macroMap.containsKey(name)
				? macroMap.get(name)
				: (parent == null) ? name : parent.resolveIfPresent(name);
	}

	/**
	 * Macro placeholderPattern $macro:
	 * 
	 * <pre>
	 * 1) first char can be `_` or a word character
	 * 2) all others can be `_`, word or digit
	 * Note: $0, $1, etc are not macros on the format string, they are args passed in 
	 *       to expression evaluator
	 * </pre>
	 */
	private static final java.util.regex.Pattern MACRO_REGEX = java.util.regex.Pattern.compile(
			"(\\$[_a-zA-Z][\\w\\d_]*)");

	/**
	 * Replaces all macros within the given line with their resolved values, or
	 * returns a default value if the line is null.
	 * <p>
	 * A macro is defined as a sequence of characters matching the
	 * placeholderPattern {@code $[_\w][\w\d_]*}, where:
	 * <ul>
	 * <li>The macro begins with a dollar sign ({@code $}).</li>
	 * <li>The first character after the dollar sign must be a letter or underscore
	 * ({@code _}).</li>
	 * <li>The remaining characters can include letters, digits, or underscores
	 * ({@code _}).</li>
	 * </ul>
	 * Each macro is resolved using the {@code resolveIfPresent(String macro)}
	 * method. If a macro cannot be resolved (i.e., {@code resolveIfPresent} returns
	 * {@code null}), the macro is left unchanged in the resulting string.
	 * <p>
	 * The method processes the macros sequentially, ensuring that newly introduced
	 * macros (resulting from replacements) are also resolved.
	 *
	 * @param line      the input string containing macros to be replaced; may be
	 *                  {@code null}.
	 * @param orDefault the default value to return if {@code line} is {@code null}.
	 * @return a new string with all macros replaced by their resolved values, or
	 *         the {@code orDefault} value if {@code line} is {@code null}.
	 */
	public String replaceOrDefault(String line, String orDefault) {
		if (line == null)
			return orDefault;

		var matcher = MACRO_REGEX.matcher(line);
		var result = new StringBuilder();

		int lastMatchEnd = 0;
		while (matcher.find()) {
			// Append the text before the current match
			result.append(line, lastMatchEnd, matcher.start());

			// Get the matched macro and resolve its replacement
			var macro = matcher.group(1);
			var newVal = resolveIfPresent(macro);

			// Append the replacement (or leave it unchanged if null)
			result.append(newVal != null ? newVal : macro);

			// Update the last match end position
			lastMatchEnd = matcher.end();
		}

		// Append any remaining text after the last match
		result.append(line, lastMatchEnd, line.length());

		String str = result.toString();
//			if (lastMatchEnd > 0) // At least 1 replacement
//				return replaceOrDefault(str, orDefault);

		return str;
	}

}