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

import com.slytechs.jnet.protocol.api.address.MacAddress;
import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
@MetaResource("icmp6-option-meta.json")
public class Icmp6Option extends Header {

	@MetaResource("icmp6-opt-ns-source-link-address-meta.json")
	public static class SourceLinkAddress extends Icmp6Option {

		public static int ID = Icmp6IdNsOptions.ICMPv6_ID_OPT_SOURCE_LINK_ADDRESS;

		public SourceLinkAddress() {
			super(ID);
		}

		@Meta
		public MacAddress linkAddress() {
			return MacAddress.get(2, buffer());
		}
		
	}

	public static int ID = Icmp6IdNsOptions.ICMPv6_ID_OPTION;

	public Icmp6Option() {
		super(ID);
	}

	/**
	 * @param id
	 */
	public Icmp6Option(int id) {
		super(id);
	}

	@Meta
	public int type() {
		return (buffer().get(0) & 0xFF);
	}

	@Meta
	public int optionLength() {
		return (buffer().get(1) & 0xFF);
	}

	@Meta(MetaType.ATTRIBUTE)
	public int optionLengthBytes() {
		return optionLength() * 8;
	}
}
