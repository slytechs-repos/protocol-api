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

import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.tcpip.constants.CoreId;

/**
 * The Internet Control Message Protocol (ICMP) version 4 header.
 * <p>
 * The Internet Control Message Protocol (ICMP) is a network layer protocol used
 * by network devices to communicate error messages and operational information
 * indicating success or failure when communicating with another IP address. For
 * example, an error is indicated when a requested service is not available or
 * that a host or router could not be reached.
 * </p>
 * <p>
 * Technical details. ICMP is part of the Internet protocol suite as defined in
 * RFC 792. ICMP messages are typically used for diagnostic or control purposes
 * or generated in response to errors in IP operations (as specified in RFC
 * 1122). ICMP errors are directed to the source IP address of the originating
 * packet. For example, every device (such as an intermediate router) forwarding
 * an IP datagram first decrements the time to live (TTL) field in the IP header
 * by one. If the resulting TTL is 0, the packet is discarded and an ICMP time
 * exceeded in transit message is sent to the datagram's source address.
 * </p>
 * <p>
 * ICMP messages are used for a variety of purposes, including:
 * </p>
 * <ul>
 * <li>Echo request and reply: These messages are used to test the reachability
 * of a network device.</li>
 * <li>Destination unreachable: This message is sent when a packet cannot be
 * delivered to its destination.</li>
 * <li>Redirect: This message is sent to inform a sender of a better route to
 * its destination.</li>
 * <li>Time exceeded: This message is sent when a packet has exceeded its TTL
 * and is discarded.</li>
 * <li>Source quench: This message is sent to inform a sender to slow down its
 * transmission rate.</li>
 * <li>Parameter problem: This message is sent to inform a sender that there is
 * a problem with the IP header of a packet.</li>
 * </ul>
 * <p>
 * ICMP is a vital part of the Internet protocol suite and is used by a variety
 * of applications, including:
 * </p>
 * <ul>
 * <li>Ping: This application is used to test the reachability of a network
 * device.</li>
 * <li>Traceroute: This application is used to trace the path that a packet
 * takes to its destination.</li>
 * <li>Network monitoring: ICMP can be used to monitor the health of a network
 * by detecting errors and problems.</li>
 * <li>Denial-of-service attacks: ICMP can be used to launch denial-of-service
 * attacks by flooding a network with ICMP packets.</li>
 * </ul>
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
@MetaResource("icmp4-meta.json")
public sealed class Icmp4 extends Icmp
		permits Icmp4Echo {

	/** The base ICMP v4 header ID constant. */
	public static final int ID = CoreId.CORE_ID_ICMPv4;

	/**
	 * Instantiates a new icmp 4.
	 */
	public Icmp4() {
		super(ID);
	}

	/**
	 * Instantiates a new icmp 4.
	 *
	 * @param id the id
	 */
	protected Icmp4(int id) {
		super(id);
	}

}
