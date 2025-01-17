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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.slytechs.jnet.platform.api.util.Reflections;
import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.api.meta.MetaAttribute;
import com.slytechs.jnet.protocol.api.meta.MetaField;
import com.slytechs.jnet.protocol.api.meta.MetaValue;
import com.slytechs.jnet.protocol.api.meta.template.DetailTemplate;
import com.slytechs.jnet.protocol.api.meta.template.FieldTemplate;
import com.slytechs.jnet.protocol.api.meta.template.Macros;
import com.slytechs.jnet.protocol.api.meta.template.Template.HeaderTemplate;

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

	private static MetaField buildField(Method method, HeaderTemplate headerTemplate) {
		Meta meta = method.getAnnotation(Meta.class);
		String name = meta.name().isBlank()
				? method.getName()
				: meta.name();

		Macros macros = headerTemplate.macros();

		var fieldTemplateArray = Arrays.stream(Detail.values())
				.map(detail -> headerTemplate.detail(detail))
				.map(detailTemplate -> buildFieldTemplate(name, detailTemplate, macros))
				.toArray(FieldTemplate[]::new);

		var childMetaFieldMap = new HashMap<Detail, MetaField[]>();

//		Arrays.stream(Detail.values())
//				.filter(detail -> fieldTemplateArray[detail.ordinal()].children() != null)
//				.map(detail -> {
//					fieldTemplateArray[detail.ordinal()].children().stream()
//						.map(null)
//				});

		try {
			var value = new MetaValue(name, method);
			return new MetaField(name, value, fieldTemplateArray, childMetaFieldMap);

		} catch (IllegalAccessException e) {
			e.printStackTrace();

			throw new RuntimeException(e);
		}

	}

	private static FieldTemplate buildFieldTemplate(String name, DetailTemplate detailTemplate, Macros macros) {
		Map<String, FieldTemplate> map = detailTemplate.fieldMap();
		FieldTemplate field = detailTemplate == null ? null : map.get(name);
		if (field == null)
			return null;

		return new FieldTemplate(
				field.detail(),
				field.name(),
				field.label(),
				field.template(),
				field.defaults(),
				field.items()

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

	public static List<MetaField> listFields(Class<?> containerClass, HeaderTemplate headerTemplate) {
		var list = new ArrayList<MetaField>();

		var fieldMethods = Reflections.listMethods(containerClass, Meta.class, a -> a
				.value() == MetaType.FIELD);

		fieldMethods.stream()
				.map(method -> buildField(method, headerTemplate))
				.forEach(list::add);

		return list;
	}

	private MetaReflections() {}

}
