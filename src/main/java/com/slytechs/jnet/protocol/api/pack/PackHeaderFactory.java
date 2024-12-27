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
package com.slytechs.jnet.protocol.api.pack;

import static com.slytechs.jnet.protocol.api.pack.PackId.*;

import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.common.HeaderFactory;
import com.slytechs.jnet.protocol.api.common.HeaderInfo;
import com.slytechs.jnet.protocol.api.core.CoreId;

/**
 * A factory for creating PackHeader objects.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public final class PackHeaderFactory implements HeaderFactory {

	/**
	 * The Class LazySupplier.
	 */
	private static class LazySupplier {

		/** The Constant EMPTY. */
		private static final LazySupplier EMPTY = new LazySupplier(null) {

			/**
			 * @see com.slytechs.jnet.protocol.api.common.protocol.pack.jnet.protocol.packet.PackHeaderFactory.LazySupplier#get()
			 */
			@Override
			Header get() {
				return null;
			}

		};

		/** The allocated header. */
		private Header allocatedHeader;

		/** The info. */
		private final HeaderInfo info;

		/**
		 * Instantiates a new lazy supplier.
		 *
		 * @param info the info
		 */
		LazySupplier(HeaderInfo info) {
			this.info = info;
		}

		/**
		 * Gets the.
		 *
		 * @return the header
		 */
		Header get() {
			if (allocatedHeader == null)
				this.allocatedHeader = info.newHeaderInstance();

			return allocatedHeader;
		}

	}

	/** The Constant table. */
	private static final LazySupplier[] table = new LazySupplier[PACK_MAXCOUNT_ORDINALS];

	static {

		/* Initialize entire table even for out of range values for quick lookups */
		for (int i = 0; i < table.length; i++)
			table[i] = LazySupplier.EMPTY;

		CoreId[] core = CoreId.values();
		for (int i = 0; i < core.length; i++)
			table[i] = new LazySupplier(core[i]);

	}

	/**
	 * Instantiates a new pack header factory.
	 */
	public PackHeaderFactory() {

	}

	/**
	 * Gets the.
	 *
	 * @param id the id
	 * @return the header
	 * @see com.slytechs.jnet.protocol.api.common.HeaderFactory#get(int)
	 */
	@Override
	public Header get(int id) {
		int pack = PackId.decodePackId(id);
		int ordinal = PackId.decodeIdOrdinal(id);

		if (pack != ProtocolPackTable.PACK_ID_CORE)
			return null;

		return table[ordinal].get();
	}

}
