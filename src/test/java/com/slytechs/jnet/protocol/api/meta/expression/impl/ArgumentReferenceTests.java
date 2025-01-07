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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ArgumentReferenceTests {
    
    @Nested
    @DisplayName("Basic Argument Tests")
    class BasicArgumentTests {
        
        @Test
        @DisplayName("Arguments should be accessible by index")
        void testBasicArguments() {
            ExpressionPattern pattern = ExpressionPattern.compile("$0");
            ExpressionEvaluator evaluator = pattern.evaluator(name -> 0);
            
            assertEquals(42, evaluator.run(42));
            assertEquals(100, evaluator.run(100));
        }
        
        @Test
        @DisplayName("Multiple arguments should be accessible")
        void testMultipleArguments() {
            ExpressionPattern pattern = ExpressionPattern.compile("$1 + $2");
            ExpressionEvaluator evaluator = pattern.evaluator(name -> 0);
            
            assertEquals(15, evaluator.run(999, 10, 5));  // $0 is unused
        }
        
        @Test
        @DisplayName("Arguments should work in complex expressions")
        void testArgumentsInExpressions() {
            ExpressionPattern pattern = ExpressionPattern.compile("($0 << 8) | $1");
            ExpressionEvaluator evaluator = pattern.evaluator(name -> 0);
            
            assertEquals(0xFF00 | 0xFF, evaluator.run(0xFF, 0xFF));
        }
    }
    
    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {
        
        @Test
        @DisplayName("Out of bounds arguments should throw exception")
        void testOutOfBounds() {
            ExpressionPattern pattern = ExpressionPattern.compile("$0 + $1");
            ExpressionEvaluator evaluator = pattern.evaluator(name -> 0);
            
            assertThrows(ExpressionException.class, () ->
                evaluator.run(42));  // $1 is out of bounds
        }
        
        @Test
        @DisplayName("Non-numeric arguments should throw exception")
        void testNonNumeric() {
            ExpressionPattern pattern = ExpressionPattern.compile("$0");
            ExpressionEvaluator evaluator = pattern.evaluator(name -> 0);
            
            assertThrows(ExpressionException.class, () ->
                evaluator.run("not a number"));
        }
        
        @Test
        @DisplayName("Invalid argument references should be rejected")
        void testInvalidReferences() {
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile("$x").evaluate(name -> 0));
                
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile("$-1").evaluate(name -> 0));
        }
    }
    
    @Nested
    @DisplayName("Mixed Usage Tests")
    class MixedUsageTests {
        
        @Test
        @DisplayName("Arguments and variables should work together")
        void testMixedUsage() {
            ExpressionPattern pattern = ExpressionPattern.compile("x + $0");
            ExpressionEvaluator evaluator = pattern.evaluator(name -> {
                if ("x".equals(name)) return 10;
                return 0;
            });
            
            assertEquals(15, evaluator.run(5));
        }
        
        @Test
        @DisplayName("Arguments should work with all operators")
        void testWithOperators() {
            ExpressionPattern pattern = ExpressionPattern.compile("$0 + $1 * $2");
            ExpressionEvaluator evaluator = pattern.evaluator(name -> 0);
            
            assertEquals(20, evaluator.run(5, 3, 5));  // 5 + 3 * 5
        }
    }
}