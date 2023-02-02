import Media.*;
//Same as Pow, but more of them
//(Also nested)
public class KerPow {
    private Turtle pen;
    private TurtleDisplayer display;
    
    public KerPow() {
        pen=new Turtle(Turtle.FAST);
        display=new TurtleDisplayer(pen);
        
        double spacing=50; //Distance from centre
        double angle=2*Math.PI/5;
        
        double radius=20;
        double sideLength=Math.sqrt( radius*radius + Math.pow(radius,2) );
        
        for (int j=0; j<5; j++) {
            pen.forward(spacing);
            
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
            
            pen.backward(spacing);//Move in
            pen.left(angle);//rotate
        }
        
        display.close();
    }
}
