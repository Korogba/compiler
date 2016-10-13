package com.kaba.helper;

import java.util.*;
import java.util.regex.Pattern;

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

        Pattern merged = Pattern.compile("(^[(]?[abcde][.+*|?()])([.|()]?)([abcde][.+*|?()][*+?.|()]*)*([abcde][*+?.|)]?[*+?.|)]?)$");
        if(generateInputSymbol(input).length > 5) {
            throw new IllegalArgumentException("You entered an invalid regular expression: Too much input alphabets");
        }

        if(!merged.matcher(input).matches()) {
            throw new IllegalArgumentException("You entered an invalid regular expression: Invalid regular expression");
        }

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
     * Returns a the count of unique characters in an input
     * Reference: http://stackoverflow.com/a/22597587/6808335
     */
    private static char[] generateInputSymbol(String input) {
        ArrayList<Character> inputAlphabet = new ArrayList<>();
        int[] inputChar = input.chars().distinct().toArray();
        for(int singleInput : inputChar){
            if(!isOperator((char) singleInput)) {
                inputAlphabet.add((char) singleInput);
            }
        }
        char[] alphabets = new char[inputAlphabet.size()];
        for(int i = 0; i < inputAlphabet.size(); i++) {
            alphabets[i] = inputAlphabet.get(i);
        }
        Fragment.setInputSymbol(alphabets);
        return alphabets;
    }

    private static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))"; //Reference: http://stackoverflow.com/questions/2206378/how-to-split-a-string-but-also-keep-the-delimiters

    /**
     * Similar to static Queue<Character> infixToPostfix(String input) but returns a Queue of Strings not characters
     * Convert and return equivalent postfix expression of input: Note that this method does some validity checks on the string
     * It ensures that the expression follows some basic algebraic conventions such as no two operators can follow
     * themselves except + and - and when they do, it resolves the conflict by assigning a "-" to different operators and
     * a "+" to the same operators
     * Uses Regular Expressions to split input string into tokens delineated by any of the operators: +, -, *, /, (, )
     */
    public static Queue<String> infixToPostfixString(String input) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Stack<String> stack = new Stack<>();
        Queue<String> postFix = new LinkedList<>();
        input = input.replaceAll("\\s", "");
        String[] tokens = input.split(String.format(WITH_DELIMITER, "[=|\\+|\\*|\\-|/|)|(]+")); //Reference: http://stackoverflow.com/questions/2206378/how-to-split-a-string-but-also-keep-the-delimiters
        String token;
        int opCount = 1;
        for(int i = 0; i < tokens.length; i++) {
            token = tokens[i];
            if(isOperator(token)){
                opCount++;
                if(opCount > 2){
                    throw new IllegalArgumentException("The expression is not properly formatted.");
                }
                if(isOperator(tokens[i + 1]) && ((tokens[i + 1].equals("*") || tokens[i + 1].equals("/")) && (token.equals("*") || token.equals("/")))){
                    throw new IllegalArgumentException("The expression is not properly formatted.");
                }
                else if(isOperator(tokens[i + 1]) && ((tokens[i + 1].equals("*") || tokens[i + 1].equals("/")) && (token.equals("+") || token.equals("-")))){
                    throw new IllegalArgumentException("The expression is not properly formatted.");
                }
                else if(isOperator(tokens[i + 1]) && ((tokens[i + 1].equals("+") || tokens[i + 1].equals("-")) && (token.equals("*") || token.equals("/")))){
                    tokens[i + 2] = tokens[i + 1] + tokens[i + 2];
                    i++;
                }
                if(isOperator(tokens[i + 1])){
                    if(!token.equals(tokens[i + 1])){
                        token = "-";
                    } else {
                        token = "+";
                    }
                    i++;
                }
                while(!stack.isEmpty() && precedence(token) < precedence(stack.peek())){
                    postFix.add(stack.pop());
                }
                stack.push(token);
            }

            else if(token.equals("(")){
                opCount = 1;
                if(isOperator(tokens[i + 1]) && (tokens[i + 1].equals("*") || tokens[i + 1].equals("/"))){
                    throw new IllegalArgumentException("The expression is not properly formatted.");
                }
                stack.push(token);
            }
            else if(token.equals(")")){
                opCount = 1;
                if(isOperator(tokens[i - 1])){
                    throw new IllegalArgumentException("The expression is not properly formatted.");
                }
                try {
                    while (!stack.peek().equals("(")) {
                        postFix.add(stack.pop());
                    }
                    stack.pop();
                } catch (EmptyStackException e){
                    throw new IllegalArgumentException("You entered an invalid expression: Missing braces");
                }
            } else {
                opCount = 1;
                postFix.add(token);
            }
        }
        while(!stack.empty()) {
            if(stack.peek().equals(")") || stack.peek().equals("(")) {
                throw new IllegalArgumentException("You entered an invalid expression: Missing braces");
            }
            postFix.add(stack.pop());
        }

        return postFix;
    }

    /**
     * Return true if String token is an operator: *, +, ?, ., |
     */
    public static boolean isOperator(String token) {
        return token.equals("=") || token.equals("+") || token.equals("*") || token.equals("/") || token.equals("-");
    }

    /**
     * Return the precedence level of char token
     * Unary operators: *, +, ? have highest precedence followed by concatenation operator: . and finally alternation operator: |
     */
    private static int precedence(String token) {
        switch (token) {
            case "/":
                return 4;
            case "*":
                return 3;
            case "+":
                return 2;
            case "-":
                return 2;
            case "=":
                return 1;
            default:
                return 0;
        }
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
