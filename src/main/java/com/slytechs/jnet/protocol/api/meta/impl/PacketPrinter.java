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
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.slytechs.jnet.protocol.api.meta.impl;

import java.io.IOException;
import java.util.stream.Stream;

import com.slytechs.jnet.platform.api.domain.DomainAccessor;
import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.MetaElement;
import com.slytechs.jnet.protocol.api.meta.MetaField;
import com.slytechs.jnet.protocol.api.meta.MetaPredicates.OpenPredicate;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern;

/**
 * Defines methods for printing packet metadata with flexible formatting and
 * indentation controls. This interface is designed to support the customization
 * of output for various metadata elements and fields associated with network
 * packets.
 * 
 * <p>
 * The methods in this interface allow for detailed and summary representations,
 * with support for configurable indentation and predicate-based filtering.
 * </p>
 * 
 * <p>
 * Implementation is expected to provide mechanisms for handling structured
 * metadata output to an {@link Appendable} target.
 * </p>
 * 
 * @see Detail
 * @see DomainAccessor
 * @see MetaField
 * @see MetaElement
 * @see MetaPattern
 * @see OpenPredicate
 * 
 * @author Mark Bednarczyk
 * @since 2024
 */
public interface PacketPrinter {

	/** Default size for indentation, measured in spaces. */
	int DEFAULT_INDENT_SIZE = 4;

	/**
	 * Appends a detailed representation of a metadata field to the specified
	 * output.
	 * 
	 * @param detail The level of detail to include in the output.
	 * @param out    The {@link Appendable} object to write the output to.
	 * @param field  The {@link MetaField} representing the metadata field to
	 *               append.
	 * @param domain The {@link DomainAccessor} used to resolve domain-specific
	 *               field values.
	 * @throws IOException If an I/O error occurs while writing to the output.
	 */
	void appendField(Detail detail, Appendable out, MetaField field, DomainAccessor domain)
			throws IOException;

	/**
	 * Appends a detailed representation of a metadata field to the specified output
	 * with control over opening and closing tags.
	 * 
	 * @param detail    The level of detail to include in the output.
	 * @param out       The {@link Appendable} object to write the output to.
	 * @param field     The {@link MetaField} representing the metadata field to
	 *                  append.
	 * @param domain    The {@link DomainAccessor} used to resolve domain-specific
	 *                  field values.
	 * @param openClose Specifies whether to include opening and closing tags for
	 *                  the field.
	 * @throws IOException If an I/O error occurs while writing to the output.
	 */
	void appendField(Detail detail, Appendable out, MetaField field, DomainAccessor domain, boolean openClose)
			throws IOException;

	/**
	 * Appends a summary representation of a metadata element to the specified
	 * output.
	 * 
	 * @param detail  The level of detail to include in the output.
	 * @param out     The {@link Appendable} object to write the output to.
	 * @param element The {@link MetaElement} representing the metadata element to
	 *                append.
	 * @param pattern The {@link MetaPattern} used for matching metadata templates.
	 * @param domain  The {@link DomainAccessor} used to resolve domain-specific
	 *                element values.
	 * @throws IOException If an I/O error occurs while writing to the output.
	 */
	void appendSummary(Detail detail, Appendable out, MetaElement element, MetaPattern pattern, DomainAccessor domain)
			throws IOException;

	/**
	 * Appends a summary representation of a metadata element to the specified
	 * output, with support for predicate-based filtering.
	 * 
	 * @param detail        The level of detail to include in the output.
	 * @param out           The {@link Appendable} object to write the output to.
	 * @param element       The {@link MetaElement} representing the metadata
	 *                      element to append.
	 * @param pattern       The {@link MetaPattern} used for matching metadata
	 *                      templates.
	 * @param domain        The {@link DomainAccessor} used to resolve
	 *                      domain-specific element values.
	 * @param openPredicate An {@link OpenPredicate} used to determine which
	 *                      elements should be expanded.
	 * @throws IOException If an I/O error occurs while writing to the output.
	 */
	void appendSummary(Detail detail, Appendable out, MetaElement element, MetaPattern pattern,
			DomainAccessor domain, OpenPredicate openPredicate)
			throws IOException;

	/**
	 * Removes the last level of indentation from the current context.
	 */
	void pop();

	/**
	 * Adds a default level of indentation to the current context.
	 */
	default void push() {
		push(DEFAULT_INDENT_SIZE);
	}

	/**
	 * Adds a specific number of spaces as indentation to the current context.
	 * 
	 * @param charCount The number of spaces to add as indentation.
	 */
	default void push(int charCount) {
		push(" ".repeat(charCount));
	}

	/**
	 * Adds a custom string as indentation to the current context.
	 * 
	 * @param additionalIndentation The string to use as additional indentation.
	 */
	void push(String additionalIndentation);

	/**
	 * Converts the {@link PacketPrinter} into an array for each {@link Detail}
	 * level.
	 * 
	 * @return An array of {@link PacketPrinter} objects, one for each
	 *         {@link Detail} level.
	 */
	default PacketPrinter[] toArray() {
		return Stream.of(Detail.values())
				.map(d -> this)
				.toArray(PacketPrinter[]::new);
	}
}
