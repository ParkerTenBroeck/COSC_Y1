import Media.*;
/**
 * @author Parker TenBroeck 7376726
 */
public class Garden
{
    Turtle turtle;
    TurtleDisplayer display;

    public Garden()
    {
        this.turtle = new Turtle();
        this.display = new TurtleDisplayer(this.turtle);
        this.turtle.setSpeed(0);
        drawGarden();
    }
    
    /**
     * Location and angle of this.turtle is preserved with this procedure
     */
    public void drawPetal() {
        // drawing the pedal acording to the lesson PDF
        this.turtle.penDown();
        this.turtle.left(Math.PI);
        this.turtle.left(Math.PI / 4.0);
        this.turtle.forward(Math.sqrt(15*15*2));
        this.turtle.left(Math.PI + Math.PI * 1.0 / 3.0 + Math.PI / 4.0);
        this.turtle.forward(30.0);
        this.turtle.left(Math.PI + 2.0 * Math.PI / 6.0);
        this.turtle.forward(30.0);
        this.turtle.left(Math.PI + Math.PI * 1.0 / 3.0 + Math.PI / 4.0);
        this.turtle.forward(Math.sqrt(15*15*2));
        this.turtle.left(Math.PI / 4.0);
        this.turtle.penUp();
    }
    
    public void drawFlower() {
        // random number of petals between 4 and 7
        int petalNumber = (int)(Math.random() * 4) + 4;
        
        // the angle to rotate each consecutive pedal by
        double angle = Math.PI * 2.0 / petalNumber;
        for (int i = 0; i < petalNumber; i ++){
            this.drawPetal();
            this.turtle.left(angle);
        }
    }
    
    public void drawGarden(){
        // height and width of screen
        double height = 300;
        double width = 300;
        // radius of the flowers (center to tip of pedal)
        double rad = Math.sin(Math.PI / 3.0) * 30 + 15;
        
        // loops through each flower (16 in total)
        for (int x = 0; x < 4; x ++){
            for (int y = 0; y < 4; y ++){
                // calculate the center x and y position of each flower
                // so that flowers are evenly spaced and the flowers are always
                // visible on screen
                double fx = x * ((width - rad * 2) / 3.0) + rad - width / 2.0;
                double fy = y * ((height - rad * 2) / 3.0) + rad - height / 2.0;
                this.turtle.penUp();
                this.turtle.moveTo(fx, fy);
                this.drawFlower();
            }
        }
    }
}
