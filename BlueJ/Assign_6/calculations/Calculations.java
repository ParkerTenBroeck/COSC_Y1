package calculations;
import BasicIO.*;
import Media.*;

/**
 * @author Parker TenBroeck 7376726
 */
public class Calculations
{
    // the current display
    TurtleDisplayer display;
    // the tabulator attached to this display
    Tabulator tabulator;

    public Calculations()
    {
        // create a new turtle and turtle displayer, setting the turtle speed to zero
        Turtle turtle = new Turtle();
        turtle.setSpeed(0);
        this.display = new TurtleDisplayer(turtle);
        // give the turtle to a new tabulator
        this.tabulator = new Tabulator(turtle);

        // load a ascii file
        ASCIIDataFile file = new ASCIIDataFile();
        // parse and tabulate the loaded file
        this.calcFile(file); 
        
        //close the display now that we are done
        this.display.close();
    }
    
    /**
     * Parses the given ascii file and tabulates it using this instances tabulator
     * 
     * @param file  the file to parse and tabulate
     */
    public void calcFile(ASCIIDataFile file) {
        // set 'readerState' to the default begining state
        FileReaderState readerState = FileReaderState.Begining;
        
        // while we are not at the end of this file
        while (readerState != FileReaderState.EOF){
            switch (readerState){
                // if we are at the begining
                // read a double, set the tabulators scale to the parsed value
                // and reset the tabulators screen
                // Now set the state to 'FileReaderState.Operator'
                case Begining:
                    this.tabulator.setScale(file.readDouble());
                    this.tabulator.resetScreen();
                    readerState = FileReaderState.Operator;
                    break;
                // First check if the file is at EOF, if so set the state to 'FileReaderState.EOF"
                // and break
                // otherwise read a single character and deside what operator it is
                // throwing a runtime exception if invalid
                // set the state to the operator found
                case Operator:
                    if (file.isEOF()){
                        readerState = FileReaderState.EOF;
                        break;
                    }
                    // read char and set the 'readerState' acording to what is found
                    // if the character is an invalid operator throw a runtime exception
                    switch (file.readC()){
                        case '+':
                            readerState = FileReaderState.Plus;
                            break;
                        case '-':
                            readerState = FileReaderState.Minus;
                            break;
                        case '*':
                            readerState = FileReaderState.Multiply;
                            break;
                        case '/':
                            readerState = FileReaderState.Divide;
                            break;
                        case '>':
                            readerState = FileReaderState.Print;
                            break;
                        case '=':
                            readerState = FileReaderState.Assign;
                            break;
                        case '~':
                            readerState = FileReaderState.LineFeed;
                            break;
                        default:
                            throw new RuntimeException("Invalid Operator char");
                    
                    }

                    break;
                // Read the rest of the line and parse it as an integer
                // assign the parse integer to the tabulator
                // set the state to 'FileReaderState.Operator'
                case Assign:
                    this.tabulator.assign(readRestOfFileLineAsInt(file));
                    readerState = FileReaderState.Operator;
                    break;
                // Discard the rest of the current line as its not needed
                // Tell the tabulator to draw its current value
                // then preform a linefeed and carriagereturn
                // set the state to 'FileReaderState.Operator'
                case Print:
                    file.readLine();
                    this.tabulator.draw();
                    this.tabulator.linefeed();
                    this.tabulator.carriagereturn();
                    readerState = FileReaderState.Operator;
                    break;
                // Discard the rest of the current line as its not needed
                // preform a line feed 
                // set the state to 'FileReaderState.Operator'
                case LineFeed:
                    file.readLine();
                    this.tabulator.linefeed();
                    readerState = FileReaderState.Operator;
                    break;
                // Read the rest of the line and parse it as an integer
                // tell tabulator to preform addition with the parsed integer
                // set the state to 'FileReaderState.Operator'  
                case Plus:
                    this.tabulator.add(readRestOfFileLineAsInt(file));
                    readerState = FileReaderState.Operator;
                    break;
                // Read the rest of the line and parse it as an integer
                // tell tabulator to preform subtraction with the parsed integer
                // set the state to 'FileReaderState.Operator'  
                case Minus:
                    this.tabulator.sub(readRestOfFileLineAsInt(file));
                    readerState = FileReaderState.Operator;
                    break;
                // Read the rest of the line and parse it as an integer
                // tell tabulator to preform division with the parsed integer
                // set the state to 'FileReaderState.Operator'  
                case Divide:
                    this.tabulator.div(readRestOfFileLineAsInt(file));
                    readerState = FileReaderState.Operator;
                    break;
                // Read the rest of the line and parse it as an integer
                // tell tabulator to preform multiplication with the parsed integer
                // set the state to 'FileReaderState.Operator'  
                case Multiply:
                    this.tabulator.mul(readRestOfFileLineAsInt(file));
                    readerState = FileReaderState.Operator;
                    break;
                // if we are of state EOF and still trying to prase the file, die
                // because how did we get here??? no really how :)
                case EOF:
                    throw new RuntimeException("How did we get here??? Cannot parse file in EOF state");                
            }
        }
        
        file.close();
    }
    
    /**
     * Reads an entire line from the given ascii file and parses it as an integer
     * If the file has no more data or the line is not a valid integer an exception will occur
     * 
     * @param file  the file to read from
     * 
     * @return the value parsed from the file
     */
    private static int readRestOfFileLineAsInt(ASCIIDataFile file){
        return Integer.parseInt(file.readLine().trim());
    }

    /**
     * The states the file reader can be in
     * 
     * <br>
     * <br>
     * Default state:<br>
     * 
     * {@link #Begining}<br>
     * 
     * <br>
     * First stage states:<br>
     * 
     * {@link #Operator}<br>
     * {@link #EOF}<br>
     * 
     * <br>
     * Operator states:<br>
     * 
     * {@link #Plus}<br>
     * {@link #Minus}<br>
     * {@link #Divide}<br>
     * {@link #Multiply}<br>
     * {@link #Assign}<br>
     * {@link #Print}<br>
     * {@link #LineFeed}<br>
     */
    public enum FileReaderState{
        /**
         * The state the FSM will be in before any of the file is read
         */
        Begining,
        
        /**
         * In this state the FSM is looking for an operator or the EOF marker
         */
        Operator,
        /**
         * The FSM is at the EOF and is done parsing
         */
        EOF,
        
        /**
         * The operation is addition and the value to be added will be parsed and passed to
         * the tabulator for calculation
         */
        Plus,
        /**
         * The operation is Subtraction and the value to be added will be parsed and passed to
         * the tabulator for calculation
         */
        Minus,
        /**
         * The operation is Division and the value to be added will be parsed and passed to
         * the tabulator for calculation
         */
        Divide,
        /**
         * The operation is Multiplication and the value to be added will be parsed and passed to
         * the tabulator for calculation
         */
        Multiply,
        /**
         * The operation is Assignment and the value to be added will be parsed and passed to
         * the tabulator for assignment
         */
        Assign,
        /**
         * The operation is Printing the remainder of the line will be discarded and the 
         * tabulator will be requested to draw
         */
        Print,
        /**
         * The operation is LineFeed the remainder of the line will be discarded and the 
         * tabulator will be requested to preform a linefeed
         */
        LineFeed,
    }
}
