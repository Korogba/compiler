package com.kaba.ui;

import com.kaba.algorithms.ThreeAddressCode;
import com.kaba.helper.BinaryTree;
import com.kaba.helper.Quadruples;
import com.kaba.helper.Triples;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EmptyStackException;
import java.util.List;

/**
 * Created by Yusuf on 10/11/2016
 * Displays the Tables for Three Address Codes, Triples & Quadruples IR
 */
class CodeGenWindow extends JPanel {
    private JButton generate;
    private JTextField inputExpression;
    private JPanel centerPanel;
    private JTable threeAddressCode;
    private JTable quadruples;
    private JTable triples;
    private boolean firstTimeClicked = true;
    private AppWindow appWindow;
    private static boolean isReset = false;

    CodeGenWindow(AppWindow appWindow) {
        super(new BorderLayout());

        this.appWindow = appWindow;

        inputExpression = new JTextField("Enter expression terminated by ';' - ");
        inputExpression.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(firstTimeClicked){
                    inputExpression.setText("");
                    firstTimeClicked = false;
                }
            }
        });

        JPanel pageEndPanel = new JPanel();

        pageEndPanel.add(inputExpression);
        generate = new JButton("Generate");
        pageEndPanel.add(generate);
        JButton reset = new JButton("Reset");
        pageEndPanel.add(reset);
        add(pageEndPanel, BorderLayout.PAGE_END);

        centerPanel = new JPanel(new GridBagLayout());
        setUpCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        generate.addActionListener(new RunnerHandler());
        reset.addActionListener(actionEvent -> {
            isReset = false;
            clearTable();
            disableAll();
        });
    }

    /**
     * Clear all the JTables: threeAddressCode, quadruples & triples
     */
    private void clearTable() {
        DefaultTableModel threeModel = (DefaultTableModel) threeAddressCode.getModel();
        threeModel.setNumRows(0);

        DefaultTableModel quadModel = (DefaultTableModel) quadruples.getModel();
        quadModel.setNumRows(0);

        DefaultTableModel triModel = (DefaultTableModel) triples.getModel();
        triModel.setNumRows(0);
    }

    /**
     * Set the headers for the three tables and add them to centerpanel
     */
    private void setUpCenterPanel() {
        String[] threeAddressCodeTitle = {"#","Statement"};
        String[] triplesTitle = {"#", "Argument One", "Operator", "Argument Two"};
        String[] quadruplesTitle = {"#", "Argument One", "Operator", "Argument Two", "Result"};

        threeAddressCode = new JTable(new DefaultTableModel(threeAddressCodeTitle, 0));
        threeAddressCode.getTableHeader().setToolTipText("Three Address Code Table");
        GridBagConstraints codeConstraints = new GridBagConstraints();
        codeConstraints.fill = GridBagConstraints.BOTH;
        codeConstraints.gridx = 0;
        codeConstraints.gridy = 0;
        codeConstraints.weightx = 0.5;
        codeConstraints.weighty = 0.5;
        centerPanel.add(new JScrollPane(threeAddressCode), codeConstraints);

        triples = new JTable(new DefaultTableModel(triplesTitle, 0));
        triples.getTableHeader().setToolTipText("Triples Table");
        GridBagConstraints triplesConstraints = new GridBagConstraints();
        triplesConstraints.fill = GridBagConstraints.BOTH;
        triplesConstraints.gridx = 1;
        triplesConstraints.gridy = 0;
        triplesConstraints.weightx = 0.5;
        triplesConstraints.weighty = 0.5;
        centerPanel.add(new JScrollPane(triples), triplesConstraints);

        quadruples = new JTable(new DefaultTableModel(quadruplesTitle, 0));
        quadruples.getTableHeader().setToolTipText("Quadruples Table");
        GridBagConstraints quadruplesConstraints = new GridBagConstraints();
        quadruplesConstraints.fill = GridBagConstraints.BOTH;
        quadruplesConstraints.gridx = 0;
        quadruplesConstraints.gridy = 1;
        quadruplesConstraints.weightx = 0.5;
        quadruplesConstraints.gridwidth = 2;
        quadruplesConstraints.weighty = 0.5;
        centerPanel.add(new JScrollPane(quadruples), quadruplesConstraints);
    }

    /**
     * Inner ActionHandler class that handles the code generation when generate button is clicked
     */
    private class RunnerHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                if(inputExpression.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(appWindow, "Enter a proper expression!", "Input Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ThreeAddressCode codeGenerator = new ThreeAddressCode();

                System.out.println("GENERATED TAC:\n");
                String regex = inputExpression.getText();
                DefaultTableModel defaultTableModel = (DefaultTableModel) threeAddressCode.getModel();
                List<BinaryTree<String>> addressCodes = codeGenerator.generateThreeAddressCode(regex);
                for(BinaryTree<String> statement: addressCodes) {
                    List<String> statementList = codeGenerator.getThreeAddress(statement);
                    System.out.print("Statement => "+ codeGenerator.getStatements().pop());
                    System.out.println(": " + statementList);
                    for (int i =0; i < statementList.size(); i++) {
                        String currentStatement = statementList.get(i);
                        int j = i + 1;
                        defaultTableModel.addRow(new String[]{j+"", currentStatement});
                    }
                }

                System.out.println("==============================================================================================================================================");
                System.out.println("GENERATED QUADRUPLE:\n");
                DefaultTableModel quadModel = (DefaultTableModel) quadruples.getModel();
                List<BinaryTree<String>> quadrupleStatements = codeGenerator.generateThreeAddressCode(regex);
                for(BinaryTree<String> statement: quadrupleStatements){
                    List<Quadruples> quadList = codeGenerator.getQuadruples(statement);
                    System.out.print("Statement => "+ codeGenerator.getStatements().pop());
                    System.out.println(": " + quadList);
                    for (int i =0; i < quadList.size(); i++) {
                        Quadruples currentQuad = quadList.get(i);
                        int j = i + 1;
                        quadModel.addRow(new String[]{j+"", currentQuad.getOperandOne(), currentQuad.getOperator(), currentQuad.getOperandTwo(), currentQuad.getResult()});
                    }
                }
                System.out.println("==============================================================================================================================================");

                ThreeAddressCode tripleGenerator = new ThreeAddressCode();
                System.out.println("GENERATED TRIPLE:\n");
                DefaultTableModel triModel = (DefaultTableModel) triples.getModel();
                List<BinaryTree<String>> tripleStatements = tripleGenerator.generateThreeAddressCode(regex);
                for(BinaryTree<String> statement: tripleStatements){
                    List<Triples> triList = tripleGenerator.getTriples(statement);
                    System.out.print("Statement => "+ tripleGenerator.getStatements().pop());
                    System.out.println(": " + triList);
                    for (Triples currentTri : triList) {
                        triModel.addRow(new String[]{"" + currentTri.getPosition(), currentTri.getArgumentOne(), currentTri.getOperator(), currentTri.getArgumentTwo()});
                    }
                }
                System.out.println("==============================================================================================================================================");


            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(appWindow, e.getLocalizedMessage(), "Input Error!",JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getLocalizedMessage());
            } catch (ArrayIndexOutOfBoundsException | EmptyStackException e) {
                JOptionPane.showMessageDialog(appWindow, "The expression is not properly formatted!", "Input Error!", JOptionPane.ERROR_MESSAGE);
                System.out.println("The expression is not properly formatted.");
            }
            finally {
                isReset = true;
                disableAll();
            }
        }
    }

    /**
     * Disable/Enable all Intermediate Code generation until the gui is cleared/reset
     */
    private void disableAll() {
        inputExpression.setEnabled(!isReset);
        generate.setEnabled(!isReset);
    }

}
