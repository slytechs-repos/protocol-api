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
package com.slytechs.jnet.protocol.tcpip.network;

import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.tcpip.constants.Ip4IdOptions;

/**
 * Base class for all IP options in IPv4 and extension headers in IPv6.
 */
@MetaResource("ip-opt-meta.json")
public sealed class IpOption extends Header
		permits Ip4Option, Ip6Option {

	/** The base IP (for either v4 or v6) option ID constant. */
	public static final int ID = Ip4IdOptions.IP_ID_OPT_HEADER;

	/**
	 * Instantiates a new IP option header.
	 */
	public IpOption() {
		super(ID);
	}

	/**
	 * Instantiates a new IP option header.
	 *
	 * @param id the sub-classed IP option ID constant
	 */
	protected IpOption(int id) {
		super(id);
	}

	public int optionDataLength() {
		return Byte.toUnsignedInt(buffer().get(1));
	}
}