package secrecy;

import java.util.Random;

/**
 *
 *
 * @author Parker TenBroeck 7376726
 */
public class TransmogrifierChained implements Transmogrifier{

    /**
     * The current random number generator used to encrypt our messages
     */
    private Random rand;
    /**
     * The seed we provided to our random generator
     */
    private long seed;

    public TransmogrifierChained(long seed){
        this.seed = seed;
        rand = new Random(this.seed);
    }

    @Override
    public char mutate(char input) {
        // check if the character is within our valid char range
        if (0 <= input && input <= 127) {
            // xors our input char with the first 7 bits of the next random number in our sequence
            return (char)(input ^ this.rand.nextInt() & 0x7F);
        } else {
            throw new InvalidCodePointException(input);
        }
    }

    @Override
    public String getKey() {
        return this.seed+"";
    }

    @Override
    public String getAntiKey() {
        // the anti key is the same as our regular key due to the symmetric properties
        // of the xor operation (and also rand giving the same sequence for the same key)
        return this.seed+"";
    }
}
