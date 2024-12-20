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

import com.slytechs.jnet.platform.api.util.Enums;
import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.common.HeaderOptionInfo;
import com.slytechs.jnet.protocol.api.common.HeaderSupplier;
import com.slytechs.jnet.protocol.api.common.Other;
import com.slytechs.jnet.protocol.api.pack.PackId;
import com.slytechs.jnet.protocol.api.pack.ProtocolPackTable;
import com.slytechs.jnet.protocol.tcpip.network.Ip4MtuProbeOption;
import com.slytechs.jnet.protocol.tcpip.network.Ip4MtuReplyOption;
import com.slytechs.jnet.protocol.tcpip.network.Ip4Option;
import com.slytechs.jnet.protocol.tcpip.network.Ip4QuickStartOption;
import com.slytechs.jnet.protocol.tcpip.network.Ip4RecordRouteOption;
import com.slytechs.jnet.protocol.tcpip.network.Ip4RouterAlertOption;
import com.slytechs.jnet.protocol.tcpip.network.Ip4SecurityDefunctOption;
import com.slytechs.jnet.protocol.tcpip.network.Ip4TimestampOption;

/**
 * IPv4 option header ID constants.
 */
public enum Ip4IdOptions implements HeaderOptionInfo, PackId, IntSupplier {

	/** A generic base IP option. */
	IP_OPTTION(254, "IP:OPT", Ip4Option::new),

	/** The base IPv4 option. */
	IPv4_OPTION(253, "IPv4:OPT", Ip4Option::new),

	/**
	 * The end of options marker 1-byte option header.
	 * <p>
	 * The IPv4 end-of-options (EOL) option is a special option that is used to
	 * indicate the end of the options field in an IPv4 packet. The EOL option is a
	 * 1-byte option with a type of 0.
	 * </p>
	 */
	END_OF_OPTIONS(0x01, "EOL"),

	/**
	 * The no operation 1-byte header.
	 * <p>
	 * The IPv4 no-operation (NOP) option is a simple option that does nothing. It
	 * is a 1-byte option with a type of 1.
	 * </p>
	 * <p>
	 * The NOP option is used to pad the options field in an IPv4 packet. This can
	 * be useful to ensure that the options field is a multiple of four bytes in
	 * length.
	 * </p>
	 * <p>
	 * The NOP option is a non-critical option in the IPv4 option header. This means
	 * that the IPv4 router can ignore the NOP option if it does not understand it.
	 * </p>
	 */
	NO_OPERATION(0x02, "NOP"),

	/**
	 * The security (deprecated) 3-byte option header with variable data-length
	 * field that contains the security-related information.
	 * <p>
	 * The IPv4 security option is a deprecated option that was used to provide
	 * security for IPv4 packets. The security option is no longer used, as it is
	 * considered to be insecure.
	 * </p>
	 */
	SECURITY_DEFUNCT(0x02, "SEC_DEF", Ip4SecurityDefunctOption::new),

	/** The record route. */
	RECORD_ROUTE(0x07, "RR", Ip4RecordRouteOption::new),

	/** The exp1 measurement. */
	EXP1_MEASUREMENT(0x0A, "ZSU"),

	/** The mtu probe. */
	MTU_PROBE(0x0B, "MTUP", Ip4MtuProbeOption::new),

	/** The mtu reply. */
	MTU_REPLY(0x0C, "MTUR", Ip4MtuReplyOption::new),

	/** The encode. */
	ENCODE(0x0F, "ENC"),

	/** The quick start. */
	QUICK_START(0x19, "QS", Ip4QuickStartOption::new),

	/** The exp1 rfc3692. */
	EXP1_RFC3692(0x1E, "EXP1"),

	/** The timestamp. */
	TIMESTAMP(0x44, "TS", Ip4TimestampOption::new),

	/** The traceroute. */
	TRACEROUTE(0x52, "RT"),

	/** The exp2 rfc3692. */
	EXP2_RFC3692(0x5E, "EXP2"),

	/** The security. */
	SECURITY(0x82, "SEC"),

	/** The loose source route. */
	LOOSE_SOURCE_ROUTE(0x83, "LSR"),

	/** The extended security. */
	EXTENDED_SECURITY(0x85, "E-SEC"),

	/** The commerical ip security. */
	COMMERICAL_IP_SECURITY(0x86, "CIPSO"),

	/** The stream id. */
	STREAM_ID(0x88, "SID"),

	/** The strict source route. */
	STRICT_SOURCE_ROUTE(0x89, "SSR"),

	/** The exp3 access control. */
	EXP3_ACCESS_CONTROL(0x8E, "VISA"),

	/** The imi traffic descriptor. */
	IMI_TRAFFIC_DESCRIPTOR(0x90, "IMITD"),

	/** The extended ip. */
	EXTENDED_IP(0x91, "E-IP"),

	/** The address extension. */
	ADDRESS_EXTENSION(0x93, "E-ADDR"),

	/** The router alert. */
	ROUTER_ALERT(0x94, "RTR_ALT", Ip4RouterAlertOption::new),

	/** The selective directed broadcast. */
	SELECTIVE_DIRECTED_BROADCAST(0x95, "SBD"),

	/** The dynamic packet state. */
	DYNAMIC_PACKET_STATE(0x97, "DPS"),

	/** The upstream multicast packet. */
	UPSTREAM_MULTICAST_PACKET(0x98, "UMP"),

	/** The exp4 rfc3692. */
	EXP4_RFC3692(0x9E, "EXP4"),

	/** The exp5 flow control. */
	EXP5_FLOW_CONTROL(0xCD, "EXP5"),

	/** The exp6 rfc3692. */
	EXP6_RFC3692(0xDE, "EXP6"),

	;

	// @formatter:off
	/** The Constant IPv4_OPTION_TYPE_EOOL. */
	public static final int IPv4_OPTION_TYPE_EOOL      = 0x00;
	
	/** The Constant IPv4_OPTION_TYPE_NOP. */
	public static final int IPv4_OPTION_TYPE_NOP       = 0x01;
	
	/** The Constant IPv4_OPTION_TYPE_SEC_DEF. */
	public static final int IPv4_OPTION_TYPE_SEC_DEF   = 0x02;
	
	/** The Constant IPv4_OPTION_TYPE_RR. */
	public static final int IPv4_OPTION_TYPE_RR        = 0x07;
	
	/** The Constant IPv4_OPTION_TYPE_EXP1_ZSU. */
	public static final int IPv4_OPTION_TYPE_EXP1_ZSU  = 0x0A;
	
	/** The Constant IPv4_OPTION_TYPE_MTUP. */
	public static final int IPv4_OPTION_TYPE_MTUP      = 41; // TODO: or is it 0x0B?
	
	/** The Constant IPv4_OPTION_TYPE_MTUR. */
	public static final int IPv4_OPTION_TYPE_MTUR      = 0x0C;
	
	/** The Constant IPv4_OPTION_TYPE_ENCODE. */
	public static final int IPv4_OPTION_TYPE_ENCODE    = 0x0F;
	
	/** The Constant IPv4_OPTION_TYPE_QS. */
	public static final int IPv4_OPTION_TYPE_QS        = 0x19;
	
	/** The Constant IPv4_OPTION_TYPE_EXP1. */
	public static final int IPv4_OPTION_TYPE_EXP1      = 0x1E;
	
	/** The Constant IPv4_OPTION_TYPE_TS. */
	public static final int IPv4_OPTION_TYPE_TS        = 0x44;
	
	/** The Constant IPv4_OPTION_TYPE_RT. */
	public static final int IPv4_OPTION_TYPE_RT        = 0x52;
	
	/** The Constant IPv4_OPTION_TYPE_EXP2. */
	public static final int IPv4_OPTION_TYPE_EXP2      = 0x5E;
	
	/** The Constant IPv4_OPTION_TYPE_SEC. */
	public static final int IPv4_OPTION_TYPE_SEC       = 0x82;
	
	/** The Constant IPv4_OPTION_TYPE_LSR. */
	public static final int IPv4_OPTION_TYPE_LSR       = 0x83;
	
	/** The Constant IPv4_OPTION_TYPE_E_SEC. */
	public static final int IPv4_OPTION_TYPE_E_SEC     = 0x85;
	
	/** The Constant IPv4_OPTION_TYPE_CIPSO. */
	public static final int IPv4_OPTION_TYPE_CIPSO     = 0x86;
	
	/** The Constant IPv4_OPTION_TYPE_SID. */
	public static final int IPv4_OPTION_TYPE_SID       = 0x88;
	
	/** The Constant IPv4_OPTION_TYPE_SSR. */
	public static final int IPv4_OPTION_TYPE_SSR       = 0x89;
	
	/** The Constant IPv4_OPTION_TYPE_EXP3_VISA. */
	public static final int IPv4_OPTION_TYPE_EXP3_VISA = 0x8E;
	
	/** The Constant IPv4_OPTION_TYPE_IMITD. */
	public static final int IPv4_OPTION_TYPE_IMITD     = 0x90;
	
	/** The Constant IPv4_OPTION_TYPE_E_IP. */
	public static final int IPv4_OPTION_TYPE_E_IP      = 0x91;
	
	/** The Constant IPv4_OPTION_TYPE_E_ADDR. */
	public static final int IPv4_OPTION_TYPE_E_ADDR    = 0x93;
	
	/** The Constant IPv4_OPTION_TYPE_RTRALT. */
	public static final int IPv4_OPTION_TYPE_RTRALT    = 0x94;
	
	/** The Constant IPv4_OPTION_TYPE_SBD. */
	public static final int IPv4_OPTION_TYPE_SBD       = 0x95;
	
	/** The Constant IPv4_OPTION_TYPE_DPS. */
	public static final int IPv4_OPTION_TYPE_DPS       = 0x97;
	
	/** The Constant IPv4_OPTION_TYPE_UMP. */
	public static final int IPv4_OPTION_TYPE_UMP       = 0x98;
	
	/** The Constant IPv4_OPTION_TYPE_EXP4. */
	public static final int IPv4_OPTION_TYPE_EXP4      = 0x9E;
	
	/** The Constant IPv4_OPTION_TYPE_EXP5. */
	public static final int IPv4_OPTION_TYPE_EXP5      = 0xCD;
	
	/** The Constant IPv4_OPTION_TYPE_EXP6. */
	public static final int IPv4_OPTION_TYPE_EXP6      = 0xDE;
	// @formatter:on

	// @formatter:off
	public static final int IP_ID_OPT_HEADER      = 0 | PACK_ID_OPTIONS | CORE_CLASS_IP_OPTION;
	public static final int IPv4_ID_OPT_HEADER    = 1 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_EOOL      = 2 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_NOP       = 3 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_SEC_DEF   = 4 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_RR        = 5 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_EXP1_ZSU  = 6 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_MTUP      = 7 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_MTUR      = 8 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_ENCODE    = 9 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_QS        = 10 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_EXP1      = 11 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_TS        = 12 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_RT        = 13 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_EXP2      = 14 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_SEC       = 15 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_LSR       = 16 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_E_SEC     = 17 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_CIPSO     = 18 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_SID       = 19 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_SSR       = 20 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_EXP3_VISA = 21 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_IMITD     = 22 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_E_IP      = 23 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_E_ADDR    = 24 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_RTRALT    = 25 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_SBD       = 26 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_DPS       = 27 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_UMP       = 28 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_EXP4      = 29 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_EXP5      = 30 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	public static final int IPv4_ID_OPT_EXP6      = 31 | PACK_ID_OPTIONS | CORE_CLASS_IPv4_OPTION;
	// @formatter:on

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

	/**
	 * Value of.
	 *
	 * @param id the id
	 * @return the ip 4 option info
	 */
	public static Ip4IdOptions valueOf(int id) {
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
		return Enums.resolve(type, Ip4IdOptions.class);
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
	Ip4IdOptions(int type, String abbr) {
		this.type = type;
		this.abbr = abbr;
		this.id = PackId.encodeId(ProtocolPackTable.OPTS, ordinal(), CORE_CLASS_IPv4_OPTION);
		this.supplier = Other::new;

		Table.MAP_TABLE[type] = id;
	}

	/**
	 * Instantiates a new IPv4 option ID and info.
	 *
	 * @param type     the type
	 * @param abbr     the abbr
	 * @param supplier the supplier
	 */
	Ip4IdOptions(int type, String abbr, HeaderSupplier supplier) {
		this.type = type;
		this.abbr = abbr;
		this.supplier = supplier;
		this.id = PackId.encodeId(ProtocolPackTable.OPTS, ordinal(), CORE_CLASS_IPv4_OPTION);
		
		Table.MAP_TABLE[type] = id;
	}

	/**
	 * Gets the extension abbr.
	 *
	 * @return the extension abbr
	 * @see com.slytechs.jnet.protocol.tcpip.network.IpExtensionId#getOptionAbbr()
	 */
	@Override
	public String getOptionAbbr() {
		return abbr;
	}

	/**
	 * Gets the header id.
	 *
	 * @return the header id
	 * @see com.slytechs.jnet.protocol.tcpip.network.IpExtensionId#id()
	 */
	@Override
	public int id() {
		return id;
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