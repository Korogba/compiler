package com.kaba.ui;

import com.kaba.automata.DFA;
import com.kaba.helper.AppGraph;
import com.kaba.helper.Fragment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Yusuf on 10/11/2016
 */
class AutomataWindow extends JPanel {

    private JTabbedPane chartPane;
    private static DFAWindow dfaWindow;
    private static NFAWindow nfaWindow;

    AutomataWindow(AppWindow appWindow) {
        super(new GridLayout(0, 1));
        chartPane = new JTabbedPane(JTabbedPane.LEFT);

        nfaWindow = new NFAWindow(appWindow);
        chartPane.addTab("Non-Deterministic Finite Automata", nfaWindow);
        chartPane.setMnemonicAt(0, KeyEvent.VK_1);

        dfaWindow = new DFAWindow(appWindow);
        chartPane.addTab("Deterministic Finite Automata", dfaWindow);
        chartPane.setMnemonicAt(1, KeyEvent.VK_2);

        chartPane.addChangeListener(changeEvent -> GraphWindow.setAppropriateTitleAndStatus((GraphWindow)chartPane.getSelectedComponent(), "Finite Automata"));

        add(chartPane);
    }

    static void setUpNfaDfa(Fragment nfa, DFA dfa) {
        nfaWindow.getChartPanel().removeAll();
        nfaWindow.getChartPanel().add(new AppGraph().init(nfa));
        dfaWindow.getChartPanel().removeAll();
        dfaWindow.getChartPanel().add(new AppGraph().init(dfa));
    }

    static void setUpStrings(String upStrings) {
        nfaWindow.getInputArea().setText(upStrings);
        dfaWindow.getInputArea().setText(upStrings);
        nfaWindow.setFirstTimeClicked(false);
        dfaWindow.setFirstTimeClicked(false);
    }
}
