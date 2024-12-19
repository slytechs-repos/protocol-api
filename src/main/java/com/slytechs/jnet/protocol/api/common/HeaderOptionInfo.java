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
 * Provides important information about a header extension in addition to
 * {@link HeaderInfo} information.
 */
public interface HeaderOptionInfo extends HeaderInfo {

	/** The empty array for efficient initialization. */
	HeaderOptionInfo[] EMPTY_ARRAY = new HeaderOptionInfo[0];

	/**
	 * Gets the option abbreviation.
	 *
	 * @return the extension abbreviation
	 */
	String getOptionAbbr();

	/**
	 * Gets the parent header id for which this extension belong to.
	 *
	 * @return the parent header id
	 */
	int getParentHeaderId();

}
