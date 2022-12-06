package calculations;

import Media.*;
/**
 * This program is provided as a VERY simple tester of a Tabulator.
 * It displays a known pattern onto a TurtleGraphics canvas, that can
 * be verified against the expected outcome.
 */
public class TabulatorTester {
    public TabulatorTester() {
        //Preliminary setup
        Turtle pen=new Turtle();
        pen.setSpeed(Turtle.FAST);
        //pen.setSpeed(0); //You shouldn't uncomment this (but... do)
        TurtleDisplayer canvas=new TurtleDisplayer(pen);
        pen.moveTo(-125,125);
        Tabulator tab=new Tabulator(pen,50.0);
        
        //Displays the values 0 through 9
        for (int i=0;i<10;i++) {
            tab.draw();
            tab.add(1);
        }
        
        //Advances and returns to the left edge
        tab.linefeed();
        tab.back(10);
        
        //Let's show the values 2, 4, 8, 16, and 32!
        tab.assign(2);
        for (int i=0;i<5;i++) {
            tab.draw();
            tab.mul(2);
            tab.advance(1); //Advance one 'space' to separate each number
        }
        
        //Again, returning to the left and advancing
        tab.back(12);
        tab.linefeed();
        
        //This time, let's superimpose 123 over top of 456
        tab.assign(123);
        tab.draw();
        tab.carriagereturn();
        tab.assign(456);
        tab.draw();
        tab.carriagereturn();
        tab.linefeed();
        
        //Let's intentionally leave a blank line
        tab.linefeed();
        
        //And now, howsabout, say... 2135754?
        tab.assign(2135754);
        tab.draw();
        tab.carriagereturn();
        tab.linefeed();
        
        //Maybe 6885550?
        tab.assign(6885550);
        tab.draw();
        tab.carriagereturn();
        tab.linefeed();
        
        canvas.close();
    }
    
    
    public static void main(String[] args) {new TabulatorTester();}
}