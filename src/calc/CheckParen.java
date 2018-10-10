package calc;

import java.util.ArrayDeque;
import java.util.Deque;

import static java.lang.System.out;

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
/*
    public static void main(String[] args) {
        new Ex4CheckParen().program();
    }

    void program() {
        // All should be true
        out.println(checkParentheses("()"));
        out.println(checkParentheses("(()())"));
        out.println(!checkParentheses("(()))")); // Unbalanced
        out.println(!checkParentheses("((())")); // Unbalanced

        out.println(checkParentheses("({})"));
        out.println(!checkParentheses("({)}"));  // Bad nesting
        out.println(checkParentheses("({} [()] ({}))"));
        out.println(!checkParentheses("({} [() ({)})"));  // Unbalanced and bad nesting
    }
*/
    private boolean isOpeningParenthesis(char c) {
        boolean parenthesis;
        if (c == '{' || c == '[' || c == '(') {
            parenthesis = true;
        } else {
            parenthesis = false;
        }
        return parenthesis;
    }

    public boolean checkParentheses(String string) {
        Deque<Character> stack = new ArrayDeque<>();
        boolean balanced = false;

        char[] letters = string.toCharArray();
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

        return stack.isEmpty();
    }

    boolean matchesStack(char c, char topOfStack) {
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

    // This is interesting because have to return, but what if no match?!?
    char matching(char ch) {
        //char c =  must initialize but to what?!
        switch (ch) {
            case ')':
                return '(';  // c = '('
            case ']':
                return '[';
            case '}':
                return '{';
            default:
                // return c;
                throw new IllegalArgumentException("No match found");
        }
    }
}
