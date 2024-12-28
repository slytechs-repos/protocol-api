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
package com.slytechs.jnet.protocol.api.core;

import java.util.function.IntSupplier;

/**
 * Layer3 frame type table, used by common descriptor types.
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public enum L3FrameType implements IntSupplier {
	IPv4(L3FrameType.L3_FRAME_TYPE_IPv4),
	IPv6(L3FrameType.L3_FRAME_TYPE_IPv6),
	IPX(L3FrameType.L3_FRAME_TYPE_IPX),
	OTHER(L3FrameType.L3_FRAME_TYPE_OTHER),

	;

	public final static int L3_FRAME_TYPE_IPv4 = 0;
	public final static int L3_FRAME_TYPE_IPv6 = 1;
	public final static int L3_FRAME_TYPE_IPX = 2;
	public final static int L3_FRAME_TYPE_OTHER = 3;

	/**
	 * Value of integer l3 type to enum constant.
	 *
	 * @param type the layer3 frame type
	 * @return the enum constant
	 */
	public static L3FrameType valueOfL3FrameType(int type) {
		return values()[type];
	}

	private final int type;

	/**
	 * Instantiates a new l 2 frame type.
	 *
	 * @param type     the id
	 * @param supplier the supplier
	 */
	L3FrameType(int type) {
		this.type = type;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HeaderInfo#id()
	 */
	public int type() {
		return type;
	}

	/**
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return type;
	}

}
