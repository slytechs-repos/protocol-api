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

import static com.slytechs.jnet.protocol.api.core.CoreId.*;
import static com.slytechs.jnet.protocol.api.pack.ProtocolPackTable.*;

import java.util.Arrays;
import java.util.function.IntSupplier;

import com.slytechs.jnet.platform.api.util.Enums;
import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.common.HeaderOptionInfo;
import com.slytechs.jnet.protocol.api.common.HeaderSupplier;
import com.slytechs.jnet.protocol.api.common.Other;
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.pack.PackId;
import com.slytechs.jnet.protocol.api.pack.ProtocolPackTable;

/**
 * IPv4 option header ID constants.
 */
public enum Ip6IdOption implements HeaderOptionInfo, PackId, IntSupplier {

	/** A generic base IP option. */
	IP_OPTTION(254, "IP:OPT", Ip4Option::new),

	/** The base IPv4 option. */
	IPv6_OPTION(253, "IPv6:OPT", Ip4Option::new),

	PAD1(0x00, "PAD1", Ip6Pad1Option::new),
	PADN(0x01, "PADN", Ip6PadnOptioin::new),
	TUNNEL_ENCAPS_LIMIT(0x04, "TL", Ip6Option::new),
	ROUTER_ALERT(0x05, "RA", Ip6RouterAlertOption::new),
	RPL(0x23, "RPL", Ip6Option::new),
	QUICK_START(0x26, "QS", Ip6Option::new),
	MTU(0x30, "MTU", Ip6Option::new),
	MPL(0x6D, "MPL", Ip6Option::new),
	JUMBO_PAYLOAD(0xC2, "JUMBO", Ip6JumboPayloadOption::new),

	;

	// @formatter:off
	public static final int IPv6_OPTION_PAD1                = 0x00;
	public static final int IPv6_OPTION_PADN                = 0x01;
	public static final int IPv6_OPTION_TUNNEL_ENCAPS_LIMIT = 0x04;
	public static final int IPv6_OPTION_ROUTER_ALERT        = 0x05;
	public static final int IPv6_OPTION_RPL                 = 0x23;
	public static final int IPv6_OPTION_QUICK_START         = 0x26;
	public static final int IPv6_OPTION_MTU                 = 0x30;
	public static final int IPv6_OPTION_MPL                 = 0x6D;
	public static final int IPv6_OPTION_JUMBO_PAYLOAD       = 0xC2;
	// @formatter:on

	// @formatter:off
	public static final int IP_ID_OPT_HEADER                 = 0 | PACK_ID_OPTIONS | CORE_CLASS_IP_OPTION;
	public static final int IPv6_ID_OPT_HEADER               = 1 | PACK_ID_OPTIONS | CORE_CLASS_IPv6_OPTION;
	public static final int IPv6_ID_OPT_PAD1                 = 2 | PACK_ID_OPTIONS | CORE_CLASS_IPv6_OPTION;
	public static final int IPv6_ID_OPT_PADN                 = 3 | PACK_ID_OPTIONS | CORE_CLASS_IPv6_OPTION;
	public static final int IPv6_ID_OPT_TUNNEL_ENCAPS_LIMIT  = 4 | PACK_ID_OPTIONS | CORE_CLASS_IPv6_OPTION;
	public static final int IPv6_ID_OPT_ROUTER_ALERT         = 5 | PACK_ID_OPTIONS | CORE_CLASS_IPv6_OPTION;
	public static final int IPv6_ID_OPT_RPL                  = 6 | PACK_ID_OPTIONS | CORE_CLASS_IPv6_OPTION;
	public static final int IPv6_ID_OPT_QUICK_START          = 7 | PACK_ID_OPTIONS | CORE_CLASS_IPv6_OPTION;
	public static final int IPv6_ID_OPT_MTU                  = 8 | PACK_ID_OPTIONS | CORE_CLASS_IPv6_OPTION;
	public static final int IPv6_ID_OPT_MPL                  = 9 | PACK_ID_OPTIONS | CORE_CLASS_IPv6_OPTION;
	public static final int IPv6_ID_OPT_JUMBO_PAYLOAD        = 10 | PACK_ID_OPTIONS | CORE_CLASS_IPv6_OPTION;

	// @formatter:on

	private final class Table {
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

	/**
	 * Value of.
	 *
	 * @param id the id
	 * @return the ip 4 option info
	 */
	public static Ip6IdOption valueOf(int id) {
		int pack = PackId.decodePackId(id);
		if (pack != ProtocolPackTable.PACK_ID_OPTIONS)
			return null;

		int index = PackId.decodeIdOrdinal(id);
		return values()[index];
	}

	/**
	 * Resolve.
	 *
	 * @param type the type
	 * @return the string
	 */
	public static String resolve(Object type) {
		return Enums.resolve(type, Ip6IdOption.class);
	}

	/** The id. */
	private final int id;

	/** The abbr. */
	private final String abbr;

	/** The type. */
	private final int type;

	/** The supplier. */
	private final HeaderSupplier supplier;

	/**
	 * Instantiates a new IPv4 option ID and info.
	 *
	 * @param type the type
	 * @param abbr the abbr
	 */
	Ip6IdOption(int type, String abbr) {
		this.type = type;
		this.abbr = abbr;
		this.id = PackId.encodeId(ProtocolPackTable.OPTS, ordinal(), CORE_CLASS_IPv6_OPTION);
		this.supplier = Other::new;

		Table.MAP_TABLE[type] = this.id;
	}

	/**
	 * Instantiates a new IPv4 option ID and info.
	 *
	 * @param type     the type
	 * @param abbr     the abbr
	 * @param supplier the supplier
	 */
	Ip6IdOption(int type, String abbr, HeaderSupplier supplier) {
		this.type = type;
		this.abbr = abbr;
		this.supplier = supplier;
		this.id = PackId.encodeId(ProtocolPackTable.OPTS, ordinal(), CORE_CLASS_IPv6_OPTION);

		Table.MAP_TABLE[type] = id;
	}

	/**
	 * Gets the extension abbr.
	 *
	 * @return the extension abbr
	 * @see com.slytechs.jnet.protocol.tcpip.ip.IpExtensionId#getOptionAbbr()
	 */
	@Override
	public String getOptionAbbr() {
		return abbr;
	}

	/**
	 * Gets the header id.
	 *
	 * @return the header id
	 * @see com.slytechs.jnet.protocol.tcpip.ip.IpExtensionId#id()
	 */
	@Override
	public int id() {
		return id;
	}

	@Override
	public String abbr() {
		return abbr;
	}

	/**
	 * Gets the extension infos.
	 *
	 * @return the extension infos
	 * @see com.slytechs.jnet.protocol.api.common.HeaderInfo#getOptionInfos()
	 */
	@Override
	public HeaderOptionInfo[] getOptionInfos() {
		return values();
	}

	/**
	 * Gets the parent header id.
	 *
	 * @return the parent header id
	 * @see com.slytechs.jnet.protocol.api.common.HeaderOptionInfo#getParentHeaderId()
	 */
	@Override
	public int getParentHeaderId() {
		return CoreId.CORE_ID_IPv4;
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
		return type;
	}

}