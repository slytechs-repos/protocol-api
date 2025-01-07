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
 * Represents a reference to an argument ($0, $1, etc).
 */
final class ArgumentNode extends ExprNode {
    
    private final int argIndex;
    
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
    
    @Override
    ExprValue evaluate(Function<String, Number> varResolver) {
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
        
        // If it's already an ExprValue, return it
        if (arg instanceof ExprValue) {
            return (ExprValue) arg;
        }
        
        // If it's a number, convert to ExprValue
        if (arg instanceof Number n) {
            return ExprValue.number(n.intValue());
        }
        
        // Otherwise, treat as string
        return ExprValue.string(arg.toString());
    }
    
    @Override
    public String toString() {
        return "$" + argIndex;
    }
}