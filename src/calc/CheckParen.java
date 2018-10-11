package calc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;

import static java.lang.System.out;
import static java.lang.System.setOut;

/*
 *
 *  Use a Stack to check parentheses (balanced and correct nesting)
 *  The parentheses are: (), [] and {}
 *
 *  See:
 *  - UseAStack
 *  - WhyInterface
 *  - SwitchStatement
 *
 */
public class CheckParen {

    /*public static void main(String[] args) {
        new CheckParen().program();
    }

    void program() {
        ArrayList<String> test = new ArrayList<String>();
        Collections.addAll(test,")","abs","abc",")","asd",")");
        checkParentheses(test);
    }*/

    private static boolean isOpeningParenthesis(char c) {
        boolean parenthesis;
        if (c == '(') {
            parenthesis = true;
        } else {
            parenthesis = false;
        }
        return parenthesis;
    }

    public static boolean checkParentheses(ArrayList<String> inputList) {
        Deque<Character> stack = new ArrayDeque<>();
        boolean balanced = false;

        char[] letters = onlyParen(inputList);
        for (int i = 0; i < letters.length; i++) {
            if (isOpeningParenthesis(letters[i])) {
                stack.push(letters[i]);
            } else if(!(stack.isEmpty())) {//If not opening parentheses, assume closing parentheses
                char toCompare = letters[i];
                char topOfStack = stack.peek();
                boolean match = matchesStack(toCompare, topOfStack);
                if (match) {
                    stack.pop();
                }
            }
            else{
                return false;
            }
        }
        if(!stack.isEmpty()){
            throw new RuntimeException(Calculator.MISSING_OPERATOR);
        }

        return stack.isEmpty();
    }

    private static char[] onlyParen(ArrayList<String> inputList){
        ArrayList<Character> returnList = new ArrayList<>();
        String tempString;
        char[] charArray;

        for(String element : inputList){
            if(("()".contains(element))){
                returnList.add(element.charAt(0));
            }
        }
        charArray = new char[returnList.size()];
        for(int i = 0; i < charArray.length; i++){
            charArray[i] = returnList.get(i);
        }

        return charArray;
    }

    private static boolean matchesStack(char c, char topOfStack) {
        if (c == ')' && topOfStack == '(') {
            return true;
        } else if (c == ']' && topOfStack == '[') {
            return true;
        } else if (c == '}' && topOfStack == '{') {
            return true;
        } else {
            return false;
        }
    }


}
