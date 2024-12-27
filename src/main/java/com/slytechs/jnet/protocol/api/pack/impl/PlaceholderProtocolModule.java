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
package com.slytechs.jnet.protocol.api.pack.impl;

import java.util.Objects;

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
public class PlaceholderProtocolModule implements ProtocolModule {
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(packId);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlaceholderProtocolModule other = (PlaceholderProtocolModule) obj;
		return Objects.equals(packId, other.packId);
	}

	public static final String VERSION = "0.0.0";

	private final String name;
	private final PackId packId;
	private final Version version;

	public PlaceholderProtocolModule(String name, PackId packId) {
		this.name = name;
		this.packId = packId;
		this.version = new Version(VERSION);
	}

	public PlaceholderProtocolModule(ProtocolPackTable packId) {
		this(packId.name(), packId);
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
		return false;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.pack.ProtocolModule#isAvailable()
	 */
	@Override
	public boolean isAvailable() {
		return false;
	}

	/**
	 * @see com.slytechs.jnet.protocol.api.pack.ProtocolModule#loadPack()
	 */
	@Override
	public Pack<?> loadPack() {
		throw new UnsupportedOperationException("the protocol pack %s is not available ".formatted(name));
	}

}
