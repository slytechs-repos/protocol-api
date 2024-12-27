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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ppp;

import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.tcpipREFACTOR.constants.CoreId;

/**
 * PPP (Point-to-Point Protocol) header implementation.
 * 
 * The PPP header consists of the following fields:
 * 
 * <pre>
 * - Flag (8 bits) 
 * - Address (8 bits) 
 * - Control (8 bits) 
 * - Protocol (16 bits)
 * </pre>
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("ppp-meta.json")
public final class Ppp extends Header {
	
	public static String resolveProtocol(Object protocol) {
		return "";
	}

	/** Standard PPP header length */
	private static final int HEADER_LEN = 4;

	/**
	 * Instantiates a new PPP header.
	 */
	public Ppp() {
		super(CoreId.CORE_ID_PPP);
	}

	/**
	 * Gets the flag field value (8 bits).
	 *
	 * @return the flag value
	 */
	@Meta
	public int flag() {
		return PppLayout.FLAG.getUnsignedByte(buffer());
	}

	/**
	 * Gets the address field value (8 bits).
	 *
	 * @return the address value
	 */
	@Meta
	public int address() {
		return PppLayout.ADDRESS.getUnsignedByte(buffer());
	}

	/**
	 * Gets the control field value (8 bits).
	 *
	 * @return the control value
	 */
	@Meta
	public int control() {
		return PppLayout.CONTROL.getUnsignedByte(buffer());
	}

	/**
	 * Gets the protocol field value (16 bits).
	 *
	 * @return the protocol value
	 */
	@Meta
	public int protocol() {
		return PppLayout.PROTOCOL.getUnsignedShort(buffer());
	}

	/**
	 * Sets the flag field value (8 bits).
	 *
	 * @param value the new flag value
	 */
	public void setFlag(int value) {
		PppLayout.FLAG.setInt(value, buffer());
	}

	/**
	 * Sets the address field value (8 bits).
	 *
	 * @param value the new address value
	 */
	public void setAddress(int value) {
		PppLayout.ADDRESS.setInt(value, buffer());
	}

	/**
	 * Sets the control field value (8 bits).
	 *
	 * @param value the new control value
	 */
	public void setControl(int value) {
		PppLayout.CONTROL.setInt(value, buffer());
	}

	/**
	 * Sets the protocol field value (16 bits).
	 *
	 * @param value the new protocol value
	 */
	public void setProtocol(int value) {
		PppLayout.PROTOCOL.setInt(value, buffer());
	}
}
