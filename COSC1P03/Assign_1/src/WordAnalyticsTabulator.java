import BasicIO.ASCIIOutputFile;

/**
 * A way to tabulate the frequencies of recurring words in a stream of words
 *
 * @author Parker TenBroeck 7376726
 */
public class WordAnalyticsTabulator {
    /**
     * The number of valid WordFrequencies stored in this.wordFrequencies
     */
    private int uniqueWordFrequenciesSize = 0;

    /**
     * The number of unique words stored inside our unique word array
     * before we increase its capacity
     */
    private int uniqueWordFrequenciesLoadFactor;

    /**
     * The internal array for storing word frequencies
     * This array should always be of length 2^n. where n is some positive integer
     */
    private UniqueWordFrequency[] uniqueWordFrequencies;

    /**
     * The total number of words analyzed
     */
    private long totalWordsTabulated = 0;

    /**
     * The count of words with length i. where i is index i-1 into the array
     */
    private int[] wordLengthRecords = new int[0];

    /**
     * Creates a new word analytics tabulator
     */
    public WordAnalyticsTabulator(){
        this(32, 0.75);
    }

    /**
     * Creates a new word analytics tabulator
     *
     * @param initialCapacity   the initial capacity to use the unique word frequencies table (must be of 2^n)
     */
    @SuppressWarnings("unused")
    public WordAnalyticsTabulator(int initialCapacity){
        this(initialCapacity, 0.75);
    }

    /**
     * Creates a new word analytics tabulator
     *
     * @param initialCapacity   the initial capacity to use for the unique word frequencies table (must be of 2^n)
     * @param loadFactor        the upper bound of total unique word capacity used as a percentage before the capacity is increased (must be (0,1] )
     */
    public WordAnalyticsTabulator(int initialCapacity, double loadFactor){
        this.uniqueWordFrequenciesLoadFactor =  (int)(initialCapacity * loadFactor);
        if (Integer.bitCount(initialCapacity) != 1){
            throw new RuntimeException("Invalid initial capacity");
        }
        if (this.uniqueWordFrequenciesLoadFactor <= 0 || this.uniqueWordFrequenciesLoadFactor > initialCapacity){
            throw new RuntimeException("Invalid load factor");
        }
        this.uniqueWordFrequencies = new UniqueWordFrequency[initialCapacity];
    }

    /**
     * Consumes the entire iterator tabulating each string in it through
     * this.analyzeAndTabulateWord
     *
     * @param iterator an iterator of words to be analyzed and tabulated
     */

    public void analyzeAndTabulateWords(WordTokenizer iterator){
        while (iterator.hasNext()){
            this.analyzeAndTabulateWord(iterator.next());
        }
    }


    /**
     * Analyze the word and record its occurrence of a unique word (either create a new
     * entry in the unique word records or increment the encountered frequency of a record
     * associated with this word if it exists)
     * <br><br>
     * And record this words length in the word length records or create a new entry if
     * this length has not yet been encountered
     * <br><br>
     * Also record the occurrence of another word analyzed
     *
     * @param word  the word to be analyzed
     */
    public void analyzeAndTabulateWord(String word){
        // analyze and record both the words uniqueness and length
        this.tabulateUniqueWordFrequency(word);
        this.tabulateWordLength(word);
        // record that we've seen a word
        this.totalWordsTabulated += 1;
    }


    /**
     * Increment an existing record for words with the same length as the provided word
     * or create a new record if one doesn't exist and initialize it to 1.
     *
     * @param word  the word to be analyzed and tabulated
     */
    private void tabulateWordLength(String word){
        // no word will be of size zero or less than 0 so we can use
        // its length as an index into the array (making sure length 1 is index 0)
        int index = word.length() - 1;

        // if the index extends pass the final element
        // in our existing array expand it so our new element will be placed
        // in the last index of the array as to not waste memory
        if (index >= this.wordLengthRecords.length){
            int[] expanded = new int[index + 1];
            //noinspection ManualArrayCopy
            for(int i = 0; i < this.wordLengthRecords.length; i ++){
                expanded[i] = this.wordLengthRecords[i];
            }
            this.wordLengthRecords = expanded;
        }
        // increment the count of the encountered words of length index + 1 by 1
        // we know that this index will always fit in the array because of the previous check
        this.wordLengthRecords[index] += 1;
    }

    /**
     * If a unique word frequency record already exists increment the frequency
     * counter. Otherwise, create a new unique word frequency record associated
     * with the given word.
     *
     * @param word the word to be analyzed and tabulated
     *
     * @throws RuntimeException if the state of our unique words array becomes invalid
     */
    private void tabulateUniqueWordFrequency(String word){
        // if the number of stored unique words is equal to the load factor of our
        // unique words array increase the arrays capacity
        //
        // one downside of this is if we are at capacity and a new word is to be tabulated
        // we will double the capacity even if the record for the word already exists.
        //
        // also check to make sure our array is still of length 2^n if not throw a runtime exception
        //
        // if the number of stored unique words is GREATER than the load factor of our
        // unique words array (shouldn't be possible but this path isn't hot so it doesn't hurt much to check)
        // throw a runtime exception
        if (this.uniqueWordFrequenciesSize >= this.uniqueWordFrequenciesLoadFactor) {
            if ( Integer.bitCount(this.uniqueWordFrequencies.length) != 1){
                throw new RuntimeException("InvalidState: word frequency array not of size 2^n");
            }
            if (this.uniqueWordFrequenciesSize > this.uniqueWordFrequencies.length) {
                throw new RuntimeException("InvalidState: Number of items in array is greater than capacity?");
            }
            this.doubleUniqueWordRecordArrayCapacity();
        }

        // cache the hash of our word for further calculations
        int hash = hash(word);

        // This is where we either increment or create a new record for the provided unique word
        //
        // there are some special properties of this.uniqueWordFrequencies and also the provided word
        // we can use to speed the search up
        //
        // The first major optimization is already knowing the hash of both the word we are trying to find
        // and the word of each record in our array. With this knowledge when comparing our word to a record
        // we can first check if the hashes are equal before preforming a string comparison to save some effort.
        //
        // The next search optimizations stem from a few assumptions
        // the first being no record is ever deleted/remove from our array. records are only created and modified
        // the second being our array size is always a power of 2, meaning masking an index can be preformed with
        // a bitwise mask instead of a modulo operation
        // the last being our array is private and can adhere to our pre-determined notion of where records should be placed
        //
        // We first start by having a loop that iterates through all the elements in our array in an increasing order
        // If we encounter a non-empty record that has a matching word, we increase its recorded frequency and return.
        // If we encounter an empty record we create a new record associated with our word with initial frequency 1.
        //
        // Now what makes it so special is our "suggested" starting index. If we add our hash with our index wrapping it
        // back around to always fit within our array length we can greatly speed up the search
        //
        // If we treat the hash as a sport of "starting" index (obviously truncated to fit our array's length)
        // we then start by searching for the first empty or matching record at this starting index in increasing order
        // wrapping back to zero if the index exceeds the last elements index. iterating over each element only once.
        //
        // this makes it so each element "wants" to be places in its suggested starting index and will settle for the
        // first element it finds after it that is free if its suggested spot is already taken
        //
        // And since no elements are ever removed from this datastructures we don't have to worry about if there exists
        // a spot we already used for this word past the first empty spot we find because it will always be placed in the first
        // empty spot if it existed in the array.
        for(int i = 0; i < this.uniqueWordFrequencies.length; i += 1){
            // this is equivalent to (i + hash) % this.uniqueWordFrequencies.length when
            // this.uniqueWordFrequencies.length is a power of 2
            //
            // we use this to ensure the index will always be within the bounds for our array
            //
            // we add hash to our index as a sport of "suggested" starting index to decrease search time on average
            int index = (i + hash) & ( this.uniqueWordFrequencies.length - 1);
            // check if the slot is empty
            if (this.uniqueWordFrequencies[index] == null){
                // if this record slot is empty make it our new home
                // we know that records can't be removed and if this record already
                // existed we would have found it before we reached an empty slot
                // once we create a new record return as we have nothing else to do
                this.uniqueWordFrequencies[index] = new UniqueWordFrequency(word, hash);
                this.uniqueWordFrequenciesSize += 1;
                return;
            }else{
                if (this.uniqueWordFrequencies[index].hash == hash){
                    if (this.uniqueWordFrequencies[index].word.equals(word)){
                        // if this records word exactly matches ours then increment its frequency and return
                        // our work here is done
                        this.uniqueWordFrequencies[index].frequency += 1;
                        return;
                    }
                }
            }
        }

        // if we have traversed through every element in our array and couldn't find either a matching record
        // or an empty spot there must be some invalid state
        //
        // we know that there should be at least 1 empty spot available
        throw new RuntimeException("InvalidState: Unable to find/insert: \"" + word + "\" into unique words array");
    }

    /**
     * Hash the provided string
     *
     * @param key   the string to be hashed
     * @return      the hash of the given string
     */
    private static int hash(String key) {
        int hash = key.hashCode();
        // this is done to better increase entropy
        return hash ^ (hash >>> 16);
    }

    /**
     * Doubles the capacity of the unique words array (because we must always be a power of 2) and places all elements
     * from the current unique frequency array into the larger capacity one.
     *
     * @throws RuntimeException if the state of the array is invalid
     */
    private void doubleUniqueWordRecordArrayCapacity(){
        // store our old map and crate a new map twice the size
        // also double are load factor size
        var oldMap = this.uniqueWordFrequencies;
        this.uniqueWordFrequencies = new UniqueWordFrequency[this.uniqueWordFrequencies.length << 1];
        this.uniqueWordFrequenciesLoadFactor <<= 1;

        // for each element that is non-null in our old map
        // find a new location in the larger capacity map
        //
        // place an element in the first non-null slot in the array starting at index = element.hash % this.uniqueWordFrequencies.length
        // increasing/wrapping until we fine an empty slot or until we iterate through each element once.
        //
        // if we find an empty slot continue to the next element in the old array and repeat.
        // otherwise we somehow have less capacity than the previous array?? so throw a runtime exception

        outer:
        for (UniqueWordFrequency uniqueWordFrequency : oldMap) {
            // if the element is null we don't need to worry about it because it's not real and cant hurt us
            if (uniqueWordFrequency == null)
                continue;

            for (int i = 0; i < this.uniqueWordFrequencies.length; i++) {
                // this is equivalent to (i + hash) % this.uniqueWordFrequencies.length when
                // this.uniqueWordFrequencies.length is a power of 2
                //
                // we use this to ensure the index will always be within the bounds for our array
                //
                // we add hash to our index as a sport of "suggested" starting index to decrease search time on average
                int index = (i + uniqueWordFrequency.hash) & (this.uniqueWordFrequencies.length - 1);
                // if this slots empty place the element into it and continue to the next element in the old array
                if (this.uniqueWordFrequencies[index] == null) {
                    this.uniqueWordFrequencies[index] = uniqueWordFrequency;
                    continue outer;
                }
            }
            // we ran out of capacity in a larger array??? ya impossible lets not continue
            throw new RuntimeException("Failed to inset element when resizing map");
        }
    }

    /**
     * Gets the number of unique words encountered
     *
     * @return the number of unique words encountered
     */
    public int getUniqueWords() {
        return this.uniqueWordFrequenciesSize;
    }

    /**
     * Gets the total number of words encountered
     *
     * @return the total number of words encountered
     */
    public long getTotalWordsTabulated() {
        return this.totalWordsTabulated;
    }

    /**
     * Writes our unique word frequencies to a file formatted as {word}: {count}\n for each unique word
     *
     * @param file The file to write our data to
     */
    public void writeUniqueWordResultsToFile(String file) {
        // creates a new writter and buffer
        ASCIIOutputFile out = new ASCIIOutputFile(file);
        StringBuilder builder = new StringBuilder();

        // for every element in our unique words array append our format to our string builder

        // the count of items outputted
        int count = 0;
        // the index into our unique word array
        int index = 0;
        // the current element from index
        UniqueWordFrequency element;
        // go through our list until we have encountered all elements stored in our unique words array
        while(count < this.uniqueWordFrequenciesSize){
            // while the element at our index doesn't exist... increase the index
            while( (element = this.uniqueWordFrequencies[index]) == null){
                index += 1;
            }
            // now we know we have a valid element write its content, then increment the count and index
            builder.append(element.word);
            builder.append(": ");
            builder.append(element.frequency);
            builder.append('\n');
            index ++;
            count ++;
        }

        // write the builders data to our file and close it
        out.writeString(builder.toString());
        out.close();
    }

    /**
     * Creates an iterator over the tabulated word lengths stored in this instance
     *
     * @apiNote Do NOT modify this instance while the returned iterator is still in use
     *
     * @return an iterator over this instances tabulated word lengths
     */
    public WordLengthIterator wordLengthTabulations() {
        // return a new iterator over our tabulated word lengths
        // since java doesn't have any notion of borrow checking and a
        // slim notion of ownership we cant exactly stop the user from
        // mutably changing the state of this instance and possibly changing
        // the length of the array without just cloning the array or adding loving
        // runtime checks :)
        //
        // So we basically just hope the user -doesn't- mutably change the state of this instance
        // when we have an active iterator
        return new WordLengthIterator();
    }

    /**
     * A storage class for unique word frequency records
     */
    public static class UniqueWordFrequency {
        /**
         * The total number of times this word has been seen
         */
        private int frequency;
        /**
         * Cached hash of this.word for fast comparisons
         */
        private final int hash;
        /**
         * The actual word that is being kept with
         */
        private final String word;

        /**
         * Creates a word frequency record with a frequency of 1
         *
         * @param word the word associated with this record
         * @param hash the hash of the provided word
         */
        private UniqueWordFrequency(String word, int hash){
            this.frequency = 1;
            this.hash = hash;
            this.word = word;
        }

        /**
         * Gets the word associated with this record
         *
         * @return the word associated with this record
         */
        public String getWord() {
            return this.word;
        }

        /**
         * Gets the number of times this word has been encountered
         *
         * @return the number of times this word has been encountered
         */
        public int getFrequency(){
            return this.frequency;
        }
    }

    /**
     * Storage class for word length frequencies
     */
    public static class WordLengthTabulation {
        /**
         * The length of the words this tabulation counts
         */
        public final int length;

        /**
         *  The number of times words of this.length were encountered during tabulation
         */
        public final int count;

        /**
         * Creates a WordLengthTabulation record
         *
         * @param length    The length of the words that were counted
         * @param count     The number of times words of length were encountered
         */
        private WordLengthTabulation(int length, int count){
            this.length = length;
            this.count = count;
        }
    }

    /**
     * An iterator over our word lengths
     * <br><br>
     * This is janky because we cannot use the regular java.util.Iterator so its a bunky version of this
     *
     */
    public class WordLengthIterator{
        /**
         * The current index into the array
         */
        int index = 0;

        boolean hasNext(){
            // if our index hasn't excepted our length
            return this.index < wordLengthRecords.length;
        }
        WordLengthTabulation next(){
            // if the index is within our array return a WordLengthTabulation with its value
            // otherwise return a WordLengthTabulation with count 0
            // it makes sense to do this because even though we don't have a stored value for it,
            // we know that because we never made room for it, we have never encountered a word size of
            // index + 1
            if (index < wordLengthRecords.length){
                // create a new WordLengthTabulation with the data stored in our array
                // incrementing our index counter then returning the newly created WordLengthTabulation
                var entry = new WordLengthTabulation(index + 1, wordLengthRecords[index]);
                index += 1;
                return entry;
            }else{
                // we don't strictly need to increment the index, but it doesn't hurt much to do so
                index += 1;
                return new WordLengthTabulation(index, 0);
            }
        }
    }
}
