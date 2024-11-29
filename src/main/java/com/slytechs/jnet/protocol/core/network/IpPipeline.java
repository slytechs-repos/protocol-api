/*
 * Sly Technologies Free License
 * 
 * Copyright 2024 Sly Technologies Inc.
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
package com.slytechs.jnet.protocol.core.network;

import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;

import com.slytechs.jnet.jnetruntime.pipeline.AbstractPipeline;
import com.slytechs.jnet.jnetruntime.pipeline.DataType;
import com.slytechs.jnet.protocol.core.network.IpPipeline.StatefulIpf;
import com.slytechs.jnet.protocol.descriptor.IpfFragment;

/**
 * @author Mark Bednarczyk
 *
 */
public class IpPipeline extends AbstractPipeline<StatefulIpf, IpPipeline> {

	/**
	 * @param name
	 * @param dataType
	 */
	public IpPipeline(String name, DataType dataType) {
		super(name, dataType);
		// TODO Auto-generated constructor stub
	}

	public interface StatefulIpf {
		void handleIpf(
				MemorySegment packetSegment, // Can be null
				ByteBuffer packetBuffer, // Captured packet data or wrapped around packetSegment
				long timestamp, int caplen, int wirelen,
				IpfFragment ipfDescriptor);
	}
}
