package calc;

import java.util.*;

import static java.lang.Double.NaN;
import static java.lang.Math.pow;


/*
 *   A calculator for rather simple arithmetic expressions
 *
 *   This is not the program, it's a class declaration (with methods) in it's
 *   own file (which must be named Calculator.java)
 *
 *   NOTE:
 *   - No negative numbers implemented
 */
class Calculator {

    // Here are the only allowed instance variables!
    // Error messages (more on static later)
    final static String MISSING_OPERAND = "Missing or bad operand";
    final static String DIV_BY_ZERO = "Division with 0";
    final static String MISSING_OPERATOR = "Missing operator or parenthesis";
    final static String OP_NOT_FOUND = "Operator not found";

    // Definition of operators
    final static String OPERATORS = "+-*/^";

    // Method used in REPL
    double eval(String expr) {
        if (expr.length() == 0) {
            return NaN;
        }
        // TODO List<String> tokens = tokenize(expr);
        // TODO List<String> postfix = infix2Postfix(tokens);
        // TODO double result = evalPostfix(postfix);
        return 0; // result;
    }

    // ------  Evaluate RPN expression -------------------

    // TODO Eval methods

    double applyOperator(String op, double d1, double d2) {
        switch (op) {
            case "+":
                return d1 + d2;
            case "-":
                return d2 - d1;
            case "*":
                return d1 * d2;
            case "/":
                if (d1 == 0) {
                    throw new IllegalArgumentException(DIV_BY_ZERO);
                }
                return d2 / d1;
            case "^":
                return pow(d2, d1);
        }
        throw new RuntimeException(OP_NOT_FOUND);
    }

    // ------- Infix 2 Postfix ------------------------

    // TODO Methods

    ArrayList<String> infix2Postfix(ArrayList<String> inputList) {
        ArrayList<String> postFix = new ArrayList<>();
        Deque<String> opStack = new ArrayDeque<>();
        int counter = 0;
        boolean inParantheses = false;

        for (String element : inputList) {
            counter++;
            //TODO maybe change
            boolean isDigit = !(OPERATORS.contains(element)) && !("()".contains(element));
            boolean isOperator = OPERATORS.contains(element);

            if (isDigit) {
                postFix.add(element);
            } else if (isOperator && !inParantheses) {
                if (getAssociativity(element) == Assoc.LEFT) {
                    //TODO make into method si
                    handleLeftOp(postFix, opStack, element);
                } else if (getAssociativity(element) == Assoc.RIGHT) {
                    opStack.push(element);
                }
                //if operator and is in parentheses
            } else {
                //Parentheses handling
                if (element.equals("(")) {
                    inParantheses = true;

                }

            }
            //clears what's left in stack at the end (and adds to list)
            popStack(inputList, postFix, opStack, counter);
        }

        return postFix;
    }

    private void popStack(ArrayList<String> inputList, ArrayList<String> postFix, Deque<String> opStack, int counter) {
        //Empty stack at the end
        if (counter >= inputList.size() - 1) {
            while (!opStack.isEmpty()) {
                postFix.add(opStack.pop());
            }
        }
    }

    private void handleLeftOp(ArrayList<String> postFix, Deque<String> opStack, String element) {
        if (opStack.isEmpty()) {
            opStack.push(element);
        } else {
            if (greaterPrecedence(element, opStack.peek())) {
                opStack.push(element);
            } else {
                while (!opStack.isEmpty()) {
                    postFix.add(opStack.pop());
                }
                opStack.push(element);
            }
        }
    }

    int getPrecedence(String op) {
        if ("+-".contains(op)) {
            return 2;
        } else if ("*/".contains(op)) {
            return 3;
        } else if ("^".contains(op)) {
            return 4;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    boolean greaterPrecedence(String element, String stackEl) {

        return getPrecedence(element) > getPrecedence(stackEl);
    }

    enum Assoc {
        LEFT,
        RIGHT
    }

    Assoc getAssociativity(String op) {
        if ("+-*/".contains(op)) {
            return Assoc.LEFT;
        } else if ("^".contains(op)) {
            return Assoc.RIGHT;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }


    // ---------- Tokenize -----------------------

    // TODO Methods to tokenize
    public ArrayList<String> tokenize(String s) {
        StringBuilder num = new StringBuilder();
        ArrayList<String> list = new ArrayList<>();
        //Split string to charArr
        char[] inputChars = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            if (inputChars[i] != ' ') {
                //if current char is digit (and not empty) append to StringBuilder
                if (Character.isDigit(inputChars[i])) {
                    num.append(inputChars[i]);
                } else {
                    //when operator is reached, transfer StringBuilder to list, empty Stringbuilder, add operand to list
                    list.add(num.toString());
                    num.setLength(0);
                    list.add(Character.toString(inputChars[i]));
                }
                //For adding numbers after last operand
                if (i == s.length() - 1 && Character.isDigit(inputChars[i])) {
                    list.add(num.toString());
                }
            }
        }
        return list;

    }


}
