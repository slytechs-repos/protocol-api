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
package com.slytechs.jnet.protocol.tcpip.network;

import com.slytechs.jnet.protocol.tcpip.constants.Ip4IdOptions;

/**
 * IPv4 timestamp option header.
 * <p>
 * The timestamp option is used to record the time that a packet has spent in
 * transit. The timestamps are recorded by each router that the packet passes
 * through. The timestamps can be used to measure the performance of the network
 * and to troubleshoot routing problems.
 * </p>
 * <p>
 * The timestamp option is supported by most modern operating systems and
 * routers. However, it is important to note that not all routers settingsSupport the
 * timestamp option. As a result, it is important to check the documentation for
 * the routers that will be used to ensure that the timestamp option is
 * supported.
 * </p>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public final class Ip4TimestampOption extends Ip4Option {

	/** The IPv4 timestamp option header ID constant. */
	public static final int ID = Ip4IdOptions.IPv4_OPTION_TYPE_TS;

	/**
	 * IPv4 timestamp option header.
	 */
	public Ip4TimestampOption() {
		super(ID);
	}

	/**
	 * The pointer field is a 1-byte field that specifies the byte position where
	 * the next timestamp should be recorded.
	 * 
	 * @return byte index to the next timestamp to be recorded
	 */
	public int pointer() {
		return Byte.toUnsignedInt(buffer().get(2));
	}

	/**
	 * The number of timestamps recorded within this timestamp option header.
	 *
	 * @return the number of timestamp records recorded
	 */
	public int timestampCount() {
		return (optionDataLength() - 3) / 4;
	}

	/**
	 * The timestamps field is a variable-length field that contains the timestamps.
	 * Each timestamp is a 4-byte field that contains the time in seconds since the
	 * epoch.
	 *
	 * @param index the timestamp index within this timestamp option header
	 * @return the unsigned 32-bit timestamp field value at specified index
	 */
	public long timestamp(int index) {
		int off = index * 4 + 2;

		return Integer.toUnsignedLong(buffer().getInt(off));
	}
}
