/*
 * Sly Technologies Free License
 * 
 * Copyright 2024 Sly Technologies Inc.
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

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public final class DescriptorConstants {

	public static final int DESC_PKT_FLAG_CRC = 0x0001;
	public static final int DESC_PKT_FLAG_PREAMBLE = 0x0002;
	public static final int DESC_TYPE1_BYTE_SIZE = 16;
	public static final int DESC_TYPE2_BYTE_SIZE_MIN = 28;
	public static final int DESC_TYPE2_RECORD_BYTE_SIZE = 8;
	public static final int DESC_TYPE2_RECORD_MAX_COUNT = 16;
	public static final int DESC_IPF_FRAG_KEY = 12;
	public static final int DESC_IPF_FRAG_IPv4_KEY_BYTE_SIZE = 12;
	public static final int DESC_IPF_FRAG_IPv6_KEY_BYTE_SIZE = 36;
	public static final int DESC_IPF_FRAG_BYTE_SIZE = DESC_IPF_FRAG_KEY + DESC_IPF_FRAG_IPv6_KEY_BYTE_SIZE;
	public static final int DESC_IPF_REASSEMBLY_BYTE_MIN_SIZE = 20;
	public static final int DESC_IPF_REASSEMBLY_RECORD_SIZE = 16;
	public static final int DESC_IPF_REASSEMBLY_BYTE_SIZE = DESC_IPF_REASSEMBLY_BYTE_MIN_SIZE
			+ DESC_IPF_REASSEMBLY_RECORD_SIZE * 32;
	public static final int DESC_TYPE2_BYTE_SIZE_MAX = DESC_TYPE2_BYTE_SIZE_MIN + (DESC_TYPE2_RECORD_MAX_COUNT
			* DESC_TYPE2_RECORD_BYTE_SIZE);

	private DescriptorConstants() {
	}
}