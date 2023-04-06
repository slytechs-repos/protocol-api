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
package com.slytechs.jnet.protocol;

import com.slytechs.jnet.protocol.packet.HeaderExtensionInfo;

/**
 * The Interface HeaderInfo.
 */
public interface HeaderInfo extends HeaderSupplier {

	/**
	 * Gets the extension infos.
	 *
	 * @return the extension infos
	 */
	default HeaderExtensionInfo[] getExtensionInfos() {
		return HeaderExtensionInfo.EMPTY_ARRAY;
	}

	/**
	 * Gets the header class name.
	 *
	 * @return the header class name
	 */
	default String getHeaderClassName() {
		return newHeaderInstance().getClass().getCanonicalName();
	}

	/**
	 * Gets the header id.
	 *
	 * @return the header id
	 */
	int getHeaderId();

	/**
	 * Name.
	 *
	 * @return the string
	 */
	String name();

	/**
	 * Ordinal.
	 *
	 * @return the int
	 */
	int ordinal();
}
