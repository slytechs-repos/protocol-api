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
package com.slytechs.jnet.protocol.tcpip.constants;

import java.util.function.IntSupplier;

/**
 * Layer 4 frame type.
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public enum L4FrameType implements IntSupplier {

	/** The tcp. */
	TCP,
	
	/** The udp. */
	UDP,
	
	/** The icmp. */
	ICMP,
	
	/** The other. */
	OTHER,
	
	/** The gre. */
	GRE,
	
	/** The sctp. */
	SCTP,
	
	/** The I pv 4. */
	IPv4,
	
	/** The I pv 6. */
	IPv6,

	;

	/**
	 * The Constant L4_FRAME_TYPE_TCP.
	 */
	public final static int L4_FRAME_TYPE_TCP = 0;
	
	/** The Constant L4_FRAME_TYPE_UDP. */
	public final static int L4_FRAME_TYPE_UDP = 1;
	
	/** The Constant L4_FRAME_TYPE_ICMP. */
	public final static int L4_FRAME_TYPE_ICMP = 2;
	
	/** The Constant L4_FRAME_TYPE_OTHER. */
	public final static int L4_FRAME_TYPE_OTHER = 3;
	
	/** The Constant L4_FRAME_TYPE_GRE. */
	public final static int L4_FRAME_TYPE_GRE = 4;
	
	/** The Constant L4_FRAME_TYPE_SCTP. */
	public final static int L4_FRAME_TYPE_SCTP = 5;
	
	/** The Constant L4_FRAME_TYPE_IPv4. */
	public final static int L4_FRAME_TYPE_IPv4 = 6;
	
	/** The Constant L4_FRAME_TYPE_IPv6. */
	public final static int L4_FRAME_TYPE_IPv6 = 7;
	
	/**
	 * Value of L 4 frame type.
	 *
	 * @param type the type
	 * @return the l 4 frame type
	 * @formatter:off 
	 */

	/**
	 * Get the value of L4 frame type constant.
	 *
	 * @param type the L4 type
	 * @return the layer 4 constant
	 */
	public static L4FrameType valueOfL4FrameType(int type) {
		return values()[type];
	}

	/**
	 * Gets the as int.
	 *
	 * @return the as int
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return ordinal();
	}

}
