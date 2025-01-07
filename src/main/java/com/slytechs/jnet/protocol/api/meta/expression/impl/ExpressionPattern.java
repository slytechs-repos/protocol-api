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
 * A compiled expression pattern that can be evaluated multiple times with different variable resolvers.
 * This class is immutable and thread-safe.
 */
public final class ExpressionPattern {
    
    /** The root node of the compiled expression tree */
    private final ExprNode root;
    
    /** The original expression string */
    private final String originalExpression;
    
    /**
     * Private constructor to force use of static factory methods.
     *
     * @param root the root node of the compiled expression tree
     * @param originalExpression the original expression string
     * @throws ExpressionException if root is null
     */
    private ExpressionPattern(ExprNode root, String originalExpression) {
        if (root == null) {
            throw new ExpressionException("Expression compilation failed: null expression tree");
        }
        this.root = root;
        this.originalExpression = originalExpression;
    }
    
    /**
     * Compiles an expression into a reusable ExpressionPattern.
     * The expression may optionally start with '=' which will be stripped.
     *
     * @param expression the expression to compile
     * @return a compiled ExpressionPattern
     * @throws ExpressionException if the expression is invalid
     * @throws NullPointerException if expression is null
     */
    public static ExpressionPattern compile(String expression) {
        if (expression == null) {
            throw new NullPointerException("Expression cannot be null");
        }
        
        // Strip leading '=' if present
        String cleanExpression = expression.startsWith("=") 
            ? expression.substring(1)
            : expression;
            
        try {
            // Create a new parser for this expression
            ExpressionParser parser = new ExpressionParser(cleanExpression);
            
            // Parse and validate the expression
            ExprNode rootNode = parser.parse();
            
            // Create and return the immutable pattern
            return new ExpressionPattern(rootNode, expression);
            
        } catch (ExpressionException e) {
            // Pass through ExpressionException with position info
            throw e;
        } catch (Exception e) {
            // Wrap any other exceptions
            throw new ExpressionException(
                "Failed to compile expression: " + expression, e);
        }
    }
    
    /**
     * Creates an evaluator that can run this pattern using the provided variable resolver.
     *
     * @param varResolver function to resolve variable names to their values
     * @return an ExpressionEvaluator bound to this pattern and resolver
     * @throws NullPointerException if varResolver is null
     */
    public ExpressionEvaluator evaluator(Function<String, Number> varResolver) {
        if (varResolver == null) {
            throw new NullPointerException("Variable resolver cannot be null");
        }
        return new ExpressionEvaluator(root, varResolver);
    }
    
    /**
     * Evaluates this pattern once with the given resolver.
     * Equivalent to creating an evaluator and calling run() once.
     *
     * @param varResolver function to resolve variable names to their values
     * @return the result of evaluating the expression
     * @throws ExpressionException if evaluation fails
     * @throws NullPointerException if varResolver is null
     */
    public ExprValue evaluate(Function<String, Number> varResolver) {
        return evaluator(varResolver).run();
    }
    
    /**
     * Evaluates this pattern with a variable resolver and initial value.
     * The initial value is available as $0 in the expression.
     *
     * @param varResolver function to resolve variable names to values
     * @param initialValue the value to use for $0
     * @return the result of evaluating the expression
     * @throws ExpressionException if evaluation fails
     * @throws NullPointerException if varResolver is null
     */
    public ExprValue evaluate(Function<String, Number> varResolver, int initialValue) {
        return evaluator(varResolver).run(initialValue);
    }
    
    /**
     * Returns the original expression string that was compiled.
     *
     * @return the original expression
     */
    public String getOriginalExpression() {
        return originalExpression;
    }
    
    /**
     * Creates a pattern that always evaluates to a constant value.
     *
     * @param value the constant value
     * @return a pattern that returns the constant value
     */
    public static ExpressionPattern constant(int value) {
        String expr = String.valueOf(value);
        ExprNode root = new ConstantNode(value, 0);
        return new ExpressionPattern(root, expr);
    }
    
    /**
     * Creates a pattern that evaluates to the value of a single variable.
     *
     * @param variableName the name of the variable
     * @return a pattern that returns the variable's value
     * @throws NullPointerException if variableName is null
     */
    public static ExpressionPattern variable(String variableName) {
        if (variableName == null) {
            throw new NullPointerException("Variable name cannot be null");
        }
        ExprNode root = new VariableNode(variableName, 0);
        return new ExpressionPattern(root, variableName);
    }
    
    /**
     * Returns a string representation of this pattern.
     * Includes both the original expression and its compiled form.
     *
     * @return string representation of the pattern
     */
    @Override
    public String toString() {
        return String.format("ExpressionPattern[expression=%s, compiled=%s]",
            originalExpression, root.toString());
    }
}