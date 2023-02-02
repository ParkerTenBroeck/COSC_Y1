import Media.*;

public class Lab02
{

    public Lab02()
    {
        Turtle turtle;
        TurtleDisplayer display;
        turtle = new Turtle();
        display = new TurtleDisplayer(turtle);
        
        turtle.setSpeed(0);
        
        for(int i = 0; i < 10; i ++){
            turtle.penDown();
            turtle.forward(90);
            turtle.backward(90);
            turtle.left(Math.PI / 5.0);
        }
        
                for(int i = 0; i < 10; i ++){
            turtle.forward(60);
            turtle.penDown();
            turtle.forward(30);
            turtle.penUp();
            turtle.backward(90);
            turtle.left(Math.PI / 5.0);
        }
        
        int i = 0;
        for (;;){
            {
                System.out.println("Hello world for the " + i + "time");
            }
            i ++;
            if (!(i < 5))
                break;
        }
        
        display.close();
    }
}
