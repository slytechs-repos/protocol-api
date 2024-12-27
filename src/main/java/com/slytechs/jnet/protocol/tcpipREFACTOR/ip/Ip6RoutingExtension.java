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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ip;

import java.util.function.IntSupplier;

import com.slytechs.jnet.platform.api.util.Enums;
import com.slytechs.jnet.protocol.api.core.CoreId;

/**
 * IPv6 routing extension header.
 * <p>
 * The IPv6 routing header is an extension header that is used to specify a
 * specific route that an IPv6 packet should take.
 * </p>
 * <p>
 * The routing header is used by the sender to specify a list of IPv6 addresses
 * that the packet should visit. The packet is then forwarded to the first
 * address in the list. If the first address cannot be reached, the packet is
 * forwarded to the second address in the list, and so on.
 * </p>
 * <h2>Assigned routing types</h2>
 * <p>
 * The currently defined IPv6 Routing Headers and their status can be found at
 * [<a href=
 * "https://www.iana.org/assignments/ipv6-parameters/ipv6-parameters.xhtml#ipv6-parameters-3">IANA-RH</a>].
 * Allocation guidelines for IPv6 Routing Headers can be found in [RFC5871]. *
 * </p>
 */
public final class Ip6RoutingExtension extends Ip6ExtensionHeader {

	/**
	 * Routing header variant.
	 */
	public enum RoutingType implements IntSupplier {

		/** Source Route (DEPRECATED). [RFC2460] [RFC5095] */
		SOURCE_ROUTE(0),

		/** Nimrod (DEPRECATED 2009-05-06) */
		NIMROD(1),

		/** Type 2 Routing Header. [RFC6275] */
		TYPE2_ROUTING_HEADER(2),

		/** RPL Source Route Header. [RFC6554] */
		RPL_SOURCE_ROUTE_HEADER(3),

		/** Segment Routing Header (SRH). [RFC8754] */
		SEGMENT_ROUTING_HEADER(4),

		/**
		 * CRH-16 (TEMPORARY - registered 2021-06-07, extension registered 2022-04-25,
		 * expires 2023-06-07). [draft-bonica-6man-comp-rtg-hdr-26]
		 */
		CRH_16(5),

		/**
		 * CRH-32 (TEMPORARY - registered 2021-06-07, extension registered 2022-04-25,
		 * expires 2023-06-07). [draft-bonica-6man-comp-rtg-hdr-26]
		 */
		CRH_32(6),

		/** RFC3692-style Experiment 1. [RFC4727] */
		TYPE253(253),

		/** RFC3692-style Experiment 2. [RFC4727] */
		TYPE254(254),

		;

		public static String resolve(Object type) {
			return Enums.resolve(type, RoutingType.class);
		}

		/**
		 * Value of.
		 *
		 * @param type the type
		 * @return the routing type
		 */
		public static Ip6RoutingExtension.RoutingType valueOf(int type) {
			for (Ip6RoutingExtension.RoutingType t : values()) {
				if (t.type == type)
					return t;
			}

			return null;
		}

		/** The IANA assigned type. */
		private final int type;

		/**
		 * Instantiates a new routing type constant.
		 *
		 * @param type the type
		 */
		RoutingType(int type) {
			this.type = type;
		}

		/**
		 * Routing header type.
		 *
		 * @return an 8-bit routing header type identifier.
		 */
		@Override
		public int getAsInt() {
			return type;
		}

	}

	/** IPv6 routing extension header ID constant. */
	public static int ID = CoreId.CORE_ID_IPv6_EXT_ROUTING;

	/**
	 * Instantiates a new ip 6 routing option.
	 */
	public Ip6RoutingExtension() {
		super(ID);
	}

	/**
	 * Segments left.
	 *
	 * @return the int
	 */
	public int segmentsLeft() {
		return Byte.toUnsignedInt(buffer().get(3));
	}

	/**
	 * Type.
	 *
	 * @return the int
	 */
	public int routingType() {
		return Byte.toUnsignedInt(buffer().get(2));
	}
}