package com.kaba.ui;

import com.kaba.algorithms.SubsetConstruction;
import com.kaba.algorithms.Thompson;
import com.kaba.automata.DFA;
import com.kaba.helper.Fragment;
import com.kaba.helper.Regex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EmptyStackException;

/**
 * Created by Yusuf on 10/11/2016
 * Abstract class that contains the methods to display an automata
 * Implemented by DFAWindow and NFAWindow
 */
abstract class GraphWindow extends JPanel {
    private JTextField inputArea;
    private JPanel chartPanel;
    private boolean firstTimeClicked = true;
    private AppWindow appWindow;

    GraphWindow(AppWindow appWindow) {
        this.appWindow = appWindow;
        /*
        * Set the layout clause
        */
        setLayout(new GridBagLayout());

        chartPanel = new JPanel(new BorderLayout());
        inputArea = new JTextField("Enter Regular Expression: ");
        GridBagConstraints inputConstraints = new GridBagConstraints();
        inputConstraints.fill = GridBagConstraints.HORIZONTAL;
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 0;
        inputConstraints.weightx = 1;
        inputArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(firstTimeClicked){
                    inputArea.setText("");
                    firstTimeClicked = false;
                }
            }
        });
        add(inputArea, inputConstraints);

        JButton run = new JButton("Run");
        GridBagConstraints searchConstraints = new GridBagConstraints();
        searchConstraints.fill = GridBagConstraints.HORIZONTAL;
        searchConstraints.gridx = 1;
        searchConstraints.gridy = 0;
        searchConstraints.weightx = 0.5;
        run.addActionListener(new RunHandler());
        add(run, searchConstraints);

        /*
        * Add to JPanel
        */
        GridBagConstraints chartConstraints = new GridBagConstraints();
        chartConstraints.fill = GridBagConstraints.BOTH;
        chartConstraints.gridx = 0;
        chartConstraints.gridy = 1;
        chartConstraints.weightx = 0.5;
        chartConstraints.weighty = 0.5;
        chartConstraints.gridwidth = 2;
        chartConstraints.gridheight = GridBagConstraints.RELATIVE;
        add(chartPanel, chartConstraints);

        GridColor gridColor = new GridColor();
        GridBagConstraints legendConstraint = new GridBagConstraints();
        legendConstraint.fill = GridBagConstraints.HORIZONTAL;
        legendConstraint.anchor = GridBagConstraints.PAGE_END;
        legendConstraint.gridx = 1;
        legendConstraint.gridy = 2;
        legendConstraint.gridwidth = 1;
        add(gridColor, legendConstraint);
    }

    JPanel getChartPanel() {
        return chartPanel;
    }

    JTextField getInputArea() {
        return inputArea;
    }

    void setFirstTimeClicked(boolean flag) {
        firstTimeClicked = flag;
    }

    /**
     * Inner ActionHandler class that handles the creation and displaying of NFA and DFA
     */
    private class RunHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                if(inputArea.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(appWindow, "Enter a proper expression!", "Input Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String regex = inputArea.getText();
                Fragment NFA = Thompson.postfixToNFA(Regex.infixToPostfix(regex));
                DFA dfa = SubsetConstruction.subsetConstruction(NFA);
                System.out.println("GENERATED NFA:\n" + NFA);
                System.out.println("==============================================================================================================================================");
                System.out.println("GENERATED DFA:\n");
                System.out.println("==============================================================================================================================================");
                System.out.println(dfa);
                AutomataWindow.setUpNfaDfa(NFA, dfa);
                AutomataWindow.setUpStrings(regex);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(appWindow, e.getLocalizedMessage(), "Input Error!",JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getLocalizedMessage());
            } catch (ArrayIndexOutOfBoundsException | EmptyStackException e) {
                JOptionPane.showMessageDialog(appWindow, "The expression is not properly formatted!", "Input Error!", JOptionPane.ERROR_MESSAGE);
                System.out.println("The expression is not properly formatted.");
            }
        }
    }

    private class ColorPanel extends JPanel {
        private String hexColor;

        ColorPanel(String hexColor, String label) {
            setLayout(new FlowLayout());
            this.hexColor = hexColor;
            add(new JLabel(label));
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawRect(10,10,10,10);
            g.setColor(Color.decode(hexColor));
            g.fillRect(10,10,10,10);
        }
    }

    private class GridColor extends JPanel {
        private final String[] colors = {"#EC7063", "#239B56", "#3498DB", "#7D3C98", "#B7950B", "#17202A"};
        private final String[] inputs = {"a", "b", "c", "d", "e", "ε"};

        GridColor() {
            setLayout(new GridLayout(2,3));
            for(int i = 0; i < colors.length; i++){
                ColorPanel colorPanel = new ColorPanel(colors[i], inputs[i]);
                this.add(colorPanel);
            }
        }
    }
}
