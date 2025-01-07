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
 * Represents a constant integer value in an expression tree. This node type is
 * immutable and always evaluates to its construction-time value.
 */
final class ConstantNode extends ExprNode {

	/** The constant integer value this node represents */
	private final int value;

	/**
	 * Constructs a new constant node with a specific value and source position.
	 *
	 * @param value          the integer value this node represents
	 * @param sourcePosition the position in source text where this constant appears
	 */
	ConstantNode(int value, int sourcePosition) {
		super(sourcePosition);
		this.value = value;
	}

	/**
	 * Static factory method to create a constant node for zero. This can be used
	 * when a default value is needed.
	 *
	 * @param sourcePosition the position in source text
	 * @return a new ConstantNode representing zero
	 */
	static ConstantNode zero(int sourcePosition) {
		return new ConstantNode(0, sourcePosition);
	}

	/**
	 * Returns the constant value this node was constructed with. The variable
	 * resolver is ignored since constants don't depend on variables.
	 *
	 * @param varResolver the variable resolver (unused for constants)
	 * @return the constant integer value
	 */
	@Override
	int evaluate(Function<String, Number> varResolver) {
		return value;
	}

	/**
	 * Returns true if this constant node represents zero.
	 *
	 * @return true if the value is 0, false otherwise
	 */
	boolean isZero() {
		return value == 0;
	}

	/**
	 * Returns true if this constant node represents one.
	 *
	 * @return true if the value is 1, false otherwise
	 */
	boolean isOne() {
		return value == 1;
	}

	/**
	 * Returns true if this constant node represents a negative value.
	 *
	 * @return true if the value is less than 0, false otherwise
	 */
	boolean isNegative() {
		return value < 0;
	}

	/**
	 * Returns a string representation of this constant node. This is primarily
	 * useful for debugging and error messages.
	 *
	 * @return a string representation of the constant value
	 */
	@Override
	public String toString() {
		return Integer.toString(value);
	}

	/**
	 * Implements value-based equality for constant nodes. Two constant nodes are
	 * equal if they represent the same integer value.
	 *
	 * @param obj the object to compare with
	 * @return true if the other object is a ConstantNode with the same value
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ConstantNode))
			return false;
		ConstantNode other = (ConstantNode) obj;
		return this.value == other.value;
	}

	/**
	 * Returns a hash code consistent with equals().
	 *
	 * @return a hash code for this constant node
	 */
	@Override
	public int hashCode() {
		return Integer.hashCode(value);
	}
}