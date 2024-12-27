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

import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.api.packet.Packet;
import com.slytechs.jnet.protocol.tcpipREFACTOR.constants.CoreId;

/**
 * A builtin model of a payload. A payload may be applied to a {@link Packet} or
 * any of the headers or extensions. A payload will be bound to data portion of
 * either a packet or a header.
 * 
 * <p>
 * For packets, payload will bound to the packet's data contents starting at the
 * first byte past the last header until the end of the packet, allowing generic
 * access to the data payload portion of the packet.
 * </p>
 * <p>
 * For headers, payload will be bound to the data portion just past the header.
 * Another words a payload will be bound to the first byte past the header and
 * until the last byte of the packet or data length defined by the header, if a
 * header specifies included data length.
 * </p>
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("payload-meta.json")
public sealed class Payload extends Header permits Other {

	/** The Constant ID. */
	public static final int ID = CoreId.CORE_ID_PAYLOAD;

	/**
	 * Instantiates a new payload.
	 */
	public Payload() {
		super(ID);
	}

	/**
	 * Copies the payload contents to a new array.
	 *
	 * @return array containing copy of the payload contents
	 */
	@Meta
	public byte[] data() {
		byte[] array = new byte[headerLength()];

		data(array);

		return array;
	}

	/**
	 * Copies the payload contents to the supplied array.
	 *
	 * @param dst the destination array where to copy the contents
	 * @return number of bytes copied into the array
	 */
	public int data(byte[] dst) {
		return data(dst, 0, dst.length);
	}

	/**
	 * Copies the payload contents to the supplied array.
	 *
	 * @param dst    the destination array where to copy the contents
	 * @param offset the offset into the destination array of the start of the copy
	 * @param length number of bytes to copy from payload to dst array
	 * @return number of bytes copied into the array
	 */
	public int data(byte[] dst, int offset, int length) {
		if (offset + length > headerLength())
			length = headerLength() - offset;

		buffer().get(dst, offset, length);

		return length;
	}

}
