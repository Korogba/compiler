package com.kaba.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Yusuf on 2/28/2016.
 * AppWindow contains the aggregate of the sub-components that make up the user interface of the application
 */
public class AppWindow extends JFrame {
    private static String title = "Compiler Construction";

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
        JMenuItem close = new JMenuItem("close");
        close.setMnemonic('x');
        close.addActionListener(actionEvent -> System.exit(0));

        file.add(close);

        jMenuBar.add(file);

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

        JTabbedPane tabbedPane = new JTabbedPane();
        AutomataWindow automataWindow = new AutomataWindow(this);
        tabbedPane.addTab("Finite State Automata", automataWindow);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        CodeGenWindow codeGenWindow = new CodeGenWindow(this);
        tabbedPane.addTab("Intermediate Code Generation", codeGenWindow);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        add(tabbedPane, tabConstraints);

    }
}
