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

import static java.util.Objects.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slytechs.jnet.platform.api.domain.DomainAccessor;
import com.slytechs.jnet.protocol.api.meta.FormatRegistry;
import com.slytechs.jnet.protocol.api.meta.template.PlaceholderPattern.Placeholder;

/**
 * A meta template, similar to JDK PlaceholderPattern, where a precompiled
 * string template with placeholders (args) is able to substitute and format all
 * references values, and substibute in place of the placeholder arguments. It
 * further allows expression parsing with specialized placeholder (arg) syntax.
 * 
 * <p>
 * The format registry is typically provided from some global definitions, so
 * its available during construction of the skeleton template and must always be
 * provided.
 * </p>
 * 
 * <p>
 * The parsed placeholderPattern
 *
 * @param template           the template
 * @param formats            the formats
 * @param placeholderPattern the placeholderPattern
 * @author Mark Bednarczyk
 */
public record TemplatePattern(String template, FormatRegistry formats, PlaceholderPattern pattern) {

	public TemplatePattern(String template) {
		this(requireNonNull(template), FormatRegistry.of(), PlaceholderPattern.compile(template, Macros.root()));
	}

	public TemplatePattern(String template, FormatRegistry formats, Macros macros) {
		this(requireNonNull(template, "template"), formats, PlaceholderPattern.compile(template, macros));
	}

	public TemplatePattern(String template, Macros macros) {
		this(requireNonNull(template, "template"), FormatRegistry.of(), PlaceholderPattern.compile(template,
				macros));
	}

	private static final Logger logger = LoggerFactory.getLogger(TemplatePattern.class);

	@Override
	public String toString() {
		return template;
	}

	public String[] fragments() {
		return pattern.fragments();
	}

	public Placeholder[] placeholders() {
		return pattern.placeholders();
	}

	public String toString(Object cwd, DomainAccessor domain) {

		StringBuilder sb = new StringBuilder();
		var frags = pattern.fragments();
		var args = pattern.placeholders();

		for (int i = 0; i < frags.length; i++) {
			String frag = frags[i];
			sb.append(frag);

			if (i >= args.length)
				continue;

			try {
				Placeholder placeholder = args[i];
				Object refValue = domain.resolve(placeholder.referenceName(), cwd);

				String formatted = placeholder.applyFormatOrElse(refValue, formats);

				sb.append(formatted);
			} catch (Throwable e) {
				logger.error("arg=%s".formatted(pattern.toString()));

				throw e;
			}
		}

		return sb.toString();
	}
}