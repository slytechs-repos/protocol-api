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
package com.slytechs.jnet.protocol.api.meta.template.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.slytechs.jnet.platform.api.common.NotFound;
import com.slytechs.jnet.protocol.api.meta.template.ResourceTemplate;
import com.slytechs.jnet.protocol.api.meta.template.ResourceTemplateBuilder;
import com.slytechs.jnet.protocol.api.meta.template.Template;

/**
 * Reads and parses YAML protocol template definitions.
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class TemplateReader {

	public static ResourceTemplate readResourceTemplate(InputStream is) throws IOException {
		Yaml yaml = new Yaml();
		Map<String, Object> root = yaml.load(is);

		var builder = new ResourceTemplateBuilder();

		return builder.build(root);
	}

	public static Template readFirstTemplate(InputStream is) throws IOException {
		ResourceTemplate t = readResourceTemplate(is);

		return t.templates().iterator().next();
	}

	public static Template readTemplate(InputStream is, String templateName) throws IOException, NotFound {
		ResourceTemplate t = readResourceTemplate(is);

		for (Template template : t.templates()) {
			if (template.name().equals(templateName))
				return template;
		}

		throw new NotFound("Template with name %s is not found".formatted(templateName));
	}
}