package calc;

import java.util.ArrayList;

class Scratch {
    public static void main(String[] args) {
        ArrayList<String> outputList = new ArrayList<>();
        String s = "  1 +  2";
        s = s.trim();
        char[] tokens = s.toCharArray();
        String operators = "*+-/^";
        boolean readOperand = false;
        boolean readOperator = false;
        int blankSpaces = 0;
        int numOperators = 0;

        for (int i = 0; i < tokens.length; i++) {
            char ch = tokens[i];

            //Two digits with space in-between without operator
            if (readOperator && blankSpaces > 0 && Character.isDigit(ch)) {
                throw new IllegalArgumentException();
            }
            //If first character is operator
            if (i == 1 && readOperator) {
                throw new IllegalArgumentException();
            }
            //Set readOperand to false, if false after loop exit, missing operand after last operator i.e (1 + 2 +)
            if (operators.contains(Character.toString(ch))) {
                readOperand = false;
                readOperator = true;
                numOperators++;
                outputList.add(Character.toString(ch));
            }
            if (Character.isDigit(ch)) {
                readOperand = true;
                outputList.add(Character.toString(ch));
            }

            if (Character.toString(ch).equals(" ")) {
                blankSpaces++;
            }
        }
        if (numOperators == 0 || !readOperand) {
            throw new IllegalArgumentException();
        }
        //After loop, check if it exited with operator as last character, or if it didn't contain any operators at all.
    }
}