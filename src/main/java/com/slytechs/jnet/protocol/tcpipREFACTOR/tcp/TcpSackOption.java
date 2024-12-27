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

import java.nio.ByteBuffer;

import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.tcpipREFACTOR.constants.CoreConstants;

/**
 * The TCP Selective Acknowledgment (SACK) option is a TCP option that allows
 * the receiver to acknowledge non-consecutive data, so that the sender can
 * retransmit only what is missing at the receiver's end.
 * <p>
 * SACK works by the receiver including a list of ranges of data that it has
 * received in its acknowledgment packets. The sender then uses this list to
 * determine which segments it needs to retransmit.
 * </p>
 * <p>
 * SACK is a more efficient way to handle lost data than the traditional
 * cumulative acknowledgment scheme used by TCP. With cumulative
 * acknowledgments, the sender only knows which segments have been received if
 * they are at the beginning of the receive window. With SACK, the sender can
 * know which segments have been received even if they are not at the beginning
 * of the receive window.
 * </p>
 * <p>
 * SACK is supported by most modern TCP implementations. It is enabled by
 * default in most operating systems and web browsers.
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
@MetaResource("tcp-opt-sack-meta.json")
public class TcpSackOption extends TcpOption {

	/** The Constant ID. */
	public static final int ID = TcpOptionId.TCP_OPT_ID_SACK;

	/**
	 * Instantiates a new tcp selective ack option.
	 */
	public TcpSackOption() {
		super(ID, CoreConstants.TCP_OPTION_KIND_SACK);
	}

	/**
	 * Gets the TCP SACK block field value.
	 * <p>
	 * The TCP SACK block field is a field in the TCP Selective Acknowledgment
	 * (SACK) option that is used to identify a range of data that has been received
	 * by the receiver. The SACK block field is 8 bytes long and consists of two
	 * 32-bit unsigned integers:
	 * </p>
	 * <ul>
	 * <li><b>Left edge of block:</b> This is the first sequence number of the range
	 * of data that has been received.</li>
	 * <li><b>Right edge of block:</b> This is the sequence number immediately
	 * following the last sequence number of the range of data that has been
	 * received.</li>
	 * </ul>
	 * <p>
	 * The SACK block field is used by the sender to determine which segments it
	 * needs to retransmit. If the sender receives a SACK option from the receiver
	 * that includes a SACK block, then the sender knows that the receiver has
	 * received all of the data in the range of sequence numbers specified by the
	 * SACK block. The sender can then discard any segments that are in the range of
	 * sequence numbers specified by the SACK block. The SACK block field is
	 * supported by most modern TCP implementations. It is enabled by default in
	 * most operating systems and web browsers.
	 * </p>
	 * <p>
	 * Here are some of the benefits of using the SACK block field:
	 * </p>
	 * <ul>
	 * <li>Reduced retransmissions: The SACK block field can reduce the number of
	 * retransmissions needed by the sender. This is because the sender only needs
	 * to retransmit the segments that have not been received, rather than the
	 * entire window.</li>
	 * <li>Increased throughput: The SACK block field can increase the throughput of
	 * TCP connections by allowing the sender to send more data before it needs to
	 * wait for an acknowledgment.</li>
	 * <li>Improved reliability: The SACK block field can improve the reliability of
	 * TCP connections by reducing the number of lost packets.</li>
	 * </ul>
	 * <p>
	 * The SACK block field is a valuable tool for improving the performance and
	 * reliability of TCP connections. It can be used to improve the performance of
	 * web browsing, file transfers, and other applications that use TCP.
	 * </p>
	 *
	 * @param index the index
	 * @return the int
	 */
	public int blockAt(int index) {
		return buffer().getInt(2 + (index << 3));
	}

	/**
	 * Count.
	 *
	 * @return the int
	 */
	public int blockCount() {
		return super.length() >> 3;
	}

	/**
	 * Copies all of the TCP SACK block values into a newly allocated array.
	 *
	 * @return the array containing all of this options block values
	 */
	public long[] blockToArray() {
		long[] array = new long[blockCount()];

		return blockToArray(array, 0);
	}

	/**
	 * Copies all of the TCP SACK block values into the supplied array.
	 *
	 * @param dst destination array where to copy all of the SACK block values
	 * @return the supplied array containing all of this options block values
	 */
	public long[] blockToArray(long[] dst) {
		return blockToArray(dst, 0);
	}

	/**
	 * Copies all of the TCP SACK block values into the supplied array.
	 *
	 * @param array  the array
	 * @param offset the offset into the array of the first index into the array
	 *               where to start the copy
	 * @return the supplied array containing all of this options block values
	 */
	public long[] blockToArray(long[] array, int offset) {
		ByteBuffer buffer = buffer();

		for (int i = 0; i < array.length; i++)
			array[i] = buffer.getLong(2 + (i << 3));

		return array;
	}

}