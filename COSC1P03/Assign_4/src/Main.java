/**
 * @author Parker TenBroeck 7376726
 */
public class Main {
    public static void main(String[] args) {
        // start our gui with the dictionary that hopefully exists somewhere
        new Gui(new Dictionary("dictionary.txt")).runBlocking();
    }
}