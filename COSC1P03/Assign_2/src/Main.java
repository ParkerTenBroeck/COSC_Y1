import BasicIO.ASCIIDataFile;
import BasicIO.ASCIIOutputFile;

import java.util.Optional;
import java.util.Scanner;

/**
 * @author Parker TenBroeck 7376726
 */
public class Main {
    // Sorry about the last assignment!!! >.<
    // I promise I don't hate you guys!
    // I just have too much free time :3
    public static void main(String[] args) {
        // we don't have to keep a reference to our list
        // we can do everything we need through a cursor
        var cursor = new LinkedList<String>().frontCursor();

        // print the initial help message
        // and prompt the user to select the initial file to load
        printHelp();
        reloadFile(cursor);

        // initialize our scanner and clipboard
        // we use optional to indicate the clipboard can be empty
        var scanner = new Scanner(System.in);
        Optional<String> clipboard = Optional.empty();

        mainLoop:
        while(true){
            // only trim the leading (so the user can specify to insert a line of only whitespace)
            var string = scanner.nextLine().stripLeading();
            // if our string is empty just skip
            if (string.length() == 0){
                continue;
            }

            // switch through the first character (our control code)
            switch (string.charAt(0)){

                // misc
                case 'r' -> reloadFile(cursor);
                case 'h' -> printHelp();
                case 's' -> saveToFile(cursor);
                case 'q' -> {
                    System.out.println("bye :3");
                    break mainLoop;
                }

                // printing text
                case 'c' -> {
                    // print the current cursor element. if we aren't actually pointing to valid text with the cursor
                    // let the user know there is nothing.
                    if (cursor.isOnList()){
                        System.out.println("Current: \"" + cursor.getCurrent() + "\"");
                    }else{
                        System.out.println("No current line(list is empty of we are off it)");
                    }
                }
                case 'p' -> {
                    // if we are off the list in the front or back send in special
                    // indexes that printList knows how to handle
                    if (cursor.isOnList()){
                        printList(cursor.getList(), cursor.getCursorIndex());
                    }else if (cursor.isOffEnd()){
                        printList(cursor.getList(), cursor.getSize());
                    }else if (cursor.isOffFront()){
                        printList(cursor.getList(), -1);
                    }
                }

                // cursor movement
                case 't' -> cursor.seekToFront();
                case 'b' -> cursor.seekOffEnd();
                case 'u' -> {
                    // make sure we don't go off the front of the list!
                    if (!cursor.isAtFront()){
                        cursor.seekPrevious();
                    }
                }
                case 'd' -> cursor.seekNext();
                case '?' -> {
                    // get our search str ignoring our '?' and make it lowercase
                    var searchStr = string.substring(1).toLowerCase();
                    // for all the items in the list including and past the cursors current element
                    while (cursor.isOnList()) {
                        // get the current item in all lower case
                        var current = cursor.getCurrent().toLowerCase();
                        // if our current line contains our target search str anywhere exit the loop
                        if (current.contains(searchStr)){
                            break;
                        }
                        cursor.seekNext();
                    }
                    // we know that if off the list we never found a match
                    // print a report letting the user know our results
                    if (cursor.isOnList()){
                        System.out.println("Found match! line: " + cursor.getCursorIndex() + 1);
                    }else{
                        System.out.println("Unable to find: \"" + searchStr + "\"");
                    }
                }

                //clipboard manipulation
                case 'x' -> {
                    // take a line if were on the list and place it into our clipboard
                    // otherwise let the user know we cant cut nothing and make our clipboard empty
                    if (cursor.isOnList()){
                        clipboard = Optional.of(cursor.removeCurrent());
                    }else{
                        clipboard = Optional.empty();
                        System.out.println("No valid line is selected cannot cut");
                    }
                }
                case 'v' -> {
                    // if we have an item to paste insert it before our cursor
                    // and move, so we are currently on the line we pasted
                    // otherwise let the user know the clipboard is empty
                    if (clipboard.isPresent()){
                        cursor.insertPrevious(clipboard.get());
                        cursor.seekPrevious();
                    }else{
                        System.out.println("The clipboard is empty");
                    }
                }

                // line manipulation
                case 'i' -> {
                    // insert our string (ignoring first 'i') before our cursor
                    // and move the cursor to be on the line we just inserted
                    var str = string.substring(1);
                    cursor.insertPrevious(str);
                    cursor.seekPrevious();
                }
                case 'e' -> {
                    // if were on the list replace the cursors current line with the provided string (ignoring first 'e')
                    // otherwise let the user know we cannot replace a non-existent node.
                    if (cursor.isOnList()){
                        var str = string.substring(1);
                        cursor.replaceCurrent(str);
                    }else{
                        System.out.println("Cannot replace current line when off the list");
                    }
                }

                default -> System.out.println("Unknown command: " + string + "\ntype \"h\" for help");
            }
        }
        // using the AsciiDataFile file selector causes the program to never terminate? lol
        System.exit(0);
    }

    /**
     * Prompts the user to pick a file to be written to then saves all the contents of the provided cursors list to the file
     * This will NOT change the state of the cursor or its internal list. All contents of the cursors list will be saved no matter the
     * state of the cursor.
     * <br><br>
     * If the file the user selected is invalid or no file is selected a report will be printed and the program will continue.
     * <br><br>
     *
     * @param cursor the cursor whose list is to be saved
     */
    public static void saveToFile(LinkedList<String>.Cursor cursor){
        // try to save the file
        // otherwise print an error
        try{
            // replace the reference to our cursor that we should NOT mutably change
            // with a new reference to a cursor we can use (being sure not to mutably change our list)
            cursor = cursor.getList().frontCursor();
            // prompts the user to select a file, create a string builder for saving
            ASCIIOutputFile file = new ASCIIOutputFile();
            StringBuilder builder = new StringBuilder();
            // for all items in our cursors list
            while (cursor.isOnList()) {
                var current = cursor.getCurrent();
                builder.append(current);
                // if we aren't at the end add a newline
                if (!cursor.isAtEnd()){
                    builder.append('\n');
                }
                cursor.seekNext();
            }
            // actually write our buffer to the file
            file.writeString(builder.toString());
            // if it wasn't successful print an error
            if (!file.successful()){
                System.out.println("Failed to write to file: " + file.getFile().getPath());
            }
            file.close();
        }catch (Exception e){
            System.out.println("Failed to write to file, no file selected");
        }
    }

    /**
     * Prompts the user to load a new file into the provided cursors list and reset its position back to the front of the list
     * If the user selected file is invalid or doesn't exist simply clear the cursors list
     *
     * @param cursor the cursor to be reset and loaded into
     */
    public static void reloadFile(LinkedList<String>.Cursor cursor){
        cursor.clearList();
        // try to load a file
        // if something goes wrong report it and wipe the list again
        try{
            var file = new ASCIIDataFile();
            var line = file.readLine();
            // for all the lines in the file insert them into the list
            while(!(file.isEOF() || file.isDataError())){
                var noNl = line.replace("\n", "");
                cursor.insertPrevious(noNl.replace("\r", ""));
                // for each newline after the first newline add a blank line
                for(int i = 1; i < line.length() - noNl.length(); i ++){
                    cursor.insertPrevious("");
                }
                line = file.readLine();
            }
            file.close();
        }catch (Exception e){
            cursor.clearList();
            System.out.println("No valid file selected, Continuing as empty");
        }

        cursor.seekToFront();
    }

    /**
     * Prints the contents of a list while also showing the currently highlighted index
     *
     * @param list              The list to be printed
     * @param highlightedIndex  The index that should be highlighted (-1 to indicate being off the front of the list and list.size to indicate being off the end of the list)
     */
    public static <T> void printList(LinkedList<T> list, int highlightedIndex){
        var cursor = list.frontCursor();
        System.out.println("================");
        // if were off the front of the list print the highlighted line before everything else
        if (highlightedIndex < 0){
            System.out.println(">");
        }
        // print each element of the list
        while (cursor.isOnList()){
            // if our current index is the index to be highlighted indicate that
            if (cursor.getCursorIndex() == highlightedIndex){
                System.out.print(">");
            }else{
                System.out.print("]");
            }
            System.out.println(cursor.getCurrent());
            cursor.seekNext();
        }
        // if were off the end of the list or the list is empty print the line after everything
        if (highlightedIndex >= cursor.getSize()){
            System.out.println(">");
        }
        System.out.println("================");
    }

    /**
     * Prints the help message for our program
     */
    public static void printHelp(){
        System.out.println("Misc: ");
        System.out.println(" h: Prints this message~!");
        System.out.println(" r: Prompts user to select a file to load again (clearing the old contents)");
        System.out.println(" s: Prompts the user to select a file for the current work to be saved to");
        System.out.println(" q: Exits this program");

        System.out.println("\nPrinting Text: ");
        System.out.println(" c: Prints the currently selected line");
        System.out.println(" p: Prints the contents of the work");

        System.out.println("\nCursor Movement: ");
        System.out.println(" t: Seeks to the first line of the file");
        System.out.println(" b: Seeks off the end of the file (after last line)");
        System.out.println(" u: Seeks one line upwards");
        System.out.println(" d: Seeks one line downwards");
        System.out.println(" ?[text]: Moves the cursor to the first line it finds containing [text] (not case sensitive)");

        System.out.println("\nClipboard Manipulation: ");
        System.out.println(" x: Cuts the line the cursor is currently on");
        System.out.println(" v: Pastes the line currently in the clipboard at the cursors current position");

        System.out.println("\nLine Manipulation: ");
        System.out.println(" i[text]: Inserts a new line with contents [text] at the cursors current position");
        System.out.println(" e[text]: Replaces the cursors currently highlighted line with [text]");

    }
}