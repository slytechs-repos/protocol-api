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

import com.slytechs.jnet.protocol.api.common.OptionsHeader;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.tcpipREFACTOR.constants.CoreId;

/**
 * Base class for all IPv6 extension headers with options.
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
@MetaResource("ip6-ext-opt-meta.json")
public sealed class Ip6OptionsExtension
		extends OptionsHeader
		implements Ip6Extension
		permits Ip6HopByHopExtension, Ip6DestinationExtension {

	public static final int ID = CoreId.CORE_ID_IPv6_EXTENSION;

	/**
	 * Instantiates a new ip 6 option.
	 */
	public Ip6OptionsExtension() {
		super(ID);
	}

	/**
	 * Instantiates a new IP v6 option.
	 *
	 * @param id the id
	 */
	protected Ip6OptionsExtension(int id) {
		super(id);
	}

	/**
	 * Gets the IPv6 extension header length field value.
	 * <p>
	 * The IPv6 extension header length field is a 8-bit field that specifies the
	 * length of the extension header in 8-byte units. The extension header length
	 * field is located at the beginning of each extension header.
	 * </p>
	 * 
	 * @return the unsigned 8-bit header length field value
	 *
	 * @return the int
	 */
	@Override
	@Meta
	public int extensionLength() {
		return Byte.toUnsignedInt(buffer().get(1));
	}

	@Override
	@Meta(MetaType.ATTRIBUTE)
	public int extensionLengthBytes() {
		return (Byte.toUnsignedInt(buffer().get(1)) << 3) + 8;
	}

	/**
	 * Gets the IPv6 extension next header field value.
	 * <p>
	 * The IPv6 extension next header field is a 8-bit field that specifies the type
	 * of the next header in the IPv6 packet. The next header field is located at
	 * the end of each extension header.
	 * </p>
	 *
	 * @return the unsigned 8-bit next header field value
	 */
	@Override
	@Meta
	public int nextHeader() {
		return Byte.toUnsignedInt(buffer().get(0));
	}

}
