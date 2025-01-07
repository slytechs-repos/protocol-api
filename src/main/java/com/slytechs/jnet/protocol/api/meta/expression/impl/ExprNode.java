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
 * Abstract base class for all expression tree nodes. This forms the foundation
 * of our expression AST (Abstract Syntax Tree).
 */
public abstract class ExprNode {

	/**
	 * Position in the source where this node was parsed from, used for error
	 * reporting.
	 */
	private final int sourcePosition;

	/**
	 * Constructs a new ExprNode with the specified source position.
	 *
	 * @param sourcePosition the position in the source text where this node starts
	 */
	protected ExprNode(int sourcePosition) {
		this.sourcePosition = sourcePosition;
	}

	/**
	 * Gets the source position of this node.
	 *
	 * @return the position in the source text where this node starts
	 */
	public final int getSourcePosition() {
		return sourcePosition;
	}

	/**
	 * Evaluates this expression node and returns an integer result. This is the
	 * core method that all node types must implement.
	 *
	 * @param varResolver function to resolve variable names to their values
	 * @return the integer result of evaluating this node
	 * @throws ExpressionException if evaluation fails
	 */
	abstract int evaluate(Function<String, Number> varResolver);

	/**
	 * Helper method to safely evaluate a child node. This wraps evaluation errors
	 * with position information.
	 *
	 * @param node        the child node to evaluate
	 * @param varResolver the variable resolver function
	 * @return the result of evaluating the child node
	 * @throws ExpressionException if evaluation fails
	 */
	protected final int evaluateChild(ExprNode node, Function<String, Number> varResolver) {
		try {
			return node.evaluate(varResolver);
		} catch (ExpressionException e) {
			throw e; // Already wrapped
		} catch (Exception e) {
			throw new ExpressionException(
					String.format("Error evaluating expression at position %d: %s",
							node.getSourcePosition(), e.getMessage()), e);
		}
	}

	/**
	 * Helper method to safely resolve a variable name to its value.
	 *
	 * @param varName     the name of the variable to resolve
	 * @param varResolver the variable resolver function
	 * @return the resolved variable value as an int
	 * @throws ExpressionException if variable resolution fails
	 */
	protected final int resolveVariable(String varName, Function<String, Number> varResolver) {
		try {
			Number value = varResolver.apply(varName);
			if (value == null) {
				throw new ExpressionException(
						String.format("Undefined variable '%s' at position %d",
								varName, getSourcePosition()));
			}
			return value.intValue();
		} catch (ExpressionException e) {
			throw e; // Already wrapped
		} catch (Exception e) {
			throw new ExpressionException(
					String.format("Error resolving variable '%s' at position %d: %s",
							varName, getSourcePosition(), e.getMessage()), e);
		}
	}

	/**
	 * Helper method to perform checked integer division.
	 * 
	 * @param dividend the number to be divided
	 * @param divisor  the number to divide by
	 * @return the result of the division
	 * @throws ExpressionException if division by zero is attempted
	 */
	protected final int checkedDivide(int dividend, int divisor) {
		if (divisor == 0) {
			throw new ExpressionException(
					String.format("Division by zero at position %d",
							getSourcePosition()));
		}
		return dividend / divisor;
	}

	/**
	 * Helper method to perform checked bit shifts. Java allows shifts by negative
	 * amounts and by amounts >= 32, but we may want to be more restrictive.
	 *
	 * @param value  the value to shift
	 * @param shift  the number of positions to shift
	 * @param isLeft true for left shift, false for right shift
	 * @return the shifted value
	 * @throws ExpressionException if shift amount is invalid
	 */
	protected final int checkedShift(int value, int shift, boolean isLeft) {
		if (shift < 0 || shift >= 32) {
			throw new ExpressionException(
					String.format("Invalid shift amount %d at position %d. Must be between 0 and 31",
							shift, getSourcePosition()));
		}
		return isLeft ? (value << shift) : (value >> shift);
	}
}