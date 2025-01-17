/*
 * Sly Technologies Free License
 * 
 * Copyright 2025 Sly Technologies Inc.
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

import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.platform.api.util.format.DetailFormatter;
import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.common.Packet;
import com.slytechs.jnet.protocol.api.meta.impl.MetaBuilder;
import com.slytechs.jnet.protocol.api.meta.template.DetailTemplate;

/**
 * Default implementation of the MetaFormatter interface.
 */
public class WiresharkFormatter implements MetaFormatter, DetailFormatter {
	private static final String FIELD_FORMAT = "%s: %s";
	private static final String ITEM_FORMAT = "[%s] %s";
	private static final String BRANCH_FORMAT = "%s {%s}";

	private final MetaBuilder builder = new MetaBuilder();
	private final WiresharkPrinter printer = new WiresharkPrinter();

	@Override
	public String formatSummary(Object model) {
		// Format summary based on model type and content
		if (model == null)
			return "null";
		return String.format("Protocol: %s", model.getClass().getSimpleName());
	}

	@Override
	public String formatField(String name, Object value) {
		return String.format(FIELD_FORMAT, name, value);
	}

	@Override
	public String formatItem(String name, Object value) {
		return String.format(ITEM_FORMAT, name, value);
	}

	@Override
	public String formatBranch(String name, Object value) {
		return String.format(BRANCH_FORMAT, name, value);
	}

	@Override
	public String formatInfo(String info) {
		return info;
	}

	/**
	 * @see com.slytechs.jnet.platform.api.util.format.DetailFormatter#formatTo(java.lang.Appendable,
	 *      java.lang.Object, com.slytechs.jnet.platform.api.util.format.Detail)
	 */
	@Override
	public void formatTo(Appendable out, Object target, Detail detail) throws IOException {

		switch (target) {
		case Header header -> formatHeader(out, builder.buildHeader(header), detail);
		case Packet packet -> formatPacket(out, builder.buildPacket(packet), detail);
		case MetaHeader header -> formatHeader(out, header, detail);
		case MetaPacket packet -> formatPacket(out, packet, detail);
		case String str -> out.append(str);

		default -> out.append("[%s] <= WARNING".formatted(target.getClass()));
		};
	}

	/**
	 * @param out
	 * @param packet
	 * @param detail
	 * @return
	 * @throws Exception
	 */
	private void formatPacket(Appendable out, MetaPacket packet, Detail detail) throws IOException {

		DetailTemplate template = packet.headerTemplate(detail);

		printer.printSummary(out, template.summary().toString(packet, packet));

		for (var field : template.fieldList()) {
			MetaAttribute att = packet.getAttribute(field.name());
			printer.printInfo(out, field.template().toString(att, packet));
		}

		for (var header : packet.headers()) {
			formatHeader(out, header, detail);
		}
	}

	/**
	 * @param out
	 * @param header
	 * @param detail
	 * @return
	 */
	private void formatHeader(Appendable out, MetaHeader header, Detail detail) {
		DetailTemplate template = header.headerTemplate(detail);
	}
}