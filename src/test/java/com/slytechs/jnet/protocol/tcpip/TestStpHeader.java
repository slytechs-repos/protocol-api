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
import com.slytechs.jnet.protocol.api.descriptor.PacketDissector;
import com.slytechs.jnet.protocol.tcpip.constants.CoreConstants;
import com.slytechs.jnet.protocol.tcpip.constants.PacketDescriptorType;
import com.slytechs.jnet.protocol.tcpip.spanning.Stp;

/**
 * VLAN header tests
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
@Tag("osi-layer2")
@Tag("stp")
class TestStpHeader {

	static final PacketDissector DISSECTOR = PacketDissector
			.dissector(PacketDescriptorType.TYPE2);

	static final ByteBuffer DESC_BUFFER = ByteBuffer
			.allocateDirect(CoreConstants.DESC_TYPE2_BYTE_SIZE_MAX)
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
	void test_Stp_protocol() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(0x0000, stp.protocol());
	}

	@Test
	void test_Stp_version() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(0, stp.version());
	}

	@Test
	void test_Stp_type() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(0, stp.type());
	}

	@Test
	void test_Stp_flags() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(0, stp.flags());
	}

	@Test
	void test_Stp_rootId() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(0x8064001c0e877800l, stp.rootId());
	}

	@Test
	void test_Stp_rootCost() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(4, stp.rootCost());
	}

	@Test
	void test_Stp_bridgeId() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(0x8064001c0e878500l, stp.bridgeId());
	}

	@Test
	void test_Stp_bridgeId_priority() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(32768, stp.rootBridgePriority());
	}

	@Test
	void test_Stp_systemBridgeId_priority() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(32768, stp.systemBridgePriority());
	}

	@Test
	void test_Stp_bridgeId_Ext() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(100, stp.rootBridgeIdExt());
	}

	@Test
	void test_Stp_systemPridgeId_Ext() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(100, stp.systemBridgeIdExt());
	}

	@Test
	void test_Stp_rootBridgeId() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());
		final var EXPECTED_ID = HexStrings.parseHexString("001c0e877800");

		assertArrayEquals(EXPECTED_ID, stp.rootBridgeId());
	}

	@Test
	void test_Stp_systemBridgeId() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());
		final var EXPECTED_ID = HexStrings.parseHexString("001c0e878500");

		assertArrayEquals(EXPECTED_ID, stp.systemBridgeId());
	}

	@Test
	void test_Stp_portId() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(0x8004, stp.portId());
	}

	@Test
	void test_Stp_messageAge() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(1, stp.messageAge());
	}

	@Test
	void test_Stp_maxTime() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(20, stp.maxTime());
	}

	@Test
	void test_Stp_helloTime() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(2, stp.helloTime());
	}

	@Test
	void test_Stp_forwardDelay() throws HeaderNotFound {
		var packet = TestPackets.STP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var stp = packet.getHeader(new Stp());

		assertEquals(15, stp.forwardDelay());
	}

}
