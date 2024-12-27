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
package com.slytechs.jnet.protocol.tcpipREFACTOR.tcp;

import com.slytechs.jnet.protocol.api.meta.MetaResource;

/**
 * 
 * The TCP option nop is a 1-byte option that does nothing. It is used to pad
 * the options field in the TCP header to an even 32-bit boundary. This is
 * sometimes done to improve the performance of TCP connections.
 */
@MetaResource("tcp-opt-nop-meta.json")
public class TcpNopOption extends TcpOption {

	/** The Constant ID. */
	public static final int ID = TcpOptionId.TCP_OPT_ID_NOP;

	/**
	 * Instantiates a new tcp no option.
	 */
	public TcpNopOption() {
		super(ID, TcpOptionConstants.TCP_OPTION_KIND_NOP, TcpOptionConstants.TCP_OPTION_LEN_NOP);
	}

	/**
	 * The NOP option length field (1 byte).
	 *
	 * @return the option length field in units of bytes
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.tcp.TcpOption#length()
	 */
	@Override
	public int length() {
		return 1;
	}
}