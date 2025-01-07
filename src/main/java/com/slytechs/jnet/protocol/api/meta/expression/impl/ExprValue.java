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
package com.slytechs.jnet.protocol.api.meta.expression.impl;

/**
 * Represents a value that can be either a number or a string.
 */
public final class ExprValue {
	private final Object value;

	private ExprValue(Object value) {
		this.value = value;
	}

	/**
	 * Creates a numeric value.
	 */
	public static ExprValue number(int value) {
		return new ExprValue(value);
	}

	/**
	 * Creates a string value.
	 */
	public static ExprValue string(String value) {
		return new ExprValue(value);
	}

	/**
	 * Gets the value as an integer.
	 */
	public int asInt() {
		if (value instanceof Number n) {
			return n.intValue();
		}
		throw new ExpressionException("Value is not a number: " + value);
	}

	/**
	 * Gets the value as a string.
	 */
	public String asString() {
		return value.toString();
	}

	/**
	 * Checks if the value is numeric.
	 */
	public boolean isNumeric() {
		return value instanceof Number;
	}

	/**
	 * Checks if the value is a string.
	 */
	public boolean isString() {
		return value instanceof String;
	}

	public Object get() {
		return value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}