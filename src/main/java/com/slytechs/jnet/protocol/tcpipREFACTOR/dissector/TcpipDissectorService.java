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
package com.slytechs.jnet.protocol.tcpipREFACTOR.dissector;

import java.util.List;

import com.slytechs.jnet.protocol.api.core.PacketDescriptorType;
import com.slytechs.jnet.protocol.api.descriptor.Dissector;
import com.slytechs.jnet.protocol.api.descriptor.impl.PacketDissectorNative;
import com.slytechs.jnet.protocol.api.descriptor.spi.DissectorService;
import com.slytechs.jnet.protocol.tcpipREFACTOR.ip.reassembly.IpfFragDissector;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class TcpipDissectorService implements DissectorService {

	/**
	 * 
	 */
	public TcpipDissectorService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.descriptor.spi.DissectorService#listDissector()
	 */
	@Override
	public List<Dissector> listDissectors() {

		List<Dissector> DISSECTORS = List.of(

				new Type1DissectorJavaImpl(),
				new Type2DissectorJavaImpl(),
				new PacketDissectorNative(PacketDescriptorType.TYPE1),
				new PacketDissectorNative(PacketDescriptorType.TYPE2),
				new IpfFragDissector()

		);

		return DISSECTORS;
	}

}
