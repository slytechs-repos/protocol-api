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
package com.slytechs.jnet.protocol.tcpipREFACTOR.icmp;

import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;

/**
 * The Internet Control Message Protocol (ICMP) version 6 header.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
@MetaResource("icmp6-meta.json")
public sealed class Icmp6
		extends Icmp
		permits Icmp6Echo, Icmp6Mlr2, Icmp6OptionsHeader, Icmp6NeighborAdvertisement {

	/** The Constant ID. */
	public static final int ID = CoreId.CORE_ID_ICMPv6;

	/**
	 * Instantiates a new icmp 6.
	 */
	public Icmp6() {
		super(ID);
	}

	protected Icmp6(int id) {
		super(id);
	}

	@Meta
	@Override
	public int type() {
		return super.type();
	}

	@Override
	@Meta
	public int code() {
		return super.code();
	}

	@Override
	@Meta(formatter = Meta.Formatter.HEX_LOWERCASE_0x)
	public int checksum() {
		return super.checksum();
	}
}
