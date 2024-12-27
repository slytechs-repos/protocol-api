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

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.util.Objects;

import com.slytechs.jnet.platform.api.util.hash.CollisionResistantHashTable;
import com.slytechs.jnet.protocol.api.descriptor.IpfFragment;

/**
 * Abstract base class for IP fragment reassembly implementations. Provides
 * common functionality for both IPv4 and IPv6 reassembly processes.
 */
public abstract class AbstractIpReassembler implements StatefulIpf {

	protected static final int REASSEMBLY_HEADER_SIZE = 64;

	protected final CollisionResistantHashTable<FragmentKey, IpReassemblyContext> reassemblyTable;
	protected final IpReassemblyConfig config;
	protected final IpReassemblyStats stats;
	protected final IpReassemblyEventManager eventManager;

	protected final ByteBuffer[] descriptorBuffers;
	protected final ByteBuffer[] datagramBuffers;

	protected final FragmentKey[] fragmentKeys;
	protected final IpReassemblyContext[] contexts;

	protected final MemorySegment tableSegment;
	protected final Arena arena;

	protected StatefulIpf outputProcessor;

	/**
	 * Creates a new IP reassembler with the specified configuration.
	 */
	protected AbstractIpReassembler(
			IpReassemblyConfig config,
			IpReassemblyStats stats,
			IpReassemblyEventManager eventManager) {

		this.config = Objects.requireNonNull(config, "config cannot be null");
		this.stats = Objects.requireNonNull(stats, "stats cannot be null");
		this.eventManager = Objects.requireNonNull(eventManager, "eventManager cannot be null");

		int maxRows = config.getMaxTableRowCount();
		int maxDatagramSize = config.getMaxDatagramSize();
		int descriptorSize = config.getMaxDescriptorBufferSize();

		// Calculate total memory needed
		long totalSize = (long) maxRows * (maxDatagramSize + descriptorSize);

		// Create arena for native memory management
		this.arena = Arena.ofConfined();

		// Allocate native memory through arena
		this.tableSegment = arena.allocate(totalSize, 8); // 8-byte alignment

		// Initialize arrays
		this.descriptorBuffers = new ByteBuffer[maxRows];
		this.datagramBuffers = new ByteBuffer[maxRows];
		this.fragmentKeys = new FragmentKey[maxRows];
		this.contexts = new IpReassemblyContext[maxRows];

		// Initialize hash table
		this.reassemblyTable = new CollisionResistantHashTable<>(maxRows);

		// Initialize buffers and objects
		initializeBuffersAndObjects(maxRows, maxDatagramSize, descriptorSize);
	}

	private void initializeBuffersAndObjects(int maxRows, int maxDatagramSize, int descriptorSize) {
		long offset = 0;

		for (int i = 0; i < maxRows; i++) {
			// Create descriptor buffer
			descriptorBuffers[i] = tableSegment.asSlice(offset, descriptorSize).asByteBuffer();
			offset += descriptorSize;

			// Create datagram buffer
			datagramBuffers[i] = tableSegment.asSlice(offset, maxDatagramSize).asByteBuffer();
			offset += maxDatagramSize;

			// Create reusable objects
			fragmentKeys[i] = new FragmentKey();
			contexts[i] = new IpReassemblyContext(
					descriptorBuffers[i],
					datagramBuffers[i],
					config);
		}
	}

	/**
	 * Processes an incoming IP fragment.
	 *
	 * @param packetSegment the packet segment
	 * @param packetBuffer  the packet buffer
	 * @param timestamp     the timestamp
	 * @param caplen        the caplen
	 * @param wirelen       the wirelen
	 * @param ipfDescriptor the ipf descriptor
	 */
	@Override
	public void handleIpf(
			MemorySegment packetSegment,
			ByteBuffer packetBuffer,
			long timestamp,
			int caplen,
			int wirelen,
			IpfFragment ipfDescriptor) {

		stats.onFragmentReceived(caplen);

		// Validate fragment
		if (!validateFragment(ipfDescriptor)) {
			stats.onInvalidFragment();
			notifyEvent(IpFragmentEventType.ERROR_INVALID_FRAGMENT, ipfDescriptor);
			return;
		}

		// Get or create reassembly context
		IpReassemblyContext context = getReassemblyContext(ipfDescriptor, timestamp);
		if (context == null) {
			stats.onTableFull();
			notifyEvent(IpFragmentEventType.ERROR_TABLE_FULL, ipfDescriptor);
			return;
		}

		// Process the fragment
		processFragment(context, packetSegment, packetBuffer, ipfDescriptor, timestamp);

		// Check if reassembly is complete
		if (context.isComplete()) {
			completeReassembly(context, timestamp);
		}

		// Cleanup expired entries
		cleanupExpiredEntries(timestamp);
	}

	/**
	 * Validates an incoming fragment.
	 */
	protected abstract boolean validateFragment(IpfFragment descriptor);

	/**
	 * Gets or creates a reassembly context for the fragment.
	 */
	protected IpReassemblyContext getReassemblyContext(IpfFragment descriptor, long timestamp) {
		FragmentKey key = createFragmentKey(descriptor);

		IpReassemblyContext context = reassemblyTable.get(key);
		if (context == null) {
			// Try to get a free slot
			int index = reassemblyTable.indexOf(key);
			if (index < 0) {
				return null; // Table is full
			}

			// Initialize new context
			context = contexts[index];
			context.reset();
			context.initialize(descriptor, timestamp);

			// Store in table
			fragmentKeys[index].copyFrom(key);
			reassemblyTable.put(fragmentKeys[index], context);

			stats.onReassemblyStarted();
			notifyEvent(IpFragmentEventType.REASSEMBLY_STARTED, descriptor);
		}

		return context;
	}

	/**
	 * Creates a fragment key for table lookup.
	 */
	protected abstract FragmentKey createFragmentKey(IpfFragment descriptor);

	/**
	 * Processes a fragment within a reassembly context.
	 */
	protected void processFragment(
			IpReassemblyContext context,
			MemorySegment packetSegment,
			ByteBuffer packetBuffer,
			IpfFragment descriptor,
			long timestamp) {

		// Check for duplicate fragment
		if (context.hasFragment(descriptor)) {
			stats.onDuplicateFragment();
			notifyEvent(IpFragmentEventType.DUPLICATE_FRAGMENT, descriptor);
			return;
		}

		// Copy fragment data
		if (packetSegment != null) {
			context.copyFragmentFromSegment(packetSegment, descriptor);
		} else {
			context.copyFragmentFromBuffer(packetBuffer, descriptor);
		}

		// Update context state
		context.updateState(descriptor, timestamp);

		notifyEvent(IpFragmentEventType.FRAGMENT_ACCEPTED, descriptor);
	}

	/**
	 * Completes the reassembly of a datagram.
	 */
	protected void completeReassembly(IpReassemblyContext context, long timestamp) {
		// Calculate reassembly time
		long reassemblyTime = timestamp - context.getStartTime();

		// Forward completed datagram
		if (outputProcessor != null) {
			outputProcessor.handleIpf(
					null,
					context.getDatagramBuffer(),
					timestamp,
					context.getDatagramSize(),
					context.getDatagramSize(),
					context.getDescriptor());
		}

		// Update statistics
		stats.onReassemblyComplete(reassemblyTime, context.getDatagramSize());

		// Remove from table
		reassemblyTable.remove(context.getKey());

		notifyEvent(IpFragmentEventType.REASSEMBLY_COMPLETE, context.getDescriptor());
	}

	/**
	 * Cleans up expired reassembly entries.
	 */
	protected void cleanupExpiredEntries(long currentTime) {
		long timeout = config.getReassemblyTimeoutNanos();

		reassemblyTable.forEach((key, context, hash, index) -> {
			if (currentTime - context.getLastUpdateTime() > timeout) {
				stats.onTimeout(determineTimeoutType(context));
				stats.onReassemblyFailed();

				notifyEvent(IpFragmentEventType.REASSEMBLY_TIMEOUT, context.getDescriptor());
				reassemblyTable.remove(key);
			}
		});
	}

	/**
	 * Determines the type of timeout that occurred.
	 */
	protected IpReassemblyStats.TimeoutType determineTimeoutType(IpReassemblyContext context) {
		if (!context.hasLastFragment()) {
			return IpReassemblyStats.TimeoutType.NO_LAST_FRAGMENT;
		}
		if (context.hasMissingFragments()) {
			return IpReassemblyStats.TimeoutType.MISSING_FRAGMENTS;
		}
		return IpReassemblyStats.TimeoutType.INCOMPLETE;
	}

	/**
	 * Notifies listeners of reassembly events.
	 */
	protected void notifyEvent(IpFragmentEventType type, IpfFragment descriptor) {
		if (config.isEventDispatchEnabled()) {
			eventManager.dispatchEvent(new IpReassemblyEvent(type, descriptor));
		}
	}

	/**
	 * Shuts down the reassembler and releases resources.
	 */
	public void shutdown() {
		// Clear tables
		reassemblyTable.clear();

		// Close the arena which will free all allocated memory
		arena.close();

		// Reset statistics
		stats.reset();
	}
}