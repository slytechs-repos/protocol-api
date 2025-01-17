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
package com.slytechs.jnet.protocol.api.meta.template;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public record Templates(List<? extends Template> templates) implements Iterable<Template> {

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Template> iterator() {
		return new Iterator<Template>() {

			Iterator<? extends Template> it = templates.iterator();

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public Template next() {
				return it.next();
			}

		};
	}

	public Stream<? extends Template> stream() {
		return templates.stream();
	}

	public Template get(int index) {
		return templates.get(index);
	}
}
