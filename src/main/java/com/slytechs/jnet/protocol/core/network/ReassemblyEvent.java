package com.slytechs.jnet.protocol.core.network;

import java.util.concurrent.TimeUnit;

import com.slytechs.jnet.protocol.core.network.ReassemblyContext.FragmentAgeStats;

/**
 * Enhanced event class for IP reassembly operations with detailed timing and
 * state information.
 */
class ReassemblyEvent {
	private final ReassemblyContext.FragmentEventType type;
	private final FragmentKey key;
	private final String details;
	private final long timestamp;

	// Reassembly state information
	private final int totalFragments;
	private final int receivedBytes;
	private final int expectedSize;
	private final double completionPercentage;
	private final boolean hasLastFragment;

	// Timing information
	private final long reassemblyAge; // Age of the reassembly context
	private final long timeSinceLastFragment; // Time since last fragment received
	private final FragmentAgeStats ageStats; // Fragment age statistics

	// Fragment-specific information
	private final int fragmentOffset;
	private final int fragmentSize;
	private final int largestGap;
	private final int overlappedFragments;

	// Memory information
	private final long currentMemoryUsage;
	private final long peakMemoryUsage;

	/**
	 * Creates a comprehensive reassembly event with detailed state information.
	 */
	public ReassemblyEvent(ReassemblyContext.FragmentEventType type,
			FragmentKey key,
			String details,
			ReassemblyContext context,
			int currentFragmentOffset,
			int currentFragmentSize,
			long currentMemory,
			long peakMemory) {
		this.type = type;
		this.key = key;
		this.details = details;
		this.timestamp = System.nanoTime();

		// Extract state from context if available
		if (context != null) {
			this.totalFragments = context.getFragmentCount();
			this.receivedBytes = context.getReceivedBytes();
			this.expectedSize = context.getExpectedSize();
			this.completionPercentage = context.getCompletionPercentage();
			this.hasLastFragment = context.hasLastFragment();
			this.reassemblyAge = context.getReassemblyTime();
			this.timeSinceLastFragment = context.getTimeSinceLastFragment();
			this.ageStats = context.getFragmentAgeStats();
			this.largestGap = context.getLargestGapSize();
			this.overlappedFragments = context.getOverlappedFragmentCount();
		} else {
			this.totalFragments = 0;
			this.receivedBytes = 0;
			this.expectedSize = 0;
			this.completionPercentage = 0.0;
			this.hasLastFragment = false;
			this.reassemblyAge = 0;
			this.timeSinceLastFragment = 0;
			this.ageStats = new FragmentAgeStats(0, 0, 0);
			this.largestGap = 0;
			this.overlappedFragments = 0;
		}

		this.fragmentOffset = currentFragmentOffset;
		this.fragmentSize = currentFragmentSize;
		this.currentMemoryUsage = currentMemory;
		this.peakMemoryUsage = peakMemory;
	}

	// Basic getters
	public ReassemblyContext.FragmentEventType getType() {
		return type;
	}

	public FragmentKey getKey() {
		return key;
	}

	public String getDetails() {
		return details;
	}

	public long getTimestamp() {
		return timestamp;
	}

	// State getters
	public int getTotalFragments() {
		return totalFragments;
	}

	public int getReceivedBytes() {
		return receivedBytes;
	}

	public int getExpectedSize() {
		return expectedSize;
	}

	public double getCompletionPercentage() {
		return completionPercentage;
	}

	public boolean hasLastFragment() {
		return hasLastFragment;
	}

	// Timing getters
	public long getReassemblyAge(TimeUnit unit) {
		return unit.convert(reassemblyAge, TimeUnit.NANOSECONDS);
	}

	public long getTimeSinceLastFragment(TimeUnit unit) {
		return unit.convert(timeSinceLastFragment, TimeUnit.NANOSECONDS);
	}

	public FragmentAgeStats getFragmentAgeStats() {
		return ageStats;
	}

	// Fragment getters
	public int getFragmentOffset() {
		return fragmentOffset;
	}

	public int getFragmentSize() {
		return fragmentSize;
	}

	public int getLargestGap() {
		return largestGap;
	}

	public int getOverlappedFragments() {
		return overlappedFragments;
	}

	// Memory getters
	public long getCurrentMemoryUsage() {
		return currentMemoryUsage;
	}

	public long getPeakMemoryUsage() {
		return peakMemoryUsage;
	}

	@Override
	public String toString() {
		return String.format("""
				Reassembly Event [%s]:
				Key: %s
				Details: %s
				Time: %s

				State:
				- Total Fragments: %d
				- Received Bytes: %d / %d (%,.1f%%)
				- Has Last Fragment: %s
				- Overlapped Fragments: %d
				- Largest Gap: %d bytes

				Timing:
				- Reassembly Age: %d ms
				- Time Since Last Fragment: %d ms
				- Fragment Ages: %s

				Current Fragment:
				- Offset: %d
				- Size: %d bytes

				Memory Usage:
				- Current: %,d bytes
				- Peak: %,d bytes
				""",
				type,
				key,
				details,
				new java.util.Date(TimeUnit.MILLISECONDS.convert(timestamp, TimeUnit.NANOSECONDS)),

				totalFragments,
				receivedBytes,
				expectedSize,
				completionPercentage,
				hasLastFragment,
				overlappedFragments,
				largestGap,

				getReassemblyAge(TimeUnit.MILLISECONDS),
				getTimeSinceLastFragment(TimeUnit.MILLISECONDS),
				ageStats,

				fragmentOffset,
				fragmentSize,

				currentMemoryUsage,
				peakMemoryUsage);
	}

	/**
	 * Creates a formatted string with timing information.
	 */
	public String getTimingReport() {
		return String.format("""
				Timing Report:
				- Total Age: %d ms
				- Last Fragment: %d ms ago
				- Min Fragment Age: %d ms
				- Max Fragment Age: %d ms
				- Avg Fragment Age: %d ms
				""",
				getReassemblyAge(TimeUnit.MILLISECONDS),
				getTimeSinceLastFragment(TimeUnit.MILLISECONDS),
				ageStats.getAge(ageStats.minAgeNanos, TimeUnit.MILLISECONDS),
				ageStats.getAge(ageStats.maxAgeNanos, TimeUnit.MILLISECONDS),
				ageStats.getAge(ageStats.avgAgeNanos, TimeUnit.MILLISECONDS));
	}

	/**
	 * Creates a formatted string with fragment statistics.
	 */
	public String getFragmentReport() {
		return String.format("""
				Fragment Report:
				- Count: %d
				- Bytes Received: %d
				- Expected Size: %d
				- Completion: %.1f%%
				- Overlapped: %d
				- Largest Gap: %d bytes
				- Current Offset: %d
				- Current Size: %d bytes
				""",
				totalFragments,
				receivedBytes,
				expectedSize,
				completionPercentage,
				overlappedFragments,
				largestGap,
				fragmentOffset,
				fragmentSize);
	}
}