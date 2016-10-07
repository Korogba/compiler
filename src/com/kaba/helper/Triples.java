package com.kaba.helper;

/**
 * Class that contains the definition for a Triple intermediate code: Created by Yusuf on 10/3/2016.
 */
public class Triples {
    private int position;
    private String operator;
    private String argumentOne;
    private String argumentTwo;

    public Triples(int position, String operator, String argumentOne, String argumentTwo) {
        this.position = position;
        this.operator = operator;
        this.argumentOne = argumentOne;
        this.argumentTwo = argumentTwo;
    }

/*    public Triples(String argumentOne, String operator, int position) {
        this.argumentOne = argumentOne;
        this.argumentTwo = "";
        this.operator = operator;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getArgumentOne() {
        return argumentOne;
    }

    public void setArgumentOne(String argumentOne) {
        this.argumentOne = argumentOne;
    }

    public String getArgumentTwo() {
        return argumentTwo;
    }

    public void setArgumentTwo(String argumentTwo) {
        this.argumentTwo = argumentTwo;
    }*/

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
