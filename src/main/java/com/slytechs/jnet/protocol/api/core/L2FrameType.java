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
import com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.Ethernet;
import com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.Llc;
import com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.Snap;

/**
 * The Enum L2FrameType.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public enum L2FrameType implements HeaderInfo, IntSupplier {

	/** The other. */
	OTHER(0),

	/** The ether. */
	ETHER(CoreId.CORE_ID_ETHER, Ethernet::new),

	/** The llc. */
	LLC(CoreId.CORE_ID_LLC, Llc::new),

	/** The snap. */
	SNAP(CoreId.CORE_ID_SNAP, Snap::new),

	/** The ppp. */
	PPP(CoreId.CORE_ID_PPP),

	/** The fddi. */
	FDDI(CoreId.CORE_ID_FDDI),

	/** The atm. */
	ATM(CoreId.CORE_ID_ATM),

	/** The novell raw. */
	NOVELL_RAW(CoreId.CORE_ID_ETHER),

	/** The isl. */
	ISL(CoreId.CORE_ID_ETHER),

	;

	/** The id. */
	private final int id;

	/** The supplier. */
	private final HeaderSupplier supplier;

	/**
	 * Instantiates a new l 2 frame type.
	 *
	 * @param id the id
	 */
	L2FrameType(int id) {
		this.id = id;
		this.supplier = Other::new;
	}

	/**
	 * Instantiates a new l 2 frame type.
	 *
	 * @param id       the id
	 * @param supplier the supplier
	 */
	L2FrameType(int id, HeaderSupplier supplier) {
		this.id = id;
		this.supplier = supplier;
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
	 * @param l2FrameType the layer2 frame type
	 * @return the enum constant
	 */
	public static L2FrameType valueOfL2FrameType(int l2FrameType) {
		return values()[l2FrameType];
	}

	/**
	 * Gets the l 2 frame type as int.
	 *
	 * @return the l 2 frame type as int
	 */
	public int getL2FrameTypeAsInt() {
		return ordinal();
	}

	/**
	 * Gets the header id.
	 *
	 * @return the header id
	 * @see com.slytechs.jnet.protocol.api.common.HeaderInfo#id()
	 */
	@Override
	public int id() {
		return id;
	}

	/**
	 * New header instance.
	 *
	 * @return the header
	 * @see com.slytechs.jnet.protocol.api.common.HeaderSupplier#newHeaderInstance()
	 */
	@Override
	public Header newHeaderInstance() {
		return supplier != null ? supplier.newHeaderInstance() : null;
	}

	/**
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return ordinal();
	}

}
