package com.kaba;

import com.kaba.algorithms.SubsetConstruction;
import com.kaba.algorithms.Thompson;
import com.kaba.helper.Fragment;
import com.kaba.helper.Regex;

import java.util.Queue;
import java.util.Scanner;

/**
 * Main method class created by Kaba Yusuf on 9/30/2016.
 */
public class Main {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.print("Enter input string: ");
        while(true) {
            try {
                String regex = input.nextLine();
                Queue<Character> queue = Regex.infixToPostfix(regex);
                //System.out.println(queue);
                Fragment NFA = Thompson.postfixToNFA(queue);
                //System.out.println(NFA);
                System.out.println(SubsetConstruction.subsetConstruction(NFA));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getLocalizedMessage());
                System.out.print("Enter another input string: ");
            }
        }
    }
}
