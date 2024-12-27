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
package com.slytechs.jnet.protocol.api.common;

import java.util.function.LongSupplier;

import com.slytechs.jnet.platform.api.util.time.Timestamp;
import com.slytechs.jnet.platform.api.util.time.TimestampUnit;
import com.slytechs.jnet.protocol.api.descriptor.impl.PacketDescriptor;
import com.slytechs.jnet.protocol.api.meta.Meta;
import com.slytechs.jnet.protocol.api.meta.Meta.MetaType;
import com.slytechs.jnet.protocol.tcpipREFACTOR.constants.CoreId;
import com.slytechs.jnet.protocol.api.meta.MetaResource;

/**
 * Frames are small parts of a message in the network.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
@MetaResource("frame-meta.json") // in src/main/resource
public final class Frame extends Header {

	/**
	 * A supplier of frame numbers.
	 */
	public interface FrameNumber {

		/**
		 * New frame number supplier starting at 0 frame number.
		 *
		 * @return the frame number supplier
		 */
		static FrameNumber of() {
			return starting(1);
		}

		/**
		 * New frame number supplier starting at specified starting frame number.
		 *
		 * @param startingNo the starting frame number
		 * @return the frame number supplier
		 */
		static FrameNumber starting(long startingNo) {
			return starting(startingNo, () -> 1);
		}

		/**
		 * New frame number supplier starting at specified starting frame number with
		 * ability to handle frame number roll over for a 63-bit counter.
		 *
		 * @param startingNo the starting frame number
		 * @param onRollOver an action which produces new starting frame number and
		 *                   handles frame counter roll overs
		 * @return the frame number supplier
		 */
		static FrameNumber starting(long startingNo, LongSupplier onRollOver) {
			return new FrameNumber() {

				long frameNo = startingNo;

				@Override
				public long getAndIncrement() {
					if (frameNo == Long.MAX_VALUE)
						return frameNo = onRollOver.getAsLong();

					return frameNo++;
				}

			};
		}

		/**
		 * Gets the current frame number and increments to the next one.
		 *
		 * @return a frame number
		 */
		long getAndIncrement();

		/**
		 * Computes a frame counter using a combination of timestamp and a port number.
		 * Basic implementations ignore both these parameters and simply increment frame
		 * numbers from a starting frame number.
		 *
		 * @param timestamp the timestamp of the packet
		 * @param portNo    the port no the packet was received on
		 * @return a frame number
		 */
		default long getUsing(long timestamp, int portNo) {
			return getAndIncrement();
		}
	}

	/** The Constant ID. */
	public static final int ID = CoreId.CORE_ID_FRAME;

	/** The descriptor. */
	private PacketDescriptor descriptor;

	/**
	 * Instantiates a new frame.
	 */
	public Frame() {
		super(ID);
	}

	/**
	 * Bind descriptor.
	 *
	 * @param descriptor the descriptor
	 */
	public void bindDescriptor(PacketDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Number of packet bytes captured.
	 *
	 * @return number of bytes
	 */
	@Meta
	public int captureLength() {
		return descriptor.captureLength();
	}

	/**
	 * Capture length in bits.
	 *
	 * @return the capture length in unit of bits
	 */
	@Meta(value = MetaType.ATTRIBUTE)
	public int captureLengthBits() {
		return captureLength() * 8;
	}

	/**
	 * Copies the frame contents to a new array.
	 *
	 * @return array containing copy of the frame contents
	 */
	public byte[] data() {
		byte[] array = new byte[headerLength()];

		data(array);

		return array;
	}

	/**
	 * Copies the frame contents to the supplied array.
	 *
	 * @param dst the destination array where to copy the contents
	 * @return number of bytes copied into the array
	 */
	public int data(byte[] dst) {
		return data(dst, 0, dst.length);
	}

	/**
	 * Copies the frame contents to the supplied array.
	 *
	 * @param dst    the destination array where to copy the contents
	 * @param offset the offset into the destination array of the start of the copy
	 * @param length number of bytes to copy from frame to dst array
	 * @return number of bytes copied into the array
	 */
	public int data(byte[] dst, int offset, int length) {
		if (offset + length > headerLength())
			length = headerLength() - offset;

		buffer().get(dst, offset, length);

		return length;
	}

	/**
	 * FrameHeader number assigned to this packet frame at the time of the packet dispatch
	 * or capture.
	 *
	 * @return frame number
	 */
	@Meta
	public long frameNo() {
		return descriptor.frameNo();
	}

	/**
	 * Port no.
	 *
	 * @return the int
	 */
	@Meta
	public int portNo() {
		return descriptor.portNo();
	}

	/**
	 * Port name.
	 *
	 * @return the string
	 */
	@Meta(MetaType.ATTRIBUTE)
	public String portName() {
		return descriptor.portName();
	}

	/**
	 * On unbind.
	 *
	 * @see com.slytechs.jnet.protocol.api.common.Header#onUnbind()
	 */
	@Override
	protected void onUnbind() {
		descriptor = null;
	}

	/**
	 * The timestamp when this packet was captured.
	 *
	 * @return 64-bit timestamp in capture ports encoding and timestamp units
	 */
	@Meta(MetaType.ATTRIBUTE)
	public long timestampAsLong() {
		return descriptor.timestamp();
	}

	/**
	 * Timestamp unit.
	 *
	 * @return the timestamp unit
	 */
	@Meta(MetaType.ATTRIBUTE)
	public TimestampUnit timestampUnit() {
		return descriptor.timestampUnit();
	}

	/**
	 * The timestamp when this packet was captured.
	 *
	 * @return a {@code Timestamp} object for current timestamp and timestamp-unit
	 */
	@Meta
	public Timestamp timestamp() {
		return new Timestamp(timestampAsLong(), timestampUnit());
	}

	/**
	 * Packet length as seen on the wire before any truncation due to snaplen.
	 *
	 * @return number of bytes
	 */
	@Meta
	public int wireLength() {
		return descriptor.wireLength();
	}

	/**
	 * Wire length in bits.
	 *
	 * @return the wire length in units of bits
	 */
	@Meta(value = MetaType.ATTRIBUTE)
	public int wireLengthBits() {
		return wireLength() * 8;
	}
}
