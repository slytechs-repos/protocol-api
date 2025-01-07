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

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NewOperatorTests {
    
    private static Function<String, Number> createResolver() {
        return name -> 0;
    }
    
    @Nested
    @DisplayName("Modulo Operator Tests")
    class ModuloTests {
        
        @Test
        @DisplayName("Basic modulo operations should work")
        void testBasicModulo() {
            assertEquals(2, ExpressionPattern.compile("7 % 5").evaluate(createResolver()),
                "7 mod 5 should be 2");
            assertEquals(0, ExpressionPattern.compile("10 % 2").evaluate(createResolver()),
                "10 mod 2 should be 0");
            assertEquals(3, ExpressionPattern.compile("15 % 4").evaluate(createResolver()),
                "15 mod 4 should be 3");
        }
        
        @Test
        @DisplayName("Modulo with variables should work")
        void testModuloWithVariables() {
            Function<String, Number> resolver = name -> {
                if ("x".equals(name)) return 17;
                if ("y".equals(name)) return 5;
                return 0;
            };
            
            assertEquals(2, ExpressionPattern.compile("x % y").evaluate(resolver),
                "17 mod 5 should be 2");
        }
        
        @Test
        @DisplayName("Modulo by zero should throw exception")
        void testModuloByZero() {
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile("x % 0").evaluate(createResolver()),
                "Modulo by zero should throw exception");
        }
        
        @Test
        @DisplayName("Modulo assignment should work")
        void testModuloAssignment() {
            assertEquals(2, ExpressionPattern.compile("%=5").evaluate(createResolver(), 7),
                "7 %= 5 should be 2");
        }
    }
    
    @Nested
    @DisplayName("Ternary Operator Tests")
    class TernaryTests {
        
        @Test
        @DisplayName("Basic ternary operations should work")
        void testBasicTernary() {
            assertEquals(1, ExpressionPattern.compile("1 ? 1 : 2").evaluate(createResolver()),
                "True condition should return first value");
            assertEquals(2, ExpressionPattern.compile("0 ? 1 : 2").evaluate(createResolver()),
                "False condition should return second value");
        }
        
        @Test
        @DisplayName("Complex conditions should work")
        void testComplexConditions() {
            Function<String, Number> resolver = name -> {
                if ("x".equals(name)) return 10;
                if ("y".equals(name)) return 5;
                return 0;
            };
            
            assertEquals(1, ExpressionPattern.compile("x > y ? 1 : 0").evaluate(resolver),
                "10 > 5 should return 1");
            assertEquals(0, ExpressionPattern.compile("x < y ? 1 : 0").evaluate(resolver),
                "10 < 5 should return 0");
        }
        
        @Test
        @DisplayName("Nested ternary operators should work")
        void testNestedTernary() {
            String expr = "1 ? (0 ? 2 : 3) : 4";
            assertEquals(3, ExpressionPattern.compile(expr).evaluate(createResolver()),
                "Nested ternary should evaluate correctly");
        }
        
        @Test
        @DisplayName("Ternary with expressions should work")
        void testTernaryWithExpressions() {
            String expr = "(5 + 5) > 7 ? (2 * 3) : (10 / 2)";
            assertEquals(6, ExpressionPattern.compile(expr).evaluate(createResolver()),
                "Ternary with expressions should work");
        }
        
        @Test
        @DisplayName("Invalid ternary syntax should be rejected")
        void testInvalidTernary() {
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile("1 ? 2").evaluate(createResolver()),
                "Missing colon should throw exception");
                
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile("1 ? : 2").evaluate(createResolver()),
                "Missing true expression should throw exception");
        }
    }
    
    @Nested
    @DisplayName("Mixed Operator Tests")
    class MixedOperatorTests {
        
        @Test
        @DisplayName("Modulo and ternary should work together")
        void testMixedOperators() {
            String expr = "x % 2 ? x + 1 : x - 1";
            
            Function<String, Number> resolver = name -> {
                if ("x".equals(name)) return 5;
                return 0;
            };
            
            assertEquals(6, ExpressionPattern.compile(expr).evaluate(resolver),
                "5 % 2 is 1 (true), so return 5 + 1");
        }
    }
}