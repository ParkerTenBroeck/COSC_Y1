import BasicIO.ASCIIDataFile;

/**
 * An iterator that tokenizes and normalizes words according to the
 * Assign1 pdf
 *
 * @author Parker TenBroeck 7376726
 */
public class WordTokenizer {
    /**
     * The input data stream
     */
    private final ASCIIDataFile input;

    /**
     * The current character that is or will be processed
     */
    private char currentChar;

    /**
     * A string builder to build a string from the incoming character stream
     * <br><br>
     * We can just reuse the same builder for every token instead of allocating a new one each time
     */
    private final StringBuilder builder = new StringBuilder();

    /**
     * @param input the input ascii stream to be tokenized
     */
    public WordTokenizer(ASCIIDataFile input){
        this.input = input;


        // since this.next() expects this.currentChar to always be a valid word character
        // we preform the following initializations

        // initialize the currentChar to the first character in the data stream
        this.nextChar();
        // skip all non word characters or until there are no more characters
        this.continueToNextWord();
    }

    /**
     * This function skips all non word characters until it encounters
     * a valid word character or until there are no more characters left to consume
     * <br><br>
     * It is always guaranteed that if there are more characters to consume
     * this.currentChar will contain a valid word char after this function
     * is called
     */
    private void continueToNextWord(){
        // Skip past all non word characters until this.currentChar
        // is a word character, or we have no more characters to process.

        while (this.hasNext()) {
            if (isCharWord(this.currentChar)){
                break;
            }
            this.nextChar();
        }
    }


    /**
     * Determines if a character is a part of a word or not according
     * to the Assign1.pdf definition
     *
     * @param input the character to be checked
     * @return whether the input character is a part of a word or not
     */
    private static boolean isCharWord(char input){
        // a character is said to be a part of a word if it is alphabetic or a '
        // since we know were only dealing with all lowercase ascii data
        // we can only do these checks
        return ('a' <= input && input <= 'z') | input == '\'';
    }

    /**
     * Fetches the next character from the data stream, normalizes it
     * and places it in this.currentChar
     */
    private void nextChar(){
        // read next char and normalize it (according to pdf)
        this.currentChar = input.readC();
        // if the char is upper case
        if ('A' <= this.currentChar && this.currentChar <= 'Z'){
            // offset the character from the start of capital ascii values
            // to the start of lowercase ascii values
            this.currentChar += 'a' - 'A';
        }
    }

    /**
     * Tests to see if this iterator has a new value
     *
     * @return if this token stream has another
     */
    public boolean hasNext() {
        // if the input isn't at eof or hasn't encountered a data error
        // we know more tokens can be consumed from the data stream
        return !(input.isEOF() || input.isDataError());
    }

    /**
     * Gets the next token in the token stream
     *
     * @return the word token
     */
    public String next() {
        // reset our string builder
        this.builder.setLength(0);
        // we assume that the current character is a valid word character
        // We append all characters to the string builder until
        // we reach a non word character or there is no more characters to
        // process
        while (this.hasNext()){
            if (!WordTokenizer.isCharWord(this.currentChar)){
                break;
            }
            builder.append(this.currentChar);
            this.nextChar();
        }

        // since we assume this function always starts with this.currentChar
        // being a valid word character we need to clean up and
        // skip past all non word characters
        this.continueToNextWord();

        // actually make a string out of the characters we collected
        // note that since we guarantee that the currentChar will always be a valid word char
        // when this method is called we know this string will always be non-empty
        return builder.toString();
    }
}
