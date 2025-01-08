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

import java.io.IOException;
import java.util.Stack;

import com.slytechs.jnet.platform.api.domain.DomainAccessor;
import com.slytechs.jnet.platform.api.util.format.Detail;
import com.slytechs.jnet.protocol.api.meta.MetaElement;
import com.slytechs.jnet.protocol.api.meta.MetaField;
import com.slytechs.jnet.protocol.api.meta.MetaPredicates.OpenPredicate;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.FieldTemplate;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern;
import com.slytechs.jnet.protocol.api.meta.MetaTemplateFormatter;

/**
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class DefaultPacketPrinter implements PacketPrinter {
	private static final String NEW_LINE = "\n";
	private static final String DOWN_TRIABLE = "▾ ";
	private static final String RIGHT_TRIABLE = "▸ ";

	public String OPEN = DOWN_TRIABLE;
	public String CLOSED = RIGHT_TRIABLE;

	private final Stack<String> stack = new Stack<>();

	private final MetaTemplateFormatter formatter = new MetaTemplateFormatter();

	public DefaultPacketPrinter() {
		stack.push("");
	}

	@Override
	public void appendField(Detail detail, Appendable out, MetaField field, DomainAccessor domain) throws IOException {
		appendField0(detail, out, field, domain, null);
	}

	@Override
	public void appendField(Detail detail, Appendable out, MetaField field, DomainAccessor domain, boolean openClose)
			throws IOException {
		appendField0(detail, out, field, domain, openClose);
	}

	private void appendField0(Detail detail, Appendable out, MetaField field, DomainAccessor domain, Boolean openClose)
			throws IOException {
		FieldTemplate template = field.template(detail);
		String rightColumn = formatter.format(field, domain, template.pattern());

		push(openClose == null ? 4 : 2);
		try {
			out.append(indentString());

			if (openClose != null) {
				out.append(openCloseString(openClose));
			}

			if (!template.label().isBlank()) {
				out.append(template.label());
				out.append(": ");
			}

			out.append(rightColumn);

//			out.append(field.value().getFormatted());
//			out.append(" ---> (");
//			out.append(template.template());
//			out.append(")");

		} finally {
			out.append(NEW_LINE);
			pop();
		}

	}

	@Override
	public void appendSummary(Detail detail, Appendable out, MetaElement element, MetaPattern pattern,
			DomainAccessor domain)
			throws IOException {
		appendSummary(null, out, element, pattern, domain, OpenPredicate.level(detail, element));
	}

	@Override
	public void appendSummary(Detail detail, Appendable out, MetaElement element, MetaPattern pattern,
			DomainAccessor domain, OpenPredicate openPredicate)
			throws IOException {
		out.append(indentString());

		out.append(openCloseString(openPredicate.testOpen(level(), 0, 1, element)));

		String summary = formatter.format(element, domain, pattern);

		out.append(summary);

		out.append(NEW_LINE);
	}

	protected String indentString() {
		return stack.getLast();
	}

	protected int level() {
		return stack.size();
	}

	protected String openCloseString(boolean openClose) {
		return openClose ? OPEN : CLOSED;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.impl.PacketPrinter#pop()
	 */
	@Override
	public void pop() {
		stack.pop();
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.impl.PacketPrinter#push(java.lang.String)
	 */
	@Override
	public void push(String additionalIndentation) {
		stack.push(indentString() + additionalIndentation);
	}

}
