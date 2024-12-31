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

import com.slytechs.jnet.protocol.api.meta.MetaResource;

/**
 * IPv6 HOP-BY-HOP Jumbo packet option.
 * <p>
 * A "jumbogram" is an IPv6 packet containing a payload longer than 65,535
 * octets. The IPv6 Jumbo Payload option, provides the means of specifying such
 * large payload lengths.
 * </p>
 */
@MetaResource("ip6-opt-padN-meta.json")
public final class Ip6PadnOptioin extends Ip6Option {

	public static final int ID = Ip6IdOption.IPv6_ID_OPT_PADN;

	/**
	 * Instantiates a new IPv6 jumbo payload hop-by-hop option.
	 */
	public Ip6PadnOptioin() {
		super(ID);
	}

}