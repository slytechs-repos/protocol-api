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
package com.slytechs.jnet.jnetruntime.util;

import java.math.BigInteger;

import com.slytechs.jnet.jnetruntime.util.UnitUtils.ConvertableUnit;

/**
 * Memory size units. Storage units are always based on 1024 byte multiple.
 * 
 * <p>
 * Note that a 64-bit long overflows after {@link #TERABYTES} bytes, thus other
 * higher units of PETABYTES, ZETA are not defined. Future extensions will have
 * to convert to {@link BigInteger} as storage, especially for the larger units.
 */
public enum MemoryUnit implements ConvertableUnit<MemoryUnit>, Unit {

	/** The bits. */
	BITS("bit") {
		@Override
		public long convert(long size, MemoryUnit sourceUnit) {
			return sourceUnit.toBits(size);
		}

		@Override
		public long toBits(long size) {
			return size;
		}

		@Override
		public long toBytes(long size) {
			return size / 8;
		}
	},

	/** The bytes. */
	BYTES("b", "byte"),

	/**
	 * A Kilo byte of 1024 bytes. Storage units are always based on 1024 byte
	 * multiple.
	 */
	KILOBYTES("kb", "kbytes", "kilo"),

	/**
	 * A Mega byte or 1024 * Kilo bytes or 1,048,576 bytes. Storage units are always
	 * based on 1024 byte multiple.
	 */
	MEGABYTES("mb", "mbytes", "mega"),

	/**
	 * A Giga byte or 1024 * Mega bytes or 1,073,741,824 bytes. Storage units are
	 * always based on 1024 byte multiple.
	 */
	GIGABYTES("gb", "gbytes", "gig", "giga"),

	/**
	 * A Tera byte or 1024 * Giga bytes or 1,099,511,627,776 bytes. Storage units
	 * are always based on 1024 byte multiple.
	 */
	TERABYTES("tb", "tbytes", "tera"),

	/**
	 * A Peta byte or 1024 * Tera bytes or 1,099,511,627,776 bytes. Storage units
	 * are always based on 1024 byte multiple.
	 */
	PETABYTES("pb", "pbytes", "peta"),

	/**
	 * A Exa byte or 1024 * Peta bytes or 1,099,511,627,776 bytes. Storage units are
	 * always based on 1024 byte multiple.
	 */
	EXABYTES("eb", "ebytes", "exa"),

	;

	/** The base. */
	private final long base;

	/** The basef. */
	private final double basef;

	/** The symbol. */
	private final String[] symbols;

	/**
	 * Instantiates a new memory unit.
	 */
	MemoryUnit(String... symbols) {
		final int ordinal = ordinal() - 1;
		long t = 1;

		for (int i = 0; i < ordinal; i++) {
			t *= 1024;
		}

		this.base = t;
		this.basef = t;
		if (symbols == null)
			this.symbols = new String[] { "" + name().charAt(0) };
		else
			this.symbols = symbols;
	}

	/**
	 * Convertf.
	 *
	 * @param inBytes the in bytes
	 * @return the double
	 */
	@Override
	public double convertf(double inBytes) {
		return convertf(inBytes, MemoryUnit.BYTES);
	}

	/**
	 * Convertf.
	 *
	 * @param size       the size
	 * @param sourceUnit the source unit
	 * @return the double
	 */
	@Override
	public double convertf(double size, MemoryUnit sourceUnit) {
		return sourceUnit.toBytesAsDouble(size) / this.base;
	}

	/**
	 * Convert.
	 *
	 * @param size       the size
	 * @param sourceUnit the source unit
	 * @return the long
	 */
	@Override
	public long convert(long size, MemoryUnit sourceUnit) {
		return sourceUnit.toBytes(size) / this.base;
	}

	/**
	 * To bits.
	 *
	 * @param size the size
	 * @return the long
	 */
	public long toBits(long size) {
		return (toBytes(size) * 8);
	}

	/**
	 * To int bits.
	 *
	 * @param size the size
	 * @return the int
	 */
	public int toBitsAsInt(long size) {
		long bits = (toBytes(size) * 8);

		if (bits > Integer.MAX_VALUE)
			throw new IllegalArgumentException("integer overflow on conversion from long to int");

		return (int) bits;
	}

	/**
	 * To bytes.
	 *
	 * @param size the size
	 * @return the long
	 */
	public long toBytes(long size) {
		return (size * base);
	}

	/**
	 * To int bytes.
	 *
	 * @param size the size
	 * @return the int
	 */
	public int toBytesAsInt(long size) {
		if (size > Integer.MAX_VALUE)
			throw new IllegalArgumentException("integer overflow on conversion from long to int");

		return (int) (size * base);
	}

	/**
	 * To bytesf.
	 *
	 * @param size the size
	 * @return the double
	 */
	private double toBytesAsDouble(double size) {
		return size * basef;
	}

	/**
	 * To kilo.
	 *
	 * @param size the size
	 * @return the long
	 */
	public long toKilo(long size) {
		return KILOBYTES.convert(size, this);
	}

	/**
	 * To mega.
	 *
	 * @param size the size
	 * @return the long
	 */
	public long toMegabytes(long size) {
		return MEGABYTES.convert(size, this);
	}

	/**
	 * To giga.
	 *
	 * @param size the size
	 * @return the long
	 */
	public long toGigabytes(long size) {
		return GIGABYTES.convert(size, this);
	}

	/**
	 * To tera.
	 *
	 * @param size the size
	 * @return the long
	 */
	public long toTerabytes(long size) {
		return TERABYTES.convert(size, this);
	}

	/**
	 * Nearest.
	 *
	 * @param inBytes the in bytes
	 * @return the memory unit
	 */
	public static MemoryUnit nearest(long inBytes) {
		return UnitUtils.nearest(inBytes, MemoryUnit.class, BYTES);
	}

	/**
	 * Format.
	 *
	 * @param fmt     the fmt
	 * @param inBytes the in bytes
	 * @return the string
	 */
	public static String format(String fmt, long inBytes) {
		return UnitUtils.format(fmt, inBytes, MemoryUnit.class, BYTES);
	}

	/**
	 * Gets the symbol.
	 *
	 * @return the symbol
	 */
	@Override
	public String getSymbol() {
		return symbols.length == 0 ? name() : symbols[0];
	}

	/**
	 * @see com.slytechs.jnet.jnetruntime.util.Unit#toBase(long)
	 */
	@Override
	public long toBase(long value) {
		return toBytes(value);
	}

	/**
	 * @see com.slytechs.jnet.jnetruntime.util.Unit#getSymbols()
	 */
	@Override
	public String[] getSymbols() {
		return symbols;
	}

}