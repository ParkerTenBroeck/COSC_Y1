package secrecy;

/**
 *
 *
 * @author Parker TenBroeck 7376726
 */
public class TransmogrifierOffset implements Transmogrifier{
    /**
     * The key used for this transmogrifier
     */
    private int key;

    public TransmogrifierOffset(int key){
        this.key = key;
    }

    @Override
    public char mutate(char input) {
        // check if the character is within our valid char range
        if (0 <= input && input <= 127){
            // add the key offset and clamp to range [-126, 126]
            // (keys can be negative resulting in negative inputs to modulo operation
            var c = ((input + this.key) % 127);
            // if an underflow occurs [-126, -1] add 127 to convert that range to [1,126]
            // this means that -1 will become 126, -2 -> 125, -3 => 124 ect.
            if (c<0)
                c += 127;
            return (char)c;
        }else{
            throw new InvalidCodePointException(input);
        }
    }

    @Override
    public String getKey() {
        return ""+this.key;
    }

    @Override
    public String getAntiKey() {
        // the anti key is simply the negation of our current key
        return ""+(-this.key);
    }
}
