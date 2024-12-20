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

import java.util.EnumSet;
import java.util.Set;
import java.util.function.IntSupplier;

import com.slytechs.jnet.platform.api.internal.util.format.BitFormat;
import com.slytechs.jnet.platform.api.util.Enums;

/**
 * TCP flag which act as control bits.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public enum TcpFlag implements IntSupplier {

	/** Congestion window reduced. */
	CWR(TcpFlag.TCP_FLAG_CWR, "Congestion window reduced"),

	/** ECN-Echo. */
	ECE(TcpFlag.TCP_FLAG_ECE, "ECN-Echo"),

	/** Urgent pointer. */
	URG(TcpFlag.TCP_FLAG_URG, "Urgent pointer"),

	/** Acknowledgment. */
	ACK(TcpFlag.TCP_FLAG_ACK, "Acknowledgment"),

	/** Push function. */
	PSH(TcpFlag.TCP_FLAG_PSH, "Push function"),

	/** Reset the connection. */
	RST(TcpFlag.TCP_FLAG_RST, "Reset the connection"),

	/** Synchronize sequence numbers. */
	SYN(TcpFlag.TCP_FLAG_SYN, "Synchronize sequence numbers"),

	/** Last packet from sender. */
	FIN(TcpFlag.TCP_FLAG_FIN, "Last packet from sender"),

	;

	/** The Constant Acknowledgment. */
	public static final int TCP_FLAG_ACK = 0x10;

	/** The Constant FLAG_CONG. */
	public static final int TCP_FLAG_CONG = 0x80;

	/** The Constant Congestion window reduced. */
	public static final int TCP_FLAG_CWR = 0x80;

	/** The Constant ECN-Echo. */
	public static final int TCP_FLAG_ECE = 0x40;

	/** The Constant FLAG_ECN. */
	public static final int TCP_FLAG_ECN = 0x40;

	/** The Constant Last packet from sender. */
	public static final int TCP_FLAG_FIN = 0x01;

	/** The Constant Push function. */
	public static final int TCP_FLAG_PSH = 0x08;

	/** The Constant Reset the connection. */
	public static final int TCP_FLAG_RST = 0x04;

	/** The Constant Synchronize sequence numbers. */
	public static final int TCP_FLAG_SYN = 0x02;

	/** The Constant Urgent pointer. */
	public static final int TCP_FLAG_URG = 0x20;

	private static final String FLAGS_FORMAT = "..B WEUA PRSF";
	private static final BitFormat FLAGS_FORMATTER = new BitFormat(FLAGS_FORMAT);

	/**
	 * Resolve.
	 *
	 * @param flags the type
	 * @return the string
	 */
	public static String resolve(Object flags) {
		return Enums.resolve(flags, TcpFlag.class);
	}

	/**
	 * Resolve bit format.
	 *
	 * @param flags the flags
	 * @return the string
	 */
	public static String resolveBitFormat(Object flags) {
		return FLAGS_FORMATTER.format(flags);
	}

	/** The bitmask. */
	private final int bitmask;

	/** The description. */
	private final String description;

	/**
	 * Instantiates a new tcp flag.
	 *
	 * @param bitmask     the bitmask
	 * @param description the description
	 */
	TcpFlag(int bitmask, String description) {
		this.bitmask = bitmask;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param flags the flags
	 * @return the sets the
	 */
	public static Set<TcpFlag> valueOfInt(int flags) {
		Set<TcpFlag> set = EnumSet.noneOf(TcpFlag.class);

		for (TcpFlag flag : values()) {
			if ((flags & flag.bitmask) != 0)
				set.add(flag);
		}

		return set;
	}

	/**
	 * Checks if is sets the.
	 *
	 * @param flags the flags
	 * @return true, if is sets the
	 */
	public boolean isSet(int flags) {
		return (flags & bitmask) != 0;
	}

	/**
	 * Checks if is not set.
	 *
	 * @param flags the flags
	 * @return true, if is not set
	 */
	public boolean isNotSet(int flags) {
		return (flags & bitmask) == 0;
	}

	/**
	 * Flag's Bitmask.
	 *
	 * @return the integer bitmask suitable for bit operations
	 */
	public int bitmask() {
		return bitmask;
	}

	/**
	 * A short description of the flag
	 *
	 * @return the description
	 */
	public String description() {
		return description;
	}

	/**
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return bitmask;
	}
}
