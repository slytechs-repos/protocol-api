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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.slytechs.jnet.platform.api.common.NotFound;
import com.slytechs.jnet.protocol.api.meta.template.Import.Imports;
import com.slytechs.jnet.protocol.api.meta.template.impl.TemplateReader;

/**
 * Represents a meta-template structure that defines or processes template
 * components such as fragments, arguments, and macros.
 * <p>
 * A {@code TemplateResource} provides the foundational behavior for template
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
 * TemplateResource template = TemplateResource.compile("Hello, ${name}!", macros);
 * 
 * // Retrieve template fragments
 * String[] fragments = template.fragments();
 * 
 * // Retrieve template arguments
 * Placeholder[] args = template.args();
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
public record ResourceTemplate(
		Overview overview,
		Defaults defaults,
		Macros macros,
		Imports imports,
		Templates templates) {

	public static ResourceTemplate loadResourceTemplate(String resourceName) throws IOException {
		if (!resourceName.contains("\\.yaml"))
			throw new IllegalArgumentException("unknown resource template type, unrecognized resource extension");

		var in = ResourceTemplate.class.getResourceAsStream(resourceName);

		return loadYAML(in, resourceName);
	}

	private static ResourceTemplate loadYAML(InputStream in, String resourceName) throws IOException {
		return TemplateReader.readResourceTemplate(in);
	}

	public static Template loadFirstTemplate(String resourceName) throws IOException {
		var res = loadResourceTemplate(resourceName);
		if (res == null)
			throw new FileNotFoundException("ResourceTemplate file %s not found".formatted(resourceName));

		return res.templates().iterator().next();
	}

	public static Template loadTemplate(String resourceName, String templateName) throws IOException, NotFound {
		var res = loadResourceTemplate(resourceName);
		if (res == null)
			throw new FileNotFoundException("ResourceTemplate file %s not found".formatted(resourceName));

		for (Template template : res.templates()) {
			if (template.name().equals(templateName))
				return template;
		}

		throw new NotFound("Template with name %s is not found".formatted(templateName));
	}
}
