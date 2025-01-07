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
 * Represents a reference to an argument passed to the evaluator.
 * Handles $0 for value and $1, $2, etc. for additional arguments.
 */
final class ArgumentNode extends ExprNode {
    
    /** The index of the argument to reference */
    private final int argIndex;
    
    /**
     * Creates a new argument reference node.
     *
     * @param argName the argument reference (e.g., "$0", "$1")
     * @param sourcePosition position in source where this reference appears
     * @throws ExpressionException if the argument name is invalid
     */
    ArgumentNode(String argName, int sourcePosition) {
        super(sourcePosition);
        
        try {
            // Strip the $ and parse the index
            this.argIndex = Integer.parseInt(argName.substring(1));
            
            if (argIndex < 0) {
                throw new ExpressionException(
                    String.format("Invalid argument index %d at position %d: must be non-negative",
                        argIndex, sourcePosition));
            }
        } catch (NumberFormatException e) {
            throw new ExpressionException(
                String.format("Invalid argument reference '%s' at position %d",
                    argName, sourcePosition));
        }
    }
    
    /**
     * Evaluates this node by retrieving the referenced argument.
     *
     * @param varResolver unused for argument nodes
     * @return the value of the referenced argument
     * @throws ExpressionException if argument index is out of bounds
     */
    @Override
    int evaluate(Function<String, Number> varResolver) {
        Object[] args = ExpressionEvaluator.getCurrentArguments();
        if (args == null || argIndex >= args.length) {
            throw new ExpressionException(
                String.format("Argument index $%d is out of bounds at position %d",
                    argIndex, getSourcePosition()));
        }
        
        Object arg = args[argIndex];
        if (arg == null) {
            throw new ExpressionException(
                String.format("Argument $%d is null at position %d",
                    argIndex, getSourcePosition()));
        }
        
        if (arg instanceof Number) {
            return ((Number) arg).intValue();
        }
        
        try {
            // Try to convert string or other types to number
            return Integer.parseInt(arg.toString());
        } catch (NumberFormatException e) {
            throw new ExpressionException(
                String.format("Cannot convert argument $%d value '%s' to integer at position %d",
                    argIndex, arg, getSourcePosition()));
        }
    }
    
    @Override
    public String toString() {
        return "$" + argIndex;
    }
}