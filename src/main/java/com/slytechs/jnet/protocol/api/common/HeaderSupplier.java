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
package com.slytechs.jnet.protocol.api.common;

/**
 * A functional interface to allocate a new instance of a protocol header.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
@FunctionalInterface
public interface HeaderSupplier {

	/**
	 * Of.
	 *
	 * @param moduleName the module name
	 * @param className  the class name
	 * @return the header supplier
	 */
	static HeaderSupplier of(String moduleName, String className) {
		return new ReflectedHeaderSupplier(moduleName, className);
	}

	/**
	 * New header instance.
	 *
	 * @return the header
	 */
	Header newHeaderInstance();
}
