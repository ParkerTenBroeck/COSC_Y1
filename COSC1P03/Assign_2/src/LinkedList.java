/**
 * This is a simple implementation of a symmetrical linked list That holds type T
 * <br><br>
 * Note that this list cannot by indexed but can only be accessed through the front
 * and back of this list (and through the cursor as well)
 * <br><br>
 * If you want to access a specific index into this collection,,, simply don't use a linked list :3
 *
 * @author Parker TenBroeck 7376726
 *
 * @param <T> The data type this list will store
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class LinkedList<T> {

    /**
     * the first element of this list
     * <br><br>
     * Is null iff the list is empty
     */
    private Node<T> first = null;

    /**
     * The last element of the list
     * <br><br>
     * Is null iff the list of empty
     */
    private Node<T> last = null;

    /**
     * The number of elements in the list
     */
    private int size = 0;


    /**
     * Returns a cursor over this list starting at the front of the list
     * <br><br>
     * IMPORTANT: Do NOT have multiple mutable cursors over the same list OR mutate the list while cursors over it exist
     *
     * @return a cursor over this list starting at the front
     * @apiNote Do NOT have multiple mutable cursors over the same list OR mutate the list while cursors over it exist
     */
    public Cursor frontCursor(){
        return new Cursor(this.first, 0);
    }

    /**
     * Returns a cursor over this list starting at the back of the list
     * <br><br>
     * IMPORTANT: Do NOT have multiple mutable cursors over the same list OR mutate the list while cursors over it exist
     *
     * @return a cursor over this list starting at the back
     * @apiNote Do NOT have multiple mutable cursors over the same list OR mutate the list while cursors over it exist
     */
    public Cursor backCursor(){
        return new Cursor(this.last, Integer.max(size-1, 0));
    }

    /**
     * Inserts an element at the front of the list.
     *
     * @param data The data to be stored at the front
     */
    public void insertFront(T data) {
        // the special case here is if the list is empty
        if (!this.isEmpty()){
            // if not empty create a new node, link the front node and our new node together
            // then set our new node as the first element in the list
            var newNode = new Node<>(data);
            newNode.next = this.first;
            this.first.previous = newNode;
            this.first = newNode;
        }else{
            // if empty simply set both the first and last nodes of our list
            // to the same new node
            this.first = new Node<>(data);
            this.last = first;
        }
        this.size += 1;
    }

    /**
     * Inserts an element at the back of the list.
     *
     * @param data The data to be stored at the back
     */
    public void insertLast(T data){
        // the special case here is if the list is empty
        if (!this.isEmpty()){
            // if not empty create a new node, link the back node and our new node together
            // then set our new node as the last element in the list
            var newNode = new Node<>(data);
            newNode.previous = this.last;
            this.last.next = newNode;
            this.last = newNode;
        }else{
            // if empty simply set both the first and last nodes of our list
            // to the same new node
            this.first = new Node<>(data);
            this.last = first;
        }
        this.size += 1;
    }

    /**
     * Gets the first element in our list (not removing it)
     *
     * @return the first element in the list
     * @throws RuntimeException if the list is empty we throw an exception
     */
    public T getFirst(){
        if(!this.isEmpty()){
            return this.first.data;
        }else{
            throw new RuntimeException("Cannot get first element when there are no elements");
        }
    }

    /**
     * Gets the last element in our list (not removing it)
     *
     * @return the last element in the list
     * @throws RuntimeException if the list is empty we throw an exception
     */
    public T getLast(){
        if(!this.isEmpty()){
            return this.last.data;
        }else{
            throw new RuntimeException("Cannot get last element when there are no elements");
        }
    }

    /**
     * Removes the first element in our list and return the data stored inside it
     *
     * @return the first element that was removed
     * @throws RuntimeException if the list is empty we throw an exception
     */
    public T removeFirst(){
        // we have two special cases here
        // one when the lists size is exactly 1
        // and one when the list is empty
        if (this.size > 1){
            // if we have more than one element
            // take the first element of our list
            // unlinking it from the list
            // and setting the first element to the old first elements next element
            var tmp = this.first;
            this.first = this.first.next;
            this.first.previous = null;
            this.size -= 1;
            return tmp.data;
        }else if(this.size == 1){
            // our list is exactly one long (so the first and last elements are the same)
            // set our size to zero and set the first and last elements to null
            this.size = 0;
            var tmp = this.first;
            this.first = null;
            this.last = null;
            return tmp.data;
        }else{
            // if the list is empty we cannot continue
            throw new RuntimeException("Cannot remove last element when there are no elements");
        }
    }

    /**
     * Removes the last element in our list and return the data stored inside it
     *
     * @return the last element that was removed
     * @throws RuntimeException if the list is empty we throw an exception
     */
    public T removeLast(){
        // we have two special cases here
        // one when the lists size is exactly 1
        // and one when the list is empty
        if (this.size > 1){
            var tmp = this.last;
            this.last = this.last.previous;
            this.last.next = null;
            this.size -= 1;
            return tmp.data;
        }else if(this.size == 1){
            // our list is exactly one long (so the first and last elements are the same)
            // set our size to zero and set the first and last elements to null
            this.size = 0;
            var tmp = this.last;
            this.first = null;
            this.last = null;
            return tmp.data;
        }else{
            // if the list is empty we cannot continue
            throw new RuntimeException("Cannot remove last element when there are no elements");
        }
    }

    /**
     * Clear the list
     */
    public void clear(){
        this.first = null;
        this.last = null;
        this.size = 0;
    }


    /**
     * Gets the size of this list
     *
     * @return the size of the list
     */
    public int getSize(){
        return this.size;
    }

    /**
     * Gets if this list is empty or not
     *
     * @return returns true if the list is empty (size == 0)
     */
    public boolean isEmpty(){
        return this.size == 0;
    }

    /**
     * Inserts a new node with data before the provided node
     * <br><br>
     * The provided node MUST be a node already in this list
     * <br><br>
     * This means the list will always have at least 1 element (we will be simplifying off that assumption)
     *
     * @param node the node that will be after the newly inserted node
     * @param data the data associated with the node to be inserted
     */
    private void insertBeforeNode(Node<T> node, T data){
        var newNode = new Node<>(data);

        // the special case here is if were at the first element
        // otherwise all other element can be treated the same (even the last element)
        if (node == this.first){
            // since we are at the first element start by setting the
            // stored first element to our new node
            // then simply link our new node and our previous first node together
            this.first = newNode;
            newNode.next = node;
            node.previous = newNode;
        }else{
            // in all other cases link our nodes next and previous references
            // to the provided node and the provided nodes previous node respectively
            // then update the provided nodes previous nodes next value to our new node
            // and the nodes previous value to our new node;
            newNode.next = node;
            newNode.previous = node.previous;
            node.previous.next = newNode;
            node.previous = newNode;
        }

        this.size += 1;
    }

    /**
     * Inserts a new node with data after the provided node
     * <br><br>
     * The provided node MUST be a node already in this list
     * <br><br>
     * This means the list will always have at least 1 element (we will be simplifying off that assumption)
     *
     * @param node the node that will be after the newly inserted node
     * @param data the data associated with the node to be inserted
     */
    private void insertAfterNode(Node<T> node, T data) {
        var newNode = new Node<>(data);

        // the special case here is if were at the last element
        // otherwise all other element can be treated the same (even the first element)
        if (node == this.last) {
            // since we are at the last element start by setting the
            // stored last element to our new node
            // then simply link our new node and our previous last node together
            this.last = newNode;
            newNode.previous = node;
            node.next = newNode;
        } else {
            // in all other cases link our nodes next and previous references
            // to the provided node and the provided nodes previous node respectively
            // then update the provided nodes next nodes previous value to our new node
            // and the nodes next value to our new node;
            newNode.previous = node;
            newNode.next = node.next;
            node.next.previous = newNode;
            node.next = newNode;
        }

        this.size += 1;
    }


    /**
     * The node must be owned by this list
     * <br><br>
     * This means the list has at least 1 node
     * The size of this list is decremented after the node is removed
     *
     * @param node the node to be removed
     */
    private void removeNode(Node<T> node){
        if (size == 1){
            // when our list has exactly one element
            // can simply set the first and last elements to null
            first = null;
            last = null;
        }else if (node == first){
            // if the node we are removing is the first node
            // make the first element the nodes next node
            first = node.next;
        }else if (node == last){
            // if the node we are removing is the last node
            // make the last element the nodes previous node
            last = node.previous;
        }else{
            // otherwise we are in the middle of two nodes
            // so set the previous nodes next node pointer to the next node held by the node we are removing
            // and the next nodes previous node to the previous node held by the node we are removing
            node.previous.next = node.next;
            node.next.previous = node.previous;
        }
        this.size -= 1;
    }

    /**
     * A cursor over an associated linked list
     *
     * @apiNote there should only be ONE mutable cursor over a single linked list, or multiple immutable cursors over the same list, when ANY cursor exists the associated list should never be mutated
     */
    public class Cursor{

        /**
         * The current node index we are at
         * <br><br>
         * Or -1 if we are off the front of the list
         * <br><br>
         * Or `size` (size of the list this cursor owns)  if we are of the end of the list
         * <br><br>
         * This value will be 0 when the list is empty
         * This should be noted
         */
        private int index;

        /**
         * The current element of this cursor
         * <br><br>
         * Is null if the list contains no element or when we are off the front/back of the list
         */
        private Node<T> current;

        private Cursor(Node<T> start, int index){
            this.current = start;
            this.index = index;
        }

        /**
         * Clears the list this cursor currently owns
         */
        public void clearList(){
            // reset the cursors position
            this.index = 0;
            this.current = null;
            // clear this cursors associated linked list
            LinkedList.this.first = null;
            LinkedList.this.last = null;
            LinkedList.this.size = 0;
        }

        /**
         * Clears the list this cursor currently owns returning a new LinkedList with the elements
         * That were held by this cursors list
         *
         * @return The elements held by this cursors list
         */
        public LinkedList<T> emptyListIntoNew(){
            // create a new linked list with the values of this current list
            var tmpList = new LinkedList<T>();
            tmpList.first = LinkedList.this.first;
            tmpList.last = LinkedList.this.last;
            tmpList.size = LinkedList.this.size;

            // clears this cursors list
            this.clearList();
            return tmpList;
        }

        /**
         * Places the cursor at the front of the list
         * this.current() will return the first element in the list
         */
        public void seekToFront(){
            this.current = LinkedList.this.first;
            this.index = 0;
        }

        /**
         * Places the cursor at the end of this list
         * this.current() will return the last element in the list
         */
        public void seekToEnd(){
            this.current = LinkedList.this.last;
            this.index = Integer.max(0, size - 1);
        }

        /**
         * Seeks off the front of this cursors list
         * <br><br>
         * this.insertNext() will insert an item at the front of the list.
         */
        public void seekOffFront(){
            this.index = -1;
            this.current = null;
        }

        /**
         * Seeks off the end of this cursors list
         * <br><br>
         * this.insertPrevious() will insert an item at the end of the list.
         */
        public void seekOffEnd(){
            this.index = size;
            this.current = null;
        }

        /**
         * Gets the current index of the cursor
         *
         * @return the current index of the list
         * @throws RuntimeException if there are no elements in the list or the cursor is off the front/end there is no valid index and an exception will be thrown
         */
        public int getCursorIndex(){
            if (this.index < 0 || this.index >= size){
                throw new RuntimeException("Index is not value. either the list is empty or the cursor is off the front/back of the list");
            }
            return this.index;
        }

        /**
         * Gets the size of the list this cursor owns
         *
         * @return the size of this cursors owned list
         */
        public int getSize(){
            return size;
        }

        /**
         * Gets the current value of the node the cursor is currently on
         *
         * @return the current data held by the node the cursor is currently on
         * @throws RuntimeException when the list is empty or this cursor is off the front/end of the list
         */
        public T getCurrent(){
            if (this.isOnList()){
                return current.data;
            }else{
                throw new RuntimeException("Cannot access current element in list when no elements are present");
            }
        }

        /**
         * Seeks to the next element in the list. If it's already off the end of the
         * list nothing happens. If it's off the front it will point to the first element
         * If the list is empty nothing will happen
         *
         * @return returns true when the item we seeked to is on the list
         */
        public boolean seekNext(){
            if (this.isOnList()){
                // were on the list, so we know the current node isn't null
                // so set our current to the next node and increase our index
                // (if this is the last node then current will be set to null and index will be
                // size and since that's our state for off the end of the list we don't need to
                // do extra checks)
                this.current = this.current.next;
                this.index += 1;
                return this.isOnList();
            }else if (LinkedList.this.isEmpty()){
                // if the list is empty
                // so set our cursors state accordingly
                // (this really should already be in this state but why not double-check)
                this.index = 0;
                this.current = null;
                return false;
            }else if (this.isOffFront()){
                // were off the front of the list
                // so set our current node to the lists first node and our index to zero
                this.index = 0;
                this.current = LinkedList.this.first;
                return true;
            }else if (this.isOffEnd()){
                // off the end of the list
                // so set our cursors state accordingly
                // (this really should already be in this state but why not double-check)
                this.current = null;
                this.index = LinkedList.this.size;
                return false;
            }else{
                // this should never happen,,, but just in case it does
                // handle it with an exception
                throw new RuntimeException("Invalid state??? we are both off the list and on the list and the list is empty and not empty");
            }
        }

        /**
         * Seeks to the previous element in the list. If it's already off the front of the
         * list nothing happens. If it's off the end it will point to the last element
         * If the list is empty nothing will happen
         *
         * @return returns true when the item we seeked to is on the list
         */
        public boolean seekPrevious(){
            if (this.isOnList()){
                // were on the list, so we know the current node isn't null
                // so set our current to the previous node and decrement our index
                // (if this is the first node then current will be set to null and index will be
                // 0 and since that's our state for off the start of the list we don't need to
                // do extra checks)
                this.current = this.current.previous;
                this.index -= 1;
                return this.isOnList();
            }else if (LinkedList.this.isEmpty()){
                // if the list is empty
                // so set our cursors state accordingly
                // (this really should already be in this state but why not double-check)
                this.index = 0;
                return false;
            }else if (this.isOffEnd()){
                // were off the end of the list
                // so set our current node to the lists last node and our index to size - 1
                this.index = LinkedList.this.size - 1;
                this.current = LinkedList.this.last;
                return true;
            }else if (this.isOffFront()){
                // off the front of the list
                // so set our cursors state accordingly
                // (this really should already be in this state but why not double-check)
                this.current = null;
                this.index = -1;
                return false;
            }else{
                // this should never happen,,, but just in case it does
                // handle it with an exception
                throw new RuntimeException("Invalid state??? we are both off the list and on the list and the list is empty and not empty");
            }
        }

        /**
         * Insert data into a new node following the node this cursor is currently pointing to
         * <br><br>
         * If the list is empty a new node will be inserted into the list and the cursor
         * will be off the front of the list
         * <br><br>
         * If the cursor is off the end of the list it doesn't make sense to insert past a non-existent node
         * so an exception is thrown
         *
         * @param data the data to be stored into the new node
         * @throws RuntimeException if this cursor is off the end of the list it doesn't make sense to insert past a non-existent node so an exception is thrown
         */
        public void insertNext(T data){
            if (this.isOnList()){
                // if were on the list simply let the list decide how the node is inserted
                LinkedList.this.insertAfterNode(this.current, data);
            }else if (LinkedList.this.isEmpty()){
                // if the list is empty let the list decide how to insert
                // to the front of the list
                LinkedList.this.insertFront(data);
                // make us off the start of the list
                this.current = null;
                this.index = -1;
            }else if (this.isOffEnd()){
                // no >:(
                throw new RuntimeException("Cannot insert to the next node while off the end of the list");
            }else if (this.isOffFront()){
                // if were off the front just let the list decide
                // how to insert into the front of the list
                LinkedList.this.insertFront(data);
            }else{
                // this should never happen,,, but just in case it does
                // handle it with an exception
                throw new RuntimeException("Invalid state??? we are both off the list and on the list and the list is empty and not empty");
            }
        }

        /**
         * Insert data into a new node before the node this cursor is currently pointing to
         * <br><br>
         * If the list is empty a new node will be inserted into the list and the cursor
         * will be off the end of the list
         * <br><br>
         * If the cursor is off the front of the list it doesn't make sense to insert before a non-existent node
         * so an exception is thrown
         *
         * @param data the data to be stored into the new node
         * @throws RuntimeException if this cursor is off the front of the list it doesn't make sense to insert before a non-existent node so an exception is thrown
         */
        public void insertPrevious(T data){
            if (this.isOnList()){
                // if were on the list simply let the list decide how the node is inserted
                insertBeforeNode(this.current, data);
                // increase our index because there is now one more node before ours
                this.index += 1;
            }else if (LinkedList.this.isEmpty()){
                // if the list is empty let the list decide how to insert
                // to the last of the list
                insertLast(data);
                // make us off the end of the list
                this.current = null;
                this.index = LinkedList.this.size;
            }else if (this.isOffFront()){
                // no >:(
                throw new RuntimeException("Cannot insert to the previous node while off the front of the list");
            }else if (this.isOffEnd()){
                // if were off the end just let the list decide
                // how to insert into the end of the list
                insertLast(data);
                this.index = LinkedList.this.size;
            }else{
                // this should never happen,,, but just in case it does
                // handle it with an exception
                throw new RuntimeException("Invalid state??? we are both off the list and on the list and the list is empty and not empty");
            }
        }

        /**
         * Replace the nodes data this cursor is currently pointing to with the provided data
         *
         * @param data the data to be used
         * @throws RuntimeException if this cursor isn't currently on the list we cannot replace a non-existent node
         */
        public void replaceCurrent(T data){
            if (this.isOnList()){
                this.current.data = data;
            }else{
                // >:( no
                throw new RuntimeException("Cannot access current element in list when no elements are present");
            }
        }

        /**
         * Remove the current node this cursor is pointing to returning what was stored in it
         *
         * @return the data stored in the node that was removed
         * @throws RuntimeException If this cursor isn't pointing to a valid node we cannot remove it.
         */
        public T removeCurrent(){
            if (this.isOnList()){
                // let the list decide how to remove our current node
                LinkedList.this.removeNode(this.current);
                // take the data from our node and set our current node to
                // the removed nodes next node
                var data = this.current.data;
                this.current = this.current.next;
                return data;
            }else{
                // >:( no
                throw new RuntimeException("Cannot access current element in list when no elements are present");
            }
        }

        /**
         * returns true iff this cursor is currently AT the end of the list
         * this will return false if this cursor is off the end of this list so
         * be careful!
         * <br><br>
         * if this list is empty this will always return false
         *
         * @return is true when the cursor is at the end of the list
         */
        public boolean isAtEnd(){
            return this.index == (LinkedList.this.size - 1);
        }

        /**
         * returns true iff this cursor is currently off the end of the list
         * When the list is empty this will always return true
         *
         * @return true when the cursor is off the end of the list
         */
        public boolean isOffEnd(){
            return this.index >= LinkedList.this.size;
        }

        /**
         * Returns true iff this cursor is on a valid node in the list
         * <br><br>
         * This means if the cursor is off the front/end of the list OR if the list is empty this will return false
         *
         * @return if the cursor is on the list or not
         */
        public boolean isOnList(){
            return this.index < size && this.index >= 0;
        }


        /**
         * returns true iff this cursor is currently AT the front of the list
         * this will return false if this cursor is off the front of this list so
         * be careful!
         * <br><br>
         * if this list is empty this will always return false
         *
         * @return if the cursor is at the front of the list
         */
        public boolean isAtFront(){
            return this.index == 0 && LinkedList.this.size != 0;
        }

        /**
         * returns true iff this cursor is currently off the front of the list
         * When the list is empty this will always return true
         *
         * @return true when the cursor is off the end of the list
         */
        public boolean isOffFront(){
            return this.index < 0 || LinkedList.this.isEmpty();
        }

        /**
         * @return returns the list this cursor is pointing to
         */
        public LinkedList<T> getList() {
            return LinkedList.this;
        }
    }

    /**
     * A symmetrically linked node that holds generic data
     *
     * @param <T> the data this node holds
     */
    private static class Node<T> {
        /**
         * The next node in the chain
         */
        private Node<T> next = null;

        /**
         * The previous node in the chain
         */
        private Node<T> previous = null;

        /**
         * The data this node holds
         */
        private T data;

        Node(T data){
            this.data = data;
        }

    }
}
