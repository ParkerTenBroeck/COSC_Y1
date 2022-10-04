import Media.*;

public class SnowflakesC
{

    public SnowflakesC()
    {
        
        Turtle turtle = new Turtle();
        TurtleDisplayer display = new TurtleDisplayer(turtle);
        turtle.setSpeed(0);
        
        for(int snowflake = 0; snowflake < 5; snowflake++){
            double sideLen = Math.random() * 19.0 + 5.0;
            double x = Math.random() * 300.0 -150.0;
            double y = Math.random() * 300.0 -150.0;
            turtle.moveTo(x,y);
            turtle.penDown();
            for(int edge = 0; edge < 5; edge++){
                turtle.forward(sideLen);
                turtle.left(Math.PI * 3.0/5.0);
                for(int side = 0; side < 4; side++){
                    turtle.forward(sideLen);            
                    turtle.right(Math.PI * 2.0/5.0);           
                }
                turtle.right(Math.PI);
                turtle.forward(sideLen);
                
                turtle.right(Math.PI * 2.0/5.0);
            }
            turtle.penUp();
        }
    }
}
