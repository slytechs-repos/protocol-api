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
 * Represents a variable reference in an expression tree. Handles both simple
 * variable access and increment/decrement operations. All modifications (++/--)
 * are kept local to the evaluation and do not affect the original variable
 * values.
 */
final class VariableNode extends ExprNode {

	/** The name of the variable this node references */
	private final String name;

	/** The type of modification being applied to this variable, if any */
	private final ModificationType modification;

	/** Whether the modification is prefix (++x) or postfix (x++) */
	private final boolean isPrefix;

	/**
	 * Enumerates the types of modifications that can be applied to a variable.
	 */
	private enum ModificationType {
		/** No modification */
		NONE,
		/** Increment (++) operation */
		INCREMENT,
		/** Decrement (--) operation */
		DECREMENT;
	}

	/**
	 * Constructs a simple variable reference without any modifications.
	 *
	 * @param name           the variable name
	 * @param sourcePosition position in source where this variable appears
	 */
	VariableNode(String name, int sourcePosition) {
		this(name, ModificationType.NONE, false, sourcePosition);
	}

	/**
	 * Constructs a variable reference with optional increment/decrement
	 * modification.
	 *
	 * @param name           the variable name
	 * @param modification   the type of modification (increment, decrement, or
	 *                       none)
	 * @param isPrefix       true if modification is prefix (++x), false if postfix
	 *                       (x++)
	 * @param sourcePosition position in source where this variable appears
	 */
	VariableNode(String name, ModificationType modification, boolean isPrefix, int sourcePosition) {
		super(sourcePosition);

		if (name == null || name.isEmpty()) {
			throw new ExpressionException(
					String.format("Invalid variable name at position %d", sourcePosition));
		}

		this.name = name;
		this.modification = modification;
		this.isPrefix = isPrefix;
	}

	/**
	 * Creates a prefix increment version of this variable (++x).
	 *
	 * @return a new VariableNode representing the prefix increment
	 */
	VariableNode makePrefixIncrement() {
		return new VariableNode(name, ModificationType.INCREMENT, true, getSourcePosition());
	}

	/**
	 * Creates a prefix decrement version of this variable (--x).
	 *
	 * @return a new VariableNode representing the prefix decrement
	 */
	VariableNode makePrefixDecrement() {
		return new VariableNode(name, ModificationType.DECREMENT, true, getSourcePosition());
	}

	/**
	 * Creates a postfix increment version of this variable (x++).
	 *
	 * @return a new VariableNode representing the postfix increment
	 */
	VariableNode makePostfixIncrement() {
		return new VariableNode(name, ModificationType.INCREMENT, false, getSourcePosition());
	}

	/**
	 * Creates a postfix decrement version of this variable (x--).
	 *
	 * @return a new VariableNode representing the postfix decrement
	 */
	VariableNode makePostfixDecrement() {
		return new VariableNode(name, ModificationType.DECREMENT, false, getSourcePosition());
	}

	/**
	 * Evaluates this variable node, handling any increment/decrement operations.
	 * All modifications are kept local to this evaluation.
	 *
	 * @param varResolver function to resolve variable names to their values
	 * @return the value of this variable, potentially modified by inc/dec
	 *         operations
	 * @throws ExpressionException if the variable cannot be resolved
	 */
	@Override
	int evaluate(Function<String, Number> varResolver) {
		// Resolve the current value
		int currentValue = resolveVariable(name, varResolver);

		// Handle the simple case first
		if (modification == ModificationType.NONE) {
			return currentValue;
		}

		// For modifications, we need to track both the return value and the
		// modified value (which would be stored back in a mutable context)
		int returnValue;
		int modifiedValue;

		switch (modification) {
		case INCREMENT:
			if (isPrefix) {
				// ++x: increment first, return new value
				modifiedValue = currentValue + 1;
				returnValue = modifiedValue;
			} else {
				// x++: return original value, then increment
				modifiedValue = currentValue + 1;
				returnValue = currentValue;
			}
			break;

		case DECREMENT:
			if (isPrefix) {
				// --x: decrement first, return new value
				modifiedValue = currentValue - 1;
				returnValue = modifiedValue;
			} else {
				// x--: return original value, then decrement
				modifiedValue = currentValue - 1;
				returnValue = currentValue;
			}
			break;

		default:
			// Should never happen due to earlier check
			throw new IllegalStateException("Unknown modification type: " + modification);
		}

		// Note: In a mutable context, we would store modifiedValue back
		// into the variable here. Since we're keeping modifications local,
		// we just return the appropriate value.
		return returnValue;
	}

	/**
	 * Returns the name of the variable this node references.
	 *
	 * @return the variable name
	 */
	String getName() {
		return name;
	}

	/**
	 * Returns true if this variable has any modification (++ or --).
	 *
	 * @return true if this variable is being incremented or decremented
	 */
	boolean hasModification() {
		return modification != ModificationType.NONE;
	}

	/**
	 * Returns a string representation of this variable node. Includes any
	 * increment/decrement operators in their correct position.
	 *
	 * @return a string representation of the variable reference
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		// Add prefix operator if any
		if (isPrefix && modification != ModificationType.NONE) {
			sb.append(modification == ModificationType.INCREMENT ? "++" : "--");
		}

		// Add variable name
		sb.append(name);

		// Add postfix operator if any
		if (!isPrefix && modification != ModificationType.NONE) {
			sb.append(modification == ModificationType.INCREMENT ? "++" : "--");
		}

		return sb.toString();
	}
}