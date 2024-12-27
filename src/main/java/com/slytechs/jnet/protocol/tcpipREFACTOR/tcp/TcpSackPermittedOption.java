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

import com.slytechs.jnet.protocol.api.meta.MetaResource;

/**
 * The TCP SACK-permitted option is a TCP option that indicates that the sender
 * is willing to receive SACK options from the receiver. This option is sent by
 * the sender in the SYN packet during the TCP connection setup.
 * <p>
 * The SACK-permitted option is a 1-byte option. The first bit of the option
 * indicates whether or not the sender is willing to receive SACK options. If
 * the bit is set to 1, then the sender is willing to receive SACK options. If
 * the bit is set to 0, then the sender is not willing to receive SACK options.
 * </p>
 * <p>
 * The SACK-permitted option is an important part of the TCP Selective
 * Acknowledgment (SACK) mechanism. SACK is a mechanism that allows the receiver
 * to acknowledge non-consecutive data, so that the sender can retransmit only
 * what is missing at the receiver's end.
 * </p>
 * <p>
 * If the sender is willing to receive SACK options, then the receiver can use
 * SACK to improve the reliability of the TCP connection. This is because SACK
 * allows the receiver to tell the sender which segments have been received,
 * even if they are not at the beginning of the receive window.
 * </p>
 * <p>
 * The SACK-permitted option is supported by most modern TCP implementations. It
 * is enabled by default in most operating systems and web browsers.
 * </p>
 * <p>
 * Here are some of the benefits of using SACK:
 * </p>
 * <ul>
 * <li>Reduced retransmissions: SACK can reduce the number of retransmissions
 * needed by the sender. This is because the sender only needs to retransmit the
 * segments that have not been received, rather than the entire window.</li>
 * <li>Increased throughput: SACK can increase the throughput of TCP connections
 * by allowing the sender to send more data before it needs to wait for an
 * acknowledgment.</li>
 * <li>Improved reliability: SACK can improve the reliability of TCP connections
 * by reducing the number of lost packets.</li>
 * </ul>
 * <p>
 * SACK is a valuable tool for improving the performance and reliability of TCP
 * connections. It can be used to improve the performance of web browsing, file
 * transfers, and other applications that use TCP.
 * </p>
 */
@MetaResource("tcp-opt-sack-perm-meta.json")
public class TcpSackPermittedOption extends TcpOption {
	/** The Constant ID. */
	public static final int ID = TcpOptionId.TCP_OPT_ID_SACK_PERMITTED;

	/**
	 * Instantiates a new tcp selective ack option.
	 */
	public TcpSackPermittedOption() {
		super(ID, TcpOptionConstants.TCP_OPTION_KIND_SACK_PERMITTED);
	}
}