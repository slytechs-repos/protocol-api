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
 * Abstract base class for all expression tree nodes.
 */
abstract class ExprNode {
    
    /** Position in source where this node was parsed from */
    private final int sourcePosition;
    
    protected ExprNode(int sourcePosition) {
        this.sourcePosition = sourcePosition;
    }
    
    public final int getSourcePosition() {
        return sourcePosition;
    }

    /**
     * Evaluates this expression node.
     *
     * @param varResolver function to resolve variable names to their values
     * @return the value of evaluating this node
     * @throws ExpressionException if evaluation fails
     */
    abstract ExprValue evaluate(Function<String, Number> varResolver);
    
    /**
     * Helper method to safely evaluate a child node.
     */
    protected final ExprValue evaluateChild(ExprNode node, Function<String, Number> varResolver) {
        try {
            return node.evaluate(varResolver);
        } catch (ExpressionException e) {
            throw e;
        } catch (Exception e) {
            throw new ExpressionException(
                String.format("Error evaluating expression at position %d: %s",
                    node.getSourcePosition(), e.getMessage()), e);
        }
    }
    
    /**
     * Helper method to safely resolve a variable name to its value.
     */
    protected final ExprValue resolveVariable(String varName, Function<String, Number> varResolver) {
        try {
            Number value = varResolver.apply(varName);
            if (value == null) {
                throw new ExpressionException(
                    String.format("Undefined variable '%s' at position %d",
                        varName, getSourcePosition()));
            }
            return ExprValue.number(value.intValue());
        } catch (ExpressionException e) {
            throw e;
        } catch (Exception e) {
            throw new ExpressionException(
                String.format("Error resolving variable '%s' at position %d: %s",
                    varName, getSourcePosition(), e.getMessage()), e);
        }
    }
}