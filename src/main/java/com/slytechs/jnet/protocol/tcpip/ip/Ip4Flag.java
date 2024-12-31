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
package com.slytechs.jnet.protocol.tcpip.ip;

import java.util.EnumSet;
import java.util.Set;

/**
 * A three-bit field follows and is used to control or identify fragments.
 * 
 * <p>
 * If the DF flag is set, and fragmentation is required to route the packet,
 * then the packet is dropped. This can be used when sending packets to a host
 * that does not have resources to perform reassembly of fragments. It can also
 * be used for path MTU discovery, either automatically by the host IP software,
 * or manually using diagnostic tools such as ping or traceroute. For
 * unfragmented packets, the MF flag is cleared. For fragmented packets, all
 * fragments except the last have the MF flag set. The last fragment has a
 * non-zero Fragment Offset field, differentiating it from an unfragmented
 * packet.
 * </p>
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public enum Ip4Flag {

	/** Reserved; must be zero. */
	RES,

	/** Don't Fragment. */
	DF,

	/** More Fragments. */
	MF,

	;

	/** The Constant Reserved; must be zero. */
	public static final int IPv4_FLAG_RESERVED = 0x4;

	/** The Constant IPv4_FLAG_DF. */
	public static final int IPv4_FLAG03_DF = 0x2;

	/** The Constant IPv4_FLAG_MF. */
	public static final int IPv4_FLAG03_MF = 0x1;

	/** The Constant IPv4_FLAG_MF. */
	public static final int IPv4_FLAG08 = 0x70;

	/** The Constant IPv4_FLAG16_DF. */
	public static final int IPv4_FLAG16_DF = 0x4000;

	/** The Constant IPv4_FLAG16_MF. */
	public static final int IPv4_FLAG16_MF = 0x2000;

	/**
	 * Value of.
	 *
	 * @param flags03 the flags
	 * @return the sets the
	 */
	public static Set<Ip4Flag> valueOfInt03(int flags03) {
		Set<Ip4Flag> set = EnumSet.noneOf(Ip4Flag.class);

		if ((flags03 & IPv4_FLAG_RESERVED) > 0)
			set.add(RES);

		if ((flags03 & IPv4_FLAG03_DF) > 0)
			set.add(DF);

		if ((flags03 & IPv4_FLAG03_MF) > 0)
			set.add(MF);

		return set;
	}

	/**
	 * Value of int 16.
	 *
	 * @param flags16 the flags 16
	 * @return the sets the
	 */
	public static Set<Ip4Flag> valueOfInt16(int flags16) {
		Set<Ip4Flag> set = EnumSet.noneOf(Ip4Flag.class);

		if ((flags16 & IPv4_FLAG16_DF) > 0)
			set.add(DF);

		if ((flags16 & IPv4_FLAG16_MF) > 0)
			set.add(MF);

		return set;
	}
}
