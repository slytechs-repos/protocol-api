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
 * The Enum L2FrameType.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public enum L2FrameType implements IntSupplier {

	/** The other. */
	OTHER(L2FrameType.L2_FRAME_TYPE_OTHER),

	/** The ether. */
	ETHER(L2FrameType.L2_FRAME_TYPE_ETHER),

	/** The llc. */
	LLC(L2FrameType.L2_FRAME_TYPE_LLC),

	/** The snap. */
	SNAP(L2FrameType.L2_FRAME_TYPE_SNAP),

	/** The ppp. */
	PPP(L2FrameType.L2_FRAME_TYPE_PPP),

	/** The fddi. */
	FDDI(L2FrameType.L2_FRAME_TYPE_FDDI),

	/** The atm. */
	ATM(L2FrameType.L2_FRAME_TYPE_ATM),

	/** The novell raw. */
	NOVELL_RAW(L2FrameType.L2_FRAME_TYPE_NOVELL_RAW),

	/** The isl. */
	ISL(L2FrameType.L2_FRAME_TYPE_ISL),

	;

	/** The id. */
	private final int type;

	/**
	 * Instantiates a new l 2 frame type.
	 *
	 * @param type the id
	 */
	L2FrameType(int type) {
		this.type = type;
	}

	/** The Constant L2_FRAME_TYPE_UNKNOWN. */
	public final static int L2_FRAME_TYPE_UNKNOWN = -1;

	/** The Constant L2_FRAME_TYPE_OTHER. */
	public final static int L2_FRAME_TYPE_OTHER = 0;

	/** The Constant L2_FRAME_TYPE_ETHER. */
	public final static int L2_FRAME_TYPE_ETHER = 1;

	/** The Constant L2_FRAME_TYPE_LLC. */
	public final static int L2_FRAME_TYPE_LLC = 2;

	/** The Constant L2_FRAME_TYPE_SNAP. */
	public final static int L2_FRAME_TYPE_SNAP = 3;

	/** The Constant L2_FRAME_TYPE_NOVELL_RAW. */
	public final static int L2_FRAME_TYPE_NOVELL_RAW = 4;

	/** The Constant L2_FRAME_TYPE_ISL. */
	public final static int L2_FRAME_TYPE_ISL = 5;

	/** The Constant L2_FRAME_TYPE_PPP. */
	public final static int L2_FRAME_TYPE_PPP = 6;

	/** The Constant L2_FRAME_TYPE_FDDI. */
	public final static int L2_FRAME_TYPE_FDDI = 7;

	/** The Constant L2_FRAME_TYPE_ATM. */
	public final static int L2_FRAME_TYPE_ATM = 8;

	/**
	 * Value of integer l2 type to enum constant.
	 *
	 * @param type the layer2 frame type
	 * @return the enum constant
	 */
	public static L2FrameType valueOfL2FrameType(int type) {
		return values()[type];
	}

	/**
	 * Gets the header id.
	 *
	 * @return the header id
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
