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
import com.slytechs.jnet.platform.api.util.IsAbbr;
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
public enum DiffServEcn implements IntSupplier, IsDescription, IsAbbr {
	NOT_ECT(0b00, "Not-ECT", "Not ECN-Capable Transport"),
	ECT1(0b01, "ECT(1)", "ECN Capable Transport(1)"),
	ECT0(0b10, "ECT(0)", "ECN Capable Transport(0)"),
	CE(0b11, "CE", "Congestion Experienced"),

	;

	public static final int ECN_MASK = 0x3;

	public static String resolveName(Object dscp) {
		try {
			int ecn = ((Number) dscp).intValue() & ECN_MASK;
			return Enums.valueOf(ecn, DiffServEcn.class).name();
		} catch (Throwable e) {
			return String.valueOf(dscp);
		}
	}

	public static String resolveDescription(Object dscp) {
		try {
			int ecn = ((Number) dscp).intValue() & ECN_MASK;
			return Enums.valueOf(ecn, DiffServEcn.class).description();
		} catch (Throwable e) {
			return String.valueOf(dscp);
		}
	}

	public static String resolveAbbr(Object dscp) {
		int ecn = ((Number) dscp).intValue() & ECN_MASK;
		try {
			return Enums.valueOf(ecn, DiffServEcn.class).abbr();
		} catch (Throwable e) {
			return String.valueOf(dscp);
		}

	}

	private final int dscp;
	private final String description;
	private final String abbr;

	/**
	 * @param dscp
	 * @param description
	 */
	DiffServEcn(int dscp, String abbr, String description) {
		this.dscp = dscp;
		this.abbr = abbr;
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

	/**
	 * Abbr.
	 *
	 * @return the string
	 */
	@Override
	public String abbr() {
		return abbr;
	}
}
