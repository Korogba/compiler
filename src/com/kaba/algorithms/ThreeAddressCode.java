package com.kaba.algorithms;

import com.kaba.helper.BinaryTree;
import com.kaba.helper.Quadruples;
import com.kaba.helper.Regex;
import com.kaba.helper.Triples;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Class that contains the algorithm to convert an expression to different variations of three address code: Created by Yusuf on 10/3/2016.
 */
public class ThreeAddressCode {
    private Stack<String> threeAddress = new Stack<>();
    private Stack<Quadruples> quadruples = new Stack<>();
    private Stack<Triples> triples = new Stack<>();
    private int labelCount = 0;
    private Stack<String> statements = new Stack<>();

    public List<String> getThreeAddress(BinaryTree<String> statement) {
        threeAddressFromBinaryTree(statement);
        return threeAddress;
    }

    public List<Quadruples> getQuadruples(BinaryTree<String> statement) {
        quadruplesFromBinaryTree(statement);
        return quadruples;
    }

    public List<Triples> getTriples(BinaryTree<String> statement) {
        triplesFromBinaryTree(statement);
        return triples;
    }

    public Stack<String> getStatements() {
        return statements;
    }

    /**
     * Return an equivalent NFA fragment of a postfix expression
     * Inputs: Queue that contains postfix expression
     * Output: NFA fragment
     * Algorithm:
     * Read a token.
     *
     * Exit
     */
    public List<BinaryTree<String>> generateThreeAddressCode(String statements) throws IllegalArgumentException {
        if(!Pattern.matches(".*;$", statements)){
            throw new IllegalArgumentException("Please enter a string terminated with ';' ");
        }
        statements = statements.replaceAll("\\s", "");
        String[] splitStatements = statements.split(";");
        List<BinaryTree<String>> addressCodes = new LinkedList<>();
        for(String statement: splitStatements) {
            Queue<String> tokens = Regex.infixToPostfixString(statement);
            addressCodes.add(postfixToBinaryTree(tokens));
            getStatements().push(statement);
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

    /**
     * Return true if token is actually a unary operation with a either a +/- and any operand
     */
    private static boolean isMergedUnary(String token) {
        Pattern merged = Pattern.compile("[+|-].*");
        return merged.matcher(token).matches();
    }

    /**
     * Return three address code from a BinaryTree (com.kaba.helper.BinaryTree)
     * Uses recursion to traverse/visit each node in the binary tree
     * Take not that internal nodes are operators and leaf nodes are operands
     */
    private String threeAddressFromBinaryTree(BinaryTree<String> binaryTree){
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

    /**
     * Return quadruple representation from a BinaryTree (com.kaba.helper.BinaryTree)
     * Uses recursion to traverse/visit each node in the binary tree
     * Take not that internal nodes are operators and leaf nodes are operands
     */
    private String quadruplesFromBinaryTree(BinaryTree<String> binaryTree){
        if(binaryTree == null){
            return "";
        }
        else if(binaryTree.isLeafNode()){
            return binaryTree.getElement();
        }
        else {
            if(binaryTree.getElement().equals("=")){
                String label = generateLabelNumber();
                Quadruples current = new Quadruples(quadruplesFromBinaryTree(binaryTree.getRight()), quadruplesFromBinaryTree(binaryTree.getLeft()), binaryTree.getElement(), label);
                quadruples.push(current);
                return "";
            }
            else {
                String label = generateLabelNumber();
                Quadruples current = new Quadruples(quadruplesFromBinaryTree(binaryTree.getRight()), quadruplesFromBinaryTree(binaryTree.getLeft()), binaryTree.getElement(), label);
                quadruples.push(current);
                return label;
            }
        }
    }

    /**
     * Return triple representation from a BinaryTree (com.kaba.helper.BinaryTree)
     * Uses recursion to traverse/visit each node in the binary tree
     * Take not that internal nodes are operators and leaf nodes are operands
     */
    private String triplesFromBinaryTree(BinaryTree<String> binaryTree){
        if(binaryTree == null){
            return "";
        }
        else if(binaryTree.isLeafNode()){
            return binaryTree.getElement();
        }
        else {
            if(binaryTree.getElement().equals("=")){
                String label = generateLabelNumber();
                Triples current = new Triples(label, binaryTree.getElement(), triplesFromBinaryTree(binaryTree.getLeft()), triplesFromBinaryTree(binaryTree.getRight()));
                triples.push(current);
                return "";
            }
            else {
                String label = generateLabelNumber();
                Triples current = new Triples(label, binaryTree.getElement(), triplesFromBinaryTree(binaryTree.getLeft()), triplesFromBinaryTree(binaryTree.getRight()));
                triples.push(current);
                return label;
            }
        }
    }

    /**
     * Generate fancy? label name for Triple address code
     */
    private String generateLabelNumber() {
        labelCount++;
        return "t" + labelCount;
    }
}
