import Media.*;

public class TestProgram {
    
    Turtle yertle;
    TurtleDisplayer display;
    
    public TestProgram() {
        System.out.println("This should display in a separate window.");

        yertle = new Turtle();
        display = new TurtleDisplayer(yertle);
        
        this.play();
    }
    
    public void play(){
        yertle.penDown();
        yertle.right(Math.PI/2);
        yertle.forward(100);
        yertle.left(Math.PI);
        yertle.forward(50);
        yertle.right(Math.PI/2);
        yertle.forward(50);
        yertle.right(Math.PI/2);
        yertle.forward(50);
        yertle.left(Math.PI/2);
        yertle.penUp();
        yertle.forward(50);
        yertle.left(Math.PI/2);
        yertle.penDown();
        yertle.forward(50);
        yertle.penUp();
        yertle.forward(25);
        yertle.penDown();
        yertle.forward(25);
        display.close();
    }
}
