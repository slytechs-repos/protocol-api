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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slytechs.jnet.platform.api.domain.DomainAccessor;
import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.platform.api.util.format.DetailFormatter;
import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.common.Packet;
import com.slytechs.jnet.protocol.api.meta.spi.HeaderTemplateService;
import com.slytechs.jnet.protocol.api.meta.spi.impl.CachedHeaderTemplateService;
import com.slytechs.jnet.protocol.api.meta.template.MetaTemplate.DetailTemplate;

/**
 * The Class PacketFormatter.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public final class PacketFormatter implements DetailFormatter {

	private static final Logger logger = LoggerFactory.getLogger(PacketFormatter.class);

	private final HeaderTemplateService templates = CachedHeaderTemplateService.CACHED.get();

	private Detail detail;

	private DomainAccessor domain = null;

	public PacketFormatter() {
		this(Detail.DEFAULT);
	}

	/**
	 * @param low
	 */
	public PacketFormatter(Detail detail) {
		this.detail = detail;
	}

	public void formatAttributeTo(Appendable out, MetaAttribute attribute, Detail detail) {

	}

	private void formatFieldTo(Appendable out, MetaField field, Detail detail)
			throws IOException {
//		printer(detail).appendField(detail, out, field, domain);
	}

	public void formatHeaderTo(Appendable out, Header header, Detail detail) throws IOException {

	}

	public void formatHeaderTo(Appendable out, MetaHeader header, Detail detail) throws IOException {
		DetailTemplate template = header.templateOrThrow(detail);

//		printer(detail).appendSummary(detail, out, header, template.compiledPattern(), domain);

		if (detail.ordinal() > Detail.SUMMARY.ordinal()) {
			for (var field : header.fieldsIterable(detail)) {
				assert field != null;

				System.out.println(field);

			}

		}

	}

	public void formatPacketTo(Appendable out, MetaPacket packet, Detail detail) throws IOException {
		this.domain = packet;

		DetailTemplate template = packet.templateOrThrow(detail);
//		printer(detail).appendSummary(detail, out, packet, template.compiledPattern(), domain);

		for (var header : packet.headers())
			formatHeaderTo(out, header, detail);
	}

	public void formatPacketTo(Appendable out, Packet packet, Detail detail) throws IOException {

	}

	/**
	 * @see com.slytechs.jnet.platform.api.util.format.DetailFormatter#formatTo(java.lang.Appendable,
	 *      java.lang.Object, com.slytechs.jnet.platform.api.util.format.Detail)
	 */
	@Override
	public void formatTo(Appendable out, Object target, Detail detail) throws IOException {
		switch (target) {
		case Packet packet -> formatPacketTo(out, packet, detail);
		case Header header -> formatHeaderTo(out, header, detail);

		case MetaPacket packet -> formatPacketTo(out, packet, detail);
		case MetaHeader header -> formatHeaderTo(out, header, detail);
		case MetaField field -> formatFieldTo(out, field, detail);
		case MetaAttribute attribute -> formatAttributeTo(out, attribute, detail);

		default -> String.valueOf(target);
		};
	}

	/**
	 * @see com.slytechs.jnet.platform.api.util.format.DetailFormatter#getDefaultDetail()
	 */
	@Override
	public Detail getDefaultDetail() {
		return this.detail;
	}

}
