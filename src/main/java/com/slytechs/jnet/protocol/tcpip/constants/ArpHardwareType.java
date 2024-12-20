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
package com.slytechs.jnet.protocol.tcpip.constants;

import java.util.function.IntSupplier;

import com.slytechs.jnet.platform.api.util.Enums;

/**
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 *
 */
public enum ArpHardwareType implements IntSupplier {
	RESERVED0(0),
	ETHERNET(1),
	EXP_ETHERNET_3MB(2),
	AX_25(3),
	PRO_NET_TOKEN_RING(4),
	CHAOS(5),
	IEEE_802_NETWORKS(6),
	ARCNET(7),
	HYPERCHANEL(8),
	LANSTAR(9),
	AUTONET(10),
	LOCALTALK(11),
	LOCALNET(12),
	ULTRA_LINK(13),
	SMDS(14),
	FRAME_RELAY(15),
	ATM1(16),
	HLDC(17),
	FIBRE_CHANNEL(18),
	ATM2(19),
	SERIAL_LINE(20),
	ATM3(21),
	MIL_STD_188_220(22),
	METRICOM(23),
	IEEE_1394(24),
	MAPOS(25),
	TWINAXIAL(26),
	EUI_64(27),
	HIPARP(28),
	IP_ARP_OVER_ISO_7816(29),
	ARPSEC(30),
	IPSEC_TUNNEL(31),
	INFINIBAND(32),
	CAI(33),
	WIEGAND(34),
	PURE_IP(35),
	HW_EXP1(46),
	HFI(37),
	UB(38),
	HW_EXP2(256),
	AETHERNET(257),
	RESERVED2(65535),

	;

	/**
	 * Resolve.
	 *
	 * @param type the type
	 * @return the string
	 */
	public static String resolve(Object type) {
		return Enums.resolve(type, ArpHardwareType.class);
	}

	private final int type;

	/**
	 * @param type
	 */
	ArpHardwareType(int type) {
		this.type = type;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return type;
	}

}
