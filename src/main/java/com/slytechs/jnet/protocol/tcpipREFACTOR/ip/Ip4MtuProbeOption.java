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

/**
 * IPv4 MTU probe optional header.
 * <p>
 * The IPv4 MTU probe option is used to probe the MTU of the path between the
 * sender and the receiver. The MTU probe option is a 3-byte option with a type
 * of 41. The first byte of the option is the MTU probe ID, which is a unique
 * identifier for the probe. The second byte of the option is the MTU probe
 * length, which specifies the length of the option in bytes. The third byte of
 * the option is the MTU probe value, which is the MTU that the sender is
 * probing for.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public final class Ip4MtuProbeOption extends Ip4Option {

	/** The IPv4 MTU probe option header ID constant. */
	public static final int ID = Ip4IdOptions.IPv4_ID_OPT_MTUP;

	/**
	 * IPv4 MTU probe option header.
	 */
	public Ip4MtuProbeOption() {
		super(ID);
	}

	/**
	 * The MTU probe ID field is a 1-byte field that is a unique identifier for the
	 * probe.
	 * 
	 * @return the 8-bit probe ID field value
	 */
	public int probeId() {
		return Byte.toUnsignedInt(buffer().get(2));
	}

	/**
	 * The MTU probe value field is a 2-byte field that is the MTU that the sender
	 * is probing for.
	 * 
	 * @return the unsigned 16-bit MTU discovery/probe value
	 */
	public int probeMtu() {
		return Short.toUnsignedInt(buffer().getShort(3));
	}
}
