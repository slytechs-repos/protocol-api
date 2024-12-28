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
package com.slytechs.jnet.protocol.api.meta.impl;

import com.slytechs.jnet.platform.api.util.HexStrings;

/**
 * The Class MetaValues.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class MetaValues {

	/**
	 * Byte array formatter.
	 *
	 * @param array the array
	 * @return the string
	 */
	private static String byteArrayFormatter(byte[] array) {
		if (array.length == 6)
			return HexStrings.toMacString(array);

		if (array.length == 4)
			return HexStrings.toIp4String(array);

		if (array.length == 16)
			return HexStrings.toIp6String(array);

		return HexStrings.toHexString(array, 0, array.length);
	}

	/**
	 * Auto.
	 *
	 * @param target the target
	 * @return the string
	 */
	public static String auto(Object target) {
		if (target instanceof byte[] array)
			return byteArrayFormatter(array);

		return (target == null) ? "" : String.valueOf(target);
	}

	/**
	 * Mac address.
	 *
	 * @param target the target
	 * @return the string
	 */
	public static String macAddress(Object target) {
		if (target instanceof byte[] array)
			return HexStrings.toMacString(array);

		return (target == null) ? "" : String.valueOf(target);
	}

	/**
	 * Ip address.
	 *
	 * @param target the target
	 * @return the string
	 */
	public static String ipAddress(Object target) {
		if (target instanceof byte[] array)
			return HexStrings.toIpString(array);

		return (target == null) ? "" : String.valueOf(target);
	}

	/**
	 * Ip 4 address.
	 *
	 * @param target the target
	 * @return the string
	 */
	public static String ip4Address(Object target) {
		if (target instanceof byte[] array)
			return HexStrings.toIp4String(array);

		return (target == null) ? "" : String.valueOf(target);
	}

	/**
	 * Ip 6 address.
	 *
	 * @param target the target
	 * @return the string
	 */
	public static String ip6Address(Object target) {
		if (target instanceof byte[] array)
			return HexStrings.toIp6String(array);

		return (target == null) ? "" : String.valueOf(target);
	}

	/**
	 * Hex lowercase.
	 *
	 * @param target the target
	 * @return the string
	 */
	public static String hexLowercase(Object target) {
		if (target instanceof Number number)
			return Long.toHexString(number.longValue());

		return (target == null) ? "" : String.valueOf(target);
	}

	/**
	 * Hex uppercase.
	 *
	 * @param target the target
	 * @return the string
	 */
	public static String hexUppercase(Object target) {
		if (target instanceof Number number)
			return "%X".formatted(number);

		return (target == null) ? "" : String.valueOf(target);
	}

	/**
	 * Hex uppercase 0 x.
	 *
	 * @param target the target
	 * @return the string
	 */
	public static String hexUppercase0x(Object target) {
		if (target instanceof Number number)
			return "0x%X".formatted(number);

		return (target == null) ? "" : String.valueOf(target);
	}

	/**
	 * Hex lowercase 0 x.
	 *
	 * @param target the target
	 * @return the string
	 */
	public static String hexLowercase0x(Object target) {
		if (target instanceof Number number)
			return "0x%x".formatted(number);

		return (target == null) ? "" : String.valueOf(target);
	}

	/**
	 * None.
	 *
	 * @param target the target
	 * @return the string
	 */
	public static String none(Object target) {
		return "";
	}

}
