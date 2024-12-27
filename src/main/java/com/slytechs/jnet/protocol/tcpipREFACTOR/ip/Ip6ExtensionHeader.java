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

import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.meta.Meta;

/**
 * Base class for all IPv6 extension headers with no options.
 * <p>
 * IPv6 extended headers are a set of optional headers that can be used to
 * provide additional information about an IPv6 packet. Extended headers are not
 * always present in an IPv6 packet, and they are only processed by the nodes
 * that need the information they contain.
 * </p>
 * <h2>Extension Header Order (from RFC 2460)</h2>
 * <p>
 * When more than one extension header is used in the same packet, it is
 * recommended that those headers appear in the following order:
 * </p>
 * <ul>
 * <li>IPv6 header</li>
 * <li>Hop-by-Hop Options header</li>
 * <li>Destination Options header</li>
 * <li>Routing header</li>
 * <li>Fragment header</li>
 * <li>Authentication header</li>
 * <li>Encapsulating Security Payload header</li>
 * <li>Destination Options header</li>
 * <li><i>upper-layer</i> header</li>
 * </ul>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
@Meta
public sealed class Ip6ExtensionHeader
		extends Header
		implements Ip6Extension
		permits Ip6AuthHeaderExtension, Ip6FragmentExtension, Ip6HostIdentityExtension,
		Ip6MobilityExtension, Ip6RoutingExtension, Ip6EcapsSecPayloadExtension, Ip6Shim6Extension {

	public static final int ID = CoreId.CORE_ID_IPv6_EXTENSION;

	/**
	 * Instantiates a new ip 6 option.
	 */
	public Ip6ExtensionHeader() {
		super(ID);
	}

	/**
	 * Instantiates a new IP v6 option.
	 *
	 * @param id the id
	 */
	protected Ip6ExtensionHeader(int id) {
		super(id);
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.Ip6Extension#extensionLength()
	 */
	@Override
	public int extensionLength() {
		return Byte.toUnsignedInt(buffer().get(1));
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.Ip6Extension#extensionLengthBytes()
	 */
	@Override
	public int extensionLengthBytes() {
		return (Byte.toUnsignedInt(buffer().get(1)) << 3) + 8;
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.ip.Ip6Extension#nextHeader()
	 */
	@Override
	public int nextHeader() {
		return Byte.toUnsignedInt(buffer().get(0));
	}
}
