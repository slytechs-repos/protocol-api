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

import java.util.List;
import java.util.regex.Pattern;

import com.slytechs.jnet.platform.api.domain.DomainAccessor;
import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.common.Packet;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.DetailTemplate;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.ProtocolTemplate;

/**
 * The Class MetaPacket.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public record MetaPacket(
		MetaBuilder builder,
		Packet packet,
		List<MetaHeader> headers,
		List<MetaAttribute> attributes,
		ProtocolTemplate template)
		implements MetaElement, DomainAccessor {

	/**
	 * Private constructor which initializes with constant non-changing properties
	 * of the packet and is used as a factory with the {@link #bindTo(Packet)}
	 * method.
	 *
	 * @param builder    the builder
	 * @param attributes the attributes
	 * @param template   the template
	 */
	MetaPacket(MetaBuilder builder, List<MetaAttribute> attributes, ProtocolTemplate template) {
		this(builder, null, null, attributes, template);
	}

	public DetailTemplate template(Detail detail) {
		return template.detail(detail);
	}

	public DetailTemplate templateOrThrow(Detail detail) throws IllegalStateException {
		DetailTemplate d = (template == null) ? null : template.detail(detail);
		if (d == null)
			throw new IllegalStateException("missing meta template for packet");

		return d;
	}

	/**
	 * Binds a packet to a copy of this meta object.
	 *
	 * @param packet the packet
	 * @return a new meta packet object initialized with packet's current headers
	 *         and all of the constant properties.
	 */
	public MetaPacket bindTo(Packet packet) {

		var newAttributes = attributes.stream()
				.map(a -> a.bindTo(packet))
				.toList();

		List<MetaHeader> newHeaders = builder.listHeaders(packet);

		var meta = new MetaPacket(builder, packet, newHeaders, newAttributes, template);

		newAttributes.forEach(att -> att.parent().setParent(meta));
		newHeaders.forEach(hdr -> hdr.parent().setParent(meta));

		return meta;
	}

	@Override
	public boolean isEmpty() {
		return headers.isEmpty();
	}

	private static final Pattern HEADER_ARRAY = Pattern.compile("header\\[(\\d+)\\]");

	/**
	 * @see com.slytechs.jnet.platform.api.domain.DomainAccessor#resolve(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public Object resolve(String name, Object ctx) {

//		System.out.printf("MetaPacket::resolve name=%s, ctx=%s%n", name, (ctx instanceof Named n) ? n.name() : ctx);

		if (ctx instanceof MetaField field) {
			if (name.equals("value") || name.equals(field.name()))
				return field.get();

			return resolve(name, field.parent().parent());
		}

		if (ctx instanceof MetaHeader header) {
			var res = header.getField(name);
			if (res instanceof MetaField field2)
				return field2.get();

			var metAtt = header.getAttribute(name);
			if (metAtt instanceof MetaAttribute att)
				return att.get();

			if (name.indexOf('[') != -1) {
				var matcher = HEADER_ARRAY.matcher(name);
				if (matcher.find()) {
					int index = Integer.parseInt(matcher.group(1));

					return header.header().buffer().get(index);
				}
			}

			return resolve(name, header.parent().parent());
		}

		if (ctx instanceof MetaPacket packet) {
			var metAtt = packet.getAttribute(name);
			if (metAtt instanceof MetaAttribute att)
				return att.get();

		}

		return "\\{%s}".formatted(name);
	}

	/**
	 * @param name
	 * @return
	 */
	public MetaAttribute getAttribute(String name) {
		return attributes.stream()
				.filter(att -> att.name().equalsIgnoreCase(name))
				.findAny()
				.orElse(null);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.MetaElement#parent()
	 */
	@Override
	public MetaElement parent() {
		return null;
	}

}
