import java.util.*;
import java.lang.Math;


/*** @author Ky Phan @cwu, SID: 41882471
 *   @description This program compares function as a simple calculator.
 */
public class SimpleCalculator
{
    // Mapping the supported non-unary operators to their precedence.
    static final Map<String, Integer> OPERATORS_PREC = new HashMap<>();
    // Mapping the supported non-unary operators to their number of arguments.
    static final Map<String, Integer> OPERATORS_ARGS = new HashMap<>();
    
    /** 
     * This method initializes the 'OPERATORS_PREC' Map which maps the supported operators (as string) with
     * their respective precedence level (as int). Also initializes the 'OPERATORS_ARGS' Map which maps the
     * supported operators (as string) with their respective number of arguments (as int)
     */
    static void init()
    {
        // Initializes 'OPERATORS_PREC' Map.
        OPERATORS_PREC.put("(", 0);
        
        OPERATORS_PREC.put("+", 1);
        OPERATORS_PREC.put("-", 1);
        OPERATORS_PREC.put("*", 2);
        OPERATORS_PREC.put("/", 2);
        OPERATORS_PREC.put("^", 3);
        
        OPERATORS_PREC.put("sin", 4);
        OPERATORS_PREC.put("cos", 4);
        OPERATORS_PREC.put("tan", 4);
        OPERATORS_PREC.put("cot", 4);
        
        OPERATORS_PREC.put("ln", 4);
        OPERATORS_PREC.put("log", 4);
        
        OPERATORS_PREC.put("~", 5);
        
        // Initializes 'OPERATORS_ARGS' Map.
        OPERATORS_ARGS.put("+", 2);
        OPERATORS_ARGS.put("-", 2);
        OPERATORS_ARGS.put("*", 2);
        OPERATORS_ARGS.put("/", 2);
        OPERATORS_ARGS.put("^", 2);
        
        OPERATORS_ARGS.put("sin", 1);
        OPERATORS_ARGS.put("cos", 1);
        OPERATORS_ARGS.put("tan", 1);
        OPERATORS_ARGS.put("cot", 1);
        
        OPERATORS_ARGS.put("ln", 1);
        OPERATORS_ARGS.put("log", 1);
        
        OPERATORS_ARGS.put("~", 1);
    }
    
    
    public static void main(String[] args)
    {
        // Initialize the 'OPERATORS_PREC' Map and the 'OPERATORS_ARGS' Map.
        init();
        
        
        // Get the mathematical expression without white spaces.
        String expression = getExpression().replaceAll("\\s","");
        // Remove the '=' at the end if it's there.
        if (expression != null && expression.length() > 0 && expression.charAt(expression.length()-1) == '=')
            expression = expression.substring(0, expression.length()-1);
        
        // Check if 'expression' is empty.
        if (expression != null)
        {
            // Try to evaluate the 'expression'. Catch any Exception.
            try
            {
                Queue<String> rpn = new LinkedList<>();
                rpn = infixToRPN(expression);
                double result = evaluateRPN(rpn);
                System.out.println("result: " + String.format("%.4f", result));
            }
            catch (Exception e) {}
        }
        
        System.out.println("Program end.");
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
     * This method convert an infix math expression to Reverse Polish Notation (RPN), aka Postfix Notation.
     * @param exp The infix math expression to be converted to RPN.
     * @return A stack of string tokens that is the RPN of the provided infix math expression (operator tokens 
     * have a "$" at the start).
     */
    public static Queue<String> infixToRPN(String exp) 
    {
        // Queue of string 'rpn' holds the RPN representation.
        Queue<String> rpn = new LinkedList<>();
        // String 'operandStr' holds the operand (number) in string form.
        String operandStr = "";
        // String 'operator' holds the operator string (> than 1 character).
        String operatorStr  = "";
        // Make 'opStack' stack for the operators.
        Stack<String> opStack = new Stack<String>();
        
        
        // Processing each character of 'exp'.
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
                    double number = Double.parseDouble(operandStr);
                    // Putting the operand to the 'rpn'.
                    rpn.add(operandStr);
                    // Reset for the next operand.
                    operandStr = "";
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Error in 'infixToRPN' method. Error when parsing number.");
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
                // Putting the 'operatorStr' to the 'opStack' always (only because the supported 'operatorStr'
                // are all unary operators).
                opStack.push(operatorStr);
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
                        double number = Double.parseDouble(operandStr);
                        // Putting the operand to the 'rpn'.
                        rpn.add(operandStr);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("Error in 'infixToRPN' method. Error parsing number.");
                        return null;
                    }
                }
                // ... then check if the ending character 'c' is a letter. A math expression should not
                // end with a string operator.
                if (Character.isLetter(c))
                {
                    System.out.println("Error in 'infixToRPN' method. Bad expression placement at the end.");
                    return null;
                }
            }
            
            
            // Getting single character operator (+ - * / ^ ...).
            if (!(Character.isDigit(c) || c == '.' || Character.isLetter(c)))
            {
                // If scanned '(', push to the 'opStack' (as string) always.
                if (c == '(')
                {
                    opStack.push("(");
                }
                // If scanned ')' ... 
                else if (c == ')') 
                {
                    // ...keep popping from the 'opStack' until a "(" is met, forming the () pair.
                    while (!opStack.isEmpty() && !opStack.peek().equals("("))
                        rpn.add("$" + opStack.pop());
                    // If no "(" is met, expression is unbalanced.
                    if (!opStack.isEmpty() && !opStack.peek().equals("("))
                    {
                        System.out.println("Error in 'infixToRPN' method. Unbalanced expression. Check your parenthesis.");
                        return null;
                    }
                    else
                        opStack.pop();
                }
                // If scanned an operator.
                else
                {
                    // The '~' will be used internally to denote the unary negation operator. So any entered 
                    // '~' character is invalid.
                    if (c == '~')
                    {
                        System.out.println("Error in 'infixToRPN' method. Operator \"~\" not implemented");
                        return null;
                    }
                    // If the scanned operator is a unary negation ('-' is the first thing in the expression,
                    // or it is after another operator (not after a number, not after a finished expression)).
                    else if ((c == '-') && ((i == 0) || (!Character.isDigit(exp.charAt(i-1)) && (exp.charAt(i-1) != '.') && (exp.charAt(i-1) != ')'))))
                    {
                        // Using '~' for the unary negation sign.
                        opStack.push("~");
                    }
                    // For all other operators.
                    else
                    {
                        // Try to catch when the operator is not available in 'OPERATORS_PREC' Map.
                        try
                        {
                            // If 'opStack' not empty, keep popping operators with <= precedence from 'opStack'
                            // to 'rpn'. Remember to keep 'c' in string form.
                            while (!opStack.isEmpty() && (OPERATORS_PREC.get("" + c) <= OPERATORS_PREC.get(opStack.peek())))
                            {
                                // Any "(" remaining in 'opStack' indicates an unbalance expression.
                                if (opStack.peek().equals("("))
                                {
                                    System.out.println("Error in 'infixToRPN' method. Unbalanced expression. Check your parenthesis.");
                                    return null;
                                }
                                rpn.add("$" + opStack.pop());
                            }
                            // Then push the scanned operator (as string) to 'opStack'.
                            opStack.push("" + c);
                        }
                        // If operator is not available in 'OPERATORS_PREC' Map, display error, end method.
                        catch (NullPointerException e)
                        {
                                System.out.println("Error in 'infixToRPN' method. Unsupported operator detected.");
                                return null;
                        }
                    }
                }
            }
            
            // Move to next character position.
            i++;
        }
        
        // Pop all the remaining operators from 'opStack' to 'rpn'.
        while (!opStack.isEmpty())
        {
            // Any "(" remaining in 'opStack' indicates an unbalance expression.
            if (opStack.peek().equals("("))
            {
                System.out.println("Error in 'infixToRPN' method. Unbalanced expression. Check your parenthesis.");
                return null;
            } 
            rpn.add("$" + opStack.pop());
        }
        
        return rpn;
    }
    
    
    /** 
     * This method take in a Queue<String> representation of an RPN expression, where each String is an operand or an 
     * operator (operator has '$' attached to front of String). Then it calculate the RPN expression to get the result.
     * @param rpn The Queue<String> representation of an RPN expression where each String is an operand or an operator 
     * (operator has '$' attached to front of String).
     * @return The result of the RPN expression.
     */
    static double evaluateRPN(Queue<String> rpn)
    {        
        Stack<Double> values = new Stack<>();
        double val1 = 0; 
        double val2 = 0;
        
        // While the 'rpn' expression queue not empty.
        while (!rpn.isEmpty())
        {
            // Collect all tokens.
            String token = rpn.remove();            
            
            // If encountered number, put it to 'values' stack. Operators have '$' at the start.
            if (token.charAt(0) != '$')
                values.push(Double.parseDouble(token));
            // If encountered operator.
            else
            {
                // Remove the '$' at the front.
                String operator = token.substring(1);
                // Check if the operator is supported.
                if (OPERATORS_ARGS.containsKey(operator))
                {
                    // Unary operations.
                    if (OPERATORS_ARGS.get(operator) == 1)
                        val1 = values.pop();
                    // Binary operations.
                    else if (OPERATORS_ARGS.get(operator) == 2)
                    {
                        val2 = values.pop();
                        val1 = values.pop();
                    }
                    
                    values.push(doOperation(operator, val1, val2));
                }
                // If the operator is NOT supported, end method, display error code.
                else
                {
                    System.out.println("Error in 'evaluateRPN' method. Unsupported operator detected.");
                    return 0;
                }
            }
        }
        
        return values.peek();
    }
    
    
    /** 
     * This method performs math operations.
     * @param operator The mathematical operator in string form.
     * @param arg1 A real number.
     * @param arg2 A real number.
     * @return The result after performing the math operation, or an error code.
     */
    static double doOperation(String operator, double arg1, double arg2)
    {
        if (operator.equals("+")) return (arg1 + arg2);
        if (operator.equals("-")) return (arg1 - arg2);
        if (operator.equals("*")) return (arg1 * arg2);
        if (operator.equals("/"))
        {
            if (arg2 != 0)
                return (arg1 / arg2);
            else
            {
                System.out.println("Error in 'doOperation' method. Division by 0.");
                return 0;
            }
        }
        if (operator.equals("^")) return Math.pow(arg1, arg2);
        
        if (operator.equals("sin")) return Math.sin(arg1);
        if (operator.equals("cos")) return Math.cos(arg1);
        if (operator.equals("tan")) return Math.tan(arg1);
        if (operator.equals("cot")) return 1.0 / Math.tan(arg1);
        
        if (operator.equals("ln"))
        {
            if (arg1 > 0)
                return Math.log(arg1);
            else
            {
                System.out.println("Error in 'doOperation' method. The 'ln' function has negative operand.");
                return 0;
            }
        }
        
        if (operator.equals("log"))
        {
            if (arg1 > 0)
                return Math.log10(arg1);
            else
            {
                System.out.println("Error in 'doOperation' method. The 'log' function has negative operand.");
                return 0;
            }
        }
        
        if (operator.equals("~")) return (arg1 * (-1));
        
        System.out.println("Error in 'doOperation' method. Unsupported operator detected.");
        return 0;
    }
}