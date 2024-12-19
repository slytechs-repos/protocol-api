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
class TestMacAddresses {

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
	void toMacAddressString() {
		var SOURCE = "00:12:3f:97:92:01".toLowerCase();
		var EXPECTED = SOURCE;
		var ADDRESS = HexStrings.parseHexString(SOURCE);
		var RESULT = MacAddress.toMacAddressString(ADDRESS);

		assertEquals(EXPECTED, RESULT);
	}

	@Test
	void fromOuiMacAddressString() {
		var SOURCE = "Dell_97:92:01"; // f8:db:88:97:92:01
		var EXPECTED = "f8:db:88:97:92:01"; // Dell_97:92:01
		var ADDRESS = MacAddress.parseOuiMacAddress(SOURCE);
		var RESULT = MacAddress.toMacAddressString(ADDRESS);

		assertEquals(EXPECTED, RESULT);
	}

	@Test
	void toOuiMacAddressString() {
		var SOURCE = "Dell_97:92:01"; // f8:db:88:97:92:01
		var EXPECTED = SOURCE;
		var ADDRESS = MacAddress.parseOuiMacAddress(SOURCE);
		var RESULT = MacAddress.toOuiMacAddressString(ADDRESS);

		assertEquals(EXPECTED, RESULT);
	}

}
