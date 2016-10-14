package com.kaba.automata;

import com.kaba.helper.DFAState;
import com.kaba.helper.State;

import java.util.*;

/**
 * Deterministic Finite Automaton: Created by Kaba Yusuf on 9/30/2016.
 */
public class DFA {
    private Set<DFAState> states;
    private DFAState startState;
    private List<DFAState> finalStates;

    public DFA(DFAState startState) {
        this.startState = startState;
        states = new HashSet<>(Collections.singletonList(startState));
        finalStates = new LinkedList<>();
    }

    public List<DFAState> getFinalStates() {
        return finalStates;
    }

    public Set<DFAState> getStates() {
        return states;
    }

    public DFAState getStartState() {
        return startState;
    }

    public DFAState returnStateFromCharacter(char label) {
        for(DFAState state : states) {
            if(state.getLabel() == label){
                return state;
            }
        }
        return null;
    }

    /**
     * Returns true of the DFA already contains this list of states as a state.
     */
    public boolean exists(List<State> state){
        boolean exists = false;
        for (DFAState entry : states) {
            if(entry.getComponentStates().equals(state)){
                exists = true;
                break;
            }
        }
        return exists;
    }

    /**
     * Adds a DFAState as a state to the states Set
     */
    public void add(DFAState state){
        if(!exists(state.getComponentStates())) {
            states.add(state);
        }
    }

    /**
     * Given a list of states, return the DFAState that is a component of OR return null
     * Should be called when it is confirmed via boolean exists(List<State> state) that dTran exists
     */
    public DFAState getStateFromList(List<State> dTran) {
        for(DFAState state: states){
            if(state.getComponentStates().equals(dTran)){
                return state;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "DFA: " +
                "\nstates: \n" + printStates() +
                "startState: " + startState +
                "\nfinalState: " + finalStates +
                "\nEnd Of DFA";
    }

    /**
     * Private method to return a properly formatted string for the set of states to be used in the toString method
     */
    private String printStates(){
        if(states.isEmpty()){
            return "Empty";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (DFAState state : states) {
                stringBuilder.append(state).append("\n");
            }
            return stringBuilder.toString();
        }
    }
}
