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

import static com.slytechs.jnet.platform.api.util.MemoryUnit.*;
import static com.slytechs.jnet.protocol.api.descriptor.Type1DescriptorLayout.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.slytechs.jnet.protocol.tcpip.constants.CoreConstants;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
class TestType1Layout {

	private static final int REPEAT = 1;
	private static final int REPEAT_TIMESTAMP = 1;
	private static final Random RANDOM = new Random();
	private ByteBuffer buf1;
	private ByteBuffer buf2 = ByteBuffer.allocate(CoreConstants.DESC_TYPE1_BYTE_SIZE);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		buf1 = ByteBuffer.allocate(CoreConstants.DESC_TYPE2_BYTE_SIZE_MAX);
		RANDOM.nextBytes(buf2.array());
	}

	@Test
	void test_timestamp() {
		final long VALUE = RANDOM.nextLong();
		final Type1DescriptorLayout LAYOUT = TIMESTAMP;

		LAYOUT.setLong(VALUE, buf1);
		LAYOUT.setLong(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getLong(buf1));
		assertEquals(VALUE, LAYOUT.getLong(buf2));
	}

	@Test
	void test_CAPLEN() {
		final int VALUE = RANDOM.nextInt(0, KILOBYTES.toBytesAsInt(64));
		final Type1DescriptorLayout LAYOUT = CAPLEN;

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	@Test
	void test_WIRELEN() {
		final Type1DescriptorLayout LAYOUT = WIRELEN;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	@Test
	void test_L2_TYPE() {
		final Type1DescriptorLayout LAYOUT = L2_FRAME_TYPE;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

}
