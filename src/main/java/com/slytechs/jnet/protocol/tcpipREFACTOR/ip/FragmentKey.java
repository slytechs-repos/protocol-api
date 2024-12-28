package com.slytechs.jnet.protocol.tcpipREFACTOR.ip;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.slytechs.jnet.protocol.tcpipREFACTOR.ip.reassembly.IpfFragment;

/**
 * Key class for identifying and tracking IP fragments. Used as the lookup key
 * in the reassembly hash table.
 */
public class FragmentKey {

	private static final int IPV4_ADDR_LEN = 4;
	private static final int IPV6_ADDR_LEN = 16;

	// Key fields
	private final byte[] srcAddr; // Variable length for IPv4/IPv6
	private final byte[] dstAddr; // Variable length for IPv4/IPv6
	private int identifier; // Fragment identifier
	private int protocol; // IP protocol number
	private boolean isIpv6; // IP version flag

	/**
	 * Creates a new fragment key with maximum address size (IPv6).
	 */
	public FragmentKey() {
		this.srcAddr = new byte[IPV6_ADDR_LEN];
		this.dstAddr = new byte[IPV6_ADDR_LEN];
	}

	/**
	 * Resets the key state for reuse.
	 */
	public void reset() {
		Arrays.fill(srcAddr, (byte) 0);
		Arrays.fill(dstAddr, (byte) 0);
		this.identifier = 0;
		this.protocol = 0;
		this.isIpv6 = false;
	}

	/**
	 * Copies values from another fragment key.
	 *
	 * @param other the key to copy from
	 */
	public void copyFrom(FragmentKey other) {
		System.arraycopy(other.srcAddr, 0, this.srcAddr, 0,
				other.isIpv6 ? IPV6_ADDR_LEN : IPV4_ADDR_LEN);
		System.arraycopy(other.dstAddr, 0, this.dstAddr, 0,
				other.isIpv6 ? IPV6_ADDR_LEN : IPV4_ADDR_LEN);
		this.identifier = other.identifier;
		this.protocol = other.protocol;
		this.isIpv6 = other.isIpv6;
	}

	/**
	 * Initializes key from an IP fragment descriptor.
	 *
	 * @param fragment the IP fragment descriptor
	 */
	public void initFrom(IpfFragment fragment) {
		// Copy source address
		byte[] src = fragment.ipSrc();
		System.arraycopy(src, 0, srcAddr, 0, src.length);

		// Copy destination address
		byte[] dst = fragment.ipDst();
		System.arraycopy(dst, 0, dstAddr, 0, dst.length);

		// Set other fields
		this.identifier = fragment.identifier();
		this.protocol = fragment.nextHeader();
		this.isIpv6 = fragment.isIp6();
	}

	/**
	 * Alternative initialization from a key buffer.
	 *
	 * @param keyBuffer buffer containing key data
	 * @param isIpv6    whether this is an IPv6 key
	 */
	public void initFrom(ByteBuffer keyBuffer, boolean isIpv6) {
		int addrLen = isIpv6 ? IPV6_ADDR_LEN : IPV4_ADDR_LEN;

		// Save buffer state
		int savedPos = keyBuffer.position();

		try {
			// Read addresses
			keyBuffer.get(srcAddr, 0, addrLen);
			keyBuffer.get(dstAddr, 0, addrLen);

			// Read other fields
			this.identifier = keyBuffer.getShort() & 0xFFFF; // Unsigned short
			this.protocol = keyBuffer.get() & 0xFF; // Unsigned byte
			this.isIpv6 = isIpv6;

		} finally {
			// Restore buffer position
			keyBuffer.position(savedPos);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		// Hash address arrays up to the appropriate length
		int addrLen = isIpv6 ? IPV6_ADDR_LEN : IPV4_ADDR_LEN;
		for (int i = 0; i < addrLen; i++) {
			result = prime * result + srcAddr[i];
			result = prime * result + dstAddr[i];
		}

		// Hash other fields
		result = prime * result + identifier;
		result = prime * result + protocol;
		result = prime * result + (isIpv6 ? 1231 : 1237);

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		FragmentKey other = (FragmentKey) obj;
		if (identifier != other.identifier || protocol != other.protocol || isIpv6 != other.isIpv6)
			return false;

		// Compare address arrays up to the appropriate length
		int addrLen = isIpv6 ? IPV6_ADDR_LEN : IPV4_ADDR_LEN;
		for (int i = 0; i < addrLen; i++) {
			if (srcAddr[i] != other.srcAddr[i] || dstAddr[i] != other.dstAddr[i])
				return false;
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FragmentKey[");

		// Format addresses
		sb.append("src=");
		formatAddress(sb, srcAddr);
		sb.append(",dst=");
		formatAddress(sb, dstAddr);

		// Add other fields
		sb.append(",id=0x").append(Integer.toHexString(identifier))
				.append(",proto=").append(protocol)
				.append(",ipv6=").append(isIpv6)
				.append(']');

		return sb.toString();
	}

	private void formatAddress(StringBuilder sb, byte[] addr) {
		int len = isIpv6 ? IPV6_ADDR_LEN : IPV4_ADDR_LEN;
		for (int i = 0; i < len; i++) {
			if (i > 0) {
				sb.append(isIpv6 ? ':' : '.');
			}
			if (isIpv6) {
				sb.append(String.format("%02x", addr[i] & 0xFF));
			} else {
				sb.append(addr[i] & 0xFF);
			}
		}
	}

	/**
	 * Gets the fragment identifier.
	 *
	 * @return the fragment identifier
	 */
	public int getIdentifier() {
		return identifier;
	}

	/**
	 * Gets the IP protocol number.
	 *
	 * @return the protocol number
	 */
	public int getProtocol() {
		return protocol;
	}

	/**
	 * Checks if this is an IPv6 key.
	 *
	 * @return true if IPv6, false if IPv4
	 */
	public boolean isIpv6() {
		return isIpv6;
	}
}