import Media.*;
public class Pow {
    private Turtle pen;
    private TurtleDisplayer display;
    
    public Pow() {
        pen=new Turtle(Turtle.FAST);
        display=new TurtleDisplayer(pen);
        
        double radius=20;
        double sideLength=Math.sqrt( radius*radius + Math.pow(radius,2) );
        pen.forward(radius);
        pen.left( 3*Math.PI/4 );
        pen.penDown(); //Now we're ready to start drawing
        for (int i=1; i<=4; i++) {
            pen.forward(sideLength);
            pen.left(Math.PI/2);
        }
        pen.penUp(); //Just cleaning up now
        pen.right(3*Math.PI/4);
        pen.backward(radius);
        
    }
}