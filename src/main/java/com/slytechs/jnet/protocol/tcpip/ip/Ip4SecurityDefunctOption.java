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
package com.slytechs.jnet.protocol.tcpip.ip;

/**
 * The IPv4 security option header (deprecated).
 * <p>
 * The IPv4 security option is a deprecated option that was used to provide
 * security for IPv4 packets. The security option is no longer used, as it is
 * considered to be insecure.
 * </p>
 * <p>
 * The security option is a 3-byte option with a type of 2. The first byte of
 * the option is the security type, which specifies the type of security that is
 * being used. The second byte of the option is the security level, which
 * specifies the level of security that is being used. The third byte of the
 * option is the security data, which contains the security-related information.
 * </p>
 */
public final class Ip4SecurityDefunctOption extends Ip4Option {

	/** The IPv4 security option header (defunct). */
	private static final int ID = Ip4IdOptions.IPv4_ID_OPT_SEC_DEF;

	/**
	 * Instantiates a new IPv4 security option header (deprecated).
	 */
	public Ip4SecurityDefunctOption() {
		super(ID);
	}

	/**
	 * The security type field is a 1-byte field that specifies the type of security
	 * that is being used
	 * 
	 * @return the type of security
	 */
	public int securityType() {
		return Byte.toUnsignedInt(buffer().get(3));
	}

	/**
	 * The security level field is a 1-byte field that specifies the level of
	 * security that is being used.
	 *
	 * @return the level of security
	 */
	public int securityLevel() {
		return Byte.toUnsignedInt(buffer().get(4));
	}

	/**
	 * The number of bytes available in the data security field.
	 *
	 * @return the length of the security data field in units of bytes
	 */
	public int securityDataLength() {
		return super.optionDataLength() - 4;
	}

	/**
	 * The security data field is a variable-length field that contains the
	 * security-related information.
	 *
	 * @param dst the destination array where field's variable length data is to be
	 *            written to
	 * @return the number of bytes written to the destination array
	 */
	public int securityData(byte[] dst) {
		return securityData(dst, 0, dst.length);
	}

	/**
	 * The security data field is a variable-length field that contains the
	 * security-related information.
	 *
	 * @param dst    the destination array where field's variable length data is to
	 *               be written to
	 * @param offset the offset into the destination array
	 * @return the number of bytes written to the destination array
	 */
	public int securityData(byte[] dst, int offset) {
		return securityData(dst, offset, dst.length - offset);
	}

	/**
	 * The security data field is a variable-length field that contains the
	 * security-related information.
	 *
	 * @param dst    the destination array where field's variable length data is to
	 *               be written to
	 * @param offset the offset into the destination array
	 * @param length the requested number of bytes to write into the destination
	 *               array but only the available number of bytes from the security
	 *               field will be written, if that length is less than the
	 *               requested length
	 * @return the number of bytes written to the destination array
	 */
	public int securityData(byte[] dst, int offset, int length) {
		int maxLen = securityDataLength() - offset;

		if (length > maxLen)
			length = maxLen;

		buffer().get(4, dst, offset, length);

		return length;
	}

}