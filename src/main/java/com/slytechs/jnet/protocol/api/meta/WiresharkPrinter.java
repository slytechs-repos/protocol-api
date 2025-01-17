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

/**
 * Implementation of MetaPrinter that writes to an Appendable sink.
 */
public class WiresharkPrinter implements MetaPrinter {
	private static final String INDENT_CHAR = "  ";
	private static final String OPEN_CARROT = "▼";
	private static final String CLOSED_CARROT = "▶";

	public WiresharkPrinter() {}

	private void indent(Appendable out, int level) throws IOException {
		for (int i = 0; i < level; i++) {
			out.append(INDENT_CHAR);
		}
	}

	@Override
	public void printSummary(Appendable out, String summary) throws IOException {
		out.append(summary).append('\n');
	}

	@Override
	public void printField(Appendable out, String field, int indent, boolean isOpen) throws Exception {
		indent(out, indent);
		out.append(field).append('\n');
	}

	@Override
	public void printItem(Appendable out, String item, int indent) throws IOException {
		indent(out, indent);
		out.append(item).append('\n');
	}

	@Override
	public void printBranch(Appendable out, String branch, int indent, boolean isOpen) throws IOException {
		indent(out, indent);
		out.append(isOpen ? OPEN_CARROT : CLOSED_CARROT)
				.append(' ')
				.append(branch)
				.append('\n');
	}

	@Override
	public void printInfo(Appendable out, String info) throws IOException {
		out.append(info).append('\n');
	}
}