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

import com.slytechs.jnet.platform.api.util.Enums;
import com.slytechs.jnet.protocol.api.common.HeaderOptionInfo;
import com.slytechs.jnet.protocol.api.common.HeaderSupplier;
import com.slytechs.jnet.protocol.api.pack.PackId;
import com.slytechs.jnet.protocol.api.pack.ProtocolPackTable;
import com.slytechs.jnet.protocol.tcpip.network.Icmp6Mlr2;
import com.slytechs.jnet.protocol.tcpip.network.Icmp6Mlr2.MulticastAddressRecord;

/**
 * MLDv2 record type (defined in RFC 3810).
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public enum Icmp6Mlr2RecordType implements IntSupplier, HeaderOptionInfo, PackId {
	MODE_IS_INCLUDE(1, "MODEIN", Icmp6Mlr2.ModeIncludeRecord::new),
	MODE_IS_EXCLUDE(2, "MODEEX", Icmp6Mlr2.ModeExcludeRecord::new),
	CHANGE_TO_INCLUDE(3, "CHGIN", Icmp6Mlr2.ChangeIncludeRecord::new),
	CHANGE_TO_EXCLUDE(4, "CHGEX", Icmp6Mlr2.ChangeExcludeRecord::new),
	ALLOW_NEW_SOURCES(5, "ALLOW", Icmp6Mlr2.AllowNewSourcesRecord::new),
	BLOCK_OLD_SOURCES(6, "BLOCK", Icmp6Mlr2.BlockOldSourcesRecord::new)

	;

	// @formatter:off
	private static final int CLASS_MLRv2_RECORD                   = 0;
	public static final int ICMPv6_ID_OPT_MLRv2_RECORD            = ID_ORDINAL_CLASS | ProtocolPackTable.PACK_ID_OPTIONS | CLASS_MLRv2_RECORD;
	public static final int ICMPv6_ID_OPT_MLRv2_MODE_IS_INCLUDE   = 0 | ProtocolPackTable.PACK_ID_OPTIONS | CLASS_MLRv2_RECORD;
	public static final int ICMPv6_ID_OPT_MLRv2_MODE_IS_EXCLUDE   = 1 | ProtocolPackTable.PACK_ID_OPTIONS | CLASS_MLRv2_RECORD;
	public static final int ICMPv6_ID_OPT_MLRv2_CHANGE_TO_INCLUDE = 2 | ProtocolPackTable.PACK_ID_OPTIONS | CLASS_MLRv2_RECORD;
	public static final int ICMPv6_ID_OPT_MLRv2_CHANGE_TO_EXCLUDE = 3 | ProtocolPackTable.PACK_ID_OPTIONS | CLASS_MLRv2_RECORD;
	public static final int ICMPv6_ID_OPT_MLRv2_ALLOW_NEW_SOURCES = 4 | ProtocolPackTable.PACK_ID_OPTIONS | CLASS_MLRv2_RECORD;
	public static final int ICMPv6_ID_OPT_MLRv2_BLOCK_OLD_SOURCES = 5 | ProtocolPackTable.PACK_ID_OPTIONS | CLASS_MLRv2_RECORD;
	// @formatter:on

	public static String resolve(Object type) {
		return Enums.resolve(type, Icmp6Mlr2RecordType.class);
	}

	private final int type;
	private String abbr;
	private HeaderSupplier supplier;
	private int id;

	/**
	 * @param i
	 */
	Icmp6Mlr2RecordType(int type, String abbr, HeaderSupplier supplier) {
		this.type = type;
		this.abbr = abbr;
		this.supplier = supplier;
		this.id = PackId.encodeId(ProtocolPackTable.OPTS, ordinal(), CLASS_MLRv2_RECORD);
	}

	/**
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return type;
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
	public Icmp6Mlr2.MulticastAddressRecord newHeaderInstance() {
		return (MulticastAddressRecord) supplier.newHeaderInstance();
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HeaderOptionInfo#getOptionAbbr()
	 */
	@Override
	public String getOptionAbbr() {
		return abbr;
	}

	@Override
	public String abbr() {
		return abbr;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HeaderOptionInfo#getParentHeaderId()
	 */
	@Override
	public int getParentHeaderId() {
		return CoreId.CORE_ID_ICMPv6_MULTICAST_LISTENER_REPORTv2;
	}
}
