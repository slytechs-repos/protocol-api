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

import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.pack.PackId;

/**
 * The Class TestCoreIds.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
class TestCoreIds {

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	private CoreId lookup(int id) {
		return CoreId.values()[PackId.decodeIdOrdinal(id)];
	}

	@Test
	void ICMPv6_MULTICAST_LISTENER_QUERY() {
		assertEquals(CoreId.ICMPv6_MULTICAST_LISTENER_QUERY, lookup(
				CoreId.CORE_ID_ICMPv6_MULTICAST_LISTENER_QUERY));
	}

	@Test
	void ICMPv6_REDIRECT() {
		assertEquals(CoreId.ICMPv6_REDIRECT, lookup(CoreId.CORE_ID_ICMPv6_REDIRECT));
	}

	@Test
	void ICMPv6_NODE_INFO_RESPONSE() {
		assertEquals(CoreId.ICMPv6_NODE_INFO_RESPONSE, lookup(CoreId.CORE_ID_ICMPv6_NODE_INFO_RESPONSE));
	}

	@Test
	void ICMPv6_HOME_AGENT_REQUEST() {
		assertEquals(CoreId.ICMPv6_HOME_AGENT_REQUEST, lookup(CoreId.CORE_ID_ICMPv6_HOME_AGENT_REQUEST));
	}

	@Test
	void ICMPv6_HOME_AGENT_REPLY() {
		assertEquals(CoreId.ICMPv6_HOME_AGENT_REPLY, lookup(CoreId.CORE_ID_ICMPv6_HOME_AGENT_REPLY));
	}

	@Test
	void ICMPv6_FMIPv6_MESSAGE() {
		assertEquals(CoreId.ICMPv6_FMIPv6_MESSAGE, lookup(CoreId.CORE_ID_ICMPv6_FMIPv6_MESSAGE));
	}
}
