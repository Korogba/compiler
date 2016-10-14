package com.kaba.helper;

/**
 * Class that contains the definition for a Quadruple intermediate code: Created by Yusuf on 10/3/2016.
 */
public class Quadruples {
    private String operandOne;
    private String operandTwo;
    private String operator;
    private String result;

    public Quadruples(String operandOne, String operandTwo, String operator, String result) {
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
        this.operator = operator;
        this.result = result;
    }

    public Quadruples(String operator, String result, String operandOne) {
        this.operator = operator;
        this.result = result;
        this.operandOne = operandOne;
        this.operandTwo = "";
    }

    public String getOperandOne() {
        return operandOne;
    }

    public String getOperandTwo() {
        return operandTwo;
    }

    public String getOperator() {
        return operator;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Quadruples{" +
                "operandOne='" + operandOne + '\'' +
                ", operandTwo='" + operandTwo + '\'' +
                ", operator='" + operator + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
