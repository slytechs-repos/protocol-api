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

import com.slytechs.jnet.protocol.tcpipREFACTOR.constants.CoreId;

/**
 * IPv6 identity extension header.
 * <p>
 * The IPv6 identity header is an extension header that is used to provide a
 * unique identifier for an IPv6 packet.
 * </p>
 * <p>
 * The identity header is used by the sender to specify an identifier for the
 * packet. The identifier is then copied to the destination node. The
 * destination node can then use the identifier to track the path that the
 * packet took through the network, prevent attackers from spoofing the source
 * address of the packet, and verify the integrity of the packet.
 * </p>
 */
public final class Ip6HostIdentityExtension extends Ip6ExtensionHeader {

	/** The id. */
	public static int ID = CoreId.CORE_ID_IPv6_EXT_HOST_IDENTITY;

	/**
	 * Instantiates a new ip 6 identity option.
	 */
	public Ip6HostIdentityExtension() {
		super(ID);
	}
}