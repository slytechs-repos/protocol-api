package com.slytechs.jnet.protocol.tcpip.network;

import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.util.BitSet;

import com.slytechs.jnet.protocol.api.descriptor.IpfFragment;

/**
 * Manages the state and data for a single IP datagram reassembly operation.
 * Optimized for high-throughput packet processing with minimal allocation.
 */
class IpReassemblyContext {

	// Binary format of descriptor buffer
	private static final int OFFSET_KEY_HASH = 0; // 8 bytes
	private static final int OFFSET_START_TIME = 8; // 8 bytes
	private static final int OFFSET_LAST_UPDATE = 16; // 8 bytes
	private static final int OFFSET_CURRENT_SIZE = 24; // 4 bytes
	private static final int OFFSET_EXPECTED_SIZE = 28; // 4 bytes
	private static final int OFFSET_FRAG_COUNT = 32; // 4 bytes
	private static final int OFFSET_LAST_OFFSET = 36; // 4 bytes
	private static final int OFFSET_FLAGS = 40; // 4 bytes
	private static final int OFFSET_RESERVED = 44; // 4 bytes
	private static final int OFFSET_BITMAP = 48; // 16 bytes for bitmap

	// Flag positions in flags field
	private static final int FLAG_HAS_LAST_FRAGMENT = 0;
	private static final int FLAG_IS_COMPLETE = 1;
	private static final int FLAG_HAS_GAPS = 2;
	private static final int FLAG_IS_IPV6 = 3;

	private static final int MAX_FRAGMENTS_BITS = 128;

	private final ByteBuffer descriptorBuffer;
	private final ByteBuffer datagramBuffer;
	private final IpReassemblyConfig config;
	private final BitSet fragmentBitmap;
	private FragmentKey key;
	private IpfFragment descriptor;

	/**
	 * Creates context with pre-allocated buffers.
	 */
	IpReassemblyContext(ByteBuffer descriptorBuffer, ByteBuffer datagramBuffer, IpReassemblyConfig config) {
		this.descriptorBuffer = descriptorBuffer;
		this.datagramBuffer = datagramBuffer;
		this.config = config;
		this.fragmentBitmap = new BitSet(MAX_FRAGMENTS_BITS);
	}

	/**
	 * Resets this context for reuse.
	 */
	void reset() {
		descriptorBuffer.clear();
		datagramBuffer.clear();
		fragmentBitmap.clear();

		// Fast clear using longs
		for (int i = 0; i < descriptorBuffer.capacity(); i += 8) {
			descriptorBuffer.putLong(i, 0L);
		}

		key = null;
		descriptor = null;
	}

	/**
	 * Initializes context for a new datagram.
	 */
	void initialize(IpfFragment descriptor, long timestamp) {
		this.descriptor = descriptor;

		setKeyHash(descriptor.hashCode());
		setStartTime(timestamp);
		setLastUpdateTime(timestamp);
		setCurrentSize(0);
		setExpectedSize(0);
		setFragmentCount(0);
		setLastOffset(0);
		clearFlags();

		setFlag(FLAG_IS_IPV6, descriptor.isIp6());
	}

	/**
	 * Updates context state with new fragment info.
	 */
	void updateState(IpfFragment fragment, long timestamp) {
		setLastUpdateTime(timestamp);
		setFragmentCount(getFragmentCount() + 1);

		int fragOffset = fragment.fragOffset();
		int dataLength = fragment.dataLength();

		int fragmentIndex = fragOffset >> 3;
		if (fragmentIndex < MAX_FRAGMENTS_BITS) {
			fragmentBitmap.set(fragmentIndex);
		}

		if (fragment.isLastFrag()) {
			setFlag(FLAG_HAS_LAST_FRAGMENT, true);
			setExpectedSize(fragOffset + dataLength);
			setLastOffset(fragOffset);
		}

		updateCompletionStatus();
	}

	/**
	 * Copies fragment data from a memory segment.
	 */
	void copyFragmentFromSegment(MemorySegment segment, IpfFragment fragment) {
		int fragOffset = fragment.fragOffset();
		int dataOffset = fragment.dataOffset();
		int dataLength = fragment.dataLength();

		if (fragOffset + dataLength > datagramBuffer.capacity()) {
			throw new IllegalArgumentException("Fragment exceeds maximum datagram size");
		}

		// Get memory segment view of target buffer
		datagramBuffer.position(fragOffset);
		var targetSlice = MemorySegment.ofBuffer(datagramBuffer);

		// Copy data using copyFrom
		targetSlice.copyFrom(segment.asSlice(dataOffset, dataLength));

		int newSize = fragOffset + dataLength;
		if (newSize > getCurrentSize()) {
			setCurrentSize(newSize);
		}
	}

	/**
	 * Copies fragment data from a byte buffer.
	 */
	void copyFragmentFromBuffer(ByteBuffer buffer, IpfFragment fragment) {
		int fragOffset = fragment.fragOffset();
		int dataOffset = fragment.dataOffset();
		int dataLength = fragment.dataLength();

		if (fragOffset + dataLength > datagramBuffer.capacity()) {
			throw new IllegalArgumentException("Fragment exceeds maximum datagram size");
		}

		int savedPosition = buffer.position();
		int savedLimit = buffer.limit();

		try {
			buffer.position(savedPosition + dataOffset);
			buffer.limit(savedPosition + dataOffset + dataLength);

			datagramBuffer.position(fragOffset);
			datagramBuffer.put(buffer);

			int newSize = fragOffset + dataLength;
			if (newSize > getCurrentSize()) {
				setCurrentSize(newSize);
			}
		} finally {
			buffer.position(savedPosition);
			buffer.limit(savedLimit);
		}
	}

	/**
	 * Checks if fragment has been received.
	 */
	boolean hasFragment(IpfFragment fragment) {
		int fragmentIndex = fragment.fragOffset() >> 3;
		return fragmentIndex < MAX_FRAGMENTS_BITS && fragmentBitmap.get(fragmentIndex);
	}

	private void updateCompletionStatus() {
		if (!hasLastFragment()) {
			return;
		}

		int lastFragmentIndex = getLastOffset() >> 3;
		boolean hasAllFragments = true;

		int byteCount = (lastFragmentIndex + 7) >> 3;
		byte[] bitmapBytes = fragmentBitmap.toByteArray();

		for (int i = 0; i < byteCount && i < bitmapBytes.length; i++) {
			if (bitmapBytes[i] != (byte) 0xFF) {
				hasAllFragments = false;
				setFlag(FLAG_HAS_GAPS, true);
				break;
			}
		}

		if (hasAllFragments) {
			setFlag(FLAG_IS_COMPLETE, true);
		}
	}

	// Package-accessible getters
	long getKeyHash() {
		return descriptorBuffer.getLong(OFFSET_KEY_HASH);
	}

	long getStartTime() {
		return descriptorBuffer.getLong(OFFSET_START_TIME);
	}

	long getLastUpdateTime() {
		return descriptorBuffer.getLong(OFFSET_LAST_UPDATE);
	}

	int getCurrentSize() {
		return descriptorBuffer.getInt(OFFSET_CURRENT_SIZE);
	}

	int getExpectedSize() {
		return descriptorBuffer.getInt(OFFSET_EXPECTED_SIZE);
	}

	int getFragmentCount() {
		return descriptorBuffer.getInt(OFFSET_FRAG_COUNT);
	}

	int getLastOffset() {
		return descriptorBuffer.getInt(OFFSET_LAST_OFFSET);
	}

	boolean hasLastFragment() {
		return getFlag(FLAG_HAS_LAST_FRAGMENT);
	}

	boolean isComplete() {
		return getFlag(FLAG_IS_COMPLETE);
	}

	boolean hasGaps() {
		return getFlag(FLAG_HAS_GAPS);
	}

	boolean isIpv6() {
		return getFlag(FLAG_IS_IPV6);
	}

	FragmentKey getKey() {
		return key;
	}

	IpfFragment getDescriptor() {
		return descriptor;
	}

	/**
	 * Checks if there are any missing fragments in the sequence. This method should
	 * only be called if we have received the last fragment.
	 *
	 * @return true if there are missing fragments, false otherwise
	 */
	boolean hasMissingFragments() {
		// If we don't have the last fragment, we can't know if fragments are missing
		if (!hasLastFragment()) {
			return true;
		}

		// Check all bits up to the last fragment
		int lastFragmentIndex = getLastOffset() >> 3;

		// Get bitmap bytes for efficient checking
		byte[] bitmapBytes = fragmentBitmap.toByteArray();
		int byteCount = (lastFragmentIndex + 7) >> 3;

		// Check all complete bytes
		for (int i = 0; i < byteCount - 1 && i < bitmapBytes.length; i++) {
			if (bitmapBytes[i] != (byte) 0xFF) {
				return true;
			}
		}

		// Check final byte if needed
		if (byteCount > 0 && byteCount <= bitmapBytes.length) {
			int remainingBits = lastFragmentIndex + 1 - ((byteCount - 1) << 3);
			byte mask = (byte) ((1 << remainingBits) - 1);
			if ((bitmapBytes[byteCount - 1] & mask) != mask) {
				return true;
			}
		}

		return false;
	}

	ByteBuffer getDatagramBuffer() {
		datagramBuffer.clear().limit(getCurrentSize());
		return datagramBuffer.asReadOnlyBuffer();
	}

	/**
	 * Gets the current size of the reassembled datagram.
	 *
	 * @return size in bytes of the current reassembled datagram
	 */
	int getDatagramSize() {
		return getCurrentSize();
	}

	// Private setters
	private void setKeyHash(long hash) {
		descriptorBuffer.putLong(OFFSET_KEY_HASH, hash);
	}

	private void setStartTime(long time) {
		descriptorBuffer.putLong(OFFSET_START_TIME, time);
	}

	private void setLastUpdateTime(long time) {
		descriptorBuffer.putLong(OFFSET_LAST_UPDATE, time);
	}

	private void setCurrentSize(int size) {
		descriptorBuffer.putInt(OFFSET_CURRENT_SIZE, size);
	}

	private void setExpectedSize(int size) {
		descriptorBuffer.putInt(OFFSET_EXPECTED_SIZE, size);
	}

	private void setFragmentCount(int count) {
		descriptorBuffer.putInt(OFFSET_FRAG_COUNT, count);
	}

	private void setLastOffset(int offset) {
		descriptorBuffer.putInt(OFFSET_LAST_OFFSET, offset);
	}

	private void setFlag(int flag, boolean value) {
		int flags = descriptorBuffer.getInt(OFFSET_FLAGS);
		flags = value ? flags | (1 << flag) : flags & ~(1 << flag);
		descriptorBuffer.putInt(OFFSET_FLAGS, flags);
	}

	private boolean getFlag(int flag) {
		return (descriptorBuffer.getInt(OFFSET_FLAGS) & (1 << flag)) != 0;
	}

	private void clearFlags() {
		descriptorBuffer.putInt(OFFSET_FLAGS, 0);
	}

	@Override
	public String toString() {
		return String.format(
				"IpReassemblyContext[size=%d/%d, fragments=%d, complete=%b, gaps=%b]",
				getCurrentSize(), getExpectedSize(), getFragmentCount(),
				isComplete(), hasGaps());
	}
}