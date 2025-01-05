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
package com.slytechs.jnet.protocol.api.meta;

import com.slytechs.jnet.platform.api.util.HexStrings;
import com.slytechs.jnet.platform.api.util.format.BitFormat;
import com.slytechs.jnet.protocol.tcpip.ethernet.EtherType;
import com.slytechs.jnet.protocol.tcpip.ethernet.MacAddress;
import com.slytechs.jnet.protocol.tcpip.ethernet.impl.MacOuiAssignments;
import com.slytechs.jnet.protocol.tcpip.ip.IpAddress;
import com.slytechs.jnet.protocol.tcpip.ip.IpType;
import com.slytechs.jnet.protocol.tcpip.tcp.TcpFlag;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class DefaultFormatters implements FormatRegistry {

	public static String any(Object value) {
		if (value == null)
			return "";

		return switch (value) {

		case byte[] arr -> array(arr);
		case Float _, Double _ -> "%f". formatted(value);
		case Number i -> "%,d".formatted(i);
		case String str -> new StringBuilder("\"").append(str).append('"').toString();

		default -> String.valueOf(value);
		};
	}

	public static String array(byte[] arr) {
		return switch (arr.length) {
		case 4 -> IpAddress.toIp4AddressString(arr);
		case 16 -> IpAddress.toIp6AddressString(arr);
		case 6 -> MacAddress.toMacAddressString(arr);

		default -> HexStrings.toHexString(arr);
		};
	}

	public static String hex(Object value) {
		Number num = (Number) value;

		return Long.toHexString(num.longValue());
	}

	public static String etherType(Object value) {
		return EtherType.resolve(value);
	}

	public static String bitLeftShift(Object value, int shiftCount) {
		if (value instanceof Number num) {
			return Long.toString(num.longValue() << shiftCount);
		}

		return value.toString();
	}

	public static String bitRightShift(Object value, int shiftCount) {
		if (value instanceof Number num) {
			return Long.toString(num.longValue() >> shiftCount);
		}

		return value.toString();
	}

	public DefaultFormatters() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.FormatRegistry#applyFormat(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public String applyFormat(Object value, String formatName) {
		var fmt = resolveFormat(formatName);
		if (fmt == null)
			return null;

		return fmt.applyFormat(value, formatName);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.FormatRegistry#resolveFormat(java.lang.String)
	 */
	@Override
	public SpecificValueFormatter resolveFormat(String formatName) {
		if (formatName == null)
			return null;

		if (formatName.matches("^[01. ]+$")) {
			var bits = new BitFormat(formatName);

			return o -> bits.format(o);
		}

		if (formatName.startsWith("<<"))
			return o -> bitLeftShift(o, Integer.parseInt(formatName.substring(2)));

		if (formatName.startsWith(">>"))
			return o -> bitRightShift(o, Integer.parseInt(formatName.substring(2)));

		if (formatName.startsWith("%"))
			return o -> formatName.formatted(o);

		return switch (formatName.toUpperCase()) {

		case "ANY" -> DefaultFormatters::any;
		case "HEX" -> DefaultFormatters::hex;
		case "IN_BITS", "BITS" -> o -> bitLeftShift(o, 3);
		case "ETHER_TYPE" -> EtherType::resolve;
		case "ETHER_MAC_OUI_NAME" -> MacOuiAssignments::resolveMacOuiName;
		case "ETHER_MAC_OUI_NAME_PREFIXED" -> MacOuiAssignments::formatMacPrefixWithOuiName;
		case "ETHER_MAC_OUI_DESCRIPTION" -> MacOuiAssignments::resolveMacOuiDescription;
		case "IP_TYPE" -> IpType::resolve;
		case "PORT_LOOKUP" -> o -> "UNKNOWN";
		case "TCP_BITS" -> TcpFlag::resolveBitFormat;
		case "TCP_FLAGS" -> TcpFlag::resolve;
		case "NO_QUOTES" -> o -> o.toString();
		case "DOUBLE_QUOTES" -> o -> new StringBuilder("\"").append(o.toString()).append('"').toString();
		case "SINGLE_QUOTES" -> o -> new StringBuilder("\'").append(o.toString()).append('\'').toString();
		case "BACK_QUOTES", "BACK_TICKS" -> o -> new StringBuilder("`").append(o.toString()).append('`').toString();

		default -> DefaultFormatters::any;
		};

	}

}
