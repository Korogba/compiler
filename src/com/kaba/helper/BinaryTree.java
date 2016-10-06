package com.kaba.helper;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Tree Structure to be used in generating intermediate code (Three Address Code): Created by Yusuf on 10/3/2016
 * Reference: http://www1.cs.columbia.edu/~bert/courses/3134/slides/Lecture7.pdf
 */
public class BinaryTree<T> {

    private T element;
    private BinaryTree<T> left;
    private BinaryTree<T> right;

    public BinaryTree(T element, BinaryTree<T> left, BinaryTree<T> right) {
        this.element = element;
        this.left = left;
        this.right = right;
    }

    public BinaryTree(T element) {
        this.element = element;
        left  = null;
        right = null;
    }

    public T getElement() {
        return element;
    }

    public BinaryTree<T> getLeft() {
        return left;
    }

    public BinaryTree<T> getRight() {
        return right;
    }

    //Reference: http://stackoverflow.com/questions/2241513/java-printing-a-binary-tree-using-level-order-in-a-specific-format
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Queue<BinaryTree<T>> current = new LinkedList<>();
        Queue<BinaryTree<T>> next = new LinkedList<>();
        current.add(this);

        while(!current.isEmpty()){
            for(BinaryTree<T> workingTree : current) {
                stringBuilder.append(workingTree.getElement()).append("\t");
                if (workingTree.getLeft() != null) {
                    next.add(workingTree.getLeft());
                }
                if (workingTree.getRight() != null) {
                    next.add(workingTree.getRight());
                }
            }
            stringBuilder.append("\n");
            current = next;
            next = new LinkedList<>();
        }
        return stringBuilder.toString();
    }
}