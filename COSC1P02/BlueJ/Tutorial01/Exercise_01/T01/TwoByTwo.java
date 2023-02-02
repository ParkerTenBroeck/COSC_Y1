import Media.*;
import static java.lang.Math.*;
import static java.awt.Color.*;


/** This class is a program to attempt to draw two squares on different displayers.
  * 
  * @author D. Hughes
  * 
  * @version 1.0 (Sept 2011)                                                     */

public class TwoByTwo {
    
    
    private TurtleDisplayer  display1;  // one display to draw on
    private TurtleDisplayer  display2;  // another display to draw on
    private Turtle           yertle;    // one turtle to do drawing
    private Turtle           mertle;    // another turtle to do drawing
    
    
    /** The constructor attempts to use 2 turtles to draw squares on different
      * displayers.                                                              */
    
    public TwoByTwo ( ) {
        
        display1 = new TurtleDisplayer();
        display2 = new TurtleDisplayer();
        yertle = new Turtle();
        mertle = new Turtle();
        yertle.setPenColor(red);
        mertle.setPenColor(green);
        display1.placeTurtle(yertle);
        display2.placeTurtle(mertle);
        display1.waitForUser();
        
        yertle.penDown();
        mertle.penDown();
        yertle.forward(40);
        mertle.forward(40);
        yertle.right(PI/2);
        mertle.right(PI/2);
        yertle.forward(40);
        mertle.forward(40);
        
        mertle = yertle;
        
        yertle.right(PI/2);
        mertle.right(PI/2);
        yertle.forward(40);
        mertle.forward(40);
        yertle.right(PI/2);
        mertle.right(PI/2);
        yertle.forward(40);
        mertle.forward(40);
        yertle.right(PI/2);
        mertle.right(PI/2);
        yertle.penUp();
        mertle.penUp();
        
        display1.close();
        display2.close();
        
    };  // constructor
    
    
    public static void main ( String[] args ) { TwoByTwo t = new TwoByTwo(); };
    
    
}