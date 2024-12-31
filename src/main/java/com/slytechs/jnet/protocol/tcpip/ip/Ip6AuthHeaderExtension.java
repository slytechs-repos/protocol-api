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

import com.slytechs.jnet.protocol.api.core.CoreId;

/**
 * IPv6 authentication extension header (AH).
 * <p>
 * The IPv6 authentication header (AH) is an extension header that provides
 * connectionless integrity and data origin authentication for IPv6 packets. The
 * AH can also provide protection against packet replays.
 * </p>
 * <p>
 * The AH is used by the sender to calculate an authentication value (AV) for
 * the packet. The AV is calculated over the following fields:
 * </p>
 * <ul>
 * <li>The IPv6 header</li>
 * <li>The extension headers that precede the AH</li>
 * <li>The upper-layer protocol data</li>
 * </ul>
 * <p>
 * The AV is then placed in the AH. When the packet is received, the receiver
 * calculates its own AV for the packet. If the receiver's AV does not match the
 * AV in the AH, the packet is discarded.
 * </p>
 */
public final class Ip6AuthHeaderExtension extends Ip6ExtensionHeader {

	/** The id. */
	public static int ID = CoreId.CORE_ID_IPv6_EXT_AUTH_HEADER;

	/**
	 * Instantiates a new ip 6 authentication option.
	 */
	public Ip6AuthHeaderExtension() {
		super(ID);
	}
}