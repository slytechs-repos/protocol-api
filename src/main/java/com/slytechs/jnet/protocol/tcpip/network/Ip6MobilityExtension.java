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

import com.slytechs.jnet.protocol.tcpip.constants.CoreId;

/**
 * IPv4 mobility extension header.
 * <p>
 * The IPv6 mobility header is an extension header that is used to settingsSupport
 * mobility for mobile nodes in IPv6 networks. The mobility header is used to
 * carry information about the mobile node's current location and its home
 * address. This information is used by the home agent to tunnel packets to the
 * mobile node, even if the mobile node is not currently located at its home
 * network.
 * </p>
 * <p>
 * The mobility header is used by the mobile node to send Binding Updates to the
 * home agent. A Binding Update is a message that is used to inform the home
 * agent of the mobile node's current location. The home agent uses the
 * information in the Binding Update to tunnel packets to the mobile node, even
 * if the mobile node is not currently located at its home network.
 * </p>
 */
public final class Ip6MobilityExtension extends Ip6ExtensionHeader {

	/** The id. */
	public static int ID = CoreId.CORE_ID_IPv6_EXT_MOBILITY;

	/**
	 * Instantiates a new ip 6 mobility option.
	 */
	public Ip6MobilityExtension() {
		super(ID);
	}
}