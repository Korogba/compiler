package com.kaba.helper;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Class for Regular Expression Methods: Created by Kaba Yusuf on 9/30/2016.
 */
public class Regex {

    /**
     * Convert and return equivalent postfix string of input: Note that this method does no validity checks on the string
     * so caller must ensure that a properly formatted regular expression is passed as input
     *
     * Algorithm: Dijkstra's Shunting yard algorithm: https://repository.cwi.nl/noauth/search/fullrecord.php?publnr=9251
     *  Read a token.
     *      If the token is a number, then add it to the output queue.
     *      If the token is a function token, then push it onto the stack.
     *      If the token is a function argument separator (e.g., a comma):
     *      Until the token at the top of the stack is a left parenthesis, pop operators off the stack onto the output queue. If no left parentheses are encountered, either the separator was misplaced or parentheses were mismatched.
     *      If the token is an operator, o1, then:
     *
     *          while there is an operator token o2, at the top of the operator stack and either
     *              o1 is left-associative and its precedence is less than or equal to that of o2, or
     *              o1 is right associative, and has precedence less than that of o2,
     *                  pop o2 off the operator stack, onto the output queue;
     *          at the end of iteration push o1 onto the operator stack.
     *
     *      If the token is a left parenthesis (i.e. "("), then push it onto the stack.
     *
     *      If the token is a right parenthesis (i.e. ")"):
     *
     *          Until the token at the top of the stack is a left parenthesis, pop operators off the stack onto the output queue.
     *          Pop the left parenthesis from the stack, but not onto the output queue.
     *          If the token at the top of the stack is a function token, pop it onto the output queue.
     *          If the stack runs out without finding a left parenthesis, then there are mismatched parentheses.
     *
     *  When there are no more tokens to read:
     *
     *      While there are still operator tokens in the stack:
     *          If the operator token on the top of the stack is a parenthesis, then there are mismatched parentheses.
     *          Pop the operator onto the output queue.
     *
     *  Exit
     */
    public static Queue<Character> infixToPostfix(String input) throws IllegalArgumentException {
        Stack<Character> stack = new Stack<>();
        Queue<Character> postFix = new LinkedList<>();
        input = input.replaceAll("\\s", "");

        for (char token: input.toCharArray()) {
            if(isOperator(token) && !isUnary(token)){
                while(!stack.isEmpty() && ((isLeftAssociative(token) && precedence(token) <= precedence(stack.peek())) || (!isLeftAssociative(token) && precedence(token) < precedence(stack.peek()))) ){
                    postFix.add(stack.pop());
                }
                stack.push(token);
            }
            else if(isOperator(token) && isUnary(token)){
                while(!stack.isEmpty() && ((isLeftAssociative(token) && precedence(token) <= precedence(stack.peek())) || (!isLeftAssociative(token) && precedence(token) < precedence(stack.peek()))) ){
                    postFix.add(stack.pop());
                }
                postFix.add(token);
            }
            else if(token == '('){
                stack.push(token);
            }
            else if(token == ')'){
                try {
                    while (stack.peek() != '(') {
                        postFix.add(stack.pop());
                    }
                    stack.pop();
                } catch (EmptyStackException e){
                    throw new IllegalArgumentException("You entered an invalid regular expression: Missing braces");
                }
            } else {
                postFix.add(token);
            }
        }
        while(!stack.empty()) {
            if(stack.peek() == ')' || stack.peek() == '(') {
                throw new IllegalArgumentException("You entered an invalid regular expression: Missing braces");
            }
            postFix.add(stack.pop());
        }

        return postFix;
    }

    /**
     * Return true of char token is a unary operator: *, +, ?
     */
    public static boolean isUnary(char token) {
        switch (token) {
            case '*':
                return true;
            case '+':
                return true;
            case '?':
                return true;
            default:
                return false;
        }
    }

    /**
     * Return the precedence level of char token
     * Unary operators: *, +, ? have highest precedence followed by concatenation operator: . and finally alternation operator: |
     */
    private static int precedence(char token) {
        switch (token) {
            case '+':
                return 3;
            case '*':
                return 3;
            case '?':
                return 3;
            case '.':
                return 2;
            case '|':
                return 1;
            default:
                return 0;
        }
    }

    /**
     * Return true if char token is left associative: ., |
     */
    private static boolean isLeftAssociative(char token) {
        switch (token) {
            case '.':
                return true;
            case '|':
                return true;
            default:
                return false;
        }
    }

    /**
     * Return true if char token is an operator: *, +, ?, ., |
     */
    public static boolean isOperator(char token) {
        return (token == '*' || token == '+' || token == '?' || token == '|' || token == '.');
    }
}
