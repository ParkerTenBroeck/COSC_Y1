import Media.*;
//KerPow, but with procedural abstraction
public class KerPower {
    private Turtle pen;
    private TurtleDisplayer display;
    
    public KerPower() {
        pen=new Turtle(0);
        display=new TurtleDisplayer(pen);
        
        boom();
        pen.moveTo(-100,120);
        boom();
        
        display.close();
    }
    
    private void boom() {
        double spacing=50; //Distance from centre
        double angle=2*Math.PI/5;
        //System.out.println(spacing);
        for (int j=0; j<5; j++) {
            pen.forward(spacing);
            
            drawDiamond();
            
            pen.backward(spacing);//Move in
            pen.left(angle);//rotate
        }
    }
    
    private void drawDiamond() {
        double radius=20;
        double sideLength=Math.sqrt( radius*radius + Math.pow(radius,2) );
        double angle=3*Math.PI/4; //We don't care that this name is redundant
        pen.forward(radius);
        
        //System.out.println(spacing); //this will tooootally work
        pen.left( angle );
        pen.penDown(); //Now we're ready to start drawing
        for (int i=1; i<=4; i++) {
            pen.forward(sideLength);
            pen.left(Math.PI/2);
        }
        pen.penUp(); //Just cleaning up now
        pen.right( angle );
        pen.backward(radius);
    }
}
