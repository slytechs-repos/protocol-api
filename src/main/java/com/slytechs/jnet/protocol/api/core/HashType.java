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
package com.slytechs.jnet.protocol.api.core;

import java.util.function.IntSupplier;

/**
 * The Enum HashType.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public enum HashType implements IntSupplier {
	
	/** The none. */
	NONE,
	
	/** The tuple2. */
	TUPLE2,
	
	/** The tuple2 sorted. */
	TUPLE2_SORTED,
	
	/** The tuple5. */
	TUPLE5,
	
	/** The tuple5 sorted. */
	TUPLE5_SORTED,
	
	/** The tuple3 gre v0. */
	TUPLE3_GRE_V0,
	
	/** The tuple3 gre v0 sorted. */
	TUPLE3_GRE_V0_SORTED,
	
	/** The tuple3 gtp v0. */
	TUPLE3_GTP_V0,
	
	/** The tuple3 gtp v0 sorted. */
	TUPLE3_GTP_V0_SORTED,
	
	/** The tuple3 gtp v1 v2. */
	TUPLE3_GTP_V1_V2,
	
	/** The tuple3 gtp v1 v2 sorted. */
	TUPLE3_GTP_V1_V2_SORTED,
	
	/** The tuple5 sctp. */
	TUPLE5_SCTP,
	
	/** The tuple5 sctp sorted. */
	TUPLE5_SCTP_SORTED,
	
	/** The last mpls. */
	LAST_MPLS,
	
	/** The all mpls labels. */
	ALL_MPLS_LABELS,
	
	/** The last vlan id. */
	LAST_VLAN_ID,
	
	/** The all vlan ids. */
	ALL_VLAN_IDS,
	
	/** The inner tuple2. */
	INNER_TUPLE2,
	
	/** The inner tuple2 sorted. */
	INNER_TUPLE2_SORTED,
	
	/** The inner tuple5. */
	INNER_TUPLE5,
	
	/** The inner tuple5 sorted. */
	INNER_TUPLE5_SORTED,
	
	/** The round robin. */
	ROUND_ROBIN,

	;

	/**
	 * Gets the as int.
	 *
	 * @return the as int
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	public int getAsInt() {
		return ordinal();
	}

	/**
	 * Value of.
	 *
	 * @param type the type
	 * @return the hash type
	 */
	public static HashType valueOf(int type) {
		return values()[type];
	}
}
