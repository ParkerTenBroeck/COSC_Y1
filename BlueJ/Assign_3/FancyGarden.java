import Media.*;
import java.awt.Color;
/**
 * @author Parker TenBroeck 7376726
 * Assignment 3
 */
public class FancyGarden
{
    Turtle turtle;
    TurtleDisplayer display;

    public FancyGarden()
    {
        this.turtle = new Turtle();
        this.display = new TurtleDisplayer(this.turtle);
        this.turtle.setSpeed(0);
        this.turtle.setPenWidth(2);
        this.fillScreen(Color.DARK_GRAY);
        this.drawGarden();
    }
    
    /**
     * fills the entire screen with color
     * The turtles state is preserved except for it's penColor
     * @param color the color to fill the screen with
     */
    public void fillScreen(Color color) {
        // save the turtles state for later
        int width = this.turtle.getPenWidth();
        double x = this.turtle.getScreenX();
        double y = this.turtle.getScreenY();
        double angle = this.turtle.getAngle();
        
        // rotate to face 0 degrees to the right of the screen
        this.turtle.left(angle);
        
        this.turtle.setPenColor(color);
        // move to center left
        this.turtle.moveTo(-300.0 / 2.0, 0);
        //set the width to be the height of the screen
        this.turtle.setPenWidth(300);
        // move across the screen
        this.turtle.penDown();
        this.turtle.forward(300);
        this.turtle.penUp();
    
        // set state to what it was before this method was called
        this.turtle.right(angle);
        this.turtle.setPenWidth(width);
        this.turtle.moveTo(x, y);
    }
    
    /**
     * Draw a petal with the current state of the turtle
     * Location and angle of this.turtle is preserved with this procedure
     * @ param petalPoint the length of the petal point described in the lesson PDF
     */
    public void drawPetal(double petalPoint) {
        // drawing the pedal acording to the lesson PDF
        
        // the back side length should be sqrt(breadth^2 + breadth^2)
        // since pedalPoint is equal to breadth * 2 we can simplify the above to
        // sqrt(petalPoint^2 / 2.0)
        double backPetalPoint = Math.sqrt(petalPoint*petalPoint / 2.0);
        
        this.turtle.penDown();
        this.turtle.left(Math.PI);
        this.turtle.left(Math.PI / 4.0);
        this.turtle.forward(backPetalPoint);
        this.turtle.left(Math.PI + Math.PI * 1.0 / 3.0 + Math.PI / 4.0);
        this.turtle.forward(petalPoint);
        this.turtle.left(Math.PI + 2.0 * Math.PI / 6.0);
        this.turtle.forward(petalPoint);
        this.turtle.left(Math.PI + Math.PI * 1.0 / 3.0 + Math.PI / 4.0);
        this.turtle.forward(backPetalPoint);
        this.turtle.left(Math.PI / 4.0);
        this.turtle.penUp();
    }
    
    /**
     * Draws a flower with the number of petals given by PetalNumber 
     * where sized is based off petalPoint
     * Location and angle of this.turtle is preserved with this procedure
     * @ param petalNumber the number of petals each flower should have
     * @ param petalPoint the length of the petal point described in the lesson PDF
     */
    public void drawFlower(int petalNumber, double petalPoint) {
        
        // the angle to rotate each consecutive pedal by
        double angle = Math.PI * 2.0 / petalNumber;
        for (int i = 0; i < petalNumber; i ++){
            this.drawPetal(petalPoint);
            this.turtle.left(angle);
        }
    }
    
    /**
     * Generate a random value within a range
     * @ param min the lowest value that will be returned
     * @ param max the upper bound of the value returned
     * @ return a random value greater than or equal to min and less than max
     */
    public double randomRange(double min, double max){
        return (Math.random() * min) + max - min;
    }
    
    /**
     * Draws a poinsettia at the current location
     * Angle of turtle is preserved
     * The turtles pen color is not preserved with this procedure
     * @ param x the x location to draw the poinsettia at
     * @ param y the y location to draw the poinsettia at
     * @ param petalPoint the length of the petal point described in the lesson PDF
     */
    public void drawPoinsettia(double x, double y, double petalPoint){
        this.turtle.moveTo(x, y);
        // random number of petals between 4 and 7
        int petalNumber = (int)randomRange(4.0, 8.0);
        // draws a red outer flower
        this.turtle.setPenColor(Color.RED);
        this.drawFlower(petalNumber, petalPoint);
        // draws a yellow inner flower
        this.turtle.setPenColor(Color.YELLOW);    
        this.drawFlower(petalNumber, petalPoint / 2.0);
    }
    
    /**
     * Draw 16 flowers evenly spaced in a grid pattern
     */
    public void drawGarden(){
        
        // height and width of screen
        double height = 300;
        double width = 300;
        
        double petalPoint = 30.0;
        
        // radius of the flowers (center to tip of pedal)
        double rad = Math.sin(Math.PI / 3.0) * petalPoint + petalPoint / 2.0;
        
        // loops through each flower (16 in total)
        for (int x = 0; x < 4; x ++){
            for (int y = 0; y < 4; y ++){
                // calculate the center x and y position of each flower
                // so that flowers are evenly spaced and the flowers are always
                // visible on screen
                double fx = x * ((width - rad * 2) / 3.0) + rad - width / 2.0;
                double fy = y * ((height - rad * 2) / 3.0) + rad - height / 2.0;
                this.turtle.penUp();
                this.drawPoinsettia(fx, fy, petalPoint);
            }
        }
    }
}
