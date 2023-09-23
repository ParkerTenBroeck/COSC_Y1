import BasicIO.ASCIIOutputFile;
import BasicIO.BasicForm;

/**
 * @author Parker TenBroeck 7376726
 */
public class Gui {
    /**
     * The actual form gui being displayed
     */
    private final BasicForm form;
    /**
     * The dictionary to use with our generator
     */
    private final Dictionary dic;


    //--------------------------------- BEHOLD the private static final Strings (keys for our gui)
    private static final String OUTPUT_TEXT_KEY = "OUTPUT_TEXT_AREA";
    private static final String LOADED_LABEL_KEY = "LOADED_LABEL";
    private static final String CHATTINESS_PERCENT_INPUT_KEY = "CHATTINESS_PERCENT_INPUT";
    private static final String PROPER_PROPORTION_PERCENT_INPUT_KEY = "PROPER_PROPORTION_PERCENT_INPUT";
    private static final String CONJUNCTION_PERCENT_INPUT_KEY = "CONJUNCTION_PERCENT_INPUT";
    private static final String PREPOSITION_PERCENT_INPUT_KEY = "PREPOSITION_PERCENT_INPUT";
    private static final String DESCRIPTIVENESS_PERCENT_INPUT_KEY = "DESCRIPTIVENESS_PERCENT_INPUT";
    private static final String MAX_DETAIL_INPUT_KEY = "MAX_DETAIL_INPUT";
    //---------------------------------

    /**
     * The number of columns (character width) that our generated text area should be
     */
    private static final int NUM_COLUMNS_OUTPUT = 180;


    public Gui(Dictionary dic){
        // haha it sounds like a funny word
        this.dic = dic;
        this.form = new BasicForm();

        // this just generates the info about the loaded dictionary
        // and shows it as a label
        {
            var label = "";

            var total = 0;
            var sections = dic.getAllAtoms();
            var sectionNames = dic.getAtomNames();
            // for each section write "[#items] [NAME], "
            for (int i = 0; i < sectionNames.length; i ++){
                label += sections[i].size() + " " + sectionNames[i] + ", ";
                total += sections[i].size();
            }
            // then add the total count to the end
            label += total + " Total";

            form.addLabel(LOADED_LABEL_KEY, label);
        }


        {
            // the controls for the generator
            // in the form
            // {KEY, Label, default value}*
            // this just makes it easier
            // this just makes creating the input fields less repetitive
            var percentageControls = new String[]{
                    CHATTINESS_PERCENT_INPUT_KEY,
                    "Chatiness",
                    "95%",

                    CONJUNCTION_PERCENT_INPUT_KEY,
                    "Conjunction",
                    "20%",

                    PREPOSITION_PERCENT_INPUT_KEY,
                    "Preposition",
                    "20%",

                    PROPER_PROPORTION_PERCENT_INPUT_KEY,
                    "Proper Proportion",
                    "50%",

                    DESCRIPTIVENESS_PERCENT_INPUT_KEY,
                    "Descriptivness",
                    "50%",

                    MAX_DETAIL_INPUT_KEY,
                    "Max Detail",
                    "500",
            };

            var yPos = form.getHeight(LOADED_LABEL_KEY) + form.getY(LOADED_LABEL_KEY) + 10;

            // for each input field
            // place at the same height, starting at x = 5 then 3*80 units apart every iteration
            // with size 10 (look idk what these units are but hey it's whatever looks good ;) )
            // also make it so all these can be edited by the user
            for (int i = 0; i < percentageControls.length; i += 3) {
                form.addTextField(percentageControls[i], percentageControls[i + 1], 10, 5 + i * 75, yPos);
                form.setEditable(percentageControls[i], true);
                form.writeString(percentageControls[i], percentageControls[i + 2]);
            }
        }

        // our output field (non editable)
        form.addTextArea(OUTPUT_TEXT_KEY, "Output", 20, NUM_COLUMNS_OUTPUT);
        form.setEditable(OUTPUT_TEXT_KEY, false);
    }


    /**
     * This will continually prompt the user for input, not returning until the user has
     * quit.
     */
    public void runBlocking(){
        try {
            loop:
            while (true) {
                // wait for user input
                int result = form.accept("Generate", "Clear", "Save", "Quit");
                // call the fn associated with each selected case
                switch (result) {
                    case 0:
                        this.generate();
                        break;
                    case 1:
                        this.clear();
                        break;
                    case 2:
                        this.save();
                        break;
                    default:
                        // this should really only ever be option 3 but in the case
                        // we get something other than 3 just crash and burn silently
                        // because why bother fixing something that ain't our problem.
                        break loop;
                }
            }
            // actually... well close our gui so we can exit
            this.form.hide();
            this.form.close();
        }catch (Exception e){
            // god forbid we don't close the gui and I have to press the stop button on my program
            // manually when we crash
            this.form.hide();
            this.form.close();
            throw e;
        }
    }

    /**
     * Reads an integer from the field in this form with name {@code key}
     * The field should be represented as num% where num is some double value
     * the returned value will be divided by 100.0 s0 95% becomes 0.95
     *
     * @param key the key to the item in our form we want to read a percentage from
     * @return the read percentage
     * throws {@link NumberFormatException} if this exception is thrown the input box will first be set to "NAN" before
     * this method exists
     */
    private double readPercent(String key){
        try{
            var val = form.readString(key);
            return Double.parseDouble(val.replace("%", "").trim()) / 100.0;
        }catch (Exception e){
            form.clear(key);
            form.writeString(key, "NAN");
            throw e;
        }
    }

    /**
     * Reads an integer from the field in this form with name {@code key}
     *
     * @param key the key to the item in our form we want to read an integer from
     * @return the read integer
     * throws {@link NumberFormatException} if this exception is thrown the input box will first be set to "NAN" before
     * this method exists
     */
    private int readInteger(String key){
        try{
            var val = form.readString(key);
            return Integer.parseInt(val);
        }catch (Exception e){
            form.clear(key);
            form.writeString(key, "NAN");
            throw e;
        }
    }

    /**
     * This reads the user preferences for the generator, generates new text from those preferences and
     * our provided dictionary, appends the generated text to our generated text area while also wrapping
     * our text for us (how nice and very original).
     */
    private void generate(){
        try{
            // read the user adjustable values
            var chattiness = this.readPercent(CHATTINESS_PERCENT_INPUT_KEY);
            var conjunction = this.readPercent(CONJUNCTION_PERCENT_INPUT_KEY);
            var preposition = this.readPercent(PREPOSITION_PERCENT_INPUT_KEY);
            var properProportion = this.readPercent(PROPER_PROPORTION_PERCENT_INPUT_KEY);
            var descriptiveness =  this.readPercent(DESCRIPTIVENESS_PERCENT_INPUT_KEY);
            var maxDetail = this.readInteger(MAX_DETAIL_INPUT_KEY);

            // create a new generator and generate an output
            var generator = new Generator(this.dic, chattiness, conjunction, preposition, properProportion, descriptiveness, maxDetail);
            var outputText = generator.generate();

            // this just wraps text
            // yes yes the while not empty is sorta redundant because we break
            // out of the loop anyways but who's keeping track of dumb things
            // I've done on this assignment anyways :p
            while (!outputText.isEmpty()){
                // get the ending Index
                var endIndex = outputText.length();
                // if its greater than the number of columns (character width)
                // of our output (minus 2 for looks)
                // split the string at the last occurring space, write the LHS of that
                // split to our output and append a newline and make the RHS our new output text
                //
                // otherwise print the remainder of our text and break out of the loop
                if (endIndex > NUM_COLUMNS_OUTPUT - 2){
                    endIndex = outputText.lastIndexOf(' ', NUM_COLUMNS_OUTPUT - 2);
                    form.writeString(OUTPUT_TEXT_KEY, outputText.substring(0, endIndex + 1) + "\n");
                    outputText = outputText.substring(endIndex + 1).trim();
                }else{
                    form.writeString(OUTPUT_TEXT_KEY, outputText);
                    break;
                }
            }
            // add some extra space for the next output
            form.writeString(OUTPUT_TEXT_KEY, "\n\n");
            form.setEditable(OUTPUT_TEXT_KEY, false);
        }catch (NumberFormatException e) {
            // Ignored (the ui will already indicate what number was ill formmated before
            // we've gotten here but we'd want to not catch anyother exceptions
        }
    }

    /**
     * Clears the generated text output field
     */
    private void clear(){
        form.clear(OUTPUT_TEXT_KEY);
        form.setEditable(OUTPUT_TEXT_KEY, false);
    }

    /**
     * Prompts the user to create/select a file that will be filled with the current
     * contents of the generated output text field.
     */
    private void save(){
        try{
            // prompt the user for a file to save to
            var dataFile = new ASCIIOutputFile();
            // for each character in our generated text field write it to the file
            var line = form.readC(OUTPUT_TEXT_KEY);
            while (!(form.isEOF() || form.isDataError())){
                dataFile.writeC(line);
                line = form.readC(OUTPUT_TEXT_KEY);
            }
        }catch (Exception e){
            //Ignore
        }
    }


}
