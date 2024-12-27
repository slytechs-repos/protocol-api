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

import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
@MetaResource("ip6-option-meta.json")
public sealed class Ip6Option extends IpOption
		permits Ip6RouterAlertOption, Ip6JumboPayloadOption, Ip6Pad1Option, Ip6PadnOptioin {

	public static final int ID = Ip6IdOption.IPv6_ID_OPT_HEADER;

	/**
	 * Instantiates a new IPv4 option header.
	 */
	public Ip6Option() {
		super(ID);
	}

	/**
	 * Instantiates a new IPv4 sub-classed option header.
	 *
	 * @param id the IPv4 sub-classed option ID constant
	 */
	protected Ip6Option(int id) {
		super(id);
	}

	/**
	 * Gets the IPv4 option type field value.
	 * <p>
	 * The IPv4 option type field is a 1-byte field that identifies the type of
	 * option. The option type field is located at the beginning of each IPv4
	 * option.
	 * </p>
	 * <p>
	 * The option type field is used by the IPv4 router to determine how to process
	 * the option. The router will look up the option type in a table to determine
	 * the appropriate processing instructions.
	 * </p>
	 * 
	 * @return the unsigned 8-bit option type field
	 */
	@Meta
	public int type() {
		return Byte.toUnsignedInt(buffer().get(0));
	}

	/**
	 * Gets the IPv4 option length field value.
	 * <p>
	 * The IPv4 option length field is a 1-byte field that specifies the length of
	 * the option in bytes. The option length field is located immediately after the
	 * option type field in each IPv4 option.
	 * </p>
	 * <p>
	 * The option length field is used by the IPv4 router to determine how much of
	 * the option to process. The router will read the option length field and then
	 * process the next option length bytes of the option.
	 * </p>
	 * 
	 * @return the unsigned 8-bit option header length
	 */
	@Override
	@Meta
	public int optionDataLength() {
		return Byte.toUnsignedInt(buffer().get(1));
	}

	/**
	 * Extension length bytes.
	 *
	 * @return the int
	 */
	@Meta(MetaType.ATTRIBUTE)
	public int extensionLengthBytes() {
		return optionDataLength() + 2;
	}
}
