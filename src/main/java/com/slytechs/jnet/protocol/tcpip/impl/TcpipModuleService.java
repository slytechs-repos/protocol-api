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
package com.slytechs.jnet.protocol.tcpip.impl;

import java.util.List;

import com.slytechs.jnet.protocol.api.pack.ProtocolModule;
import com.slytechs.jnet.protocol.api.pack.ProtocolModuleService;
import com.slytechs.jnet.protocol.api.pack.ProtocolPackTable;
import com.slytechs.jnet.protocol.api.pack.impl.PlaceholderProtocolModule;
import com.slytechs.jnet.protocol.tcpip.BuiltinProtocolModule;
import com.slytechs.jnet.protocol.tcpip.TcpipProtocolModule;

/**
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class TcpipModuleService implements ProtocolModuleService {

	private static final List<ProtocolModule> MODULES = List.of(

			new BuiltinProtocolModule(),
			new TcpipProtocolModule(),
			new PlaceholderProtocolModule(ProtocolPackTable.AAA),
			new PlaceholderProtocolModule(ProtocolPackTable.TELCO)

	);

	public TcpipModuleService() {
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.pack.ProtocolModuleService#listModules()
	 */
	@Override
	public List<ProtocolModule> listModules() {
		return MODULES;
	}

}
