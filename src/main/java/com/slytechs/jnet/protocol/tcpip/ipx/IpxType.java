/*
 * Sly Technologies Free License
 * 
 * Copyright 2024 Sly Technologies Inc.
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
package com.slytechs.jnet.protocol.tcpip.ipx;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */

import java.util.function.IntSupplier;

import com.slytechs.jnet.platform.api.util.Enums;

/**
 * ETH_IPX_SPX packet types used to identify the type of service being
 * requested.
 */
public enum IpxType implements IntSupplier {

	UNKNOWN(IpxType.IPX_TYPE_UNKNOWN),
	RIP(IpxType.IPX_TYPE_RIP),
	ECHO_PACKET(IpxType.IPX_TYPE_ECHO),
	ERROR_PACKET(IpxType.IPX_TYPE_ERROR),
	PEP(IpxType.IPX_TYPE_PEP),
	SPX(IpxType.IPX_TYPE_SPX),
	NCP(IpxType.IPX_TYPE_NCP),
	;

	public static final int IPX_TYPE_UNKNOWN = 0;
	public static final int IPX_TYPE_RIP = 1;
	public static final int IPX_TYPE_ECHO = 2;
	public static final int IPX_TYPE_ERROR = 3;
	public static final int IPX_TYPE_PEP = 4;
	public static final int IPX_TYPE_SPX = 5;
	public static final int IPX_TYPE_NCP = 17;

	public static String resolve(Object obj) {
		return Enums.resolve(obj, IpxType.class);
	}

	public static IpxType valueOfIpxType(int type) {
		return Enums.valueOf(type, IpxType.class);
	}

	private final int type;

	IpxType(int type) {
		this.type = type;
	}

	@Override
	public int getAsInt() {
		return type;
	}

	public static IpxType valueOf(int type) {
		for (var e : values())
			if (e.type == type)
				return e;

		return null;
	}

	/**
	 * Resolve protocol value to name.
	 *
	 * @param protocolIntValue the protocol int value
	 * @return the protocol name or "Unknown" if not matched
	 */
	public static String resolveType(Object protocolIntValue) {
		var resolved = valueOf((Integer) protocolIntValue);
		if (resolved == null)
			return "Unknown";

		return resolved.toString();
	}
}