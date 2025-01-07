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

import java.util.ArrayList;
import java.util.List;

/**
 * Parser for expressions. Converts text expressions into ExprNode trees.
 */
final class ExpressionParser {
    
    private final String input;
    private int pos;
    private final List<Token> tokens;
    private int tokenIndex;
    
    /**
     * Token types recognized by the parser.
     */
    private enum TokenType {
        NUMBER,         // Numeric literal
        IDENTIFIER,     // Variable name
        OPERATOR,       // Binary or unary operator
        LEFT_PAREN,     // (
        RIGHT_PAREN,    // )
        EOF            // End of input
    }
    
    /**
     * Represents a token in the input stream.
     */
    private static class Token {
        final TokenType type;
        final String text;
        final int position;
        
        Token(TokenType type, String text, int position) {
            this.type = type;
            this.text = text;
            this.position = position;
        }
        
        @Override
        public String toString() {
            return String.format("%s('%s' at %d)", type, text, position);
        }
    }
    
    ExpressionParser(String input) {
        this.input = input;
        this.pos = 0;
        this.tokens = new ArrayList<>();
        this.tokenIndex = 0;
    }
    
    ExprNode parse() {
        tokenize();
        ExprNode result = parseExpression();
        
        if (peek().type != TokenType.EOF) {
            throw new ExpressionException(
                String.format("Unexpected token '%s' at position %d",
                    peek().text, peek().position));
        }
        
        return result;
    }
    
    private void tokenize() {
        while (pos < input.length()) {
            char c = input.charAt(pos);
            
            if (Character.isWhitespace(c)) {
                pos++;
                continue;
            }
            
            int startPos = pos;
            
            if (Character.isDigit(c)) {
                tokenizeNumber(startPos);
            }
            else if (isIdentifierStart(c)) {
                tokenizeIdentifier(startPos);
            }
            else if (c == '(') {
                tokens.add(new Token(TokenType.LEFT_PAREN, "(", startPos));
                pos++;
            }
            else if (c == ')') {
                tokens.add(new Token(TokenType.RIGHT_PAREN, ")", startPos));
                pos++;
            }
            else if (isOperatorStart(c)) {
                tokenizeOperator(startPos);
            }
            else {
                throw new ExpressionException(
                    String.format("Unexpected character '%c' at position %d", c, pos));
            }
        }
        
        tokens.add(new Token(TokenType.EOF, "", pos));
    }
    
    private boolean isOperatorStart(char c) {
        return "+-*/<>=&|^~!".indexOf(c) >= 0;
    }
    
    private void tokenizeNumber(int startPos) {
        NumericLiteralParser.ParseResult result = NumericLiteralParser.parse(input, startPos);
        tokens.add(new Token(TokenType.NUMBER, String.valueOf(result.value), startPos));
        pos += result.charsConsumed;
    }
    
    private void tokenizeIdentifier(int startPos) {
        StringBuilder sb = new StringBuilder();
        sb.append(input.charAt(pos++));
        
        while (pos < input.length() && isIdentifierPart(input.charAt(pos))) {
            sb.append(input.charAt(pos++));
        }
        
        tokens.add(new Token(TokenType.IDENTIFIER, sb.toString(), startPos));
    }
    
    private void tokenizeOperator(int startPos) {
        // Check for three-character operators first
        String threeChars = remainingInput(3);
        if (isValidOperator(threeChars)) {
            tokens.add(new Token(TokenType.OPERATOR, threeChars, startPos));
            pos += 3;
            return;
        }
        
        // Then two-character operators
        String twoChars = remainingInput(2);
        if (isValidOperator(twoChars)) {
            tokens.add(new Token(TokenType.OPERATOR, twoChars, startPos));
            pos += 2;
            return;
        }
        
        // Finally single-character operators
        String oneChar = remainingInput(1);
        if (isValidOperator(oneChar)) {
            tokens.add(new Token(TokenType.OPERATOR, oneChar, startPos));
            pos += 1;
            return;
        }
        
        throw new ExpressionException(
            String.format("Invalid operator at position %d", startPos));
    }
    
    private String remainingInput(int length) {
        if (pos + length <= input.length()) {
            return input.substring(pos, pos + length);
        }
        return input.substring(pos);
    }
    
    private boolean isValidOperator(String op) {
        switch (op) {
            // Three-character operators
            case "<<=":
            case ">>=":
                
            // Two-character operators
            case "<<":
            case ">>":
            case "+=":
            case "-=":
            case "*=":
            case "/=":
            case "&=":
            case "|=":
            case "^=":
            case "~=":
            case "++":
            case "--":
                
            // Single-character operators
            case "+":
            case "-":
            case "*":
            case "/":
            case "&":
            case "|":
            case "^":
            case "~":
            case "<":
            case ">":
                return true;
                
            default:
                return false;
        }
    }
    
    private static boolean isIdentifierStart(char c) {
        return Character.isLetter(c) || c == '_' || c == '$';
    }
    
    private static boolean isIdentifierPart(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }
    
    private Token peek() {
        if (tokenIndex >= tokens.size()) {
            return tokens.get(tokens.size() - 1); // EOF token
        }
        return tokens.get(tokenIndex);
    }
    
    private Token consume() {
        return tokens.get(tokenIndex++);
    }
    
    private void expect(TokenType type, String errorMessage) {
        Token token = peek();
        if (token.type != type) {
            throw new ExpressionException(
                String.format("%s at position %d", errorMessage, token.position));
        }
        consume();
    }
    
    private ExprNode parseExpression() {
        Token first = peek();
        if (first.type == TokenType.OPERATOR && first.text.endsWith("=")) {
            return parseAssignment();
        }
        return parseExpressionAtPrecedence(0);
    }

    private ExprNode parseAssignment() {
        Token op = consume();
        ExprNode right = parseExpression();
        
        // Special case for bitwise NOT
        if ("~=".equals(op.text)) {
            return UnaryOpNode.createBitwiseNot(right, op.position);
        }
        
        // Create argument node for $0
        ExprNode left = new ArgumentNode("$0", op.position);
        
        // Map operators to operations
        switch (op.text) {
            case "*=":  return new BinaryOpNode(BinaryOpNode.BinaryOperator.MULTIPLY, left, right, op.position);
            case "/=":  return new BinaryOpNode(BinaryOpNode.BinaryOperator.DIVIDE, left, right, op.position);
            case "+=":  return new BinaryOpNode(BinaryOpNode.BinaryOperator.ADD, left, right, op.position);
            case "-=":  return new BinaryOpNode(BinaryOpNode.BinaryOperator.SUBTRACT, left, right, op.position);
            case "&=":  return new BinaryOpNode(BinaryOpNode.BinaryOperator.BITWISE_AND, left, right, op.position);
            case "|=":  return new BinaryOpNode(BinaryOpNode.BinaryOperator.BITWISE_OR, left, right, op.position);
            case "^=":  return new BinaryOpNode(BinaryOpNode.BinaryOperator.BITWISE_XOR, left, right, op.position);
            case "<<=": return new BinaryOpNode(BinaryOpNode.BinaryOperator.LEFT_SHIFT, left, right, op.position);
            case ">>=": return new BinaryOpNode(BinaryOpNode.BinaryOperator.RIGHT_SHIFT, left, right, op.position);
            default:
                throw new ExpressionException(String.format("Unknown assignment operator '%s' at position %d",
                    op.text, op.position));
        }
    }
    
    private ExprNode parseExpressionAtPrecedence(int minPrecedence) {
        ExprNode left = parsePrimary();
        
        while (true) {
            Token token = peek();
            if (token.type != TokenType.OPERATOR) {
                break;
            }
            
            BinaryOpNode.BinaryOperator op = getBinaryOperator(token.text);
            if (op == null || op.getPrecedence() < minPrecedence) {
                break;
            }
            
            consume();
            ExprNode right = parseExpressionAtPrecedence(op.getPrecedence() + 1);
            left = new BinaryOpNode(op, left, right, token.position);
        }
        
        return left;
    }
    
    private ExprNode parsePrimary() {
        Token token = peek();
        
        switch (token.type) {
            case NUMBER:
                consume();
                return new ConstantNode(Integer.parseInt(token.text), token.position);
                
            case IDENTIFIER:
                return parseIdentifier();
                
            case OPERATOR:
                if (isUnaryOperator(token.text)) {
                    return parseUnaryOp();
                }
                break;
                
            case LEFT_PAREN:
                consume();
                ExprNode expr = parseExpression();
                expect(TokenType.RIGHT_PAREN, "Unclosed parenthesis");
                return expr;
        }
        
        throw new ExpressionException(
            String.format("Unexpected token '%s' at position %d",
                token.text, token.position));
    }
    
    private ExprNode parseIdentifier() {
        Token id = consume();
        if (id.text.startsWith("$")) {
            return new ArgumentNode(id.text, id.position);
        }
        
        VariableNode var = new VariableNode(id.text, id.position);
        
        Token next = peek();
        if (next.type == TokenType.OPERATOR) {
            if ("++".equals(next.text)) {
                consume();
                return var.makePostfixIncrement();
            }
            if ("--".equals(next.text)) {
                consume();
                return var.makePostfixDecrement();
            }
        }
        
        return var;
    }
    
    private ExprNode parseUnaryOp() {
        Token op = consume();
        ExprNode operand = parsePrimary();
        
        switch (op.text) {
            case "++":
                if (!(operand instanceof VariableNode)) {
                    throw new ExpressionException(
                        String.format("Invalid target for ++ at position %d",
                            op.position));
                }
                return ((VariableNode)operand).makePrefixIncrement();
            
            case "--":
                if (!(operand instanceof VariableNode)) {
                    throw new ExpressionException(
                        String.format("Invalid target for -- at position %d",
                            op.position));
                }
                return ((VariableNode)operand).makePrefixDecrement();
            
            case "~": return UnaryOpNode.createBitwiseNot(operand, op.position);
            case "!": return UnaryOpNode.createLogicalNot(operand, op.position);
            case "+": return UnaryOpNode.createPlus(operand, op.position);
            case "-": return UnaryOpNode.createMinus(operand, op.position);
            
            default:
                throw new ExpressionException(
                    String.format("Unknown unary operator '%s' at position %d",
                        op.text, op.position));
        }
    }
    
    private static BinaryOpNode.BinaryOperator getBinaryOperator(String symbol) {
        for (BinaryOpNode.BinaryOperator op : BinaryOpNode.BinaryOperator.values()) {
            if (op.getSymbol().equals(symbol)) {
                return op;
            }
        }
        return null;
    }
    
    private static boolean isUnaryOperator(String op) {
        return "++".equals(op) || "--".equals(op) ||
               "~".equals(op) || "!".equals(op) ||
               "+".equals(op) || "-".equals(op);
    }
}