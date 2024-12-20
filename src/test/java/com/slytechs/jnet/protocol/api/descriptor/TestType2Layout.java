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
import static com.slytechs.jnet.protocol.api.descriptor.Type2DescriptorLayout.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import com.slytechs.jnet.protocol.tcpip.constants.CoreConstants;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
class TestType2Layout {

	private static final int REPEAT = 1;
	private static final int REPEAT_TIMESTAMP = 1;
	private static final Random RANDOM = new Random();
	private ByteBuffer buf1;
	private ByteBuffer buf2 = ByteBuffer.allocate(CoreConstants.DESC_TYPE2_BYTE_SIZE_MAX);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		buf1 = ByteBuffer.allocate(CoreConstants.DESC_TYPE2_BYTE_SIZE_MAX);
		RANDOM.nextBytes(buf2.array());
	}

	//@Test
	@RepeatedTest(value = REPEAT_TIMESTAMP)
	void test_timestamp() {
		final long VALUE = RANDOM.nextLong();
		final Type2DescriptorLayout LAYOUT = TIMESTAMP;

		LAYOUT.setLong(VALUE, buf1);
		LAYOUT.setLong(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getLong(buf1));
		assertEquals(VALUE, LAYOUT.getLong(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_CAPLEN() {
		final int VALUE = RANDOM.nextInt(0, KILOBYTES.toBytesAsInt(64));
		final Type2DescriptorLayout LAYOUT = CAPLEN;

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_RX_PORT() {
		final Type2DescriptorLayout LAYOUT = RX_PORT;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_TX_PORT() {
		final Type2DescriptorLayout LAYOUT = TX_PORT;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_WIRELEN() {
		final Type2DescriptorLayout LAYOUT = WIRELEN;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_TX_NOW() {
		final Type2DescriptorLayout LAYOUT = TX_NOW;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_TX_IGNORE() {
		final Type2DescriptorLayout LAYOUT = TX_IGNORE;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_TX_CRC_OVERRIDE() {
		final Type2DescriptorLayout LAYOUT = TX_CRC_OVERRIDE;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_TX_SET_CLOCK() {
		final Type2DescriptorLayout LAYOUT = TX_SET_CLOCK;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_L2_TYPE() {
		final Type2DescriptorLayout LAYOUT = L2_TYPE;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_HASH_TYPE() {
		final Type2DescriptorLayout LAYOUT = HASH_TYPE;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_RECORD_COUNT() {
		final Type2DescriptorLayout LAYOUT = RECORD_COUNT;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_L3_IS_FRAG() {
		final Type2DescriptorLayout LAYOUT = L3_IS_FRAG;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_L3_LAST_FRAG() {
		final Type2DescriptorLayout LAYOUT = RECORD_COUNT;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_L3_HASH24() {
		final Type2DescriptorLayout LAYOUT = RECORD_COUNT;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

	//@Test
	@RepeatedTest(value = REPEAT)
	void test_BITMASK() {
		final Type2DescriptorLayout LAYOUT = RECORD_COUNT;
		final int VALUE = RANDOM.nextInt(0, 1 << LAYOUT.bitSize());

		LAYOUT.setInt(VALUE, buf1);
		LAYOUT.setInt(VALUE, buf2);

		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf1));
		assertEquals(VALUE, LAYOUT.getUnsignedInt(buf2));
	}

}
