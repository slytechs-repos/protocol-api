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
 * Represents a unary operation in an expression tree. Handles operators like
 * logical not (!), bitwise complement (~), unary plus (+) and minus (-).
 */
final class UnaryOpNode extends ExprNode {

	/** The operand of this unary operation */
	private final ExprNode operand;

	/** The type of unary operation */
	private final UnaryOperator operator;

	/**
	 * Enumerates the types of unary operations supported.
	 */
	public enum UnaryOperator {
		/** Logical not (!): converts non-zero to 0, zero to 1 */
		LOGICAL_NOT("!") {
			@Override
			int evaluate(int value) {
				return value == 0 ? 1 : 0;
			}
		},

		/** Bitwise complement (~): inverts all bits */
		BITWISE_NOT("~") {
			@Override
			int evaluate(int value) {
				return ~value;
			}
		},

		/** Unary plus (+): effectively a no-op */
		PLUS("+") {
			@Override
			int evaluate(int value) {
				return value;
			}
		},

		/** Unary minus (-): negates the value */
		MINUS("-") {
			@Override
			int evaluate(int value) {
				return -value;
			}
		};

		private final String symbol;

		UnaryOperator(String symbol) {
			this.symbol = symbol;
		}

		/**
		 * Gets the string symbol for this operator.
		 *
		 * @return the operator symbol
		 */
		public String getSymbol() {
			return symbol;
		}

		/**
		 * Evaluates this unary operator on the given value.
		 *
		 * @param value the input value
		 * @return the result of applying this operator
		 */
		abstract int evaluate(int value);
	}

	/**
	 * Constructs a new unary operation node.
	 *
	 * @param operator       the unary operator to apply
	 * @param operand        the expression to apply the operator to
	 * @param sourcePosition position in source where this operation appears
	 * @throws ExpressionException if operand is null
	 */
	UnaryOpNode(UnaryOperator operator, ExprNode operand, int sourcePosition) {
		super(sourcePosition);

		if (operand == null) {
			throw new ExpressionException(
					String.format("Missing operand for %s operator at position %d",
							operator.getSymbol(), sourcePosition));
		}

		this.operator = operator;
		this.operand = operand;
	}

	/**
	 * Creates a logical NOT node (!).
	 *
	 * @param operand        the expression to negate
	 * @param sourcePosition position in source
	 * @return a new UnaryOpNode for logical NOT
	 */
	static UnaryOpNode createLogicalNot(ExprNode operand, int sourcePosition) {
		return new UnaryOpNode(UnaryOperator.LOGICAL_NOT, operand, sourcePosition);
	}

	/**
	 * Creates a bitwise NOT node (~).
	 *
	 * @param operand        the expression to complement
	 * @param sourcePosition position in source
	 * @return a new UnaryOpNode for bitwise NOT
	 */
	static UnaryOpNode createBitwiseNot(ExprNode operand, int sourcePosition) {
		return new UnaryOpNode(UnaryOperator.BITWISE_NOT, operand, sourcePosition);
	}

	/**
	 * Creates a unary plus node (+).
	 *
	 * @param operand        the expression to apply unary plus to
	 * @param sourcePosition position in source
	 * @return a new UnaryOpNode for unary plus
	 */
	static UnaryOpNode createPlus(ExprNode operand, int sourcePosition) {
		return new UnaryOpNode(UnaryOperator.PLUS, operand, sourcePosition);
	}

	/**
	 * Creates a unary minus node (-).
	 *
	 * @param operand        the expression to negate
	 * @param sourcePosition position in source
	 * @return a new UnaryOpNode for unary minus
	 */
	static UnaryOpNode createMinus(ExprNode operand, int sourcePosition) {
		return new UnaryOpNode(UnaryOperator.MINUS, operand, sourcePosition);
	}

	/**
	 * Evaluates this unary operation by: 1. Evaluating the operand 2. Applying the
	 * unary operator to the result
	 *
	 * @param varResolver function to resolve variable names to their values
	 * @return the result of applying the unary operator to the operand
	 * @throws ExpressionException if evaluation fails
	 */
	@Override
	int evaluate(Function<String, Number> varResolver) {
		try {
			int operandValue = evaluateChild(operand, varResolver);
			return operator.evaluate(operandValue);
		} catch (ArithmeticException e) {
			throw new ExpressionException(
					String.format("Arithmetic error in %s operation at position %d: %s",
							operator.getSymbol(), getSourcePosition(), e.getMessage()), e);
		}
	}

	/**
	 * Returns the operator type of this node.
	 *
	 * @return the unary operator
	 */
	UnaryOperator getOperator() {
		return operator;
	}

	/**
	 * Returns the operand expression.
	 *
	 * @return the operand node
	 */
	ExprNode getOperand() {
		return operand;
	}

	/**
	 * Returns a string representation of this unary operation. Format is: operator
	 * operand (e.g. "!x" or "~42")
	 *
	 * @return string representation of the operation
	 */
	@Override
	public String toString() {
		return operator.getSymbol() + operand.toString();
	}
}