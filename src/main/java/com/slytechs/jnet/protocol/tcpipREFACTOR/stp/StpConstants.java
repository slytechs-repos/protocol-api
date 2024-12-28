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
package com.slytechs.jnet.protocol.tcpipREFACTOR.stp;

/**
 * Interface defining constants for the Spanning Tree Protocol (STP). STP is a
 * network protocol that ensures a loop-free topology for Ethernet networks. It
 * prevents broadcast storms caused by network loops and is a fundamental part
 * of network design.
 * 
 * <p>
 * This interface includes constants related to the structure of STP headers.
 * </p>
 * 
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public interface StpConstants {

	/**
	 * Length of the STP header in bytes.
	 */
	int STP_HEADER_LEN = 35;

}
