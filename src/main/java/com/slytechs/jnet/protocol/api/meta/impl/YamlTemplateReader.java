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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.DetailTemplate;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.FieldTemplate;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaMacros;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.ProtocolTemplate;

public class YamlTemplateReader implements TemplateReader {
	private static final Logger logger = LoggerFactory.getLogger(YamlTemplateReader.class.getSimpleName());

	/**
	 * Pre-process reader input stream to replace single `\{` with double `\\{` YAML
	 * approved sequence before YAML parser. This is why all of the YAML template
	 * resource files can use a single `\{value}` in a YAML file.
	 */
	static class EscapeSequenceReader extends FilterReader {
		private int lastChar = -1;
		private int backslashCount = 0;

		public EscapeSequenceReader(Reader in) {
			super(in);
		}

		@Override
		public int read() throws IOException {
			if (backslashCount > 0) {
				backslashCount--;
				return '\\';
			}

			if (lastChar == -1) {
				lastChar = super.read();
			}

			if (lastChar == '\\') {
				int next = super.read();
				if (next == '{') {
					lastChar = next;
					backslashCount = 1; // Queue up one more backslash
					return '\\'; // Return first backslash
				}
				int temp = lastChar;
				lastChar = next;
				return temp;
			}

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
					return count == 0 ? -1 : count;
				}
				cbuf[off + i] = (char) c;
				count++;
			}
			return count;
		}

		public static void main(String[] args) throws IOException {
			String[] tests = {
					"Hello \\{world}",
					"\\{test} \\{test2}",
					"No replacement needed",
					"Multiple \\\\ backslashes \\{test}",
					"template: \"\\{value} (\\{portName})\"",
					""
			};

			for (String test : tests) {
				System.out.println("\nTest input: " + test);
				Reader reader = new EscapeSequenceReader(new StringReader(test));
				StringBuilder result = new StringBuilder();
				int c;
				while ((c = reader.read()) != -1) {
					result.append((char) c);
				}
				System.out.println("Result: " + result);
				System.out.println("Expected: " + test.replace("\\{", "\\\\{"));
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
		reader = new EscapeSequenceReader(reader);

		Yaml yaml = new Yaml();
		Map<String, Object> root = yaml.load(reader);

		return parseHeaderFromMap(root);
	}

	@Override
	public Map<String, ProtocolTemplate> parseAllHeaders(Reader reader) {
		reader = new EscapeSequenceReader(reader);

		Yaml yaml = new Yaml();
		Map<String, Object> root = yaml.load(reader);
		return parseAllHeadersFromMap(root);
	}

	@Override
	public ProtocolTemplate parseHeader(String yamlContent) {
		yamlContent = yamlContent.replace("\\{", "\\\\{");

		Yaml yaml = new Yaml();
		Map<String, Object> root = yaml.load(yamlContent);
		return parseHeaderFromMap(root);
	}

	@SuppressWarnings("unchecked")
	private Map<String, ProtocolTemplate> parseAllHeadersFromMap(Map<String, Object> root) {
		var map = new HashMap<String, ProtocolTemplate>();

		for (Map.Entry<String, Object> protocolEntry : root.entrySet()) {
			String protocolName = protocolEntry.getKey();
			Map<String, Object> protocol = (Map<String, Object>) protocolEntry.getValue();

			var hdr = parseHeaderFromMap(protocol, protocolName);
			map.put(protocolName, hdr);
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	private ProtocolTemplate parseHeaderFromMap(Map<String, Object> root) {
		Map.Entry<String, Object> protocolEntry = root.entrySet().iterator().next();
		String protocolName = protocolEntry.getKey();
		Map<String, Object> protocol = (Map<String, Object>) protocolEntry.getValue();

		return parseHeaderFromMap(protocol, protocolName);
	}

	@SuppressWarnings("unchecked")
	private ProtocolTemplate parseHeaderFromMap(Map<String, Object> protocol, String protocolName) {
		Map<String, Object> defaults = (Map<String, Object>) protocol.get("defaults");
		int defaultWidth = ((Number) defaults.getOrDefault("width", 50)).intValue();

		Map<String, String> macroMap = (Map<String, String>) protocol.get("macros");
		MetaMacros metaMacros = new MetaMacros(macroMap == null ? Map.of() : macroMap);

		// Parse meta section
		Map<String, String> meta = new HashMap<>();
		if (protocol.containsKey("meta")) {
			Map<String, Object> metaMap = (Map<String, Object>) protocol.get("meta");
			metaMap.forEach((k, v) -> meta.put(k, String.valueOf(v)));
		}

		// Parse templates with inheritance
		Map<String, Object> templates = (Map<String, Object>) protocol.get("templates");
		Map<Detail, DetailTemplate> details = parseDetailTemplates(templates, defaultWidth, metaMacros);

		return new ProtocolTemplate(protocolName, details, metaMacros, meta);
	}

	@SuppressWarnings("unchecked")
	private Map<Detail, DetailTemplate> parseDetailTemplates(Map<String, Object> templates, int defaultWidth,
			MetaMacros macros) {
		Map<Detail, DetailTemplate> details = new EnumMap<>(Detail.class);

		// First pass: parse explicitly defined templates
		for (Map.Entry<String, Object> entry : templates.entrySet()) {
			Detail detail = Detail.valueOf(entry.getKey());
			Map<String, Object> template = (Map<String, Object>) entry.getValue();

			String summary = (String) template.get("summary");
			MetaPattern pattern = MetaPattern.compile(summary, macros);

			List<FieldTemplate> fields = new ArrayList<>();

			if (template.containsKey("fields")) {
				Object fieldsObj = template.get("fields");
				fields = parseFields(fieldsObj, defaultWidth);
			}

			details.put(detail, new DetailTemplate(summary, fields, pattern));
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

	@SuppressWarnings("unchecked")
	private List<FieldTemplate> parseFields(Object fieldsObj, int defaultWidth) {
		List<FieldTemplate> fields = new ArrayList<>();

		if (fieldsObj instanceof List) {
			List<Map<String, Object>> fieldsList = (List<Map<String, Object>>) fieldsObj;
			for (Map<String, Object> field : fieldsList) {
				fields.add(createFieldTemplate(field, defaultWidth));
			}
		} else if (fieldsObj instanceof Map) {
			Map<String, Map<String, Object>> fieldsMap = (Map<String, Map<String, Object>>) fieldsObj;
			for (Map.Entry<String, Map<String, Object>> field : fieldsMap.entrySet()) {
				Map<String, Object> fieldProps = new HashMap<>(field.getValue());
				fieldProps.put("name", field.getKey());
				fields.add(createFieldTemplate(fieldProps, defaultWidth));
			}
		}

		return fields;
	}

	private FieldTemplate createFieldTemplate(Map<String, Object> field, int defaultWidth) {
		return new FieldTemplate(
				(String) field.get("name"),
				(String) field.get("label"),
				(String) field.get("template"),
				((Number) field.getOrDefault("width", defaultWidth)).intValue());
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
					System.out.printf("      Width: %d%n", field.width());
				}
			}
		}
	}
}
