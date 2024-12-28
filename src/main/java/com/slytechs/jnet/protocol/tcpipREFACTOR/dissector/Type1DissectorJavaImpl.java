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
package com.slytechs.jnet.protocol.tcpipREFACTOR.dissector;

import java.nio.ByteBuffer;

import com.slytechs.jnet.protocol.api.core.PacketDescriptorType;
import com.slytechs.jnet.protocol.api.descriptor.DescriptorConstants;
import com.slytechs.jnet.protocol.api.descriptor.impl.Type1DescriptorLayout;

/**
 * The Class Type1DissectorJavaImpl.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public class Type1DissectorJavaImpl extends PacketL3DissectorJava {

	/**
	 * Write descriptor.
	 *
	 * @param desc the desc
	 * @return the int
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL3DissectorJava#writeDescriptor(java.nio.ByteBuffer)
	 */
	@Override
	public int writeDescriptor(ByteBuffer desc) {
		int len = writeDescriptorUsingLayout(desc);

		desc.position(desc.position() + len);

		return len;
	}

	/**
	 * Write descriptor using layout.
	 *
	 * @param desc the desc
	 * @return the int
	 */
	private int writeDescriptorUsingLayout(ByteBuffer desc) {

		Type1DescriptorLayout.TIMESTAMP.setLong(super.timestamp, desc);

		Type1DescriptorLayout.CAPLEN.setInt(super.captureLength, desc);
		Type1DescriptorLayout.L2_FRAME_TYPE.setInt(super.l2Type, desc);
		Type1DescriptorLayout.L3_OFFSET.setInt(super.l3Offset, desc);
		Type1DescriptorLayout.L3_SIZE.setInt(super.l3Size, desc);

		Type1DescriptorLayout.WIRELEN.setInt(super.wireLength, desc);
		Type1DescriptorLayout.VLAN_COUNT.setInt(super.vlanCount, desc);
		Type1DescriptorLayout.MPLS_COUNT.setInt(super.mplsCount, desc);
		Type1DescriptorLayout.L3_FRAME_TYPE.setInt(super.l3Type, desc);
		Type1DescriptorLayout.L4_FRAME_TYPE.setInt(super.l4Type, desc);
		Type1DescriptorLayout.L4_SIZE.setInt(super.l4Size, desc);

		return DescriptorConstants.DESC_TYPE1_BYTE_SIZE;
	}

	/**
	 * Instantiates a new type 1 dissector java impl.
	 */
	public Type1DissectorJavaImpl() {
	}

	/**
	 * Destroy dissector.
	 *
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL3DissectorJava#destroyDissector()
	 */
	@Override
	protected void destroyDissector() {
	}

	/**
	 * Reset.
	 *
	 * @see com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissector#reset()
	 */
	@Override
	public void reset() {
	}

	/**
	 * Dissect extension type.
	 *
	 * @param buf        the buf
	 * @param offset     the offset
	 * @param id         the id
	 * @param nextHeader the next header
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL3DissectorJava#dissectExtensionType(java.nio.ByteBuffer,
	 *      int, int, int)
	 */
	@Override
	protected void dissectExtensionType(ByteBuffer buf, int offset, int id, int nextHeader) {
	}

	/**
	 * Dissect ip 4 options.
	 *
	 * @param offset     the offset
	 * @param hlen       the hlen
	 * @param nextHeader the next header
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL3DissectorJava#dissectIp4Options(int,
	 *      int, int)
	 */
	@Override
	protected void dissectIp4Options(int offset, int hlen, int nextHeader) {
	}

	/**
	 * Dissect ip 6 options.
	 *
	 * @param offset     the offset
	 * @param nextHeader the next header
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL3DissectorJava#dissectIp6Options(int,
	 *      int)
	 */
	@Override
	protected void dissectIp6Options(int offset, int nextHeader) {
	}

	/**
	 * Adds the record.
	 *
	 * @param id     the id
	 * @param offset the offset
	 * @param length the length
	 * @return true, if successful
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL3DissectorJava#addRecord(int,
	 *      int, int)
	 */
	@Override
	protected boolean addRecord(int id, int offset, int length) {
		return true;
	}

	/**
	 * Dissect udp.
	 *
	 * @param offset the offset
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL3DissectorJava#dissectUdp(int)
	 */
	@Override
	protected void dissectUdp(int offset) {
	}

	/**
	 * Dissect gre.
	 *
	 * @param offset the offset
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL3DissectorJava#dissectGre(int)
	 */
	@Override
	protected void dissectGre(int offset) {
	}

	/**
	 * Dissect icmp 6.
	 *
	 * @param offset the offset
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL3DissectorJava#dissectIcmp6(int)
	 */
	@Override
	protected void dissectIcmp6(int offset) {
	}

	/**
	 * Dissect icmp 4.
	 *
	 * @param offset the offset
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL3DissectorJava#dissectIcmp4(int)
	 */
	@Override
	protected void dissectIcmp4(int offset) {
	}

	/**
	 * Dissect sctp.
	 *
	 * @param offset the offset
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL3DissectorJava#dissectSctp(int)
	 */
	@Override
	protected void dissectSctp(int offset) {
	}

	/**
	 * Dissect extension ports.
	 *
	 * @param buf    the buf
	 * @param offset the offset
	 * @param id     the id
	 * @param src    the src
	 * @param dst    the dst
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL3DissectorJava#dissectExtensionPorts(java.nio.ByteBuffer,
	 *      int, int, int, int)
	 */
	@Override
	protected void dissectExtensionPorts(ByteBuffer buf, int offset, int id, int src, int dst) {
	}

	/**
	 * Dissect tcp options.
	 *
	 * @param offset         the offset
	 * @param tcpHeaderLenth the tcp header lenth
	 * @see com.slytechs.jnet.protocol.tcpipREFACTOR.dissector.PacketL3DissectorJava#dissectTcpOptions(int,
	 *      int)
	 */
	@Override
	protected void dissectTcpOptions(int offset, int tcpHeaderLenth) {
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.descriptor.Dissector#descriptorType()
	 */
	@Override
	public PacketDescriptorType descriptorType() {
		return PacketDescriptorType.TYPE1;
	}

}
