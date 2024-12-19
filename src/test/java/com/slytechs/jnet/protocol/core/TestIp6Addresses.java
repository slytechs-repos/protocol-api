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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.slytechs.jnet.jnetruntime.util.HexStrings;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
class TestIp6Addresses {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void ipv6AddressCompressionRule1() {
		var ADDRESS = HexStrings.parseHexString("FE82:1234:0000:1235:1416:1A12:1B12:1C1F");
		var EXPECTED = "FE82:1234::1235:1416:1A12:1B12:1C1F".toLowerCase();
		var RESULT = IpAddress.toIp6AddressString(ADDRESS);

		assertEquals(EXPECTED, RESULT);
	}

	@Test
	void ipv6AddressCompressionRule2() {
		var ADDRESS = HexStrings.parseHexString("FE82:0000:0000:0000:0000:1A12:1234:1A12");
		var EXPECTED = "FE82::1A12:1234:1A12".toLowerCase();
		var RESULT = IpAddress.toIp6AddressString(ADDRESS);

		assertEquals(EXPECTED, RESULT);
	}

	@Test
	void ipv6AddressCompressionRule3() {
		var ADDRESS = HexStrings.parseHexString("2001:1234:0000:0000:1A12:0000:0000:1A13");
		var EXPECTED = "2001:1234::1A12:0:0:1A13".toLowerCase();
		var RESULT = IpAddress.toIp6AddressString(ADDRESS);

		assertEquals(EXPECTED, RESULT);
	}

	@Test
	void ipv6AddressCompressionSingleByteAtTheEnd() {
		var ADDRESS = HexStrings.parseHexString("ff02:0000:0000:0000:0000:0000:0000:0016");
		var EXPECTED = "ff02::16".toLowerCase();
		var RESULT = IpAddress.toIp6AddressString(ADDRESS);

		assertEquals(EXPECTED, RESULT);
	}

	@Test
	void ipv6AddressUncompressed() {
		var ADDRESS = HexStrings.parseHexString("ff02:0000:0000:0000:0000:0000:0000:0016");
		var EXPECTED = "ff02:0:0:0:0:0:0:16".toLowerCase();
		var RESULT = IpAddress.toUncompressedIp6AddressString(ADDRESS);

		assertEquals(EXPECTED, RESULT);
	}
}
