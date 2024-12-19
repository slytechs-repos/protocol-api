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

import com.slytechs.jnet.jnetruntime.util.HexStrings;
import com.slytechs.jnet.protocol.HeaderNotFound;
import com.slytechs.jnet.protocol.tcpip.constants.CoreConstants;
import com.slytechs.jnet.protocol.tcpip.constants.PacketDescriptorType;
import com.slytechs.jnet.protocol.api.descriptor.PacketDissector;

/**
 * VLAN header tests
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
@Tag("osi-layer2")
@Tag("llc")
@Tag("snap")
class TestLlcSnapHeader {

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
	void test_Llc_ssap() throws HeaderNotFound {
		var packet = TestPackets.IEEE8023_SNAP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var llc = packet.getHeader(new Llc());

		assertEquals(0xAA, llc.ssap());
	}

	@Test
	void test_Llc_dsap() throws HeaderNotFound {
		var packet = TestPackets.IEEE8023_SNAP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var llc = packet.getHeader(new Llc());

		assertEquals(0xAA, llc.dsap());
	}

	@Test
	void test_Llc_control() throws HeaderNotFound {
		var packet = TestPackets.IEEE8023_SNAP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var llc = packet.getHeader(new Llc());

		assertEquals(0x03, llc.control());
	}

	@Test
	void test_Snap_oui() throws HeaderNotFound {
		var packet = TestPackets.IEEE8023_SNAP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var snap = packet.getHeader(new Snap());
		final var EXPECTED_OUI = HexStrings.parseHexString("00000c");

		assertArrayEquals(EXPECTED_OUI, snap.oui());
	}

	@Test
	void test_Snap_pid() throws HeaderNotFound {
		var packet = TestPackets.IEEE8023_SNAP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var snap = packet.getHeader(new Snap());

		assertEquals(0x0115, snap.pid());
	}

}
