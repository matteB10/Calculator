package calc;
class Scratch {
    public static void main(String[] args) {
        String s = "  1 +  2";
        s = s.trim();
        char[] input = s.toCharArray();
        String operators = "*+-/^";
        boolean readOperand = false;
        boolean readOperator = false;
        int spaces = 0;
        int operands = 0;

        for(int i = 0; i < input.length; i++){
            char ch = input[i];
            if(readOperator && i == 1){
                throw new IllegalArgumentException();
            }

            if(Character.isDigit(ch)){
                readOperand = true;
                operands++;
            }
            if(operators.contains((Character.toString(ch)))){
                readOperator = true;
                readOperand = false;
            }
            if(Character.toString(ch).equals(" ")){
                spaces++;
            }

            if(spaces > 0 && readOperand && Character.isDigit(ch));{
                //throw new
            }

        }
    }
}