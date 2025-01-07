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

import java.util.function.Function;

/**
 * Represents a constant value (number or string) in an expression tree.
 */
final class ConstantNode extends ExprNode {

	private final ExprValue value;

	/**
	 * Creates a numeric constant.
	 */
	ConstantNode(int value, int sourcePosition) {
		super(sourcePosition);
		this.value = ExprValue.number(value);
	}

	/**
	 * Creates a string constant.
	 */
	ConstantNode(String value, int sourcePosition) {
		super(sourcePosition);
		this.value = ExprValue.string(value);
	}

	/**
	 * Static factory method for zero constant.
	 */
	static ConstantNode zero(int sourcePosition) {
		return new ConstantNode(0, sourcePosition);
	}

	@Override
	ExprValue evaluate(Function<String, Number> varResolver) {
		return value;
	}

	@Override
	public String toString() {
		if (value.isString()) {
			return "\"" + value.asString() + "\"";
		}
		return value.toString();
	}
}