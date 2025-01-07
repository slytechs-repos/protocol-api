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
 * Represents a ternary conditional operation (condition ? trueExpr : falseExpr).
 * The condition is evaluated as true if non-zero, following C-style semantics.
 */
final class TernaryNode extends ExprNode {
    
    private final ExprNode condition;
    private final ExprNode trueExpr;
    private final ExprNode falseExpr;
    
    /**
     * Creates a new ternary conditional node.
     *
     * @param condition the condition to evaluate
     * @param trueExpr expression to evaluate if condition is true (non-zero)
     * @param falseExpr expression to evaluate if condition is false (zero)
     * @param sourcePosition position in source where this operation appears
     * @throws ExpressionException if any expression is null
     */
    TernaryNode(ExprNode condition, ExprNode trueExpr, ExprNode falseExpr, int sourcePosition) {
        super(sourcePosition);
        
        if (condition == null || trueExpr == null || falseExpr == null) {
            throw new ExpressionException(
                String.format("Invalid ternary operation at position %d: missing expression",
                    sourcePosition));
        }
        
        this.condition = condition;
        this.trueExpr = trueExpr;
        this.falseExpr = falseExpr;
    }
    
    /**
     * Creates a ternary operation node.
     */
    static TernaryNode create(ExprNode condition, ExprNode trueExpr, ExprNode falseExpr, int sourcePosition) {
        return new TernaryNode(condition, trueExpr, falseExpr, sourcePosition);
    }
    
    /**
     * Evaluates this ternary operation by:
     * 1. Evaluating the condition
     * 2. Based on condition, evaluating either trueExpr or falseExpr
     * Note: Only one branch is evaluated, providing short-circuit behavior.
     *
     * @param varResolver function to resolve variable names to their values
     * @return the result of evaluating the selected branch
     * @throws ExpressionException if evaluation fails
     */
    @Override
    int evaluate(Function<String, Number> varResolver) {
        int condValue = evaluateChild(condition, varResolver);
        
        return evaluateChild(
            condValue != 0 ? trueExpr : falseExpr,
            varResolver
        );
    }
    
    @Override
    public String toString() {
        return String.format("(%s ? %s : %s)",
            condition.toString(),
            trueExpr.toString(),
            falseExpr.toString());
    }
}