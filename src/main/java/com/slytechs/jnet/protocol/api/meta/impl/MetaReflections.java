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
package com.slytechs.jnet.protocol.api.meta.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.slytechs.jnet.platform.api.util.Reflections;
import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.api.meta.MetaAttribute;
import com.slytechs.jnet.protocol.api.meta.MetaField;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.DetailTemplate;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.FieldTemplate;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaMacros;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.ProtocolTemplate;
import com.slytechs.jnet.protocol.api.meta.MetaValue;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public final class MetaReflections {

	private static MetaAttribute buildAttribute(Method method) {
		Meta meta = method.getAnnotation(Meta.class);
		String name = meta.name().isBlank()
				? method.getName()
				: meta.name();

		try {
			var value = new MetaValue(name, method);
			return new MetaAttribute(name, value);

		} catch (IllegalAccessException e) {
			e.printStackTrace();

			throw new RuntimeException(e);
		}

	}

	private static MetaField buildField(Method method, ProtocolTemplate template) {
		Meta meta = method.getAnnotation(Meta.class);
		String name = meta.name().isBlank()
				? method.getName()
				: meta.name();

		MetaMacros macros = template.metaMacros();

		var fieldTemplateArray = Arrays.stream(Detail.values())
				.map(detail -> template.detail(detail))
				.map(detailTemplate -> buildFieldTemplate(name, detailTemplate, macros))
				.toArray(FieldTemplate[]::new);

		try {
			var value = new MetaValue(name, method);
			return new MetaField(name, value, fieldTemplateArray);

		} catch (IllegalAccessException e) {
			e.printStackTrace();

			throw new RuntimeException(e);
		}

	}

	private static FieldTemplate buildFieldTemplate(String name, DetailTemplate detailTemplate, MetaMacros macros) {
		FieldTemplate field = detailTemplate == null ? null : detailTemplate.fieldMap().get(name);
		if (field == null)
			return null;

		MetaPattern pattern = MetaPattern.compile(field.template(), macros);

		return new FieldTemplate(

				field.name(),
				field.label(),
				field.template(),
				field.width(),
				pattern

		);
	}

	public static List<MetaAttribute> listAttributes(Class<?> containerClass) {
		var list = new ArrayList<MetaAttribute>();

		var attributeMethods = Reflections.listMethods(containerClass, Meta.class,
				a -> a.value() == MetaType.ATTRIBUTE);

		attributeMethods.stream()
				.map(MetaReflections::buildAttribute)
				.forEach(list::add);

		return list;
	}

	public static List<MetaField> listFields(Class<?> containerClass, ProtocolTemplate template) {
		var list = new ArrayList<MetaField>();

		var attributeMethods = Reflections.listMethods(containerClass, Meta.class, a -> a
				.value() == MetaType.FIELD);

		attributeMethods.stream()
				.map(method -> buildField(method, template))
				.forEach(list::add);

		return list;
	}

	private MetaReflections() {}

}
