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
package com.slytechs.jnet.protocol.api.core;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public final class LayerConstants {

	/* Common L2 constants */
	public static final int L2_HEADER_MIN_LEN = 14;

	/* Common L3 constants */
	public static final int L3_HEADER_MIN_LEN = 20;
	public static final int L3_ADDR_LEN_IPv4 = 4;
	public static final int L3_ADDR_LEN_IPv6 = 16;

	/* Common L4 constants */
	public static final int L4_HEADER_MIN_LEN = 8;
	public static final int L4_PORT_LEN = 2;

	private LayerConstants() {
	}
}