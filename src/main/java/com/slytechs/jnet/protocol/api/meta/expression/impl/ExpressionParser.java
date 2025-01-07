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
        STRING,         // String literal
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
    }
    
    ExpressionParser(String input) {
        this.input = input;
        this.pos = 0;
        this.tokens = new ArrayList<>();
        this.tokenIndex = 0;
    }
    
    ExprNode parse() {
        tokenize();
        return parseExpression();
    }
    
    private void tokenize() {
        while (pos < input.length()) {
            char c = input.charAt(pos);
            int startPos = pos;
            
            if (Character.isWhitespace(c)) {
                pos++;
                continue;
            }
            
            tokens.add(switch (c) {
                case '(' -> { pos++; yield new Token(TokenType.LEFT_PAREN, "(", startPos); }
                case ')' -> { pos++; yield new Token(TokenType.RIGHT_PAREN, ")", startPos); }
                case '"' -> tokenizeString(startPos);
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> tokenizeNumber(startPos);
                default -> {
                    if (isIdentifierStart(c)) yield tokenizeIdentifier(startPos);
                    else if (isOperatorStart(c)) yield tokenizeOperator(startPos);
                    else throw new ExpressionException(
                        String.format("Invalid character '%c' at position %d", c, startPos));
                }
            });
        }
        
        tokens.add(new Token(TokenType.EOF, "", pos));
    }
    
    private Token tokenizeString(int startPos) {
        pos++; // Skip opening quote
        StringBuilder sb = new StringBuilder();
        
        while (pos < input.length()) {
            char c = input.charAt(pos++);
            if (c == '"') {
                return new Token(TokenType.STRING, sb.toString(), startPos);
            }
            if (c == '\\' && pos < input.length()) {
                // Handle escape sequences
                char next = input.charAt(pos++);
                sb.append(switch (next) {
                    case 'n' -> '\n';
                    case 't' -> '\t';
                    case 'r' -> '\r';
                    case '"' -> '"';
                    case '\\' -> '\\';
                    default -> throw new ExpressionException(
                        String.format("Invalid escape sequence '\\%c' at position %d",
                            next, pos - 2));
                });
            } else {
                sb.append(c);
            }
        }
        
        throw new ExpressionException(
            String.format("Unterminated string starting at position %d", startPos));
    }
    
    private Token tokenizeNumber(int startPos) {
        StringBuilder sb = new StringBuilder();
        
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos++));
        }
        
        return new Token(TokenType.NUMBER, sb.toString(), startPos);
    }
    
    private Token tokenizeIdentifier(int startPos) {
        StringBuilder sb = new StringBuilder();
        sb.append(input.charAt(pos++));
        
        while (pos < input.length() && isIdentifierPart(input.charAt(pos))) {
            sb.append(input.charAt(pos++));
        }
        
        return new Token(TokenType.IDENTIFIER, sb.toString(), startPos);
    }
    
    private Token tokenizeOperator(int startPos) {
        // Try three-character operators
        if (pos + 2 < input.length()) {
            String op = input.substring(pos, pos + 3);
            if ("<<=".equals(op) || ">>=".equals(op)) {
                pos += 3;
                return new Token(TokenType.OPERATOR, op, startPos);
            }
        }
        
        // Try two-character operators
        if (pos + 1 < input.length()) {
            String op = input.substring(pos, pos + 2);
            if (isTwoCharOperator(op)) {
                pos += 2;
                return new Token(TokenType.OPERATOR, op, startPos);
            }
        }
        
        // Single-character operators
        String op = String.valueOf(input.charAt(pos++));
        if (isOneCharOperator(op)) {
            return new Token(TokenType.OPERATOR, op, startPos);
        }
        
        throw new ExpressionException(
            String.format("Invalid operator at position %d", startPos));
    }
    
    private ExprNode parseExpression() {
        Token token = peek();
        
        // Handle assignment operators at the start of the expression
        if (token.type == TokenType.OPERATOR && token.text.endsWith("=") && token.text.length() > 1) {
            token = consume(); // consume operator
            ExprNode right = parseConditional();
            ExprNode left = new ArgumentNode("$0", token.position);
            
            // Map assignment operators to operations
            return switch (token.text) {
                case "*=" -> new BinaryOpNode(BinaryOpNode.BinaryOperator.MULTIPLY, left, right, token.position);
                case "/=" -> new BinaryOpNode(BinaryOpNode.BinaryOperator.DIVIDE, left, right, token.position);
                case "%=" -> new BinaryOpNode(BinaryOpNode.BinaryOperator.MODULO, left, right, token.position);
                case "+=" -> new BinaryOpNode(BinaryOpNode.BinaryOperator.ADD, left, right, token.position);
                case "-=" -> new BinaryOpNode(BinaryOpNode.BinaryOperator.SUBTRACT, left, right, token.position);
                case "&=" -> new BinaryOpNode(BinaryOpNode.BinaryOperator.BITWISE_AND, left, right, token.position);
                case "|=" -> new BinaryOpNode(BinaryOpNode.BinaryOperator.BITWISE_OR, left, right, token.position);
                case "^=" -> new BinaryOpNode(BinaryOpNode.BinaryOperator.BITWISE_XOR, left, right, token.position);
                case "<<=" -> new BinaryOpNode(BinaryOpNode.BinaryOperator.LEFT_SHIFT, left, right, token.position);
                case ">>=" -> new BinaryOpNode(BinaryOpNode.BinaryOperator.RIGHT_SHIFT, left, right, token.position);
                case "~=" -> UnaryOpNode.createBitwiseNot(right, token.position);
                default -> throw new ExpressionException(
                    String.format("Unknown assignment operator '%s' at position %d",
                        token.text, token.position));
            };
        }
        
        // Not an assignment operator, parse as normal expression
        return parseConditional();
    }
    
    private ExprNode parseConditional() {
        ExprNode condition = parseExpressionAtPrecedence(0);
        
        // Check for ternary operator
        if (peek().type == TokenType.OPERATOR && "?".equals(peek().text)) {
            int startPos = peek().position;
            consume(); // consume ?
            
            ExprNode trueExpr = parseExpression();
            
            if (peek().type != TokenType.OPERATOR || !":".equals(peek().text)) {
                throw new ExpressionException(
                    String.format("Expected ':' for ternary operator at position %d",
                        peek().position));
            }
            consume(); // consume :
            
            ExprNode falseExpr = parseExpression();
            
            return new TernaryNode(condition, trueExpr, falseExpr, startPos);
        }
        
        return condition;
    }
    
    private ExprNode parseExpressionAtPrecedence(int minPrecedence) {
        ExprNode left = parsePrimary();
        
        while (true) {
            Token token = peek();
            if (token.type != TokenType.OPERATOR) break;
            
            BinaryOpNode.BinaryOperator op = getBinaryOperator(token.text);
            if (op == null || op.getPrecedence() < minPrecedence) break;
            
            consume();
            ExprNode right = parseExpressionAtPrecedence(op.getPrecedence() + 1);
            left = new BinaryOpNode(op, left, right, token.position);
        }
        
        return left;
    }
    
    private ExprNode parsePrimary() {
        Token token = peek();
        
        return switch (token.type) {
            case NUMBER -> {
                consume();
                yield new ConstantNode(Integer.parseInt(token.text), token.position);
            }
            case IDENTIFIER -> parseIdentifier();
            case LEFT_PAREN -> {
                consume();
                ExprNode expr = parseExpression();
                if (peek().type != TokenType.RIGHT_PAREN) {
                    throw new ExpressionException(
                        String.format("Unclosed parenthesis, started at position %d",
                            token.position));
                }
                consume();
                yield expr;
            }
            case OPERATOR -> {
                if (isUnaryOperator(token.text)) yield parseUnaryOp();
                else throw new ExpressionException(
                    String.format("Unexpected operator '%s' at position %d",
                        token.text, token.position));
            }
            default -> throw new ExpressionException(
                String.format("Unexpected token '%s' at position %d",
                    token.text, token.position));
        };
    }
    
    private ExprNode parseIdentifier() {
        Token token = consume();
        
        // Handle argument references ($0, $1, etc.)
        if (token.text.startsWith("$")) {
            return new ArgumentNode(token.text, token.position);
        }
        
        // Handle variables with postfix operators
        VariableNode var = new VariableNode(token.text, token.position);
        Token next = peek();
        
        if (next.type == TokenType.OPERATOR) {
            return switch (next.text) {
                case "++" -> { consume(); yield var.makePostfixIncrement(); }
                case "--" -> { consume(); yield var.makePostfixDecrement(); }
                default -> var;
            };
        }
        
        return var;
    }
    
    private ExprNode parseUnaryOp() {
        Token op = consume();
        ExprNode operand = parsePrimary();
        
        if (operand instanceof VariableNode varNode) {
            return switch (op.text) {
                case "++" -> varNode.makePrefixIncrement();
                case "--" -> varNode.makePrefixDecrement();
                default -> createUnaryOp(op, operand);
            };
        }
        
        return createUnaryOp(op, operand);
    }
    
    private ExprNode createUnaryOp(Token op, ExprNode operand) {
        return switch (op.text) {
            case "~" -> UnaryOpNode.createBitwiseNot(operand, op.position);
            case "!" -> UnaryOpNode.createLogicalNot(operand, op.position);
            case "+" -> UnaryOpNode.createPlus(operand, op.position);
            case "-" -> UnaryOpNode.createMinus(operand, op.position);
            default -> throw new ExpressionException(
                String.format("Unknown unary operator '%s' at position %d",
                    op.text, op.position));
        };
    }
    
    private Token peek() {
        return tokens.get(Math.min(tokenIndex, tokens.size() - 1));
    }
    
    private Token consume() {
        return tokens.get(tokenIndex++);
    }
    
    private static boolean isIdentifierStart(char c) {
        return Character.isLetter(c) || c == '_' || c == '$';
    }
    
    private static boolean isIdentifierPart(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }
    
    private static boolean isOperatorStart(char c) {
        return "+-*/%<>=!&|^~?:".indexOf(c) >= 0;
    }
    
    private static boolean isTwoCharOperator(String op) {
        return switch (op) {
            case "++", "--", "<<", ">>", "*=", "/=", "%=", "+=",
                 "-=", "&=", "|=", "^=", "~=", "==", "!=" -> true;
            default -> false;
        };
    }
    
    private static boolean isOneCharOperator(String op) {
        return switch (op) {
            case "+", "-", "*", "/", "%", "&", "|", "^",
                 "~", "?", ":", "<", ">", "=" -> true;
            default -> false;
        };
    }
    
    private static boolean isUnaryOperator(String op) {
        return switch (op) {
            case "++", "--", "~", "!", "+", "-" -> true;
            default -> false;
        };
    }
    
    private static BinaryOpNode.BinaryOperator getBinaryOperator(String symbol) {
        for (BinaryOpNode.BinaryOperator op : BinaryOpNode.BinaryOperator.values()) {
            if (op.getSymbol().equals(symbol)) {
                return op;
            }
        }
        return null;
    }
}