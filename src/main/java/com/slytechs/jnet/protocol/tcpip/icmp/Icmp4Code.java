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
package com.slytechs.jnet.protocol.tcpip.icmp;

import com.slytechs.jnet.protocol.api.meta.MetaField;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public enum Icmp4Code {

	NET_UNREACHABLE(3, 0),
	HOST_UNREACHABLE(3, 1),
	PROTO_UNREACHABLE(3, 2),
	PORT_UNREACHABLE(3, 3),
	FRAG_REQUIRED(3, 4),
	SOURCE_ROUTE(3, 5),
	NET_UNKNOWN(3, 6),
	HOST_UNKNOWN(3, 7),
	HOST_ISOLATED(3, 8),
	NET_PROHIBITED(3, 9),
	HOST_PROHIBITED(3, 10),
	NET_UNREACHABLE_TOS(3, 11),
	HOST_UNREACHABLE_TOS(3, 12),
	COMM_PROHIBITED(3, 13),
	HOST_PRECEDENCE_VIOLATIO(3, 14),
	PRECEDENCE_CUTOFF_IN_EFFECT(3, 15),

	NET_DGRAM_REDIRECT(4, 0),
	HOST_DGRAM_REDIRECT(4, 1),
	NET_DGRAM_TOS_REDIRECT(4, 2),
	HOST_DGRAM_TOS_REDIRECT(4, 3),

	TTL_EXPIRED(11, 0),
	FRAG_TTL_EXCEEDED(11, 1),

	POINTER_TO_ERROR(12, 0),
	MISSING_OPTION(12, 1),
	BAD_LENGTH(12, 2),

	NO_ERROR(43, 0),
	MALFORMED_QUERY(43, 1),
	NO_SUCH_INTERFACE(43, 2),
	NO_SUCH_TABLE_ENTRY(43, 3),
	MULTIPLE_INTS_SATISFY_QUERY(43, 4),

	;

	public static String resolve(MetaField field, Object value) {
		Number typeNum = field.getParentHeader().getField("type").get();
		int type = typeNum.intValue();
		int code = ((Number) value).intValue();

		for (Icmp4Code c : values()) {
			if (c.type == type && c.code == code)
				return c.name();
		}

		return "unassigned";
	}

	private final int type;
	private final int code;

	/**
	 * @param type
	 * @param code
	 */
	Icmp4Code(int type, int code) {
		this.type = type;
		this.code = code;
	}

}
