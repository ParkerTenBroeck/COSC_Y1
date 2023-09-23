package assign3;

import BasicIO.ASCIIDataFile;
import BasicIO.ASCIIOutputFile;
import secrecy.*;

/**
 * @author Parker TenBroeck 7376726
 */
public class Main {
    public static void main(String[] args) {
        var file = new ASCIIDataFile("cleartext.txt");

        //load our file into a single string for processing
        var data = new StringBuilder();
        var line = file.readLine();
        while (true){
            data.append(line);
            if (file.isDataError() || file.isEOF()){
                break;
            }else{
                data.append('\n');
            }
            line = file.readLine();
        }
        var fileContents = data.toString();

        // static based offset
        {
            var key = 47;
            var encrypter = new TransmogrifierOffset(key);
            var encryptedMessage = encrypter.mutate(fileContents);
            var fileWritter = new ASCIIOutputFile("encryptedOff.txt");
            fileWritter.writeString(encryptedMessage);
            fileWritter.close();
        }

        // string key based encryption
        {
            var key = "LEMON";
            var encrypter = new TransmogrifierPolySubstitution(key);
            var encryptedMessage = encrypter.mutate(fileContents);
            var fileWritter = new ASCIIOutputFile("encryptedPoly.txt");
            fileWritter.writeString(encryptedMessage);
            fileWritter.close();

            encrypter = new TransmogrifierPolySubstitution(encrypter.getAntiKey());
            encryptedMessage = encrypter.mutate(encryptedMessage);
            if (!encryptedMessage.equals(fileContents)){
                throw new RuntimeException(encryptedMessage);
            }
        }

        // chained random with key
        {
            var key = 42069; // :3
            var encrypter = new TransmogrifierChained(key);
            var encryptedMessage = encrypter.mutate(fileContents);
            var fileWritter = new ASCIIOutputFile("encryptedChained.txt");
            fileWritter.writeString(encryptedMessage);
            fileWritter.close();
        }
    }
}