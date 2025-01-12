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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.Defaults;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.DetailTemplate;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.FieldTemplate;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.Item;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.Macros;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.PlaceholderPattern;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.Template;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.TemplatePattern;

/**
 * Reads and parses YAML protocol template definitions.
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class TemplateReader {

	private static final String SUMMARY_KEYWORD = "summary";
	private static final String FIELD_KEYWORD = "field";
	private static final String DETAIL_KEYWORD = "detail";
	private static final String PROTOCOLS_KEYWORD = "Templates";
	private static final String OVERVIEW_KEYWORD = "Overview";
	private static final String DEFAULTS_KEYWORD = "defaults";
	private static final String ITEMS_KEYWORDS = "items";
	private static final String TREE_KEYWORD = "tree";
	private static final String SUBTREE_KEYWORD = "subtree";
	private static final String MACROS_KEYWORD = "macros";
	private static final String TEMPLATE_KEYWORD = "template";
	private static final String DETAILS_KEYWORD = "details";
	private static final String NAME_KEYWORD = "name";
	private static final String LABEL_KEYWORD = "label";

	private static final List<Detail> DETAIL_HIERARCHY = List.of(
			Detail.HEXDUMP,
			Detail.DEBUG,
			Detail.HIGH,
			Detail.MEDIUM,
			Detail.SUMMARY,
			Detail.OFF);

	public static Template readProtocolTemplate(String protocol) throws IOException {
		String resourcePath = String.format("/meta/tcpip/%s.yaml", protocol);
		try (InputStream is = TemplateReader.class.getResourceAsStream(resourcePath)) {
			if (is == null)
				throw new IOException("Resource not found: " + resourcePath);

			return parseYamlTemplate(is);
		}
	}

	private static Macros rootMacros = Macros.root();
	private static Defaults rootDefaults = Defaults.root();

	@SuppressWarnings("unchecked")
	public static Template parseYamlTemplate(InputStream is) {
		Yaml yaml = new Yaml();
		Map<String, Object> root = yaml.load(is);

		List<Map<String, Object>> protocols = (List<Map<String, Object>>) root.get(PROTOCOLS_KEYWORD);
		Map<String, Object> protocol = protocols.get(0);
		String name = (String) protocol.get("header");
		// Get root level and protocol level macros and defaults
		Map<String, Object> overview = (Map<String, Object>) root.get(OVERVIEW_KEYWORD);

		Macros macros = Macros.fromContainer(rootMacros, overview);
		Defaults defaults = Defaults.fromContainer(rootDefaults, overview);

		Map<Detail, DetailTemplate> detailMap = parseDetails(
				(List<Map<String, Object>>) protocol.get(DETAILS_KEYWORD),
				macros,
				defaults);

		return new Template(name, detailMap, macros, defaults);
	}

	@SuppressWarnings("unchecked")
	private static Map<Detail, DetailTemplate> parseDetails(
			List<Map<String, Object>> detailsList,
			Macros macros,
			Defaults defaults) {

		Map<Detail, DetailTemplate> result = new EnumMap<>(Detail.class);
		Map<Detail, DetailTemplate> explicitDetails = new EnumMap<>(Detail.class);

		// First pass - load explicit details
		for (Map<String, Object> detailMap : detailsList) {
			String detailName = ((String) detailMap.get(DETAIL_KEYWORD)).toUpperCase();
			Detail detail = Detail.valueOf(detailName);

			String summaryStr = (String) detailMap.get(SUMMARY_KEYWORD);
			TemplatePattern summary = new TemplatePattern(summaryStr, macros);
			defaults = Defaults.fromMap(defaults, detailMap);

			List<FieldTemplate> fields = parseFields(
					(List<Map<String, Object>>) detailMap.getOrDefault(ITEMS_KEYWORDS,
							detailMap.getOrDefault(TREE_KEYWORD,
									detailMap.getOrDefault(SUBTREE_KEYWORD, null))),
					detail,
					macros,
					defaults);

			DetailTemplate dt = new DetailTemplate(detail, summary, fields, defaults);
			explicitDetails.put(detail, dt);
		}

		// Apply inheritance in correct order
		DetailTemplate previous = null;
		DetailTemplate highDetail = null;
		for (Detail detail : DETAIL_HIERARCHY) {
			DetailTemplate current = explicitDetails.get(detail);

			if (current != null) {
				result.put(detail, current);
				if (detail == Detail.HIGH) {
					highDetail = current;
				}
				previous = current;
			} else if (previous != null && detail != Detail.OFF) {
				DetailTemplate template = previous;

				// Use HIGH's fields for HEXDUMP and DEBUG
				if ((detail == Detail.HEXDUMP || detail == Detail.DEBUG) && highDetail != null) {
					template = new DetailTemplate(
							detail,
							previous.summary(),
							highDetail.fieldList(),
							previous.defaults());
				}

				result.put(detail, template);
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private static List<FieldTemplate> parseFields(
			List<Map<String, Object>> fieldsList,
			Detail detail,
			Macros macros,
			Defaults defaults) {

		if (fieldsList == null)
			return Collections.emptyList();

		List<FieldTemplate> fields = new ArrayList<>();
		for (Map<String, Object> fieldMap : fieldsList) {
			String name = (String) fieldMap.get(FIELD_KEYWORD);
			String label = (String) fieldMap.getOrDefault(LABEL_KEYWORD, "");
			String template = (String) fieldMap.getOrDefault(TEMPLATE_KEYWORD,
					fieldMap.getOrDefault(SUMMARY_KEYWORD, null));

			Map<String, Object> fieldDefaults = (Map<String, Object>) fieldMap.getOrDefault(DEFAULTS_KEYWORD,
					Collections.emptyMap());
			Defaults fieldDefaultsObj = Defaults.fromMap(defaults, fieldDefaults);

			List<Item> items = parseItems(
					(List<Map<String, Object>>) fieldMap.getOrDefault(ITEMS_KEYWORDS,
							fieldMap.getOrDefault(TREE_KEYWORD,
									fieldMap.getOrDefault(SUBTREE_KEYWORD, null))),
					null,
					detail,
					macros,
					fieldDefaultsObj);

			fields.add(new FieldTemplate(detail, name, label, template, fieldDefaultsObj, items));
		}

		return fields;
	}

	@SuppressWarnings("unchecked")
	private static List<Item> parseItems(
			List<Map<String, Object>> itemsList,
			Item parent,
			Detail detail,
			Macros macros,
			Defaults defaults) {

		if (itemsList == null)
			return Collections.emptyList();

		List<Item> items = new ArrayList<>();
		for (Map<String, Object> itemMap : itemsList) {
			String name = (String) itemMap.get("header"); // First try header
			if (name == null) {
				name = ""; // For subtree items without explicit names
			}

			String template = (String) itemMap.get(TEMPLATE_KEYWORD);
			if (template == null) {
				template = (String) itemMap.get(SUMMARY_KEYWORD);
			}

			Map<String, Object> itemDefaults = (Map<String, Object>) itemMap.getOrDefault(DEFAULTS_KEYWORD, Collections
					.emptyMap());
			Defaults itemDefaultsObj = Defaults.fromMap(defaults, itemDefaults);

			List<Item> children = parseItems(
					(List<Map<String, Object>>) itemMap.getOrDefault(ITEMS_KEYWORDS,
							itemMap.getOrDefault(TREE_KEYWORD,
									itemMap.getOrDefault(SUBTREE_KEYWORD, null))), // Check subtree key
					null,
					detail,
					macros,
					itemDefaultsObj);

			var pattern = PlaceholderPattern.compile(template, macros);

			Item item = new Item(parent, detail, name, template, itemDefaultsObj, pattern, children);
			items.add(item);
		}

		return items;
	}
}