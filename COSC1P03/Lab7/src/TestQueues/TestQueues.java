package TestQueues;

import Collections.*;
import BasicIO.*;


/** This class is a test class for testing the Queue implementation: ConQueue. It
  * creates a queue and then loads it with the letters from 'A' to 'Z'. It then
  * removes the items from the queue until empty, printing them. The result should
  * be the letters in order.
  *
  * @see  Queue
  * @see  ConQueue
  * @see  LnkQueue
  *
  * @author  D. Hughes
  *
  * @version  1.0 (Jan. 2014)                                                    */

public class TestQueues {
    
    
    private ASCIIDisplayer  out;
    
    
    /** The constructor uses the method testQueue to test the queue
      * implementation.                                                          */
    
    public TestQueues ( ) {
        
        Queue<Character>  q;  // a queue
        
        out = new ASCIIDisplayer();
        
        q = new ConQueue<Character>(100);
        out.writeString("Testing Contiguous Queue");
        out.newLine();
        out.newLine();
        testQueue(q);
        out.newLine();
        out.newLine();
        
/*        q = new LnkQueue<Character>();
        out.writeString("Testing Linked Queue");
        out.newLine();
        out.newLine();
        testQueue(q);
*/        
        out.close();
        
    }; // constructor
    
    
    /** This method does the actual test of a Queue.
      *
      * @param  q  a queue to test                                               */
    
    private void testQueue ( Queue<Character> q ) {
        
        char c;
        
        out.writeString("Initial queue length: ");
        out.writeInt(q.length());
        out.newLine();
        out.writeString("Loading queue...");
        out.newLine();
        for ( c='A' ; c<='Z' ; c++ ) {
            q.enter(c);
        };
        out.writeString("Queue loaded.");
        out.newLine();
        out.writeString("Queue length: ");
        out.writeInt(q.length());
        out.newLine();
        out.writeString("Front item: ");
        out.writeChar(q.front());
        out.newLine();
        out.writeString("Dumping queue");
        out.newLine();
        while ( ! q.empty() ) {
            out.writeChar(q.leave());
        };
        out.newLine();
        out.writeString("Queue empied");
        out.newLine();
        out.writeString("Queue length: ");
        out.writeInt(q.length());
        out.newLine();
        out.writeString("Front item: ");
        try {
            out.writeChar(q.front());
        }
        catch ( NoItemException e ) {
            out.writeString("underflow");
        };
        out.newLine();
        out.writeString("Leave at empty: ");
        try {
            out.writeChar(q.leave());
        }
        catch ( NoItemException e ) {
            out.writeString("underflow");
        };
        out.newLine();
        
    }; // testQueue
    
    
    public static void main ( String[] args ) { TestQueues t = new TestQueues(); };
    
    
} // TestQueues