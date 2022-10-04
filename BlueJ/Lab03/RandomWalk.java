import Media.*;

public class RandomWalk
{
     Turtle turtle;
     TurtleDisplayer display;

    public RandomWalk()
    {
        turtle = new Turtle();   
        display = new TurtleDisplayer(turtle); 
        turtle.setSpeed(0);
        this.randomWalk(20);
        
        display.close();
    }
    
    public void randomWalk(int numSteps){
        
        this.turtle.penDown();
        for (int i = 0; i < numSteps; i ++){
            double x = (Math.random() * 300 - 150);
            double y = (Math.random() * 300 - 150);
            this.turtle.moveTo(x, y);
        }
        this.turtle.penUp();
    }
    
    public void step() {
        this.turtle.penDown();
        this.turtle.forward(10);
        this.turtle.penUp();
    }
}
