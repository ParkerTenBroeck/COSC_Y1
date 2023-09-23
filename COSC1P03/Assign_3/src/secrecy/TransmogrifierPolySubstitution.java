package secrecy;

/**
 *
 *
 * @author Parker TenBroeck 7376726
 */
public class TransmogrifierPolySubstitution implements Transmogrifier{

    /**
     * the current key
     */
    private char[] key;
    /**
     * the current index into our key
     */
    private int index;

    public TransmogrifierPolySubstitution(String key){
        this.index = 0;
        // convert our key to a char array
        this.key = key.toCharArray();
        // iterate through each element in our char array
        // convert all chars to uppercase and throw an exception
        // if a character isn't a letter (outside of A-Z)
        for(int i = 0; i < this.key.length; i ++){
            this.key[i] = Character.toUpperCase(this.key[i]);
            if( !('A' <= this.key[i] && this.key[i] <= 'Z')){
                throw new InvalidCodePointException(this.key[i]);
            }
        }
    }
    @Override
    public char mutate(char input) {
        // check if the character is within our valid char range
        if (0 <= input && input <= 127) {
            int inputOffset;
            // if the letter is uppercase offset such that 'A' is zero
            // if the letter is lowercase offset such that 'a' is zero
            // otherwise we ignore all other input characters (leaving this instance unchanged)
            if ('A' <= input && input <= 'Z')
                inputOffset = 'A';
            else if ('a' <= input && input <= 'z')
                inputOffset = 'a';
            else
                // assuming when we skip non-alphabetic characters
                // we also leave the state of this encryption instance
                // unchanged ?(hopefully)
                return input;

            // offset so our input is in range [0, 25]
            input -= inputOffset;
            // offset our new range by the next offset provided by our key
            input += nextMutationOffset();
            // modulo our result clamping it to the range [0,25]
            input %= 1 + 'Z' - 'A';
            // re add our offset so [0, 25] -> [a\A, z\Z] (depending on if it was upper or lower case to begin with)
            input += inputOffset;

            return input;
        } else {
            throw new InvalidCodePointException(input);
        }
    }

    /**
     *
     * @return The offset in which we need to mutate the encrypted value we are currently computing
     * @apiNote This should only be called once per mutation event (i.e once per character mutation).
     * and shouldn't be called when the current character is ignored due to being out of range
     */
    private int nextMutationOffset(){
        this.index %= this.key.length;
        return this.key[this.index ++] - 'A';
    }

    @Override
    public String getKey() {
        return new String(this.key);
    }

    @Override
    public String getAntiKey() {
        var builder = new StringBuilder();
        // we can create an anti key by changing each character in our key
        // by the following steps
        for(char c : this.key){
            // make c [A,Z] -> [0,26]
            c -= 'A';
            // map c {0,1,2,3,...,25,26} to {26,25,...,3,2,1,0}
            c = (char)(('Z' - 'A') - c);
            // map c {26,25,...,3,2,1,0} to {0,26,25,...,4,3,2,1}
            c = (char)((c+1) % ('Z' - 'A' + 1));

            // map c {0,26,25,...,4,3,2,1} to {A,Z,Y,...,E,D,C,B}
            c += 'A';
            builder.append(c);
        }
        return builder.toString();
    }
}
