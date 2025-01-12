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

import com.slytechs.jnet.platform.api.util.CountUnit;
import com.slytechs.jnet.platform.api.util.HexStrings;
import com.slytechs.jnet.platform.api.util.format.BitFormat;
import com.slytechs.jnet.protocol.api.meta.expression.impl.ExprValue;
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
public class DefaultFormats implements FormatRegistry {

	public static final String ANY = "any";

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

	public static String macAddress(Object value) {
		return MacAddress.toMacAddressString((byte[]) value);
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

	public DefaultFormats() {
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
//	private static final PlaceholderPattern EXPR_REGEX = PlaceholderPattern.compile("^(.*?)\s*(=.*)$");

	private static final String EXPR = "^(.*?)\\s*(=|[+\\-*/%&|^]=|<<=|>>=|>>>=)\\s*(.*)$";
	private static final Pattern EXPR_REGEX = Pattern.compile(EXPR);

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
		var expressionMatcher = EXPR_REGEX.matcher(formatName);
		if (expressionMatcher.find()) {

			// If 3 groups, we have both format reference (left) and expression
			// (middle+right)
			boolean hasLeft = expressionMatcher.groupCount() == 3;
			String left = hasLeft ? expressionMatcher.group(1) : ANY;
			String middle = hasLeft ? expressionMatcher.group(2) : expressionMatcher.group(1);
			String right = hasLeft ? expressionMatcher.group(3) : expressionMatcher.group(2);

			SpecificValueFormatter leftSide = resolveFormat(left);

			String expression = middle + " " + right;
			ExpressionPattern pattern;

//			System.err.println();
//			System.err.println("INFO: DefaultFormats:: regex=\"" + EXPR + "\"");
//			System.err.println("INFO: DefaultFormats:: input=" + formatName);
//			System.err.println("INFO: DefaultFormats:: matcher=" + expressionMatcher);
//			System.err.println("INFO: DefaultFormats:: left=" + left);
//			System.err.println("INFO: DefaultFormats:: middle=" + middle);
//			System.err.println("INFO: DefaultFormats:: right=" + right);
//			System.err.println("INFO: DefaultFormats:: expression=\"" + expression + "\"");

			try {
				pattern = ExpressionPattern.compile(expression);
			} catch (RuntimeException e) {
				System.err.println("ERROR: DefaultFormats:: regex=\"" + EXPR + "\"");
				System.err.println("ERROR: DefaultFormats:: input=" + formatName);
				System.err.println("ERROR: DefaultFormats:: matcher=" + expressionMatcher);
				System.err.println("ERROR: DefaultFormats:: left=" + left);
				System.err.println("ERROR: DefaultFormats:: middle=" + middle);
				System.err.println("ERROR: DefaultFormats:: right=" + right);
				System.err.println("ERROR: DefaultFormats:: expression=\"" + expression + "\"");
				e.printStackTrace();
				throw e;
			}

			return o -> {
				Function<String, Number> resolver = varName -> {
					return switch (varName) {
					case "value" -> ((Number) o);

					default -> throw new IllegalStateException("unresolved value reference in expression ");
					};
				};

				var eval = pattern.evaluator(resolver);
				ExprValue expressionResult = eval.run(o);

//				System.err.println("INFO: DefaultFormats:: expressionResult=\"" + expressionResult + "\"");

				return leftSide.applyFormat(expressionResult.get());
			};
		}

		if (formatName.contains("%"))
			return o -> formatName.formatted(o);

		if (formatName.startsWith("\"")) {

			// Strip the double-qoutes
			var str = formatName.trim();
			str = str.substring(1, formatName.length() - 1);

			var newFormat = str;

			return o -> newFormat.formatted(o);
		}

		return switch (formatName) {

		case "any" -> o -> DefaultFormats.any(o);
		case "Any" -> o -> capitalizeFirstLetter(DefaultFormats.any(o));
		case "ANY" -> o -> DefaultFormats.any(o).toUpperCase();

		case "is_set" -> o -> longValue(o) != 0 ? "set" : "not set";
		case "Is_set" -> o -> longValue(o) != 0 ? "Set" : "Not set";
		case "Is_Set" -> o -> longValue(o) != 0 ? "Set" : "Not Set";
		
		case "bits" -> o -> "" + any(longValue(o) << 3) + " bits";
		case "Bits" -> o -> "" + any(longValue(o) << 3) + " Bits";
		case "bytes" -> o -> "" + any(o) + " bytes";
		case "Bytes" -> o -> "" + any(o) + " Bytes";
		
		case "bytes-scaled" -> o -> CountUnit.formatScaled("%.1f %sbytes", longValue(o));
		case "Bytes-scaled" -> o -> CountUnit.formatScaled("%.1f %sBytes", longValue(o));
		case "bits-scaled" -> o -> CountUnit.formatScaled("%.1f %sbits", longValue(o));
		case "Bits-scaled" -> o -> CountUnit.formatScaled("%.1f %sBits", longValue(o));
		case "scaled" -> o -> CountUnit.formatScaled("%.1f %s", longValue(o));
		case "count" -> o -> any(CountUnit.scaleUnit(longValue(o)).value1());
		case "unit" -> o -> CountUnit.scaleUnit(longValue(o)).value2().getSymbol();

		case "ETHER_MAC" -> DefaultFormats::macAddress;
		case "ETHER_TYPE" -> EtherType::resolve;
		case "ETHER_MAC_OUI_NAME" -> MacOuiAssignments::resolveMacOuiName;
		case "ETHER_MAC_OUI_NAME_PREFIXED" -> MacOuiAssignments::formatMacPrefixWithOuiName;
		case "ETHER_MAC_OUI_DESCRIPTION" -> MacOuiAssignments::resolveMacOuiDescription;
		case "IP_TYPE" -> IpType::resolve;
		case "PORT_LOOKUP" -> DefaultFormats::commonPortNumbers;
		case "TCP_BITS" -> TcpFlag::resolveBitFormat;
		case "TCP_FLAGS" -> TcpFlag::resolve;
		case "DOUBLE_QUOTES" -> o -> new StringBuilder("\"").append(o.toString()).append('"').toString();
		case "SINGLE_QUOTES" -> o -> new StringBuilder("\'").append(o.toString()).append('\'').toString();
		case "BACK_QUOTES", "BACK_TICKS" -> o -> new StringBuilder("`").append(o.toString()).append('`').toString();

		default -> o -> UNRESOLVED.formatted(DefaultFormats.any(o), formatName);
		};

	}

	public static String capitalizeFirstLetter(String str) {
		if (str == null || str.isEmpty()) {
			return str; // Return the original string if it's null or empty
		}
		char firstChar = Character.toUpperCase(str.charAt(0));

		var sb = new StringBuilder(str);
		sb.setCharAt(0, firstChar);

		return sb.toString();
	}

	private static Number numberValue(Object value) {
		return (Number) value;
	}

	private static int intValue(Object value) {
		return numberValue(value).intValue();
	}

	private static long longValue(Object value) {
		return numberValue(value).longValue();
	}

	private static final String UNRESOLVED = "%s <=!FORMAT(\":%s\")!";
}
