package com.kaba;

import com.kaba.algorithms.SubsetConstruction;
import com.kaba.algorithms.Thompson;
import com.kaba.algorithms.ThreeAddressCode;
import com.kaba.helper.BinaryTree;
import com.kaba.helper.Fragment;
import com.kaba.helper.Regex;

import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import static javafx.scene.input.KeyCode.F;

/**
 * Main method class created by Kaba Yusuf on 9/30/2016.
 */
public class Main {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.print("Enter input string: ");
        //System.out.println("Enter String to tokenize: ");
        while(true) {
            try {
                String regex = input.nextLine();
//                Queue<Character> queue = Regex.infixToPostfix(regex);
                //System.out.println(queue);
//                Fragment NFA = Thompson.postfixToNFA(queue);
                //System.out.println(NFA);
//                SubsetConstruction.subsetConstruction(NFA);
                //System.out.println(SubsetConstruction.subsetConstruction(NFA));
                //Queue<String> queue = Regex.infixToPostfixString(regex);
                //System.out.println(queue);
                List<BinaryTree<String>> addressCodes = ThreeAddressCode.generateThreeAddressCode(regex);
                for(BinaryTree<String> statement: addressCodes){
                    System.out.println(ThreeAddressCode.getTriples(statement));
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getLocalizedMessage());
                System.out.print("Enter another input string: ");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("The expression is not properly formatted.");
                System.out.print("Enter another input string: ");
            }
        }
    }
}
