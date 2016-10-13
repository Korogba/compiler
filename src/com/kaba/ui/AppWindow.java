package com.kaba.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Created by Yusuf on 2/28/2016.
 * AppWindow contains the gui components of the application
 */
public class AppWindow extends JFrame {
    /*
    * Initialize gui components
    */
    private JMenuItem slow;
    private JMenuItem moderate;
    private JMenuItem fast;
    private JTextField status;
    private JTabbedPane tabbedPane;
    private static String title = "Compiler Construction";
    public static int speed = 500;

    /*
    * Constructor for displaying application window
    */
    public AppWindow(){
        /*
        * Call superclass constructor
        */
        super(title);
        /*
        * Set the layout
        */
        setLayout(new GridBagLayout());
        /*
        * Set up menus
        */
        JMenuBar jMenuBar = new JMenuBar();
        JMenu file = new JMenu("File");


        JMenu speed = new JMenu("Speed");
        speed.setMnemonic('s');
        slow = new JMenuItem("Slow");
        speed.add(slow);
        moderate = new JMenuItem("Moderate (Default)");
        speed.add(moderate);
        fast = new JMenuItem("Fast");
        speed.add(fast);

        jMenuBar.add(file);
        jMenuBar.add(speed);

        setJMenuBar(jMenuBar);

        /*
        * Set up Tabs
        */
        GridBagConstraints tabConstraints = new GridBagConstraints();
        tabConstraints.fill = GridBagConstraints.BOTH;
        tabConstraints.gridx = 0;
        tabConstraints.gridy = 0;
        tabConstraints.weightx = 0.5;
        tabConstraints.weighty = 0.5;
        tabConstraints.gridwidth = GridBagConstraints.REMAINDER;

        tabbedPane = new JTabbedPane();
        AutomataWindow automataWindow = new AutomataWindow(this);
        tabbedPane.addTab("Finite State Automata", automataWindow);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        CodeGenWindow codeGenWindow = new CodeGenWindow(this);
        tabbedPane.addTab("Minimization Graph", codeGenWindow);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        tabbedPane.addChangeListener(changeEvent -> {
//            if(tabbedPane.getSelectedComponent().getClass().equals(SearchWindow.class)){
//                //setAppropriateTitleAndStatus(searchWindow.getAlgorithms(), "Graph Search");
//            }
//            if(tabbedPane.getSelectedComponent().getClass().equals(OptimizationWindow.class)){
//                //setAppropriateTitleAndStatus(optimizationWindow.getAlgorithms(), "Minimization Problem");
//            }
//            if(tabbedPane.getSelectedComponent().getClass().equals(GAWindow.class)){
//               // setAppropriateTitleAndStatus(gaWindow.getChartPane(), "Genetic Algorithm");
//            }
//            if(tabbedPane.getSelectedComponent().getClass().equals(NeuralNet.class)){
//                setAppropriateTitleAndStatus("Multilayer Perceptron");
//            }
        });

        add(tabbedPane, tabConstraints);

        /*
        * Set up status bar below
        */
        String statusBar = "status";
        status = new JTextField(statusBar);
        status.setEditable(false);
        GridBagConstraints statusConstraints = new GridBagConstraints();
        statusConstraints.fill = GridBagConstraints.HORIZONTAL;
        statusConstraints.gridx = 0;
        statusConstraints.gridy = 1;
        statusConstraints.weightx =0.5;
        statusConstraints.gridwidth = GridBagConstraints.REMAINDER;
        statusConstraints.gridheight = 1;
        add(status, statusConstraints);

        //callListeners();
    }

    private void setAppropriateTitleAndStatus(String title) {
        changeTitle("Compiler Construction: " + title);
        changeStatus(title + " selected");
    }

    private void setAppropriateTitleAndStatus(List<JRadioButton> algorithms, String defaultTitle) {
        boolean flag = true;
        for(JRadioButton radioButton : algorithms){
            if(radioButton.isSelected()){
                //setAlgorithmString(radioButton.getText());
                changeTitle("Compiler Construction: " + radioButton.getText());
                changeStatus(radioButton.getText() + " selected");
                flag = false;
            }
        }
        if(flag){
            changeTitle("Compiler Construction: " + defaultTitle);
            changeStatus(defaultTitle + " selected");
        }
    }

    public JTextField getStatus() {
        return status;
    }

    public JMenuItem getSlow() {
        return slow;
    }

    public JMenuItem getModerate() {
        return moderate;
    }

    public JMenuItem getFast() {
        return fast;
    }

    /*
        * Change title to reflect current algorithm
        */
    public void changeTitle(String newTitle) {
        title = newTitle;
        setTitle(newTitle);
    }

    /*
    * Change status bar to reflect current/last operation
    */
    public void changeStatus(String newStatus) {
        status.setText(newStatus);
    }

    public void allStatus(boolean flag){
        slow.setEnabled(flag);
        moderate.setEnabled(flag);
        fast.setEnabled(flag);
        tabbedPane.setEnabled(flag);
    }

    public void disableExceptClear(){
        slow.setEnabled(false);
        moderate.setEnabled(false);
        fast.setEnabled(false);
        tabbedPane.setEnabled(true);
    }
}
