package com.kaba.ui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Yusuf on 10/11/2016
 */
public class CodeGenWindow extends JPanel {
    private JButton generate;
    private JTextField inputExpression;
    private JButton reset;
    private JPanel centerPanel;
    private JTable threeAddressCode;
    private JTable quadruples;
    private JTable triples;
    private boolean firstTimeClicked = true;

    public CodeGenWindow(AppWindow appWindow) {
        super(new BorderLayout());

        inputExpression = new JTextField("Enter expression terminated by ';' - ");
        add(inputExpression, BorderLayout.PAGE_END);
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
        add(generate, BorderLayout.PAGE_END);

        reset = new JButton("Reset");
        add(reset, BorderLayout.PAGE_END);

        centerPanel = new JPanel(new GridBagLayout());
        setUpCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

    }

    private void setUpCenterPanel() {
        String[] threeAddressCodeTitle = {"#","Statement"};
        String[] triplesTitle = {"#","Operator", "Argument One", "Argument Two"};
        String[] quadruplesTitle = {"#","Operator", "Argument One", "Argument Two", "Result"};

        threeAddressCode = new JTable(new MyTableModel(threeAddressCodeTitle));
        GridBagConstraints codeConstraints = new GridBagConstraints();
        codeConstraints.fill = GridBagConstraints.HORIZONTAL;
        codeConstraints.gridx = 0;
        codeConstraints.gridy = 0;
        codeConstraints.weightx = 0.5;
        codeConstraints.weighty = 0.5;
        centerPanel.add(new JScrollPane(threeAddressCode), codeConstraints);

        triples = new JTable(new MyTableModel(triplesTitle));
        GridBagConstraints triplesConstraints = new GridBagConstraints();
        triplesConstraints.fill = GridBagConstraints.HORIZONTAL;
        triplesConstraints.gridx = 1;
        triplesConstraints.gridy = 0;
        triplesConstraints.weightx = 0.5;
        triplesConstraints.weighty = 0.5;
        centerPanel.add(new JScrollPane(triples), triplesConstraints);

        quadruples = new JTable(new MyTableModel(quadruplesTitle));
        GridBagConstraints quadruplesConstraints = new GridBagConstraints();
        quadruplesConstraints.fill = GridBagConstraints.BOTH;
        quadruplesConstraints.gridx = 0;
        quadruplesConstraints.gridy = 1;
        quadruplesConstraints.weightx = 0.5;
        quadruplesConstraints.gridwidth = 2;
        quadruplesConstraints.weighty = 0.5;
        centerPanel.add(new JScrollPane(quadruples), quadruplesConstraints);
    }

    class MyTableModel extends AbstractTableModel {
        private String[] columnNames;
        private String[][] data = {};

        public MyTableModel(String[] columnNames) {
            this.columnNames = columnNames;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(String value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }

}
