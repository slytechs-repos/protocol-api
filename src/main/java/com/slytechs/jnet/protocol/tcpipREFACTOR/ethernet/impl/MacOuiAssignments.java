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
package com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.slytechs.jnet.platform.api.common.settings.BooleanProperty;
import com.slytechs.jnet.platform.api.util.HexStrings;
import com.slytechs.jnet.platform.api.util.array.ArrayUtils;
import com.slytechs.jnet.protocol.tcpipREFACTOR.ethernet.MacAddress;

/**
 * The Class IeeeOuiAssignments.
 */
public final class MacOuiAssignments {

	/**
	 * The Class OuiEntry.
	 */
	public static class OuiEntry {

		/** The prefix. */
		private final byte[] prefix;

		/** The name. */
		private final String name;

		/** The descriptrion. */
		private final String descriptrion;

		/** The mask. */
		private final int mask;

		/**
		 * Instantiates a new oui entry.
		 *
		 * @param prefix       the prefix
		 * @param mask         the mask
		 * @param name         the name
		 * @param descriptrion the descriptrion
		 */
		public OuiEntry(byte[] prefix, int mask, String name, String descriptrion) {
			this.prefix = prefix;
			this.mask = mask;
			this.name = name;
			this.descriptrion = descriptrion;
		}

		/**
		 * Equals.
		 *
		 * @param obj the obj
		 * @return true, if successful
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof byte[] array))
				return false;

			return ArrayUtils.equals(prefix, array, mask);
		}

		/**
		 * Gets the descriptrion.
		 *
		 * @return the descriptrion
		 */
		public String getDescriptrion() {
			return descriptrion;
		}

		/**
		 * Gets the mask.
		 *
		 * @return the mask
		 */
		public int getMask() {
			return mask;
		}

		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Gets the prefix.
		 *
		 * @return the prefix
		 */
		public byte[] getPrefix() {
			return prefix;
		}

		/**
		 * Hash code.
		 *
		 * @return the int
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return ArrayUtils.hashCodeWithNetmask(prefix, 24);
		}

		/**
		 * To string.
		 *
		 * @return the string
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "OuiEntry ["
					+ "prefix=" + HexStrings.toHexString(prefix)
					+ "/" + mask
					+ ", name=" + name
					+ ", descriptrion=" + descriptrion
					+ "]";
		}

	}

	/**
	 * The Class OuiTable.
	 */
	private static class OuiTable {

		/** The Constant EUI64_BYTE_LENGTH. */
		private static final int EUI64_BYTE_LENGTH = 8;

		/** The Constant OUI_MANUFACTURER_TABLE. */
		private static final OuiTable OUI_MANUFACTURER_TABLE = new OuiTable();

		/**
		 * Parses the mac.
		 *
		 * @param mac the mac
		 * @return the byte[]
		 */
		private static byte[] parseMac(String mac) {
			int index = mac.indexOf('/');
			if (index != -1)
				mac = mac.substring(0, index);

			byte[] array = HexStrings.parseHexString(mac);
			if (array.length != EUI64_BYTE_LENGTH) // Store all macs as EUI-64 in size
				array = Arrays.copyOf(array, EUI64_BYTE_LENGTH);

			return array;
		}

		/**
		 * Parses the mask.
		 *
		 * @param mac the mac
		 * @return the int
		 */
		private static int parseMask(String mac) {
			int mask = 24; // default mask

			int index = mac.indexOf('/');
			if (index != -1)
				mask = Integer.parseInt(mac.substring(index + 1));

			return mask;
		}

		/** The map 24 bit hash. */
		private final Map<Integer, OuiEntry[]> map24BitHash = new HashMap<>();

		/** The map by oui name. */
		private final Map<String, OuiEntry> mapByOuiName = new HashMap<>();

		/** The size. */
		private int size = 0;

		/**
		 * Instantiates a new oui table.
		 */
		public OuiTable() {

		}

		/**
		 * Adds the default oui entries.
		 */
		private void addDefaultOuiEntries() {
			addEntry(new byte[] { 0x33,
					0x33,
					0x00
			}, 24, "IPv6mcast", "IPv6 multicast");;
		}

		/**
		 * Adds the entry.
		 *
		 * @param prefix      the prefix
		 * @param mask        the mask
		 * @param name        the name
		 * @param description the description
		 */
		public void addEntry(byte[] prefix, int mask, String name, String description) {
			OuiEntry entry = new OuiEntry(prefix, mask, name, description);
			int hash = entry.hashCode();

			// Get entry and resize for new element
			OuiEntry[] row = map24BitHash.getOrDefault(hash, new OuiEntry[0]);
			row = Arrays.copyOf(row, row.length + 1);
			map24BitHash.put(hash, row);
			mapByOuiName.put(name, entry);

			row[row.length - 1] = entry;
			Arrays.sort(row, (e1, e2) -> e2.mask - e1.mask); // longest bit-mask match first

			size++;
		}

		/**
		 * Clear.
		 */
		public void clear() {
			map24BitHash.clear();
			size = 0;
		}

		/**
		 * Checks if is loaded.
		 *
		 * @return true, if is loaded
		 */
		public boolean isLoaded() {
			return size > 0;
		}

		/**
		 * Load cvs table.
		 *
		 * @param cvsResourceName the cvs resource name
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		public void loadCvsTable(String cvsResourceName) throws IOException {
			InputStream in = MacOuiAssignments.class.getResourceAsStream(cvsResourceName);
			if (in == null)
				throw new FileNotFoundException("resource: " + cvsResourceName);

			readTable(in);

			addDefaultOuiEntries();
		}

		/**
		 * Lookup entry.
		 *
		 * @param mac the mac
		 * @return the optional
		 */
		public Optional<OuiEntry> lookupEntry(byte[] mac) {

			int hash = ArrayUtils.hashCodeWithNetmask(mac, 24);

			OuiEntry[] e = map24BitHash.get(hash);
			if (e == null)
				return Optional.empty();

			for (int i = 0; i < e.length; i++)
				if (e[i].equals(mac))
					return Optional.of(e[i]);

			return Optional.empty();
		}

		/**
		 * Lookup entry.
		 *
		 * @param ouiName the oui name
		 * @return the optional
		 */
		public Optional<OuiEntry> lookupEntry(String ouiName) {
			return Optional.ofNullable(mapByOuiName.getOrDefault(ouiName, null));
		}

		/**
		 * Read table.
		 *
		 * @param in the in
		 * @return the int
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		public int readTable(InputStream in) throws IOException {

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

				int errorCount = 0;
				int lineno = 0;
				String line = null;
				while ((line = reader.readLine()) != null) {
					lineno++;

					// Skip blank lines and comments
					if (line.isBlank() || line.startsWith("#"))
						continue;

					// Remove comments at the end of the line
					int indx = line.indexOf('#');
					if (indx != -1)
						line = line.substring(0, indx);

					String[] c = null;
					try {
						c = line.split("\t", 3);
						if (c.length == 3)
							addEntry(parseMac(c[0]), parseMask(c[0]), c[1], c[2]);

						else if (c.length == 2)
							addEntry(parseMac(c[0]), parseMask(c[0]), c[1], ""); // No description

						else
							throw new Error("Invalid line format");

					} catch (Throwable e) {
						if (errorCount++ < 36) // Stop reporting warnings after a few dozen
							LOG.warning("" + lineno + ": " + line + "! Invalid line format");
					}

				}

				return lineno;
			}
		}

		/**
		 * To string.
		 *
		 * @return the string
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "OuiTable ["
					+ "size=" + size
					+ " rowOverlap=" + (size - map24BitHash.size())
					+ "]";
		}
	}

	/** The Constant LOG. */
	private final static Logger LOG = Logger.getLogger(MacOuiAssignments.class.getCanonicalName());

	/** The Constant OUI_MANUFACTURE_CVS. */
	private static final String OUI_MANUFACTURE_CVS = "/ieee-oui-manufacturer-assignments.cvs";

	/** Is the manufacturer OUI TABLE lookup enabled. */
	private static final BooleanProperty isOuiLookupEnabled = new BooleanProperty("oui.lookup.enable", true);

	/**
	 * Enable manufacturer oui lookup.
	 *
	 * @param b the b
	 */
	public static void enableOuiLookup(boolean b) {
		isOuiLookupEnabled.setValue(b);

		if (!b && OuiTable.OUI_MANUFACTURER_TABLE.isLoaded())
			OuiTable.OUI_MANUFACTURER_TABLE.clear();
	}

	/**
	 * Format prefix mac with oui name.
	 *
	 * @param obj the obj
	 * @return the string
	 */
	public static String formatMacPrefixWithOuiName(Object obj) {
		if (obj instanceof String str) {
			var arr = MacAddress.parseOuiMacAddress(str);
			return MacAddress.toOuiMacAddressString(arr);
		}

		if (obj instanceof MacAddress mac)
			return MacAddress.toOuiMacAddressString(mac.toArray());

		if (obj instanceof byte[] mac)
			return MacAddress.toOuiMacAddressString(mac);

		return "";
	}

	/**
	 * Checks if is oui lookup enabled.
	 *
	 * @return true, if is oui lookup enabled
	 */
	public static boolean isOuiLookupEnabled() {
		return isOuiLookupEnabled.getBoolean();
	}

	/**
	 * Loads IEEE assignment tables. If the tables have already been loaded, this
	 * method does nothing.
	 * 
	 * <p>
	 * To enable logging on load success {@link java.util.logging.Level#CONFIG}
	 * level.
	 * </p>
	 *
	 * @throws IllegalStateException since all of the tables are internal resources
	 *                               and included with the distribution, only
	 *                               abnormal circumstances would cause the tables
	 *                               to not be loaded
	 */
	public static void loadIeeeTables() throws IllegalStateException {
		if (!isOuiLookupEnabled.getBoolean() || OuiTable.OUI_MANUFACTURER_TABLE.isLoaded())
			return;

		try {
			long ts = System.currentTimeMillis();
			OuiTable.OUI_MANUFACTURER_TABLE.loadCvsTable(OUI_MANUFACTURE_CVS);

			LOG.config("loaded IEEE OUI manufacturer cvs lookup table in "
					+ (System.currentTimeMillis() - ts)
					+ "ms, with " + OuiTable.OUI_MANUFACTURER_TABLE.size + " entries");
		} catch (IOException e) {
			LOG.warning("Unable to local IEEE oui manufacturer cvs lookup table " + OUI_MANUFACTURE_CVS);

			throw new IllegalStateException("Unable to load internal IEEE oui cvs table", e);
		}
	}

	/**
	 * Looks up the provided MAC address against internal IEEE OUI manufacturer
	 * prefix table. If a match is found, using the provided MAC address and a
	 * netmask defined in the table.
	 * 
	 * <p>
	 * Note, that if the internal tables have not been loaded yet, a table load will
	 * be triggered. Therefore the first such lookup, may take significantly longer
	 * to complete as the tables are rather large. All subsequent lookups will be
	 * performed quickly with efficiency. It is recommended, that the user call
	 * {@link MacOuiAssignments#loadIeeeTables()} at an appropriate time, before
	 * calling the lookup method.
	 * </p>
	 *
	 * @param macAddress the MAC address to find a entry for
	 * @return if found the entry will be returned, otherwise an empty will be
	 *         returned instead
	 */
	public static Optional<OuiEntry> lookupManufacturerOui(byte[] macAddress) {
		if (!isOuiLookupEnabled.getBoolean())
			return Optional.empty();

		if (!OuiTable.OUI_MANUFACTURER_TABLE.isLoaded())
			loadIeeeTables();

		return OuiTable.OUI_MANUFACTURER_TABLE.lookupEntry(macAddress);
	}

	/**
	 * Resolve mac oui description.
	 *
	 * @param obj the obj
	 * @return the string
	 */
	public static String resolveMacOuiDescription(Object obj) {
		if (obj instanceof byte[] mac) {
			Optional<OuiEntry> oui = lookupManufacturerOui(mac);

			return oui
					.map(OuiEntry::getDescriptrion)
					.orElse("");
		}

		return "";
	}

	/**
	 * Resolve mac oui name.
	 *
	 * @param obj the obj
	 * @return the string
	 */
	public static String resolveMacOuiName(Object obj) {
		if (obj instanceof byte[] mac) {
			Optional<OuiEntry> oui = lookupManufacturerOui(mac);

			return oui
					.map(OuiEntry::getName)
					.orElse("");
		}

		return "";
	}

	/**
	 * Reverse lookup oui name.
	 *
	 * @param ouiName the oui name
	 * @return the optional
	 */
	public static Optional<OuiEntry> reverseLookupOuiName(String ouiName) {
		if (!isOuiLookupEnabled.getBoolean())
			return Optional.empty();

		if (!OuiTable.OUI_MANUFACTURER_TABLE.isLoaded())
			loadIeeeTables();

		return OuiTable.OUI_MANUFACTURER_TABLE.lookupEntry(ouiName);
	}

	/**
	 * Instantiates a new ieee oui assignments.
	 */
	private MacOuiAssignments() {
	}
}
