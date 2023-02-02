import Media.*;
import static Media.Turtle.*;
import static java.lang.Math.*;

/** TurtleGraphics application for Exercise 1 that draws a 
  ** pulse graph using nested and non nested loops.
  **
  ** @author D. Bockus, adapted  by Dave Hughes
  **
  ** @version 1.1 (Sept. 2002)
  ** @version 1.2 - Dave Hughes (Sept. 2010)                                    */

public class Pulses {
    
    
    private TurtleDisplayer  display;
    private Turtle           yertle;
    
    
    /** The constructor uses a Turtle to draw the graph                         */
    
    public Pulses ( ) {
        
        yertle = new Turtle(MEDIUM);
        display = new TurtleDisplayer(yertle,400,200);
        
        yertle.backward(400/2);      // This block of code draws
        yertle.penDown();            // the centre line and moves
        yertle.forward(400);         // the Turtle to the far left
        yertle.penUp();              // in preparation to draw the 
        yertle.backward(400);        // graph
        yertle.left(PI/2);
        
        yertle.penDown();  
        for ( int i=1; i<=4; i++ ) { // This is the first FOR
            yertle.forward(60);      // loop. The code in this code
            yertle.right(PI/2);      // segment will draw the large
            yertle.forward(8);       // pulses on the graph.
            yertle.right(PI/2);
            yertle.forward(2*60);
            yertle.left(PI/2);
            yertle.forward(8);
            yertle.left(PI/2);
            yertle.forward(60);
        };       // End of first FOR loop (Part A)
        
        for (int j=1; j<=5; j++ ) {  // This is the second FOR
            yertle.forward(60/2);    //loop. The code in this code
            yertle.right(PI/2);      //segment will draw the small
            yertle.forward(8);       //pulses on the graph
            yertle.right(PI/2);
            yertle.forward(60);
            yertle.left(PI/2);
            yertle.forward(8);
            yertle.left(PI/2);
            yertle.forward(60/2);
        };        // End of second FOR loop
        
//      };      // New end of first FOR loop (Part B)
        
        display.close();
        
    }; // constructor
    
    
    public static void main ( String[] args ) {Pulses p = new Pulses(); };
    
    
}