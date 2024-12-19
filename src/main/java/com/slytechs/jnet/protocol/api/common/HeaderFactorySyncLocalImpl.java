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

import com.slytechs.jnet.protocol.api.pack.PackId;

/**
 * The Class HeaderFactorySyncLocalImpl.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
class HeaderFactorySyncLocalImpl implements HeaderFactory {

	/** The allocator. */
	private final HeaderFactory allocator = HeaderFactory.newInstance();

	/** The cache 0. */
	private final Header[][] cache0 = new Header[PACK_MAXCOUNT_PACKS][PACK_MAXCOUNT_ORDINALS];
	
	/** The cache 1. */
	private final Header[][][] cache1 = new Header[PACK_MAXCOUNT_PACKS][PACK_MAXCOUNT_ORDINALS][PACK_MAXCOUNT_ORDINALS];

	/**
	 * Gets the.
	 *
	 * @param id the id
	 * @return the header
	 * @see com.slytechs.jnet.protocol.api.common.HeaderFactory#get(int)
	 */
	@Override
	public Header get(int id) {
		int proto = PackId.decodeIdOrdinal(id);
		int pack = PackId.decodePackOrdinal(id);

		Header header = cache0[pack][proto];
		if (header == null)
			cache0[pack][proto] = header = allocator.get(id);

		header.getLock().lock();

		return header;
	}

	/**
	 * Checks if is release supported.
	 *
	 * @return true, if is release supported
	 * @see com.slytechs.jnet.protocol.api.common.HeaderFactory#isReleaseSupported()
	 */
	@Override
	public boolean isReleaseSupported() {
		return true;
	}

	/**
	 * Release.
	 *
	 * @param header the header
	 * @see com.slytechs.jnet.protocol.api.common.HeaderFactory#release(com.slytechs.jnet.protocol.api.common.Header)
	 */
	@Override
	public void release(Header header) {
		header.unbind();

			header.getLock().unlock();

		if (allocator.isReleaseSupported())
			allocator.release(header);
	}

	/**
	 * Gets the extension.
	 *
	 * @param primaryId   the primary id
	 * @param extensionId the extension id
	 * @return the extension
	 * @see com.slytechs.jnet.protocol.api.common.HeaderFactory#getExtension(int, int)
	 */
	@Override
	public Header getExtension(int primaryId, int extensionId) {
		int proto0 = PackId.decodeIdOrdinal(primaryId);
		int pack0 = PackId.decodePackOrdinal(primaryId);
		int proto1 = PackId.decodeIdOrdinal(extensionId);

		Header header = cache1[pack0][proto0][proto1];
		if (header == null)
			cache1[pack0][proto0][proto1] = header = allocator.get(primaryId);

		header.getLock().lock();

		return header;
	}

}
