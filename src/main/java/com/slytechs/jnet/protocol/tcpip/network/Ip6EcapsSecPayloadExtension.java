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
 * The IPv6 security header (also known as the Authentication Header or AH) is
 * an extension header that provides connectionless integrity and data origin
 * authentication for IPv6 packets. The AH can also provide protection against
 * packet replays.
 * 
 */
public final class Ip6EcapsSecPayloadExtension extends Ip6ExtensionHeader {

	/** The id. */
	public static int ID = CoreId.CORE_ID_IPv6_EXT_ENCAPS_SEC_PAYLOAD;

	/**
	 * Instantiates a new ip 6 security option.
	 */
	public Ip6EcapsSecPayloadExtension() {
		super(ID);
	}
}