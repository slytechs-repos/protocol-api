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
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.impl.LlcLayout;

/**
 * Logical Link Control header (LLC).
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
@Meta
public final class Llc extends Header {

	/** Core protocol pack assigned header ID. */
	public static final int ID = CoreId.CORE_ID_LLC;

	/**
	 * Instantiates a new llc.
	 */
	public Llc() {
		super(ID);
	}

	/**
	 * Instantiates a new llc.
	 *
	 * @param lock the lock
	 */
	public Llc(Lock lock) {
		super(ID, lock);
	}

	/**
	 * The Control Byte.
	 * 
	 * <p>
	 * Following the SAPs is a one byte control field that specifies the type of LLC
	 * frame that this is.
	 * </p>
	 *
	 * @return 1-byte control field
	 */
	@Meta(offset = 2, length = 1)
	public int control() {
		return LlcLayout.CONTROL.getUnsignedByte(buffer());
	}

	/**
	 * The Destination Service Access Point (DSAP)
	 * <p>
	 * he Destination Service Access Point or DSAP, is a 1 byte field that simply
	 * acts as a pointer to a memory buffer in the receiving station. It tells the
	 * receiving network interface card in which buffer to put this information.
	 * This functionality is crucial in situations where users are running multiple
	 * protocol stacks, etc...
	 * </p>
	 *
	 * @return 1-byte dsap field
	 */
	@Meta(offset = 0, length = 1)
	public int dsap() {
		return LlcLayout.DSAP.getUnsignedByte(buffer());
	}

	/**
	 * The Source Service Access Point (SSAP).
	 * 
	 * <p>
	 * The Source Service Access Point or SSAP is analogous to the DSAP and
	 * specifies the Source of the sending process
	 * </p>
	 *
	 * @return 1-byte ssap field
	 */
	@Meta(offset = 1, length = 1)
	public int ssap() {
		return LlcLayout.SSAP.getUnsignedByte(buffer());
	}
}
