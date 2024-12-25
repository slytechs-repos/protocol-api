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

import java.nio.ByteBuffer;

import com.slytechs.jnet.platform.api.util.collection.IntArrayList;
import com.slytechs.jnet.platform.api.util.collection.IntList;
import com.slytechs.jnet.protocol.api.common.OptionsHeader;
import com.slytechs.jnet.protocol.api.descriptor.IpfReassembly;
import com.slytechs.jnet.protocol.api.descriptor.IpfTracking;
import com.slytechs.jnet.protocol.api.descriptor.PacketDescriptor;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.api.meta.MetaResource;
import com.slytechs.jnet.protocol.tcpip.constants.CoreConstants;
import com.slytechs.jnet.protocol.tcpip.constants.CoreId;
import com.slytechs.jnet.protocol.tcpip.constants.IpfDescriptorType;

/**
 * Internet Protocol base definition.
 * <p>
 * Internet Protocol is a widely used protocol in data communication over
 * various types of networks, particularly in packet-switched layer networks
 * like Ethernet. It provides a logical connection between network devices by
 * assigning identification to each device. IP uses a best effort delivery model
 * and is a connectionless protocol, meaning that neither delivery nor proper
 * sequencing or avoidance of duplicate delivery is guaranteed.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("ip-meta.json")
public sealed class Ip
		extends OptionsHeader
		permits Ip4, Ip6 {

	/** The Constant generic IP header ID, classifier only. */
	public static final int ID = CoreId.CORE_ID_IP;

	/** The version. */
	private int version;

	/**
	 * Instantiates a new common IP header.
	 */
	public Ip() {
		super(ID);
	}

	/**
	 * Instantiates a new version specific IP header.
	 *
	 * @param id the version specific IP header ID
	 */
	protected Ip(int id) {
		super(id);
	}

	/**
	 * Gets the IP destination field value.
	 * <p>
	 * The IP destination field is a field in the IPv4 header that specifies the IP
	 * address of the destination host. The IP destination field is 32 bits wide and
	 * can take on a value of 0 to 4,294,967,295.
	 * </p>
	 * <p>
	 * The IP destination field is used by routers to determine how to forward IP
	 * packets. When a router receives an IP packet, it looks at the IP destination
	 * field to determine which interface to use to forward the packet.
	 * </p>
	 *
	 * @return an array containing the field value, either IPv4 and IPv6 address
	 */
	@Meta(MetaType.ATTRIBUTE)
	public byte[] dst() {
		if (version == 4) {
			return dst(new byte[CoreConstants.IPv4_FIELD_SRC_LEN], 0);
		} else {
			return dst(new byte[CoreConstants.IPv6_FIELD_SRC_LEN], 0);

		}
	}

	/**
	 * Gets the IP destination field value.
	 * <p>
	 * The IP destination field is a field in the IPv4 header that specifies the IP
	 * address of the destination host. The IP destination field is 32 bits wide and
	 * can take on a value of 0 to 4,294,967,295.
	 * </p>
	 * <p>
	 * The IP destination field is used by routers to determine how to forward IP
	 * packets. When a router receives an IP packet, it looks at the IP destination
	 * field to determine which interface to use to forward the packet.
	 * </p>
	 *
	 * @param dst the destination array where the IP address field value should be
	 *            stored
	 * @return an array containing the field value, either IPv4 and IPv6 address
	 */
	public byte[] dst(byte[] dst) {
		return dst(dst, 0);
	}

	/**
	 * Gets the IP destination field value.
	 * <p>
	 * The IP destination field is a field in the IPv4 header that specifies the IP
	 * address of the destination host. The IP destination field is 32 bits wide and
	 * can take on a value of 0 to 4,294,967,295.
	 * </p>
	 * <p>
	 * The IP destination field is used by routers to determine how to forward IP
	 * packets. When a router receives an IP packet, it looks at the IP destination
	 * field to determine which interface to use to forward the packet.
	 * </p>
	 *
	 * @param dst    the destination array where the IP address field value should
	 *               be stored
	 * @param offset an offset into the dst array, where the field value should be
	 *               written
	 * @return an array containing the field value, either IPv4 and IPv6 address
	 */
	public byte[] dst(byte[] dst, int offset) {
		if (version == 4)
			buffer().get(CoreConstants.IPv4_FIELD_DST, dst, offset, CoreConstants.IPv4_FIELD_DST_LEN);
		else
			buffer().get(CoreConstants.IPv6_FIELD_DST, dst, offset, CoreConstants.IPv6_FIELD_DST_LEN);

		return dst;
	}

	/**
	 * Gets the IP destination field value as an ip address object.
	 *
	 * @return the IP address object containing the field value
	 */
	public IpAddress dstAsAddress() {
		return version == 4 ? new Ip4Address(dst()) : new Ip6Address(dst());
	}

	/**
	 * A list of reassembled IP fragments.
	 *
	 * @return array of frame numbers for each of the fragments that were used to
	 *         reassemble this IP datagram. This method never returns null.
	 */
	public final long[] getIpfFrameIndexes() {
		IntList list = new IntArrayList();
		if (hasIpfTrackingDescriptor())
			throw new UnsupportedOperationException("tracking retrieval not implemented yet");
		else
			return new long[0];
	}

	/**
	 * Gets the ipf reassembly descriptor.
	 *
	 * @return the ipf reassembly descriptor
	 */
	public final IpfReassembly getIpfReassemblyDescriptor() {
		return (IpfReassembly) super.descriptor().peekDescriptor(IpfDescriptorType.IPF_REASSEMBLY);
	}

	/**
	 * Gets the ipf tracking descriptor.
	 *
	 * @return the ipf tracking descriptor
	 */
	public final IpfTracking getIpfTrackingDescriptor() {
		return (IpfTracking) super.descriptor().peekDescriptor(IpfDescriptorType.IPF_TRACKING);
	}

	/**
	 * Gets the reassembled buffer.
	 *
	 * @return the reassembled buffer
	 */
	public final ByteBuffer getReassembledBuffer() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/**
	 * Checks if is reassembly tracking.
	 *
	 * @return true, if is reassembly tracking
	 */
	public final boolean hasIpfTrackingDescriptor() {
		return super.descriptor().hasDescriptor(IpfDescriptorType.IPF_TRACKING);
	}

	/**
	 * Checks if this IP datagram has been reassembled.
	 *
	 * @return true, if is reassembled
	 */
	public final boolean isReassembled() {
		return super.descriptor().hasDescriptor(IpfDescriptorType.IPF_REASSEMBLY);
	}

	/**
	 * On bind.
	 *
	 * @see com.slytechs.jnet.platform.api.common.binding.jnetruntime.MemoryBinding#onBind()
	 */
	@Override
	protected void onBind() {
		/* Common to both v4 and v6 */
		this.version = Ip4Struct.VERSION.getInt(buffer());

		super.onBind();
	}

	/**
	 * Returns the length of the IP payload, everything past the IP header in units
	 * of bytes.
	 *
	 * @return the length of the payload data.
	 * @see com.slytechs.jnet.protocol.api.common.Header#payloadLength()
	 */
	@Override
	public int payloadLength() {
		PacketDescriptor desc = descriptor();
		int payloadLen = desc.captureLength() - super.payloadOffset();

		return payloadLen;
	}

	/**
	 * Gets the IP protocol field value.
	 * <p>
	 * The IP protocol field is a field in the IPv4 header that identifies the
	 * upper-layer protocol that is encapsulated in the IP packet. The IP protocol
	 * field is 8 bits wide and can take on a value of 0 to 255.
	 * </p>
	 *
	 * @return the unsigned 8-bit field value.
	 */
	@Meta(MetaType.ATTRIBUTE)
	public int protocol() {
		if (version == 4)
			return Ip4Struct.PROTO.getInt(buffer());
		else
			return Ip6Layout.NEXT.getInt(buffer());
	}

	/**
	 * Gets the IP source address field value.
	 * <p>
	 * The IP source field is a field in the IPv4 header that specifies the IP
	 * address of the source host. The IP source field is 32 bits wide and can take
	 * on a value of 0 to 4,294,967,295.
	 * </p>
	 * <p>
	 * The IP source field is used by routers to determine how to forward IP
	 * packets. When a router receives an IP packet, it looks at the IP source field
	 * to determine which interface to use to send the packet back to the source
	 * host.
	 * </p>
	 * 
	 * @return an array containing the field value, either IPv4 and IPv6 address
	 */
	@Meta(MetaType.ATTRIBUTE)
	public byte[] src() {
		if (version == 4)
			return src(new byte[CoreConstants.IPv4_FIELD_SRC_LEN], 0);
		else
			return src(new byte[CoreConstants.IPv6_FIELD_SRC_LEN], 0);
	}

	/**
	 * Gets the IP source address field value.
	 * <p>
	 * The IP source field is a field in the IPv4 header that specifies the IP
	 * address of the source host. The IP source field is 32 bits wide and can take
	 * on a value of 0 to 4,294,967,295.
	 * </p>
	 * <p>
	 * The IP source field is used by routers to determine how to forward IP
	 * packets. When a router receives an IP packet, it looks at the IP source field
	 * to determine which interface to use to send the packet back to the source
	 * host.
	 * </p>
	 *
	 * @param dst the array where the IP address should be written to
	 * @return an array containing the field value, either IPv4 and IPv6 address
	 */
	public byte[] src(byte[] dst) {
		return src(dst, 0);
	}

	/**
	 * Gets the IP source address field value.
	 * <p>
	 * The IP source field is a field in the IPv4 header that specifies the IP
	 * address of the source host. The IP source field is 32 bits wide and can take
	 * on a value of 0 to 4,294,967,295.
	 * </p>
	 * <p>
	 * The IP source field is used by routers to determine how to forward IP
	 * packets. When a router receives an IP packet, it looks at the IP source field
	 * to determine which interface to use to send the packet back to the source
	 * host.
	 * </p>
	 *
	 * @param dst    the array where the IP address should be written to
	 * @param offset the offset into the dst array where to start writing field
	 *               value to
	 * @return an array containing the field value, either IPv4 and IPv6 address
	 */
	public byte[] src(byte[] dst, int offset) {
		if (version == 4)
			buffer().get(CoreConstants.IPv4_FIELD_SRC, dst, offset, CoreConstants.IPv4_FIELD_SRC_LEN);
		else
			buffer().get(CoreConstants.IPv6_FIELD_SRC, dst, offset, CoreConstants.IPv6_FIELD_SRC_LEN);

		return dst;
	}

	/**
	 * Gets the IP source address as an ip address object containing the field
	 * value.
	 *
	 * @return the ip address object with the field's value
	 */
	public IpAddress srcAsAddress() {
		return version == 4
				? new Ip4Address(src())
				: new Ip6Address(src());
	}

	/**
	 * Gets the IP header version number.
	 * <p>
	 * The IP version field is the first field in the IPv4 header. It is a 4-bit
	 * field that specifies the version of the IP protocol being used. For IPv4, the
	 * value of this field is 4.
	 * </p>
	 *
	 * @return the IP header version as read from the first byte of the header
	 *         buffer
	 */
	@Meta
	public int version() {
		return this.version;
	}

}
