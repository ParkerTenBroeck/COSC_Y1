import Media.*;

/**
 * @author Parker TenBroeck 7376726
 */
public class SnowflakesB
{

    public SnowflakesB()
    {
        Turtle turtle = new Turtle();
        TurtleDisplayer display = new TurtleDisplayer(turtle);
        
        turtle.setSpeed(0);
        turtle.penDown();
        
        double sideLen = Math.random() * 19.0 + 5.0;
        
        // draw the 5 sides of the snowflake 
        for(int edge = 0; edge < 5; edge++){
            turtle.forward(sideLen);
            turtle.left(Math.PI * 3.0/5.0);
            
            // draws the small unclosed pentagon on each snowflake side
            for(int side = 0; side < 4; side++){
                turtle.forward(sideLen);            
                turtle.right(Math.PI * 2.0/5.0);           
            }
            turtle.right(Math.PI);
            turtle.forward(sideLen);
            
            turtle.right(Math.PI * 2.0/5.0);
        }
        
        display.close();
    }
}
