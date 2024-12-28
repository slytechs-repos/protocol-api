package com.slytechs.jnet.protocol.tcpipREFACTOR.ip.reassembly;

import java.util.concurrent.TimeUnit;

/**
 * Configuration class for IP fragment reassembly system.
 * 
 * <p>
 * This class provides configuration parameters for the IP reassembly process,
 * including table size, timeout values, and memory limits. All parameters are
 * validated when set to ensure system stability.
 * </p>
 */
public class IpReassemblyConfig {

    private static final int DEFAULT_MAX_TABLE_SIZE = 1024;
    private static final int DEFAULT_MAX_SEGMENTS = 256;
    private static final long DEFAULT_TIMEOUT_NANOS = TimeUnit.SECONDS.toNanos(30);
    private static final int DEFAULT_MAX_DATAGRAM_SIZE = 65535;
    private static final int DEFAULT_MAX_DESCRIPTOR_SIZE = 1024;

    private static final int MIN_TABLE_SIZE = 16;
    private static final int MIN_SEGMENTS = 2;
    private static final long MIN_TIMEOUT_NANOS = TimeUnit.SECONDS.toNanos(1);
    private static final int MIN_DATAGRAM_SIZE = 576;
    private static final int MIN_DESCRIPTOR_SIZE = 128;

    private int maxTableRowCount;
    private int maxSegmentsPerDgram;
    private long reassemblyTimeoutNanos;
    private int maxDatagramSize;
    private int maxDescriptorBufferSize;
    private boolean eventDispatchEnabled;

    /**
     * Creates a new configuration with default values.
     */
    public IpReassemblyConfig() {
        this.maxTableRowCount = DEFAULT_MAX_TABLE_SIZE;
        this.maxSegmentsPerDgram = DEFAULT_MAX_SEGMENTS;
        this.reassemblyTimeoutNanos = DEFAULT_TIMEOUT_NANOS;
        this.maxDatagramSize = DEFAULT_MAX_DATAGRAM_SIZE;
        this.maxDescriptorBufferSize = DEFAULT_MAX_DESCRIPTOR_SIZE;
        this.eventDispatchEnabled = false;
    }

    /**
     * Sets the maximum number of concurrent reassembly operations.
     *
     * @param count maximum number of rows in the reassembly table
     * @return this configuration instance for method chaining
     * @throws IllegalArgumentException if count is less than MIN_TABLE_SIZE
     */
    public IpReassemblyConfig setMaxTableRowCount(int count) {
        if (count < MIN_TABLE_SIZE) {
            throw new IllegalArgumentException(
                    "Table row count must be at least " + MIN_TABLE_SIZE);
        }
        this.maxTableRowCount = count;
        return this;
    }

    /**
     * Sets the maximum number of fragments allowed per datagram.
     *
     * @param count maximum fragments per datagram
     * @return this configuration instance for method chaining
     * @throws IllegalArgumentException if count is less than MIN_SEGMENTS
     */
    public IpReassemblyConfig setMaxSegmentsPerDgram(int count) {
        if (count < MIN_SEGMENTS) {
            throw new IllegalArgumentException(
                    "Segments per datagram must be at least " + MIN_SEGMENTS);
        }
        this.maxSegmentsPerDgram = count;
        return this;
    }

    /**
     * Sets the timeout for reassembly operations in nanoseconds.
     *
     * @param timeoutNanos timeout in nanoseconds
     * @return this configuration instance for method chaining
     * @throws IllegalArgumentException if timeout is less than MIN_TIMEOUT_NANOS
     */
    public IpReassemblyConfig setReassemblyTimeoutNanos(long timeoutNanos) {
        if (timeoutNanos < MIN_TIMEOUT_NANOS) {
            throw new IllegalArgumentException(
                    "Timeout must be at least " + MIN_TIMEOUT_NANOS + " nanoseconds");
        }
        this.reassemblyTimeoutNanos = timeoutNanos;
        return this;
    }

    /**
     * Sets the maximum size of a reassembled datagram.
     *
     * @param size maximum datagram size in bytes
     * @return this configuration instance for method chaining
     * @throws IllegalArgumentException if size is less than MIN_DATAGRAM_SIZE
     */
    public IpReassemblyConfig setMaxDatagramSize(int size) {
        if (size < MIN_DATAGRAM_SIZE) {
            throw new IllegalArgumentException(
                    "Datagram size must be at least " + MIN_DATAGRAM_SIZE + " bytes");
        }
        this.maxDatagramSize = size;
        return this;
    }

    /**
     * Sets the maximum size of the descriptor buffer.
     *
     * @param size maximum descriptor buffer size in bytes
     * @return this configuration instance for method chaining
     * @throws IllegalArgumentException if size is less than MIN_DESCRIPTOR_SIZE
     */
    public IpReassemblyConfig setMaxDescriptorBufferSize(int size) {
        if (size < MIN_DESCRIPTOR_SIZE) {
            throw new IllegalArgumentException(
                    "Descriptor buffer size must be at least " + MIN_DESCRIPTOR_SIZE + " bytes");
        }
        this.maxDescriptorBufferSize = size;
        return this;
    }

    /**
     * Enables or disables event dispatch.
     *
     * @param enabled true to enable event dispatch, false to disable
     * @return this configuration instance for method chaining
     */
    public IpReassemblyConfig setEventDispatchEnabled(boolean enabled) {
        this.eventDispatchEnabled = enabled;
        return this;
    }

    // Getters

    public int getMaxTableRowCount() {
        return maxTableRowCount;
    }

    public int getMaxSegmentsPerDgram() {
        return maxSegmentsPerDgram;
    }

    public long getReassemblyTimeoutNanos() {
        return reassemblyTimeoutNanos;
    }

    public int getMaxDatagramSize() {
        return maxDatagramSize;
    }

    public int getMaxDescriptorBufferSize() {
        return maxDescriptorBufferSize;
    }

    public boolean isEventDispatchEnabled() {
        return eventDispatchEnabled;
    }

    /**
     * Creates a copy of this configuration.
     *
     * @return a new IpReassemblyConfig instance with the same values
     */
    public IpReassemblyConfig copy() {
        return new IpReassemblyConfig()
                .setMaxTableRowCount(maxTableRowCount)
                .setMaxSegmentsPerDgram(maxSegmentsPerDgram)
                .setReassemblyTimeoutNanos(reassemblyTimeoutNanos)
                .setMaxDatagramSize(maxDatagramSize)
                .setMaxDescriptorBufferSize(maxDescriptorBufferSize)
                .setEventDispatchEnabled(eventDispatchEnabled);
    }
}