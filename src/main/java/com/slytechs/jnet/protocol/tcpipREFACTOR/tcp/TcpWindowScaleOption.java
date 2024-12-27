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
package com.slytechs.jnet.protocol.tcpipREFACTOR.tcp;

import static com.slytechs.jnet.protocol.tcpipREFACTOR.constants.CoreConstants.*;

import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;

/**
 * The TCP window scale option is a TCP option that allows the sender and
 * receiver to increase the size of the receive window beyond the default value
 * of 65,535 bytes. The receive window is the amount of data that the receiver
 * can buffer before it needs to send an acknowledgment to the sender.
 * <p>
 * The TCP window scale option is a 3-byte option. The first byte of the option
 * contains the window scale factor. The window scale factor is a number between
 * 0 and 14. The window scale factor is multiplied by the default receive window
 * size of 65,535 bytes to calculate the actual receive window size.
 * </p>
 * <p>
 * For example, if the window scale factor is 2, then the actual receive window
 * size is 2 * 65,535 = 131,072 bytes.
 * </p>
 * <p>
 * The TCP window scale option is supported by most modern TCP implementations.
 * It is enabled by default in most operating systems and web browsers.
 * </p>
 * <p>
 * Here are some of the benefits of using the TCP window scale option:
 * </p>
 * <ul>
 * <li>Increased throughput: The TCP window scale option can increase the
 * throughput of TCP connections by allowing the sender to send more data before
 * it needs to wait for an acknowledgment.</li>
 * <li>Improved reliability: The TCP window scale option can improve the
 * reliability of TCP connections by reducing the number of retransmissions
 * needed.</li>
 * </ul>
 * <p>
 * The TCP window scale option is a valuable tool for improving the performance
 * and reliability of TCP connections. It can be used to improve the performance
 * of web browsing, file transfers, and other applications that use TCP.
 * </p>
 */
@MetaResource("tcp-opt-win-scale-meta.json")
public class TcpWindowScaleOption extends TcpOption {

	/** The Constant ID. */
	public static final int ID = TcpOptionId.TCP_OPT_ID_WIN_SCALE;

	/**
	 * Instantiates a new tcp window scale option.
	 */
	public TcpWindowScaleOption() {
		super(ID, TCP_OPTION_KIND_WIN_SCALE, TCP_OPTION_LEN_WIN_SCALE);
	}

	/**
	 * Computes the scale multiplier based on the field values of this TCP option.
	 *
	 * @return the unsigned 32-bit calculated multiplier value to be used to
	 *         calculate window size values in units of bytes
	 */
	@Meta(MetaType.ATTRIBUTE)
	public int multiplier() {
		return (1 << shiftCount());
	}

	/**
	 * Computes a scaled window value by applying the bit shift field value to the
	 * supplied unscaled window size.
	 *
	 * @param window the unscaled window size to be scaled using the field value of
	 *               this option
	 * @return the unsigned 32-bit field value in units of bytes
	 */
	public int scaleWindow(int window) {
		return window << shiftCount();
	}

	/**
	 * Gets the TCP window scale option shift count field value.
	 * <p>
	 * The TCP window scale option window shift count field is a 4-bit field that
	 * specifies the number of bits to shift the receive window size. The receive
	 * window size is the amount of data that the receiver can buffer before it
	 * needs to send an acknowledgment to the sender.
	 * </p>
	 * <p>
	 * The window shift count field is used to increase the receive window size
	 * beyond the default value of 65,535 bytes. For example, if the window shift
	 * count is 2, then the receive window size is 2^2 * 65,535 = 262,144 bytes.
	 * </p>
	 * <p>
	 * The window shift count field is supported by most modern TCP implementations.
	 * It is enabled by default in most operating systems and web browsers.
	 * </p>
	 * <p>
	 * Per RFC-7323 (TCP Extensions for High Performance):
	 * <q>The maximum scale exponent is limited to 14 for a maximum permissible
	 * receive window size of 1 GiB (2^(14+16)).</q>
	 * </p>
	 *
	 * @return the int
	 */
	@Meta
	public int shiftCount() {
		return Byte.toUnsignedInt(buffer().get(TCP_OPTION_FIELD_DATA));
	}
}