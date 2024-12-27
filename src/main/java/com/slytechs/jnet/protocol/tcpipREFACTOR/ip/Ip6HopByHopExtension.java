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

import com.slytechs.jnet.protocol.api.common.HasOption;
import com.slytechs.jnet.protocol.api.common.HeaderNotFound;
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.meta.MetaResource;

/**
 * IPv6 hop-by-hop extension header.
 * <p>
 * The IPv6 hop-by-hop header is an extension header that is used to carry
 * information that needs to be processed by all the nodes that the packet
 * passes through. This header is optional and can be used to carry a variety of
 * options
 * </p>
 * <p>
 * The hop-by-hop header is used by the sender to specify options that need to
 * be processed by all the nodes that the packet passes through. The options are
 * then processed by the routers and nodes that the packet passes through.
 * </p>
 */
@MetaResource("ip6-ext-hop-by-hop-meta.json")
public final class Ip6HopByHopExtension
		extends Ip6OptionsExtension
		implements HasOption<Ip6Option> {

	/** The id. */
	public static int ID = CoreId.CORE_ID_IPv6_EXT_HOP_BY_HOP_OPTIONS;

	/**
	 * Instantiates a new ip 6 hop by hop option.
	 */
	public Ip6HopByHopExtension() {
		super(ID);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HasOption#getOption(com.slytechs.jnet.protocol.Header,
	 *      int)
	 */
	@Override
	public <E extends Ip6Option> E getOption(E extension, int depth) throws HeaderNotFound {
		return super.getOptionHeader(extension, depth);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HasOption#hasOption(int, int)
	 */
	@Override
	public boolean hasOption(int extensionId, int depth) {
		return super.hasOptionHeader(extensionId, depth);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HasOption#peekOption(com.slytechs.jnet.protocol.Header,
	 *      int)
	 */
	@Override
	public <E extends Ip6Option> E peekOption(E extension, int depth) {
		return super.peekOptionHeader(extension, depth);
	}
}