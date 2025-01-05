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
public class WiresharkPacketPrinter implements PacketPrinter {
	MetaTemplateFormatter formatter = new MetaTemplateFormatter();

	static class Debug extends Summary {}

	static class Hexdump extends Summary {}

	static class High extends Summary {

		@Override
		public void appendField(Appendable out, MetaField field, DomainAccessor domain) throws IOException {
			FieldTemplate template = field.template(Detail.HIGH);
			String rightColumn = formatter.format(field, domain, template.pattern());

			out.append(indentString());

			if (!template.label().isBlank()) {
				out.append(template.label());
				out.append(": ");
			}

			out.append(rightColumn);

//			out.append(field.value().getFormatted());
//			out.append(" ---> (");
//			out.append(template.template());
//			out.append(")");

			out.append(NEW_LINE);
		}

		@Override
		public void appendField(Appendable out, MetaField field, DomainAccessor domain, boolean openClose)
				throws IOException {
			FieldTemplate template = field.template(Detail.HIGH);
			String rightColumn = formatter.format(field, domain, template.pattern());

			out.append(indentString());

			out.append(openCloseString(openClose));

			if (!template.label().isBlank()) {
				out.append(template.label());
				out.append(": ");
			}

			out.append(rightColumn);

//			out.append(field.value().getFormatted());
//			out.append(" ---> (");
//			out.append(template.template());
//			out.append(")");

			out.append(NEW_LINE);
		}

		@Override
		public void appendSummary(Appendable out, MetaElement element, MetaPattern pattern, DomainAccessor domain)
				throws IOException {
			appendSummary(out, element, pattern, domain, OpenPredicate.level(Detail.HIGH, element));
		}
	}

	static class Medium extends Summary {
		@Override
		public void appendSummary(Appendable out, MetaElement element, MetaPattern pattern, DomainAccessor domain)
				throws IOException {
			appendSummary(out, element, pattern, domain, OpenPredicate.level(Detail.MEDIUM, element));
		}
	}

	static class Off extends WiresharkPacketPrinter {}

	static class Summary extends WiresharkPacketPrinter {
		@Override
		public void appendSummary(Appendable out, MetaElement element, MetaPattern pattern, DomainAccessor domain)
				throws IOException {
			appendSummary(out, element, pattern, domain, OpenPredicate.level(Detail.SUMMARY, element));
		}

		@Override
		public void appendSummary(Appendable out, MetaElement element, MetaPattern pattern, DomainAccessor domain,
				OpenPredicate openPredicate)
				throws IOException {
			out.append(indentString());

			out.append(openCloseString(openPredicate.testOpen(level(), 0, 1, element)));

			String summary = formatter.format(element, domain, pattern);

			out.append(summary);

			out.append(NEW_LINE);
		}
	}

	private static final String NEW_LINE = "\n";
	private static final String DOWN_TRIABLE = "▾ ";
	private static final String RIGHT_TRIABLE = "▸ ";

	public final String OPEN = DOWN_TRIABLE;
	public final String CLOSED = RIGHT_TRIABLE;

	private static final int DEFAULT_INDENT_SIZE = 4;

	private final Stack<String> stack = new Stack<>();

	private char openChar = '▾';
	private char closeChar = '▸';

	public WiresharkPacketPrinter() {
		stack.push("");
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.impl.PacketPrinter#appendField(java.lang.Appendable,
	 *      com.slytechs.jnet.protocol.api.meta.MetaField,
	 *      com.slytechs.jnet.protocol.api.meta.impl.PacketDomain)
	 */
	@Override
	public void appendField(Appendable out, MetaField field, DomainAccessor domain)
			throws IOException {}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.impl.PacketPrinter#appendField(java.lang.Appendable,
	 *      com.slytechs.jnet.protocol.api.meta.MetaField,
	 *      com.slytechs.jnet.protocol.api.meta.impl.PacketDomain, boolean)
	 */
	@Override
	public void appendField(Appendable out, MetaField field, DomainAccessor domain, boolean openClose)
			throws IOException {}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.impl.PacketPrinter#appendSummary(java.lang.Appendable,
	 *      MetaElement, java.lang.String,
	 *      com.slytechs.jnet.protocol.api.meta.impl.PacketDomain)
	 */
	@Override
	public void appendSummary(Appendable out, MetaElement element, MetaPattern pattern, DomainAccessor domain)
			throws IOException {}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.impl.PacketPrinter#appendSummary(java.lang.Appendable,
	 *      MetaElement, java.lang.String,
	 *      com.slytechs.jnet.protocol.api.meta.impl.PacketDomain, OpenPredicate)
	 */
	@Override
	public void appendSummary(Appendable out, MetaElement element, MetaPattern pattern, DomainAccessor domain,
			OpenPredicate openPredicate)
			throws IOException {}

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
	 * @see com.slytechs.jnet.protocol.api.meta.impl.PacketPrinter#push()
	 */
	@Override
	public void push() {
		push(DEFAULT_INDENT_SIZE);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.impl.PacketPrinter#push(int)
	 */
	@Override
	public void push(int charCount) {
		push(" ".repeat(charCount));
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.impl.PacketPrinter#push(java.lang.String)
	 */
	@Override
	public void push(String additionalIndentation) {
		stack.push(indentString() + additionalIndentation);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.impl.PacketPrinter#toArray()
	 */
	@Override
	public PacketPrinter[] toArray() {
		PacketPrinter[] table = new PacketPrinter[] {
				new Off(),
				new Summary(),
				new Medium(),
				new High(),
				new Debug(),
				new Hexdump(),
		};

		assert table.length == Detail.values().length;

		return table;
	}

}
