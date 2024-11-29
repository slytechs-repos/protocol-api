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
package com.slytechs.jnet.protocol.core.network;

import com.slytechs.jnet.protocol.core.constants.Ip4IdOptions;

/**
 * IPv4 quick start option header.
 * <p>
 * The Quick Start option is used to improve the performance of TCP connections
 * by allowing the sender to send data at a higher rate without having to wait
 * for an ACK from the receiver. The Quick Start option works by providing the
 * receiver with information about the sender's congestion control algorithm.
 * This information allows the receiver to send ACKs more frequently, which
 * allows the sender to send data at a higher rate.
 * </p>
 * <p>
 * The Quick Start option is supported by most modern operating systems.
 * However, it is important to note that not all routers settingsSupport the Quick Start
 * option. As a result, it is important to check the documentation for the
 * routers that will be used to ensure that the Quick Start option is supported.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public final class Ip4QuickStartOption extends Ip4Option {

	/** The IPv4 MTU quick start option header ID constant. */
	public static final int ID = Ip4IdOptions.IPv4_ID_OPT_QS;

	/**
	 * IPv4 quick start option header.
	 */
	public Ip4QuickStartOption() {
		super(ID);
	}

	/**
	 * The MSS field is a 2-byte field that specifies the maximum segment size (MSS)
	 * that the sender is willing to receive.
	 *
	 * @return the unsigned 16-bit mss field value
	 */
	public int mss() {
		return Short.toUnsignedInt(buffer().getShort(2));
	}

	/**
	 * The Window Scale field is a 1-byte field that specifies the window scale
	 * factor. The window scale factor is used to increase the window size beyond
	 * the default value of 64K.
	 *
	 * @return the unsigned 8-bit windows scale field value
	 */
	public int windowScale() {
		return Byte.toUnsignedInt(buffer().get(3));
	}

	/**
	 * The Quick Start Bits field is a 1-byte field that contains a number of bits
	 * that provide information about the sender's congestion control algorithm.
	 *
	 * @return the unsigned 8-bit quick start bits field value
	 */
	public int quickStartBits() {
		return Byte.toUnsignedInt(buffer().get(4));
	}
}
