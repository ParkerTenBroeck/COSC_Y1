import BasicIO.ASCIIDataFile;
import BasicIO.BasicForm;

/**
 * The class responsible for prompting the user when input is needed and showing the results
 * of the data the user inputted
 *
 * @author Parker TenBroeck 7376726
 */
public class UserPrompter {

    /**
     * The default value for inputFile
     */
    public final String DEFAULT_INPUT_FILE = "essay.txt";
    /**
     * The default value for outputFile
     */
    public final String DEFAULT_OUTPUT_FILE = "output_usage.txt";
    /**
     * The default value for rowCutOff (aka let the graph drawer make the decision)
     */
    public final Integer DEFAULT_ROW_CUTOFF = null;

    /**
     * The actual form that is being displayed
     */
    public BasicForm form;


    /**
     * The key for our output text area
     */
    private final String OUTPUT_TEXT_KEY = "OUTPUT";
    /**
     * The key for our input text field
     */
    private final String INPUT_TEXT_KEY = "INPUT";

    /**
     * Set up a new user prompter (but don't show yet)
     */
    public UserPrompter(){
        this.form = new BasicForm();
        // layout the components we use in our GUI
        form.addTextArea(OUTPUT_TEXT_KEY, "Output", 20, 140);
        form.setEditable(OUTPUT_TEXT_KEY, false);
        form.addTextField(INPUT_TEXT_KEY, "Analyzer Args:", 60);
        form.writeString(INPUT_TEXT_KEY, "");
        form.setEditable(OUTPUT_TEXT_KEY, true);
        // prints the initial help message to our GUI to tell the user what to do and how we work
        this.printHelp();
    }

    /**
     * Waits until the user selects the "Analyze" or "Exit" buttons
     * Returning the arguments provided or NULL of "Exit" was selected
     *
     * @return Either null signaling the user has selected to exit or a string containing the args for the analyzer
     */
    private String waitForUserInputBlocking() {
        form.setEditable(INPUT_TEXT_KEY, true);
        int result = form.accept("Analyze", "Exit");
        if (result == 1){
            return null;
        }
        return form.readString(INPUT_TEXT_KEY);
    }

    /**
     * Prints the help message explaining and showing how to use our GUI and analyzer
     */
    private void printHelp(){
        this.formPrintln("Arguments should be comma separated and in the format -[IDENT][ASSOCIATED_DATA]*");
        this.formPrintln("-e            -> exit program immediately, all other arguments typed in will be ignored if this is encountered");
        this.formPrintln("-h            -> print help, (these messages) (no analysis will be done if this argument is passed)");
        this.formPrintln("-f            -> open a file selector prompt for the input file");
        this.formPrintln("-o {FILE}     -> set the filepath of the output file for the results of the analysis, leave blank to use the default value: \"" + DEFAULT_OUTPUT_FILE + "\"");
        this.formPrintln("-c {INTEGER}  -> set the max word length graph cutoff. leaving blank will let the graph displayer decide what number is best");
        this.formPrintln("EXAMPLES: ");
        this.formPrintln("-f                    -> prompts the user to select the file to be analyzed");
        this.formPrintln("-h                    -> print this message");
        this.formPrintln("-c 12,-f              -> prompts the user to select the file to be analyzed and cuts off the word length graph at 12 bars");
        this.formPrintln("                      -> if left blank all default options are used when analyzing");
        this.formPrintln("\n");
    }


    /**
     * Prints the provided value to our GUIs output then goes to a new line
     *
     * @param output the value to be printed
     */
    private void formPrintln(Object output){
        formPrint(output.toString() + "\n");
    }

    /**
     * Prints the provided value to our GUIs output
     *
     * @param output the value to be printed
     */
    private void formPrint(Object output){
        form.writeString(OUTPUT_TEXT_KEY, output.toString());
        form.setEditable(OUTPUT_TEXT_KEY, false);
    }

    /**
     * Actually show and run our GUI
     *
     * @apiNote This function is blocking and will not return until the user has indicated they wish to exit the program
     */
    public void runBlocking(){
        // show our form
        form.show();

        // start our user input blocking loop
        blockingLoop:
        while(true){
            // initialize our variables to their default values
            String inputFile = DEFAULT_INPUT_FILE;
            String outputFile = DEFAULT_OUTPUT_FILE;
            Integer rowCutoff = DEFAULT_ROW_CUTOFF;

            // wait for user input
            this.formPrintln("\n");
            var input = waitForUserInputBlocking();

            // our user has selected exit break from our blocking loop
            if (input == null){
                break;
            }

            // Our arguments are comma separated so split at every comma and iterate through each individual argument
            for (String arg: input.split(",")){
                // remove any leading or trailing white space and ignore if it is blank
                arg = arg.trim();
                if (arg.isBlank()){
                    continue;
                }

                // we know that no leading whitespace is present, so we can test for the starting signature
                // of our arguments
                // if no valid argument is found print an error and ask for user input again
                if (arg.startsWith("-e")) {
                    // exit the blocking loop
                    break blockingLoop;
                }else if (arg.startsWith("-f")){
                    // remove the default value for our input file
                    inputFile = null;
                }else if (arg.startsWith("-o")){
                    // remove the leading "-o" and trim the result
                    var tmp = arg.substring(2).trim();
                    // test to make sure that our filename isn't empty or blank
                    if (tmp.isBlank()){
                        // if so print an error and ask for user input again
                        this.formPrintln("Output file cannot have a blank name");
                        continue blockingLoop;
                    }else{
                        // otherwise set the output file to this argument
                        outputFile = tmp;
                    }
                }else if (arg.startsWith("-c")){
                    try{
                        // remove the "-c" trim and try to parse as an integer
                        rowCutoff = Integer.parseInt(arg.substring(2).trim());
                        if (rowCutoff <= 0){
                            // if the user provided an invalid value print an error and ask for user input again
                            this.formPrintln("Row cutoff numbers cannot be negative or zero");
                            continue blockingLoop;
                        }
                    }catch (Exception e){
                        // if an exception occurs while trying to parse the int print an error and ask for user input again
                        this.formPrintln("Invalid input string for *c argument: " + arg.substring(2).trim());
                        continue blockingLoop;
                    }
                }else if (arg.startsWith("-h")){
                    // print help and ask for user input again
                    this.printHelp();
                    continue blockingLoop;
                }else{
                    // invalid argument print an error and ask for user input again
                    this.formPrintln("Invalid argument: \"" + arg + "\"");
                    continue blockingLoop;
                }
            }
            // analyze with the given arguments
            this.analyzeFromOptions(inputFile, outputFile, rowCutoff);
        }
        // close our form before we leave
        this.close();
    }

    /**
     * Preform analysis on the provided inputFile, tabulate the results and show the word length tabulations
     * to a visual graph and print the tabulated results of unique word frequencies to our outputFile
     *
     * @param inputFile     The file to read and preform analysis on
     * @param outputFile    The file to write the results of the unique word frequencies to
     * @param rowCutoff     The word length at which to cut off the bar graph and just show the resulting summations of remaining word lengths over the threshold
     */
    private void analyzeFromOptions(String inputFile, String outputFile, Integer rowCutoff){
        // open our essay
        ASCIIDataFile file;
        // if a provided file path isn't given prompt the user with a file selector
        if (inputFile != null){
            file = new ASCIIDataFile(inputFile);
        }else {
            file = new ASCIIDataFile();
            inputFile = file.getFile().getPath();
        }

        // create a WordTokenizer from the opened file.
        // crate an instance of WordFrequencyTabulator and old.WordLengthTabulator
        var tokenizer = new WordTokenizer(file);
        var wordAnalytics = new WordAnalyticsTabulator();

        long start = System.currentTimeMillis();
        // analyze and tabulate the word statistics from the word stream generated
        // by the word tokenizer
        wordAnalytics.analyzeAndTabulateWords(tokenizer);

        long end = System.currentTimeMillis();

        // print out some info letting us know how much we processed and how long it took
        this.formPrintln("File: \""+inputFile+"\" Analyzed in: "  + (end - start) + "ms\nFound: " + wordAnalytics.getTotalWordsTabulated() + " words, of which "
                + wordAnalytics.getUniqueWords() + " are unique");


        WordLengthDisplayer lengthDisplayer;
        // if no rowCutOff was specified let the graph displayer decide what the cutoff will be
        // otherwise use the specified value
        // this will actually display our results graphically
        if (rowCutoff == null){
            lengthDisplayer = new WordLengthDisplayer(wordAnalytics.wordLengthTabulations());
        }else{
            lengthDisplayer = new WordLengthDisplayer(wordAnalytics.wordLengthTabulations(), rowCutoff);
        }

        // output our tabulated word frequency data to file
        wordAnalytics.writeUniqueWordResultsToFile(outputFile);
        this.formPrintln("Unique word analytics written to file: \"" + outputFile + "\"");

        // prompt user to close gui
        lengthDisplayer.close();
    }

    /**
     * Close our GUI
     */
    public void close(){
        form.hide();
        form.close();
    }
}
