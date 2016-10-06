package com.kaba.algorithms;

import com.kaba.helper.BinaryTree;
import com.kaba.helper.Regex;

import java.util.*;
import java.util.regex.Pattern;

import static javafx.scene.input.KeyCode.T;

/**
 * Class that contains the algorithm to convert an expression to three address code: Created by Yusuf on 10/3/2016.
 */
public class ThreeAddressCode {
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
    private static BinaryTree<String> postfixToBinaryTree(Queue<String> queue) throws EmptyStackException {
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
                workingContainer.push(new BinaryTree<>(token));
            }
        }
        return workingContainer.pop();
    }

    public static String threeAddressFromBinaryTree(BinaryTree<String> binaryTree){
        StringBuilder threeAddressCodes = new StringBuilder();
        if(binaryTree == null){
            return "";
        }
        if(binaryTree.getLeft() != null) {
            threeAddressCodes.append(threeAddressFromBinaryTree(binaryTree.getLeft()));
        }
        threeAddressCodes.append(binaryTree.getElement()).append("\t");
        if(binaryTree.getRight() != null) {
            threeAddressCodes.append(threeAddressFromBinaryTree(binaryTree.getRight())).append(";\n");
        }
        return threeAddressCodes.toString();
    }
}
