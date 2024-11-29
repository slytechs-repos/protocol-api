package com.slytechs.jnet.protocol.core.network;

import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;

import com.slytechs.jnet.jnetruntime.util.Registration;
import com.slytechs.jnet.protocol.core.network.IpPipeline.StatefulIpf;
import com.slytechs.jnet.protocol.descriptor.IpfFragment;

/**
 * Main IP fragment reassembly system interface. Handles both IPv4 and IPv6
 * packet reassembly.
 */
public class IpReassembly implements StatefulIpf {

	private final IpReassemblyConfig config;
	private final IpReassemblyStats stats;
	private final IpReassemblyEventManager eventManager;

	private final Ip4Reassembler ip4Reassembler;
	private final Ip6Reassembler ip6Reassembler;

	private StatefulIpf outputProcessor;

	/**
	 * Creates an IP reassembly processor with default configuration.
	 */
	public IpReassembly() {
		this(new IpReassemblyConfig());
	}

	/**
	 * Creates an IP reassembly processor with specified configuration.
	 *
	 * @param config the reassembly configuration
	 */
	public IpReassembly(IpReassemblyConfig config) {
		this.config = config;
		this.stats = new IpReassemblyStats();
		this.eventManager = IpReassemblyEventManager.create(config.isEventDispatchEnabled());

		// Create protocol-specific reassemblers
		this.ip4Reassembler = new Ip4Reassembler(config, stats, eventManager);
		this.ip6Reassembler = new Ip6Reassembler(config, stats, eventManager);
	}

	/**
	 * Processes an IP fragment. The method determines the IP version and delegates
	 * to the appropriate reassembler.
	 */
	@Override
	public void handleIpf(
			MemorySegment packetSegment,
			ByteBuffer packetBuffer,
			long timestamp,
			int caplen,
			int wirelen,
			IpfFragment ipfDescriptor) {

		// Choose appropriate reassembler based on IP version
		if (ipfDescriptor.isIp4()) {
			ip4Reassembler.handleIpf(packetSegment, packetBuffer, timestamp, caplen, wirelen, ipfDescriptor);
		} else if (ipfDescriptor.isIp6()) {
			ip6Reassembler.handleIpf(packetSegment, packetBuffer, timestamp, caplen, wirelen, ipfDescriptor);
		}
	}

	/**
	 * Adds an event listener for reassembly events.
	 *
	 * @param listener the event listener to add
	 * @return registration object for listener removal
	 */
	public Registration addEventListener(IpReassemblyEventListener listener) {
		return eventManager.addEventListener(listener);
	}

	/**
	 * Gets the current reassembly statistics.
	 *
	 * @return the statistics object
	 */
	public IpReassemblyStats getStats() {
		return stats;
	}

	/**
	 * Gets the current configuration.
	 *
	 * @return the configuration object
	 */
	public IpReassemblyConfig getConfig() {
		return config;
	}

	/**
	 * Shuts down the reassembly system and releases resources.
	 */
	public void shutdown() {
		ip4Reassembler.shutdown();
		ip6Reassembler.shutdown();
	}

	/**
	 * Creates a new reassembly processor with builder pattern.
	 *
	 * @return new builder instance
	 */
	public static Builder newBuilder() {
		return new Builder();
	}

	/**
	 * Builder for configuring and creating IpReassembly instances.
	 */
	public static class Builder {
		private final IpReassemblyConfig config = new IpReassemblyConfig();

		/**
		 * Sets maximum table size.
		 */
		public Builder withMaxTableSize(int size) {
			config.setMaxTableRowCount(size);
			return this;
		}

		/**
		 * Sets maximum segments per datagram.
		 */
		public Builder withMaxSegmentsPerDgram(int count) {
			config.setMaxSegmentsPerDgram(count);
			return this;
		}

		/**
		 * Sets reassembly timeout in nanoseconds.
		 */
		public Builder withReassemblyTimeout(long timeoutNanos) {
			config.setReassemblyTimeoutNanos(timeoutNanos);
			return this;
		}

		/**
		 * Sets maximum datagram size.
		 */
		public Builder withMaxDatagramSize(int size) {
			config.setMaxDatagramSize(size);
			return this;
		}

		/**
		 * Sets maximum descriptor buffer size.
		 */
		public Builder withMaxDescriptorSize(int size) {
			config.setMaxDescriptorBufferSize(size);
			return this;
		}

		/**
		 * Enables or disables event dispatch.
		 */
		public Builder withEventDispatch(boolean enabled) {
			config.setEventDispatchEnabled(enabled);
			return this;
		}

		/**
		 * Builds the IpReassembly instance.
		 *
		 * @return new IpReassembly instance
		 */
		public IpReassembly build() {
			return new IpReassembly(config);
		}
	}
}