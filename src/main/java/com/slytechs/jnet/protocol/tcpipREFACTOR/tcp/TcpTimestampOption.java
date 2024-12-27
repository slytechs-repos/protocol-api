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

/**
 * The TCP timestamp option is a TCP option that allows each end of a TCP
 * connection to keep track of the round-trip time (RTT) of packets sent over
 * the connection. The RTT is the time it takes for a packet to travel from one
 * end of the connection to the other and back again.
 * <p>
 * The TCP timestamp option is a 16-byte option. The first 8 bytes of the option
 * contain the timestamp value for the sending end of the connection. The second
 * 8 bytes of the option contain the timestamp value for the receiving end of
 * the connection.
 * </p>
 * <p>
 * The TCP timestamp option is used by both the sender and receiver to calculate
 * the RTT of packets sent over the connection. The RTT is used by the TCP
 * congestion control algorithm to determine how quickly data can be sent over
 * the connection.
 * </p>
 * <p>
 * The TCP timestamp option is supported by most modern TCP implementations. It
 * is enabled by default in most operating systems and web browsers.
 * </p>
 * <p>
 * Here are some of the benefits of using the TCP timestamp option:
 * </p>
 * <ul>
 * <li>Improved congestion control: The TCP timestamp option can help the TCP
 * congestion control algorithm to more accurately determine the RTT of packets
 * sent over the connection. This can lead to improved performance and
 * reliability of TCP connections.</li>
 * <li>Improved debugging: The TCP timestamp option can be used to help debug
 * TCP connections. By looking at the RTT values, it can be determined where
 * bottlenecks are in the network.</li>
 * </ul>
 * <p>
 * The TCP timestamp option is a valuable tool for improving the performance,
 * reliability, and debugging of TCP connections. It can be used to improve the
 * performance of web browsing, file transfers, and other applications that use
 * TCP.
 * </p>
 */
@MetaResource("tcp-opt-tstamp-meta.json")
public class TcpTimestampOption extends TcpOption {

	/** The Constant ID. */
	public static final int ID = TcpOptionId.TCP_OPT_ID_TIMESTAMP;

	/**
	 * Instantiates a new tcp timestamp option.
	 */
	public TcpTimestampOption() {
		super(ID, TCP_OPTION_KIND_TIMESTAMP, TCP_OPTION_LEN_TIMESTAMP);
	}

	/**
	 * The TCP timestamp option timestamp field is a 32-bit field that contains the
	 * timestamp of the segment. The timestamp is the number of milliseconds since
	 * the TCP connection was established.
	 * <p>
	 * The timestamp field is used by both the sender and receiver to calculate the
	 * round-trip time (RTT) of packets sent over the connection. The RTT is the
	 * time it takes for a packet to travel from one end of the connection to the
	 * other and back again.
	 * </p>
	 * .
	 *
	 * @return the unsigned 32-bit field value
	 */
	@Meta
	public long tsValue() {
		return Integer.toUnsignedLong(buffer().getInt(2));
	}

	/**
	 * Gets the TCP Timestamp option timestamp echo field value.
	 * <p>
	 * The TCP timestamp option timestamp echo field is a 32-bit field that contains
	 * the timestamp of the segment that was acknowledged. The timestamp is the
	 * number of milliseconds since the TCP connection was established.
	 * </p>
	 * <p>
	 * The timestamp echo field is used by both the sender and receiver to calculate
	 * the round-trip time (RTT) of packets sent over the connection. The RTT is the
	 * time it takes for a packet to travel from one end of the connection to the
	 * other and back again.
	 * </p>
	 * <p>
	 * The RTT is used by the TCP congestion control algorithm to determine how
	 * quickly data can be sent over the connection. The congestion control
	 * algorithm uses the RTT to calculate the window size, which is the amount of
	 * data that the sender can send before it needs to wait for an acknowledgment.
	 * </p>
	 * <p>
	 * The timestamp echo field is supported by most modern TCP implementations. It
	 * is enabled by default in most operating systems and web browsers.
	 * </p>
	 *
	 * @return the unsigned 32-bit field value
	 */
	@Meta
	public long tsEchoReply() {
		return Integer.toUnsignedLong(buffer().getInt(6));
	}

}