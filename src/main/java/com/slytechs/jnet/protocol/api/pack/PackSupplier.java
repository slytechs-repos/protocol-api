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
package com.slytechs.jnet.protocol.api.pack;

import java.util.function.IntFunction;

/**
 * A protocol pack supplier given a numerical pack id.
 */
public interface PackSupplier extends IntFunction<Pack<?>> {

	/** A supplier which supplies an empty pack. */
	PackSupplier EMPTY = ignored -> null;

	/**
	 * Apply.
	 *
	 * @param packId the pack id
	 * @return the pack
	 * @see java.util.function.IntFunction#apply(int)
	 */
	@Override
	Pack<?> apply(int packId);
}
