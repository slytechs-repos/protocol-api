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
package com.slytechs.jnet.protocol.api.meta.template;

import java.io.IOException;

import com.slytechs.jnet.platform.api.common.NotFound;
import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.template.Template.TemplateDetail;
import com.slytechs.jnet.protocol.api.meta.template.impl.ResourceLocator;

/**
 * Test class for validating template loading and parsing.
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class TemplateTest {

	private static void dumpItem(Item item, String indent) {
		if (item == null)
			return;

		System.out.printf("%sItem [%s]%n", indent, item.name());
		System.out.printf("%s  template: %s%n", indent, item.template());
		System.out.printf("%s  defaults: %s%n", indent, item.defaults());

		if (item.items() != null) {
			for (Item child : item.items()) {
				dumpItem(child, indent + "    ");
			}
		}
	}

	private static void dumpField(Item field, String indent) {
		if (field == null)
			return;

		System.out.printf("%sField [%s]%n", indent, field.name());
		System.out.printf("%s  label: %s%n", indent, field.label());
		System.out.printf("%s  template: %s%n", indent, field.template());
		System.out.printf("%s  defaults: %s%n", indent, field.defaults());

		if (field.items() != null) {
			for (Item item : field.items()) {
				dumpItem(item, indent + "    ");
			}
		}
	}

	private static void dumpDetailTemplate(TemplateDetail dt, String indent) {
		if (dt == null)
			return;

		System.out.printf("%sDetail [%s]%n", indent, dt.detail());
		System.out.printf("%s  summary: %s%n", indent, dt.summary());
		System.out.printf("%s  defaults: %s%n", indent, dt.defaults());
		System.out.printf("%s  fields:%n", indent);

		for (var field : dt.items()) {
			dumpField(field, indent + "    ");
		}
		System.out.println();
	}

	public static void main(String[] args) throws IOException, NotFound {
		String RESOURCE = "/meta/tcpip/ip4.yaml";
		ResourceLocator reader = new ResourceLocator(TemplateTest.class::getResourceAsStream);

		System.out.println(TemplateTest.class.getResource(RESOURCE));

		Template template = reader.resolveTemplate(RESOURCE);
		System.out.printf("Template [%s]%n", template.name());
		System.out.printf("  Macros: %s%n", template.macros());
		System.out.printf("  Defaults: %s%n%n", template.defaults());

		for (Detail detail : Detail.values()) {
			TemplateDetail dt = template.templateDetail(detail);
			if (dt != null) {
				dumpDetailTemplate(dt, "");
			}
		}
	}
}