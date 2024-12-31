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
package com.slytechs.jnet.protocol.tcpip.ethernet;

/**
 * Interface defining constants for Virtual LAN (VLAN) tagging in Ethernet
 * frames. VLAN tagging allows network administrators to segregate and manage
 * network traffic by embedding VLAN IDs into Ethernet frame headers.
 * 
 * <p>
 * These constants include field offsets, lengths, and header sizes used in
 * VLAN-tagged Ethernet frames.
 * </p>
 * 
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface VlanConstants {

	/**
	 * Length of the VLAN header in bytes.
	 */
	int VLAN_HEADER_LEN = 4;

	/**
	 * Offset of the Ethernet Type field in the VLAN header.
	 */
	int VLAN_FIELD_TYPE = 2;

	/**
	 * Length of the Tag Control Information (TCI) field in bytes.
	 */
	int VLAN_FIELD_LEN_TCI = 2;

}
