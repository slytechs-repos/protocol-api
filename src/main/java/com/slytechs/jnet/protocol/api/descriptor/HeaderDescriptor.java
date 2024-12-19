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
package com.slytechs.jnet.protocol.api.descriptor;

import com.slytechs.jnet.protocol.api.pack.PackId;
import com.slytechs.jnet.protocol.tcpip.constants.PacketDescriptorType;

/**
 * A header descriptor. A header descriptor defines the current state of a
 * specific header. The header descriptor is recorded in some kind of binary
 * compact form such as {@code PackId 64-bit RECORD} and a descriptor is an
 * expanded for of the binary header descriptor.
 */
public final class HeaderDescriptor {

	/** An empty header descriptor. */
	public static final HeaderDescriptor EMPTY = new HeaderDescriptor();

	/** The descriptor type. */
	private PacketDescriptorType descriptorType;

	/** The offset. */
	private int offset;

	/** The length. */
	private int length;

	/** The effective id. */
	private int effectiveId;

	/** The meta. */
	private int meta;

	/** The depth. */
	private int depth;

	/**
	 * Instantiates a new header descriptor.
	 */
	public HeaderDescriptor() {
		super();
	}

	/**
	 * Assign.
	 *
	 * @param id             the id
	 * @param depth          the depth
	 * @param offset         the offset
	 * @param length         the length
	 * @param descriptorType the descriptor type
	 * @return true, if successful
	 */
	public boolean assign(int id, int depth, int offset, int length, PacketDescriptorType descriptorType) {
		this.descriptorType = descriptorType;
		this.depth = depth;

		this.effectiveId = id;
		this.offset = offset;
		this.length = length;

		return true;
	}

	/**
	 * Assign from record.
	 *
	 * @param record         the record
	 * @param depth          the depth
	 * @param hint           the hint
	 * @param descriptorType the descriptor type
	 * @return true, if successful
	 */
	public boolean assignFromRecord(long record, int depth, int hint, PacketDescriptorType descriptorType) {
		this.descriptorType = descriptorType;
		this.depth = depth;
		this.meta = hint;

		this.effectiveId = PackId.decodeRecordId(record);
		this.offset = PackId.decodeRecordOffset(record);
		this.length = PackId.decodeRecordSize(record);

		return true;
	}

	/**
	 * Gets the depth.
	 *
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Gets the descriptor type.
	 *
	 * @return the descriptor type
	 */
	public PacketDescriptorType getDescriptorType() {
		return descriptorType;
	}

	/**
	 * Gets the effective id.
	 *
	 * @return the effective id
	 */
	public int getEffectiveId() {
		return effectiveId;
	}

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Gets the meta.
	 *
	 * @return the meta
	 */
	public int getMeta() {
		return meta;
	}

	/**
	 * Gets the offset.
	 *
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Sets the depth.
	 *
	 * @param depth the depth to set
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	/**
	 * Sets the descriptor type.
	 *
	 * @param descriptorType the new descriptor type
	 */
	public void setDescriptorType(PacketDescriptorType descriptorType) {
		this.descriptorType = descriptorType;
	}

	/**
	 * Sets the effective id.
	 *
	 * @param effectiveId the new effective id
	 */
	public void setEffectiveId(int effectiveId) {
		this.effectiveId = effectiveId;
	}

	/**
	 * Sets the length.
	 *
	 * @param length the new length
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Sets the meta.
	 *
	 * @param meta the new meta
	 */
	public void setMeta(int meta) {
		this.meta = meta;
	}

	/**
	 * Sets the offset.
	 *
	 * @param offset the new offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HeaderDescriptor [offset=" + offset + ", length=" + length + ", effectiveId=" + effectiveId
				+ ", descriptorType=" + descriptorType + ", meta=" + meta + "]";
	}
}