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

/**
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public record Import(String path) {

	/**
	 * @author Mark Bednarczyk [mark@slytechs.com]
	 * @author Sly Technologies Inc.
	 */
	public record Imports(List<Import> imports) implements Iterable<Import> {

		/**
		 * @see java.lang.Iterable#iterator()
		 */
		@Override
		public Iterator<Import> iterator() {
			return imports.iterator();
		}

	}

}
