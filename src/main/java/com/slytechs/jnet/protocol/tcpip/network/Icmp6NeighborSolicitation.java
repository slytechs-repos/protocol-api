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

import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.tcpip.constants.CoreId;

/**
 * The Neighbor Solicitation (NS) message header.
 * 
 * <p>
 * The Neighbor Solicitation (NS) message is used to request the link-layer
 * address of a neighbor node that is known to have a particular IPv6 address.
 * The NS message is sent as a multicast message to the solicited-node multicast
 * address for the target address. The solicited-node multicast address is
 * formed by appending the FF02::1 suffix to the target address.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
@MetaResource("icmp6-neighbor-solicitation-meta.json")
public final class Icmp6NeighborSolicitation
		extends Icmp6OptionsHeader<Icmp6Option> {

	public static final int ID = CoreId.CORE_ID_ICMPv6_NEIGHBOR_SOLICITATION;
	public static final int HEADER_LEN = 8 + IpAddress.IPv6_ADDRESS_SIZE;

	public Icmp6NeighborSolicitation() {
		super(ID);
	}

	@Meta
	public int reserved() {
		return buffer().getInt(4);
	}

	@Meta
	public Ip6Address targetAddress() {
		return Ip6Address.get(8, buffer());
	}

}
