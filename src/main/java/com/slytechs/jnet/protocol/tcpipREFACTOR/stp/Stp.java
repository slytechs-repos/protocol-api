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
package com.slytechs.jnet.protocol.tcpipREFACTOR.stp;

import java.util.concurrent.locks.Lock;

import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.core.CoreId;
import com.slytechs.jnet.protocol.api.meta.Meta;

/**
 * The Spanning Tree Protocol (STP).
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
@Meta
public final class Stp extends Header {

	/** Core protocol pack assigned header ID. */
	public static int ID = CoreId.CORE_ID_STP;

	/**
	 * Instantiates a new STP header.
	 */
	public Stp() {
		super(ID);
	}

	/**
	 * Instantiates a new STP header.
	 *
	 * @param lock the lock
	 */
	public Stp(Lock lock) {
		super(ID, lock);
	}

	/**
	 * Protocol identifier.
	 *
	 * @return the int
	 */
	@Meta
	public int protocol() {
		return StpLayout.PROTOCOL.getUnsignedShort(buffer());
	}

	/**
	 * Version.
	 *
	 * @return the int
	 */
	@Meta
	public int version() {
		return StpLayout.VERSION.getUnsignedByte(buffer());
	}

	/**
	 * Message type.
	 *
	 * @return the int
	 */
	@Meta
	public int type() {
		return StpLayout.TYPE.getUnsignedByte(buffer());
	}

	/**
	 * Flags.
	 *
	 * @return the int
	 */
	@Meta
	public int flags() {
		return StpLayout.FLAGS.getUnsignedByte(buffer());
	}

	/**
	 * Root id.
	 *
	 * @return the long
	 */
	@Meta
	public long rootId() {
		return StpLayout.ROOT_ID.getLong(buffer());
	}

	/**
	 * Root bridge priority.
	 *
	 * @return the int
	 */
	@Meta
	public int rootBridgePriority() {
		return StpLayout.ROOT_BRIDGE_PRIORITY.getUnsignedByte(buffer()) << 8;
	}

	/**
	 * Root bridge id ext.
	 *
	 * @return the int
	 */
	@Meta
	public int rootBridgeIdExt() {
		return StpLayout.ROOT_BRIDGE_ID_EXT.getInt(buffer());
	}

	/**
	 * Root bridge id.
	 *
	 * @return the byte[]
	 */
	@Meta
	public byte[] rootBridgeId() {
		return StpLayout.ROOT_BRIDGE_ID.getByteArray(buffer());
	}

	/**
	 * Root path cost.
	 *
	 * @return the int
	 */
	@Meta
	public int rootCost() {
		return StpLayout.ROOT_PATH_COST.getInt(buffer());
	}

	/**
	 * Bridge id.
	 *
	 * @return the long
	 */
	@Meta
	public long bridgeId() {
		return StpLayout.BRIDGE_ID.getLong(buffer());
	}

	/**
	 * System bridge priority.
	 *
	 * @return the int
	 */
	@Meta
	public int systemBridgePriority() {
		return StpLayout.SYSTEM_BRIDGE_PRIORITY.getUnsignedByte(buffer()) << 8;
	}

	/**
	 * System bridge id ext.
	 *
	 * @return the int
	 */
	@Meta
	public int systemBridgeIdExt() {
		return StpLayout.SYSTEM_BRIDGE_ID_EXT.getInt(buffer());
	}

	/**
	 * System bridge id.
	 *
	 * @return the byte[]
	 */
	@Meta
	public byte[] systemBridgeId() {
		return StpLayout.SYSTEM_BRIDGE_ID.getByteArray(buffer());
	}

	/**
	 * Port id.
	 *
	 * @return the int
	 */
	@Meta
	public int portId() {
		return StpLayout.PORT_ID.getUnsignedShort(buffer());
	}

	/**
	 * Message age.
	 *
	 * @return the int
	 */
	@Meta
	public int messageAge() {
		return StpLayout.MSG_AGE.getUnsignedShort(buffer());
	}

	/**
	 * Maximum time.
	 *
	 * @return the int
	 */
	@Meta
	public int maxTime() {
		return StpLayout.MAX_AGE.getUnsignedShort(buffer());
	}

	/**
	 * Hello time.
	 *
	 * @return the int
	 */
	@Meta
	public int helloTime() {
		return StpLayout.HELLO.getUnsignedShort(buffer());
	}

	/**
	 * Forward delay.
	 *
	 * @return the int
	 */
	@Meta
	public int forwardDelay() {
		return StpLayout.FORWARD.getUnsignedShort(buffer());
	}
}
