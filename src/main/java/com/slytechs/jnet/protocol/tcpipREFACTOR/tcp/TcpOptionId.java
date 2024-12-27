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
package com.slytechs.jnet.protocol.tcpipREFACTOR.tcp;

import static com.slytechs.jnet.protocol.api.pack.ProtocolPackTable.*;

import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.common.HeaderOptionInfo;
import com.slytechs.jnet.protocol.api.common.HeaderSupplier;
import com.slytechs.jnet.protocol.api.pack.PackId;
import com.slytechs.jnet.protocol.api.pack.ProtocolPackTable;
import com.slytechs.jnet.protocol.tcpipREFACTOR.constants.CoreConstants;
import com.slytechs.jnet.protocol.tcpipREFACTOR.constants.CoreId;

/**
 * TCP header option ID constants.
 */
public enum TcpOptionId implements HeaderOptionInfo, PackId {

	/** The option. */
	OPTION(254, "OPT", TcpOption::new),

	/** The eol. */
	EOL(CoreConstants.TCP_OPTION_KIND_EOL, "EOL", TcpEolOption::new),

	/** The nop. */
	NOP(CoreConstants.TCP_OPTION_KIND_NOP, "NOP", TcpNopOption::new),

	/** The mss. */
	MSS(CoreConstants.TCP_OPTION_KIND_MSS, "MSS", TcpMssOption::new),

	/** The win scale. */
	WIN_SCALE(CoreConstants.TCP_OPTION_KIND_WIN_SCALE, "WIN", TcpWindowScaleOption::new),

	/** The sack. */
	SACK_PERMITTED(CoreConstants.TCP_OPTION_KIND_SACK_PERMITTED, "SACK_PERM", TcpSackPermittedOption::new),

	/** The sack. */
	SACK(CoreConstants.TCP_OPTION_KIND_SACK, "SACK", TcpSackOption::new),

	/** The timestamp. */
	TIMESTAMP(CoreConstants.TCP_OPTION_KIND_TIMESTAMP, "TS", TcpTimestampOption::new),

	/** The fastopen. */
	FASTOPEN(CoreConstants.TCP_OPTION_KIND_FASTOPEN, "FAST", TcpFastOpenOption::new),

	;

	/** The Constant TCP_OPT_CLASS_OPTION. */
	public static final int TCP_OPT_CLASS_OPTION = CoreId.CORE_CLASS_TCP_OPT;

	// @formatter:off
	/** The Constant TCP_OPT_ID_OPTION. */
	public static final int TCP_OPT_ID_OPTION    = 0 | PACK_ID_OPTIONS | TCP_OPT_CLASS_OPTION;
	
	/** The Constant TCP_OPT_ID_EOL. */
	public static final int TCP_OPT_ID_EOL       = 1 | PACK_ID_OPTIONS | TCP_OPT_CLASS_OPTION;
	
	/** The Constant TCP_OPT_ID_NOP. */
	public static final int TCP_OPT_ID_NOP       = 2 | PACK_ID_OPTIONS | TCP_OPT_CLASS_OPTION;
	
	/** The Constant TCP_OPT_ID_MSS. */
	public static final int TCP_OPT_ID_MSS       = 3 | PACK_ID_OPTIONS | TCP_OPT_CLASS_OPTION;
	
	/** The Constant TCP_OPT_ID_WIN_SCALE. */
	public static final int TCP_OPT_ID_WIN_SCALE = 4 | PACK_ID_OPTIONS | TCP_OPT_CLASS_OPTION;
	
	/** The Constant TCP_OPT_ID_SACK. */
	public static final int TCP_OPT_ID_SACK_PERMITTED      = 5 | PACK_ID_OPTIONS | TCP_OPT_CLASS_OPTION;
	
	/** The Constant TCP_OPT_ID_SACK. */
	public static final int TCP_OPT_ID_SACK      = 6 | PACK_ID_OPTIONS | TCP_OPT_CLASS_OPTION;
	
	/** The Constant TCP_OPT_ID_TIMESTAMP. */
	public static final int TCP_OPT_ID_TIMESTAMP = 7 | PACK_ID_OPTIONS | TCP_OPT_CLASS_OPTION;
	
	/** The Constant TCP_OPT_ID_FASTOPEN. */
	public static final int TCP_OPT_ID_FASTOPEN  = 8 | PACK_ID_OPTIONS | TCP_OPT_CLASS_OPTION;
	// @formatter:on

	/** The Constant MAP_TABLE. */
	private static final int[] MAP_TABLE = new int[256];

	static {
		for (TcpOptionId opt : values())
			MAP_TABLE[opt.type] = opt.id;
	}

	/**
	 * Map kind to id.
	 *
	 * @param type the type
	 * @return the int
	 */
	public static int mapKindToId(int type) {
		return MAP_TABLE[type];
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
	 * Instantiates a new tcp option info.
	 *
	 * @param type     the type
	 * @param abbr     the abbr
	 * @param supplier the supplier
	 */
	TcpOptionId(int type, String abbr, HeaderSupplier supplier) {
		this.type = type;
		this.abbr = abbr;
		this.id = PackId.encodeId(ProtocolPackTable.OPTS, ordinal(), TCP_OPT_CLASS_OPTION);
		this.supplier = supplier;
	}

	/**
	 * Value of.
	 *
	 * @param id the id
	 * @return the tcp option info
	 */
	public static TcpOptionId valueOf(int id) {
		int pack = PackId.decodePackId(id);
		if (pack != ProtocolPackTable.PACK_ID_OPTIONS)
			return null;

		int index = PackId.decodeIdOrdinal(id);
		return values()[index];
	}

	/**
	 * Gets the parent header id.
	 *
	 * @return the parent header id
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.IpExtensionId#getParentHeaderId()
	 */
	@Override
	public int getParentHeaderId() {
		return CoreId.CORE_ID_TCP;
	}

	/**
	 * Gets the extension abbr.
	 *
	 * @return the extension abbr
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.IpExtensionId#getOptionAbbr()
	 */
	@Override
	public String getOptionAbbr() {
		return abbr;
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

}
