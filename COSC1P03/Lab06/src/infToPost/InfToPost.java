package infToPost;

//Note: added the CharStacks.jar library under File > Project Structure... > Modules > Dependencies
import CharStacks.*; //For CharStack, and ConCharStack/LnkCharStack
import BasicIO.*;


/** This class is a program to perform infix to postfix conversion. It
 * consecutively reads strings representing infix expressions and then produces
 * and displays the equivalent postfix expression. The process involves a stack
 * of characters.
 *
 * @see  CharStack
 * @see  ConCharStack
 *
 * @author  your name
 *
 * @version  1.0 (Feb. 2015)                                                    */

public class InfToPost {


    private BasicForm  display;  // form for UI


    /** The constructor repeatedly reads infix expressions, converts them to
     * postfix and displays them.                                               */

    public InfToPost ( ) {
        display = new BasicForm();
        buildForm();
        
    }; // constructor


    /** This method builds the form for expression string IO.                    */

    private void buildForm ( ) {

        display.setTitle("Infix to Postfix");
        display.addTextField("infix","Infix");
        display.addTextField("postfix","Postfix");

    };  // buildForm


    /** This method does the actual conversion from infix to postfix. It
     * concatenates a dummy operator to the end of the input string and then
     * processes the string from left to right, placing operands into the output
     * string and pushing or popping operators to a stack depending on their
     * relative priorities.
     *
     * @param  in  the infix expression to be converted.
     *
     * @return   String  the equivalent postfix expression.                     */

    private String translate ( String in ) {

        return null;

    }; // translate


    /** This method returns the relative priority of an operator.
     *
     * @param  c  the operator
     *
     * @return  int  the relative priority of c.                                */

    private int prio ( char c ) {

        return 0;

    }; // prio


    public static void main ( String[] args ) {
        InfToPost i = new InfToPost();
    };


} // InfToPost