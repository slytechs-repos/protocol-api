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

import java.net.ProtocolException;
import java.nio.ByteBuffer;

import com.slytechs.jnet.platform.api.util.time.TimestampSource;
import com.slytechs.jnet.protocol.api.core.L2FrameType;
import com.slytechs.jnet.protocol.api.core.PacketDescriptorType;
import com.slytechs.jnet.protocol.api.packet.Packet;

/**
 * A packet dissector which records the state of the packet. A dissectors
 * decodes the contents of the packet and breaks it down into individual headers
 * and other relevant information. The dissector stores the result internally
 * until the next reset on it called. The dissectors state is written to a
 * descriptor buffer which for the specific descriptor type that was specified
 * at the time the dissector was allocated.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface PacketDissector {

	/**
	 * Interface used to record new header records after dissection.
	 */
	@FunctionalInterface
	public interface RecordRecorder {

		/**
		 * Adds the record.
		 *
		 * @param id     the id
		 * @param offset the offset
		 * @param length the length
		 * @return true, if successful
		 */
		boolean addRecord(int id, int offset, int length);
	}

	/**
	 * Allocated a new packet dissector of the given type. First a native dissector
	 * is allocated if available otherwise a java implemented dissector is
	 * allocated.
	 *
	 * @param type the descriptor type for which appropriate dissector is found
	 * @return newly allocated packet dissector
	 */
	static PacketDissector dissector(PacketDescriptorType type) {
		return isNativeDissectorSupported(type)
				? nativeDissector(type)
				: javaDissector(type);
	}

	/**
	 * Allocated a new packet dissector of the given type. A java implemented
	 * dissector is allocated.
	 *
	 * @param type the descriptor type for which appropriate dissector is found
	 * @return newly allocated packet dissector
	 */
	static PacketDissector javaDissector(PacketDescriptorType type) {

		return switch (type) {
		case TYPE1 -> new Type1DissectorJavaImpl();
		case TYPE2 -> new Type2DissectorJavaImpl();

		default -> throw new UnsupportedOperationException("Not implemented yet, dissector [%s]".formatted(type
				.name()));
		};

	}

	/**
	 * Checks if is native dissector supported of given type.
	 * <p>
	 * The native dissector API consists of a native library and header files. The
	 * native dissector class calls on the native dissector from java.
	 * <p>
	 * The following native API makes up the dissector API and is utilized by the
	 * native dissector class:
	 * 
	 * <pre>
	 * <code>
	int pkt_is_dissector_supported(PktDescriptorType_e type);
		PktDissector pkt_allocate_dissector(PktDescriptorType_e type);
	int pkt_free_dissector(PktDissector dissector);
	int pkt_dissect(PktDissector dissector, uint8_t *packet, uint64_t timestamp, uint32_t captureLength, uint32_t wireLength);
	int pkt_dissect_and_write(PktDissector dissector, uint8_t *descriptor, size_t descriptorOffset, uint8_t *packet, size_t packetOffset, uint64_t timestamp, uint32_t captureLength, uint32_t wireLength);
	int pkt_write_descriptor(PktDissector dissector, uint8_t *descriptor);
	int pkt_reset_dissector(PktDissector dissector);
	int pkt_set_dissector_datalink(PktDissector dissector, L2FrameType_e l2Type);
	 * </code>
	 * </pre>
	 *
	 * @param type the descriptor type
	 * @return true, if is native dissector supported
	 */
	static boolean isNativeDissectorSupported(PacketDescriptorType type) {
		return PacketDissectorNative.isSupported(type);
	}

	/**
	 * Allocates a new packet dissector of the given type. A native implemented
	 * dissector is allocated.
	 * <p>
	 * The native dissector API consists of a native library and header files. The
	 * native dissector class calls on the native dissector from java.
	 * <p>
	 * The following native API makes up the dissector API and is utilized by the
	 * native dissector class:
	 * 
	 * <pre>
	 * <code>
	int pkt_is_dissector_supported(PktDescriptorType_e type);
		PktDissector pkt_allocate_dissector(PktDescriptorType_e type);
	int pkt_free_dissector(PktDissector dissector);
	int pkt_dissect(PktDissector dissector, uint8_t *packet, uint64_t timestamp, uint32_t captureLength, uint32_t wireLength);
	int pkt_dissect_and_write(PktDissector dissector, uint8_t *descriptor, size_t descriptorOffset, uint8_t *packet, size_t packetOffset, uint64_t timestamp, uint32_t captureLength, uint32_t wireLength);
	int pkt_write_descriptor(PktDissector dissector, uint8_t *descriptor);
	int pkt_reset_dissector(PktDissector dissector);
	int pkt_set_dissector_datalink(PktDissector dissector, L2FrameType_e l2Type);
	 * </code>
	 * </pre>
	 *
	 * @param type the descriptor type for which appropriate dissector is found
	 * @return newly allocated packet dissector
	 */
	static PacketDissector nativeDissector(PacketDescriptorType type) {
		if (!isNativeDissectorSupported(type))
			throw new UnsupportedOperationException("No native implementation for dissector type %s was found"
					.formatted(type.name()));

		return new PacketDissectorNative(type);
	}

	/**
	 * Dissect a packet and store its state.
	 *
	 * @param buffer the packet buffer
	 * @return number of bytes processed in the buffer
	 */
	default int dissectPacket(ByteBuffer buffer) {
		int caplen = buffer.remaining();
		long timestamp = TimestampSource.system().timestamp();

		return dissectPacket(buffer, timestamp, caplen, caplen);
	}

	/**
	 * Dissect a packet and store its state.
	 *
	 * @param packet the packet
	 * @return number of bytes processed in the buffer
	 */
	default int dissectPacket(Packet packet) {
		return dissectPacket(packet.buffer());
	}

	/**
	 * Dissect a packet and store its state.
	 *
	 * @param buffer    the packet buffer
	 * @param timestamp the timestamp
	 * @param caplen    the caplen
	 * @param wirelen   the wirelen
	 * @return number of bytes processed in the buffer
	 */
	int dissectPacket(ByteBuffer buffer, long timestamp, int caplen, int wirelen);

	/**
	 * Checks if is native.
	 *
	 * @return true, if is native
	 */
	boolean isNative();

	/**
	 * Reset the state of the dissector. Calling a reset on a dissector will reset
	 * it state and get it ready for the next dissection.
	 */
	void reset();

	/**
	 * Sets a datalink type for all packets being dissected.
	 *
	 * @param l2Type the l 2 type
	 * @return the packet dissector
	 * @throws ProtocolException the protocol exception
	 */
	PacketDissector setDatalinkType(L2FrameType l2Type) throws ProtocolException;

	/**
	 * Write the state of the dissection into the provided descriptor.
	 *
	 * @param buffer the descriptor buffer
	 * @return number of byte written
	 */
	int writeDescriptor(ByteBuffer buffer);

	/**
	 * Write the state of the dissection into the provided descriptor.
	 *
	 * @param descriptor the descriptor
	 * @return number of byte written
	 */
	default int writeDescriptor(PacketDescriptor descriptor) {
		return writeDescriptor(descriptor.buffer());
	}

}
