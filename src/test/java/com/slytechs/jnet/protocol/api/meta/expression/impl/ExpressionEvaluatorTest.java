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

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for the expression evaluation system.
 */
class ExpressionEvaluatorTest {
    
    private Map<String, Integer> variables;
    
    @BeforeEach
    void setUp() {
        variables = new HashMap<>();
        variables.put("x", 10);
        variables.put("y", 5);
        variables.put("z", 0);
    }
    
    /**
     * Helper method to create a variable resolver from the test variables.
     */
    private Function<String, Number> createResolver() {
        return name -> variables.get(name);
    }
    
    @Nested
    @DisplayName("Basic Expression Tests")
    class BasicExpressionTests {
        
        @Test
        @DisplayName("Constant expressions should evaluate correctly")
        void testConstantExpressions() {
            assertEquals(42, ExpressionPattern.compile("42").evaluate(createResolver()));
            assertEquals(0, ExpressionPattern.compile("0").evaluate(createResolver()));
            assertEquals(-123, ExpressionPattern.compile("-123").evaluate(createResolver()));
        }
        
        @Test
        @DisplayName("Variable references should resolve correctly")
        void testVariableReferences() {
            assertEquals(10, ExpressionPattern.compile("x").evaluate(createResolver()));
            assertEquals(5, ExpressionPattern.compile("y").evaluate(createResolver()));
            assertEquals(0, ExpressionPattern.compile("z").evaluate(createResolver()));
        }
        
        @Test
        @DisplayName("Basic arithmetic should work")
        void testBasicArithmetic() {
            assertEquals(15, ExpressionPattern.compile("x + y").evaluate(createResolver()));
            assertEquals(5, ExpressionPattern.compile("x - y").evaluate(createResolver()));
            assertEquals(50, ExpressionPattern.compile("x * y").evaluate(createResolver()));
            assertEquals(2, ExpressionPattern.compile("x / y").evaluate(createResolver()));
        }
    }
    
    @Nested
    @DisplayName("Operator Tests")
    class OperatorTests {
        
        @Test
        @DisplayName("Bitwise operators should work correctly")
        void testBitwiseOperators() {
            assertEquals(10 & 5, ExpressionPattern.compile("x & y").evaluate(createResolver()));
            assertEquals(10 | 5, ExpressionPattern.compile("x | y").evaluate(createResolver()));
            assertEquals(10 ^ 5, ExpressionPattern.compile("x ^ y").evaluate(createResolver()));
            assertEquals(~10, ExpressionPattern.compile("~x").evaluate(createResolver()));
        }
        
        @Test
        @DisplayName("Shift operators should work correctly")
        void testShiftOperators() {
            assertEquals(10 << 2, ExpressionPattern.compile("x << 2").evaluate(createResolver()));
            assertEquals(10 >> 1, ExpressionPattern.compile("x >> 1").evaluate(createResolver()));
        }
        
        @Test
        @DisplayName("Increment/Decrement operators should work correctly")
        void testIncrementDecrement() {
            // Prefix increment
            assertEquals(11, ExpressionPattern.compile("++x").evaluate(createResolver()));
            assertEquals(10, variables.get("x")); // Original value unchanged
            
            // Postfix increment
            assertEquals(10, ExpressionPattern.compile("x++").evaluate(createResolver()));
            assertEquals(10, variables.get("x")); // Original value unchanged
            
            // Prefix decrement
            assertEquals(9, ExpressionPattern.compile("--x").evaluate(createResolver()));
            assertEquals(10, variables.get("x")); // Original value unchanged
            
            // Postfix decrement
            assertEquals(10, ExpressionPattern.compile("x--").evaluate(createResolver()));
            assertEquals(10, variables.get("x")); // Original value unchanged
        }
    }
    
    @Nested
    @DisplayName("Expression Parsing Tests")
    class ParsingTests {
        
        @Test
        @DisplayName("Expressions with parentheses should respect precedence")
        void testParentheses() {
            // Test values: x = 10, y = 5
            
            // (10 + 5) * 5 = 15 * 5 = 75
            assertEquals(75, ExpressionPattern.compile("(x + y) * y").evaluate(createResolver()),
                "Parentheses grouping should be evaluated first");
                
            // 10 + (5 * 3) = 10 + 15 = 25
            assertEquals(25, ExpressionPattern.compile("x + (y * 3)").evaluate(createResolver()),
                "Nested parentheses should maintain precedence");
                
            // Compare with no parentheses to verify difference:
            // 10 + 5 * 5 = 10 + 25 = 35
            assertEquals(35, ExpressionPattern.compile("x + y * y").evaluate(createResolver()),
                "Default precedence without parentheses");
        }
        
        @Test
        @DisplayName("Complex expressions should evaluate correctly")
        void testComplexExpressions() {
            assertEquals(
                ((10 + 5) * 2) >> 1,
                ExpressionPattern.compile("(x + y) * 2 >> 1").evaluate(createResolver())
            );
        }
        
        @Test
        @DisplayName("Invalid expressions should throw appropriate exceptions")
        void testInvalidExpressions() {
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile("x + ").evaluate(createResolver()));
                
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile("(x + y").evaluate(createResolver()));
                
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile("x + * y").evaluate(createResolver()));
        }
    }
    
    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {
        
        @Test
        @DisplayName("Division by zero should be handled")
        void testDivisionByZero() {
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile("x / z").evaluate(createResolver()));
        }
        
        @Test
        @DisplayName("Invalid shift amounts should be handled")
        void testInvalidShifts() {
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile("x << -1").evaluate(createResolver()));
                
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile("x >> 32").evaluate(createResolver()));
        }
        
        @Test
        @DisplayName("Undefined variables should be handled")
        void testUndefinedVariables() {
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile("x + undefined").evaluate(createResolver()));
        }
    }
    
    @Nested
    @DisplayName("Debug Feature Tests")
    class DebugTests {
        
        @Test
        @DisplayName("Debug events should be recorded correctly")
        void testDebugEvents() {
            ExpressionPattern pattern = ExpressionPattern.compile("x + y");
            ExpressionEvaluator evaluator = pattern.evaluator(createResolver());
            
            evaluator.setDebugEnabled(true);
            evaluator.run();
            
            List<ExpressionEvaluator.DebugEvent> events = evaluator.getDebugEvents();
            assertFalse(events.isEmpty());
            
            // Verify we have node entry/exit events
            boolean hasNodeEnter = events.stream()
                .anyMatch(e -> e.getType() == ExpressionEvaluator.DebugEventType.NODE_ENTER);
            boolean hasNodeExit = events.stream()
                .anyMatch(e -> e.getType() == ExpressionEvaluator.DebugEventType.NODE_EXIT);
                
            assertTrue(hasNodeEnter);
            assertTrue(hasNodeExit);
        }
        
        @Test
        @DisplayName("Debug mode should not affect results")
        void testDebugModeResults() {
            ExpressionPattern pattern = ExpressionPattern.compile("x * y + z");
            ExpressionEvaluator evaluator = pattern.evaluator(createResolver());
            
            // Get result without debug
            int normalResult = evaluator.run();
            
            // Get result with debug
            evaluator.setDebugEnabled(true);
            int debugResult = evaluator.run();
            
            assertEquals(normalResult, debugResult);
        }
    }
    
    @Nested
    @DisplayName("Cache Behavior Tests")
    class CacheTests {
        
        @Test
        @DisplayName("Cache should maintain consistency within evaluation")
        void testCacheConsistency() {
            ExpressionPattern pattern = ExpressionPattern.compile("x + x");
            ExpressionEvaluator evaluator = pattern.evaluator(name -> {
                // Return different values each time to test cache
                return variables.get(name) + 1;
            });
            
            // Without cache, we'd get different values for each 'x'
            // With cache, both 'x' references should get the same value
            int result = evaluator.run();
            assertEquals(result, (10 + 1) * 2);
        }
        
        @Test
        @DisplayName("Cache should clear between evaluations")
        void testCacheClear() {
            ExpressionPattern pattern = ExpressionPattern.compile("x + x");
            ExpressionEvaluator evaluator = pattern.evaluator(createResolver());
            
            // First, verify cache is empty initially
            assertFalse(evaluator.isCached("x"), "Cache should be empty before evaluation");
            
            // Run evaluation and verify the result
            int result = evaluator.run();
            assertEquals(20, result, "Expression x + x should evaluate to 20 with x = 10");
            
            // Verify x was cached during evaluation
            Integer cachedValue = evaluator.getCachedValue("x");
            assertNotNull(cachedValue, "X should have a cached value");
            assertEquals(10, cachedValue.intValue(), "Cached value of x should be 10");
            assertTrue(evaluator.isCached("x"), "Variable 'x' should be cached after evaluation");
            
            // Clear cache and verify it's empty
            evaluator.clearCache();
            assertFalse(evaluator.isCached("x"), "Cache should be empty after clear");
            assertNull(evaluator.getCachedValue("x"), "X should not have a cached value after clear");
        }
    }
    
    @Nested
    @DisplayName("Pattern Reuse Tests")
    class PatternReuseTests {
        
        @Test
        @DisplayName("Patterns should be reusable with different variables")
        void testPatternReuse() {
            ExpressionPattern pattern = ExpressionPattern.compile("a + b");
            
            // First evaluation
            Map<String, Integer> vars1 = new HashMap<>();
            vars1.put("a", 1);
            vars1.put("b", 2);
            assertEquals(3, pattern.evaluate(name -> vars1.get(name)));
            
            // Second evaluation with different values
            Map<String, Integer> vars2 = new HashMap<>();
            vars2.put("a", 10);
            vars2.put("b", 20);
            assertEquals(30, pattern.evaluate(name -> vars2.get(name)));
        }
        
        @Test
        @DisplayName("Pattern compilation should be thread-safe")
        void testThreadSafety() throws InterruptedException {
            ExpressionPattern pattern = ExpressionPattern.compile("x + y");
            
            // Create multiple threads to evaluate the pattern concurrently
            Thread[] threads = new Thread[10];
            boolean[] success = new boolean[10];
            
            for (int i = 0; i < threads.length; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        pattern.evaluate(createResolver());
                        success[index] = true;
                    } catch (Exception e) {
                        success[index] = false;
                    }
                });
                threads[i].start();
            }
            
            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }
            
            // Verify all evaluations succeeded
            for (boolean succeeded : success) {
                assertTrue(succeeded);
            }
        }
    }
}