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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ip;

import com.slytechs.jnet.protocol.tcpipREFACTOR.ip.reassembly.IpfFragment;

/**
 * @author Mark Bednarczyk
 *
 */
public class IpReassemblyEvent {

	private final IpFragmentEventType type;
	private final IpfFragment fragment;
	private final long timestamp;
	private final int fragmentSize;
	private final int totalSize;
	private final int fragmentCount;
	private final long reassemblyTime;

	/**
	 * Constructs a new reassembly event.
	 *
	 * @param type     the event type
	 * @param fragment the fragment descriptor, may be null for some event types
	 */
	public IpReassemblyEvent(IpFragmentEventType type, IpfFragment fragment) {
		this(type, fragment, System.nanoTime(), 0, 0, 0, 0);
	}

	/**
	 * Constructs a new reassembly event with detailed information.
	 *
	 * @param type           the event type
	 * @param fragment       the fragment descriptor
	 * @param timestamp      event timestamp
	 * @param fragmentSize   size of current fragment
	 * @param totalSize      total reassembled size
	 * @param fragmentCount  current fragment count
	 * @param reassemblyTime time spent in reassembly
	 */
	public IpReassemblyEvent(
			IpFragmentEventType type,
			IpfFragment fragment,
			long timestamp,
			int fragmentSize,
			int totalSize,
			int fragmentCount,
			long reassemblyTime) {
		this.type = type;
		this.fragment = fragment;
		this.timestamp = timestamp;
		this.fragmentSize = fragmentSize;
		this.totalSize = totalSize;
		this.fragmentCount = fragmentCount;
		this.reassemblyTime = reassemblyTime;
	}

	/**
	 * Gets the event type.
	 *
	 * @return the event type
	 */
	public IpFragmentEventType getType() {
		return type;
	}

	/**
	 * Gets the fragment descriptor.
	 *
	 * @return the fragment descriptor, may be null
	 */
	public IpfFragment getFragment() {
		return fragment;
	}

	/**
	 * Gets the event timestamp.
	 *
	 * @return the timestamp in nanoseconds
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Gets the size of the current fragment.
	 *
	 * @return the fragment size in bytes
	 */
	public int getFragmentSize() {
		return fragmentSize;
	}

	/**
	 * Gets the total reassembled size.
	 *
	 * @return the total size in bytes
	 */
	public int getTotalSize() {
		return totalSize;
	}

	/**
	 * Gets the current fragment count.
	 *
	 * @return the number of fragments
	 */
	public int getFragmentCount() {
		return fragmentCount;
	}

	/**
	 * Gets the time spent in reassembly.
	 *
	 * @return the reassembly time in nanoseconds
	 */
	public long getReassemblyTime() {
		return reassemblyTime;
	}

	@Override
	public String toString() {
		return String.format(
				"IpReassemblyEvent[type=%s, fragmentSize=%d, totalSize=%d, count=%d, time=%dns]",
				type, fragmentSize, totalSize, fragmentCount, reassemblyTime);
	}
}
