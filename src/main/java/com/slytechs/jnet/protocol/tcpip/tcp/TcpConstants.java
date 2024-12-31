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
package com.slytechs.jnet.protocol.tcpip.tcp;

/**
 * Interface defining constants for the Transmission Control Protocol (TCP). TCP
 * is a core protocol of the Internet Protocol Suite, providing reliable,
 * ordered, and error-checked delivery of data between applications.
 * 
 * <p>
 * This interface includes constants for TCP header fields and flags used in TCP
 * packet structures.
 * </p>
 * 
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface TcpConstants {

	/* Header Constants */

	/**
	 * Length of the TCP header in bytes.
	 */
	int TCP_HEADER_LEN = 20;

	/**
	 * Offset of the Internet Header Length (IHL) field in the TCP header.
	 */
	int TCP_FIELD_IHL = 12;

	/**
	 * Offset of the Source Port field in the TCP header.
	 */
	int TCP_FIELD_SRC = 0;

	/**
	 * Offset of the Destination Port field in the TCP header.
	 */
	int TCP_FIELD_DST = 2;

	/**
	 * Offset of the Sequence Number field in the TCP header.
	 */
	int TCP_FIELD_SEQ = 4;

	/**
	 * Offset of the Acknowledgment Number field in the TCP header.
	 */
	int TCP_FIELD_ACK = 8;

	/**
	 * Offset of the Data Offset field in the TCP header.
	 */
	int TCP_FIELD_OFFSET = 12;

	/**
	 * Offset of the Flags field in the TCP header.
	 */
	int TCP_FIELD_FLAGS = 13;

	/**
	 * Offset of the Window Size field in the TCP header.
	 */
	int TCP_FIELD_WINDOW = 14;

	/**
	 * Offset of the Checksum field in the TCP header.
	 */
	int TCP_FIELD_CHECKSUM = 16;

	/**
	 * Offset of the Urgent Pointer field in the TCP header.
	 */
	int TCP_FIELD_URGENT = 18;

	/* TCP Flags */

	/**
	 * FIN (Finish) flag.
	 */
	int TCP_FLAG_FIN = 0x01;

	/**
	 * SYN (Synchronize) flag.
	 */
	int TCP_FLAG_SYN = 0x02;

	/**
	 * RST (Reset) flag.
	 */
	int TCP_FLAG_RST = 0x04;

	/**
	 * PSH (Push) flag.
	 */
	int TCP_FLAG_PSH = 0x08;

	/**
	 * ACK (Acknowledgment) flag.
	 */
	int TCP_FLAG_ACK = 0x10;

	/**
	 * URG (Urgent) flag.
	 */
	int TCP_FLAG_URG = 0x20;

	/**
	 * ECE (Explicit Congestion Notification Echo) flag.
	 */
	int TCP_FLAG_ECE = 0x40;

	/**
	 * CWR (Congestion Window Reduced) flag.
	 */
	int TCP_FLAG_CWR = 0x80;

	/**
	 * ECN (Explicit Congestion Notification) flag.
	 */
	int TCP_FLAG_ECN = 0x40;

	/**
	 * Congestion flag.
	 */
	int TCP_FLAG_CONG = 0x80;

}
