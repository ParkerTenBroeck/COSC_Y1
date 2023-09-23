package Collections;


import java.io.*;


/** This class represents an implementation of the Stack interface using contiguous
  * memory (i.e. an array).
  * 
  * @see  Stack
  * @see  NoSpaceException
  * @see  NoItemException
  * 
  * @author  D. Hughes
  * 
  * @version  1.0 (Jan. 2014)                                                    */

public class ConStack <E> implements Stack<E>, Serializable {
    
    
    private int  top;   // next available element
    private E[]  elts;  // elements of the stack
    
    
    /** This constructor creates a new, empty stack capable of holding 100 items.*/
    
    public ConStack ( ) {
        
        this(100);
        
    }; // constructor
    
    
    /** This constructor creates a new, empty stack capable of holding a particular
      * number of items.
      * 
      * @param  size  the number of items for the stack.                         */
    
    public ConStack ( int size ) {
        
        elts = (E[]) new Object[size];
        top = 0;
        
    }; // constructor
    
    
    /** This method adds an item to the stack. Stack overflow occurs if there is no
      * room to add another item.
      * 
      * @param  item  the item to be added.
      * 
      * @exception  NoSpaceException  no more room to add to stack.              */
    
    public void push ( E item ) {
        
        if ( top >= elts.length ) {
            throw new NoSpaceException();
        }
        else {
            elts[top] = item;
            top = top + 1;
        };
        
    }; // push
    
    
    /** This method removes an item to the stack. Stack underflow occurs if there
      * are no more items left.
      * 
      * @return  E  the item removed.
      * 
      * @exception  NoItemException  no items available in stack.                */
    
    public E pop ( ) {
        
        E  i;
        
        if ( top <= 0 ) {
            throw new NoItemException();
        }
        else {
            top = top - 1;
            i = elts[top];
            elts[top] = null;
            return i;
        }
        
    }; // pop
    
    
    /** This method returns the top item of the stack. Stack underflow occurs if
      * there are no more items left.
      * 
      * @return  E  the top item.
      * 
      * @exception  NoItemException  no items available in stack.                */
    
    public E top ( ) {
        
        if ( top <= 0 ) {
            throw new NoItemException();
        }
        else {
            return elts[top-1];
        }
        
    }; // top
    
    
    /** This method returns true if the stack contains no items.
      * 
      * @return  boolean  whether the stack is empty.                            */
    
    public boolean empty ( ) {
        
        return top <= 0;
        
    }; // empty
    
    
} // ConStack