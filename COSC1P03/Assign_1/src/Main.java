/**
 * @author Parker TenBroeck 7376726
 */
public class Main {
    public static void main(String[] args) {
        // show our GUI and run until the user signals to exit
        var userPrompter = new UserPrompter();
        userPrompter.runBlocking();
        // Try running this on a large text file :)
        // this can easily chug through a million-word text file
        // in under 1000ms
        // for reference a 14534326 word (361867 unique) file was
        // parsed in just under 2 seconds (1921ms)
        // (these files are fun) http://mattmahoney.net/dc/textdata.html
    }
}