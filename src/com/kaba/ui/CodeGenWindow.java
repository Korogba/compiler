package com.kaba.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Yusuf on 10/11/2016
 */
public class CodeGenWindow extends JPanel {
    private JTextArea textArea;
    private JButton generate;
    private JTextField inputExpression;
    private JButton reset;
    private JPanel centerPanel;
    private boolean firstTimeClicked = true;

    public CodeGenWindow(AppWindow appWindow) {
        super(new GridBagLayout());

        inputExpression = new JTextField("Enter expression terminated by ';' - ");
        GridBagConstraints inputConstraint = new GridBagConstraints();
        inputConstraint.fill = GridBagConstraints.HORIZONTAL;
        inputConstraint.gridx = 0;
        inputConstraint.gridy = 0;
        inputConstraint.weightx = 0.5;
        add(inputExpression, inputConstraint);
        inputExpression.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(firstTimeClicked){
                    inputExpression.setText("");
                    firstTimeClicked = false;
                }
            }
        });

        generate = new JButton("Generate");
        GridBagConstraints generateConstraints = new GridBagConstraints();
        generateConstraints.fill = GridBagConstraints.HORIZONTAL;
        generateConstraints.gridx = 1;
        generateConstraints.gridy = 0;
        generateConstraints.weightx = 0.5;
        add(generate, generateConstraints);

        reset = new JButton("Reset");
        GridBagConstraints resetConstraint = new GridBagConstraints();
        resetConstraint.fill = GridBagConstraints.HORIZONTAL;
        resetConstraint.gridx = 2;
        resetConstraint.gridy = 0;
        resetConstraint.weightx = 0.5;
        add(reset, resetConstraint);

        centerPanel = new JPanel(new GridLayout());
        GridBagConstraints centerConstraints = new GridBagConstraints();
        centerConstraints.fill = GridBagConstraints.BOTH;
        centerConstraints.gridx = 0;
        centerConstraints.gridy = 1;
        centerConstraints.weightx = 0.5;
        centerConstraints.weighty = 0.5;
        centerConstraints.gridwidth = 3;
        add(centerPanel, centerConstraints);

        textArea = new JTextArea("Results: ", 3, 0);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints textConstraints = new GridBagConstraints();
        textConstraints.fill = GridBagConstraints.HORIZONTAL;
        textConstraints.gridx = 0;
        textConstraints.gridy = 2;
        textConstraints.weighty = 0;
        textConstraints.weightx = 1;
        textConstraints.gridwidth = 3;
        add(textArea, textConstraints);
    }

}
