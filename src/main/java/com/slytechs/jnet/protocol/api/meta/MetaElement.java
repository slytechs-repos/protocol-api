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

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface MetaElement {

	public class MetaParent implements MetaElement {

		private MetaElement parent;

		public MetaParent() {}

		public MetaParent(MetaElement newParent) {
			setParent(newParent);
		}

		public void setParent(MetaElement newParent) {
			this.parent = newParent;
		}

		/**
		 * @see com.slytechs.jnet.protocol.api.meta.MetaElement#parent()
		 */
		@Override
		public MetaElement parent() {
			return parent;
		}

	}

	/**
	 * Is empty where there are no child elements present.
	 *
	 * @return true, if is empty
	 */
	default boolean isEmpty() {
		return false;
	}

	MetaElement parent();
}
