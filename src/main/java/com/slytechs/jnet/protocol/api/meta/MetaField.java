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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import com.slytechs.jnet.platform.api.util.Named;
import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.template.Item.FieldItem;

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
		FieldItem[] templateArray,
		Map<Detail, MetaField[]> children)
		implements MetaElement, Named {

	public MetaField(String name, MetaValue value, FieldItem[] templateArray, Map<Detail, MetaField[]> children) {
		this(null, name, List.of(), value, templateArray, children);
	}

	public boolean isPresent(Detail detail) {
		return templateArray[detail.ordinal()] != null;
	}

	public FieldItem template(Detail detail) {
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

		Map<Detail, MetaField[]> newChildren = children();

		if (!children.isEmpty()) {
			var tempMap = new HashMap<Detail, MetaField[]>(children().size());

			Stream.of(Detail.values())
					.filter(detail -> children.containsKey(detail))
					.forEach(detail -> {
						MetaField[] childrenAtDetail = children.get(detail);

						MetaField[] arr = Arrays.stream(childrenAtDetail)
								.map(m -> m.bindTo(target))
								.toArray(MetaField[]::new);

						tempMap.put(detail, arr);
					});

			newChildren = tempMap;
		}

		return new MetaField(new MetaParent(), name, newAttributes, value.bindTo(target), templateArray, newChildren);
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
