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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ip.reassembly;

/**
 * Ip fragmentation information interface implemented by both IPv4 and IPv6
 * headers. Information is stored differently in IPv4 and IPv6 datagrams.
 * 
 * <p>
 * This interface abstracts how fragmentation information accessed between the
 * two different IP header versions.
 * </p>
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface IpFragment {

	/**
	 * Fragment byte offset.
	 *
	 * @return the int
	 */
	int fragmentByteOffset();

	/**
	 * The fragment offset. The offset, in 8-octet units, of the data following this
	 * header, relative to the start of the Fragmentable Part of the original
	 * packet. *
	 * 
	 * @return the unsigned fragment data offset in units of 8-bytes
	 */
	int fragmentOffset();

	/**
	 * A flag which indicates that there are more fragments to follow. If the flag
	 * is false, this indicates that this is the last fragment of the fragmented
	 * original datagram.
	 *
	 * @return true, if bit flag is set to 1, otherwise false
	 */
	boolean hasMoreFragments();

	/**
	 * A flag which indicates that there this IP datagram should not be fragmented.
	 * If the flag is false, this indicates that this datagram can be fragmented.
	 *
	 * @return true, if bit flag is set to 1, otherwise false
	 */
	boolean hasDoNotFragment();

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
	 * @return the identifier field value.
	 */
	int fragmentIdentifier();
}
