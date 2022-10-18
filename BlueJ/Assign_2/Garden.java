import Media.*;
/**
 * Write a description of class Guarden here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Garden
{
    Turtle turtle;
    TurtleDisplayer display;

    public Garden()
    {
        this.turtle = new Turtle();
        this.display = new TurtleDisplayer(this.turtle);
        drawGarden();
    }
    
    public void drawPetal() {
        this.turtle.left(Math.PI / 4.0);
        this.turtle.forward(1.0);
        this.turtle.right(Math.PI * 4.0 / 3.0 + Math.PI / 4.0);
        this.turtle.forward(30.0);
    }
    
    public void drawFlower() {
        int petalNumber = (int)(Math.random() * 4) + 4;
        
        double angle = Math.PI * 2.0 / petalNumber;
        for (int i = 0; i < petalNumber; i ++){
            this.turtle.left(angle);
            this.drawPetal();
        }
    }
    
    public void drawGarden(){
        for (int x = 0; x < 4; x ++){
            for (int y = 0; y < 4; y ++){
                this.turtle.moveTo(x,y);
                this.drawFlower();
            }
        }
    }
}
