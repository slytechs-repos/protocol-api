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
package com.slytechs.jnet.protocol.tcpip.network;

import com.slytechs.jnet.protocol.tcpip.constants.CoreId;
import com.slytechs.jnet.protocol.tcpip.constants.IcmpEchoMessageType;

/**
 * Interface for all ICMP Echo messages.
 * 
 * <p>
 * ICMP echo messages, also known as ICMP echo requests and echo replies, are
 * used to test the reachability of a remote host. The echo request message is
 * sent by a host to a remote host, and the remote host responds with an echo
 * reply message. The echo request message contains a 16-bit identifier and a
 * 32-bit sequence number. The identifier is used to help the remote host match
 * the echo request message with the echo reply message. The sequence number is
 * used to help the local host track the sequence of echo request and echo reply
 * messages.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface IcmpEchoMessage {

	/**
	 * Identifier.
	 *
	 * @return the int
	 */
	int identifier();

	/**
	 * Sequence.
	 *
	 * @return the int
	 */
	int sequence();

	/**
	 * Checks if is request.
	 *
	 * @return true, if is request
	 */
	default boolean isRequest() {
		return (id() == CoreId.CORE_ID_ICMPv4_ECHO_REQUEST) || (id() == CoreId.CORE_ID_ICMPv6_ECHO_REQUEST);
	}

	/**
	 * Checks if the message type is reply.
	 *
	 * @return true, if is reply
	 */
	default boolean isReply() {
		return !isRequest();
	}

	/**
	 * Echo message type.
	 *
	 * @return message type constant of either REQUEST or REPLY.
	 */
	default IcmpEchoMessageType messageType() {
		return isRequest() ? IcmpEchoMessageType.REQUEST : IcmpEchoMessageType.REPLY;
	}

	/**
	 * The protocol header's unique numerical ID assigned at the pack level.
	 *
	 * @return the id
	 */
	int id();
}