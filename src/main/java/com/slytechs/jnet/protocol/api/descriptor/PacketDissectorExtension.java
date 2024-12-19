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

import java.nio.ByteBuffer;
import java.util.List;

import com.slytechs.jnet.protocol.api.descriptor.PacketDissector.RecordRecorder;
import com.slytechs.jnet.protocol.tcpip.constants.PacketDescriptorType;

/**
 * Dissector extension point for protocol packs.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public interface PacketDissectorExtension {

	/**
	 * A factory for creating DissectorExtension objects.
	 */
	interface DissectorExtensionFactory {
		
		/**
		 * New instance.
		 *
		 * @param type the type
		 * @return the dissector extension
		 */
		PacketDissectorExtension newInstance(PacketDescriptorType type);
	}

	/** The empty. */
	static PacketDissectorExtension EMPTY = new PacketDissectorExtension() {

		@Override
		public void setRecorder(RecordRecorder recorder) {
		}

		@Override
		public boolean dissectEncaps(ByteBuffer buffer, int offset, int encapsId, int encapsOffset, int encapsLength) {
			return false;
		}

		@Override
		public boolean dissectType(ByteBuffer buffer, int offset, int encapsId, int type) {
			return false;
		}

		@Override
		public boolean dissectPorts(ByteBuffer buffer, int offset, int encapsId, int src, int dst) {
			return false;
		}

		@Override
		public void reset() {
		}

		@Override
		public void setExtensions(PacketDissectorExtension ext) {
		}
	};

	/**
	 * Wrap.
	 *
	 * @param list the list
	 * @return the dissector extension
	 */
	static PacketDissectorExtension wrap(List<PacketDissectorExtension> list) {
		return new PacketDissectorExtension() {

			@Override
			public void setRecorder(RecordRecorder recorder) {
				list.forEach(ext -> ext.setRecorder(recorder));
			}

			@Override
			public void setExtensions(PacketDissectorExtension ext) {
				list.forEach(e -> e.setExtensions(ext));
			}

			@Override
			public boolean dissectEncaps(ByteBuffer buffer, int offset, int encapsId, int encapsOffset,
					int encapsLength) {
				for (PacketDissectorExtension ext : list)
					if (ext.dissectEncaps(buffer, offset, encapsId, encapsOffset, encapsLength))
						return true;

				return false;
			}

			@Override
			public boolean dissectType(ByteBuffer buffer, int offset, int encapsId, int type) {
				for (PacketDissectorExtension ext : list)
					if (ext.dissectType(buffer, offset, encapsId, type))
						return true;

				return false;
			}

			@Override
			public boolean dissectPorts(ByteBuffer buffer, int offset, int encapsId, int src, int dst) {
				for (PacketDissectorExtension ext : list)
					if (ext.dissectPorts(buffer, offset, encapsId, src, dst))
						return true;

				return false;
			}

			@Override
			public void reset() {
			}

		};
	}

	/**
	 * Sets the recorder.
	 *
	 * @param recorder the new recorder
	 */
	void setRecorder(RecordRecorder recorder);

	/**
	 *
	 * @param ext the new extensions
	 */
	void setExtensions(PacketDissectorExtension ext);

	/**
	 * Dissect encaps.
	 *
	 * @param buffer       the buffer
	 * @param offset       the offset
	 * @param encapsId     the encaps id
	 * @param encapsOffset the encaps offset
	 * @param encapsLength the encaps length
	 * @return true, if successful
	 */
	default boolean dissectEncaps(ByteBuffer buffer, int offset, int encapsId, int encapsOffset, int encapsLength) {
		return false;
	}

	/**
	 * Dissect type.
	 *
	 * @param buffer   the buffer
	 * @param offset   the offset
	 * @param encapsId the encaps id
	 * @param type     the type
	 * @return true, if successful
	 */
	default boolean dissectType(ByteBuffer buffer, int offset, int encapsId, int type) {
		return false;
	}

	/**
	 * Dissect ports.
	 *
	 * @param buffer   the buffer
	 * @param offset   the offset
	 * @param encapsId the encaps id
	 * @param src      the src
	 * @param dst      the dst
	 * @return true, if successful
	 */
	default boolean dissectPorts(ByteBuffer buffer, int offset, int encapsId, int src, int dst) {
		return false;
	}

	/**
	 * Reset.
	 */
	void reset();
}
