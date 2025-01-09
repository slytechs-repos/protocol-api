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
package com.slytechs.jnet.protocol.tcpip.arp;

import java.util.concurrent.locks.Lock;

import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.MetaResource;

/**
 * Address Resolution Protocol (ARP) header.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
@MetaResource("/meta/tcpip/arp.yaml")
public final class Arp extends Header {

	/** Core protocol pack assigned header ID. */
	public static final int ID = CoreId.CORE_ID_ARP;

	/**
	 * Instantiates a new ARP header.
	 */
	public Arp() {
		super(ID);
	}

	/**
	 * Instantiates a new ARP header.
	 *
	 * @param lock new custom lock for synchronizing on this object, if desired
	 */
	public Arp(Lock lock) {
		super(ID, lock);
	}

	/**
	 * Hardware size.
	 *
	 * @return the int
	 */
	@Meta
	public int hardwareSize() {
		return Byte.toUnsignedInt(ArpLayout.HSIZE.getByte(buffer()));
	}

	/**
	 * Hardware type.
	 *
	 * @return the int
	 */
	@Meta
	public int hardwareType() {
		return Short.toUnsignedInt(ArpLayout.HTYPE.getShort(buffer()));
	}

	/**
	 * Opcode.
	 *
	 * @return the int
	 */
	@Meta
	public int opcode() {
		return Short.toUnsignedInt(ArpLayout.OPCODE.getShort(buffer()));
	}

	/**
	 * Operation get as enum.
	 *
	 * @return the arp op
	 */
	public ArpOp operationGetAsEnum() {
		return ArpOp.valueOfArpOp(opcode());
	}

	/**
	 * Protocol size.
	 *
	 * @return the int
	 */
	@Meta
	public int protocolSize() {
		return Byte.toUnsignedInt(ArpLayout.PSIZE.getByte(buffer()));
	}

	/**
	 * Protocol type.
	 *
	 * @return the int
	 */
	@Meta
	public int protocolType() {
		return Short.toUnsignedInt(ArpLayout.PTYPE.getShort(buffer()));
	}

	/**
	 * Sender mac address.
	 *
	 * @return the byte[]
	 */
	@Meta
	public byte[] senderMacAddress() {
		return senderMacAddress(new byte[ArpConstants.ARP_LEN_HALEN], 0);
	}

	/**
	 * Sender ha.
	 *
	 * @param dst the dst
	 * @return the byte[]
	 */
	public byte[] senderHa(byte[] dst) {
		return senderMacAddress(dst, 0);
	}

	/**
	 * Sender mac address.
	 *
	 * @param dst    the dst
	 * @param offset the offset
	 * @return the byte[]
	 */
	public byte[] senderMacAddress(byte[] dst, int offset) {
		buffer().get(ArpConstants.ARP_FIELD_SHA, dst, offset, ArpConstants.ARP_LEN_HALEN);
		return dst;
	}

	/**
	 * Sender protocol address.
	 *
	 * @return the byte[]
	 */
	@Meta
	public byte[] senderProtocolAddress() {
		return senderProtocolAddress(new byte[ArpConstants.ARP_LEN_PALEN], 0);
	}

	/**
	 * Sender protocol address.
	 *
	 * @param dst the dst
	 * @return the byte[]
	 */
	public byte[] senderProtocolAddress(byte[] dst) {
		return senderProtocolAddress(dst, 0);
	}

	/**
	 * Sender protocol address.
	 *
	 * @param dst    the dst
	 * @param offset the offset
	 * @return the byte[]
	 */
	public byte[] senderProtocolAddress(byte[] dst, int offset) {
		buffer().get(ArpConstants.ARP_FIELD_SPA, dst, offset, ArpConstants.ARP_LEN_PALEN);
		return dst;
	}

	/**
	 * Target mac address.
	 *
	 * @return the byte[]
	 */
	@Meta
	public byte[] targetMacAddress() {
		return targetMacAddress(new byte[ArpConstants.ARP_LEN_HALEN], 0);
	}

	/**
	 * Target mac address.
	 *
	 * @param dst the dst
	 * @return the byte[]
	 */
	public byte[] targetMacAddress(byte[] dst) {
		return senderMacAddress(dst, 0);
	}

	/**
	 * Target mac address.
	 *
	 * @param dst    the dst
	 * @param offset the offset
	 * @return the byte[]
	 */
	public byte[] targetMacAddress(byte[] dst, int offset) {
		buffer().get(ArpConstants.ARP_FIELD_THA, dst, offset, ArpConstants.ARP_LEN_HALEN);
		return dst;
	}

	/**
	 * Target protocol address.
	 *
	 * @return the byte[]
	 */
	@Meta
	public byte[] targetProtocolAddress() {
		return targetProtocolAddress(new byte[ArpConstants.ARP_LEN_PALEN], 0);
	}

	/**
	 * Target protocol address.
	 *
	 * @param dst the dst
	 * @return the byte[]
	 */
	public byte[] targetProtocolAddress(byte[] dst) {
		return targetProtocolAddress(dst, 0);
	}

	/**
	 * Target protocol address.
	 *
	 * @param dst    the dst
	 * @param offset the offset
	 * @return the byte[]
	 */
	public byte[] targetProtocolAddress(byte[] dst, int offset) {
		buffer().get(ArpConstants.ARP_FIELD_TPA, dst, offset, ArpConstants.ARP_LEN_PALEN);
		return dst;
	}

}
