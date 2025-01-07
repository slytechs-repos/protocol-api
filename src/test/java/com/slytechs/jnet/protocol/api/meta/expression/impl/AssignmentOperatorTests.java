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

class AssignmentOperatorTests {
    
    // Helper method to create a resolver that always returns 0
    private static Function<String, Number> createResolver() {
        return name -> 0;
    }
    
    @Nested
    @DisplayName("Basic Assignment Operators")
    class BasicAssignmentTests {
        
        @Test
        @DisplayName("Arithmetic assignment operators should work")
        void testArithmeticAssignment() {
            // *=4 is equivalent to $0 * 4
            assertEquals(20, ExpressionPattern.compile("*=4").evaluate(createResolver(), 5));
            
            // /=2 is equivalent to $0 / 2
            assertEquals(5, ExpressionPattern.compile("/=2").evaluate(createResolver(), 10));
            
            // +=5 is equivalent to $0 + 5
            assertEquals(15, ExpressionPattern.compile("+=5").evaluate(createResolver(), 10));
            
            // -=3 is equivalent to $0 - 3
            assertEquals(7, ExpressionPattern.compile("-=3").evaluate(createResolver(), 10));
        }
        
        @Test
        @DisplayName("Bitwise assignment operators should work")
        void testBitwiseAssignment() {
            // &=5 is equivalent to $0 & 5
            assertEquals(5, ExpressionPattern.compile("&=5").evaluate(createResolver(), 7), 
                "AND: 7 & 5 should equal 5");
            
            // |=2 is equivalent to $0 | 2
            assertEquals(7, ExpressionPattern.compile("|=2").evaluate(createResolver(), 5),
                "OR: 5 | 2 should equal 7");
            
            // ^=3 is equivalent to $0 ^ 3
            assertEquals(6, ExpressionPattern.compile("^=3").evaluate(createResolver(), 5),
                "XOR: 5 ^ 3 should equal 6");
            
            // ~= is special: evaluates to ~(operand)
            assertEquals(~10, ExpressionPattern.compile("~=10").evaluate(createResolver(), 999),
                "NOT: ~10 should equal " + ~10);
        }
        
        @Test
        @DisplayName("Shift assignment operators should work")
        void testShiftAssignment() {
            // Test left shift
            ExpressionPattern leftPattern = ExpressionPattern.compile("<<=2");
            System.out.println("Testing left shift: " + leftPattern.getOriginalExpression());
            ExprValue leftResult = leftPattern.evaluate(createResolver(), 5);
            assertEquals(20, leftResult.asInt(), "Left shift: 5 << 2 should equal 20");

            // Test right shift
            ExpressionPattern rightPattern = ExpressionPattern.compile(">>=1");
            System.out.println("Testing right shift: " + rightPattern.getOriginalExpression());
            ExprValue rightResult = rightPattern.evaluate(createResolver(), 10);
            assertEquals(5, rightResult.asInt(), "Right shift: 10 >> 1 should equal 5");
        }
    }
    
    @Nested
    @DisplayName("Complex Assignment Tests")
    class ComplexAssignmentTests {
        
        @Test
        @DisplayName("Assignment with expressions should work")
        void testComplexAssignment() {
            // *=(2 + 3) is equivalent to $0 * (2 + 3)
            assertEquals(25, ExpressionPattern.compile("*=(2 + 3)").evaluate(createResolver(), 5));
            
            // &=(1 << 3) is equivalent to $0 & (1 << 3)
            assertEquals(8, ExpressionPattern.compile("&=(1 << 3)").evaluate(createResolver(), 12));
        }
        
        @Test
        @DisplayName("Assignment with variables should work")
        void testAssignmentWithVariables() {
            Function<String, Number> resolver = name -> "x".equals(name) ? 4 : 0;
            assertEquals(20, ExpressionPattern.compile("*=x").evaluate(resolver, 5));  // 5 * 4
        }
    }
    
    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {
        
        @Test
        @DisplayName("Invalid assignment operators should be rejected")
        void testInvalidAssignment() {
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile(">=2").evaluate(createResolver(), 10));
        }
        
        @Test
        @DisplayName("Division by zero should be handled")
        void testDivisionByZero() {
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile("/=0").evaluate(createResolver(), 10));
        }
        
        @Test
        @DisplayName("Invalid shift amounts should be handled")
        void testInvalidShifts() {
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile("<<=32").evaluate(createResolver(), 10));
                
            assertThrows(ExpressionException.class, () ->
                ExpressionPattern.compile(">>=(-1)").evaluate(createResolver(), 10));
        }
    }
}