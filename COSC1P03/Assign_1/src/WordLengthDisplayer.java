import Media.Turtle;
import Media.TurtleDisplayer;


/**
 * A graphical display to show word length on a bar graph
 *
 * @author Parker TenBroeck 7376726
 */
public class WordLengthDisplayer {

    /**
     * The margin to use (number of turtle units to move inward on each size
     * of the turtle displayer screen)
     */
    private final double margin;
    /**
     * The number of bars to show with the last being the summation of
     * all lengths remaining summed together
     */
    private final int lengthCutoff;


    /**
     * The graphical display
     */
    TurtleDisplayer display;
    /**
     * The height of our graphical display
     */
    int height;
    /**
     * The width of our graphical display
     */
    int width;

    /**
     * The pen used to draw our bar graph
     */
    Turtle pen;

    /**
     * Displays lengthTabulations graphically as a bar graph
     *
     * @param lengthTabulations the data to be displayed graphically
     */
    public WordLengthDisplayer(WordAnalyticsTabulator.WordLengthIterator lengthTabulations){
        this(lengthTabulations, 9);
    }

    /**
     *  Displays lengthTabulations graphically as a bar graph
     *
     * @param lengthTabulations the data to be displayed graphically
     * @param lengthCutoff      the number of bars to show with the last being the summation of all remaining recorded length tabulations
     */
    public WordLengthDisplayer(WordAnalyticsTabulator.WordLengthIterator lengthTabulations, int lengthCutoff){
        this(lengthTabulations, lengthCutoff, 10.0);
    }

    /**
     * Displays lengthTabulations graphically as a bar graph
     *
     * @param lengthTabulations the data to be displayed graphically
     * @param lengthCutoff      the number of bars to show with the last being the summation of all remaining recorded length tabulations
     * @param margin            the margin (in turtle units) to use on the graph
     */
    public WordLengthDisplayer(WordAnalyticsTabulator.WordLengthIterator lengthTabulations, int lengthCutoff,  double margin){
        // initialize instant variables and display the provided lengthTabulations
        this.margin = margin;
        this.lengthCutoff = lengthCutoff;
        this.pen = new Turtle();
        this.width = 300;
        this.height = 300;
        this.display = new TurtleDisplayer(this.pen, this.width, this.height);
        this.pen.setSpeed(0);
        this.displayLengths(lengthTabulations);
    }

    /**
     * Displays all the length tabulations provided on a bar graph
     * that is automatically scaled to size
     *
     * @param lengthTabulations length tabulations to be displayed
     */
    private void displayLengths(WordAnalyticsTabulator.WordLengthIterator lengthTabulations) {
        // initialize an array of length tabulations equal to the number of bars to be displayed.
        // start a tracker for the maximum encountered tabulation count as zero
        // and create an iterator over the provided length tabulations
        int maxTabulation = 0;
        int[] lengthTabulationsToDisplay = new int[this.lengthCutoff];

        for(int i = 0; i < lengthTabulationsToDisplay.length; i ++){
            // initialize each index of the tabulations array with the next values count
            lengthTabulationsToDisplay[i] = lengthTabulations.next().count;
            // once we've reached the final element of our tabulations display array
            // go through the remaining items in the iterator and sum their count
            // to the final element
            if (i == lengthTabulationsToDisplay.length - 1){
                while (lengthTabulations.hasNext()){
                    lengthTabulationsToDisplay[i] += lengthTabulations.next().count;
                }
            }
            // if this elements count is greater than the current highest tabulation count
            // set it as the new highest count
            if (maxTabulation < lengthTabulationsToDisplay[i]){
                maxTabulation = lengthTabulationsToDisplay[i];
            }
        }

        // calculate the scale such that the highest bar on the bar graph
        // reaches exactly the top of our drawable area
        double scale = 1.0 / maxTabulation;

        // for each element in our tabulations array draw the bar at positional offset i
        // print the exact number for some diagnostics
        int past = 0;
        for(int i = 0; i < lengthTabulationsToDisplay.length; i ++){
            int current = lengthTabulationsToDisplay[i];
            System.out.println((i + 1) + (i + 1 == lengthTabulationsToDisplay.length ? "+: " :": " )+ current);
            drawBar(scale, i + 1, past, current);
            past = current;
        }
    }

    /**
     * Draw a bar on the graph of size tabulation, scaled with scale and at the position for
     * wordLength
     *
     * @param scale             the scale to apply to the height of the bar
     * @param wordLength        the word length associated with the tabulation count (this changes where the bar is drawn)
     * @param lastTabulation    the previous bar tabulation count
     * @param tabulation        the current word length tabulation count
     */
    private void drawBar(double scale, int wordLength, int lastTabulation, int tabulation) {
        // find the largest value of the current or previous tabulation value and scale it
        double lineHeight = Math.max(lastTabulation, tabulation) * scale;
        // calculate the current height of the bar
        double barHeight = tabulation * scale;
        // calculate the width of the bar (so that we can fit this.lengthCutoff bars spaced evenly)
        double width = 1.0 / (this.lengthCutoff);
        // calculate the offset of the bar we are going to draw
        double xOffset = width * (wordLength - 1);

        // move pen to the bottom left hand side of the bar
        this.movePenToFrameCord(xOffset, 0.0);
        // draw a line upward connecting the previous bars top
        this.pen.penDown();
        this.movePenToFrameCord(xOffset, lineHeight);
        // move to this bars height
        this.movePenToFrameCord(xOffset, barHeight);
        // draw this bars top
        this.movePenToFrameCord(xOffset + width, barHeight);

        // if the bar we are drawing is the final bar draw the right hand size of the bar
        // and draw the bottom line for all the bars on the graph
        if (wordLength >= this.lengthCutoff){
            this.movePenToFrameCord(xOffset + width, 0.0);
            this.movePenToFrameCord(0.0, 0.0);
        }
        // stop drawing
        this.pen.penUp();
    }

    /**
     * Move the pen to cords (x,y) where the domain of x,y is [0.0, 1.0]
     * This will automatically scale the cords within the margin stretching
     * from the bottom left corner of the margin at (0.0, 0.0) to the top
     * right corner of the margin (1.0, 1.0)
     *
     * @param x the x cord to move the pen to
     * @param y the y cord to move the pen to
     */
    private void movePenToFrameCord(double x, double y){
        // translate the x and y cords from the functions coordinate space
        // to the turtles coordinate space.
        this.pen.moveTo(
                x * (this.width - this.margin * 2) + this.margin - this.width / 2.0,
                y * (this.height - this.margin * 2) + this.margin - this.height / 2.0
        );
    }

    /**
     * Show the closing dialog/options
     *
     * @apiNote This is blocking and will not return until the window is closed
     * by the user.
     */
    public void close() {
        this.display.close();
    }
}
