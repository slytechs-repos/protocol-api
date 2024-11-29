package com.slytechs.jnet.protocol.core.network;

import com.slytechs.jnet.protocol.descriptor.IpfFragment;

/**
 * IPv4 specific packet fragment reassembly implementation.
 */
public class Ip4Reassembler extends AbstractIpReassembler {

	/**
	 * IPv4 minimum fragment size (except last fragment).
	 */
	private static final int IPV4_MIN_FRAGMENT_SIZE = 8;

	/**
	 * Creates new IPv4 reassembler.
	 *
	 * @param config       configuration settings
	 * @param stats        statistics tracker
	 * @param eventManager event dispatch manager
	 */
	public Ip4Reassembler(
			IpReassemblyConfig config,
			IpReassemblyStats stats,
			IpReassemblyEventManager eventManager) {
		super(config, stats, eventManager);
	}

	@Override
	protected boolean validateFragment(IpfFragment descriptor) {
		// Verify this is an IPv4 fragment
		if (!descriptor.isIp4()) {
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
		if (!descriptor.isLastFrag() && fragLength < IPV4_MIN_FRAGMENT_SIZE) {
			return false;
		}

		// Verify total reassembled size won't exceed maximum
		if (fragOffset + fragLength > config.getMaxDatagramSize()) {
			return false;
		}

		// Fragment offset must be multiple of 8 for IPv4
		if ((fragOffset & 0x7) != 0) {
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