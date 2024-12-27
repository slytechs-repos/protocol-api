package com.slytechs.jnet.protocol.tcpipREFACTOR.ip;

import java.util.concurrent.TimeUnit;

import com.slytechs.jnet.protocol.tcpipREFACTOR.ip.ReassemblyContext.FragmentAgeStats;

/**
 * Gets a detailed status report about the current state of reassembly. Includes
 * comprehensive information about progress, timing, and fragment state.
 */
class ReassemblyStatus {
	private final ReassemblyContext context;
	private final long currentTime;

	public ReassemblyStatus(ReassemblyContext context) {
		this.context = context;
		this.currentTime = System.nanoTime();
	}

	/**
	 * Format timing duration in a human-readable format.
	 */
	private String formatDuration(long nanos) {
		long millis = TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS);
		if (millis < 1000)
			return millis + " ms";
		long seconds = millis / 1000;
		if (seconds < 60)
			return seconds + " sec";
		return String.format("%d min %d sec", seconds / 60, seconds % 60);
	}

	@Override
	public String toString() {
		FragmentAgeStats ageStats = context.getFragmentAgeStats();

		return String.format("""
				Reassembly Status:
				Progress:
				  - Received Bytes: %,d
				  - Expected Size: %,d
				  - Completion: %.1f%%
				  - Remaining: %,d bytes

				Fragments:
				  - Count: %d
				  - Has Last Fragment: %s
				  - Gaps Present: %s
				  - Largest Gap: %,d bytes
				  - Overlapped Count: %d

				Fragment Sizes:
				  - Smallest: %,d bytes
				  - Largest: %,d bytes
				  - Average: %.1f bytes

				Timing:
				  - Age: %s
				  - Last Activity: %s ago
				  - Fragment Ages:
				      Min: %s
				      Max: %s
				      Avg: %s

				Offsets:
				  - Smallest: %,d
				  - Largest: %,d
				""",
				// Progress
				context.getReceivedBytes(),
				context.getExpectedSize(),
				context.getCompletionPercentage(),
				context.getRemainingBytes(),

				// Fragments
				context.getFragmentCount(),
				context.hasLastFragment(),
				context.hasGaps(),
				context.getLargestGapSize(),
				context.getOverlappedFragmentCount(),

				// Fragment Sizes
				context.getSmallestFragmentSize(),
				context.getLargestFragmentSize(),
				context.getAverageFragmentSize(),

				// Timing
				formatDuration(currentTime - context.getCreationTime()),
				formatDuration(currentTime - context.getLastFragmentTime()),
				formatDuration(ageStats.minAgeNanos),
				formatDuration(ageStats.maxAgeNanos),
				formatDuration(ageStats.avgAgeNanos),

				// Offsets
				context.getSmallestOffset(),
				context.getLargestOffset());
	}

}
