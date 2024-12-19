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

import com.slytechs.jnet.protocol.api.descriptor.CompactDescriptor;
import com.slytechs.jnet.protocol.api.descriptor.HeaderDescriptor;

/**
 * Interface which defines specific lookup operations within a descriptor for
 * different protocol headers.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public interface HeaderLookup {

	/**
	 * Checks if is header extension supported.
	 *
	 * @return true, if is header extension supported
	 */
	boolean isHeaderExtensionSupported();

	/**
	 * List headers recorded within a descriptor or lookup source.
	 *
	 * @return an array of {@link CompactDescriptor} encoded header IDs and other
	 *         auxilary information such as offset and length of the header.
	 */
	long[] listHeaders();

	/**
	 * Lookup a header extension and at specific inner tunnel depth.
	 *
	 * @param headerId        the primary header id
	 * @param extId           the header extension id
	 * @param depth           the tunnel inner depth
	 * @param recordIndexHint the record index hint, this information is typically
	 *                        index of where info about this header resides within a
	 *                        descriptor, if repeated call and available
	 * @return A {@link CompactDescriptor} encoded long value with information about
	 *         the header or {@link CompactDescriptor#ID_NOT_FOUND} if header at
	 *         depth is not found or available
	 */
//	long lookupHeaderExtension(int headerId, int extId, int depth, int recordIndexHint);

	/**
	 * Lookup a primary header and at specific inner tunnel depth.
	 *
	 * @param id         a header id
	 * @param depth      the tunnel inner depth
	 * @param descriptor (out) the header descriptor where matched header
	 *                   information will be stored
	 * @return if true, the header was found, otherwise false
	 */
	boolean lookupHeader(int id, int depth, HeaderDescriptor descriptor);

	/**
	 * Lookup a header extension and at specific inner tunnel depth.
	 *
	 * @param id              the primary header id
	 * @param extId           the header extension id
	 * @param extDepth        the extension/option depth
	 * @param recordIndexHint the record index hint, this information is typically
	 *                        index of where info about this header resides within a
	 *                        descriptor, if repeated call and available
	 * @param descriptor      (out) the header descriptor where matched header
	 *                        extension information will be stored
	 * @return if true, the header extension was found, otherwise false
	 */
	boolean lookupHeaderExtension(int id, int extId, int extDepth, int recordIndexHint, HeaderDescriptor descriptor);

//	boolean lookupHeaderExtensionAt(int index, int recordIndexHint, HeaderDescriptor descriptor);
}
