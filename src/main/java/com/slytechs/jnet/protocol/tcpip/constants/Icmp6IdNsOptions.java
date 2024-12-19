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

import static com.slytechs.jnet.protocol.api.pack.ProtocolPackTable.*;
import static com.slytechs.jnet.protocol.tcpip.constants.CoreId.*;

import java.util.Arrays;
import java.util.function.IntSupplier;

import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.common.HeaderOptionInfo;
import com.slytechs.jnet.protocol.api.common.HeaderSupplier;
import com.slytechs.jnet.protocol.api.pack.PackId;
import com.slytechs.jnet.protocol.api.pack.ProtocolPackTable;
import com.slytechs.jnet.protocol.tcpip.network.Icmp6Option;
import com.slytechs.jnet.protocol.tcpip.network.Icmp6Option.SourceLinkAddress;

/**
 * The ICMPv6 Neighbor Solicitation (NS) message option IDs and types.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public enum Icmp6IdNsOptions implements HeaderOptionInfo, PackId, IntSupplier {

	OPTION(PackId.ID_ORDINAL_CLASS, "Option", Icmp6Option::new),
	SOURCE_LINK_ADDRESS(1, "SADDR", SourceLinkAddress::new)

	;

	public static int ICMPv6_OPTION_TYPE_SOURCE_LINK_ADDRESS = 1;

	public static int ICMPv6_ID_OPTION = PackId.ID_ORDINAL_CLASS | PACK_ID_OPTIONS | CORE_CLASS_ICMP_OPT;
	public static int ICMPv6_ID_OPT_SOURCE_LINK_ADDRESS = 1 | PACK_ID_OPTIONS;

	private class Table {
		/** The Constant MAP_TABLE. */
		private static final int[] MAP_TABLE = new int[256];

		static {
			Arrays.fill(MAP_TABLE, -1);
		}
	}

	/**
	 * Map kind to id.
	 *
	 * @param type the type
	 * @return the int
	 */
	public static int mapTypeToId(int type) {
		return Table.MAP_TABLE[type];
	}

	private final int type;
	private final String abbr;
	private final HeaderSupplier supplier;
	private final int id;

	Icmp6IdNsOptions(int type, String abbr, HeaderSupplier supplier) {
		this.type = type;
		this.abbr = abbr;
		this.supplier = supplier;
		this.id = PackId.encodeId(ProtocolPackTable.OPTS, ordinal(), CORE_CLASS_IPv4_OPTION);

		Table.MAP_TABLE[type] = id;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HeaderInfo#id()
	 */
	@Override
	public int id() {
		return id;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HeaderSupplier#newHeaderInstance()
	 */
	@Override
	public Header newHeaderInstance() {
		return supplier.newHeaderInstance();
	}

	/**
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return type;
	}

	@Override
	public String abbr() {
		return abbr;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HeaderOptionInfo#getOptionAbbr()
	 */
	@Override
	public String getOptionAbbr() {
		return abbr;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HeaderOptionInfo#getParentHeaderId()
	 */
	@Override
	public int getParentHeaderId() {
		return CoreId.CORE_ID_ICMPv6_NEIGHBOR_SOLICITATION;
	}

}
