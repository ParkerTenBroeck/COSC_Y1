import BasicIO.ASCIIDataFile;

import java.util.ArrayList;

/**
 * @author Parker TenBroeck 7376726
 */
public class Dictionary {
    public ArrayList<String> properNouns;
    public ArrayList<String> commonNouns;
    public ArrayList<String> articles;
    public ArrayList<String> adjectives;
    public ArrayList<String> intransitiveVerbs;
    public ArrayList<String> transitiveVerbs;
    public ArrayList<String> infinitiveVerbs;
    public ArrayList<String> adverbs;
    public ArrayList<String> prepositions;
    public ArrayList<String> conjunctions;

    // ------------------------------------------ behold the public static final ints
    public static final int PROPER_NOUNS_INDEX = 0;
    public static final int COMMON_NOUNS_INDEX = 1;
    public static final int ARTICLES_INDEX = 2;
    public static final int ADJECTIVES_INDEX = 3;
    public static final int INTRANSITIVE_VERBS_INDEX = 4;
    public static final int TRANSITIVE_VERBS_INDEX = 5;

    public static final int INFINITIVE_VERBS_INDEX = 6;
    public static final int ADVERBS_INDEX = 7;
    public static final int PREPOSITIONS_INDEX = 8;
    public static final int CONJUNCTIONS_INDEX = 9;
    // ------------------------------------------ truly a sight to see


    private Dictionary(){
        // ya I feel like this ones pretty self explanatory
        this.properNouns = new ArrayList<>();
        this.commonNouns = new ArrayList<>();
        this.articles = new ArrayList<>();
        this.adjectives = new ArrayList<>();
        this.intransitiveVerbs = new ArrayList<>();
        this.transitiveVerbs = new ArrayList<>();
        this.infinitiveVerbs = new ArrayList<>();
        this.adverbs = new ArrayList<>();
        this.prepositions = new ArrayList<>();
        this.conjunctions = new ArrayList<>();
    }

    /**
     * Files should be in the form
     * There are 10 word groups
     * Each word group should be in the form
     * ((\S+)\n)+\n
     * Except for the last being
     * ((\S+)\n)+
     *
     * @param fileName the name/path of the file to be loaded
     */
    public Dictionary(String fileName){
        // initialize self
        this();
        // initialize variables
        int wordKindIndex = 0;
        ASCIIDataFile file = new ASCIIDataFile(fileName);

        // for each line in the file
        var line = file.readLine();
        while(!(file.isEOF() || file.isDataError())){
            // trim
            line = line.trim();
            // if its blank thats a new section
            if (line.isBlank()){
                // increase the wordkind index and check if its still in bounds
                wordKindIndex += 1;
                if (wordKindIndex > CONJUNCTIONS_INDEX){
                    throw new IllegalArgumentException("Provided data file contains more sections then expected");
                }
            // otherwise add the line (word) to the current wordkind section
            }else{
                this.getAllAtoms()[wordKindIndex].add(line);
            }

            line = file.readLine();
        }
        // check that we have at least 1 word in each section
        // if not throw an exception because the provided file is stinky and bad
        var atomsList = this.getAllAtoms();
        for(int i = 0; i < atomsList.length; i ++){
            if (atomsList[i].size() == 0){
                throw new IllegalArgumentException("Provided data file has an empty or malformed " + this.getAtomNames()[i] + " section, each section must contain at least 1 word");
            }
        }
    }

    /**
     *
     * @return an array of all atom categories <br>
     * 0 -> proper nouns <br>
     * 1 -> common nouns <br>
     * 2 -> articles <br>
     * 3 -> adjectives <br>
     * 4 -> intransitive verbs <br>
     * 5 -> transitive verbs <br>
     * 6 -> infinitive verbs <br>
     * 7 -> adverbs <br>
     * 8 -> prepositions <br>
     * 9 -> conjunctions <br>
     */
    public ArrayList<String>[] getAllAtoms(){
        return new ArrayList[]{
                this.properNouns,
                this.commonNouns,
                this.articles,
                this.adjectives,
                this.intransitiveVerbs,
                this.transitiveVerbs,
                this.infinitiveVerbs,
                this.adverbs,
                this.prepositions,
                this.conjunctions
        };
    }

    /**
     *
     * @return an array of all atom categorie names <br>
     * 0 -> proper nouns <br>
     * 1 -> common nouns <br>
     * 2 -> articles <br>
     * 3 -> adjectives <br>
     * 4 -> intransitive verbs <br>
     * 5 -> transitive verbs <br>
     * 6 -> infinitive verbs <br>
     * 7 -> adverbs <br>
     * 8 -> prepositions <br>
     * 9 -> conjunctions <br>
     */
    public String[] getAtomNames(){
        return new String[]{
                "Proper Nouns",
                "Common Nouns",
                "Articles",
                "Adjectives",
                "Intransitive Verbs",
                "Transitive Verbs",
                "Infinitive Verbs",
                "Adverbs",
                "Prepositions",
                "Conjunctions",
        };
    }
}
