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
 * IPv6 destination extension header.
 * <p>
 * The IPv6 destination header is an extension header that can be used to
 * provide additional information about an IPv6 packet. It is used to specify
 * delivery or processing options at either intermediate destinations or the
 * final destination.
 * </p>
 * <p>
 * The destination header is used by the sender to specify delivery or
 * processing options for the packet. The options are then processed by the
 * routers and nodes that the packet passes through.
 * </p>
 */
public final class Ip6DestinationExtension extends Ip6OptionsExtension {

	/** The id. */
	public static int ID = CoreId.CORE_ID_IPv6_EXT_DEST_OPTIONS;

	/**
	 * Instantiates a new ip 6 destination option.
	 */
	public Ip6DestinationExtension() {
		super(ID);
	}
}