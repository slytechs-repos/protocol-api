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

import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.Defaults;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.DetailTemplate;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.FieldTemplate;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.Macros;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.Node;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.ProtocolTemplate;

public class YamlTemplateReader implements TemplateReader {

	private static final String DEFAULTS_KEYWORD = "defaults";
	private static final String FIELDS_KEYWORD = "fields";
	private static final String GROUP_KEYWORD = "group";
	private static final String MACROS_KEYWORD = "macros";
	private static final String TEMPLATE_KEYWORD = "template";
	private static final String TEMPLATES_KEYWORD = "templates";
	private static final String NAME_KEYWORD = "name";
	private static final String LABEL_KEYWORD = "label";
	private static final String SUMMARY_KEYWORD = "summary";
	private static final String HEADERS_KEYWORD = "headers";

	private Defaults defaults = Defaults.root();
	private Macros defaultMacros = Macros.root();

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.impl.TemplateReader#defaults()
	 */
	@Override
	public Defaults defaults() {
		return defaults;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.impl.TemplateReader#setDefaults(com.slytechs.jnet.protocol.api.meta.MetaTemplate.Defaults)
	 */
	@Override
	public void setDefaults(Defaults newDefaults) {
		this.defaults = newDefaults;
	}

	private static final Logger logger = LoggerFactory.getLogger(YamlTemplateReader.class.getSimpleName());

	/**
	 * Pre-process reader input stream to replace: 1) Single `\{` with double `\\{`
	 * (YAML-approved sequence). 2) Each tab `'\t'` with TAB_CHAR_WIDTH spaces.
	 */
	static class YamlPreprocessorReader extends FilterReader {
		public static final int TAB_CHAR_WIDTH = 4;

		private int lastChar = -1;
		private int backslashCount = 0;
		private int spaceCount = 0; // how many ' ' spaces remain to output after a tab

		public YamlPreprocessorReader(Reader in) {
			super(in);
		}

		@Override
		public int read() throws IOException {
			// 1) If we owe spaces from a recent tab, return a space
			if (spaceCount > 0) {
				spaceCount--;
				return ' ';
			}

			// 2) If we owe an extra backslash, return it
			if (backslashCount > 0) {
				backslashCount--;
				return '\\';
			}

			// 3) If lastChar is "uninitialized", read from underlying
			if (lastChar == -1) {
				lastChar = super.read();
			}

			// 4) Check for tab expansion
			if (lastChar == '\t') {
				// We will convert this tab into TAB_CHAR_WIDTH spaces in total
				spaceCount = TAB_CHAR_WIDTH - 1; // we'll return one space now
				lastChar = -1; // sentinel to avoid re-processing this char
				return ' ';
			}

			// 5) Check for a backslash + '{' sequence that needs doubling
			if (lastChar == '\\') {
				int next = super.read();
				if (next == '{') {
					// We want to convert "\{" into "\\{"
					// so we queue up one more backslash to output.
					lastChar = next; // so next iteration sees '{'
					backslashCount = 1; // one more backslash to insert
					return '\\'; // return the first backslash now
				}
				// Otherwise, not "\{", so just return the backslash
				int temp = lastChar;
				lastChar = next; // keep reading
				return temp;
			}

			// 6) Normal character path
			int temp = lastChar;
			lastChar = super.read();
			return temp;
		}

		@Override
		public int read(char[] cbuf, int off, int len) throws IOException {
			int count = 0;
			for (int i = 0; i < len; i++) {
				int c = read();
				if (c == -1) {
					return (count == 0) ? -1 : count;
				}
				cbuf[off + i] = (char) c;
				count++;
			}
			return count;
		}

		public static void main(String[] args) throws IOException {
			// Demo: a few test strings including tabs and "\{" combos
			String[] tests = {
					"Hello \\{world}",
					"\\{test} \\{test2}",
					"No replacement needed\t(tab here)",
					"\tLeading tab and backslash \\{test}",
					"template:\t\"\\{value} (\\{portName})\"",
					""
			};

			for (String test : tests) {
				System.out.println("\nTest input:    " + test.replace("\t", "\\t"));
				Reader reader = new YamlPreprocessorReader(new StringReader(test));
				StringBuilder result = new StringBuilder();
				int c;
				while ((c = reader.read()) != -1) {
					result.append((char) c);
				}
				// Show result with literal tabs replaced for clarity
				System.out.println("Result:        " + result.toString()
						.replace("\t", "\\t"));
				// Show what we'd expect from doubling "\{"
				String expectedDoubleBackslash = test.replace("\\{", "\\\\{")
						.replace("\t", "→");
				System.out.println("Double \\{:    " + expectedDoubleBackslash);
			}
		}
	}

	private static final List<Detail> DETAIL_HIERARCHY = List.of(
			Detail.HEXDUMP,
			Detail.DEBUG,
			Detail.HIGH,
			Detail.MEDIUM,
			Detail.SUMMARY,
			Detail.OFF);

	@Override
	public ProtocolTemplate parseResource(String resourcePath) throws IOException {

		try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
			if (is == null) {
				throw new IOException("Resource not found: " + resourcePath);
			}

			try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
				return parseHeader(reader);
			}
		} catch (Throwable e) {
			logger.error("error reading resource {}", getClass().getResource(resourcePath));
			throw e;
		}
	}

	@Override
	public Map<String, ProtocolTemplate> parseAllResources(String resourcePath) throws IOException {
		try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
			if (is == null) {
				throw new IOException("Resource not found: " + resourcePath);
			}

			try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
				return parseAllHeaders(reader);
			}
		} catch (Throwable e) {
			logger.error("error reading resource {}", getClass().getResource(resourcePath));
			throw e;
		}
	}

	@Override
	public ProtocolTemplate parseHeader(Reader reader) throws IOException {
		reader = new YamlPreprocessorReader(reader);

		Yaml yaml = new Yaml();
		Map<String, Object> root = yaml.load(reader);

		return parseProtocolFromMap(root);
	}

	@Override
	public Map<String, ProtocolTemplate> parseAllHeaders(Reader reader) {
		reader = new YamlPreprocessorReader(reader);

		Yaml yaml = new Yaml();
		Map<String, Object> root = yaml.load(reader);
		return parseAllProtocolsFromMap(root);
	}

	@Override
	public ProtocolTemplate parseHeader(String yamlContent) {
		yamlContent = yamlContent.replace("\\{", "\\\\{");

		Yaml yaml = new Yaml();
		Map<String, Object> root = yaml.load(yamlContent);
		return parseProtocolFromMap(root);
	}

	@SuppressWarnings("unchecked")
	private Map<String, ProtocolTemplate> parseAllProtocolsFromMap(Map<String, Object> root) {
		var map = new HashMap<String, ProtocolTemplate>();

		for (Map.Entry<String, Object> protocolEntry : root.entrySet()) {
			String protocolName = protocolEntry.getKey();
			Map<String, Object> protocol = (Map<String, Object>) protocolEntry.getValue();

			var hdr = parseProtocolFromMap(protocol, protocolName);
			map.put(protocolName, hdr);
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	private ProtocolTemplate parseProtocolFromMap(Map<String, Object> root) {
		Map.Entry<String, Object> protocolEntry = root.entrySet().iterator().next();
		String protocolName = protocolEntry.getKey();
		Map<String, Object> protocol = (Map<String, Object>) protocolEntry.getValue();

		return parseProtocolFromMap(protocol, protocolName);
	}

	@SuppressWarnings("unchecked")
	private ProtocolTemplate parseProtocolFromMap(Map<String, Object> protocol, String protocolName) {
		Defaults defaults = Defaults.fromMap(this.defaults, (Map<String, Object>) protocol.get(
				DEFAULTS_KEYWORD));

		Map<String, String> macroMap = (Map<String, String>) protocol.get(MACROS_KEYWORD);
		Macros macros = new Macros(this.defaultMacros, macroMap);

		// Parse templates with inheritance
		Map<String, Object> templates = (Map<String, Object>) protocol.get(TEMPLATES_KEYWORD);
		Map<Detail, DetailTemplate> details = parseDetailTemplates(templates, defaults, macros);

		return new ProtocolTemplate(protocolName, details, macros, defaults);
	}

	@SuppressWarnings("unchecked")
	private Map<Detail, DetailTemplate> parseDetailTemplates(Map<String, Object> templates, Defaults defaults,
			Macros macros) {

		// Chain new Defaults if present, otherwise the passed in parent Defaults are
		// returned
		defaults = Defaults.fromMap(defaults, (Map<String, Object>) templates.get(DEFAULTS_KEYWORD));

		Map<Detail, DetailTemplate> details = new EnumMap<>(Detail.class);

		// First pass: parse explicitly defined templates
		for (Map.Entry<String, Object> entry : templates.entrySet()) {
			Detail detail = Detail.valueOf(entry.getKey());
			Map<String, Object> template = (Map<String, Object>) entry.getValue();

			String summary = (String) template.get(SUMMARY_KEYWORD);
			MetaPattern pattern = MetaPattern.compile(summary, macros);

			List<FieldTemplate> fields = new ArrayList<>();

			if (template.containsKey(FIELDS_KEYWORD)) {
				Object fieldsObj = template.get(FIELDS_KEYWORD);
				fields = parseFields(detail, fieldsObj, defaults);
			}

			details.put(detail, new DetailTemplate(detail, summary, fields, defaults, pattern));
		}

		// Second pass: apply inheritance
		for (Detail detail : DETAIL_HIERARCHY) {
			if (!details.containsKey(detail)) {
				DetailTemplate inherited = findInheritedTemplate(detail, details);
				if (inherited != null) {
					details.put(detail, inherited);
				}
			}
		}

		// Third pass: propagate last to missing from lowest to highest order
		DetailTemplate last = null;
		for (Detail detail : DETAIL_HIERARCHY.reversed()) {
			if (!details.containsKey(detail)) {
				details.put(detail, last);
			} else {
				last = details.get(detail);
			}
		}

		return details;
	}

	private DetailTemplate findInheritedTemplate(Detail detail, Map<Detail, DetailTemplate> details) {
		// Find the next higher detail level that has a template defined
		int currentIndex = DETAIL_HIERARCHY.indexOf(detail);
		for (int i = currentIndex - 1; i >= 0; i--) {
			Detail higherDetail = DETAIL_HIERARCHY.get(i);
			DetailTemplate template = details.get(higherDetail);
			if (template != null) {
				return template;
			}
		}
		return null;
	}

	private List<FieldTemplate> parseFields(Detail detail, Object fieldsObj, Defaults defaults) {
		return parseFields(detail, fieldsObj, null, defaults);
	}

	@SuppressWarnings("unchecked")
	private List<FieldTemplate> parseFields(
			Detail detail,
			Object fieldsObj,
			String nameFormatter,
			Defaults defaults) {

		List<FieldTemplate> fields = new ArrayList<>();

		if (fieldsObj instanceof List) {
			List<Map<String, Object>> fieldsList = (List<Map<String, Object>>) fieldsObj;

			int fieldIndex = 0;
			for (Map<String, Object> field : fieldsList) {
				defaults = Defaults.fromMap(defaults, (Map<String, Object>) field.get(DEFAULTS_KEYWORD));

				if (field.get(NAME_KEYWORD) == null)
					field.put(NAME_KEYWORD, nameFormatter.formatted(fieldIndex++));

				String name = (String) field.get(NAME_KEYWORD);

				List<Node> nodes = null;

				// Process child group of fields recursively
				if (field.containsKey(GROUP_KEYWORD)) {
					Object nodesObj = field.get(GROUP_KEYWORD);
					String nodeNameFormatter = name + "/node[%d]";

//					nodes = parseGroup(null, detail, nodesObj, nodeNameFormatter, defaults);

//					nodes.forEach(System.out::println);
				}

				fields.add(createFieldTemplate(detail, field, nodes, defaults));
			}

		}

		return fields;
	}

	private static class Helper {

		public final Map<String, Object> map;

		public Helper(Object yamlObj) {
			this.map = (Map<String, Object>) yamlObj;
		}

		public class Group extends Helper {

			public Group(Object yamlObj) {
				super(yamlObj);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<Node> parseGroup(Node parent, Detail detail, Helper.Group groupObj, String nameFormatter,
			Defaults defaults) {
		List<Map<String, Object>> nodesList = (List<Map<String, Object>>) groupObj;

		List<Node> nodes = new ArrayList<>(nodesList.size());

		int index = 0;
		for (var nodeMap : nodesList) {
			defaults = Defaults.fromMap(defaults, nodeMap.get(DEFAULTS_KEYWORD));
			String template = (String) nodeMap.get(TEMPLATE_KEYWORD);

			if (nodeMap.get(NAME_KEYWORD) == null)
				nodeMap.put(NAME_KEYWORD, nameFormatter.formatted(index++));

			String name = (String) nodeMap.get(NAME_KEYWORD);

			var children = new ArrayList<Node>();

			var node = new Node(parent, detail, name, template, defaults, null, children);

//			if (nodeMap.containsKey(GROUP_KEYWORD)) {
//				Object childNodeObj = nodeMap.get(GROUP_KEYWORD);
//				String childName = name + "/node[%d]";
//
//				var childNodeList = parseGroup(node, detail, childNodeObj, childName, defaults);
//				children.addAll(childNodeList);
//			}
//
			nodes.add(node);
		}

		return nodes;
	}

	private FieldTemplate createFieldTemplate(
			Detail detail,
			Map<String, Object> field,
			List<Node> nodes,
			Defaults defaults) {

		return new FieldTemplate(
				detail,
				Objects.requireNonNull((String) field.get(NAME_KEYWORD), "Must provide or inherit a field name"),
				(String) field.getOrDefault(LABEL_KEYWORD, ""),
				(String) field.get(TEMPLATE_KEYWORD),
				defaults,
				nodes);
	}

	public static void main(String[] args) {
		TemplateReader parser = new YamlTemplateReader();

		try {
			// Load and parse Ethernet template
			ProtocolTemplate ethernet = parser.parseResource("/tcpip/ip4.yaml");
			System.out.println("Loaded " + ethernet.name() + " template:");
			printTemplate(ethernet);

			System.out.println("\n" + "=".repeat(80) + "\n");

			// Load and parse TCP template
			ProtocolTemplate tcp = parser.parseResource("/tcpip/tcp.yaml");
			System.out.println("Loaded " + tcp.name() + " template:");
			printTemplate(tcp);

		} catch (IOException e) {
			System.err.println("Error loading templates: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void printTemplate(ProtocolTemplate header) {
		// Print each detail level
		for (Map.Entry<Detail, DetailTemplate> entry : header.detailMap().entrySet()) {
			Detail detail = entry.getKey();
			DetailTemplate template = entry.getValue();

			System.out.println("\nDetail Level: " + detail);
			System.out.println("Summary: " + template.summary());

			if (template.fieldList() != null && !template.fieldList().isEmpty()) {
				System.out.println("Fields:");
				for (FieldTemplate field : template.fieldList()) {
					System.out.printf("  - %s:%n", field.name());
					System.out.printf("      Label: %s%n", field.label());
					System.out.printf("      Template: %s%n", field.template());
					System.out.printf("      Width: %d%n", field.defaults().width());
				}
			}
		}
	}
}
