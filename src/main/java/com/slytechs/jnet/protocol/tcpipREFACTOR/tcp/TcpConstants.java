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
package com.slytechs.jnet.protocol.tcpipREFACTOR.tcp;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public final class TcpConstants {

	/* Header constants */
	public static final int TCP_HEADER_LEN = 20;
	public static final int TCP_FIELD_IHL = 12;
	public static final int TCP_FIELD_SRC = 0;
	public static final int TCP_FIELD_DST = 2;
	public static final int TCP_FIELD_SEQ = 4;
	public static final int TCP_FIELD_ACK = 8;
	public static final int TCP_FIELD_OFFSET = 12;
	public static final int TCP_FIELD_FLAGS = 13;
	public static final int TCP_FIELD_WINDOW = 14;
	public static final int TCP_FIELD_CHECKSUM = 16;
	public static final int TCP_FIELD_URGENT = 18;

	/* TCP Flags */
	public static final int TCP_FLAG_FIN = 0x01;
	public static final int TCP_FLAG_SYN = 0x02;
	public static final int TCP_FLAG_RST = 0x04;
	public static final int TCP_FLAG_PSH = 0x08;
	public static final int TCP_FLAG_ACK = 0x10;
	public static final int TCP_FLAG_URG = 0x20;
	public static final int TCP_FLAG_ECE = 0x40;
	public static final int TCP_FLAG_CWR = 0x80;
	public static final int TCP_FLAG_ECN = 0x40;
	public static final int TCP_FLAG_CONG = 0x80;

	private TcpConstants() {
	}
}