package Collections;


import java.io.*;


/** This class represents an implementation of the Queue interface using a
  * contiguous (array) implementation.
  *
  * @see  Queue
  * @see  NoSpaceException
  * @see  NoItemException
  *
  * @author  D. Hughes
  *
  * @version  1.0 (Jan. 2014)
  *
  * new concepts: contiguous implementation of a queue                           */

public class ConQueue < E > implements Queue<E>, Serializable {
    
    
    private E[]  items;  // the items of the queue
    private int  front;  // index of the front item
    private int  rear;   // index of next available element
    private int  count;  // number of items in queue
    
    
    /** This constructor creates a new empty queue capable of holding 100 items. */
    
    public ConQueue ( ) {
        
        this(100);
        
    }; // constructor
    
    
    /** This constructor creates a new empty queue capable of holding a particular
      * number of items.
      *
      * @param  size  max number of items in the queue.                          */
    
    public ConQueue ( int size ) {
        
        items = (E[]) new Object[size];
        front = 0;
        rear = 0;
        count = 0;
        
    }; // constructor
    
    
    /** This method adds an item to the end of the queue. Queue overflow occurs if
      * there is no room to add another item.
      * 
      * @param  item  the item to be added.
      *
      * @exception  NoSpaceException  no more room to add to queue.              */
    
    public void enter ( E item ) {
        
        if ( count >= items.length ) {
            throw new NoSpaceException();
        }
        else {
            items[rear] = item;
            rear = (rear + 1) % items.length;
            count = count + 1;
        };
        
    }; // enter
    
    
    /** This method removes the front item from the queue. Queue underflow occurs
      * if there are no more items left.
      *
      * @return  E  the item removed.
      *
      * @exception  NoItemException  no items available in queue.                */
    
    public E leave ( ) {
        
        E  i;  // item removed
        
        if ( count <= 0 ) {
            throw new NoItemException();
        }
        else {
            i = items[front];
            items[front] = null;
            front = (front + 1) % items.length;
            count = count - 1;
            return i;
        }
        
    }; // leave
    
    
    /** This method returns the front item of the queue. Queue underflow occurs if
      * there are no more items left.
      *
      * @return  E  the front item
      *
      * @exception  NoItemException  no items available in queue.                */
    
    public E front ( ) {
        
        if ( count <= 0 ) {
            throw new NoItemException();
        }
        else {
            return items[front];
        }
        
    }; // front
    
    
    /** This method returns true if the queue contains no items.
      *
      * @return  boolean  whether the queue is empty.                            */
    
    public boolean empty ( ) {
        
        return count <= 0;
        
    }; // empty
    
    
    /** This method returns the number of items currently in the queue.
      *
      * @return  int  the number of items in the queue.                          */
    
    public int length ( ) {
        
        return count;
        
    }; // length
    
    
} // ConQueue