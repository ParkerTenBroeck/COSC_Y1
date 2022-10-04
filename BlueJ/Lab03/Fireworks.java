import Media.*;
import java.awt.Color;

public class Fireworks
{
     Turtle turtle;
     TurtleDisplayer display;

    public Fireworks()
    {
        turtle = new Turtle();   
        display = new TurtleDisplayer(turtle); 
        turtle.setSpeed(0);
        this.randomFireworks(5);
        display.close();
    }
    
    public void randomFireworks(int count){
        for(int i = 0; i < count; i ++){
            double size = Math.random() * 50.0 + 15.0;
            double x = (Math.random() * (300 - size * 2.0) - 150 + size);
            double y = (Math.random() * (300 - size * 2.0) - 150 + size);
            Color color = Color.getHSBColor((float)Math.random(), 1.0f, 1.0f);
            this.firework(x ,y, size, color);
        }
    }
    
    public void firework(double x, double y, double radius, Color color){
        this.turtle.setPenColor(color);
        this.turtle.moveTo(x,y);
        for(int i = 0; i < 10; i ++){
            this.improvedStep(radius);
            this.turtle.left(Math.PI * 2.0 / 10.0);
        }
    }
    
    public void improvedStep(double length){
            this.turtle.penUp();
            this.turtle.forward( 3.0/4.0 * length);
            this.turtle.penDown();
            this.turtle.forward( 1.0/4.0  * length);
            this.turtle.penUp();
            this.turtle.backward(length);
    }
}
