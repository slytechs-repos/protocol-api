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
 * Represents a binary operation in an expression tree.
 * Handles arithmetic, bitwise, and shift operations between two operands.
 */
final class BinaryOpNode extends ExprNode {
    
    /** The left operand of the binary operation */
    private final ExprNode left;
    
    /** The right operand of the binary operation */
    private final ExprNode right;
    
    /** The type of binary operation */
    private final BinaryOperator operator;
    
    /**
     * Enumerates the types of binary operations supported, with their precedence levels.
     * Higher precedence values indicate operations that should be performed first.
     */
    public enum BinaryOperator {
        // Multiplicative (precedence 5)
        MULTIPLY("*", 5) {
            @Override
            int evaluate(int left, int right) {
                return left * right;
            }
        },
        DIVIDE("/", 5) {
            @Override
            int evaluate(int left, int right) {
                if (right == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return left / right;
            }
        },
        MODULO("%", 5) {
            @Override
            int evaluate(int left, int right) {
                if (right == 0) {
                    throw new ArithmeticException("Modulo by zero");
                }
                return left % right;
            }
        },
        
        // Additive (precedence 4)
        ADD("+", 4) {
            @Override
            int evaluate(int left, int right) {
                return left + right;
            }
        },
        SUBTRACT("-", 4) {
            @Override
            int evaluate(int left, int right) {
                return left - right;
            }
        },
        
        // Shift operations (precedence 3)
        LEFT_SHIFT("<<", 3) {
            @Override
            int evaluate(int left, int right) {
                if (right < 0 || right >= 32) {
                    throw new ArithmeticException("Invalid shift amount: " + right);
                }
                return left << right;
            }
        },
        RIGHT_SHIFT(">>", 3) {
            @Override
            int evaluate(int left, int right) {
                if (right < 0 || right >= 32) {
                    throw new ArithmeticException("Invalid shift amount: " + right);
                }
                return left >> right;
            }
        },
        
        // Bitwise AND (precedence 2)
        BITWISE_AND("&", 2) {
            @Override
            int evaluate(int left, int right) {
                return left & right;
            }
        },
        
        // Bitwise XOR (precedence 1)
        BITWISE_XOR("^", 1) {
            @Override
            int evaluate(int left, int right) {
                return left ^ right;
            }
        },
        
        // Bitwise OR (precedence 0)
        BITWISE_OR("|", 0) {
            @Override
            int evaluate(int left, int right) {
                return left | right;
            }
        };
        
        private final String symbol;
        private final int precedence;
        
        BinaryOperator(String symbol, int precedence) {
            this.symbol = symbol;
            this.precedence = precedence;
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
         * Gets the precedence level of this operator.
         * Higher values indicate higher precedence.
         *
         * @return the precedence level
         */
        public int getPrecedence() {
            return precedence;
        }
        
        /**
         * Evaluates this binary operator on the given values.
         *
         * @param left the left operand value
         * @param right the right operand value
         * @return the result of applying this operator
         * @throws ArithmeticException if the operation fails (e.g., division by zero)
         */
        abstract int evaluate(int left, int right);
    }
    
    /**
     * Constructs a new binary operation node.
     *
     * @param operator the binary operator to apply
     * @param left the left operand expression
     * @param right the right operand expression
     * @param sourcePosition position in source where this operation appears
     * @throws ExpressionException if either operand is null
     */
    BinaryOpNode(BinaryOperator operator, ExprNode left, ExprNode right, int sourcePosition) {
        super(sourcePosition);
        
        if (left == null || right == null) {
            throw new ExpressionException(
                String.format("Missing operand for %s operator at position %d",
                    operator.getSymbol(), sourcePosition));
        }
        
        this.operator = operator;
        this.left = left;
        this.right = right;
    }
    
    /**
     * Creates a binary operation node for the specified operator.
     *
     * @param operator the binary operator to apply
     * @param left the left operand
     * @param right the right operand
     * @param sourcePosition position in source
     * @return a new BinaryOpNode
     */
    static BinaryOpNode create(BinaryOperator operator, ExprNode left, ExprNode right, int sourcePosition) {
        return new BinaryOpNode(operator, left, right, sourcePosition);
    }
    
    /**
     * Evaluates this binary operation by:
     * 1. Evaluating both operands
     * 2. Applying the binary operator to the results
     *
     * @param varResolver function to resolve variable names to their values
     * @return the result of applying the binary operator to the operands
     * @throws ExpressionException if evaluation fails
     */
    @Override
    int evaluate(Function<String, Number> varResolver) {
        try {
            int leftValue = evaluateChild(left, varResolver);
            int rightValue = evaluateChild(right, varResolver);
            return operator.evaluate(leftValue, rightValue);
        } catch (ArithmeticException e) {
            throw new ExpressionException(
                String.format("Arithmetic error in %s operation at position %d: %s",
                    operator.getSymbol(), getSourcePosition(), e.getMessage()), e);
        }
    }
    
    /**
     * Returns the operator type of this node.
     *
     * @return the binary operator
     */
    BinaryOperator getOperator() {
        return operator;
    }
    
    /**
     * Returns the left operand expression.
     *
     * @return the left operand node
     */
    ExprNode getLeft() {
        return left;
    }
    
    /**
     * Returns the right operand expression.
     *
     * @return the right operand node
     */
    ExprNode getRight() {
        return right;
    }
    
    /**
     * Returns a string representation of this binary operation.
     * Format is: (left operator right) with parentheses to clarify precedence.
     *
     * @return string representation of the operation
     */
    @Override
    public String toString() {
        return String.format("(%s %s %s)",
            left.toString(),
            operator.getSymbol(),
            right.toString());
    }
}