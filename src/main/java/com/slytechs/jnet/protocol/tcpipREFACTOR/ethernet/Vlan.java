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
import com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.impl.VlanLayout;

/**
 * Virtual LAN tag (802.1Q)
 * 
 * <p>
 * A switch uses VLAN tags to differentiate packets belonging to different
 * VLANs. To achieve this, IEEE 802.1Q inserts a 4-byte VLAN tag between the
 * Source/Destination MAC address and Length/Type fields of an Ethernet frame,
 * which specifies the VLAN to which the frame belongs.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author IEEE 802.1Q FrameHeader Format
 */
@Meta
public final class Vlan extends Header {

	/** Core protocol pack assigned header ID. */
	public static final int ID = CoreId.CORE_ID_VLAN;

	/**
	 * Instantiates a new vlan.
	 */
	public Vlan() {
		super(ID);
	}

	/**
	 * Instantiates a new vlan.
	 *
	 * @param lock the lock
	 */
	public Vlan(Lock lock) {
		super(ID, lock);
	}

	/**
	 * Priority (PRI), indicating the 802.1p priority of a frame.
	 * 
	 * <p>
	 * The value is in the range from 0 to 7. A larger value indicates a higher
	 * priority. If congestion occurs, the switch sends packets with the highest
	 * priority first.
	 * </p>
	 *
	 * @return 3-bit pri value
	 */
	@Meta(offset = 0, length = 1)
	public int priority() {
		return VlanLayout.PRI.getUnsignedShort(buffer());
	}

	/**
	 * Canonical Format Indicator (CFI), indicating whether a MAC address is
	 * encapsulated in canonical format over different transmission media. CFI is
	 * used to ensure compatibility between Ethernet and token ring networks.
	 * 
	 * <p>
	 * The value 0 indicates that the MAC address is encapsulated in canonical
	 * format, and the value 1 indicates that the MAC address is encapsulated in
	 * non-canonical format. The CFI field has a fixed value of 0 on Ethernet
	 * networks.
	 * </p>
	 *
	 * @return the 1-bit cfi value
	 */
	@Meta(offset = 0, length = 1)
	public int formatIdentifier() {
		return VlanLayout.CFI.getUnsignedShort(buffer());
	}

	/**
	 * VLAN ID (VID), indicating the VLAN to which a frame belongs.
	 * 
	 * <p>
	 * The VLAN ID is in the range from 0 to 4095. The values 0 and 4095 are
	 * reserved, and therefore available VLAN IDs are in the range from 1 to 4094.
	 * </p>
	 *
	 * @return the 12-bit VID value.
	 */
	@Meta(offset = 0, length = 2)
	public int vlanId() {
		return VlanLayout.VID.getUnsignedShort(buffer());
	}

	@Meta(offset = 2, length = 2)
	public int type() {
		return VlanLayout.TYPE.getUnsignedShort(buffer());
	}
}
