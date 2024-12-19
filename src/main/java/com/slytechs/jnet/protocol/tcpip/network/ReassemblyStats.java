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
package com.slytechs.jnet.protocol.tcpip.network;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Statistics tracking
 * 
 * @author Mark Bednarczyk
 */
public class ReassemblyStats {
	// Basic counters
	private final AtomicLong totalFragments = new AtomicLong();
	private final AtomicLong completedReassemblies = new AtomicLong();
	private final AtomicLong failedReassemblies = new AtomicLong();
	private final AtomicLong currentReassemblies = new AtomicLong();

	// Fragment-specific counters
	private final AtomicLong duplicateFragments = new AtomicLong();
	private final AtomicLong overlappingFragments = new AtomicLong();
	private final AtomicLong outOfOrderFragments = new AtomicLong();
	private final AtomicLong gapsDetected = new AtomicLong();
	private final AtomicLong invalidFragments = new AtomicLong();

	// Resource counters
	private final AtomicLong tableFull = new AtomicLong();
	private final AtomicLong memoryLimitReached = new AtomicLong();
	private final AtomicLong fragmentLimitReached = new AtomicLong();

	// Timeout counters
	private final AtomicLong timeoutIncomplete = new AtomicLong();
	private final AtomicLong timeoutNoLastFragment = new AtomicLong();
	private final AtomicLong timeoutMissingFragments = new AtomicLong();

	// Performance metrics
	private final AtomicLong totalReassemblyTime = new AtomicLong();
	private final AtomicLong maxReassemblyTime = new AtomicLong();
	private final AtomicLong totalFragmentSize = new AtomicLong();
	private final AtomicLong maxFragmentSize = new AtomicLong();

	// Memory metrics
	private final AtomicLong currentMemoryUsage = new AtomicLong();
	private final AtomicLong peakMemoryUsage = new AtomicLong();
	private final AtomicLong totalBytesReassembled = new AtomicLong();

	public void recordFragmentReceived(int size) {
		totalFragments.incrementAndGet();
		totalFragmentSize.addAndGet(size);
		maxFragmentSize.updateAndGet(current -> Math.max(current, size));
	}

	public void recordReassemblyComplete(long timeNanos, int totalSize) {
		completedReassemblies.incrementAndGet();
		currentReassemblies.decrementAndGet();
		totalReassemblyTime.addAndGet(timeNanos);
		maxReassemblyTime.updateAndGet(current -> Math.max(current, timeNanos));
		totalBytesReassembled.addAndGet(totalSize);
	}

	public void recordMemoryUsage(long bytes) {
		currentMemoryUsage.set(bytes);
		peakMemoryUsage.updateAndGet(current -> Math.max(current, bytes));
	}

	// Add increment methods for all counters
	public void incrementDuplicateFragments() {
		duplicateFragments.incrementAndGet();
	}

	public void incrementOverlappingFragments() {
		overlappingFragments.incrementAndGet();
	}

	public void incrementOutOfOrderFragments() {
		outOfOrderFragments.incrementAndGet();
	}

	public void incrementGapsDetected() {
		gapsDetected.incrementAndGet();
	}

	public void incrementInvalidFragments() {
		invalidFragments.incrementAndGet();
	}

	public void incrementTableFull() {
		tableFull.incrementAndGet();
	}

	public void incrementMemoryLimitReached() {
		memoryLimitReached.incrementAndGet();
	}

	public void incrementFragmentLimitReached() {
		fragmentLimitReached.incrementAndGet();
	}

	public void incrementTimeoutIncomplete() {
		timeoutIncomplete.incrementAndGet();
	}

	public void incrementTimeoutNoLastFragment() {
		timeoutNoLastFragment.incrementAndGet();
	}

	public void incrementTimeoutMissingFragments() {
		timeoutMissingFragments.incrementAndGet();
	}

	// Add getters for all metrics
	public long getAverageReassemblyTime() {
		long completed = completedReassemblies.get();
		return completed > 0 ? totalReassemblyTime.get() / completed : 0;
	}

	public long getAverageFragmentSize() {
		long fragments = totalFragments.get();
		return fragments > 0 ? totalFragmentSize.get() / fragments : 0;
	}

	// Add getters for all counters...

	@Override
	public String toString() {
		return String.format("""
				Reassembly Statistics:
				Total Fragments: %d
				Completed Reassemblies: %d
				Failed Reassemblies: %d
				Current Reassemblies: %d

				Fragment Issues:
				Duplicate: %d
				Overlapping: %d
				Out of Order: %d
				Gaps Detected: %d
				Invalid: %d

				Resource Usage:
				Table Full Events: %d
				Memory Limit Events: %d
				Fragment Limit Events: %d
				Current Memory Usage: %d bytes
				Peak Memory Usage: %d bytes

				Timeouts:
				Incomplete: %d
				No Last Fragment: %d
				Missing Fragments: %d

				Performance:
				Average Reassembly Time: %d ns
				Max Reassembly Time: %d ns
				Average Fragment Size: %d bytes
				Total Bytes Reassembled: %d
				""",
				totalFragments.get(),
				completedReassemblies.get(),
				failedReassemblies.get(),
				currentReassemblies.get(),

				duplicateFragments.get(),
				overlappingFragments.get(),
				outOfOrderFragments.get(),
				gapsDetected.get(),
				invalidFragments.get(),

				tableFull.get(),
				memoryLimitReached.get(),
				fragmentLimitReached.get(),
				currentMemoryUsage.get(),
				peakMemoryUsage.get(),

				timeoutIncomplete.get(),
				timeoutNoLastFragment.get(),
				timeoutMissingFragments.get(),

				getAverageReassemblyTime(),
				maxReassemblyTime.get(),
				getAverageFragmentSize(),
				totalBytesReassembled.get());
	}
}
