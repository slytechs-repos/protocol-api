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
package com.slytechs.jnet.protocol.api.pack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.slytechs.jnet.platform.api.util.Reflections;
import com.slytechs.jnet.protocol.api.common.Header;
import com.slytechs.jnet.protocol.api.common.HeaderInfo;
import com.slytechs.jnet.protocol.api.common.HeaderOptionInfo;
import com.slytechs.jnet.protocol.api.common.Other;
import com.slytechs.jnet.protocol.api.descriptor.PacketDissectorExtension;
import com.slytechs.jnet.protocol.api.descriptor.PacketDissectorExtension.DissectorExtensionFactory;
import com.slytechs.jnet.protocol.tcpip.constants.PacketDescriptorType;

/**
 * A protocol pack information and instance.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 * @param <E> the element type
 */
public abstract class Pack<E extends Enum<? extends HeaderInfo>> {

	/**
	 * An empty protocol pack used as place holder.
	 */
	private enum EmptyPack implements HeaderInfo {
		;

		/**
		 * Id.
		 *
		 * @return the int
		 * @see com.slytechs.jnet.protocol.api.common.HeaderInfo#id()
		 */
		@Override
		public int id() {
			throw new UnsupportedOperationException("not implemented yet");
		}

		/**
		 * New header instance.
		 *
		 * @return the header
		 * @see com.slytechs.jnet.protocol.api.common.HeaderSupplier#newHeaderInstance()
		 */
		@Override
		public Header newHeaderInstance() {
			return new Other();
		}

	}

	/**
	 * A placeholder protocol pack used to represent all of the defined packs, which
	 * are not yet loaded. When a real pack is actually loaded, the PackNotLoaded
	 * stub is replaced with real protocol pack information.
	 */
	private static class PackNotLoaded extends Pack<EmptyPack> {

		/**
		 * Instantiates a new pack not loaded.
		 *
		 * @param id the id
		 */
		protected PackNotLoaded(ProtocolPackTable id) {
			super(id, id.getPackName(), new EmptyPack[0]);
		}

		/**
		 * Checks if is detectable.
		 *
		 * @return true, if is detectable
		 * @see com.slytechs.jnet.protocol.api.pack.Pack#isDetectable()
		 */
		@Override
		protected boolean isDetectable() {
			return Pack.isPackDetected(getProtocolPackId());
		}

		/**
		 * Checks if is pack loaded.
		 *
		 * @return true, if is pack loaded
		 * @see com.slytechs.jnet.protocol.api.pack.Pack#isPackLoaded()
		 */
		@Override
		public boolean isPackLoaded() {
			return false;
		}

		/**
		 * Gets the header.
		 *
		 * @param id the id
		 * @return the header
		 * @see com.slytechs.jnet.protocol.api.pack.Pack#findHeader(int)
		 */
		@Override
		public Optional<HeaderInfo> findHeader(int id) {
			throw new UnsupportedOperationException("pack is not loaded");
		}

	}

	/**
	 * A specialized pack used to represent all of the protocol header extensions
	 * and options.
	 */
	private static class PackVariableOptions extends Pack<EmptyPack> {

		/**
		 * Instantiates a new pack variable options.
		 */
		protected PackVariableOptions() {
			super(ProtocolPackTable.OPTS, ProtocolPackTable.OPTS.getPackName(), new EmptyPack[0]);
		}

		/**
		 * Checks if is detectable.
		 *
		 * @return true, if is detectable
		 * @see com.slytechs.jnet.protocol.api.pack.Pack#isDetectable()
		 */
		@Override
		protected boolean isDetectable() {
			return true;
		}

		/**
		 * Checks if is pack loaded.
		 *
		 * @return true, if is pack loaded
		 * @see com.slytechs.jnet.protocol.api.pack.Pack#isPackLoaded()
		 */
		@Override
		public boolean isPackLoaded() {
			return true;
		}

		/**
		 * Gets the header.
		 *
		 * @param id the id
		 * @return the header
		 * @see com.slytechs.jnet.protocol.api.pack.Pack#findHeader(int)
		 */
		@Override
		public Optional<HeaderInfo> findHeader(int id) {
			throw new UnsupportedOperationException("not implemented yet");
		}

		/**
		 * @see com.slytechs.jnet.protocol.api.pack.Pack#isOptions()
		 */
		@Override
		public boolean isOptions() {
			return true;
		}
	}

	/** The Constant packTable. */
	private static final Pack<?>[] packTable = new Pack[ProtocolPackTable.values().length];

	static {
		ProtocolPackTable[] values = ProtocolPackTable.values();

		for (int i = 0; i < values.length; i++)
			packTable[i] = new PackNotLoaded(values[i]);

		if (!loadPack(ProtocolPackTable.CORE))
			throw new IllegalStateException("unable to load core protocol pack");

		packTable[ProtocolPackTable.OPTS.ordinal()] = new PackVariableOptions();
	}

	/**
	 * Finds information for a protocol header with a given id within this pack.
	 *
	 * @param id the header id
	 * @return optional header information
	 */
	public static Optional<? extends HeaderInfo> lookupHeader(int id) {
		int packId = PackId.decodePackId(id);
		Pack<?> pack = getLoadedPack(packId);
		if (pack == null)
			return Optional.empty();

		if (pack.isOptions())
			throw new IllegalStateException("id[0x%X] is of unknown OPTIONAL pack"
					.formatted(id));

		return pack.findHeader(id);
	}

	/**
	 * Finds information for a protocol header extension of a parent header within
	 * this pack.
	 *
	 * @param id          parent header id
	 * @param extensionId the header extension id
	 * @return optional header information
	 */
	public static Optional<? extends HeaderInfo> findExtension(int id, int extensionId) {
		Optional<? extends HeaderInfo> parent = lookupHeader(id);
		if (parent.isEmpty())
			return Optional.empty();

		var extInfos = parent.get().getOptionInfos();
		int extOrdinal = PackId.decodeIdOrdinal(extensionId);

		return Optional.of(extInfos[extOrdinal]);
	}

	/**
	 * To string.
	 *
	 * @param id the id
	 * @return the string
	 */
	public static String toString(int id) {
		return lookupHeader(id)
				.map(HeaderInfo::abbr)
				.orElse("N/A(%d)".formatted(id));
	}

	/**
	 * To string.
	 *
	 * @param id    the id
	 * @param extId the ext id
	 * @return the string
	 */
	public static String toString(int id, int extId) {

		var parent = lookupHeader(id)
				.map(HeaderInfo::abbr)
				.orElse("N/A(%d)".formatted(id));

		var ext = findExtension(id, extId)
				.map(HeaderInfo::abbr)
				.orElse("N/A(%d.%d)".formatted(id, extId));

		return "%s+%s".formatted(parent, ext);
	}

	/**
	 * Gets information for a protocol header extension of a parent header within
	 * this pack.
	 *
	 * @param id the header id
	 * @return the header information, does not return null
	 */
	public abstract Optional<HeaderInfo> findHeader(int id);

	/**
	 * Count all pack extensions.
	 *
	 * @param <E> the element type
	 * @return the int
	 */
	private static <E extends Enum<? extends HeaderInfo>> int countAllPackExtensions() {
		int count = 0;

		for (Pack<?> pack : packTable) {
			if (!pack.isPackLoaded() || (pack.protocolPackTable == ProtocolPackTable.OPTS))
				continue;

			count += countSinglePackExtensions(pack.headerIds);
		}

		return count;
	}

	/**
	 * Count all protocols.
	 *
	 * @param <E> the element type
	 * @param ids the ids
	 * @return the int
	 */
	private static <E extends Enum<? extends HeaderInfo>> int countAllProtocols(E[] ids) {
		int count = ids.length;

		for (E id : ids) {
			HeaderInfo i = (HeaderInfo) id;

			HeaderOptionInfo[] exts = i.getOptionInfos();
			if (exts.length > 0)
				count += exts.length;
		}

		return count;
	}

	/**
	 * Count single pack extensions.
	 *
	 * @param <E> the element type
	 * @param ids the ids
	 * @return the int
	 */
	private static <E extends Enum<? extends HeaderInfo>> int countSinglePackExtensions(E[] ids) {
		int count = 0;

		for (E id : ids) {
			HeaderInfo i = (HeaderInfo) id;

			HeaderOptionInfo[] exts = i.getOptionInfos();
			if (exts.length > 0)
				count += exts.length;
		}

		return count;
	}

	/**
	 * Gets all of the detected packs.
	 *
	 * @param <T_PACK> the generic type
	 * @param packId   the pack id
	 * @return the detected pack
	 */
	@SuppressWarnings("unchecked")
	public static <T_PACK extends Pack<?>> T_PACK getDetectedPack(int packId) {
		int packOrdinal = PackId.decodePackOrdinal(packId);

		Pack<?> pack = packTable[packOrdinal];
		if (!pack.isDetectable())
			return null;

		return (T_PACK) pack;
	}

	/**
	 * Gets all of the loaded packs.
	 *
	 * @param <T_PACK> the generic type
	 * @param packId   the pack id
	 * @return the loaded pack
	 */
	@SuppressWarnings("unchecked")
	public static <T_PACK extends Pack<?>> T_PACK getLoadedPack(int packId) {
		int packOrdinal = PackId.decodePackOrdinal(packId);

		Pack<?> pack = packTable[packOrdinal];
		if (!pack.isPackLoaded())
			return null;

		return (T_PACK) pack;
	}

	/**
	 * Gets the detected pack.
	 *
	 * @param <T_PACK> the generic type
	 * @param id       the id
	 * @return the detected pack
	 */
	public static <T_PACK extends Pack<?>> T_PACK getDetectedPack(ProtocolPackTable id) {
		return getDetectedPack(id.getPackIdAsInt());
	}

	/**
	 * Checks if is pack detected.
	 *
	 * @param protocolPackTable the pack info
	 * @return true, if is pack detected
	 */
	public static boolean isPackDetected(ProtocolPackTable protocolPackTable) {
		try {
			String moduleName = protocolPackTable.getPackModuleName();
			String className = protocolPackTable.getPackClassName();

			if (className == null)
				return false;

			Optional<Module> module = Optional.empty();
			if (moduleName != null)
				module = ModuleLayer.boot()
						.findModule(moduleName);

			if (module.isPresent())
				return (Class.forName(module.get(), className) != null);
			else
				return (Class.forName(className, false, Pack.class.getClassLoader()) != null);

		} catch (SecurityException | ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * List all declared packs.
	 *
	 * @return the list
	 */
	public static List<Pack<?>> listAllDeclaredPacks() {
		return Arrays.asList(packTable);
	}

	/**
	 * List all detectable packs.
	 *
	 * @return the list
	 */
	public static List<Pack<?>> listAllDetectablePacks() {
		return Arrays.asList(packTable)
				.stream()
				.filter(Pack::isDetectable)
				.collect(Collectors.toList());
	}

	/**
	 * List all loaded packs.
	 *
	 * @return the list
	 */
	public static List<Pack<?>> listAllLoadedPacks() {
		return Arrays.asList(packTable)
				.stream()
				.filter(Pack::isPackLoaded)
				.collect(Collectors.toList());
	}

	/**
	 * Wrap all extensions.
	 *
	 * @param type the type
	 * @param core the core
	 * @return the dissector extension
	 */
	public static PacketDissectorExtension wrapAllExtensions(PacketDescriptorType type, PacketDissectorExtension core) {

		final List<PacketDissectorExtension> list = Pack.listAllLoadedPacks().stream()
				.filter(Pack::isNotCore)
				.map(Pack::extensionFactory)
				.filter(Objects::nonNull)
				.map(f -> f.newInstance(type))
				.collect(Collectors.toList());

		final var mutable = new ArrayList<PacketDissectorExtension>();
		mutable.add(core);
		mutable.addAll(list);

		return PacketDissectorExtension.wrap(mutable);
	}

	/**
	 * Load pack.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
	public static synchronized boolean loadPack(int id) {
		ProtocolPackTable protocolPackTable = ProtocolPackTable.values()[PackId.decodePackOrdinal(id)];

		return loadPack(protocolPackTable);
	}

	/**
	 * Load pack.
	 *
	 * @param protocolPackTable the pack info
	 * @return true, if successful
	 */
	public static synchronized boolean loadPack(ProtocolPackTable protocolPackTable) {
		if (packTable[protocolPackTable.ordinal()].isPackLoaded())
			return true;

		String moduleName = protocolPackTable.getPackModuleName();
		String className = protocolPackTable.getPackClassName();

		Pack<?> pack = loadPackClass(moduleName, className);
		if (pack == null)
			return false;

		packTable[protocolPackTable.ordinal()] = pack;

		return true;
	}

	/**
	 * Load all detected packs.
	 *
	 * @return true, if successful
	 */
	public static synchronized boolean loadAllDetectedPacks() {
		listAllDeclaredPacks().forEach(p -> loadPack(p.protocolPackTable));

		return true;
	}

	/**
	 * Load pack class.
	 *
	 * @param moduleName the module name
	 * @param className  the class name
	 * @return the pack
	 */
	private static Pack<?> loadPackClass(String moduleName, String className) {
		try {

			if (className == null)
				return null;

			Optional<Module> module = Optional.empty();
			if (moduleName != null)
				module = ModuleLayer.boot()
						.findModule(moduleName);

			Class<Pack<?>> clazz = Reflections.loadClass(moduleName, className);

			Method singletonMethod = clazz.getDeclaredMethod("singleton");

			Pack<?> pack = (Pack<?>) singletonMethod.invoke(null);

			return pack;

		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Unload pack.
	 *
	 * @param protocolPackTable the pack info
	 * @return true, if successful
	 */
	public static synchronized boolean unloadPack(ProtocolPackTable protocolPackTable) {

		if (!packTable[protocolPackTable.ordinal()].isPackLoaded())
			return false;

		packTable[protocolPackTable.ordinal()] = new PackNotLoaded(protocolPackTable);

		return true;
	}

	/** The pack info. */
	private final ProtocolPackTable protocolPackTable;

	/** The name. */
	private final String name;

	/** The int id. */
	private final int intId;

	/** The protocol ids. */
	private final E[] headerIds;

	/** The module. */
	private Module module;

	/**
	 * Instantiates a new pack.
	 *
	 * @param packs     the id
	 * @param headerIds the protocol ids
	 */
	protected Pack(ProtocolPackTable packs, E[] headerIds) {
		this.protocolPackTable = packs;
		this.name = packs.getPackName();
		this.headerIds = Objects.requireNonNull(headerIds, "headerIds");
		this.intId = packs.getPackIdAsInt();
		this.module = ModuleLayer.boot()
				.findModule(packs.getPackModuleName())
				.orElse(null);
	}

	/**
	 * Instantiates a new pack.
	 *
	 * @param packs     the id
	 * @param name      the name
	 * @param headerIds the protocol ids
	 */
	protected Pack(ProtocolPackTable packs, String name, E[] headerIds) {
		this.protocolPackTable = packs;
		this.name = name;
		this.headerIds = Objects.requireNonNull(headerIds, "headerIds");
		this.intId = packs.getPackIdAsInt();
	}

	/**
	 * Gets the pack id.
	 *
	 * @return the pack id
	 */
	public ProtocolPackTable getProtocolPackId() {
		return protocolPackTable;
	}

	/**
	 * Gets the pack class name.
	 *
	 * @return the pack class name
	 */
	public String getPackClassName() {
		return protocolPackTable.getPackClassName();
	}

	/**
	 * Gets the pack module name.
	 *
	 * @return the pack module name
	 */
	public String getPackModuleName() {
		return protocolPackTable.getPackModuleName();
	}

	/**
	 * Gets the module.
	 *
	 * @return the module
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * Gets the pack id as int.
	 *
	 * @return the pack id as int
	 */
	public int getPackIdAsInt() {
		return intId;
	}

	/**
	 * Gets the pack name.
	 *
	 * @return the pack name
	 */
	public String getPackName() {
		return name;
	}

	/**
	 * Gets the protocol ids.
	 *
	 * @return the protocol ids
	 */
	public E[] getHeaderIds() {
		if (headerIds == null)
			throw new UnsupportedOperationException();

		return headerIds;
	}

	/**
	 * Checks if is detectable.
	 *
	 * @return true, if is detectable
	 */
	protected boolean isDetectable() {
		return true;
	}

	/**
	 * Checks if is pack loaded.
	 *
	 * @return true, if is pack loaded
	 */
	public boolean isPackLoaded() {
		return true;
	}

	/**
	 * To array.
	 *
	 * @return the header info[]
	 */
	public HeaderInfo[] toArray() {
		return (HeaderInfo[]) headerIds;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		int id = getPackIdAsInt();
		int ordinal = PackId.decodePackOrdinal(id);

		boolean isOptions = (protocolPackTable == ProtocolPackTable.OPTS);
		boolean isDetect = isDetectable();
		boolean isLoaded = isPackLoaded();

		String status;
		if (isOptions) {
			int count = countAllPackExtensions();
			status = "loaded=%2s definitions".formatted(count);

		} else if (isLoaded) {
			int count = countAllProtocols(headerIds);
			status = "loaded=%2s definitions".formatted(count);

		} else {
			status = isDetect
					? "<pack not loaded but detected>"
					: "<pack not loaded>";
		}

		return "Pack ["
				+ "name=%-16s".formatted("\"" + name + "\"")
				+ " (%2d/0x%03X)".formatted(ordinal, id)
				+ ", " + status
				+ "]";
	}

	/**
	 * Extension factory.
	 *
	 * @return the dissector extension factory
	 */
	protected DissectorExtensionFactory extensionFactory() {
		return null;
	}

	/**
	 * Checks if is not core.
	 *
	 * @return true, if is not core
	 */
	private boolean isNotCore() {
		return !isCore();
	}

	/**
	 * Checks if is core.
	 *
	 * @return true, if is core
	 */
	public boolean isCore() {
		return false;
	}

	/**
	 * Checks if is options.
	 *
	 * @return true, if is options
	 */
	public boolean isOptions() {
		return false;
	}

}