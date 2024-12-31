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
package com.slytechs.jnet.protocol.tcpip.icmp;

/**
 * Interface for all Internet Control Message Protocol (ICMP) messages.
 * 
 * <p>
 * The ICMP message header is a 8-byte header that is used to carry ICMP
 * messages. It is located immediately after the IPv4 or IPv6 header. The ICMP
 * message header contains the following fields:
 * </p>
 * <ul>
 * <li><b>Type:</b> This field identifies the type of ICMP message. There are
 * many different types of ICMP messages, each with its own purpose.</li>
 * <li><b>Code:</b> This field provides additional information about the ICMP
 * message. The meaning of the code field depends on the type of ICMP message.
 * </li>
 * <li><b>Checksum:</b> This field is used to verify the integrity of the ICMP
 * message. The checksum is calculated over the entire ICMP message, including
 * the header and the data.</li>
 * </ul>
 * 
 * <p>
 * The following table lists some of the most common types of ICMP messages:
 * </p>
 * <ul>
 * <li><b>Echo Request:</b> This message is used to request a response from a
 * remote host.</li>
 * <li><b>Echo Reply:</b> This message is used to respond to an Echo Request
 * message.</li>
 * <li><b>Destination Unreachable:</b> This message is used to indicate that a
 * packet could not be delivered to its destination.</li>
 * <li><b>Time Exceeded:</b> This message is used to indicate that a packet
 * exceeded its TTL (Time to Live).</li>
 * <li><b>Parameter Problem:</b> This message is used to indicate that a packet
 * contained an invalid IP header field.</li>
 * </ul>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface IcmpMessage {

	/**
	 * This field identifies the type of ICMP message. There are many different
	 * types of ICMP messages, each with its own purpose.
	 *
	 * @return the type field value
	 */
	int type();

	/**
	 * The ICMP code field is an 8-bit field in the ICMP header that specifies the
	 * subtype of the ICMP message. The code field is used to provide additional
	 * information about the purpose of the ICMP message.
	 *
	 * 
	 * @return the unsigned 8-bit field value
	 */
	int code();

	/**
	 * The ICMP checksum field is a 16-bit field in the ICMP header that is used to
	 * verify the integrity of the ICMP message. The checksum is calculated by
	 * adding the 16-bit values of all the fields in the ICMP header and the data
	 * field, and then taking the one's complement of the result.
	 * <p>
	 * If the checksum of an ICMP message does not match, the message is discarded.
	 * This helps to prevent errors from occurring when ICMP messages are
	 * transmitted over the network.
	 * </p>
	 * <p>
	 * The ICMP checksum field is a valuable tool for troubleshooting network
	 * problems. By verifying the checksum of an ICMP message, you can quickly
	 * identify if the message has been corrupted in transit.
	 * </p>
	 *
	 * @return the unsigned 16-bit field value
	 */
	int checksum();
}
