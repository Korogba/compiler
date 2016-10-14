package com.kaba.helper;

import com.kaba.automata.DFA;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import java.util.List;
import java.util.Map;

/**
 * Created by Yusuf on 3/2/2016
 * Uses the Graphstream library (http://graphstream-project.org/) to display NFAs and DFAs generated from regular expressions
 */
public class AppGraph {

    private Graph graph = new SingleGraph("Graph Traversal");
    private static DFA dfa;
    private static Fragment fragment;

    /*
    * Initialize graph with edges and nodes for a Deterministic Finite Automaton
    */
    public ViewPanel init(DFA dfa) {
        initGraph();
        setGraph(dfa);
        return attachViewPanel();
    }

    /*
    * Initialize graph with edges and nodes for a Non-Deterministic Finite Automaton
    */
    public ViewPanel init(Fragment fragment) {
        initGraph();
        setGraph(fragment);
        return attachViewPanel();
    }

    /*
    * Set stylesheet for the graph and the quality of the display
    */
    private void initGraph() {
        String styleSheet = "graph {" +
                "fill-color: #FFFFFF;" +
                "}" +
                "edge {" +
                "size: 2px;" +
                "fill-mode: dyn-plain;" +
                "}" +
                "edge.caseA {" +
                "fill-color: #EC7063;" +
                "}" +
                "edge.caseB {" +
                "fill-color: #239B56;" +
                "}" +
                "edge.caseC {" +
                "fill-color: #3498DB;" +
                "}" +
                "edge.caseD {" +
                "fill-color: #7D3C98;" +
                "}" +
                "edge.caseE {" +
                "fill-color: #B7950B;" +
                "}" +
                "edge.default {" +
                "fill-color: #17202A;" +
                "}" +
                "node {" +
                "size: 25px;" +
                "fill-mode: dyn-plain;" +
                "fill-color: #EEEEEE, #f2ede4, #95b205;" +
                "text-size: 16px;" +
                "}" +
                "node.start {" +
                "fill-color: #fff033;" +
                "stroke-mode: plain;" +
                "stroke-color: #555;" +
                "stroke-width: 3px;" +
                "}" +
                "node.final {" +
                "fill-color: #e1f9f2;" +
                "stroke-mode: plain;" +
                "stroke-color: #855;" +
                "stroke-width: 3px;" +
                "}" +
                "node:clicked {" +
                "fill-color: #c7e475;" +
                "}";
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
    }

    /*
    * Create a Viewer for the graph and return the ViewPanel that contains the graph
    */
    private ViewPanel attachViewPanel() {
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        return viewer.addDefaultView(false);
    }

    /*
    * Add nodes and edges to the DFA graph
    */
    private void setGraph(DFA dfa){
        AppGraph.dfa = dfa;
        for(DFAState dfaState: dfa.getStates()) {
            String currentLabel = Character.toString(dfaState.getLabel());
            boolean isStart = false;
            boolean isFinal = false;
            if(dfa.getStartState().equals(dfaState)){
                isStart = true;
            }
            if(dfa.getFinalStates().contains(dfaState)){
                isFinal = true;
            }
            if(graph.getNode(currentLabel) == null){
                addNode(currentLabel, isStart, isFinal);
            }
            for (Map.Entry<Character, List<Character>> entry : dfaState.getTransitions().entrySet()) {
                for(Character stateNodes : entry.getValue()) {
                    String transitionLabel = Character.toString(stateNodes);
                    buildGraph(transitionLabel, currentLabel, entry.getKey(), true);
                }
            }
        }
    }

    /*
    * Add nodes and edges to the NFA graph
    */
    private void setGraph(Fragment fragment) {
        AppGraph.fragment = fragment;
        for(State state: fragment.getStates()) {
            String currentLabel = Character.toString(state.getLabel());
            boolean isStart = false;
            boolean isFinal = false;
            if(fragment.getStartState().equals(state)){
                isStart = true;
            }
            if(fragment.getFinalState().equals(state)){
                isFinal = true;
            }
            if(graph.getNode(currentLabel) == null){
                addNode(currentLabel, isStart, isFinal);
            }
            for (Map.Entry<Character, List<State>> entry : state.getTransitions().entrySet()) {
                for(State stateNodes : entry.getValue()) {
                    String transitionLabel = Character.toString(stateNodes.getLabel());
                    buildGraph(transitionLabel, currentLabel, entry.getKey(), false);
                }
            }
        }
    }

    /*
    * Add nodes and edges to graph setting all the appropriate classes and labels
    */
    private void addNode(String currentLabel, boolean isStart, boolean isFinal) {
        Node currentNode = graph.addNode(currentLabel);
        currentNode.addAttribute("ui.label", currentLabel);
        if(isStart){
            currentNode.addAttribute("ui.class", "start");
        }
        if(isFinal){
            currentNode.addAttribute("ui.class", "final");
        }
    }

    /*
    * Create node if it does not exist and add edge between current node and all node it transitions to via char key
    */
    private void buildGraph(String transitionLabel, String currentLabel, Character key, boolean isDFA){
        if(graph.getNode(transitionLabel) == null){
            Node transitionNode = graph.addNode(transitionLabel);
            boolean isStart = false;
            boolean isFinal = false;
            if(isDFA) {
                DFAState transitionState = dfa.returnStateFromCharacter(transitionLabel.charAt(0));
                if (dfa.getStartState().equals(transitionState)) {
                    isStart = true;
                }
                if (dfa.getFinalStates().contains(transitionState)) {
                    isFinal = true;
                }
            } else {
                State transitionState = fragment.returnStateFromCharacter(transitionLabel.charAt(0));
                if (fragment.getStartState().equals(transitionState)) {
                    isStart = true;
                }
                if (fragment.getFinalState().equals(transitionState)) {
                    isFinal = true;
                }
            }

            transitionNode.addAttribute("ui.label", transitionLabel);
            if(isStart){
                transitionNode.addAttribute("ui.class", "start");
            }
            if(isFinal){
                transitionNode.addAttribute("ui.class", "final");
            }

            Edge edge = graph.addEdge(currentLabel+transitionLabel, (Node) graph.getNode(currentLabel), graph.getNode(transitionLabel), true);
            edge.setAttribute("ui.class", getClassType(key));
        } else {
            Edge edge = graph.addEdge(currentLabel+transitionLabel, (Node) graph.getNode(currentLabel), graph.getNode(transitionLabel), true);
            edge.setAttribute("ui.class", getClassType(key));
        }
    }

    /*
    * Return classes depending on character (key)
    */
    private String getClassType(Character key) {
        String classType;
        switch (key){
            case 'a' :
                classType = "caseA";
                break;
            case 'b' :
                classType = "caseB";
                break;
            case 'c' :
                classType = "caseC";
                break;
            case 'd' :
                classType = "caseD";
                break;
            case 'e' :
                classType = "caseE";
                break;
            default:
                classType = "default";
                break;
        }
        return classType;
    }

}
