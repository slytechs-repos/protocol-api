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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ip;

import com.slytechs.jnet.protocol.api.meta.MetaResource;

/**
 * IPv6 HOP-BY-HOP Jumbo packet option.
 * <p>
 * A "jumbogram" is an IPv6 packet containing a payload longer than 65,535
 * octets. The IPv6 Jumbo Payload option, provides the means of specifying such
 * large payload lengths.
 * </p>
 */
@MetaResource("ip6-opt-jumbo-payload-meta.json")
public final class Ip6JumboPayloadOption extends Ip6Option {

	public static final int ID = Ip6IdOption.IPv6_ID_OPT_JUMBO_PAYLOAD;

	/**
	 * Instantiates a new IPv6 jumbo payload hop-by-hop option.
	 */
	public Ip6JumboPayloadOption() {
		super(ID);
	}

	/**
	 * Jumbo payload length.
	 * 
	 * <p>
	 * 32-bit unsigned integer. Length of the IPv6 packet in octets, excluding the
	 * IPv6 header but including the Hop-by-Hop Options header and any other
	 * extension headers present. Must be greater than 65,535.
	 * </p>
	 * 
	 * @return 32-bit unsigned integer payload length
	 */
	public int jumboPayloadLength() {
		return buffer().getInt(2);
	}
}