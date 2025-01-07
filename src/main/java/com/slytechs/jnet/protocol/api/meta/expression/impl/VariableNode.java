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
 * Represents a variable reference in an expression tree.
 */
final class VariableNode extends ExprNode {
    
    private final String name;
    private final ModificationType modification;
    private final boolean isPrefix;
    
    private enum ModificationType {
        NONE,
        INCREMENT,
        DECREMENT;
    }
    
    VariableNode(String name, int sourcePosition) {
        this(name, ModificationType.NONE, false, sourcePosition);
    }
    
    private VariableNode(String name, ModificationType modification, boolean isPrefix, int sourcePosition) {
        super(sourcePosition);
        
        if (name == null || name.isEmpty()) {
            throw new ExpressionException(
                String.format("Invalid variable name at position %d", sourcePosition));
        }
        
        this.name = name;
        this.modification = modification;
        this.isPrefix = isPrefix;
    }
    
    VariableNode makePrefixIncrement() {
        return new VariableNode(name, ModificationType.INCREMENT, true, getSourcePosition());
    }
    
    VariableNode makePrefixDecrement() {
        return new VariableNode(name, ModificationType.DECREMENT, true, getSourcePosition());
    }
    
    VariableNode makePostfixIncrement() {
        return new VariableNode(name, ModificationType.INCREMENT, false, getSourcePosition());
    }
    
    VariableNode makePostfixDecrement() {
        return new VariableNode(name, ModificationType.DECREMENT, false, getSourcePosition());
    }
    
    @Override
    ExprValue evaluate(Function<String, Number> varResolver) {
        ExprValue value = resolveVariable(name, varResolver);
        int currentValue = value.asInt();
        
        // Handle increment/decrement
        return switch (modification) {
            case INCREMENT -> ExprValue.number(isPrefix ? ++currentValue : currentValue++);
            case DECREMENT -> ExprValue.number(isPrefix ? --currentValue : currentValue--);
            case NONE -> value;
        };
    }
    
    String getName() {
        return name;
    }
    
    boolean hasModification() {
        return modification != ModificationType.NONE;
    }
    
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