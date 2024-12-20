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

import com.slytechs.jnet.platform.api.util.Enums;
import com.slytechs.jnet.platform.api.util.IsIntBitmask;
import com.slytechs.jnet.platform.api.util.IsIntFlag;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public enum Icmp6NeighborAdvertisementFlag implements IsIntFlag, IsIntBitmask {

	ROUTER(Icmp6NeighborAdvertisementFlag.ICMPv6_NA_FLAG_ROUTER),
	SOLICITED(Icmp6NeighborAdvertisementFlag.ICMPv6_NA_FLAG_SOLICITED),
	OVERRIDE(Icmp6NeighborAdvertisementFlag.ICMPv6_NA_FLAG_OVERRIDE),

	;

	public static final int ICMPv6_NA_MASK_FLAG = 0xe0000000;
	public static final int ICMPv6_NA_MASK_RESERVED = ~ICMPv6_NA_MASK_FLAG;
	public static final int ICMPv6_NA_FLAG_ROUTER = 0x80000000;
	public static final int ICMPv6_NA_FLAG_SOLICITED = 0x40000000;
	public static final int ICMPv6_NA_FLAG_OVERRIDE = 0x20000000;

	public static String resolve(Object flags) {
		return Enums.setOf(flags, Icmp6NeighborAdvertisementFlag.class).toString();
	}

	private final int bits;

	Icmp6NeighborAdvertisementFlag(int bits) {
		this.bits = bits;
	}

	/**
	 * @see com.slytechs.jnet.jnetruntime.util.IsIntBitmask#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return bits;
	}

}
