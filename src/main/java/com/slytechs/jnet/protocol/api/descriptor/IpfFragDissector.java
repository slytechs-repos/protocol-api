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
package com.slytechs.jnet.protocol.api.descriptor;

import static com.slytechs.jnet.protocol.api.descriptor.DescriptorConstants.*;
import static com.slytechs.jnet.protocol.tcpipREFACTOR.ip.Ip4Constants.*;
import static com.slytechs.jnet.protocol.tcpipREFACTOR.ip.Ip6Constants.*;
import static com.slytechs.jnet.protocol.tcpipREFACTOR.ip.IpConstants.*;

import java.nio.ByteBuffer;

import com.slytechs.jnet.platform.api.util.Bits;
import com.slytechs.jnet.protocol.api.core.L3FrameType;
import com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL2DissectorJava;
import com.slytechs.jnet.protocol.tcpipREFACTOR.ip.IpAddress;

/**
 * An IP fragment dissector which generates a IpfFragment descriptor for further
 * IPF reassembly and tracking.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public class IpfFragDissector extends PacketL2DissectorJava {

	private int ipType;
	private int ipOffset;
	private int ipHeaderSize;
	private int ipNextHeader;
	private boolean ipIsFrag;
	private boolean ipLastFrag;
	private int ipFragOffset;
	private int ipIdent;
	private short ipTotalLen;
	private int fragDataOffset;
	private int fragDataLength;

	private final byte[] ip4_src = new byte[IpAddress.IPv4_ADDRESS_SIZE];
	private final byte[] ip4_dst = new byte[IpAddress.IPv4_ADDRESS_SIZE];
	private final byte[] ip6_src = new byte[IpAddress.IPv6_ADDRESS_SIZE];
	private final byte[] ip6_dst = new byte[IpAddress.IPv6_ADDRESS_SIZE];

	/**
	 * @see com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissector#writeDescriptor(java.nio.ByteBuffer)
	 */
	@Override
	public int writeDescriptor(ByteBuffer buffer) {
		if (!ipIsFrag)
			return 0;

		IpfFragmentLayout.IP_TYPE.setInt(ipType, buffer);
		IpfFragmentLayout.IP_IS_FRAG.setInt(ipIsFrag ? 1 : 0, buffer);
		IpfFragmentLayout.IP_IS_LAST.setInt(ipLastFrag ? 1 : 0, buffer);
		IpfFragmentLayout.IP_HDR_OFFSET.setInt(ipOffset, buffer);
		IpfFragmentLayout.IP_HDR_LEN.setInt(ipHeaderSize, buffer);
		IpfFragmentLayout.IP_NEXT.setInt(ipNextHeader, buffer);
		IpfFragmentLayout.FIELD_FRAG_OFFSET.setInt(ipFragOffset, buffer);
		IpfFragmentLayout.FIELD_IDENTIFIER.setInt(ipIdent, buffer);
		IpfFragmentLayout.FRAG_DATA_OFFSET.setInt(fragDataOffset, buffer);
		IpfFragmentLayout.FRAG_DATA_LEN.setInt(fragDataLength, buffer);

		if (ipType == L3FrameType.L3_FRAME_TYPE_IPv4) {
			IpfFragmentLayout.KEY_IPv4_SRC.setByteArray(ip4_src, buffer);
			IpfFragmentLayout.KEY_IPv4_DST.setByteArray(ip4_dst, buffer);
		} else {
			IpfFragmentLayout.KEY_IPv6_SRC.setByteArray(ip6_src, buffer);
			IpfFragmentLayout.KEY_IPv6_DST.setByteArray(ip6_dst, buffer);
		}

		buffer.position(buffer.position() + DESC_IPF_FRAG_BYTE_SIZE);

		return DESC_IPF_FRAG_BYTE_SIZE;
	}

	public IpfFragDissector() {
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL2DissectorJava#dissectIp(int)
	 */
	@Override
	protected void dissectIp(int offset) {
		if (!hasRemaining(offset, IPv4_HEADER_LEN))
			return;

		this.ipOffset = offset;

		int r0 = Byte.toUnsignedInt(buf.get(ipOffset + IPv4_FIELD_VER)); // 07:00 IP header len & version
		int ver = calcIpVersion((byte) r0); // Common to IPv4 and IPv6

		int nextHeader = NO_NEXT_HEADER;

		if (ver == 4) {
			this.ipType = L3FrameType.L3_FRAME_TYPE_IPv4;
			this.ipHeaderSize = ((r0 >> 0) & Bits.BITS_04) << 2;
			ipNextHeader = buf.get(ipOffset + IPv4_FIELD_PROTOCOL);

			int sword3 = buf.getShort(ipOffset + IPv4_FIELD_FLAGS);
			boolean mf = (sword3 & IPv4_FLAG16_MF) > 0;
			this.ipFragOffset = (sword3 & IPv4_MASK16_FRAGOFF) << 3;

			this.ipIsFrag = mf || (ipFragOffset > 0);
			this.ipLastFrag = !mf && (ipFragOffset > 0);
			this.ipIdent = buf.getShort(ipOffset + IPv4_FIELD_IDENT);
			this.ipTotalLen = buf.getShort(ipOffset + IPv4_FIELD_TOTAL_LEN);
			this.fragDataOffset = offset + ipHeaderSize;
			this.fragDataLength = ipTotalLen - ipHeaderSize;

			buf.get(offset + IPv4_FIELD_SRC, ip4_src);
			buf.get(offset + IPv4_FIELD_DST, ip4_dst);

		} else if (hasRemaining(ipOffset, IPv6_HEADER_LEN)) {
			this.ipType = L3FrameType.L3_FRAME_TYPE_IPv6;
			this.ipHeaderSize = IPv6_HEADER_LEN >> 2;

			nextHeader = Byte.toUnsignedInt(buf.get(ipOffset + IPv6_FIELD_NEXT_HOP));

			buf.get(offset + IPv6_FIELD_SRC, ip6_src);
			buf.get(offset + IPv6_FIELD_DST, ip6_dst);

			dissectIp6Options(ipOffset, nextHeader);
		}
	}

	protected void dissectIp6Options(int offset, int nextHeader) {

		int extLen = 0;
		offset += IPv6_HEADER_LEN;

		// Calculate IPv6 header size including options
		LOOP: while (hasRemaining(offset + 2)) {

			// IPv6 options
			switch (nextHeader) {
			case IP_TYPE_IPv6_FRAGMENT_HEADER: {
				int sword1 = buf.getShort(offset + IPv6_FIELD_FRAG_OFFSET);
				boolean mf = (sword1 & IPv4_FLAG16_MF) > 0;
				this.ipFragOffset = (sword1 & IPv4_MASK16_FRAGOFF);

				this.ipIsFrag = true;
				this.ipLastFrag = !mf;
				this.ipIdent = buf.getInt(IPv6_FIELD_IDENTIFICATION);
			}

			case IP_TYPE_IPv6_HOP_BY_HOP:
			case IP_TYPE_IPv6_DESTINATION_OPTIONS:
			case IP_TYPE_IPv6_ROUTING_HEADER:
			case IP_TYPE_IPv6_SHIM6_PROTOCOL: {// Shim6 protocol
				nextHeader = buf.get(offset + extLen + 0);
				int len = (buf.get(offset + extLen + 1) << 3) + 8; // (in units of 8 bytes)

				extLen += len;
				offset += len;
				break;
			}

			case IP_TYPE_NO_NEXT: // No Next Header
			default:
				break LOOP;
			}

		} // End LOOP:
	}

	protected final int calcIpVersion(byte versionField) {
		return (versionField >> 4) & Bits.BITS_04;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissector#reset()
	 */
	@Override
	public void reset() {
		super.reset();
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL2DissectorJava#addRecord(int,
	 *      int, int)
	 */
	@Override
	protected boolean addRecord(int id, int offset, int length) {
		return true;
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL2DissectorJava#destroyDissector()
	 */
	@Override
	protected void destroyDissector() {
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL2DissectorJava#dissectExtensionType(java.nio.ByteBuffer,
	 *      int, int, int)
	 */
	@Override
	protected void dissectExtensionType(ByteBuffer buf, int offset, int id, int nextHeader) {
	}

	/**
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL2DissectorJava#dissectIpx(int)
	 */
	@Override
	protected void dissectIpx(int offset) {
	}

	@Override
	protected boolean isDissectionSuccess() {
		return ipIsFrag;
	}

}
