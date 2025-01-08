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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.slytechs.jnet.platform.api.util.Named;
import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.impl.DefaultMetaPattern;

/**
 * Represents a meta-template structure that defines or processes template
 * components such as fragments, arguments, and macros.
 * <p>
 * A {@code MetaTemplate} provides the foundational behavior for template
 * parsing, compilation, and rendering. It separates static components
 * (fragments) from dynamic components (arguments) and facilitates dynamic
 * resolution or formatting at runtime.
 * </p>
 *
 * <p>
 * Example Usage:
 * 
 * <pre>{@code
 * Macros macros = Macros.root();
 * MetaTemplate template = MetaTemplate.compile("Hello, ${name}!", macros);
 * 
 * // Retrieve template fragments
 * String[] fragments = template.fragments();
 * 
 * // Retrieve template arguments
 * Arg[] args = template.args();
 * 
 * // Use fragments and arguments to evaluate the template at runtime
 * String result = ...; // Combine fragments and resolved argument values
 * System.out.println(result); // Outputs "Hello, John!" (with appropriate resolution)
 * }</pre>
 * </p>
 *
 * <p>
 * Constants in this interface provide standard macro-related symbols used for
 * template parsing and processing.
 * </p>
 */
public interface MetaTemplate {

	/**
	 * The prefix character used to identify macros in a template.
	 * <p>
	 * A macro is typically a variable or symbol prefixed by this character, such as
	 * {@code $macroName}.
	 * </p>
	 * <p>
	 * Example:
	 * 
	 * <pre>{@code
	 * String exampleMacro = MACRO_PREFIX_CHAR + "example";
	 * System.out.println(exampleMacro); // Outputs "$example"
	 * }</pre>
	 * </p>
	 */
	char MACRO_PREFIX_CHAR = '$';

	/**
	 * The prefix string used to identify macros in a template.
	 * <p>
	 * This string is equivalent to the {@link #MACRO_PREFIX_CHAR} constant but in
	 * string form, making it easier to concatenate with other strings or variables.
	 * </p>
	 * <p>
	 * Example:
	 * 
	 * <pre>{@code
	 * String exampleMacro = MACRO_PREFIX + "example";
	 * System.out.println(exampleMacro); // Outputs "$example"
	 * }</pre>
	 * </p>
	 */
	String MACRO_PREFIX = "" + MACRO_PREFIX_CHAR;

	/**
	 * Represents a compiled pattern for processing templates, combining static
	 * fragments and dynamic arguments.
	 * <p>
	 * A {@code MetaPattern} provides methods to access:
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
	 * // Compile a template into a MetaPattern
	 * MetaPattern pattern = MetaTemplate.compile("Hello, ${name}!", macros);
	 * 
	 * // Retrieve static fragments
	 * String[] fragments = pattern.fragments();
	 * 
	 * // Retrieve dynamic arguments
	 * Arg[] args = pattern.args();
	 * 
	 * // Process the pattern with resolved arguments
	 * String result = ...; // Some logic to combine fragments and resolved arguments
	 * System.out.println(result); // Outputs: "Hello, John!" (example resolution)
	 * }</pre>
	 * </p>
	 */
	interface MetaPattern {

		/**
		 * Represents an argument that can hold an expression, a reference name, and an
		 * optional format. This interface provides methods to check for format
		 * presence, manipulate expressions, and apply formatting.
		 */
		public interface Arg {

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
		 * Compiles the given template string into a {@link MetaPattern} using the
		 * specified macros.
		 * <p>
		 * The {@code compile} method parses the input template and processes any macros
		 * within it, returning a {@link MetaPattern} object that separates static
		 * fragments from dynamic arguments. This allows for efficient evaluation of the
		 * template at runtime.
		 * </p>
		 *
		 * <p>
		 * Example:
		 * 
		 * <pre>{@code
		 * Macros macros = Macros.root();
		 * MetaPattern pattern = MetaTemplate.compile("Hello, ${name}!", macros);
		 *
		 * // Retrieve static fragments
		 * String[] fragments = pattern.fragments(); // ["Hello, ", "!"]
		 *
		 * // Retrieve dynamic arguments
		 * Arg[] args = pattern.args(); // [Arg representing ${name}]
		 * }</pre>
		 * </p>
		 *
		 * @param template the template string to compile, which may contain static and
		 *                 dynamic components.
		 * @param macros   the {@link Macros} instance used to resolve any macros in the
		 *                 template.
		 * @return a {@link MetaPattern} representing the compiled template.
		 * @throws IllegalArgumentException if the template is invalid or cannot be
		 *                                  parsed.
		 * @see MetaPattern
		 * @see Macros
		 */
		static MetaPattern compile(String template, Macros macros) {
			return new DefaultMetaPattern(template, macros);
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
		 * Returns the dynamic arguments defined in the compiled template.
		 * <p>
		 * Arguments represent the variable or dynamic components of the template that
		 * are resolved or formatted at runtime. For example, in the template
		 * {@code "Hello, ${name}!"}, the argument would be the dynamic part:
		 * {@code ${name}}.
		 * </p>
		 *
		 * @return an array of {@link Arg} objects representing the dynamic components
		 *         of the template.
		 */
		Arg[] args();
	}

	/**
	 * Represents a template for a protocol, containing a collection of detailed
	 * templates for different levels of detail, along with macros, defaults, and a
	 * hierarchical structure for metadata.
	 * <p>
	 * This class provides:
	 * <ul>
	 * <li>{@code name}: The unique name of the protocol.</li>
	 * <li>{@code detailMap}: A mapping of {@link Detail} levels to corresponding
	 * {@link DetailTemplate} objects.</li>
	 * <li>{@code detailArray}: An array of {@link DetailTemplate} objects indexed
	 * by {@code Detail.ordinal()}.</li>
	 * <li>{@code detailList}: An immutable list of {@link DetailTemplate} objects
	 * ordered by {@link Detail} enumeration.</li>
	 * <li>{@code macros}: A {@link Macros} instance for resolving macros within the
	 * protocol.</li>
	 * <li>{@code defaults}: Default settings such as alignment, indentation, and
	 * width.</li>
	 * </ul>
	 * <p>
	 * The {@code detailMap} is the primary input for constructing the protocol
	 * template, and it is automatically converted into the array and list formats
	 * for efficient access by ordinal index or as an ordered collection.
	 * </p>
	 *
	 * <p>
	 * Example Usage:
	 * 
	 * <pre>{@code
	 * Map<Detail, DetailTemplate> detailMap = Map.of(
	 *     Detail.BASIC, new DetailTemplate(Detail.BASIC, "Basic Summary", List.of(...), defaults, null),
	 *     Detail.DETAILED, new DetailTemplate(Detail.DETAILED, "Detailed Summary", List.of(...), defaults, null)
	 * );
	 *
	 * ProtocolTemplate protocolTemplate = new ProtocolTemplate(
	 *     "ExampleProtocol",
	 *     detailMap,
	 *     new Macros(null, Map.of("$example", "Example Macro")),
	 *     Defaults.root()
	 * );
	 *
	 * System.out.println(protocolTemplate.detail(Detail.BASIC)); // Access detail template by level
	 * }</pre>
	 * </p>
	 *
	 * @param name        The unique name of the protocol.
	 * @param detailMap   A mapping of {@link Detail} levels to corresponding
	 *                    {@link DetailTemplate} objects.
	 * @param detailArray An array of {@link DetailTemplate} objects, derived from
	 *                    {@code detailMap}, indexed by {@code Detail.ordinal()}.
	 * @param detailList  An immutable list of {@link DetailTemplate} objects
	 *                    ordered by {@link Detail} enumeration.
	 * @param macros      A {@link Macros} instance for resolving macros within the
	 *                    protocol.
	 * @param defaults    Default settings for the protocol, such as alignment and
	 *                    indentation.
	 * @see MetaTemplate
	 */
	public record ProtocolTemplate(
			String name,
			Map<Detail, DetailTemplate> detailMap,
			DetailTemplate[] detailArray,
			List<DetailTemplate> detailList,
			Macros macros,
			Defaults defaults) implements MetaTemplate {

		/**
		 * Converts the provided {@code detailMap} into an array of
		 * {@link DetailTemplate} objects.
		 * <p>
		 * The array is indexed by the ordinal value of the {@link Detail} enumeration,
		 * and any missing details in the map are represented as {@code null}.
		 * </p>
		 *
		 * @param detailMap The mapping of {@link Detail} levels to
		 *                  {@link DetailTemplate} objects.
		 * @return An array of {@link DetailTemplate} objects indexed by
		 *         {@code Detail.ordinal()}.
		 */
		private static DetailTemplate[] mapToArray(Map<Detail, DetailTemplate> detailMap) {
			return Arrays.stream(Detail.values())
					.map(detailMap::get)
					.toArray(DetailTemplate[]::new);
		}

		/**
		 * Converts the provided {@code detailMap} into an immutable list of
		 * {@link DetailTemplate} objects.
		 * <p>
		 * The list is ordered by the ordinal value of the {@link Detail} enumeration,
		 * and any missing details in the map are represented as {@code null}.
		 * </p>
		 *
		 * @param detailMap The mapping of {@link Detail} levels to
		 *                  {@link DetailTemplate} objects.
		 * @return An immutable list of {@link DetailTemplate} objects ordered by
		 *         {@link Detail}.
		 */
		private static List<DetailTemplate> mapToList(Map<Detail, DetailTemplate> detailMap) {
			var list = Arrays.stream(Detail.values())
					.map(detailMap::get)
					.toList();

			return Collections.unmodifiableList(list);
		}

		/**
		 * Constructs a {@code ProtocolTemplate} from a name, a detail map, macros, and
		 * defaults.
		 * <p>
		 * The {@code detailMap} is automatically converted into an array and list for
		 * efficient access by ordinal index and as an ordered collection.
		 * </p>
		 *
		 * @param name      The unique name of the protocol.
		 * @param detailMap A mapping of {@link Detail} levels to {@link DetailTemplate}
		 *                  objects.
		 * @param macros    A {@link Macros} instance for resolving macros within the
		 *                  protocol.
		 * @param defaults  Default settings for the protocol, such as alignment and
		 *                  indentation.
		 */
		public ProtocolTemplate(
				String name,
				Map<Detail, DetailTemplate> detailMap,
				Macros macros,
				Defaults defaults) {
			this(name, detailMap, mapToArray(detailMap), mapToList(detailMap), macros, defaults);
		}

		/**
		 * Retrieves the {@link DetailTemplate} associated with the given {@link Detail}
		 * level.
		 * <p>
		 * This method provides efficient access to the detail template using the
		 * {@code ordinal()} value of the {@link Detail} enumeration.
		 * </p>
		 *
		 * @param detail The detail level for which to retrieve the template.
		 * @return The {@link DetailTemplate} associated with the given detail level, or
		 *         {@code null} if no template is defined.
		 */
		public DetailTemplate detail(Detail detail) {
			return detailArray[detail.ordinal()];
		}
	}

	/**
	 * Represents a detailed template configuration containing a summary, a list of
	 * field templates, a map of field templates for fast lookup, default settings,
	 * and optional validation or formatting patterns.
	 * <p>
	 * This class supports hierarchical and detailed configurations for structured
	 * data templates. It provides:
	 * <ul>
	 * <li>{@code detail}: Metadata describing the detail level or context for the
	 * template.</li>
	 * <li>{@code summary}: A textual summary describing the template.</li>
	 * <li>{@code fieldList}: An ordered list of {@link FieldTemplate} objects
	 * defining individual fields in the template.</li>
	 * <li>{@code fieldMap}: A map of field templates by their name, allowing fast
	 * lookups.</li>
	 * <li>{@code defaults}: Default settings, such as alignment, width, or
	 * indentation.</li>
	 * <li>{@code pattern}: An optional {@link MetaPattern} for additional
	 * processing or validation.</li>
	 * </ul>
	 * The {@code fieldMap} is automatically derived from the {@code fieldList} and
	 * provides an immutable view of the fields by name.
	 * </p>
	 *
	 * <p>
	 * Example Usage:
	 * 
	 * <pre>{@code
	 * Defaults defaults = Defaults.root();
	 *
	 * FieldTemplate field1 = new FieldTemplate(
	 * 		Detail.BASIC,
	 * 		"field1",
	 * 		"Field 1",
	 * 		"{value}",
	 * 		null,
	 * 		defaults);
	 *
	 * FieldTemplate field2 = new FieldTemplate(
	 * 		Detail.BASIC,
	 * 		"field2",
	 * 		"Field 2",
	 * 		"{value * 2}",
	 * 		null,
	 * 		defaults);
	 *
	 * DetailTemplate template = new DetailTemplate(
	 * 		Detail.BASIC,
	 * 		"This is a summary",
	 * 		List.of(field1, field2),
	 * 		defaults,
	 * 		null);
	 *
	 * System.out.println(template.fieldMap().get("field1")); // Access by name
	 * System.out.println(template.fieldList()); // Access by list order
	 * }</pre>
	 * </p>
	 *
	 * @param detail    Metadata describing the detail level or context for the
	 *                  template.
	 * @param summary   A textual summary describing the template.
	 * @param fieldList An ordered list of {@link FieldTemplate} objects defining
	 *                  the fields in this template.
	 * @param fieldMap  An immutable map of field templates keyed by their name,
	 *                  derived from {@code fieldList}.
	 * @param defaults  Default settings for this template, such as alignment or
	 *                  indentation.
	 * @param pattern   An optional {@link MetaPattern} for additional validation or
	 *                  processing.
	 */
	public record DetailTemplate(
			Detail detail,
			String summary,
			List<FieldTemplate> fieldList,
			Map<String, FieldTemplate> fieldMap,
			Defaults defaults,
			MetaPattern pattern) {

		/**
		 * Converts a list of {@link FieldTemplate} objects into an immutable map, where
		 * each field is keyed by its name.
		 *
		 * @param fieldList the list of {@code FieldTemplate} objects to convert.
		 * @return an immutable map of field templates keyed by their name.
		 */
		private static Map<String, FieldTemplate> listToMap(List<FieldTemplate> fieldList) {
			var map = fieldList.stream().collect(Collectors.toMap(FieldTemplate::name, t -> t));
			return Collections.unmodifiableMap(map);
		}

		/**
		 * Constructs a {@code DetailTemplate} with a list of field templates.
		 * <p>
		 * The {@code fieldMap} is automatically derived from the {@code fieldList} and
		 * is immutable.
		 * </p>
		 *
		 * @param detail    Metadata describing the detail level or context for the
		 *                  template.
		 * @param summary   A textual summary describing the template.
		 * @param fieldList An ordered list of {@link FieldTemplate} objects defining
		 *                  the fields in this template.
		 * @param defaults  Default settings for this template, such as alignment or
		 *                  indentation.
		 * @param pattern   An optional {@link MetaPattern} for additional validation or
		 *                  processing.
		 */
		public DetailTemplate(
				Detail detail,
				String summary,
				List<FieldTemplate> fieldList,
				Defaults defaults,
				MetaPattern pattern) {
			this(detail, summary, fieldList, listToMap(fieldList), defaults, pattern);
		}
	}

	/**
	 * Represents a template for a field in a hierarchical data structure,
	 * supporting attributes such as name, label, formatting template, child fields,
	 * and default settings.
	 * <p>
	 * A {@code FieldTemplate} allows the definition of a field with:
	 * <ul>
	 * <li>{@code detail}: A {@link Detail} object providing additional information
	 * about the field.</li>
	 * <li>{@code name}: A unique identifier for the field.</li>
	 * <li>{@code label}: A human-readable label for the field, which may be
	 * empty.</li>
	 * <li>{@code template}: A string defining the formatting or representation of
	 * the field.</li>
	 * <li>{@code nodes}: A list of child {@code FieldTemplate} objects,
	 * representing nested fields.</li>
	 * <li>{@code defaults}: Default settings inherited or overridden for this field
	 * (e.g., alignment, width).</li>
	 * <li>{@code pattern}: An optional {@link MetaPattern} for additional
	 * processing or validation.</li>
	 * </ul>
	 * This structure allows for defining hierarchical templates where fields can be
	 * nested and rendered based on customizable templates and default values.
	 * </p>
	 *
	 * <p>
	 * Example:
	 * 
	 * <pre>{@code
	 * Defaults defaults = Defaults.root();
	 * FieldTemplate child = new FieldTemplate(
	 * 		Detail.BASIC,
	 * 		"childField",
	 * 		"Child Field",
	 * 		"{value}",
	 * 		null,
	 * 		defaults);
	 *
	 * FieldTemplate parent = new FieldTemplate(
	 * 		Detail.BASIC,
	 * 		"parentField",
	 * 		"Parent Field",
	 * 		"{parentTemplate}",
	 * 		List.of(child),
	 * 		defaults);
	 *
	 * System.out.println(parent);
	 * }</pre>
	 * </p>
	 *
	 * @param detail   Provides additional details or metadata about the field.
	 * @param name     The unique name of the field.
	 * @param label    A human-readable label for the field, or empty if no label is
	 *                 defined.
	 * @param template The formatting template for the field's representation.
	 * @param nodes A list of child {@code FieldTemplate} objects representing
	 *                 nested fields, or {@code null}.
	 * @param defaults The default settings for the field, such as indentation or
	 *                 alignment.
	 * @param pattern  An optional {@link MetaPattern} for validating or formatting
	 *                 the field's value.
	 * @see Named
	 */
	public record FieldTemplate(
			Detail detail,
			String name,
			String label,
			String template,
			Defaults defaults,
			MetaPattern pattern,
			List<Node> nodes) implements Named {

		/**
		 * Returns a string representation of this {@code FieldTemplate}, including its
		 * properties and a limited number of child field names (up to 10).
		 *
		 * @return a string representation of this instance.
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			final int maxLen = 10;
			return "FieldTemplate ["
					+ "detail=" + detail
					+ ", name=" + name
					+ (label.isBlank() ? "" : ", label=" + label)
					+ ", template=" + template
					+ (nodes == null
							? ""
							: ", nodes=" + nodes.stream()
									.map(Named::name)
									.limit(maxLen)
									.collect(Collectors.joining(", ", "[", "]")))
					+ ", defaults=" + defaults
					+ (pattern == null ? "" : ", pattern=" + pattern)
					+ "]";
		}

		/**
		 * Constructs a {@code FieldTemplate} without a {@link MetaPattern}.
		 * <p>
		 * This constructor allows for creating a {@code FieldTemplate} when no
		 * {@code MetaPattern} is required, while retaining all other attributes.
		 * </p>
		 *
		 * @param detail   The detail metadata for the field.
		 * @param name     The name of the field.
		 * @param label    The label for the field.
		 * @param template The formatting template for the field.
		 * @param nodes The list of child fields, or {@code null}.
		 * @param defaults The default settings for the field.
		 */
		public FieldTemplate(
				Detail detail,
				String name,
				String label,
				String template,
				Defaults defaults,
				List<Node> nodes) {
			this(detail, name, label, template, defaults, null, nodes);
		}
	}

	/**
	 * Used to for formatted display content such as groups that expand and provide
	 * more extensive information information.
	 *
	 * @author Mark Bednarczyk [mark@slytechs.com]
	 * @author Sly Technologies Inc.
	 */
	public record Node(
			Node parent,
			Detail detail,
			String name,
			String template,
			Defaults defaults,
			MetaPattern pattern,
			List<Node> nodes) implements Named {

		public boolean isEmpty() {
			return nodes == null || nodes.isEmpty();
		}

		/**
		 * Returns a string representation of this {@code FieldTemplate}, including its
		 * properties and a limited number of child field names (up to 10).
		 *
		 * @return a string representation of this instance.
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			final int maxLen = 10;
			return "FieldTemplate ["
					+ "detail=" + detail
					+ ", name=" + name
					+ ", template=" + template
					+ (nodes == null
							? ""
							: ", nodes=" + nodes.stream()
									.map(Named::name)
									.limit(maxLen)
									.collect(Collectors.joining(", ", "[", "]")))
					+ ", defaults=" + defaults
					+ (pattern == null ? "" : ", pattern=" + pattern)
					+ "]";
		}
	}

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
	record Macros(Macros parent, Map<String, String> macroMap) {

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
		 * Macro pattern $macro:
		 * 
		 * <pre>
		 * 1) first char can be `_` or a word character
		 * 2) all others can be `_`, word or digit
		 * Note: $0, $1, etc are not macros on the format string, they are args passed in 
		 *       to expression evaluator
		 * </pre>
		 */
		private static final Pattern MACRO_REGEX = Pattern.compile("(\\$[_a-zA-Z][\\w\\d_]*)");

		/**
		 * Replaces all macros within the given line with their resolved values, or
		 * returns a default value if the line is null.
		 * <p>
		 * A macro is defined as a sequence of characters matching the pattern
		 * {@code $[_\w][\w\d_]*}, where:
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
			if (lastMatchEnd > 0) // At least 1 replacement
				return replaceOrDefault(str, orDefault);

			return str;
		}

	}

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
	public record Defaults(Defaults parent, int indent, int width, Align align, String prefix) {

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
					+ ", prefix=\"" + prefix() + "\""
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
			return new Defaults(null, DEFAULT_INDENT, DEFAULT_WIDTH, DEFAULT_ALIGN, "");
		}

		@SuppressWarnings("unchecked")
		public static Defaults fromMap(Defaults parent, Object defaultsMap) {
			return fromMap(parent, (Map<String, Object>) defaultsMap);
		}

		/**
		 * Creates a {@code Defaults} instance from a given map, with an optional
		 * parent.
		 * <p>
		 * If the provided map is {@code null} or empty, the parent instance is returned
		 * directly. Otherwise, a new {@code Defaults} instance is created with values
		 * from the map. Any missing values in the map will inherit from the parent.
		 * </p>
		 * <p>
		 * Map Keys:
		 * <ul>
		 * <li>{@code indent} (integer): The indentation level.</li>
		 * <li>{@code width} (integer): The layout width.</li>
		 * <li>{@code align} (string): The text alignment (case-insensitive, e.g.,
		 * "left").</li>
		 * <li>{@code prefix} (string): The output prefix.</li>
		 * </ul>
		 * </p>
		 *
		 * @param parent      the parent {@code Defaults} instance, or {@code null} if
		 *                    no parent exists.
		 * @param defaultsMap the map containing default values for this instance.
		 * @return a new {@code Defaults} instance, or the parent if the map is
		 *         {@code null} or empty.
		 */
		public static Defaults fromMap(Defaults parent, Map<String, Object> defaultsMap) {
			if (defaultsMap == null || defaultsMap.isEmpty())
				return parent;

			int indent = (int) defaultsMap.getOrDefault("indent", EMPTY_INT);
			int width = (int) defaultsMap.getOrDefault("width", EMPTY_INT);
			Align align = defaultsMap.containsKey("align")
					? Align.valueOf(((String) defaultsMap.get("align")).toUpperCase())
					: null;
			String prefix = (String) defaultsMap.getOrDefault("prefix", null);

			return new Defaults(parent, indent, width, align, prefix);
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
		 * Returns the prefix string, inheriting from the parent if uninitialized.
		 *
		 * @return the resolved prefix string.
		 */
		public String prefix() {
			return prefix == null ? parent.prefix() : this.prefix;
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

}