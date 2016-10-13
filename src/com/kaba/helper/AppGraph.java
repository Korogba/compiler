package com.kaba.helper;

import com.kaba.automata.DFA;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Yusuf on 3/2/2016
 * Graph display
 */
public class AppGraph {

    private Graph graph = new SingleGraph("Graph Traversal");

    /*
    *Initialize graph with edges and nodes for a Deterministic Finite Automaton
    */
    public ViewPanel init(DFA dfa) {
        initGraph();
        setGraph(dfa);
        return attachViewPanel();
    }

    /*
    *Initialize graph with edges and nodes for a Non-Deterministic Finite Automaton
    */
    public ViewPanel init(Fragment fragment) {
        initGraph();
        setGraph(fragment);
        return attachViewPanel();
    }

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
                "fill-color: #e1f9f2;" +
                "stroke-mode: plain;" +
                "stroke-color: #555;" +
                "stroke-width: 3px;" +
                "}" +
                "node.final {" +
                "fill-color: #e1f9f2;" +
                "stroke-mode: plain;" +
                "stroke-color: #855;" +
                "stroke-width: 3px;" +
                "shape: rounded-box;" +
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

    private ViewPanel attachViewPanel() {
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        return viewer.addDefaultView(false);
    }

    private void setGraph(DFA dfa){
        for(DFAState dfaState: dfa.getStates()) {
            String currentLabel = Character.toString(dfaState.getLabel());
            if(graph.getNode(currentLabel) == null){
                Node currentNode = graph.addNode(currentLabel);
                currentNode.addAttribute("ui.label", currentLabel);
            }
            for (Map.Entry<Character, List<Character>> entry : dfaState.getTransitions().entrySet()) {
                for(Character stateNodes : entry.getValue()) {
                    String transitionLabel = Character.toString(stateNodes);
                    buildGraph(transitionLabel, currentLabel, entry.getKey());
                }
            }
        }
    }

    private void setGraph(Fragment fragment){
        for(State state: fragment.getStates()) {
            String currentLabel = Character.toString(state.getLabel());
            if(graph.getNode(currentLabel) == null){
                Node currentNode = graph.addNode(currentLabel);
                currentNode.addAttribute("ui.label", currentLabel);
            }
            for (Map.Entry<Character, List<State>> entry : state.getTransitions().entrySet()) {
                for(State stateNodes : entry.getValue()) {
                    String transitionLabel = Character.toString(stateNodes.getLabel());
                    buildGraph(transitionLabel, currentLabel, entry.getKey());
                }
            }
        }
    }

    private void buildGraph(String transitionLabel, String currentLabel, Character key){
        if(graph.getNode(transitionLabel) == null){
            Node transitionNode = graph.addNode(transitionLabel);
            transitionNode.addAttribute("ui.label", transitionLabel);
            Edge edge = graph.addEdge(currentLabel+transitionLabel, (Node) graph.getNode(currentLabel), graph.getNode(transitionLabel), true);
            edge.setAttribute("ui.class", getClassType(key));
        } else {
            Edge edge = graph.addEdge(currentLabel+transitionLabel, (Node) graph.getNode(currentLabel), graph.getNode(transitionLabel), true);
            edge.setAttribute("ui.class", getClassType(key));
        }
    }

    private String getClassType(Character key) {
        String classType = "";
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
