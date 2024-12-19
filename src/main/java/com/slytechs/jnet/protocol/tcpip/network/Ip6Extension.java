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

/**
 * Interface for all IPv6 extension headers to access extension length and next
 * header type fields.
 * <p>
 * IPv6 extension headers are a set of optional headers that can be used to
 * carry additional information with an IPv6 packet. They are placed between the
 * fixed IPv6 header and the upper-layer protocol header. Extension headers form
 * a chain, using the Next Header field. The Next Header field in the fixed
 * header indicates the type of the first extension header; the Next Header
 * field of the last extension header indicates the type of the upper-layer
 * protocol header in the payload of the packet. All extension headers are a
 * multiple of 8 octets in size; some extension headers require internal padding
 * to meet this requirement.
 * </p>
 * <p>
 * There are several different types of IPv6 extension headers, each with its
 * own purpose. Some of the most common extension headers include:
 * </p>
 * 
 * <ul>
 * <li><b>Routing header:</b> This header is used to specify a list of one or
 * more intermediate nodes that the packet must pass through before reaching its
 * destination.</li>
 * <li><b>Fragmentation header:</b> This header is used to fragment a large
 * packet into smaller packets that can be forwarded through smaller network
 * links.</li>
 * <li><b>Authentication header:</b> This header is used to provide
 * authentication and integrity for an IPv6 packet.</li>
 * <li><b>Encapsulation header:</b> This header is used to encapsulate an IPv6
 * packet within another protocol, such as IPsec.</li>
 * <li><b>Destination options header:</b> This header is used to carry optional
 * information that is only intended for the destination node.</li>
 * </ul>
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface Ip6Extension {

	/**
	 * Gets the IPv6 extension header length field value (in 8-byte units).
	 * <p>
	 * The IPv6 extension header length field is a 8-bit field that specifies the
	 * length of the extension header in 8-byte units. The extension header length
	 * field is located at the beginning of each extension header.
	 * </p>
	 * 
	 * @return the unsigned 8-bit header length field value (in 8-byte units)
	 */
	int extensionLength();

	/**
	 * Gets the IPv6 extension header length field value (in 1-byte units).
	 *
	 * @return the unsigned 8-bit header length field value (in 1-byte units)
	 */
	int extensionLengthBytes();

	/**
	 * Gets the IPv6 extension next header field value.
	 * <p>
	 * The IPv6 extension next header field is a 8-bit field that specifies the type
	 * of the next header in the IPv6 packet. The next header field is located at
	 * the end of each extension header.
	 * </p>
	 *
	 * @return the unsigned 8-bit next header field value
	 */
	int nextHeader();

}