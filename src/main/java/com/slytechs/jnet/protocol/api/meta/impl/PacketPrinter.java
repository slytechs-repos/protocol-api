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

import com.slytechs.jnet.platform.api.domain.DomainAccessor;
import com.slytechs.jnet.protocol.api.meta.MetaElement;
import com.slytechs.jnet.protocol.api.meta.MetaField;
import com.slytechs.jnet.protocol.api.meta.MetaPredicates.OpenPredicate;
import com.slytechs.jnet.protocol.api.meta.MetaTemplate.MetaPattern;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface PacketPrinter {

	void appendField(Appendable out, MetaField field, DomainAccessor domain)
			throws IOException;

	void appendField(Appendable out, MetaField field, DomainAccessor domain, boolean openClose)
			throws IOException;

	void appendSummary(Appendable out, MetaElement element, MetaPattern pattern, DomainAccessor domain) throws IOException;

	void appendSummary(Appendable out, MetaElement element, MetaPattern pattern, DomainAccessor domain,
			OpenPredicate openPredicate)
			throws IOException;

	void pop();

	void push();

	void push(int charCount);

	void push(String additionalIndentation);

	PacketPrinter[] toArray();

}