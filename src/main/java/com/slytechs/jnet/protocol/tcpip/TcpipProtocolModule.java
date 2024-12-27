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
package com.slytechs.jnet.protocol.tcpip;

import com.slytechs.jnet.platform.api.util.Version;
import com.slytechs.jnet.protocol.api.pack.Pack;
import com.slytechs.jnet.protocol.api.pack.PackId;
import com.slytechs.jnet.protocol.api.pack.ProtocolModule;
import com.slytechs.jnet.protocol.api.pack.ProtocolPackTable;

/**
 * 
 *
 * @author Mark Bednarczyk [mark@slytechs.com]
 * @author Sly Technologies Inc.
 */
public class TcpipProtocolModule implements ProtocolModule {

	public static final String VERSION = "0.1.0";
	public static final String NAME = "tcpip";

	private final String name;
	private final PackId packId;
	private final Version version;
	private boolean isLoaded;

	public TcpipProtocolModule() {
		this(NAME, ProtocolPackTable.TCPIP, new Version(VERSION));
	}

	private TcpipProtocolModule(String name, PackId packId, Version version) {
		Version.minimalCheck(name, VERSION, version.toString());

		this.name = name;
		this.packId = packId;
		this.version = version;

	}

	/**
	 * @see com.slytechs.jnet.protocol.api.pack.ProtocolModule#name()
	 */
	@Override
	public String name() {
		return name;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.pack.ProtocolModule#version()
	 */
	@Override
	public Version version() {
		return version;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.pack.ProtocolModule#packId()
	 */
	@Override
	public PackId packId() {
		return packId;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.pack.ProtocolModule#isLoaded()
	 */
	@Override
	public boolean isLoaded() {
		return isLoaded;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.pack.ProtocolModule#isAvailable()
	 */
	@Override
	public boolean isAvailable() {
		return true;
	}

	/**
	 * @param isLoaded the isLoaded to set
	 */
	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.pack.ProtocolModule#loadPack()
	 */
	@Override
	public Pack<?> loadPack() {
		return CorePackDefinition.singleton();
	}

}
