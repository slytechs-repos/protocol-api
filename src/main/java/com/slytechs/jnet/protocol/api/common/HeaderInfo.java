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
 * Interface which provides important information about a protocol header.
 */
public interface HeaderInfo extends HeaderSupplier {

	/**
	 * Gets the extension infos.
	 *
	 * @return the extension infos
	 */
	default HeaderOptionInfo[] getOptionInfos() {
		return HeaderOptionInfo.EMPTY_ARRAY;
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
	 * Gets the numerical header id of the protocol header described by this object.
	 *
	 * @return the header id
	 */
	int id();

	/**
	 * Name name of the protocol header.
	 *
	 * @return the string
	 */
	String name();

	/**
	 * The header's definition ordinal number within this pack. Header ids and
	 * header ordinals differ because header id has pack id and ordinal information
	 * encoded, whereas ordinal by itself is only unique within a pack.
	 *
	 * @return the ordinal number of this protocol header, unique only within a
	 *         defining protocol pack
	 */
	int ordinal();

	/**
	 * Abbr.
	 *
	 * @return the string
	 */
	default String abbr() {
		return name();
	}
}
