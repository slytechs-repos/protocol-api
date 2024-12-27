package com.slytechs.jnet.protocol.tcpipREFACTOR.ip;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Statistics tracking for IP fragment reassembly operations.
 * 
 * <p>
 * Tracks various metrics related to fragment processing, reassembly operations,
 * and resource usage. All counters are thread-safe using atomic operations.
 * </p>
 */
public class IpReassemblyStats {

	// Basic counters
	private final AtomicLong totalFragments = new AtomicLong();
	private final AtomicLong completedReassemblies = new AtomicLong();
	private final AtomicLong failedReassemblies = new AtomicLong();
	private final AtomicInteger currentReassemblies = new AtomicInteger();

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

	/**
	 * Records the start of fragment processing.
	 */
	public void onFragmentReceived(int fragmentSize) {
		totalFragments.incrementAndGet();
		totalFragmentSize.addAndGet(fragmentSize);
		maxFragmentSize.updateAndGet(current -> Math.max(current, fragmentSize));
	}

	/**
	 * Records a completed reassembly operation.
	 */
	public void onReassemblyComplete(long reassemblyTime, int datagramSize) {
		completedReassemblies.incrementAndGet();
		currentReassemblies.decrementAndGet();
		totalReassemblyTime.addAndGet(reassemblyTime);
		maxReassemblyTime.updateAndGet(current -> Math.max(current, reassemblyTime));
		totalBytesReassembled.addAndGet(datagramSize);
	}

	/**
	 * Records a failed reassembly operation.
	 */
	public void onReassemblyFailed() {
		failedReassemblies.incrementAndGet();
		currentReassemblies.decrementAndGet();
	}

	/**
	 * Records the start of a new reassembly operation.
	 */
	public void onReassemblyStarted() {
		currentReassemblies.incrementAndGet();
	}

	/**
	 * Records a duplicate fragment detection.
	 */
	public void onDuplicateFragment() {
		duplicateFragments.incrementAndGet();
	}

	/**
	 * Records an overlapping fragment detection.
	 */
	public void onOverlappingFragment() {
		overlappingFragments.incrementAndGet();
	}

	/**
	 * Records an out-of-order fragment.
	 */
	public void onOutOfOrderFragment() {
		outOfOrderFragments.incrementAndGet();
	}

	/**
	 * Records a gap detection in fragment sequence.
	 */
	public void onGapDetected() {
		gapsDetected.incrementAndGet();
	}

	/**
	 * Records an invalid fragment detection.
	 */
	public void onInvalidFragment() {
		invalidFragments.incrementAndGet();
	}

	/**
	 * Records a table full condition.
	 */
	public void onTableFull() {
		tableFull.incrementAndGet();
	}

	/**
	 * Records a memory limit reached condition.
	 */
	public void onMemoryLimitReached() {
		memoryLimitReached.incrementAndGet();
	}

	/**
	 * Records a fragment limit reached condition.
	 */
	public void onFragmentLimitReached() {
		fragmentLimitReached.incrementAndGet();
	}

	/**
	 * Records various timeout conditions.
	 */
	public void onTimeout(TimeoutType type) {
		switch (type) {
		case INCOMPLETE:
			timeoutIncomplete.incrementAndGet();
			break;
		case NO_LAST_FRAGMENT:
			timeoutNoLastFragment.incrementAndGet();
			break;
		case MISSING_FRAGMENTS:
			timeoutMissingFragments.incrementAndGet();
			break;
		}
	}

	/**
	 * Updates memory usage statistics.
	 */
	public void updateMemoryUsage(long currentUsage) {
		currentMemoryUsage.set(currentUsage);
		peakMemoryUsage.updateAndGet(peak -> Math.max(peak, currentUsage));
	}

	/**
	 * Resets all statistics to zero.
	 */
	public void reset() {
		totalFragments.set(0);
		completedReassemblies.set(0);
		failedReassemblies.set(0);
		currentReassemblies.set(0);
		duplicateFragments.set(0);
		overlappingFragments.set(0);
		outOfOrderFragments.set(0);
		gapsDetected.set(0);
		invalidFragments.set(0);
		tableFull.set(0);
		memoryLimitReached.set(0);
		fragmentLimitReached.set(0);
		timeoutIncomplete.set(0);
		timeoutNoLastFragment.set(0);
		timeoutMissingFragments.set(0);
		totalReassemblyTime.set(0);
		maxReassemblyTime.set(0);
		totalFragmentSize.set(0);
		maxFragmentSize.set(0);
		currentMemoryUsage.set(0);
		peakMemoryUsage.set(0);
		totalBytesReassembled.set(0);
	}

	// Getters for all metrics
	public long getTotalFragments() {
		return totalFragments.get();
	}

	public long getCompletedReassemblies() {
		return completedReassemblies.get();
	}

	public long getFailedReassemblies() {
		return failedReassemblies.get();
	}

	public int getCurrentReassemblies() {
		return currentReassemblies.get();
	}

	public long getDuplicateFragments() {
		return duplicateFragments.get();
	}

	public long getOverlappingFragments() {
		return overlappingFragments.get();
	}

	public long getOutOfOrderFragments() {
		return outOfOrderFragments.get();
	}

	public long getGapsDetected() {
		return gapsDetected.get();
	}

	public long getInvalidFragments() {
		return invalidFragments.get();
	}

	public long getTableFull() {
		return tableFull.get();
	}

	public long getMemoryLimitReached() {
		return memoryLimitReached.get();
	}

	public long getFragmentLimitReached() {
		return fragmentLimitReached.get();
	}

	public long getTimeoutIncomplete() {
		return timeoutIncomplete.get();
	}

	public long getTimeoutNoLastFragment() {
		return timeoutNoLastFragment.get();
	}

	public long getTimeoutMissingFragments() {
		return timeoutMissingFragments.get();
	}

	public long getTotalReassemblyTime() {
		return totalReassemblyTime.get();
	}

	public long getMaxReassemblyTime() {
		return maxReassemblyTime.get();
	}

	public long getTotalFragmentSize() {
		return totalFragmentSize.get();
	}

	public long getMaxFragmentSize() {
		return maxFragmentSize.get();
	}

	public long getCurrentMemoryUsage() {
		return currentMemoryUsage.get();
	}

	public long getPeakMemoryUsage() {
		return peakMemoryUsage.get();
	}

	public long getTotalBytesReassembled() {
		return totalBytesReassembled.get();
	}

	/**
	 * Calculates the average reassembly time in nanoseconds.
	 */
	public double getAverageReassemblyTime() {
		long completed = completedReassemblies.get();
		return completed > 0 ? (double) totalReassemblyTime.get() / completed : 0.0;
	}

	/**
	 * Calculates the average fragment size in bytes.
	 */
	public double getAverageFragmentSize() {
		long total = totalFragments.get();
		return total > 0 ? (double) totalFragmentSize.get() / total : 0.0;
	}

	/**
	 * Timeout condition types for statistics tracking.
	 */
	public enum TimeoutType {
		INCOMPLETE,
		NO_LAST_FRAGMENT,
		MISSING_FRAGMENTS
	}

	@Override
	public String toString() {
		return String.format("""
				IP Reassembly Statistics:
				Total Fragments: %d
				Completed Reassemblies: %d
				Failed Reassemblies: %d
				Current Reassemblies: %d
				Duplicate Fragments: %d
				Overlapping Fragments: %d
				Out of Order Fragments: %d
				Gaps Detected: %d
				Invalid Fragments: %d
				Table Full Count: %d
				Memory Limit Reached: %d
				Fragment Limit Reached: %d
				Timeout (Incomplete): %d
				Timeout (No Last Fragment): %d
				Timeout (Missing Fragments): %d
				Average Reassembly Time: %.2f ns
				Max Reassembly Time: %d ns
				Average Fragment Size: %.2f bytes
				Max Fragment Size: %d bytes
				Current Memory Usage: %d bytes
				Peak Memory Usage: %d bytes
				Total Bytes Reassembled: %d
				""",
				getTotalFragments(),
				getCompletedReassemblies(),
				getFailedReassemblies(),
				getCurrentReassemblies(),
				getDuplicateFragments(),
				getOverlappingFragments(),
				getOutOfOrderFragments(),
				getGapsDetected(),
				getInvalidFragments(),
				getTableFull(),
				getMemoryLimitReached(),
				getFragmentLimitReached(),
				getTimeoutIncomplete(),
				getTimeoutNoLastFragment(),
				getTimeoutMissingFragments(),
				getAverageReassemblyTime(),
				getMaxReassemblyTime(),
				getAverageFragmentSize(),
				getMaxFragmentSize(),
				getCurrentMemoryUsage(),
				getPeakMemoryUsage(),
				getTotalBytesReassembled());
	}
}