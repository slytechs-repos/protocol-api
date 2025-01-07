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

/**
 * Specialized parser for handling different numeric literal formats.
 * Supports decimal, hexadecimal, binary, octal and numeric separators.
 */
final class NumericLiteralParser {
    
    private static final int MAX_LITERAL_LENGTH = 32;  // Reasonable limit for integer literals
    
    /**
     * Different bases for number parsing
     */
    private enum Base {
        BINARY(2),
        OCTAL(8),
        DECIMAL(10),
        HEXADECIMAL(16);
        
        final int value;
        
        Base(int value) {
            this.value = value;
        }
    }
    
    /**
     * Parses a numeric literal from the input string starting at the given position.
     * Supports decimal, hex (0x), binary (0b), octal (0o) and underscore separators.
     *
     * @param input the input string containing the number
     * @param startPos starting position in the input
     * @return parsing result containing the value and number of characters consumed
     * @throws ExpressionException if the literal is invalid
     */
    static ParseResult parse(String input, int startPos) {
        if (startPos >= input.length()) {
            throw new ExpressionException("Unexpected end of input while parsing number");
        }
        
        // Check for hex/binary/octal prefix
        Base base = Base.DECIMAL;
        int pos = startPos;
        int prefixLength = 0;
        
        if (pos + 1 < input.length() && input.charAt(pos) == '0') {
            char prefix = Character.toLowerCase(input.charAt(pos + 1));
            switch (prefix) {
                case 'x':  // Hexadecimal
                    base = Base.HEXADECIMAL;
                    prefixLength = 2;
                    break;
                case 'b':  // Binary
                    base = Base.BINARY;
                    prefixLength = 2;
                    break;
                case 'o':  // Octal
                    base = Base.OCTAL;
                    prefixLength = 2;
                    break;
            }
        }
        
        pos += prefixLength;
        
        // Collect digits, allowing underscores between digits
        StringBuilder digits = new StringBuilder();
        boolean hasDigits = false;
        boolean lastWasUnderscore = false;
        
        while (pos < input.length()) {
            char c = input.charAt(pos);
            
            if (c == '_') {
                if (!hasDigits || lastWasUnderscore) {
                    throw new ExpressionException(
                        String.format("Invalid underscore position in numeric literal at position %d", pos));
                }
                lastWasUnderscore = true;
                pos++;
                continue;
            }
            
            if (isValidDigit(c, base)) {
                if (digits.length() >= MAX_LITERAL_LENGTH) {
                    throw new ExpressionException(
                        String.format("Numeric literal too long at position %d", startPos));
                }
                digits.append(c);
                hasDigits = true;
                lastWasUnderscore = false;
                pos++;
            } else {
                break;
            }
        }
        
        // Validate the literal
        if (!hasDigits) {
            throw new ExpressionException(
                String.format("Invalid numeric literal at position %d: no digits", startPos));
        }
        if (lastWasUnderscore) {
            throw new ExpressionException(
                String.format("Invalid numeric literal at position %d: ends with underscore", startPos));
        }
        
        // Parse the value
        try {
            int value = Integer.parseInt(digits.toString(), base.value);
            return new ParseResult(value, pos - startPos);
        } catch (NumberFormatException e) {
            throw new ExpressionException(
                String.format("Invalid numeric literal at position %d: %s",
                    startPos, e.getMessage()));
        }
    }
    
    /**
     * Checks if a character is a valid digit for the given base.
     */
    private static boolean isValidDigit(char c, Base base) {
        switch (base) {
            case BINARY:
                return c == '0' || c == '1';
                
            case OCTAL:
                return c >= '0' && c <= '7';
                
            case DECIMAL:
                return Character.isDigit(c);
                
            case HEXADECIMAL:
                return Character.isDigit(c) ||
                       (Character.toLowerCase(c) >= 'a' && Character.toLowerCase(c) <= 'f');
                
            default:
                return false;
        }
    }
    
    /**
     * Result of parsing a numeric literal.
     */
    static class ParseResult {
        final int value;          // The parsed numeric value
        final int charsConsumed;  // Number of characters consumed from input
        
        ParseResult(int value, int charsConsumed) {
            this.value = value;
            this.charsConsumed = charsConsumed;
        }
    }
}