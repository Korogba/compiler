package com.kaba.algorithms;

import com.kaba.helper.BinaryTree;
import com.kaba.helper.Quadruples;
import com.kaba.helper.Regex;
import com.kaba.helper.Triples;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that contains the algorithm to convert an expression to three address code: Created by Yusuf on 10/3/2016.
 */
public class ThreeAddressCode {
    private static Stack<String> threeAddress = new Stack<>();
    private static Stack<Quadruples> quadruples = new Stack<>();
    private static Stack<Triples> triples = new Stack<>();
    private static int labelCount = 0;

    public static List<String> getThreeAddress(BinaryTree<String> statement) {
        threeAddressFromBinaryTree(statement);
        return threeAddress;
    }

    public static Stack<Quadruples> getQuadruples(BinaryTree<String> statement) {
        quadruplesFromBinaryTree(statement);
        return quadruples;
    }

    public static Stack<Triples> getTriples(BinaryTree<String> statement) {
        triplesFromBinaryTree(statement);
        return triples;
    }

    /**
     *  Return an equivalent NFA fragment of a postfix expression
     *  Inputs: Queue that contains postfix expression
     *  Output: NFA fragment
     *  Algorithm:
     *  Read a token.
     *
     *  Exit
     */
    public static List<BinaryTree<String>> generateThreeAddressCode(String statements) throws IllegalArgumentException {
        if(!Pattern.matches(".*;$", statements)){
            throw new IllegalArgumentException("Please enter a string terminated with ';' ");
        }
        statements = statements.replaceAll("\\s", "");
        String[] splitStatements = statements.split(";");
        List<BinaryTree<String>> addressCodes = new LinkedList<>();
        for(String statement: splitStatements) {
            Queue<String> tokens = Regex.infixToPostfixString(statement);
            addressCodes.add(postfixToBinaryTree(tokens));
        }
        return addressCodes;
    }

    /**
     *  Return an equivalent BinaryTree of a postfix expression
     *  Inputs: Queue that contains postfix expression
     *  Output: BinaryTree
     *  Algorithm: (Same as Fragment postfixToNFA(Queue<Character> queue) throws EmptyStackException)
     *  Read a token.
     *      If token is an operator , 01, then:
     *          pop the top two operands from the top of the stack
     *          apply operator 02 to both by making them children of the operator node
     *          push result to the top of the stack
     *      If the token is an operator
     *          push the operand to the top of the stack
     *
     *  When there are no more tokens to read:
     *      pop the single operand from the stack and return as result
     *
     *  Exit
     */
    private static BinaryTree<String> postfixToBinaryTree(Queue<String> queue) throws EmptyStackException, IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Stack<BinaryTree<String>> workingContainer = new Stack<>();
        while(!queue.isEmpty()) {
            String token = queue.remove();
            if(Regex.isOperator(token)) {
                BinaryTree<String> result;
                BinaryTree<String> secondOperand = workingContainer.pop();
                if(!workingContainer.isEmpty()) {
                    BinaryTree<String> firstOperand = workingContainer.pop();
                    result = new BinaryTree<>(token, firstOperand, secondOperand);
                    workingContainer.push(result);
                }
            } else {
                if(isMergedUnary(token)){
                    char[] splitToken = token.toCharArray();
                    if(splitToken.length > 2){
                        throw new IllegalArgumentException("Split token in postfixToBinaryTree returns more than two characters: Postfix expression not properly formed!");
                    }
                    BinaryTree<String> unaryTree = new BinaryTree<>(Character.toString(splitToken[0]), new BinaryTree<>(Character.toString(splitToken[1])));
                    workingContainer.push(unaryTree);
                }
                else {
                    workingContainer.push(new BinaryTree<>(token));
                }
            }
        }
        return workingContainer.pop();
    }

    private static boolean isMergedUnary(String token) {
        Pattern merged = Pattern.compile("[+|-].*");
        return merged.matcher(token).matches();
    }

    private static String threeAddressFromBinaryTree(BinaryTree<String> binaryTree){
        if(binaryTree == null){
            return "";
        }
        else if(binaryTree.isLeafNode()){
            return binaryTree.getElement();
        }
        else {
            StringBuilder stringBuilder = new StringBuilder();
            if(binaryTree.getElement().equals("=")){
                threeAddress.push(stringBuilder.append(threeAddressFromBinaryTree(binaryTree.getLeft()))
                        .append(" ")
                        .append(binaryTree.getElement())
                        .append(" ")
                        .append(threeAddressFromBinaryTree(binaryTree.getRight())).toString());
                return "";
            }
            else {
                String label = generateLabelNumber();
                threeAddress.push(stringBuilder.append(label)
                        .append(" = ")
                        .append(" ")
                        .append(threeAddressFromBinaryTree(binaryTree.getLeft()))
                        .append(" ")
                        .append(binaryTree.getElement())
                        .append(" ")
                        .append(threeAddressFromBinaryTree(binaryTree.getRight())).toString());
                return label;
            }
        }
    }

    private static String quadruplesFromBinaryTree(BinaryTree<String> binaryTree){
        if(binaryTree == null){
            return "";
        }
        else if(binaryTree.isLeafNode()){
            return binaryTree.getElement();
        }
        else {
            if(binaryTree.getElement().equals("=")){
                Quadruples current = new Quadruples(binaryTree.getElement(), quadruplesFromBinaryTree(binaryTree.getLeft()), quadruplesFromBinaryTree(binaryTree.getRight()));
                quadruples.push(current);
                return "";
            }
            else {
                String label = generateLabelNumber();
                Quadruples current = new Quadruples(quadruplesFromBinaryTree(binaryTree.getLeft()), quadruplesFromBinaryTree(binaryTree.getRight()), binaryTree.getElement(), label);
                quadruples.push(current);
                return label;
            }
        }
    }

    private static String triplesFromBinaryTree(BinaryTree<String> binaryTree){
        if(binaryTree == null){
            return "";
        }
        else if(binaryTree.isLeafNode()){
            return binaryTree.getElement();
        }
        else {
            if(binaryTree.getElement().equals("=")){
                generateLabelNumber();
                Triples current = new Triples(labelCount, binaryTree.getElement(), triplesFromBinaryTree(binaryTree.getLeft()), triplesFromBinaryTree(binaryTree.getRight()));
                triples.push(current);
                return "";
            }
            else {
                generateLabelNumber();
                Triples current = new Triples(labelCount, binaryTree.getElement(), triplesFromBinaryTree(binaryTree.getLeft()), triplesFromBinaryTree(binaryTree.getRight()));
                triples.push(current);
                return labelCount + "";
            }
        }
    }

    private static String generateLabelNumber() {
        labelCount++;
        return "t" + labelCount;
    }
}
