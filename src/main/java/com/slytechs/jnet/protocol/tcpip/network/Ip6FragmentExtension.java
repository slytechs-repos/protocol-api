/*
 * Sly Technologies Free License
 * 
 * Copyright 2023 Sly Technologies Inc.
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
package com.slytechs.jnet.protocol.tcpip.network;

import com.slytechs.jnet.protocol.tcpip.constants.CoreId;

/**
 * IPv6 fragmentation extension header.
 * <p>
 * The IPv6 fragmentation header is an extension header that is used to fragment
 * IPv6 packets that are too large to be forwarded by a particular node.
 * </p>
 * <p>
 * When a node receives an IPv6 packet that is too large to be forwarded, the
 * node will fragment the packet into smaller pieces. The fragmentation header
 * will be added to each fragment, and the node will set the M flag to 0 for all
 * but the last fragment. The node will then forward each fragment to the next
 * hop.
 * </p>
 * <p>
 * When the destination node receives the fragments, it will reassemble the
 * fragments into the original packet. The destination node will check the M
 * flag on each fragment to determine the order in which the fragments should be
 * reassembled. The destination node will then discard any fragments that are
 * not part of the original packet.
 * </p>
 * <h2>Example of IPv6 routing header in use (from RFC 2460/8200):</h2>
 * 
 * <pre>
 * 
   +---------------+------------------------
   |  IPv6 header  | TCP header + data
   |               |
   | Next Header = |
   |      TCP      |
   +---------------+------------------------

   +---------------+----------------+------------------------
   |  IPv6 header  | Routing header | TCP header + data
   |               |                |
   | Next Header = |  Next Header = |
   |    Routing    |      TCP       |
   +---------------+----------------+------------------------

   +---------------+----------------+-----------------+-----------------
   |  IPv6 header  | Routing header | Fragment header | fragment of TCP
   |               |                |                 |  header + data
   | Next Header = |  Next Header = |  Next Header =  |
   |    Routing    |    Fragment    |       TCP       |
   +---------------+----------------+-----------------+-----------------
 * 
 * </pre>
 * 
 * <h2>IPv6 fragment header format</h2>
 * <p>
 * The Fragment header is identified by a Next Header value of 44 in the
 * immediately preceding header and has the following format:
 * </p>
 * 
 * <pre>
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |  Next Header  |   Reserved    |      Fragment Offset    |Res|M|
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                         Identification                        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * </pre>
 */
public final class Ip6FragmentExtension extends Ip6ExtensionHeader implements IpFragment {

	/** The id. */
	public static int ID = CoreId.CORE_ID_IPv6_EXT_FRAGMENT;

	/**
	 * Instantiates a new ip 6 fragment option.
	 */
	public Ip6FragmentExtension() {
		super(ID);
	}

	/**
	 * Fragment byte offset.
	 * <p>
	 * This field indicates the offset of the fragment from the beginning of the
	 * original datagram, in units of 8 bytes.
	 * </p>
	 *
	 * @return the calculated fragment offset in units of bytes
	 */
	@Override
	public int fragmentByteOffset() {
		return fragmentOffset() << 3;
	}

	/**
	 * The fragment offset. The offset, in 8-octet units, of the data following this
	 * header, relative to the start of the Fragmentable Part of the original
	 * packet. *
	 * 
	 * @return the unsigned 13-bit fragment data offset in units of 8-bytes
	 */
	@Override
	public int fragmentOffset() {
		return Short.toUnsignedInt(buffer().getShort(2)) & 0x1FFF;
	}

	/**
	 * A flag which indicates that there are more fragments to follow. If the flag
	 * is false, this indicates that this is the last fragment of the fragmented
	 * original datagram.
	 *
	 * @return true, if bit flag is set to 1, otherwise false
	 */
	@Override
	public boolean hasMoreFragments() {
		return (buffer().get(3) & 0x8) != 0;
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpip.network.IpFragment#hasDoNotFragment()
	 */
	@Override
	public boolean hasDoNotFragment() {
		return false;
	}

	/**
	 * Unique "original" packet identifier.
	 * <p>
	 * For every packet that is to be fragmented, the source node generates an
	 * Identification value. The Identification must be different than that of any
	 * other fragmented packet sent recently with the same Source Address and
	 * Destination Address. If a Routing header is present, the Destination Address
	 * of concern is that of the final destination.
	 * </p>
	 *
	 * @return the 32-bit identifier field value.
	 */
	@Override
	public int fragmentIdentifier() {
		return buffer().getInt(4);
	}
}