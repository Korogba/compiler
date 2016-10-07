package com.kaba;

import com.kaba.algorithms.SubsetConstruction;
import com.kaba.algorithms.Thompson;
import com.kaba.algorithms.ThreeAddressCode;
import com.kaba.helper.BinaryTree;
import com.kaba.helper.Fragment;
import com.kaba.helper.Regex;

import java.util.List;
import java.util.Scanner;

/**
 * Main method class created by Kaba Yusuf on 9/30/2016.
 */
public class Main {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.print("Enter 1 - Regular Expression => NFA => DFA\nEnter 2 - Converting Expressions to TAC, Triples and Quadruples\nEnter x to quit\t");
        while(true) {
            try {
                String selected = input.nextLine();
                if(!selected.equals("1") && !selected.equals("2") && !selected.equals("x")) {
                    throw new IllegalArgumentException("Invalid selection: 1 or 2 or x");
                }
                if(selected.equals("1")){
                    System.out.println("1 - Regular Expression => NFA => DFA");
                    System.out.println("==============================================================================================================================================");
                    System.out.println("Use '*' for zero or more repetitions, '+' for one or more repetitions, '.' for concatenation, '(' and ')' for grouping, '|' for union and '?' for zero or one");
                    System.out.print("Enter properly formatted string: \t");
                    String regex = input.nextLine();
                    Fragment NFA = Thompson.postfixToNFA( Regex.infixToPostfix(regex));
                    System.out.println("==============================================================================================================================================");
                    System.out.println("GENERATED NFA:\n" + NFA);
                    System.out.println("==============================================================================================================================================");
                    System.out.println(SubsetConstruction.subsetConstruction(NFA));
                    System.out.println("==============================================================================================================================================");
                }
                if(selected.equals("2")){
                    System.out.println("2 - Converting Expressions to TAC, Triples and Quadruples");
                    System.out.println("==============================================================================================================================================");
                    System.out.println("Use the following algebraic symbols combined with alphanumeric characters to generate TAC, Quadruples and Triples (from any number of statements): {+, -, *, -, (, ), /}. Statements must be terminated by a semi-colon(;)");
                    System.out.print("Enter properly formatted string: \t");
                    String expression = input.nextLine();
                    System.out.println("==============================================================================================================================================");
                    System.out.println("GENERATED TAC:\n");
                    List<BinaryTree<String>> addressCodes = ThreeAddressCode.generateThreeAddressCode(expression);
                    for(BinaryTree<String> statement: addressCodes){
                        System.out.print("Statement => "+ ThreeAddressCode.getStatements().pop());
                        System.out.println(": " + ThreeAddressCode.getThreeAddress(statement));
                    }
                    System.out.println("==============================================================================================================================================");

                    System.out.println("GENERATED QUADRUPLE:\n");
                    List<BinaryTree<String>> quadrupleStatements = ThreeAddressCode.generateThreeAddressCode(expression);
                    for(BinaryTree<String> statement: quadrupleStatements){
                        System.out.print("Statement => "+ ThreeAddressCode.getStatements().pop());
                        System.out.println(": " + ThreeAddressCode.getQuadruples(statement));
                    }
                    System.out.println("==============================================================================================================================================");

                    System.out.println("GENERATED TRIPLE:\n");
                    List<BinaryTree<String>> tripleStatements = ThreeAddressCode.generateThreeAddressCode(expression);
                    for(BinaryTree<String> statement: tripleStatements){
                        System.out.print("Statement => "+ ThreeAddressCode.getStatements().pop());
                        System.out.println(": " + ThreeAddressCode.getTriples(statement));
                    }
                    System.out.println("==============================================================================================================================================");
                }
                if(selected.equals("x")){
                    System.out.println("You have terminated the program!");
//                    System.exit(0);
                    break;
                }
                System.out.print("Enter 1 - Regular Expression => NFA => DFA\nEnter 2 - Converting Expressions to TAC, Triples and Quadruples\nEnter x to quit\t");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getLocalizedMessage());
                System.out.print("Enter 1 - Regular Expression => NFA => DFA\nEnter 2 - Converting Expressions to TAC, Triples and Quadruples\nEnter x to quit\t");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("The expression is not properly formatted.");
                System.out.print("Enter 1 - Regular Expression => NFA => DFA\nEnter 2 - Converting Expressions to TAC, Triples and Quadruples\nEnter x to quit\t");
            }
        }
    }
}
