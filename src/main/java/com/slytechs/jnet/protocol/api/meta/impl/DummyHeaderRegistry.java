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
package com.slytechs.jnet.protocol.api.meta.impl;

import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.pack.PackId;
import com.slytechs.jnet.protocol.tcpip.ethernet.Ethernet;
import com.slytechs.jnet.protocol.tcpip.ip.Ip4;
import com.slytechs.jnet.protocol.tcpip.ip.Ip4IdOptions;
import com.slytechs.jnet.protocol.tcpip.ip.Ip4MtuProbeOption;
import com.slytechs.jnet.protocol.tcpip.ip.Ip4MtuReplyOption;
import com.slytechs.jnet.protocol.tcpip.ip.Ip4RouterAlertOption;
import com.slytechs.jnet.protocol.tcpip.ip.Ip4SecurityDefunctOption;
import com.slytechs.jnet.protocol.tcpip.ip.Ip4TimestampOption;
import com.slytechs.jnet.protocol.tcpip.tcp.Tcp;
import com.slytechs.jnet.protocol.tcpip.tcp.TcpMssOption;
import com.slytechs.jnet.protocol.tcpip.tcp.TcpNopOption;
import com.slytechs.jnet.protocol.tcpip.tcp.TcpSackPermittedOption;
import com.slytechs.jnet.protocol.tcpip.tcp.TcpTimestampOption;
import com.slytechs.jnet.protocol.tcpip.tcp.TcpWindowScaleOption;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class DummyHeaderRegistry implements HeaderRegistry {

	/**
	 * 
	 */
	public DummyHeaderRegistry() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.impl.HeaderRegistry#isOption(int)
	 */
	@Override
	public boolean isOption(int id) {
		return PackId.isOption(id);
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.impl.HeaderRegistry#lookupHeader(int)
	 */
	@Override
	public Header lookupHeader(int id) {

		return switch (id) {

		case CoreId.CORE_ID_ETHER -> new Ethernet();
		case CoreId.CORE_ID_IPv4 -> new Ip4();
		case CoreId.CORE_ID_TCP -> new Tcp();

		default -> throw new IllegalStateException("Unknown header ID 0x%08X".formatted(id));
		};
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.meta.impl.HeaderRegistry#lookupOption(int,
	 *      int)
	 */
	@Override
	public Header lookupOption(int id, int parentId) {

		return switch (parentId) {

		case CoreId.CORE_ID_TCP -> lookupTcpOption(id);
		case 0 -> lookupIp4Option(id);

		default -> throw new IllegalStateException("Unknown header ID 0x%08X".formatted(parentId));
		};
	}

	private Header lookupTcpOption(int optionId) {
		return switch (optionId) {

		case 0x02000103 -> new TcpMssOption();
		case 0x02000105 -> new TcpSackPermittedOption();
		case 0x02000107 -> new TcpTimestampOption();
		case 0x02000102 -> new TcpNopOption();
		case 0x02000104 -> new TcpWindowScaleOption();

		default -> throw new IllegalStateException("Unknown TCP option ID 0x%08X".formatted(optionId));
		};

	}
	

	private Header lookupIp4Option(int optionId) {
		return switch (optionId) {

		case Ip4IdOptions.IPv4_ID_OPT_RTRALT -> new Ip4RouterAlertOption();
		case Ip4IdOptions.IPv4_ID_OPT_TS -> new Ip4TimestampOption();
		case Ip4IdOptions.IPv4_ID_OPT_SEC -> new Ip4SecurityDefunctOption();
		case Ip4IdOptions.IPv4_ID_OPT_MTUP -> new Ip4MtuProbeOption();
		case Ip4IdOptions.IPv4_ID_OPT_MTUR -> new Ip4MtuReplyOption();

		default -> throw new IllegalStateException("Unknown TCP option ID 0x%08X".formatted(optionId));
		};

	}

}
