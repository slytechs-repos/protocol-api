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
package com.slytechs.jnet.protocol.tcpipREFACTOR.icmp;

import static com.slytechs.jnet.protocol.tcpipREFACTOR.icmp.IcmpConstants.*;

import com.slytechs.jnet.protocol.api.common.OptionsHeader;
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.api.pack.PackId;

/**
 * The Internet Control Message Protocol (ICMP) header.
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
@MetaResource("icmp-meta.json")
public sealed class Icmp extends OptionsHeader
		permits Icmp4, Icmp6 {

	/** The base ICMP header ID constant. */
	public static int ID = CoreId.CORE_ID_ICMP;

	/** The version. */
	private int version;

	/**
	 * Instantiates a new icmp.
	 */
	public Icmp() {
		super(ID);
	}

	/**
	 * Instantiates a new icmp.
	 *
	 * @param id the id
	 */
	protected Icmp(int id) {
		super(id);
	}

	/**
	 * Gets the ICMP type field value.
	 * <p>
	 * The ICMP type field is an 8-bit field in the ICMP header that specifies the
	 * type of ICMP message. The type field is used to identify the purpose of the
	 * ICMP message.
	 * </p>
	 * <p>
	 * The following are some of the most common ICMP types:
	 * </p>
	 * <ul>
	 * <li>Echo request (8): This message is used to test the reachability of a
	 * network device.</li>
	 * <li>Echo reply (0): This message is a response to an echo request
	 * message.</li>
	 * <li>Destination unreachable (3): This message is sent when a packet cannot be
	 * delivered to its destination.</li>
	 * <li>Redirect (5): This message is sent to inform a sender of a better route
	 * to its destination.</li>
	 * <li>Time exceeded (11): This message is sent when a packet has exceeded its
	 * TTL and is discarded.</li>
	 * <li>Source quench (4): This message is sent to inform a sender to slow down
	 * its transmission rate.</li>
	 * <li>Parameter problem (12): This message is sent to inform a sender that
	 * there is a problem with the IP header of a packet.</li>
	 * </ul>
	 *
	 * @return the unsigned 8-bit field value
	 */
	public int type() {
		return Byte.toUnsignedInt(buffer().get(ICMPv4_FIELD_TYPE));
	}

	/**
	 * The ICMP code field is an 8-bit field in the ICMP header that specifies the
	 * subtype of the ICMP message. The code field is used to provide additional
	 * information about the purpose of the ICMP message.
	 *
	 * 
	 * @return the unsigned 8-bit field value
	 */
	public int code() {
		return Byte.toUnsignedInt(buffer().get(ICMPv4_FIELD_CODE));
	}

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
	public int checksum() {
		return Short.toUnsignedInt(buffer().getShort(ICMPv4_FIELD_CHECKSUM));
	}

	/**
	 * On bind.
	 *
	 * @see com.slytechs.jnet.platform.api.common.binding.jnetruntime.MemoryBinding#onBind()
	 */
	@Override
	protected void onBind() {
		super.onBind();

		int effectiveId = getHeaderDescriptor().getEffectiveId();

		this.version = PackId.classmaskCheck(CoreId.CORE_CLASS_V4, effectiveId)
				? 4
				: 6;
	}

	/**
	 * ICMP header version. This value is derived from the the IP protocol or
	 * next-header field values.
	 *
	 * @return either integer version 4 or 6
	 */
	@Meta(MetaType.ATTRIBUTE)
	public int version() {
		return this.version;
	}
}
