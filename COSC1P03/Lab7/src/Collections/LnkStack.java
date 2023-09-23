package Collections;


import java.io.*;


/** This class represents an implementation of the Stack interface using a
  * sequentially-linked structure.
  * 
  * @see  Stack
  * @see  NoSpaceException
  * @see  NoItemException
  * 
  * @author  D. Hughes
  *
  * @version  1.0 (Jan. 2014)                                                    */

public class LnkStack <E> implements Stack<E>, Serializable {
    
    
    private Node<E>  top;  // top element of the stack
    
    
    /** This constructor creates a new, empty stack.                             */
    
    public LnkStack ( ) {
        
        top = null;
        
    }; // constructor
    
    
    /** This method adds an item to the stack. Stack overflow occurs if there is no
      * room to add another item.
      * 
      * @param  item  the item to be added.
      * 
      * @exception  NoSpaceException  no more room to add to stack.              */
    
    public void push ( E item ) {
        
        top = new Node<E>(item,top);
        
    }; // push
    
    
    /** This method removes an item to the stack. Stack underflow occurs if there
      * are no more items left.
      * 
      * @return  E  the item removed.
      * 
      * @exception  NoItemException  no items available in stack.                */
    
    public E pop ( ) {
        
        E  i;
        
        if ( top == null ) {
            throw new NoItemException();
        }
        else {
            i = top.item;
            top = top.next;
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
        
        if ( top == null ) {
            throw new NoItemException();
        }
        else {
            return top.item;
        }
        
    }; // top
    
    
    /** This method returns true if the stack contains no items.
      * 
      * @return  boolean  whether the stack is empty.                            */
    
    public boolean empty ( ) {
        
        return top == null;
        
    }; // empty
    
    
} // LnkStack