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

import java.util.List;
import java.util.stream.Stream;

import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.common.Packet;
import com.slytechs.jnet.protocol.api.meta.template.DetailTemplate;
import com.slytechs.jnet.protocol.api.meta.template.HeaderTemplate;

/**
 * The Class MetaHeader.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public record MetaHeader(
		MetaParent parent,
		Header header,
		List<MetaHeader> subHeaders,
		List<MetaField> fields,
		List<MetaAttribute> attributes,
		HeaderTemplate headerTemplate)
		implements MetaElement {

	/**
	 * Private constructor which initializes with global non-changing properties of
	 * the header and is used as a factory with the {@link #bindTo(Header)} method.
	 * 
	 * @param header
	 * @param fields
	 * @param attributes
	 * @param headerTemplate
	 */
	public MetaHeader(Header header, List<MetaField> fields, List<MetaAttribute> attributes, HeaderTemplate headerTemplate) {
		this(new MetaParent(), header, List.of(), fields, attributes, headerTemplate);
	}

	/**
	 * @param string
	 * @return
	 */
	public MetaField getField(String name) {
		return fields.stream()
				.filter(fld -> fld.name().equalsIgnoreCase(name))
				.findAny()
				.orElse(null);
	}

	public MetaAttribute getAttribute(String name) {
		return attributes.stream()
				.filter(att -> att.name().equalsIgnoreCase(name))
				.findAny()
				.orElse(null);
	}

	public String name() {
		return header.headerName();
	}

	public List<MetaField> fields(Detail detail) {
		return fieldsStream(detail)
				.toList();

	}

	public Stream<MetaField> fieldsStream(Detail detail) {

		var temp = headerTemplate.detail(detail)
				.fieldList();

		return temp.entries()
				.map(ft -> getField(ft.name()));
	}

	public Iterable<MetaField> fieldsIterable(Detail detail) {

		var it = fieldsStream(detail)
				.filter(f -> f.isPresent(detail))
				.iterator();

		return () -> it;
	}

	public DetailTemplate template(Detail detail) {
		return headerTemplate.detail(detail);
	}

	public DetailTemplate templateOrThrow(Detail detail) throws IllegalStateException {
		DetailTemplate d = (headerTemplate == null) ? null : headerTemplate.detail(detail);
		if (d == null)
			throw new IllegalStateException("missing meta header headerTemplate [%s]".formatted(name()));

		return d;
	}

	/**
	 * Binds a packet to a copy of this meta object, reusing the existing protocol
	 * header backing this meta object.
	 *
	 * @param packet the packet
	 * @return a new meta header object initialized with packet's current headers
	 *         and all of the constant properties.
	 */
	public MetaHeader bindTo(Packet packet) {
		if (packet.peekHeader(this.header) == null)
			throw new IllegalStateException("can not bind this header [%s]to packet".formatted(header.headerName()));

		var newFields = fields.stream()
				.map(f -> f.bindTo(header))
				.toList();

		var newAttributes = attributes.stream()
				.map(a -> a.bindTo(header))
				.toList();

		var meta = new MetaHeader(new MetaParent(), header, subHeaders, newFields, newAttributes, headerTemplate);

		newFields.forEach(fld -> fld.parent().setParent(meta));
		newAttributes.forEach(att -> att.parent().setParent(meta));

		return meta;
	}

	/**
	 * Binds a new protocol header to a copy of this meta object. The new protocol
	 * header must of the same class as the one currently backing this meta object.
	 * 
	 * <p>
	 * Note: if a competely different header was bound to this meta object, the meta
	 * headerTemplate data would no longer match the new header.
	 * </p>
	 *
	 * @param header the header
	 * @return a new meta header object initialized with packet's current headers
	 *         and all of the constant properties.
	 */
	public MetaHeader bindTo(Header header) {

		var newFields = fields.stream()
				.map(f -> f.bindTo(header))
				.toList();

		var newAttributes = attributes.stream()
				.map(a -> a.bindTo(header))
				.toList();

		var meta = new MetaHeader(new MetaParent(), header, subHeaders, newFields, newAttributes, headerTemplate);

		newFields.forEach(fld -> fld.parent().setParent(meta));
		newAttributes.forEach(att -> att.parent().setParent(meta));

		return meta;
	}

	@Override
	public boolean isEmpty() {

//		System.out.println("MetaHeader::%s::isEmpty fields.isEmpty=%s".formatted(name(), fields.isEmpty()));
//		System.out.println("MetaHeader::%s::isEmpty fields.size=%s".formatted(name(), fields.size()));
		return fields.size() < 3;
	}
}
