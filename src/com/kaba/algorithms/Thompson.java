package com.kaba.algorithms;

import com.kaba.helper.Fragment;
import com.kaba.helper.Regex;

import java.util.*;

/**
 * Class that implements conversion of Regexp tp NFA based on the Thompson algorithm: Created by Kaba Yusuf on 10/1/2016.
 */
public class Thompson {
    /**
     *  Return an equivalent NFA fragment of a postfix expression
     *  Inputs: Queue that contains postfix expression
     *  Output: NFA fragment
     *  Algorithm:
     *  Read a token.
     *      If the token is a unary operator, o1, then:
     *          Pop the operand from the top of the stack
     *          apply the operator 01 to the operand
     *          push the result on the stack
     *      If token is a binary operator , 01, then:
     *          pop the top two operands from the top of the stack
     *          apply operator 02 to both
     *          push result to the top of the stack
     *      If the token is an operator
     *          push the operand to the top of the stack
     *
     *  When there are no more tokens to read:
     *      pop the single operand from the stack and return as result
     *
     *  Exit
     */
    public static Fragment postfixToNFA(Queue<Character> queue) throws EmptyStackException {
        Stack<Fragment> workingContainer = new Stack<>();
        while(!queue.isEmpty()) {
            char token = queue.remove();
            if(Regex.isOperator(token)){
                Fragment result;
                if(Regex.isUnary(token)){
                    Fragment operand = workingContainer.pop();
                    result = performAppropriateOperation(token, operand);
                } else {
                    Fragment secondOperand = workingContainer.pop();
                    Fragment firstOperand = workingContainer.pop();
                    result = performAppropriateOperation(token, firstOperand, secondOperand);
                }
                workingContainer.push(result);
            } else {
                workingContainer.push(basic(token));
            }
        }
        return workingContainer.pop();
    }
    /**
     * Takes a unary character operator and performs the operation on the fragment operand
     */
    private static Fragment performAppropriateOperation(char token, Fragment operand) {
        switch (token) {
            case '*':
                return zeroOrMore(operand);
            case '+':
                return oneOrMore(operand);
            case '?':
                return oneOrNothing(operand);
            default:
                return null;
        }
    }
    /**
     * Takes a binary character operator and performs the operation on the fragment operands: firstOperand and secondOperand
     */
    private static Fragment performAppropriateOperation(char token, Fragment firstOperand, Fragment secondOperand) {
        switch (token) {
            case '|':
                return alternate(firstOperand, secondOperand);
            case '.':
                return concatenate(firstOperand, secondOperand);
            default:
                return null;
        }
    }

    /**
     * Start a building block //Remove ε transition to final for buildingBlock
     * Make the start state of the building block transition via ε to the operand
     * Make a transition from 'final' of operand to 'start' of operand
     * Set the label of start states of the operand
     * Make a transition from 'final' of operand to 'start' of operand
     * Make the final states of the operand transition via ε to a new final state
     * Set the label of final states of the operand
     * Return buildingBlock
     */
    private static Fragment oneOrMore(Fragment operand) {
        Fragment buildingBlock = new Fragment('\u03B5');
        buildingBlock.getStartState().removeAll();

        operand.transitionFromFinalToStart();
        buildingBlock.epsilonTransitionToFragmentViaStart(operand);
        buildingBlock.setFinalStateViaEpsilon(operand);

        return buildingBlock;
    }

    /**
     * Start a building block
     * Make the start state of the building block transition via ε to the operand
     * Make a transition from 'final' of operand to 'start' of operand
     * Set the label of start states of the operand
     * Make the final states of the operand transition via ε to a new final state
     * Set the label of final states of the operand
     * Return buildingBlock
     */
    private static Fragment zeroOrMore(Fragment operand) {
        Fragment buildingBlock = new Fragment('\u03B5');

        operand.transitionFromFinalToStart();
        buildingBlock.epsilonTransitionToFragmentViaStart(operand);
        buildingBlock.setFinalStateViaEpsilon(operand);

        return buildingBlock;
        /*
        Initial zeroOrMore: Find out problem- Causes StackOverFlow Exception (apt ain't it? :-) )
        State newStart = new State('S', new HashMap<>());
        State newFinal = new State('F', new HashMap<>());

        //Set up new start
        List<State> stateList = new LinkedList<>(Arrays.asList(operand.getStartState(), newFinal));
        HashMap<Character, List<State>> transitions = new HashMap<>();
        transitions.put('\u03B5', stateList);
        newStart.updateTransitions(transitions);

        //Set up pseudo-last
        operand.getFinalState().updateTransitions(transitions);

        //Update start and final
        operand.setStartState(newStart);
        operand.setFinalState(newFinal);
        operand.getStates().addAll(new LinkedList<>(Arrays.asList(newStart, newFinal)));
        return operand;*/
    }

    /**
     * Start a building block //Remove ε transition to final for buildingBlock
     * Make the start state of the building block transition via ε to the operand
     * Make a transition from 'final' of operand to 'start' of operand
     * Set the label of start states of the operand
     * Make the final states of the operand transition via ε to a new final state
     * Set the label of final states of the operand
     * Return buildingBlock
     */
    private static Fragment oneOrNothing(Fragment operand) {
        operand.transitionFromStartToFinal();
        return operand;
    }

    /**
     * Start a building block
     * Make the start state of the building block transition via ε to the two fragments
     * Set the label of start states of the two fragments
     * Make the final states of the two fragments transition via ε to a new final state
     * Set the label of final states of the two fragments
     * Return buildingBlock
     */
    private static Fragment alternate(Fragment first, Fragment second) {
        Fragment buildingBlock = new Fragment('\u03B5');
        buildingBlock.getStartState().removeAll();

        buildingBlock.epsilonTransitionToFragmentViaStart(first);
        buildingBlock.epsilonTransitionToFragmentViaStart(second);

        buildingBlock.setFinalStateViaEpsilon(first);
        buildingBlock.setFinalStateViaEpsilon(second);

        return buildingBlock;
    }

    /**
     * Make the final state of first equal the start state of second
     * Make the final state of the second the final state
     * Concatenate the set of states
     * Return second
     */
    private static Fragment concatenate(Fragment first, Fragment second) {
        first.concatenate(second);
        return second;
    }

    /**
     * Returns a basic fragment that contains a start and final state and a transition via input from start to final
     */
    private static Fragment basic (Character input){
        return new Fragment(input);
    }
}
