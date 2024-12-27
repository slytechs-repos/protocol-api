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

import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.common.HeaderInfo;
import com.slytechs.jnet.protocol.api.common.HeaderSupplier;
import com.slytechs.jnet.protocol.api.common.Other;
import com.slytechs.jnet.protocol.tcpipREFACTOR.constants.CoreId;
import com.slytechs.jnet.protocol.tcpipREFACTOR.ip.Ip4;

/**
 * Layer3 frame type table, used by common descriptor types.
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public enum L3FrameType implements HeaderInfo, IntSupplier {
	IPv4(CoreId.CORE_ID_IPv4, Ip4::new),
	IPv6(CoreId.CORE_ID_IPv4, Ip4::new),
	IPX(CoreId.CORE_ID_IPv4, Ip4::new),
	OTHER(0, Other::new),

	;

	public final static int L3_FRAME_TYPE_IPv4 = 0;
	public final static int L3_FRAME_TYPE_IPv6 = 1;
	public final static int L3_FRAME_TYPE_IPX = 2;
	public final static int L3_FRAME_TYPE_OTHER = 3;

	/**
	 * Value of integer l3 type to enum constant.
	 *
	 * @param l3FrameType the layer3 frame type
	 * @return the enum constant
	 */
	public static L3FrameType valueOfL3FrameType(int l3FrameType) {
		return values()[l3FrameType];
	}

	private final int id;
	private final HeaderSupplier supplier;

	/**
	 * Instantiates a new l 2 frame type.
	 *
	 * @param id       the id
	 * @param supplier the supplier
	 */
	L3FrameType(int id, HeaderSupplier supplier) {
		this.id = id;
		this.supplier = supplier;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HeaderSupplier#newHeaderInstance()
	 */
	@Override
	public Header newHeaderInstance() {
		return supplier.newHeaderInstance();
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HeaderInfo#id()
	 */
	@Override
	public int id() {
		return id;
	}

	/**
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return ordinal();
	}

}
