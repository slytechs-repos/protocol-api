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
package com.slytechs.jnet.protocol.core.network;

import com.slytechs.jnet.protocol.core.constants.Ip4IdOptions;

/**
 * IPv4 traceroute option header.
 * <p>
 * The traceroute option is used to trace the path that a packet takes to reach
 * its destination. The traceroute option works by sending a packet with a TTL
 * of 1. When the packet reaches a router, the router decrements the TTL of the
 * packet by 1. If the TTL of the packet is 0, the router sends an ICMP Time
 * Exceeded message back to the source of the packet. The source of the packet
 * then knows that the router is the first hop in the path to the destination.
 * The source of the packet then sends a packet with a TTL of 2. The process
 * repeats until the source of the packet reaches the destination.
 * </p>
 * <p>
 * The traceroute option is supported by most modern operating systems and
 * routers. However, it is important to note that not all routers settingsSupport the
 * traceroute option. As a result, it is important to check the documentation
 * for the routers that will be used to ensure that the traceroute option is
 * supported.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public final class Ip4TracerouteOption extends Ip4Option {

	/** The IPv4 traceroute option header ID constant. */
	public static final int ID = Ip4IdOptions.IPv4_ID_OPT_RT;

	/**
	 * IPv4 traceroute option header.
	 */
	public Ip4TracerouteOption() {
		super(ID);
	}

	public int tracerouteId() {
		return Short.toUnsignedInt(buffer().getShort(2));
	}

	public int hopCountOutbound() {
		return Short.toUnsignedInt(buffer().getShort(4));
	}

	public int hoptCountReturn() {
		return Short.toUnsignedInt(buffer().getShort(6));
	}

	public byte[] originatorIpAddress() {
		byte[] dst = new byte[IpAddress.IPv4_ADDRESS_SIZE];

		buffer().get(8, dst);

		return dst;
	}
}
