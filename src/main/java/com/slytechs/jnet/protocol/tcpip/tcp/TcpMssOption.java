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
package com.slytechs.jnet.protocol.tcpip.tcp;

import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;

/**
 * The TCP MSS option is a TCP option that specifies the maximum segment size
 * (MSS) that can be sent over the connection. The MSS is the largest amount of
 * data, specified in bytes, that a computer or communications device can
 * receive in a single TCP segment. MSS does not include the TCP header or the
 * IP header.
 * <p>
 * The MSS option is used to prevent data from being fragmented, which can lead
 * to performance problems. When a TCP segment is fragmented, it is broken up
 * into smaller pieces that must be reassembled by the receiver. This can add
 * overhead and delay to the transmission of data.
 * </p>
 * <p>
 * The MSS option is negotiated during the TCP handshake. Both the sender and
 * receiver send their MSS values in the SYN packet. The MSS value that is used
 * for the connection is the smallest of the two values.
 * </p>
 * <p>
 * The default MSS value is 536 bytes. However, this value can be changed if the
 * network or the application requires a larger or smaller MSS. For example, if
 * the network has a small MTU, the MSS must be reduced to avoid fragmentation.
 * </p>
 * <p>
 * The MSS option is an important part of the TCP protocol. It helps to ensure
 * that data is transmitted efficiently and reliably.
 * </p>
 * <p>
 * Here are some additional details about the TCP MSS option:
 * </p>
 * <ul>
 * <li>The TCP MSS option is a 4-byte option.</li>
 * <li>The TCP MSS option is used to specify the maximum segment size (MSS) that
 * can be sent over the connection.</li>
 * <li>The MSS is the largest amount of data, specified in bytes, that a
 * computer or communications device can receive in a single TCP segment.</li>
 * <li>MSS does not include the TCP header or the IP header.</li>
 * <li>The MSS option is negotiated during the TCP handshake.</li>
 * <li>The MSS value that is used for the connection is the smallest of the two
 * values.</li>
 * <li>The default MSS value is 536 bytes.</li>
 * <li>The MSS value can be changed if the network or the application requires a
 * larger or smaller MSS.</li>
 * </ul>
 */
@MetaResource("/meta/tcpip/tcp-options.yaml#TCP-MSS")
public class TcpMssOption extends TcpOption {

	/** The Constant ID. */
	public static final int ID = TcpOptionId.TCP_OPT_ID_MSS;

	/**
	 * Instantiates a new tcp MSS option.
	 */
	public TcpMssOption() {
		super(ID, TcpOptionConstants.TCP_OPTION_KIND_MSS, TcpOptionConstants.TCP_OPTION_LEN_MSS);
	}

	/**
	 * Gets the TCP MSS mss field value.
	 * <p>
	 * The MSS option is a 16-bit option. The first 8 bits of the option contain the
	 * MSS value for the sending host. The second 8 bits of the option contain the
	 * MSS value for the receiving host.
	 * </p>
	 * <p>
	 * If the MSS options are not negotiated, the default MSS value is 536 bytes.
	 * However, the MSS value can be negotiated to a larger value if the network
	 * supports it.
	 * </p>
	 * <p>
	 * The MSS option is important for two reasons. First, it helps to prevent
	 * fragmentation. Fragmentation occurs when a TCP segment is too large to be
	 * sent in a single IP packet. When fragmentation occurs, the IP layer breaks
	 * the TCP segment into multiple IP packets. This can cause problems, such as
	 * lost data and increased latency.
	 * </p>
	 * <p>
	 * Second, the MSS option helps to improve performance. By limiting the size of
	 * TCP segments, the MSS option helps to reduce the number of IP packets that
	 * need to be sent. This can improve performance by reducing the amount of time
	 * it takes to send data over the network.
	 * </p>
	 * <p>
	 * The MSS option is supported by most TCP implementations. It is enabled by
	 * default in most operating systems and web browsers.
	 * </p>
	 * <p>
	 * Here are some of the benefits of using the TCP MSS option:
	 * </p>
	 * <ul>
	 * <li>Reduced fragmentation: The TCP MSS option can help to reduce
	 * fragmentation by limiting the size of TCP segments.</li>
	 * <li>Improved performance: The TCP MSS option can improve performance by
	 * reducing the number of IP packets that need to be sent.</li>
	 * <li>Improved reliability: The TCP MSS option can improve reliability by
	 * reducing the chance of data loss.</li>
	 * </ul>
	 * <p>
	 * The TCP MSS option is a valuable tool for improving the performance,
	 * reliability, and security of TCP connections. It can be used to improve the
	 * performance of web browsing, file transfers, and other applications that use
	 * TCP.
	 * </p>
	 * 
	 * @return the unsigned 16-bit field value
	 */
	@Meta
	public int mss() {
		return Short.toUnsignedInt(buffer().getShort(2));
	}

}