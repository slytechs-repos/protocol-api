package com.slytechs.jnet.protocol.tcpipREFACTOR.ip;

import com.slytechs.jnet.protocol.tcpipREFACTOR.ip.reassembly.IpfFragment;

/**
 * IPv6 specific packet fragment reassembly implementation.
 */
public class Ip6Reassembler extends AbstractIpReassembler {

	/**
	 * IPv6 minimum fragment size (except last fragment).
	 */
	private static final int IPV6_MIN_FRAGMENT_SIZE = 8;

	/**
	 * Maximum IPv6 packet size (65535 bytes by default).
	 */
	private static final int IPV6_MAX_PACKET_SIZE = 65535;

	/**
	 * Creates new IPv6 reassembler.
	 *
	 * @param config       configuration settings
	 * @param stats        statistics tracker
	 * @param eventManager event dispatch manager
	 */
	public Ip6Reassembler(
			IpReassemblyConfig config,
			IpReassemblyStats stats,
			IpReassemblyEventManager eventManager) {
		super(config, stats, eventManager);
	}

	@Override
	protected boolean validateFragment(IpfFragment descriptor) {
		// Verify this is an IPv6 fragment
		if (!descriptor.isIp6()) {
			return false;
		}

		// Must be marked as a fragment
		if (!descriptor.isFrag()) {
			return false;
		}

		// Fragment offset and length validations
		int fragOffset = descriptor.fragOffset();
		int fragLength = descriptor.dataLength();

		// Check minimum size (except for last fragment)
		if (!descriptor.isLastFrag() && fragLength < IPV6_MIN_FRAGMENT_SIZE) {
			return false;
		}

		// Verify total reassembled size won't exceed IPv6 max or configured max
		int maxAllowedSize = Math.min(IPV6_MAX_PACKET_SIZE, config.getMaxDatagramSize());
		if (fragOffset + fragLength > maxAllowedSize) {
			return false;
		}

		// Fragment offset must be multiple of 8 for IPv6
		if ((fragOffset & 0x7) != 0) {
			return false;
		}

		// For IPv6, validate next header field
		int nextHeader = descriptor.nextHeader();
		if (nextHeader == IpType.IP_TYPE_IPv6_FRAGMENT_HEADER) {
			// Fragment header cannot contain another fragment header
			return false;
		}

		return true;
	}

	@Override
	protected FragmentKey createFragmentKey(IpfFragment descriptor) {
		FragmentKey key = new FragmentKey();
		key.initFrom(descriptor);
		return key;
	}
}