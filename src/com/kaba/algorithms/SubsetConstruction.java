package com.kaba.algorithms;

import com.kaba.automata.DFA;
import com.kaba.helper.DFAState;
import com.kaba.helper.Fragment;
import com.kaba.helper.State;

import java.util.*;

/**
 * SubsetConstruction algorithm to convert an NFA into a DFA: Created by Yusuf on 10/2/2016
 */
public class SubsetConstruction {
    /**
     * References: http://homepages.gold.ac.uk/nikolaev/
     * Input: NFA Fragment
     * Output: DFA Fragment
     * Initialize: Let ε-closure (s0) be the only state in DStates (of the DFA)
     * Repeat: while there are unmarked states  T  in  DStates  do
     *      mark T
     *      for each input symbol: a  do
     *          U = ε-closure(move(T, a))
     *          if U is not in DStates then
     *              add U as unmarked state in  DStates
     *              If U contains final state of NFA:
     *                  add U to the final states set of DFA
     *          DTran[T, a] = U
     *      end
     * end
     *
     */
    public static DFA subsetConstruction(Fragment NFA){
        Queue<DFAState> dfaStates = new LinkedList<>();
        DFA generatedDFA;

        State startState = NFA.getStartState();
        List<State> startEClosure = startState.getEClosure();

        DFAState start = new DFAState('S', startEClosure);
        generatedDFA = new DFA(start);
        dfaStates.add(start);
        int loopCount = 0;
        while (!dfaStates.isEmpty()) {
            DFAState current = dfaStates.remove();
            for (char input : Fragment.getInputSymbol()) {
                LinkedList<State> dTran = new LinkedList<>();
                DFAState newState;
                for (State move : move(current.getComponentStates(), input)) {
                    move.getEClosure().stream().filter(moveState -> !dTran.contains(moveState)).forEach(dTran::add);
                }
                if(!generatedDFA.exists(dTran)) {
                    newState = new DFAState(dTran);
                    generatedDFA.add(newState);
                    dfaStates.add(newState);
                    if(dTran.contains(NFA.getFinalState())){
                        generatedDFA.getFinalStates().add(newState);
                    }
                } else {
                    newState = generatedDFA.getStateFromList(dTran);
                }
                current.updateTransitions(input, newState.getLabel());
            }
            loopCount++;
            if(loopCount == 15){
                break;
            }
        }
        State.resetLabelCount();
        return generatedDFA;
    }

    private static List<State> move(List<State> current, char input) {
        List<State> moveViaInput = new LinkedList<>();
        for(State state : current){
            moveViaInput.addAll(state.getStatesTransitionViaInput(input));
        }
        return moveViaInput;
    }
}
