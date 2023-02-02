package calculations;
import Media.*;
import java.awt.Color;

/**
 * @author Parker TenBroeck 7376726
 */
public class Tabulator
{
    // Current value of tabulator
    private int value = 0;
    // The pen used to draw tabulator
    private Turtle pen = null;
    // The scaling factor use
    private double scalingFactor = 1.0;

    /**
     * Constructs a new Tabulator
     * 
     * @param pen               the turtle to use as a pen
     * @param scalingFactor     the scalingFactor to use for this instance of Tabulator
     */
    public Tabulator(Turtle pen, double scalingFactor)
    {
        this.pen = pen;
        this.scalingFactor = scalingFactor;
    }

    /**
     * Constructs a new Tabulator
     * 
     * @param pen               the turtle to use as a pen
     */
    public Tabulator(Turtle pen)
    {
        this.pen = pen;
    }

    /**
     * Clear the screen to Color.WHITE and reset the cursor position
     */
    public void resetScreen() {
        this.fillScreen(Color.WHITE);
        pen.moveTo(-150 + scalingFactor / 1.5, 150 - scalingFactor  / 1.5);
        this.setPenAngle(0.0);
    }

    /**
     * fills the entire screen with color
     * this method preserves the turtles state (rotation, position and, color)
     * 
     * @param color the color to fill the screen with
     */
    public void fillScreen(Color color) {
        // save the turtles state for later
        int width = this.pen.getPenWidth();
        double x = this.pen.getScreenX();
        double y = this.pen.getScreenY();
        double angle = this.pen.getAngle();
        Color oldColor = this.pen.getPenColor();

        // rotate to face 0 degrees to the right of the screen
        this.pen.left(angle);

        this.pen.setPenColor(color);
        // move to center left
        this.pen.moveTo(-300.0 / 2.0, 0);
        //set the width to be the height of the screen
        this.pen.setPenWidth(300);
        // move across the screen
        this.pen.penDown();
        this.pen.forward(300);
        this.pen.penUp();

        // set state to what it was before this method was called
        this.pen.right(angle);
        this.pen.setPenWidth(width);
        this.pen.moveTo(x, y);
        this.pen.setPenColor(oldColor);
    }

    /**
     * Adds the given value to the value stored within this instance 
     *
     * @param  num  The number to add to this.value
     */
    public void add(int num){
        this.value += num;
    }

    /**
     * Subtracts the given value to the value stored within this instance 
     *
     * @param  num  The number to subtract from this.value
     */
    public void sub(int num) {
        this.value -= num;
    }

    /**
     * Multiply the given value to the stored within this instance
     *
     * @param  num  The number to multiply with this.value
     */
    public void mul(int num) {
        this.value *= num;
    }

    /**
     * Divide the value stored within this instance by the given value
     *
     * @param  num  the number to divide this.value by
     */
    public void div(int num) {
        this.value /= num;
    }

    /**
     * Store the given value in this instance as its current value
     * 
     * @param num   the value to assign
     */
    public void assign(int num) {
        this.value = num;
    }

    /**
     * Gets the current value of this instance
     * 
     * @returns     the value stored within this instance
     */
    public int get() {
        return this.value;
    }

    /**
     * Draws the current value of this instance at the current cursor position
     * The cursor will be advanced the # of digits present in the current value in base 10
     * Negative numbers arent displayed properly
     */
    public void draw() {
        // gets the absolute value of the current value stored in this instance
        int calc = Math.abs(this.value);

        // print the least significant digit base 10 of calc,
        // move the decimal place left one and repeate while calc is non zero
        do{
            // get the least significant digit of calc in base 10
            int dig = calc % 10;
            // remove the least significant digit of calc in base 10
            calc /= 10;

            // draw the digit that was calculated and advance the curser by 1
            this.drawDigit(dig);
            this.advance(1);
        }while(calc != 0);
    }

    /**
     * Draw the provided digit as a graphical glyph specified by the assignment PDF
     * The glyph will be drawn at the current cursor position and will NOT change
     * the position of the cursor
     * 
     * A runtime exception will occur if the digit is not within the bounds [0,9]
     * 
     * @param val   the digit to be drawn [0,9]
     */
    private void drawDigit(int val){
        // In reality we can actually draw digits larger than 9 but for this assignment
        // we limit the digits to 9 inclusive
        // obviously a digit cannot be negative that simply wouldnt make sense
        if (val < 0 || val > 9){
            throw new RuntimeException("Invalid digit, outside range [0,9]");
        }
        // stores the current x and y of the pen for later
        int x = this.pen.getScreenX();
        int y = this.pen.getScreenY();

        // moves the cursor back so the current pen position is in the center left of the
        // characters bounding box
        this.back(1);
        switch (val){
            case 0:
                // face the turtle right and move forward one scalingFactor
                // drawing a stright horizontal line
                this.setPenAngle(0.0);
                this.pen.penDown();
                this.pen.forward(this.scalingFactor);
                this.pen.penUp();
                break;
            case 1:
                // translate the pen to the center top bounding box of the current
                // cursor position
                // face the turtle down and draw a line of length scalingFactor
                this.translatePen(this.scalingFactor / 2.0, this.scalingFactor / 2.0);
                this.setPenAngle(-Math.PI / 2.0);
                this.pen.penDown();
                this.pen.forward(this.scalingFactor);
                this.pen.penUp();
                break;
            case 2:{
                    // calculate the total side length of a diagonal line from the two corners of 
                    // the characters bounding box
                    double sideLength = Math.sqrt(this.scalingFactor * this.scalingFactor * 2.0);

                    // find the distance left from the current scaling factor subtracted from the
                    // calculated side length
                    double remainder = (sideLength - this.scalingFactor);
                    // translates the pen to the bottom left corner of the character bounding box
                    this.translatePen(0.0, -this.scalingFactor / 2.0);

                    // set the angle 45 degrees right upward
                    this.setPenAngle(Math.PI / 4.0);
                    //move the pen half the remainder forward
                    this.pen.forward(remainder / 2.0);
                    // start drawing with the pen and move 'scalingFactor' forward
                    this.pen.penDown();
                    this.pen.forward(this.scalingFactor);
                    this.pen.penUp();
                    break;   
                }
            default:{
                    // find the radius of the regular polygon 
                    double r = this.scalingFactor / 2.0;
                    // calculate the side length for our n sided polygon with 'val' sides
                    double sideLength = Math.sqrt(2 * r*r * (1-Math.cos(2*Math.PI/val)));
                    // move the cursor to the center right of the characters bounding box
                    this.advance(2);
                    this.pen.penDown();
                    // set the angle to 90 degrees / val + 180 degrees
                    // this flips the turtle to face the right and rotates half the interior angle
                    // of the polygon above the center line
                    this.setPenAngle(Math.PI / (val) + Math.PI / 2.0);

                    // for every side in the polygon
                    for(int i = 0; i < val; i ++){
                        // draw a line of 'sideLength'
                        this.pen.forward(sideLength);
                        // rotate the interior angle of the polygon
                        this.pen.left(2.0 * Math.PI / val);
                    }
                    this.pen.penUp();

                    break;
                }
        }
        // restores the location of the cursor to its unmodified location
        this.pen.moveTo(x,y);
    }

    /**
     * Moves the current cursor position back the number of units equal to the number of digits
     * of the stored value in this instance in base 10
     */
    public void carriagereturn() {
        // find the absolute value of this.value
        int abs = Math.abs(this.value);
        // if its zero set abs to 1 (zero is undefined but we want zero to be drawn as a 
        // single digit so 1 works
        if (abs == 0)
            abs = 1;
        // move back the number of digits in the base 10 representation of abs
        this.back((int)Math.log10(abs) + 1);
    }

    /**
     * Moves the current cursor position down one line
     */
    public void linefeed() {
        this.translatePen(0.0, -this.scalingFactor);
    }

    /**
     * Move the cursor one half character length to the right
     */
    public void advance(int num) {
        this.translatePen(num * this.scalingFactor / 2.0, 0.0);
    }

    /**
     * Move the cursor one half character length to the left
     */
    public void back(int num) {
        this.translatePen(num * this.scalingFactor / -2.0, 0.0);
    }

    /**
     * Sets the current scale (width of character) of this tabulator
     * throws an exception if the scale is < 0
     * 
     * @param scale     the new scale to use
     */
    public void setScale(double scale) {
        if (scale < 0.0) {
            throw new RuntimeException("Invalid scale, cannot be negative");
        }
        this.scalingFactor = scale;
    }

    /**
     * Sets the pen angle to a given absolute value
     * 
     * @param angle     the current angle (abs) to set the pen to
     */
    private void setPenAngle(double angle) {
        this.pen.left(this.pen.getAngle());
        this.pen.left(angle);
    }

    /**
     * Translates the pen by [x,y] preserving all other states
     * 
     * @param x     the x translation 
     * @param y     the y translation
     */
    private void translatePen(double x, double y){
        this.pen.moveTo(this.pen.getScreenX() + x, this.pen.getScreenY() + y);
    }
}
