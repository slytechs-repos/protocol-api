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

import java.nio.ByteBuffer;

import com.slytechs.jnet.protocol.api.descriptor.HeaderDescriptor;
import com.slytechs.jnet.protocol.api.descriptor.PacketDescriptor;
import com.slytechs.jnet.protocol.api.meta.Meta;

/**
 * A specialized header subclass implemented by all header options.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
@Meta
public abstract class OptionsHeader
		extends Header {

	/** The source buffer. */
	private ByteBuffer packet;

	/** The descriptor. */
	private PacketDescriptor descriptor;

	/** The meta. */
	private int meta;

	/**
	 * Reset this header option instance, importantly reset the reference to
	 * sourceBuffer to release it.
	 *
	 * @see com.slytechs.jnet.protocol.api.common.Header#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		packet = null;
		descriptor = null;
		meta = 0;

		super.onUnbind();
	}

	/**
	 * Instantiates a new extendable header.
	 *
	 * @param id the id
	 */
	protected OptionsHeader(int id) {
		super(id);
	}

	/**
	 * Gets the option.
	 *
	 * @param <E>       the element type
	 * @param option the option
	 * @param depth     the depth
	 * @return the option
	 * @throws HeaderNotFound the header not found
	 * @see com.slytechs.jnet.protocol.api.common.HasOption#getOption(com.slytechs.jnet.protocol.api.common.Header,
	 *      int)
	 */
	protected final <E extends Header> E getOptionHeader(E option, int depth) throws HeaderNotFound {
		E t = peekOptionHeader(option, depth);
		if (t == null)
			throw new HeaderNotFound(option.headerName());

		return t;
	}

	/**
	 * Checks for option.
	 *
	 * @param optionId the option id
	 * @param depth       the depth
	 * @return true, if successful
	 * @see com.slytechs.jnet.protocol.api.common.HasOption#hasOption(int, int)
	 */
	protected final boolean hasOptionHeader(int optionId, int depth) {
		return descriptor.lookupHeaderExtension(super.id, optionId, depth, meta, HeaderDescriptor.EMPTY);
	}

	/**
	 * Peek option.
	 *
	 * @param <E>       the element type
	 * @param option the option
	 * @param depth     the depth
	 * @return the e
	 * @see com.slytechs.jnet.protocol.api.common.HasOption#peekOption(com.slytechs.jnet.protocol.api.common.Header,
	 *      int)
	 */
	protected final <E extends Header> E peekOptionHeader(E option, int depth) {

		if (descriptor.lookupHeaderExtension(super.id, option.id(), depth, meta, option.getHeaderDescriptor())) {
			option.bindHeaderToPacket(packet, descriptor);

			return option;
		} else {
			option.unbind();

			return null;

		}
	}

	/**
	 * Bind options to packet.
	 *
	 * @param packet     the packet
	 * @param descriptor the descriptor
	 * @param meta       the meta
	 */
	@Override
	public void bindOptionsToPacket(ByteBuffer packet, PacketDescriptor descriptor) {
		this.packet = packet;
		this.descriptor = descriptor;
		this.meta = super.getHeaderDescriptor().getMeta();
	}

}
