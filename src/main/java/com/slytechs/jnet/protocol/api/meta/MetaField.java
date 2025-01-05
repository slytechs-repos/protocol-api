/*
 * Sly Technologies Free License
 * 
 * Copyright 2023 Sly Technologies Inc.
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
import java.util.Objects;
import java.util.stream.Collectors;

import com.slytechs.jnet.platform.api.util.Named;
import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.FieldTemplate;

/**
 * The Class MetaField.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public record MetaField(
		MetaParent parent,
		String name,
		List<MetaAttribute> attributes,
		MetaValue value,
		Map<Detail, FieldTemplate> templateMap,
		FieldTemplate[] templateArray,
		List<FieldTemplate> templateList)
		implements MetaElement, Named {

	/**
	 * @param templateArray
	 * @return
	 */
	private static List<FieldTemplate> arrayToList(FieldTemplate[] templateArray) {
		var list = Arrays.asList(templateArray);

		return Collections.unmodifiableList(list);
	}

	/**
	 * @param templateArray2
	 * @return
	 */
	private static Map<Detail, FieldTemplate> arrayToMap(FieldTemplate[] templateArray) {
		var map = Arrays.stream(Detail.values())
				.filter(detail -> templateArray[detail.ordinal()] != null)
				.collect(Collectors.toMap(detail -> detail, detail -> templateArray[detail.ordinal()]));

		return Collections.unmodifiableMap(map);
	}

	public MetaField(String name, MetaValue value, FieldTemplate[] templateArray) {
		this(null, name, List.of(), value, arrayToMap(templateArray), templateArray, arrayToList(templateArray));
	}

	public boolean isPresent(Detail detail) {
		return templateArray[detail.ordinal()] != null;
	}

	public FieldTemplate template(Detail detail) {
		return templateArray[detail.ordinal()];
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaField other = (MetaField) obj;
		return Objects.equals(name, other.name);
	}

	public MetaField bindTo(Object target) {
		var newAttributes = attributes.stream()
				.map(a -> a.bindTo(target))
				.toList();

		return new MetaField(new MetaParent(), name, newAttributes, value.bindTo(target),
				templateMap,
				templateArray, templateList);
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get() {
		return (T) value.get();
	}

	/**
	 * @return
	 */
	public MetaHeader getParentHeader() {
		throw new UnsupportedOperationException("not implemented yet");
	}

}
