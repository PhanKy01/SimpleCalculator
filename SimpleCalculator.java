import java.util.*;
import java.lang.Math;

public class SimpleCalculator
{
    // Mapping the supported non-unary operators to their precedence.
    static final Map<String, Integer> OPERATORS = new HashMap<>();
 
    
    public static void main(String[] args)
    {
        init();
        
        // Get the mathematical expression without white spaces.
        String expression = getExpression().replaceAll("\\s","");
        System.out.println(expression);
        
        // Try to convert the 'expression' to RPN. Catch any error.
//         try
//         {
            System.out.println("rpn: "+infixToRPN(expression).toString()+"end");// 
//         }
//         catch (Exception e) {System.out.println("err end.");}
        
        System.out.println("end.");
//         System.out.println("end."+Double.parseDouble("(0.23)d"));
    }
    
    /** 
     * This method prompts the user to enter the mathematical expression.
     * @return The string the user entered.
     */
    static String getExpression()
    {
        System.out.println("Welcome to Simple Calculator.");
        
        System.out.println("\nSupported operation:");        
        System.out.println("_Parenthesis (only () is supported, [] and {} are not supported).");
        System.out.println("_Simple operation: +, -, *, /, ^");
        System.out.println("_Trigonometric functions (in radian): sin, cos, tan, cot");
        System.out.println("_Logarithmic functions: ln (natural logarithm), log (log base 10, or common log)");
        
        System.out.println("\nPlease enter a mathematical expression:");
        Scanner console = new Scanner(System.in);
        return console.nextLine();
    }
    
    /** 
     * This method performs math operations.
     * @param operator The mathematical operator in string form.
     * @param arg1 A real number.
     * @param arg2 A real number.
     * @return The result after performing the math operation, or -123 if there's error.
     */
    static double doOperation(String operator, double arg1, double arg2)
    {
        if (operator.equals("+")) return (arg1 + arg2);
        if (operator.equals("-")) return (arg1 - arg2);
        if (operator.equals("*")) return (arg1 * arg2);
        if (operator.equals("/")) return (arg1 / arg2);
        if (operator.equals("^")) return Math.pow(arg1, arg2);
        
        if (operator.equals("sin")) return Math.sin(arg1);
        if (operator.equals("cos")) return Math.cos(arg1);
        if (operator.equals("tan")) return Math.tan(arg1);
        if (operator.equals("cot")) return 1.0 / Math.tan(arg1);
        
        if (operator.equals("ln")) return Math.log(arg1);
        if (operator.equals("log")) return Math.log10(arg1);
        
        if (operator.equals("~")) return (arg1 * (-1));
        
        System.err.println("Operator \"" + operator + "\" not implemented");
        return -123;
    }
    
    /** 
     * This method convert an infix math expression to Reverse Polish Notation (RPN), aka Postfix Notation.
     * @param exp The infix math expression to be converted to RPN.
     * @return A stack of string tokens that is the RPN of the provided infix math expression (operator tokens 
     * have a "$" at the start).
     */
    public static Stack<String> infixToRPN(String exp) 
    {
        // Stack of string 'rpn' holds the RPN representation.
        Stack<String> rpn = new Stack<>();
        // String 'operandStr' holds the operand (number) in string form.
        String operandStr = "";
        // String 'operator' holds the operator string (> than 1 character).
        String operatorStr  = "";
        
        // Make 'opStack' stack for the operators.
        Stack<String> opStack = new Stack<String>();
        
        
        int i = 0;
        while (i < exp.length())
        {
            // The current character 'c'.
            char c = exp.charAt(i);
            
            
            // If current character 'c' is in an operand, begin building 'operandStr' (to convert to a real
            // number later).
            if (Character.isDigit(c) || c == '.')
                operandStr += c;
            // If current character 'c' is no longer in the operand part, check for any available 'operandStr'
            // and convert it to a real number.
            else if (operandStr.length() > 0)
            {
                // Try to convert 'operandStr' a real number later. Warn user, end program if error occurs.
                try
                {
                    Double number = Double.parseDouble(operandStr);
                    System.out.println("number: "+number);
                    // Putting the operand to the 'rpn'.
                    rpn.push(operandStr);
                    // Reset for the next operand.
                    operandStr = "";
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Error when parsing number.");
                    return null;
                }
            }
            
            
            // If current character 'c' is a letter (in an operator expression), begin building 'operatorStr'.
            if (Character.isLetter(c))
                operatorStr += c;
            // If current character 'c' is no longer in an operator expression, check for any available
            // 'operatorStr'.
            else if (operatorStr.length() > 0)
            {
                System.out.println("operatorStr: " + operatorStr);
                // Putting the 'operatorStr' to the 'opStack' always (only because the supported 'operatorStr'
                // are all unary operators).
                opStack.push(operatorStr);
                System.out.println(opStack.toString());
                // Reset for the next operator expression.
                operatorStr = "";
            }
            
            
            // If current character 'c' at the end (the entire 'exp' string is almost processed) ... 
            if (i == exp.length() - 1)
            {
                // ... then check if any 'operandStr' is available, convert it to a real number. This is in
                // case the provided expression 'exp' is ending with a number, and we need the number. 
                if (operandStr.length() > 0)
                {
                    try
                    {
                        Double number = Double.parseDouble(operandStr);
                        System.out.println("number: "+number);
                        // Putting the operand to the 'rpn'.
                        rpn.push(operandStr);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("Error parsing number.");
                        return null;
                    }
                }
                // ... then check if the ending character 'c' is a letter. A math expression should not
                // end with a string operator.
                if (Character.isLetter(c))
                {
                    System.out.println("Error parsing expression.");
                    return null;
                }
            }

            if (!(Character.isDigit(c) || c == '.' || Character.isLetter(c)))
            {
                // If scanned '(', push to the 'opStack' (as string) always.
                if (c == '(')
                {
                    opStack.push("(");
                    System.out.println(opStack.toString());
                }
                // If scanned ')' ... 
                else if (c == ')') 
                {
                    // ...keep popping from the 'opStack' until a "(" is met, forming the () pair.
                    while (!opStack.isEmpty() && !opStack.peek().equals("("))
                    {
                        rpn.push("$" + opStack.pop());
                    }
                    // If no "(" is met, expression is unbalanced.
                    if (!opStack.isEmpty() && !opStack.peek().equals("("))
                    {
                        System.out.println("Unbalanced expression. Check your parenthesis.");
                        return null;
                    }
                    else
                        opStack.pop();
                    
                    System.out.println(opStack.toString());
                }
                // If scanned an operator.
                else
                {
                    // The '~' will be used internally to denote the unary negation operator. So any entered '~'
                    // character is invalid.
                    if (c == '~')
                    {
                        System.out.println("Error parsing expression.");
                        return null;
                    }
                    // If the scanned operator is a unary negation (the '-' is the first thing in the expression,
                    // or it comes after another operator (not after a number, not after a finished expression)).
                    else if ((c == '-') && ((i == 0) || (!Character.isDigit(exp.charAt(i-1)) && (exp.charAt(i-1) != '.') && (exp.charAt(i-1) != ')'))))
                    {
                        // Using '~' for the unary negation sign.
                        opStack.push("~");
                    }
                    // For all other operators.
                    else
                    {
                        System.out.println("opStack before while: "+opStack.toString());
                        System.out.println("c before while: "+c);
                        // If 'opStack' not empty, keep popping operators with <= precedence from 'opStack'
                        // to 'rpn'. Remember to keep 'c' in string form.
                        while (!opStack.isEmpty() && (OPERATORS.get("" + c) <= OPERATORS.get(opStack.peek())))
                        {
                            // Any "(" remaining in 'opStack' indicates an unbalance expression.
                            if (opStack.peek().equals("("))
                            {
                                System.out.println("Unbalanced expression. Check your parenthesis.");
                                return null;
                            }
                            rpn.push("$" + opStack.pop());
                        }
                        // Then push the scanned operator (as string) to 'opStack'.
                        opStack.push("" + c);
                    }
                }
            }
            
            System.out.println("i: "+i);
            i++;
        }
        
        // Pop all the remaining operators from 'opStack' to 'rpn'.
        while (!opStack.isEmpty())
        {
            // Any "(" remaining in 'opStack' indicates an unbalance expression.
            if (opStack.peek().equals("("))
            {
                System.out.println("Unbalanced expression. Check your parenthesis.");
                return null;
            } 
            rpn.push("$" + opStack.pop());
        }
        
        return rpn;
    }
    
    
    
    static void init()
    {
        OPERATORS.put("(", 0);
        
        OPERATORS.put("+", 1);
        OPERATORS.put("-", 1);
        OPERATORS.put("*", 2);
        OPERATORS.put("/", 2);
        OPERATORS.put("^", 3);
        
        OPERATORS.put("sin", 4);
        OPERATORS.put("cos", 4);
        OPERATORS.put("tan", 4);
        OPERATORS.put("cot", 4);
        
        OPERATORS.put("ln", 4);
        OPERATORS.put("log", 4);
        
        OPERATORS.put("~", 5);
    }
}