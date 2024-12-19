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

import com.slytechs.jnet.protocol.api.meta.Meta;

/**
 * A specialized builtin header type, which allows encapsulation of any header
 * type as long as we have offset and length information about it. This class is
 * identical to {@link Payload} but has more specific nomenclature for "Other"
 * or otherwise undefined, protocol header type.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
@Meta
public final class Other extends Payload {

	/**
	 * Instantiates a new other.
	 */
	public Other() {
		super();
	}

}
