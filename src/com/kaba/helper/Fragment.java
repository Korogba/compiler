package com.kaba.helper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Fragment represents building blocks/components of NFA or DFA: Created by Kaba Yusuf on 9/30/2016.
 * This class is used by the Thompson algorithm to build up an NFA from a postfix expression
 */
public class Fragment {

    private Set<State> states;
    private State startState;
    private State finalState;
    private static char[] inputAlphabet;

    public Fragment(Character input) {
        //Create Start state
        State startState = new State('S');
        HashMap<Character, List<State>> transitions = new HashMap<>();

        //Create final state
        State finalState = new State('F');

        //Update startStart to point to final state via transitions
        List<State> stateList = new LinkedList<>(Collections.singletonList(finalState));
        transitions.put(input, stateList);
        startState.setTransitions(transitions);

        //Initialize parameters to default values
        this.states = new HashSet<>(Arrays.asList(startState, finalState));
        this.startState = startState;
        this.finalState = finalState;
    }

    Set<State> getStates() {
        return states;
    }

    public State getStartState() {
        return startState;
    }

    private void setStartState(State startState) {
        this.startState = startState;
    }

    public State getFinalState() {
        return finalState;
    }

    private void setFinalState(State finalState) {
        this.finalState = finalState;
    }

    /**
     * Private method to return a properly formatted string for the set of states to be used in the toString method
     */
    private String printStates(){
        if(states.isEmpty()){
            return "Empty";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (State state : states) {
                stringBuilder.append(state).append("\n");
            }
            return stringBuilder.toString();
        }
    }

    @Override
    public String toString() {
        return "Fragment: " +
                "\nstates: \n" + printStates() +
                "\nstartState: " + startState +
                "\nfinalState: " + finalState +
                "\nEnd Of Fragment";
    }

    /**
     * Transition the the fragment parameter from the start of the calling fragment via epsilon
     * The start of the fragment parameter is pointed to the start of the calling fragment
     * This merges the two fragments and is used in the alternation operator of Thompson
     */
    public void epsilonTransitionToFragmentViaStart(Fragment fragment) {
        //Get start state of fragment and add to a list
        List<State> stateList = new LinkedList<>(Collections.singletonList(fragment.getStartState()));

        HashMap<Character, List<State>> transitions = new HashMap<>();
        transitions.put('\u03B5', stateList);
        this.getStartState().updateTransitions(transitions);
        fragment.getStartState().setLabel();
        this.getStates().addAll(fragment.getStates());
    }

    /**
     * Make the final state of the fragment parameter point the final state of the calling fragment
     * Transition via epsilon to the new final state from the previous final state of the fragment parameter
     * This merges the two fragments and is used in the alternation operator of Thompson
     */
    public void setFinalStateViaEpsilon(Fragment fragment) {
        //Get final state of fragment
        State finalState = fragment.getFinalState();

        //Create transition from final state of fragment to final of buildingBlock (calling fragment)
        List<State> stateList = new LinkedList<>(Collections.singletonList(this.getFinalState()));
        HashMap<Character, List<State>> transitions = new HashMap<>();
        transitions.put('\u03B5', stateList);
        finalState.updateTransitions(transitions);

        //Update label of fragment's 'final' state && point fragment's final state to buildingBlock (calling fragment)
        finalState.setLabel();
        fragment.setFinalState(this.getFinalState());
    }

    /*public void setFinalStateViaEpsilon(State finalState) {
        //Get final state of fragment and add to a list
        List<State> stateList = new LinkedList<>(Collections.singletonList(finalState));

        HashMap<Character, List<State>> transitions = new HashMap<>();
        transitions.put('\u03B5', stateList);
        this.getFinalState().updateTransitions(transitions);
        this.getFinalState().setLabel();
        this.getStates().add(finalState);
    }*/

    /**
     * Concatenate the calling fragment with the fragment second
     * This is done by pointing all transitions leading to the final state in the first fragment to the start state in the second
     * The start of the second is then set to the start of the first, the final of the first is also set to the start of the second
     * second fragment is returned
     * This merges the two fragments and is used in the concatenation operator of Thompson
     */
    public void concatenate(Fragment second) {
        LinkedList<State> statesThatTransitionToFinal = this.getStatesThatTransitionToFinal();
        for(State state : statesThatTransitionToFinal){
            state.moveTransitionsFromFinalTo(second.getStartState(), this.getFinalState());
        }
        second.getStartState().setLabel();
        second.setStartState(this.getStartState());
        this.getStates().remove(this.getFinalState());
        this.setFinalState(second.getStartState());
        second.getStates().addAll(this.getStates());
    }

    /**
     * This creates a transition via epsilon from the final state to the start state of the calling fragment
     * This is used in unary operators in Thompson
     */
    public void transitionFromFinalToStart() {
        //Create transition from final state of this to start state of this
        if(this.getFinalState().containsTransitionTo(getStartState(), '\u03B5')){
            return;
        }
        List<State> stateList = new LinkedList<>(Collections.singletonList(this.getStartState()));
        HashMap<Character, List<State>> transitions = new HashMap<>();
        transitions.put('\u03B5', stateList);
        this.getFinalState().updateTransitions(transitions);
    }

    /**
     * This creates a transition via epsilon from the start state to the final state of the calling fragment
     * This is used in unary operators in Thompson
     */
    public void transitionFromStartToFinal() {
        //Create transition from final state of this to start state of this
        if(this.getStartState().containsTransitionTo(getFinalState(), '\u03B5')){
            return;
        }
        List<State> stateList = new LinkedList<>(Collections.singletonList(this.getFinalState()));
        HashMap<Character, List<State>> transitions = new HashMap<>();
        transitions.put('\u03B5', stateList);
        this.getStartState().updateTransitions(transitions);
    }

    /**
     * This is a helper method that returns states that have transitions to the final state
     * This is used in the concatenate method
     */
    private LinkedList<State> getStatesThatTransitionToFinal() {
        return this.getStates().stream().filter(state -> state.containsTransitionTo(this.getFinalState())).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Returns the input alphabets accepted by the language
     */
    public static char[] getInputSymbol() {
        return inputAlphabet;
    }

    /**
     * Returns the input alphabets accepted by the language
     */
    public static void setInputSymbol(char[] setAlphabet) {
        inputAlphabet = setAlphabet;
    }
}
