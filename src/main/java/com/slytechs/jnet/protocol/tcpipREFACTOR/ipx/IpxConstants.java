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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ipx;

/**
 * Interface defining constants for the Internetwork Packet Exchange (IPX)
 * protocol. IPX is a network layer protocol used primarily in Novell NetWare
 * networks.
 * 
 * <p>
 * These constants include header lengths and field values specific to IPX
 * protocol operations.
 * </p>
 * 
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface IpxConstants {

	/**
	 * Length of the IPX header in bytes.
	 */
	int IPX_HEADER_LEN = 30;

	/**
	 * Field value for the IPX checksum.
	 */
	int IPX_FIELD_VALUE_CHECKSUM = 0;

}
