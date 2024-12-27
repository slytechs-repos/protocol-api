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
package com.slytechs.jnet.protocol.tcpipREFACTOR.icmp;

import static com.slytechs.jnet.protocol.tcpipREFACTOR.icmp.Icmp6NeighborAdvertisementFlag.*;

import java.util.Set;

import com.slytechs.jnet.platform.api.util.Enums;
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.tcpipREFACTOR.ip.Ip6Address;
import com.slytechs.jnet.protocol.tcpipREFACTOR.ip.IpAddress;
import com.slytechs.jnet.protocol.api.meta.MetaResource;

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
@MetaResource("icmp6-neighbor-advertisement-meta.json")
public final class Icmp6NeighborAdvertisement
		extends Icmp6 {

	public static final int ID = CoreId.CORE_ID_ICMPv6_NEIGHBOR_ADVERTISEMENT;
	public static final int HEADER_LEN = 8 + IpAddress.IPv6_ADDRESS_SIZE;

	public Icmp6NeighborAdvertisement() {
		super(ID);
	}

	@Meta(MetaType.ATTRIBUTE)
	public int reserved() {
		return buffer().getInt(4) & ICMPv6_NA_MASK_RESERVED;
	}

	@Meta
	public Ip6Address targetAddress() {
		return Ip6Address.get(8, buffer());
	}

	@Meta(abbr = "flgs", formatter = Meta.Formatter.HEX_LOWERCASE_0x)
	public int flags() {
		return buffer().getInt(4) & ICMPv6_NA_MASK_FLAG;
	}
	
	@Meta(value = MetaType.ATTRIBUTE, abbr = "flg3")
	public int flags3bits() {
		return ((buffer().getInt(4) & ICMPv6_NA_MASK_FLAG) >> 29) & 0x7;
	}
	
	@Meta(MetaType.ATTRIBUTE)
	public int flagsByte() {
		return flags() >> 28;
	}
	
	@Meta(value = MetaType.ATTRIBUTE, abbr = "r")
	public int flagRouter() {
		return (flags() >> 31) & 0x1;
	}
	
	@Meta(value = MetaType.ATTRIBUTE, abbr = "s")
	public int flagSolicited() {
		return (flags() >> 30) & 0x1;
	}
	
	@Meta(value = MetaType.ATTRIBUTE, abbr = "o")
	public int flagOverride() {
		return (flags() >> 29) & 0x1;
	}

	@Meta(MetaType.ATTRIBUTE)
	public Set<Icmp6NeighborAdvertisementFlag> flagsSet() {
		return Enums.setOf(flags(), Icmp6NeighborAdvertisementFlag.class);
	}

	@Meta(MetaType.ATTRIBUTE)
	public boolean isRouterFlag() {
		return ROUTER.isSet(flags());
	}

	@Meta(MetaType.ATTRIBUTE)
	public boolean isSolicitedFlag() {
		return SOLICITED.isSet(flags());
	}

	@Meta(MetaType.ATTRIBUTE)
	public boolean isOverrideFlag() {
		return OVERRIDE.isSet(flags());
	}

}
