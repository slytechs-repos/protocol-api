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
package com.slytechs.jnet.protocol.api.meta;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.slytechs.jnet.jnetruntime.time.Timestamp;
import com.slytechs.jnet.protocol.api.meta.MetaValue.ValueResolver;
import com.slytechs.jnet.protocol.api.meta.MetaValue.ValueResolverTuple2;
import com.slytechs.jnet.protocol.tcpip.constants.ArpHardwareType;
import com.slytechs.jnet.protocol.tcpip.constants.ArpOp;
import com.slytechs.jnet.protocol.tcpip.constants.EtherType;
import com.slytechs.jnet.protocol.tcpip.constants.Icmp4Code;
import com.slytechs.jnet.protocol.tcpip.constants.Icmp4Type;
import com.slytechs.jnet.protocol.tcpip.constants.Icmp6Mlr2RecordType;
import com.slytechs.jnet.protocol.tcpip.constants.Icmp6Type;
import com.slytechs.jnet.protocol.tcpip.constants.Ip4IdOptions;
import com.slytechs.jnet.protocol.tcpip.constants.Ip6IdOption;
import com.slytechs.jnet.protocol.tcpip.constants.IpType;
import com.slytechs.jnet.protocol.tcpip.constants.MacOuiAssignments;
import com.slytechs.jnet.protocol.tcpip.constants.TcpFlag;
import com.slytechs.jnet.protocol.tcpip.link.PppProtocol;

/**
 * Resolves field values to human readable representations. For example IP
 * addresses are can be resolved to host names, etc..
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 *
 */
@Repeatable(Resolvers.class)
@Retention(RUNTIME)
@Target({
		METHOD,
		FIELD
})
public @interface Resolver {

	/**
	 * The Enum ResolverType.
	 */
	public enum ResolverType {

		/** The none. */
		NONE((ValueResolver) null),

		/** The timestamp. */
		TIMESTAMP(Timestamp::formatTimestamp),

		/** The timestamp unit. */
		TIMESTAMP_UNIT(Timestamp::formatTimestampUnit),

		/** The timestamp in pcap micro. */
		TIMESTAMP_IN_PCAP_MICRO(Timestamp::formatTimestampPcapMicro),

		/** The timestamp in epoch milli. */
		TIMESTAMP_IN_EPOCH_MILLI(Timestamp::formatTimestampInEpochMilli),

		/** The ether type. */
		ETHER_TYPE(EtherType::resolve),

		/** The ip type. */
		IP_TYPE(IpType::resolve), // 48E3R20

		/** The ip option. */
		IPv4_OPT_TYPE(Ip4IdOptions::resolve),

		/** The I pv 6 OP T TYPE. */
		IPv6_OPT_TYPE(Ip6IdOption::resolve),

		/** The ip type. */
		ARP_OP(ArpOp::resolve),

		/** The arp hwtype. */
		ARP_HWTYPE(ArpHardwareType::resolve),

		/** The ether mac oui name. */
		ETHER_MAC_OUI_NAME(MacOuiAssignments::resolveMacOuiName),

		/** The ether mac oui name prefixed. */
		ETHER_MAC_OUI_NAME_PREFIXED(MacOuiAssignments::formatMacPrefixWithOuiName),

		/** The ether mac oui description. */
		ETHER_MAC_OUI_DESCRIPTION(MacOuiAssignments::resolveMacOuiDescription),

		/** The bitshift 1. */
		BITSHIFT_1(v -> DisplayUtil.bitshiftIntLeft(v, 1)),

		/** The bitshift 2. */
		BITSHIFT_2(v -> DisplayUtil.bitshiftIntLeft(v, 2)),

		/** The bitshift 3. */
		BITSHIFT_3(v -> DisplayUtil.bitshiftIntLeft(v, 3)),

		/** The bitshift 4. */
		BITSHIFT_4(v -> DisplayUtil.bitshiftIntLeft(v, 4)),

		/** The bitshift 5. */
		BITSHIFT_5(v -> DisplayUtil.bitshiftIntLeft(v, 5)),

		/** The bitshift 6. */
		BITSHIFT_6(v -> DisplayUtil.bitshiftIntLeft(v, 6)),

		/** The bitshift 7. */
		BITSHIFT_7(v -> DisplayUtil.bitshiftIntLeft(v, 7)),

		/** The bitshift 8. */
		BITSHIFT_8(v -> DisplayUtil.bitshiftIntLeft(v, 8)),

		/** The ICM pv 4 TYPE. */
		ICMPv4_TYPE(Icmp4Type::resolve),

		/** The ICM pv 6 TYPE. */
		ICMPv6_TYPE(Icmp6Type::resolve),

		/** The ICM pv 4 CODE. */
		ICMPv4_CODE(Icmp4Code::resolve),

		/** The tcp flags. */
		TCP_FLAGS(TcpFlag::resolve),

		/** The tcp flags bits. */
		TCP_BITS(TcpFlag::resolveBitFormat),

		/** The port lookup. */
		PORT_LOOKUP(o -> "UNKNOWN"),

		/** The MLRv2 record type. */
		MLRv2_TYPE(Icmp6Mlr2RecordType::resolve),

		/** PPP protocol type */
		PPP_PROTOCOL(PppProtocol::resolveProtocol),

		;

		/** The resolver. */
		private final ValueResolver resolver;

		/**
		 * Instantiates a new resolver type.
		 *
		 * @param resolver the resolver
		 */
		ResolverType(ValueResolver resolver) {
			this.resolver = resolver;
		}

		/**
		 * Instantiates a new resolver type.
		 *
		 * @param resolver the resolver
		 */
		ResolverType(ValueResolverTuple2 resolver) {
			this.resolver = new ValueResolver() {

				@Override
				public String resolveValue(Object value) {
					throw new UnsupportedOperationException("not implemented yet");
				}

				@Override
				public String resolveValue(MetaField field, Object value) {
					return resolver.resolveValue(field, value);
				}
			};
		}

		/**
		 * Gets the resolver.
		 *
		 * @return the resolver
		 */
		public ValueResolver getResolver() {
			return this.resolver;
		}

	}

	/**
	 * Value.
	 *
	 * @return the resolver type
	 */
	ResolverType value() default ResolverType.NONE;

	/**
	 * Resolver class.
	 *
	 * @return A value resolver compatible class which can be instantiated
	 */
	Class<? extends ValueResolver> resolverClass() default ValueResolver.class;

	/**
	 * If resolver does not exist or is unable to resolve the value, default to
	 * formatted value output {@link MetaField#getFormatted}.
	 *
	 * @return true, if successful
	 */
	boolean defaultToFormatted() default true;

	/**
	 * Default value.
	 *
	 * @return the string
	 */
	String defaultValue() default "";
}
