package Collections;


import java.io.*;


/** This class represents a node in a sequentially-linked structure representing a
  * collection of objects of type E.
  * 
  * @author  D. Hughes
  * 
  * @version  1.0 (Jan. 2014)                                                    */

class Node <E> implements Serializable {
    
    
    E        item;  // the item in the stack
    Node<E>  next;  // the next node in the list
    
    
    /** This constructor creates a new node for the sequentially-linked structure
      * representing a collection.
      * 
      * @param  i  the item in the node.
      * 
      * @param  n  the next node in the list.                                    */
    
    public Node ( E i, Node<E> n ) {
        
        item = i;
        next = n;
        
    }; // constructor
    
    
} // Node