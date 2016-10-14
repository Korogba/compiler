package com.kaba.ui;

import com.kaba.automata.DFA;
import com.kaba.helper.AppGraph;
import com.kaba.helper.Fragment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Yusuf on 10/11/2016
 * Panel for the two automata graphs: NFA & DFA
 */
class AutomataWindow extends JPanel {

    private static DFAWindow dfaWindow;
    private static NFAWindow nfaWindow;

    AutomataWindow(AppWindow appWindow) {
        super(new GridLayout(0, 1));
        JTabbedPane chartPane = new JTabbedPane(JTabbedPane.LEFT);

        nfaWindow = new NFAWindow(appWindow);
        chartPane.addTab("Non-Deterministic Finite Automata", nfaWindow);
        chartPane.setMnemonicAt(0, KeyEvent.VK_1);

        dfaWindow = new DFAWindow(appWindow);
        chartPane.addTab("Deterministic Finite Automata", dfaWindow);
        chartPane.setMnemonicAt(1, KeyEvent.VK_2);

        add(chartPane);
    }

    /**
     * Clear the panels for both NFA & DFA and replace with the latest nfa & dfa
     */
    static void setUpNfaDfa(Fragment nfa, DFA dfa) {
        nfaWindow.getChartPanel().removeAll();
        nfaWindow.getChartPanel().add(new AppGraph().init(nfa));
        nfaWindow.getChartPanel().revalidate();
        dfaWindow.getChartPanel().removeAll();
        dfaWindow.getChartPanel().add(new AppGraph().init(dfa));
        dfaWindow.getChartPanel().revalidate();

    }

    /**
     * Set the input text area of both NFA and DFA windows to the regexp that just executed
     */
    static void setUpStrings(String upStrings) {
        nfaWindow.getInputArea().setText(upStrings);
        dfaWindow.getInputArea().setText(upStrings);
        nfaWindow.setFirstTimeClicked(false);
        dfaWindow.setFirstTimeClicked(false);
    }
}
