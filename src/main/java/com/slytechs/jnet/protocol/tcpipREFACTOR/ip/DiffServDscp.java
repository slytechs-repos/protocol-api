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

import java.util.function.IntSupplier;

import com.slytechs.jnet.platform.api.util.Enums;
import com.slytechs.jnet.platform.api.util.IsDescription;

/**
 * Network differentiated services (DiffServ).
 * 
 * <p>
 * Network differentiated services (DiffServ) is a computer networking
 * architecture that specifies a mechanism for classifying and managing network
 * traffic and providing quality of service (QoS) on modern IP networks.
 * DiffServ can, for example, be used to provide low-latency to critical network
 * traffic such as voice or streaming media while providing best-effort service
 * to non-critical services such as web traffic or file transfers.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public enum DiffServDscp implements IntSupplier, IsDescription {
	CS0(0, "Default"),
	CS1(8, "Low-priority data"),
	CS2(16, "Network O.A.M."),
	CS3(24, "Boradcast video"),
	CS4(32, "Real-time interactive"),
	CS5(40, "Signaling"),
	CS6(48, "Network control"),
	CS7(56, "Reserved"),

	;

	public static String resolveName(Object dscp) {
		try {
			int dscpInt = ((Number) dscp).intValue();
			return Enums.valueOf(dscpInt, DiffServDscp.class).name();
		} catch (Throwable e) {
			return String.valueOf(dscp);
		}
	}

	public static String resolveDescription(Object dscp) {
		try {
			int dscpInt = ((Number) dscp).intValue();
			return Enums.valueOf(dscpInt, DiffServDscp.class).description();
		} catch (Throwable e) {
			return String.valueOf(dscp);
		}
	}

	private final int dscp;
	private final String description;

	/**
	 * @param dscp
	 * @param description
	 */
	DiffServDscp(int dscp, String description) {
		this.dscp = dscp;
		this.description = description;
	}

	/**
	 * @see java.util.function.IntSupplier#getAsInt()
	 */
	@Override
	public int getAsInt() {
		return dscp;
	}

	/**
	 * Description.
	 *
	 * @return the string
	 */
	@Override
	public String description() {
		return description;
	}
}
