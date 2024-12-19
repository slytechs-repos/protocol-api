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

import java.util.function.IntSupplier;

/**
 * A reference resolver for values of type int.
 */
interface IntMetaResolver {

	/**
	 * Resolve int value by name.
	 *
	 * @param refName the reference name
	 * @return the integer value
	 */
	int resolveInt(String refName);

	/**
	 * Create an {@code IntSupplier} with a given reference name.
	 *
	 * @param refName the name
	 * @return an adapted {@code IntSupplier} to this resolver to this reference
	 *         name name
	 * @throws IllegalStateException thrown if the given reference name can not be
	 *                               resolved
	 */
	default IntSupplier toIntSuplier(String refName) throws IllegalStateException {
		IntSupplier ref = () -> resolveInt(refName);

		/* TODO: Validate reference is linked, otherwise exception is thrown */
		ref.getAsInt();

		return ref;
	}
}