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

import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.tcpip.constants.Ip6IdOption;

/**
 * IPv6 router alert option header.
 * <p>
 * The IPv6 router alert option is an option that can be included in an IPv6
 * packet to indicate that the packet should be handled specially by routers.
 * The router alert option is defined in RFC 2113.
 * </p>
 */
@MetaResource("ip6-opt-router-alert-meta.json")
public final class Ip6RouterAlertOption extends Ip6Option {

	/** The IPv4 Router Alert option ID constant. */
	public static final int ID = Ip6IdOption.IPv6_ID_OPT_ROUTER_ALERT;

	/**
	 * Instantiates a new ip 4 opt router.
	 */
	public Ip6RouterAlertOption() {
		super(ID);
	}

	/**
	 * Examine packet.
	 *
	 * @return true, if successful
	 */
	public boolean examinePacket() {
		return buffer().getShort(2) == 0;
	}

	/**
	 * Gets the IPv4 option router-alert field value.
	 * <p>
	 * The IPv4 router alert option field is a 2-byte field that is used to indicate
	 * that the packet should be handled specially by routers. The value of the
	 * field is an opaque identifier that is used by the router to determine how to
	 * handle the packet.
	 * </p>
	 * <p>
	 * The router alert option field was originally defined in RFC 2113 and is used
	 * by protocols such as RSVP (Resource Reservation Protocol) to indicate that
	 * the packet contains a reservation request. Routers that settingsSupport RSVP will
	 * inspect the packet and, if necessary, reserve resources along the path of the
	 * packet.
	 * </p>
	 * <p>
	 * The router alert option field can also be used by other protocols to indicate
	 * that the packet contains special handling instructions. For example, the
	 * IPsec protocol uses the router alert option field to indicate that the packet
	 * is encrypted.
	 * </p>
	 * <p>
	 * The router alert option field is a valuable tool for improving the
	 * performance and security of IP networks. By allowing protocols to indicate
	 * special handling instructions, the router alert option field allows routers
	 * to take the necessary steps to ensure that packets are delivered reliably and
	 * securely.
	 * </p>
	 *
	 * @return the unsigned 16-bit field value
	 */
	@Meta
	public int routerAlert() {
		return Short.toUnsignedInt(buffer().getShort(2));
	}

	/**
	 * Calculated alert description based on router alert field's value.
	 *
	 * @return a string describing the state of the router alert option
	 */
	@Meta(MetaType.ATTRIBUTE)
	public String routerAlertDescription() {
		return (routerAlert() == 0)
				? "Router shall examine packet (0)"
				: "Reserved";
	}
}