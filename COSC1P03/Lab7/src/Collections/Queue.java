package Collections;


/** This interface defines the generic data type queue representing a FIFO
  * collection of objects of the same type (E).
  * 
  * @see  NoSpaceException
  * @see  NoItemException
  * 
  * @author  D. Hughes
  * 
  * @version  1.0 (Jan. 2014)                                                    */

public interface Queue < E > {
    
    
    /** This method adds an item to the end of the queue. Queue overflow occurs if
      * there is no room to add another item.
      * 
      * @param  item  the item to be added.
      * 
      * @exception  NoSpaceException  no more room to add to queue.              */
    
    public void enter ( E item );
    
    
    /** This method removes the front item from the queue. Queue underflow occurs
      * if there are no more items left.
      * 
      * @return  E  the item removed.
      * 
      * @exception  NoItemException  no items available in queue.                */
    
    public E leave ( );
    
    
    /** This method returns the front item of the queue. Queue underflow occurs if
      * there are no more items left.
      * 
      * @return  E  the front item
      * 
      * @exception  NoItemException  no items available in queue.                */
    
    public E front ( );
    
    
    /** This method returns true if the queue contains no items.
      * 
      * @return  boolean  whether the queue is empty.                            */
    
    public boolean empty ( );
    
    
    /** This method returns the number of items currently in the queue.
      * 
      * @return  int  the number of items in the queue.                          */
    
    public int length ( );
    
    
} // Queue