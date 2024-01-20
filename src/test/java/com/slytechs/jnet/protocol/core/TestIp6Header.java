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
package com.slytechs.jnet.protocol.core;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.slytechs.jnet.jnetruntime.util.HexStrings;
import com.slytechs.jnet.protocol.HeaderNotFound;
import com.slytechs.jnet.protocol.core.DiffServ;
import com.slytechs.jnet.protocol.core.Ip6;
import com.slytechs.jnet.protocol.core.constants.CoreConstants;
import com.slytechs.jnet.protocol.core.constants.CoreId;
import com.slytechs.jnet.protocol.core.constants.PacketDescriptorType;
import com.slytechs.jnet.protocol.descriptor.PacketDissector;

/**
 * VLAN header tests
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
@Tag("osi-layer3")
@Tag("ip")
@Tag("IPv6")
class TestIp6Header {

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
	void test_Ip6_src() throws HeaderNotFound {
		var packet = TestPackets.ETH_IPv6_TCP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var ip6 = packet.getHeader(new Ip6());
		var EXPECTED_IP = HexStrings.parseHexString("fc000002000000020000000000000001");

		assertArrayEquals(EXPECTED_IP, ip6.src());
	}

	@Test
	void test_Ip6_dst() throws HeaderNotFound {
		var packet = TestPackets.ETH_IPv6_TCP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var ip6 = packet.getHeader(new Ip6());
		var EXPECTED_IP = HexStrings.parseHexString("fc000002000000010000000000000001");

		assertArrayEquals(EXPECTED_IP, ip6.dst());
	}

	@Test
	void test_Ip6_dsfield() throws HeaderNotFound {
		var packet = TestPackets.ETH_IPv6_TCP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		DiffServ ip6 = packet.getHeader(new Ip6());

		assertEquals(0x00, ip6.trafficDscp());
	}

	@Test
	void test_Ip6_flowLabel() throws HeaderNotFound {
		var packet = TestPackets.ETH_IPv6_TCP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var ip6 = packet.getHeader(new Ip6());

		assertEquals(0xd684a, ip6.flowLabel());
	}

	@Test
	void test_Ip6_hopLimit() throws HeaderNotFound {
		var packet = TestPackets.ETH_IPv6_TCP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var ip6 = packet.getHeader(new Ip6());

		assertEquals(64, ip6.hopLimit());
	}

	@Test
	void test_Ip6_payloadLength() throws HeaderNotFound {
		var packet = TestPackets.ETH_IPv6_TCP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());

		var ip6 = packet.getHeader(new Ip6());

		assertEquals(40, ip6.payloadLength());
	}

	@Test
	void test_Ip6_nextHeader() throws HeaderNotFound {
		var packet = TestPackets.ETH_IPv6_TCP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());
		
		var ip6 = packet.getHeader(new Ip6());

		assertEquals(6, ip6.nextHeader());
	}

	@Test
	void test_Ip6_version() throws HeaderNotFound {
		var packet = TestPackets.ETH_IPv6_TCP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());
		
		var ip6 = packet.getHeader(new Ip6());

		assertEquals(6, ip6.version());
	}

	@Test
	void test_Ip6_containsTcpHeader() throws HeaderNotFound {
		var packet = TestPackets.ETH_IPv6_TCP.toPacket();
		packet.descriptor().bind(DESC_BUFFER);

		DISSECTOR.dissectPacket(packet);
		DISSECTOR.writeDescriptor(packet.descriptor());
		
		assertTrue(packet.hasHeader(CoreId.CORE_ID_TCP));
	}

}
