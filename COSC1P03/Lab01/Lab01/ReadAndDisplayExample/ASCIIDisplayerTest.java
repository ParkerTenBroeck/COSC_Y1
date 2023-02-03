package ReadAndDisplayExample;

import BasicIO.*;

/**
 * A minimal and a complete example that shows how to use ASCIIDisplayer.
 * It allows the user to output on screen. In other words, ASCIIDisplayer
 * is a fancy System.out.println(...);
 * 
 * @author Maysara Al Jumaily
 * @since  January 17th, 2021
 * 
 */
public class ASCIIDisplayerTest {
  
  private ASCIIDisplayer display;//allows us to write to screen. A fancy System.out.println();
  
  public ASCIIDisplayerTest() { 
    display = new ASCIIDisplayer();//shows it on screen
    int x = 50;
    display.writeInt(40);
    display.writeInt(x);
    display.writeString("Hello!");
    display.newLine();//jumps to the next line (basically, hitting the enter key in a word document).
    
    display.writeInt(x + 70);//x is 50, so 50 + 70 = 120.
    display.writeDouble(7.31);
    display.newLine();//jumps to the next line (basically, hitting the enter key in a word document).
    
    //writes a string and calls newLine() automatically
    display.writeLine("A complete line with (go to next line) added automatically at the end!");
    display.writeInt(x);
    
    //Note:
    //writeString will write a string and stay on the same line.
    //writeLine   will write a string and continues to the next line
    display.close();//important to close it!
  }
  
  public static void main(String[] args) { 
    ASCIIDisplayerTest t = new ASCIIDisplayerTest();
  }
}