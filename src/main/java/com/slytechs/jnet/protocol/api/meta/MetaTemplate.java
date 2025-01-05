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
package com.slytechs.jnet.protocol.api.meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.impl.DefaultMetaPattern;

public interface MetaTemplate {

	char MACRO_PREFIX_CHAR = '$';
	String MACRO_PREFIX = "" + MACRO_PREFIX_CHAR;

	interface MetaPattern {
		public interface Arg {

			default boolean isEmpty() {
				return expression().isEmpty();
			}

			default boolean startsWith(String str) {
				return expression().startsWith(str);
			}

			boolean isFormatPresent();

			String expression();

			String referenceName();

			String formatName();

			default String applyFormat(Object value) {
				return null;
			}

			default String applyFormatOrElse(Object value, FormatRegistry registry) {
				var formattedValue = applyFormat(value);
				if (formattedValue == null) {
//					System.out.println("MetaTemplate::Arg::apply trying global registry \\{" + expression() + "}");

					return registry.applyFormat(value, formatName());
				}

				return formattedValue;
			}
		}

		static MetaPattern compile(String template, MetaMacros metaMacros) {
			return new DefaultMetaPattern(template, metaMacros);
		}

		String[] fragments();

		Arg[] args();
	}

	record MetaMacros(Map<String, String> macroMap) {

		public static boolean isMacro(String name) {
			return name.startsWith(MACRO_PREFIX);
		}

		public boolean isMacroPresent(String name) {
			return name != null && isMacro(name) && macroMap.containsKey(name);
		}

		public String resolveIfPresent(String name) {
			if (!isMacro(name))
				return name;

			return macroMap.getOrDefault(name, name);
		}
	}

	record ProtocolTemplate(
			String name,
			Map<Detail, DetailTemplate> detailMap,
			DetailTemplate[] detailArray,
			List<DetailTemplate> detailList,
			MetaMacros metaMacros,
			Map<String, String> meta) implements MetaTemplate {

		private static DetailTemplate[] mapToArray(Map<Detail, DetailTemplate> detailMap) {
			return Arrays.stream(Detail.values())
					.map(detailMap::get)
					.toArray(DetailTemplate[]::new);
		}

		private static List<DetailTemplate> mapToList(Map<Detail, DetailTemplate> detailMap) {
			var list = Arrays.stream(Detail.values())
					.map(detailMap::get)
					.toList();

			return Collections.unmodifiableList(list);
		}

		public ProtocolTemplate(String name,
				Map<Detail, MetaTemplate.DetailTemplate> detailMap,
				MetaMacros metaMacros,
				Map<String, String> meta) {
			this(name, detailMap, mapToArray(detailMap), mapToList(detailMap), metaMacros, meta);

		}

		public DetailTemplate detail(Detail detail) {
			return detailArray[detail.ordinal()];
		}

	}

	record DetailTemplate(String summary, List<FieldTemplate> fieldList, Map<String, FieldTemplate> fieldMap,
			MetaPattern pattern) {

		private static Map<String, FieldTemplate> listToMap(List<FieldTemplate> fieldList) {
			var map = fieldList.stream().collect(Collectors.toMap(t -> t.name(), t -> t));

			return Collections.unmodifiableMap(map);
		}

		public DetailTemplate(
				String summary,
				List<FieldTemplate> fieldList,
				MetaPattern pattern) {
			this(summary, fieldList, listToMap(fieldList), pattern);
		}

	}

	record FieldTemplate(String name, String label, String template, int width, MetaPattern pattern) {
		public FieldTemplate(String name, String label, String template, int width) {
			this(name, label, template, width, null);
		}
	}

}