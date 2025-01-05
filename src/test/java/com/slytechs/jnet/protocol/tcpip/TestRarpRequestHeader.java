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
package com.slytechs.jnet.protocol.tcpip;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.slytechs.jnet.platform.api.util.HexStrings;
import com.slytechs.jnet.protocol.api.common.HeaderNotFound;
import com.slytechs.jnet.protocol.api.core.PacketDescriptorType;
import com.slytechs.jnet.protocol.api.descriptor.DescriptorConstants;
import com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissector;
import com.slytechs.jnet.protocol.api.meta.PacketFormatter;
import com.slytechs.jnet.protocol.tcpip.arp.Arp;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
@Tag("osi-layer2")
@Tag("rarp")
@Tag("rarp-request")
class TestRarpRequestHeader {

	static final PacketDissector DISSECTOR = PacketDissector
			.dissector(PacketDescriptorType.TYPE2);

	static final ByteBuffer DESC_BUFFER = ByteBuffer
			.allocateDirect(DescriptorConstants.DESC_TYPE2_BYTE_SIZE_MAX)
			.order(ByteOrder.nativeOrder());

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		DISSECTOR.reset();

		DESC_BUFFER.clear();
		while (DESC_BUFFER.remaining() > 0)
			DESC_BUFFER.put((byte) 0);

		DESC_BUFFER.clear();
	}

	@Test
	void test_RarpRequest_hardwareType() throws HeaderNotFound {
		var packet = TestPackets.RARP1_REQUEST.toPacket();
		packet.setFormatter(new PacketFormatter());
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		Arp arp = packet.getHeader(new Arp());

		assertEquals(1, arp.hardwareType());
	}

	@Test
	void test_RarpRequest_protocolType() throws HeaderNotFound {
		var packet = TestPackets.RARP1_REQUEST.toPacket();
		packet.setFormatter(new PacketFormatter());
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		Arp arp = packet.getHeader(new Arp());

		assertEquals(0x800, arp.protocolType());
	}

	@Test
	void test_RarpRequest_hardwareSize() throws HeaderNotFound {
		var packet = TestPackets.RARP1_REQUEST.toPacket();
		packet.setFormatter(new PacketFormatter());
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		Arp arp = packet.getHeader(new Arp());

		assertEquals(6, arp.hardwareSize());
	}

	@Test
	void test_RarpRequest_protocolSize() throws HeaderNotFound {
		var packet = TestPackets.RARP1_REQUEST.toPacket();
		packet.setFormatter(new PacketFormatter());
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		Arp arp = packet.getHeader(new Arp());

		assertEquals(4, arp.protocolSize());
	}

	@Test
	void test_RarpRequest_operation() throws HeaderNotFound {
		var packet = TestPackets.RARP1_REQUEST.toPacket();
		packet.setFormatter(new PacketFormatter());
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		Arp arp = packet.getHeader(new Arp());

		assertEquals(3, arp.opcode());
	}

	@Test
	void test_RarpRequest_senderMacAddress() throws HeaderNotFound {
		var packet = TestPackets.RARP1_REQUEST.toPacket();
		packet.setFormatter(new PacketFormatter());
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		final byte[] EXPECTED_MAC = HexStrings.parseHexString("000c29340bde");

		Arp arp = packet.getHeader(new Arp());

		assertArrayEquals(EXPECTED_MAC, arp.senderMacAddress());
	}

	@Test
	void test_RarpRequest_senderProtocolAddress() throws HeaderNotFound {
		var packet = TestPackets.RARP1_REQUEST.toPacket();
		packet.setFormatter(new PacketFormatter());
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		final byte[] EXPECTED_IP = HexStrings.parseHexString("00000000");

		Arp arp = packet.getHeader(new Arp());

		assertArrayEquals(EXPECTED_IP, arp.senderProtocolAddress());
	}

	@Test
	void test_RarpRequest_targetMacAddress() throws HeaderNotFound {
		var packet = TestPackets.RARP1_REQUEST.toPacket();
		packet.setFormatter(new PacketFormatter());
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		final byte[] EXPECTED_MAC = HexStrings.parseHexString("000c29340bde");

		Arp arp = packet.getHeader(new Arp());

		assertArrayEquals(EXPECTED_MAC, arp.targetMacAddress());
	}

	@Test
	void test_RarpRequest_targetProtocolAddress() throws HeaderNotFound {
		var packet = TestPackets.RARP1_REQUEST.toPacket();
		packet.setFormatter(new PacketFormatter());
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		final byte[] EXPECTED_IP = HexStrings.parseHexString("00000000");

		Arp arp = packet.getHeader(new Arp());

		assertArrayEquals(EXPECTED_IP, arp.targetProtocolAddress());
	}

}
