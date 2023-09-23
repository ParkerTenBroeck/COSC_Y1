package Collections;

/** This interface defines the generic data type stack representing a LIFO
  * collection of items of the same type (E).
  * 
  * @see  NoSpaceException
  * @see  NoItemException
  * 
  * @author  D. Hughes
  * 
  * @version  1.0 (Jan. 2014)                                                    */

public interface Stack <E> {
    
    
    /** This method adds an item to the stack. Stack overflow occurs if there is no
      * room to add another item.
      * 
      * @param  item  the item to be added.
      * 
      * @exception  NoSpaceException  no more room to add to stack.              */
    
    public void push ( E item );
    
    
    /** This method removes an item to the stack. Stack underflow occurs if there
      * are no more items left.
      * 
      * @return  E  the item removed.
      * 
      * @exception  NoItemException  no items available in stack.                */
    
    public E pop ( );
    
    
    /** This method returns the top item of the stack. Stack underflow occurs if
      * there are no more items left.
      * 
      * @return  E  the top item.
      * 
      * @exception  NoItemException  no items available in stack.                */
    
    public E top ( );
    
    
    /** This method returns true if the stack contains no items.
      * 
      * @return  boolean  whether the stack is empty.                            */
    
    public boolean empty ( );
    
    
} // Stack