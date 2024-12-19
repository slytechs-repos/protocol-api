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
package com.slytechs.jnet.protocol.api.common;

import static com.slytechs.jnet.protocol.api.pack.PackId.*;

import java.util.function.Supplier;

import com.slytechs.jnet.protocol.api.pack.PackId;
import com.slytechs.jnet.protocol.api.pack.ProtocolPackTable;

/**
 * Packet header implementation.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
class PackHeaderFactory implements HeaderFactory {

	/**
	 * Private header supplier
	 * 
	 * @author Sly Technologies Inc
	 * @author repos@slytechs.com
	 */
	private interface HeaderSupplier extends Supplier<Header> {
		HeaderSupplier EMPTY = () -> null;

		@Override
		Header get();
	}

	/**
	 * Lazy supplier doesn't load any classes until actually invoked. Also only hold
	 * weak references to classes so that they can be GCed when not needed anymore.
	 */
	private static class LazySupplier implements HeaderSupplier {

		private final HeaderInfo info;
		private Header header;

		/**
		 * Lazy allocated supplier.
		 * 
		 * @param info
		 */
		LazySupplier(HeaderInfo info) {
			this.info = info;
		}

		@Override
		public Header get() {
			if (header == null)
				header = info.newHeaderInstance();

			return header;
		}
	}

	private final HeaderSupplier[] primary;
	private final HeaderSupplier[][] extension;
	private final int pack;

	/**
	 * 
	 * @param pack
	 * @param packIds
	 */
	public PackHeaderFactory(int pack, HeaderInfo[] packIds) {
		this.pack = pack;
		this.primary = initializeProtocolTable(packIds);
		this.extension = initializeExtensionTable(packIds);
	}

	/**
	 * Get a header supplier if one is available for the header id.
	 * 
	 * @param id header id
	 * @return supplier or null if not found
	 */
	private HeaderSupplier lookupSupplier(int id) {
		int pack = PackId.decodePackId(id);
		int ordinal = PackId.decodeIdOrdinal(id);

		if (pack != this.pack)
			return null;

		return primary[ordinal];
	}

	/**
	 * Get a header supplier if one is available for the header id.
	 * 
	 * @param primaryId   primary header id
	 * @param extensionId extension header id
	 * @return supplier or null if not found
	 */
	private HeaderSupplier lookupSupplier(int primaryId, int extensionId) {
		int pack0 = PackId.decodePackId(primaryId);
		int ordinal0 = PackId.decodeIdOrdinal(primaryId);
		int pack1 = PackId.decodePackId(extensionId);
		int ordinal1 = PackId.decodeIdOrdinal(extensionId);

		if ((pack0 != this.pack) || (pack1 != ProtocolPackTable.PACK_ID_OPTIONS))
			return null;

		HeaderSupplier[] table = extension[ordinal0];
		if (table == null)
			return null;

		return table[ordinal1];
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HeaderFactory#get(int)
	 */
	@Override
	public Header get(int id) {
		HeaderSupplier supplier = lookupSupplier(id);
		if (supplier == null)
			return null;

		return supplier.get();
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.common.HeaderFactory#getExtension(int, int)
	 */
	@Override
	public Header getExtension(int primaryId, int extensionId) {
		HeaderSupplier supplier = lookupSupplier(primaryId, extensionId);
		if (supplier == null)
			return null;

		return supplier.get();
	}

	private HeaderSupplier[][] initializeExtensionTable(HeaderInfo[] protocolIds) {
		HeaderSupplier[][] table = new HeaderSupplier[PACK_MAXCOUNT_ORDINALS][];

		/* Initialize entire table even for out of range values for quick lookups */
		for (int i = 0; i < protocolIds.length; i++) {

			HeaderOptionInfo[] exts = protocolIds[i].getOptionInfos();
			if (exts.length == 0)
				continue;

			table[i] = initializeProtocolTable(exts);
		}

		return table;
	}

	private HeaderSupplier[] initializeProtocolTable(HeaderInfo[] headerInfos) {
		HeaderSupplier[] table = new HeaderSupplier[headerInfos.length];

		/* Initialize entire table even for out of range values for quick lookups */
		for (int i = 0; i < table.length; i++)
			table[i] = HeaderSupplier.EMPTY;

		for (int i = 0; i < headerInfos.length; i++)
			table[i] = new LazySupplier(headerInfos[i]);

		return table;
	}

}
