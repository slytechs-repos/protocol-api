package com.slytechs.jnet.protocol.tcpip.ip.reassembly;

import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Enhanced context for tracking IP packet reassembly.
 */
class ReassemblyContext {

	/**
	 * Fragment inner class with active timing settingsSupport
	 */
	private static class Fragment {
		final ByteBuffer data;
		final int offset;
		final int length;
		final long receivedTime;
		boolean overlapped;

		Fragment(ByteBuffer data, int offset, int length) {
			this.data = data;
			this.offset = offset;
			this.length = length;
			this.receivedTime = System.nanoTime();
			this.overlapped = false;
		}

		/**
		 * Gets the age of this fragment in nanoseconds.
		 * 
		 * @return age in nanoseconds
		 */
		long getAge() {
			return System.nanoTime() - receivedTime;
		}

		/**
		 * Gets the age of this fragment in the specified time unit.
		 * 
		 * @param unit the time unit for the result
		 * @return age in the specified unit
		 */
		long getAge(TimeUnit unit) {
			return unit.convert(getAge(), TimeUnit.NANOSECONDS);
		}

		/**
		 * Checks if this fragment is older than the specified duration.
		 * 
		 * @param duration the duration to check against
		 * @param unit     the time unit of the duration
		 * @return true if the fragment is older than the specified duration
		 */
		boolean isOlderThan(long duration, TimeUnit unit) {
			return getAge(unit) > duration;
		}
	}

	/**
	 * Statistics about fragment ages in a reassembly context.
	 */
	public static class FragmentAgeStats {
		public final long minAgeNanos;
		public final long maxAgeNanos;
		public final long avgAgeNanos;

		FragmentAgeStats(long minAgeNanos, long maxAgeNanos, long avgAgeNanos) {
			this.minAgeNanos = minAgeNanos;
			this.maxAgeNanos = maxAgeNanos;
			this.avgAgeNanos = avgAgeNanos;
		}

		/**
		 * Gets the specified age value in the requested time unit.
		 * 
		 * @param age  the age value in nanoseconds
		 * @param unit the desired time unit
		 * @return the age converted to the specified unit
		 */
		public long getAge(long age, TimeUnit unit) {
			return unit.convert(age, TimeUnit.NANOSECONDS);
		}

		@Override
		public String toString() {
			return String.format(
					"FragmentAgeStats[min=%d ms, max=%d ms, avg=%d ms]",
					TimeUnit.MILLISECONDS.convert(minAgeNanos, TimeUnit.NANOSECONDS),
					TimeUnit.MILLISECONDS.convert(maxAgeNanos, TimeUnit.NANOSECONDS),
					TimeUnit.MILLISECONDS.convert(avgAgeNanos, TimeUnit.NANOSECONDS));
		}
	}

	/**
	 * Fragment event types indicating the result of fragment processing.
	 */
	enum FragmentEventType {
		// Success events
		COMPLETE,
		ACCEPTED,

		// Fragment issues
		DUPLICATE_FRAGMENT,
		OVERLAPPING_FRAGMENT,
		OUT_OF_ORDER_FRAGMENT,
		FRAGMENT_GAP_DETECTED,

		// Error events
		ERROR_INVALID_FRAGMENT,
		ERROR_INVALID_LENGTH,
		ERROR_INVALID_OFFSET;
	}

	private final FragmentKey key;

	private final long creationTime;

	private final int maxDatagramSize;

	private final NavigableMap<Integer, Fragment> fragments;

	private final BitSet receivedOffsets;
	private int expectedSize;
	private boolean hasLastFragment;
	private int receivedBytes;
	private int largestOffset;

	private int smallestOffset;
	private long lastFragmentTime;

	public ReassemblyContext(FragmentKey key, int maxDatagramSize) {
		this.key = key;
		this.maxDatagramSize = maxDatagramSize;
		this.creationTime = System.nanoTime();
		this.fragments = new TreeMap<>();
		this.receivedOffsets = new BitSet(maxDatagramSize / 8);
		this.lastFragmentTime = creationTime;
		this.smallestOffset = Integer.MAX_VALUE;
		this.largestOffset = 0;
	}

	public FragmentEventType addFragment(ByteBuffer data, int offset, int length, boolean isLastFragment) {
		// Validate fragment
		if (!validateFragment(offset, length)) {
			return FragmentEventType.ERROR_INVALID_FRAGMENT;
		}

		// Check for duplicate
		if (receivedOffsets.get(offset / 8)) {
			return FragmentEventType.DUPLICATE_FRAGMENT;
		}

		// Check for overlaps
		FragmentEventType overlapResult = checkOverlap(offset, length);
		if (overlapResult != null) {
			return overlapResult;
		}

		// Update fragment tracking
		receivedOffsets.set(offset / 8);
		updateFragmentStats(offset, length);

		// Copy fragment data
		ByteBuffer fragmentData = ByteBuffer.allocate(length);
		data.limit(data.position() + length);
		fragmentData.put(data);
		fragmentData.flip();

		// Store fragment
		fragments.put(offset, new Fragment(fragmentData, offset, length));
		receivedBytes += length;
		lastFragmentTime = System.nanoTime();

		// Handle last fragment
		if (isLastFragment) {
			if (hasLastFragment) {
				return FragmentEventType.DUPLICATE_FRAGMENT;
			}
			hasLastFragment = true;
			expectedSize = offset + length;
		}

		return detectEventType();
	}

	public ByteBuffer assembleDatagramIfComplete() {
		if (!isComplete()) {
			return null;
		}

		ByteBuffer assembled = ByteBuffer.allocate(expectedSize);
		fragments.values().forEach(fragment -> {
			fragment.data.mark();
			assembled.put(fragment.data);
			fragment.data.reset();
		});

		assembled.flip();
		return assembled;
	}

	private FragmentEventType checkOverlap(int newOffset, int newLength) {
		var prevEntry = fragments.floorEntry(newOffset);
		if (prevEntry != null) {
			Fragment prev = prevEntry.getValue();
			if (prev.offset + prev.length > newOffset) {
				prev.overlapped = true;
				return FragmentEventType.OVERLAPPING_FRAGMENT;
			}
		}

		var nextEntry = fragments.ceilingEntry(newOffset);
		if (nextEntry != null) {
			Fragment next = nextEntry.getValue();
			if (newOffset + newLength > next.offset) {
				next.overlapped = true;
				return FragmentEventType.OVERLAPPING_FRAGMENT;
			}
		}

		return null;
	}

	public void cleanup() {
		fragments.values().forEach(fragment -> {
			if (fragment.data != null) {
				fragment.data.clear();
			}
		});
		fragments.clear();
		receivedOffsets.clear();
	}

	/**
	 * Gets the number of fragments older than the specified duration.
	 * 
	 * @param duration the duration to check against
	 * @param unit     the time unit of the duration
	 * @return count of fragments older than the specified duration
	 */
	public int countFragmentsOlderThan(long duration, TimeUnit unit) {
		return (int) fragments.values().stream()
				.filter(f -> f.isOlderThan(duration, unit))
				.count();
	}

	private FragmentEventType detectEventType() {
		if (hasGaps()) {
			return FragmentEventType.FRAGMENT_GAP_DETECTED;
		}

		if (isComplete()) {
			return FragmentEventType.COMPLETE;
		}

		if (fragments.size() > 1 &&
				!fragments.containsKey(smallestOffset)) {
			return FragmentEventType.OUT_OF_ORDER_FRAGMENT;
		}

		return FragmentEventType.ACCEPTED;
	}

	/**
	 * Gets the average size of all fragments.
	 */
	public double getAverageFragmentSize() {
		if (fragments.isEmpty()) {
			return 0.0;
		}
		return fragments.values().stream()
				.mapToInt(f -> f.length)
				.average()
				.orElse(0.0);
	}

	public double getCompletionPercentage() {
		if (!hasLastFragment) {
			return 0.0;
		}
		return ((double) receivedBytes / expectedSize) * 100.0;
	}

	public long getCreationTime() {
		return creationTime;
	}

	/**
	 * Gets the expected total size of the reassembled datagram. This value is only
	 * valid if hasLastFragment() returns true.
	 * 
	 * @return expected total size in bytes, or 0 if last fragment not received
	 */
	public int getExpectedSize() {
		return hasLastFragment ? expectedSize : 0;
	}

	/**
	 * Gets statistics about fragment ages in this reassembly context.
	 * 
	 * @return FragmentAgeStats containing min, max, and average ages
	 */
	public FragmentAgeStats getFragmentAgeStats() {
		if (fragments.isEmpty()) {
			return new FragmentAgeStats(0, 0, 0);
		}

		long minAge = Long.MAX_VALUE;
		long maxAge = 0;
		long totalAge = 0;

		for (Fragment fragment : fragments.values()) {
			long age = fragment.getAge();
			minAge = Math.min(minAge, age);
			maxAge = Math.max(maxAge, age);
			totalAge += age;
		}

		long avgAge = totalAge / fragments.size();
		return new FragmentAgeStats(minAge, maxAge, avgAge);
	}

	// Additional tracking methods
	public int getFragmentCount() {
		return fragments.size();
	}

	public FragmentKey getKey() {
		return key;
	}

	/**
	 * Gets the size of the largest fragment received.
	 */
	public int getLargestFragmentSize() {
		if (fragments.isEmpty()) {
			return 0;
		}
		
		return fragments.values().stream()
				.mapToInt(f -> f.length)
				.max()
				.orElse(0);
	}

	public int getLargestGapSize() {
		if (fragments.isEmpty())
			return 0;

		int maxGap = 0;
		int lastEnd = 0;

		for (Fragment fragment : fragments.values()) {
			int gap = fragment.offset - lastEnd;
			maxGap = Math.max(maxGap, gap);
			lastEnd = fragment.offset + fragment.length;
		}

		return maxGap;
	}

	/**
	 * Gets the largest byte offset of any received fragment.
	 */
	public int getLargestOffset() {
		return largestOffset;
	}

	/**
	 * Gets the time of last fragment receipt in nanoseconds.
	 */
	public long getLastFragmentTime() {
		return lastFragmentTime;
	}

	/**
	 * Gets the age of the oldest fragment in this reassembly context.
	 * 
	 * @return age in nanoseconds, or 0 if no fragments
	 */
	public long getOldestFragmentAge() {
		if (fragments.isEmpty()) {
			return 0;
		}

		return fragments.values().stream()
				.mapToLong(Fragment::getAge)
				.max()
				.orElse(0);
	}

	public int getOverlappedFragmentCount() {
		return (int) fragments.values().stream()
				.filter(f -> f.overlapped)
				.count();
	}

	/**
	 * Add the following method to ReassemblyContext:
	 */
	public String getReassemblyStatus() {
		return new ReassemblyStatus(this).toString();
	}

	public long getReassemblyTime() {
		return System.nanoTime() - creationTime;
	}

	/**
	 * Gets the total number of bytes received across all fragments.
	 * 
	 * @return total number of received bytes
	 */
	public int getReceivedBytes() {
		return receivedBytes;
	}

	/**
	 * Gets the number of bytes still needed to complete reassembly. Only valid if
	 * hasLastFragment() returns true.
	 */
	public int getRemainingBytes() {
		if (!hasLastFragment || expectedSize == 0) {
			return 0;
		}
		return Math.max(0, expectedSize - receivedBytes);
	}

	/**
	 * Gets the size of the smallest fragment received.
	 */
	public int getSmallestFragmentSize() {
		if (fragments.isEmpty()) {
			return 0;
		}
		return fragments.values().stream()
				.mapToInt(f -> f.length)
				.min()
				.orElse(0);
	}

	/**
	 * Gets the smallest byte offset of any received fragment.
	 */
	public int getSmallestOffset() {
		return smallestOffset == Integer.MAX_VALUE ? 0 : smallestOffset;
	}

	public long getTimeSinceLastFragment() {
		return System.nanoTime() - lastFragmentTime;
	}

	public boolean hasGaps() {
		if (!hasLastFragment) {
			return true;
		}

		int expectedOffset = 0;
		for (Fragment fragment : fragments.values()) {
			if (fragment.offset != expectedOffset) {
				return true;
			}
			expectedOffset += fragment.length;
		}

		return expectedOffset != expectedSize;
	}

	/**
	 * Checks if the last fragment has been received.
	 * 
	 * @return true if the last fragment has been received, false otherwise
	 */
	public boolean hasLastFragment() {
		return hasLastFragment;
	}

	public boolean hasTimedOut(long timeoutNanos) {
		return (System.nanoTime() - creationTime) > timeoutNanos;
	}

	public boolean isComplete() {
		return hasLastFragment && !hasGaps();
	}

	/**
	 * Removes fragments older than the specified duration.
	 * 
	 * @param duration the duration threshold
	 * @param unit     the time unit of the duration
	 * @return number of fragments removed
	 */
	public int removeFragmentsOlderThan(long duration, TimeUnit unit) {
		int initialSize = fragments.size();
		fragments.values().removeIf(f -> f.isOlderThan(duration, unit));
		return initialSize - fragments.size();
	}

	private void updateFragmentStats(int offset, int length) {
		smallestOffset = Math.min(smallestOffset, offset);
		largestOffset = Math.max(largestOffset, offset + length);
	}

	private boolean validateFragment(int offset, int length) {
		if (offset < 0 || length <= 0) {
			return false;
		}

		if (offset + length > maxDatagramSize) {
			return false;
		}

		if (offset % 8 != 0) { // Check 8-byte alignment
			return false;
		}

		return true;
	}
}