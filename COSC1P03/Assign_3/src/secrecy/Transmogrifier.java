package secrecy;

/**
 *
 *
 * @author Parker TenBroeck 7376726
 */
public interface Transmogrifier {
    /**
     * This method takes a single un encrypted char then modifys it in some way
     * returning the encrypted char
     *
     * @param input the char to mutate
     * @return the mutated (encrypted) char
     */
    char mutate(char input);

    /**
     * This method takes a single un encrypted String then modifys it in some way
     * returning the encrypted String.
     *
     * @param input the String to mutate
     * @return the mutated (encrypted) String
     */
    default String mutate(String input){
        var builder = new StringBuilder();
        for(int i = 0; i < input.length(); i ++){
            builder.append(this.mutate(input.charAt(i)));
        }
        return builder.toString();
    }

    /**
     *
     * @return The current key being used to encrypt/decrypt messages
     */
    String getKey();

    /**
     *
     * @return A key that when used as the primary key for another transmorgifier of the same kind
     * will mutate the values encrypted by this instance to their original unencrypted
     * values.
     */
    String getAntiKey();
}
