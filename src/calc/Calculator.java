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
        ArrayList<String> tokens = tokenize(expr);

        //Check for invalid parentheses
        checkParen(tokens);

        ArrayList<String> postfix = infix2Postfix(tokens);
        double result = evalPostFix(postfix);
        return result; // result;
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

    ArrayList<String> infix2Postfix(ArrayList<String> inputList){
        ArrayList<String> postFix = new ArrayList<>();
        Deque<String> opStack = new ArrayDeque<>();
        int counter = 0;

        for (String element : inputList) {
            counter++;
            //TODO maybe change
            boolean isParen = ("()".contains(element));
            boolean isOperator = OPERATORS.contains(element);
            boolean isDigit = !isParen && !isOperator;

            if (isDigit) {
                postFix.add(element);
            } else if (isOperator) {
                if (getAssociativity(element) == Assoc.LEFT) {
                    //TODO make into method si
                    handleLeftOp(postFix, opStack, element);
                } else if (getAssociativity(element) == Assoc.RIGHT) {
                    opStack.push(element);
                }
                //if parentheses
            } else if(isParen){
                handleParentheses(postFix, opStack, element);

            }
            //clears what's left in stack at the end (and adds to list)
            if (counter >= inputList.size()) {
                popStack(inputList, postFix, opStack, counter);
            }
        }

        return postFix;
    }

    private void handleParentheses(ArrayList<String> postFix, Deque<String> opStack, String element){
        //Parentheses handling
        if (element.equals("(")) {
            opStack.push(element);

        } else if(element.equals(")")){
            while (!(opStack.peek().equals("("))) {
                postFix.add(opStack.pop());
            }
            opStack.pop();
            //Poppa allt till startparantes
        }
        else{
            //not digit, not known operator and not a parantheses.
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    private void popStack(ArrayList<String> inputList, ArrayList<String> postFix, Deque<String> opStack, int counter) {
        //Empty stack at the end
        while (!opStack.isEmpty()) {
            postFix.add(opStack.pop());
        }
    }

    private void handleLeftOp(ArrayList<String> postFix, Deque<String> opStack, String element) {
        if (opStack.isEmpty()) {
            opStack.push(element);
        } else {
            if (opStack.peek().equals("(") || greaterPrecedence(element, opStack.peek())) {
                opStack.push(element);
            } else {
                while (!opStack.isEmpty() && !(opStack.peek().equals("("))) {
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
        StringBuilder numBuilder = new StringBuilder();
        ArrayList<String> tokenList = new ArrayList<>();
        //Split string to charArr
        s = s.trim();
        char[] inputChars = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {

            if (inputChars[i] != ' ') {
                //Errorhandling
                checkMissingOperand(i,inputChars);

                //if current char is digit (and not empty) append to StringBuilder
                if (Character.isDigit(inputChars[i])) {
                    numBuilder.append(inputChars[i]);

                    //if operator
                } else {
                    //when operator is reached, transfer StringBuilder to tokenList, empty Stringbuilder, add operand to tokenList
                    addToTokenlist(numBuilder, tokenList, inputChars[i]);
                }
                //For adding numbers after last operand
                if (i == s.length() - 1 && Character.isDigit(inputChars[i])) {
                    tokenList.add(numBuilder.toString());
                }
            }
        }

        return tokenList;

    }

    private void addToTokenlist(StringBuilder numBuilder, ArrayList<String> tokenList, char element) {
        if (numBuilder.length() == 0) {
            tokenList.add(Character.toString(element));

        } else {
            tokenList.add(numBuilder.toString());
            numBuilder.setLength(0);
            tokenList.add(Character.toString(element));
        }
    }

    public double evalPostFix(ArrayList<String> postfix) {
        double number;
        Deque<Double> stack = new ArrayDeque<>();
        for (String element : postfix) {
            if (OPERATORS.contains(element)) {
                double first = stack.pop();
                double second = stack.pop();
                stack.push(applyOperator(element, first, second));


            } else {
                number = Double.valueOf(element);
                stack.push(number);
            }
        }
        return stack.pop();
    }

    void checkMissingOperand(int i, char[] inputChars){
        boolean currentIsOp = OPERATORS.contains(Character.toString(inputChars[i]));

        if (i > 0) {
            boolean lastIsOp = OPERATORS.contains(Character.toString(inputChars[i-1]));
            boolean lastIsParen = "()".contains(Character.toString(inputChars[i-1]));
            //if two operators in a row, or expression start or ends with an operator.
            if((lastIsOp && currentIsOp) || (currentIsOp && i == inputChars.length -1)){
                throw new RuntimeException(MISSING_OPERAND);
            }

        }else if(currentIsOp){
            throw new RuntimeException(MISSING_OPERAND);
        }
    }

}