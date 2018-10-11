package calc;

import java.util.*;

import static calc.CheckParen.checkParentheses;
import static java.lang.Double.NaN;
import static java.lang.Math.pow;

//Paggan är cool B)
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
        char[] input = tokenize(expr);
        ArrayList<String> tokens = controlInput(input);

        System.out.println(tokens.toString());
        //Lastly, Check for invalid parentheses
        checkParentheses(tokens);

        ArrayList<String> postfix = infix2Postfix(tokens);
        double result = evalPostFix(postfix);

        return result; // result;
    }

    // ------  Evaluate RPN expression -------------------

//Call expression depending on input (Jocke wrote this method)
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
//The string convertion from infix to postfix, no caclulation.

    ArrayList<String> infix2Postfix(ArrayList<String> inputList){
        ArrayList<String> postFix = new ArrayList<>();
        Deque<String> opStack = new ArrayDeque<>();
        int counter = 0;

        for (String element : inputList) {
            counter++;

            boolean isParen = ("()".contains(element));
            boolean isOperator = OPERATORS.contains(element);
            int tempNum = 0;
            boolean isNumber;

            try {
                tempNum = Integer.parseInt(element);
                isNumber = true;
            }catch(Exception e){
                isNumber = false;
            }

            if (isNumber) {
                postFix.add(element);
            } else if (isOperator) {
                if (getAssociativity(element) == Assoc.LEFT) {
                    handleLeftOp(postFix, opStack, element);
                } else if (getAssociativity(element) == Assoc.RIGHT) {
                    opStack.push(element);
                }
                //if parentheses
            } else if(isParen){
                handleParentheses(postFix, opStack, element);

            }else{
                throw new RuntimeException(MISSING_OPERAND);
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
            //If element on stack is a parentheses or has great precedence, push.
            if (opStack.peek().equals("(") || greaterPrecedence(element, opStack.peek())) {
                opStack.push(element);
            } else {
                //Pops stack all the way to start parentheses (if there is one) + adds to postfix
                while (!opStack.isEmpty() && !(opStack.peek().equals("("))) {
                    postFix.add(opStack.pop());
                }
                opStack.push(element);
            }
        }
    }
    //Return precedence of operator. Exception is never thrown.
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

    //Used to check if precedence of current operator is greater than
    //the operator we comparing with.
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

    public ArrayList<String> controlInput(char[] tokens){
        //StringBuilder numBuilder = new StringBuilder();
        //ArrayList<String> tokenList = new ArrayList<>();
        StringBuilder numBuilder = new StringBuilder();

        boolean readOperand = false;
        boolean readOperator = false;
        int blankSpaces = 0;
        int numOperators = 0;
        ArrayList<String> outputList = new ArrayList<>();

        for(int i = 0; i < tokens.length; i++){
            char ch = tokens[i];

            //Two digits with space in-between without operator
            if (readOperand && blankSpaces > 0 && Character.isDigit(ch)) {
                throw new RuntimeException(MISSING_OPERATOR);
            }
            //If first character is operator
            if (i == 1 && readOperator) {
                throw new RuntimeException(MISSING_OPERAND);
            }

            //TODO: DESSA BORDE VARA ELSE IF VA?
            //Set readOperand to false, if false after loop exit, missing operand after last operator i.e (1 + 2 +)
            if (OPERATORS.contains(Character.toString(ch))) {
                readOperand = false;
                readOperator = true;
                numOperators++;
                //Need to add digit to Stringbuilder, only does this if two digits right after one another, needed to create numbers with more than one digit
                outputList.add(numBuilder.toString());
                //Then reset numBuilder so the next number is created correctly
                numBuilder.setLength(0);
                //Finally, add Operator to output, so the order is correct when passed to infix2postfix
                outputList.add(Character.toString(ch));
            }

            if (Character.isDigit(ch)) {
                if(readOperator){
                    blankSpaces = 0;
                }
                readOperand = true;
                numBuilder.append(ch);
                //outputList.add(Character.toString(ch));
            }

            if (Character.toString(ch).equals(" ")) {
                blankSpaces++;
            }

            if(numBuilder.length() != 0 && i == tokens.length -1){
                outputList.add(numBuilder.toString());
            }

            if("()".contains(Character.toString(ch))){
                outputList.add(Character.toString(ch));
            }
        }

        //After loop, check if it exited with operator as last character, or if it didn't contain any operators at all.
        if (numOperators == 0) {
            throw new RuntimeException(MISSING_OPERATOR);
        } else if(!readOperand){
            throw new RuntimeException(MISSING_OPERAND);
        }




        return outputList;
    }

    public char[] tokenize(String s) {
        s = s.trim();
        char[] inputChars = s.toCharArray();

        return inputChars;
    }
    //Called in method tokenize, used for converting chars to a list of strings.
    //Numbers with more than 1 digit need to concatenate, done with stringBuilder before adding to list.
    private void addToTokenlist(StringBuilder numBuilder, ArrayList<String> tokenList, char element) {
        if (numBuilder.length() == 0) {
            tokenList.add(Character.toString(element));
        } else {
            tokenList.add(numBuilder.toString());
            numBuilder.setLength(0);
            tokenList.add(Character.toString(element));
        }
    }
    //Do all calculations
    //method parameter: a list with the expression in postfix order
    //returns the result (a double) of the calculation.
    public double evalPostFix(ArrayList<String> postfix) {
        double number;
        Deque<Double> stack = new ArrayDeque<>();
        for (String element : postfix) {
            //if current element in expression is an operator
            if (OPERATORS.contains(element)) {
                double first = stack.pop();
                double second = stack.pop();
                //calculate the result of 'first' and 'second' with 'element' as operator
                //push result back to stack
                stack.push(applyOperator(element, first, second));

            //if current element in expression is a number, push it to stack
            } else {
                number = Double.valueOf(element);
                stack.push(number);
            }
        }
        //return result of calculation
        return stack.pop();
    }


    //Check if input is invalid and operand is missing, throws Exception if
    //two operators in sequence or if expression starts/ends with operator.
    void checkMissingOperand(int i, char[] inputChars){
        //check if current element is operator
        boolean currentIsOp = OPERATORS.contains(Character.toString(inputChars[i]));

        if (i > 0) {
            //Check if last element was operator
            boolean lastIsOp = OPERATORS.contains(Character.toString(inputChars[i-1]));
            //if two operators in a row, or expression start or ends with an operator.
            if((lastIsOp && currentIsOp) || (currentIsOp && i == inputChars.length -1)){
                throw new RuntimeException(MISSING_OPERAND);
            }

        }else if(currentIsOp){
            throw new RuntimeException(MISSING_OPERAND);
        }

    }
}