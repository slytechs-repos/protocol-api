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
 * IPv4 MTU reply option header.
 * <p>
 * The IPv4 MTU reply option is used to reply to an MTU probe option. The MTU
 * reply option is a 3-byte option with a type of 42. The first byte of the
 * option is the MTU reply ID, which is the same ID as the MTU probe option that
 * it is replying to. The second byte of the option is the MTU reply length,
 * which specifies the length of the option in bytes. The third byte of the
 * option is the MTU reply value, which is the MTU of the path between the
 * sender and the receiver.
 * </p>
 * <p>
 * When a router receives a packet with an MTU probe option, it will reply with
 * an MTU reply option if it cannot forward the packet without fragmenting it.
 * The MTU reply option will contain the MTU of the router that cannot forward
 * the packet. The sender can then use this information to determine the MTU of
 * the path between the sender and the receiver.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public final class Ip4MtuReplyOption extends Ip4Option {

	/** The IPv4 MTU reply option header ID constant. */
	public static final int ID = Ip4IdOptions.IPv4_OPTION_TYPE_MTUR;

	/**
	 * IPv4 reply probe option header.
	 */
	public Ip4MtuReplyOption() {
		super(ID);
	}

	/**
	 * The MTU reply ID field is a 1-byte field that is the same ID as the MTU probe
	 * option that it is replying to *
	 * 
	 * @return the 8-bit probe ID field value
	 */
	public int replyId() {
		return Byte.toUnsignedInt(buffer().get(2));
	}

	/**
	 * The MTU reply value field is a 2-byte field that is the MTU of the path
	 * between the sender and the receiver *
	 * 
	 * @return the unsigned 16-bit MTU discovery/probe value
	 */
	public int replyMtu() {
		return Short.toUnsignedInt(buffer().getShort(3));
	}
}
