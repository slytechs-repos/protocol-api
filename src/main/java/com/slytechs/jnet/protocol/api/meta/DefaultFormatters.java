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

import java.util.function.Function;
import java.util.regex.Pattern;

import com.slytechs.jnet.platform.api.util.HexStrings;
import com.slytechs.jnet.platform.api.util.format.BitFormat;
import com.slytechs.jnet.protocol.api.meta.expression.impl.ExpressionPattern;
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

	public static String bitSetOrNotSet(Object value) {
		return ((Number) value).longValue() != 0
				? "Set"
				: "Not set";
	}

	public static String commonPortNumbers(Object o) {
		int port = ((Number) o).intValue();

		return switch (port) {
		case 20 -> "FTP Data";
		case 21 -> "FTP Control";
		case 22 -> "SSH";
		case 23 -> "Telnet";
		case 25 -> "SMTP";
		case 53 -> "DNS";
		case 67 -> "DHCP Server";
		case 68 -> "DHCP Client";
		case 69 -> "TFTP";
		case 80 -> "HTTP";
		case 110 -> "POP3";
		case 119 -> "NNTP";
		case 123 -> "NTP";
		case 135 -> "RPC Endpoint Mapper";
		case 136, 137, 138, 139 -> "NetBIOS Services";
		case 143 -> "IMAP";
		case 161 -> "SNMP";
		case 162 -> "SNMP Trap";
		case 443 -> "HTTPS";
		case 445 -> "SMB";
		case 465 -> "SMTPS";
		case 587 -> "SMTP (submission)";
		case 993 -> "IMAPS";
		case 995 -> "POP3S";
		case 1433 -> "Microsoft SQL Server";
		case 1521 -> "Oracle Database";
		case 3306 -> "MySQL";
		case 3389 -> "RDP (Remote Desktop)";
		case 5432 -> "PostgreSQL";
		case 6379 -> "Redis";
		case 8080 -> "HTTP Alternative";
		default -> "UNKNOWN";
		};
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

	private static final Pattern BITFORMAT_REGEX = Pattern.compile("^\\s*/(.+)/\\s*$");
	private static final Pattern EXPR_REGEX = Pattern.compile("^(.*?)\s*(=.*)$");

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.FormatRegistry#resolveFormat(java.lang.String)
	 */
	@Override
	public SpecificValueFormatter resolveFormat(String formatName) {
		if (formatName == null)
			return null;

		var bitFormatMatcher = BITFORMAT_REGEX.matcher(formatName);
		if (bitFormatMatcher.find()) {
			var bits = new BitFormat(bitFormatMatcher.group(1));

			return value -> {
				int intValue = ((Number) value).intValue();
				var result = bits.format(intValue);

				return result;
			};
		}

		/* Handle formatters with expression FORMAT? = EXPRESSION */
		var exprMatcher = EXPR_REGEX.matcher(formatName);
		if (exprMatcher.find()) {

			SpecificValueFormatter leftSide = exprMatcher.groupCount() == 2
					? resolveFormat(exprMatcher.group(1))
					: DefaultFormatters::any;

			String rightSide = exprMatcher.group(exprMatcher.groupCount() == 2 ? 2 : 1);

			ExpressionPattern exp = ExpressionPattern.compile(rightSide);

			return o -> {
				Function<String, Number> resolver = varName -> {
					return switch (varName) {
					case "value" -> ((Number) o);

					default -> throw new IllegalStateException("unresolved value reference in expression ");
					};
				};

				var eval = exp.evaluator(resolver);
				int expressionResult = eval.run(o);

				return leftSide.applyFormat(expressionResult);
			};

		}

		if (formatName.contains("%"))
			return o -> formatName.formatted(o);

		return switch (formatName) {

		case "any" -> DefaultFormatters::any;
		case "bits" -> o -> "" + any(((Number) o).longValue() << 3) + " bits";
		case "bytes" -> o -> "" + any(o) + " bytes";
		case "ETHER_TYPE" -> EtherType::resolve;
		case "ETHER_MAC_OUI_NAME" -> MacOuiAssignments::resolveMacOuiName;
		case "ETHER_MAC_OUI_NAME_PREFIXED" -> MacOuiAssignments::formatMacPrefixWithOuiName;
		case "ETHER_MAC_OUI_DESCRIPTION" -> MacOuiAssignments::resolveMacOuiDescription;
		case "IP_TYPE" -> IpType::resolve;
		case "PORT_LOOKUP" -> DefaultFormatters::commonPortNumbers;
		case "TCP_BITS" -> TcpFlag::resolveBitFormat;
		case "TCP_FLAGS" -> TcpFlag::resolve;
		case "DOUBLE_QUOTES" -> o -> new StringBuilder("\"").append(o.toString()).append('"').toString();
		case "SINGLE_QUOTES" -> o -> new StringBuilder("\'").append(o.toString()).append('\'').toString();
		case "BACK_QUOTES", "BACK_TICKS" -> o -> new StringBuilder("`").append(o.toString()).append('`').toString();
		case "BIT_SET_OR_NOT_SET" -> DefaultFormatters::bitSetOrNotSet;

		default -> DefaultFormatters::any;
		};

	}

}
