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
package com.slytechs.jnet.protocol.api.descriptor.impl;

import java.lang.foreign.MemorySegment;
import java.net.ProtocolException;
import java.nio.ByteBuffer;

import com.slytechs.jnet.platform.api.memory.foreign.DefaultForeignDowncall;
import com.slytechs.jnet.platform.api.memory.foreign.DefaultForeignInitializer;
import com.slytechs.jnet.protocol.api.core.L2FrameType;
import com.slytechs.jnet.protocol.api.core.PacketDescriptorType;
import com.slytechs.jnet.protocol.api.descriptor.Type1Descriptor;

/**
 * Native dissector implementation.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class PacketDissectorNative extends AbstractPacketDissector {

	/** {@code int pkt_is_dissector_supported(PktDescriptorType_e type)} */
	static final DefaultForeignDowncall pkt_is_dissector_supported;

	/** {@code PktDissector pkt_allocate_dissector(PktDescriptorType_e type)} */
	static final DefaultForeignDowncall pkt_allocate_dissector;

	/** {@code int pkt_free_dissector(PktDissector dissector)} */
	static final DefaultForeignDowncall pkt_free_dissector;

	/**
	 * {@code int pkt_dissect(PktDissector dissector, uint8_t *packet, uint64_t timestamp, uint32_t captureLength, uint32_t wireLength)}
	 */
	static final DefaultForeignDowncall pkt_dissect;

	/**
	 * {@code int pkt_dissect_and_write(PktDissector dissector, uint8_t *descriptor, size_t descriptorOffset, uint8_t *packet, size_t packetOffset, uint64_t timestamp, uint32_t captureLength, uint32_t wireLength}
	 */
	static final DefaultForeignDowncall pkt_dissect_and_write;

	/**
	 * {@code int pkt_write_descriptor(PktDissector dissector, uint8_t *descriptor)}
	 */
	static final DefaultForeignDowncall pkt_write_descriptor;

	/** {@code int pkt_reset_dissector(PktDissector dissector)} */
	static final DefaultForeignDowncall pkt_reset_dissector;

	/**
	 * {@code int pkt_set_dissector_datalink(PktDissector dissector, L2FrameType_e l2Type)}
	 */
	static final DefaultForeignDowncall pkt_set_dissector_datalink;

	static {
		try (var foreign = new DefaultForeignInitializer(Type1Descriptor.class)) {

			// @formatter:off
			pkt_is_dissector_supported = foreign.downcall("pkt_is_dissector_supported(I)I");
			pkt_allocate_dissector     = foreign.downcall("pkt_allocate_dissector(I)A");
			pkt_free_dissector         = foreign.downcall("pkt_free_dissector(A)I");
			pkt_dissect                = foreign.downcall("pkt_dissect(AIJII)I");
			pkt_dissect_and_write      = foreign.downcall("pkt_dissect_and_write(AIJIIJJII)I");
			pkt_write_descriptor       = foreign.downcall("pkt_write_descriptor(AI)I");
			pkt_reset_dissector        = foreign.downcall("pkt_reset_dissector(A)I");
			pkt_set_dissector_datalink = foreign.downcall("pkt_set_dissector_datalink(AI)I");
			// @formatter:on

		}
	}

	private final MemorySegment address;

	/**
	 * Instantiates a new native dissector.
	 */
	public PacketDissectorNative(PacketDescriptorType type) {
		this.address = pkt_allocate_dissector.invokeObj(type.getAsInt());
	}

	public static boolean isSupported(PacketDescriptorType type) {
		boolean b = pkt_set_dissector_datalink.isNativeSymbolResolved();

		return b && pkt_is_dissector_supported.invokeInt(type.getAsInt()) != 0;
	}

	/**
	 * Dissect packet.
	 *
	 * @param buffer    the buffer
	 * @param timestamp the timestamp
	 * @param caplen    the caplen
	 * @param wirelen   the wirelen
	 * @return the int
	 * @see com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissector#dissectPacket(java.nio.ByteBuffer,
	 *      long, int, int)
	 */
	@Override
	public int dissectPacket(ByteBuffer buffer, long timestamp, int caplen, int wirelen) {
		var bufAddress = MemorySegment.ofBuffer(buffer).address();

		return pkt_dissect.invokeInt(address, bufAddress, timestamp, caplen, wirelen);
	}

	/**
	 * Write descriptor.
	 *
	 * @param buffer the buffer
	 * @return the int
	 * @see com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissector#writeDescriptor(java.nio.ByteBuffer)
	 */
	@Override
	public int writeDescriptor(ByteBuffer buffer) {
		var bufAddress = MemorySegment.ofBuffer(buffer).address();

		return pkt_reset_dissector.invokeInt(address, bufAddress);
	}

	/**
	 * Reset.
	 *
	 * @see com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissector#reset()
	 */
	@Override
	public void reset() {
		pkt_reset_dissector.invokeInt(address);
	}

	/**
	 * Sets the datalink type.
	 *
	 * @param l2Type the l 2 type
	 * @return the packet dissector
	 * @throws ProtocolException the protocol exception
	 * @see com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissector#setDatalinkType(com.slytechs.jnet.protocol.api.core.L2FrameType)
	 */
	@Override
	public PacketDissector setDatalinkType(L2FrameType l2Type) throws ProtocolException {
		pkt_set_dissector_datalink.invokeInt(address, l2Type.getAsInt());

		return this;
	}

	/**
	 * Checks if is native.
	 *
	 * @return true, if is native
	 * @see com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissector#isNative()
	 */
	@Override
	public boolean isNative() {
		return true;
	}

}
