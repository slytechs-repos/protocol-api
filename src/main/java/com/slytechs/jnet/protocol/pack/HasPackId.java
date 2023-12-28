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
package com.slytechs.jnet.protocol.pack;

/**
 * * A unique 32-bit protocol ID or a 32-bit record and a box class. A protocol
 * pack ID is unique ID across all protocol packs and identifies specific
 * headers and protocol packs.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface HasPackId {

	/**
	 * Gets a 32-bit pack id for this object.
	 *
	 * @return the id
	 */
	int id();

}
