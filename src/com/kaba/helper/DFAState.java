package com.kaba.helper;

import java.util.*;

/**
 * DFAState (similar to the State) defines each state the DFA: Created by Kaba Yusuf on 9/30/2016.
 * Each state has a char label and a HashMap of transitions linking an input alphabet to a list of States that it transitions
 * to via that alphabetCreated by Yusuf on 10/3/2016
 */
public class DFAState {
    private char label;
    private List<State> componentStates;
    private HashMap<Character, List<Character>> transitions;
    private static int stateLabel = 65;

    public DFAState(List<State> componentStates) {
        this.componentStates = componentStates;
        if(componentStates.isEmpty()){
            this.label = '-';
        } else {
            setLabel();
        }
        transitions = new HashMap<>();
    }

    public DFAState(char label, List<State> componentStates) {
        this.label = label;
        this.componentStates = componentStates;
        transitions = new HashMap<>();
    }

    public char getLabel() {
        return label;
    }

    public HashMap<Character, List<Character>> getTransitions() {
        return transitions;
    }

    private void setLabel() {
        if(91 <= stateLabel && stateLabel <= 96) {
            stateLabel = 97;
        }
        if(stateLabel == 70 || stateLabel == 83){
            stateLabel++;
        }
        label = (char) stateLabel;
        stateLabel++;
    }

    public List<State> getComponentStates() {
        return componentStates;
    }

    /**
     * Given an input alphabet and a character label
     * If the input character already exists, update the its list of labels
     * If the input character does not exist, add a new entry to the transitions
     */
    public void updateTransitions(char input, char label) {
        if(transitions.containsKey(input)){
            transitions.get(input).add(label);
        } else {
            transitions.put(input, Collections.singletonList(label));
        }
    }

    @Override
    public String toString() {
        return "DFAState {label: " + label + " || componentStates: " + printComponentStates() + "|| transitions=" + printTransitions() + '}';
    }

    private String printComponentStates() {
        if(componentStates.isEmpty()){
            return "Empty";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (State state : componentStates) {
                stringBuilder.append(state.getLabel()).append(", ");
            }
            return stringBuilder.toString();
        }
    }

    /**
     * Return formatted String for transitions to be used in the toString method: Based on similar method from State class
     */
    private String printTransitions() {
        if(transitions.isEmpty()){
            return "Empty";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<Character, List<Character>> entry : transitions.entrySet()) {
                stringBuilder.append(entry.getKey()).append(": ");
                for(Character stateLabel : entry.getValue()) {
                    stringBuilder.append(stateLabel).append(", ");
                }
            }
            return stringBuilder.toString();
        }
    }

    /**
     * Reset label after each DFA is generated!
     */
    public static void resetLabelCount() {
        stateLabel = 65;
    }
}
