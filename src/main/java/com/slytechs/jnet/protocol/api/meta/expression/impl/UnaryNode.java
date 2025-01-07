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
 * Represents a unary operation in an expression tree.
 */
final class UnaryOpNode extends ExprNode {
    
    /** The operand of this unary operation */
    private final ExprNode operand;
    
    /** The type of unary operation */
    private final UnaryOperator operator;
    
    public enum UnaryOperator {
        LOGICAL_NOT("!") {
            @Override
            int evaluate(int value) {
                return value == 0 ? 1 : 0;
            }
        },
        
        BITWISE_NOT("~") {
            @Override
            int evaluate(int value) {
                return ~value;
            }
        },
        
        PLUS("+") {
            @Override
            int evaluate(int value) {
                return value;
            }
        },
        
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
        
        public String getSymbol() {
            return symbol;
        }
        
        abstract int evaluate(int value);
    }
    
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
    
    static UnaryOpNode createLogicalNot(ExprNode operand, int sourcePosition) {
        return new UnaryOpNode(UnaryOperator.LOGICAL_NOT, operand, sourcePosition);
    }
    
    static UnaryOpNode createBitwiseNot(ExprNode operand, int sourcePosition) {
        return new UnaryOpNode(UnaryOperator.BITWISE_NOT, operand, sourcePosition);
    }
    
    static UnaryOpNode createPlus(ExprNode operand, int sourcePosition) {
        return new UnaryOpNode(UnaryOperator.PLUS, operand, sourcePosition);
    }
    
    static UnaryOpNode createMinus(ExprNode operand, int sourcePosition) {
        return new UnaryOpNode(UnaryOperator.MINUS, operand, sourcePosition);
    }
    
    @Override
    ExprValue evaluate(Function<String, Number> varResolver) {
        ExprValue operandValue = evaluateChild(operand, varResolver);
        return ExprValue.number(operator.evaluate(operandValue.asInt()));
    }
    
    @Override
    public String toString() {
        return operator.getSymbol() + operand.toString();
    }
}