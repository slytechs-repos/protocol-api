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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet;

import java.util.concurrent.locks.Lock;

import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.tcpipREFACTOR.constants.CoreId;

/**
 * The sub-network access protocol (SNAP).
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
@Meta
public final class Snap extends Header {

	/** Core protocol pack assigned header ID. */
	public static final int ID = CoreId.CORE_ID_SNAP;

	/**
	 * Instantiates a new snap.
	 */
	public Snap() {
		super(ID);
	}

	/**
	 * Instantiates a new snap.
	 * <p>
	 * The first 3 bytes of the SNAP header is the vendor code, generally the same
	 * as the first three bytes of the source address although it is sometimes set
	 * to zero.
	 * </p>
	 * 
	 * @param lock the lock
	 */
	public Snap(Lock lock) {
		super(ID, lock);
	}

	/**
	 * The Vendor Code.
	 * <p>
	 * Following the Vendor Code is a 2 byte field that typically contains an
	 * Ethertype for the frame. This is where the backwards compatibility with
	 * Version II Ethernet is implemented.
	 * </p>
	 * 
	 * @return 3-byte vendor code
	 */
	@Meta(offset = 0, length = 3)
	public byte[] oui() {
		return SnapLayout.OUI.getByteArray(buffer());
	}

	/**
	 * Ethertype for the frame.
	 *
	 * @return the 2-byte local code
	 */
	@Meta(offset = 3, length = 2)
	public int pid() {
		return SnapLayout.PID.getUnsignedShort(buffer());
	}

}
