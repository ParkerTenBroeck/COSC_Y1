package secrecy;

/**
 *
 *
 * @author Parker TenBroeck 7376726
 */
public class InvalidCodePointException extends RuntimeException{
    public InvalidCodePointException(char invalid){
        super("Unsupported codepoint: " + (int)invalid);
    }
}
