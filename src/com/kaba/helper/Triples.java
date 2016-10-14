package com.kaba.helper;

/**
 * Class that contains the definition for a Triple intermediate code: Created by Yusuf on 10/3/2016.
 */
public class Triples {
    private String position;
    private String operator;
    private String argumentOne;
    private String argumentTwo;

    public Triples(String position, String operator, String argumentOne, String argumentTwo) {
        this.position = position;
        this.operator = operator;
        this.argumentOne = argumentOne;
        this.argumentTwo = argumentTwo;
    }

    public String getPosition() {
        return position;
    }

    public String getOperator() {
        return operator;
    }

    public String getArgumentOne() {
        return argumentOne;
    }

    public String getArgumentTwo() {
        return argumentTwo;
    }

    @Override
    public String toString() {
            return position +
                    " : " +
                    argumentOne + " " +
                    operator +
                    " " +
                    argumentTwo;
    }
}
