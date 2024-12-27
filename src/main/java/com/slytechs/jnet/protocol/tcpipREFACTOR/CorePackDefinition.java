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
package com.slytechs.jnet.protocol.tcpipREFACTOR;

import java.util.Optional;

import com.slytechs.jnet.protocol.api.common.HeaderInfo;
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.pack.Pack;
import com.slytechs.jnet.protocol.api.pack.PackId;
import com.slytechs.jnet.protocol.api.pack.ProtocolPackTable;

/**
 * Core protocol pack definition.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public final class CorePackDefinition extends Pack<CoreId> {

	/**
	 * Checks if is core.
	 *
	 * @return true, if is core
	 * @see com.slytechs.jnet.protocol.api.pack.Pack#isCore()
	 */
	@Override
	public boolean isCore() {
		return true;
	}

	/** Core Protocol Pack singleton definition. */
	private static final CorePackDefinition SINGLETON = new CorePackDefinition();

	/**
	 * Core pack is always loaded and reference kept as a singleton.
	 *
	 * @return the core protocol pack
	 */
	public static CorePackDefinition singleton() {
		return SINGLETON;
	}

	/**
	 * Pack definitions are designed to be singltons.
	 */
	private CorePackDefinition() {
		super(ProtocolPackTable.CORE, CoreId.values());
	}

	/**
	 * Gets the header.
	 *
	 * @param id the id
	 * @return the header
	 * @see com.slytechs.jnet.protocol.api.pack.Pack#findHeader(int)
	 */
	@Override
	public Optional<HeaderInfo> findHeader(int id) {
		int packId = PackId.decodePackId(id);
		int hdrOrdinal = PackId.decodeIdOrdinal(id);
		if (packId != ProtocolPackTable.PACK_ID_CORE)
			return Optional.empty();

		var headers = CoreId.values();

		if (hdrOrdinal > headers.length)
			return Optional.empty();

		return Optional.of(CoreId.values()[hdrOrdinal]);
	}

}
