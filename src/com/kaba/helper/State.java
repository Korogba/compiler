package com.kaba.helper;

import java.util.*;

/**
 * State defines each state in the NFA/Fragment: Created by Kaba Yusuf on 9/30/2016.
 * Each state has a char label and a HashMap of transitions linking an input alphabet to a list of States that it transitions
 * to via that alphabet
 */
public class State {
    private char label;
    private HashMap<Character, List<State>> transitions;
    private static int labelCount = 65;

    State(char label) {
        this.label = label;
        transitions = new HashMap<>();
    }

    char getLabel() {
        return label;
    }

    /**
     * Apart from the start and final states, State labels are set using a static (class-wide) variable: labelCount
     * to determine what character label is available and avoid special special/reserved characters such as 'S', '^'
     */
    void setLabel() {
        if(91 <= labelCount && labelCount <= 96) {
            labelCount = 97;
        }
        if(labelCount == 70 || labelCount == 83){
            labelCount++;
        }
        this.label = (char)labelCount;
        labelCount++;
    }

    private HashMap<Character, List<State>> getTransitions() {
        return transitions;
    }

    void setTransitions(HashMap<Character, List<State>> transitions) {
        this.transitions = transitions;
    }

    /**
     * Given a HashMap of transitions, add the updates to the existing transitions
     * If the input character already exists, update the its list of states
     * If the input character does not exist, add a new entry to the transitions
     */
    void updateTransitions(HashMap<Character, List<State>> updates){
        for (Map.Entry<Character, List<State>> entry : updates.entrySet()) {
            if(this.getTransitions().containsKey(entry.getKey())) {
                List<State> states = getTransitions().get(entry.getKey());
                states.addAll(entry.getValue());
                getTransitions().replace(entry.getKey(), states);
            } else {
                this.getTransitions().put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Return formatted String for transitions to be used in the toString method
     */
    private String printTransitions(){
        if(transitions.isEmpty()){
            return "Empty";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<Character, List<State>> entry : transitions.entrySet()) {
                stringBuilder.append(entry.getKey()).append(": ");
                for(State state : entry.getValue()) {
                    stringBuilder.append(state.getLabel()).append(", ");
                }
            }
            return stringBuilder.toString();
        }
    }

    /**
     * Get transition entries that move to finalState and link them to startState instead
     * Used in concatenation to merge the final states of the first fragment to the start state of the second fragment
     */
    void moveTransitionsFromFinalTo(State startState, State finalState) {
        transitions.entrySet().stream().filter(entry -> entry.getValue().contains(finalState)).forEach(entry -> {
            List<State> states = entry.getValue();
            states.add(startState);
            states.remove(finalState);
            transitions.replace(entry.getKey(), entry.getValue(), states);
        });
    }

    /**
     * empty contents of transitions
     */
    public void removeAll() {
        transitions.clear();
    }

    /**
     * return true if this state contains a transition to state via character token
     */
    boolean containsTransitionTo(State state, Character token){
        return (this.getTransitions().containsKey(token) && this.getTransitions().get(token) == state);
    }

    /**
     * return true if this state contains a transition to state via ANY input character
     */
    boolean containsTransitionTo(State state){
        for (Map.Entry<Character, List<State>> entry : transitions.entrySet()) {
            if(entry.getValue().contains(state)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "State {label: " + label + ", transitions: " + printTransitions() +"}";
    }

    /**
     * Return a list of States reachable from this state via ε
     * This method uses recursion as the ε-closure of a state that contains a transition
     * to another state via ε is just a combination of that state and the state's ε-closure
     * Used in the SubsetConstruction algorithm
     */
    public LinkedList<State> getEClosure() {
        LinkedList<State> eClosure = new LinkedList<>();
        eClosure.add(this);
        if(!this.getTransitions().containsKey('\u03B5')){
            return eClosure;
        }
        for(State state : this.getTransitions().get('\u03B5')){
            eClosure.addAll(state.getEClosure());
        }
        return eClosure;
    }

    /**
     * Similar method to getEClosure but gets only the states that can be directly visited via input from this state
     * Used in the SubsetConstruction algorithm
     */
    public LinkedList<State> getStatesTransitionViaInput(char input) {
        LinkedList<State> statesTransition = new LinkedList<>();
        if(!this.getTransitions().containsKey(input)){
            return statesTransition;
        }
        statesTransition.addAll(this.getTransitions().get(input));
        return statesTransition;
    }
    /**
     * Reset label after each NFA or DFA is generated!
     */
    public static void resetLabelCount(){
        labelCount = 65;
    }
}
