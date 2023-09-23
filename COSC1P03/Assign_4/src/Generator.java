import java.util.ArrayList;

/**
 * @author Parker TenBroeck 7376726
 */
public class Generator {

    /**
     * the %chance that another sentence will be appended to the current one in {@code generateText}
     */
    private double chattiness = 0.95;
    /**
     * the %chance that the conjunction path will be chosen in {@code generateSentence}
     */
    private double conjunction = 0.2;
    /**
     * the %chance a prepositional phrase will be inserted in {@code generateNounPhrase}
     */
    private double preposition = 0.2;
    /**
     * the %chance a proper noun will be inserted
     */
    private double properProportion = 0.5;
    /**
     * the %chance a adverb and adjective will be inserted
     */
    private double descriptiveness = 0.5;
    /**
     * The current dictionary to use when generating our text
     */
    private Dictionary dic;

    /**
     * This is to help protect against both stack-overflows and taking an unreasonable amount of branches
     * when preforming operations that branch two or more times to the same level (looking at you sentences).<br>
     *
     * Note that this isn't a hard stack frame or branch limit but will instead force the generator to take the least
     * detailed path while still forming "correct" sentences. even if they exclude detail. <br><br>
     *
     * Another thing to note is this will take priority over the %chances of the other tunable values
     * That means, if something has a 100% chance of occurring AND the maximum detail has been exceeded
     * the event will be ignored if there exists a path with less detail even though it has a 100% chance of occurring.<br><br>
     */
    private int maxDetail = 500;



    public Generator(Dictionary dic){
        this.dic = dic;
    }

    public Generator(Dictionary dic, double chattiness, double conjunction, double preposition, double properProportion, double descriptiveness, int maxDetail){
        this.dic = dic;
        this.chattiness = chattiness;
        this.conjunction = conjunction;
        this.preposition = preposition;
        this.properProportion = properProportion;
        this.descriptiveness = descriptiveness;
        this.maxDetail = maxDetail;
    }

    /**
     * Generates a random int in the range [0,bound)
     *
     * @param bound the upper bound of the integer
     * @return a random int between [0,bound)
     */
    private int randInt(int bound){
        return (int)(Math.random() * bound);
    }

    /**
     * Simulates a random change of something happening with a probability of {@code percentage}
     *
     * @param percentage  the percent change the event should occur
     * @return            whether or not the event will have "occurred"
     */
    private boolean percentageChance(double percentage){
        return Math.random() <= percentage;
    }

    /**
     * Generates a random word from the dictionary from section {@code wordKind}
     *
     * @param wordKind the section index to use when selecting random words from
     * @return         a random word in the section specified by {@code workKind}
     */
    private String randomDictionaryWord(int wordKind){
        // gets the section we want
        ArrayList<String> words = this.dic.getAllAtoms()[wordKind];
        // gets a random index into that section
        return words.get(randInt(words.size()));
    }

    /**
     * Actually generate the text
     *
     * @return the generated text
     */
    public String generate(){
        // this is more a helper to start the recursive process
        // starting detail is zero
        return generateText(0);
    }

    /**
     * This is the first step in our generator
     * this method represents our text stage <br><br>
     *
     * T -> S. <br>
     * T -> S. T <br><br>
     *
     * T: Text <br>
     * S: Sentence <br><br>
     *
     * the T -> S. T path is chosen at ranom with %chance {@code this.chattiness} <br>
     *
     * @param detail the level of detail we're currently at
     * @return       the generated sentence
     */
    private String generateText(int detail){
        // we need at least one sentence to start
        String text = this.generateSentence(true, detail + 1);
        // spare extra recursive calls by looping instead.
        // also make sure not to loop if we are at our max detail
        while (percentageChance(this.chattiness) && detail < maxDetail){
            // continue appending sentences while we have detail to spare
            // and also while we continue to decide to by chatty.
            text += ". " + this.generateSentence(true, detail + 1);
            // we still want a cutoff so after each iteration increment
            // our detail to 'simulate' a recursive call
            detail += 1;
        }
        // punctuation people punctuation.
        return text + ".";
    }

    /**
     * This method is the second layer of our generator. Representing the sentence grammar rule <br> <br>
     *
     * S -> S, Con S <br>
     * S -> NP VP <br> <br>
     *
     * S: Sentence <br>
     * Con: Conjunction <br>
     * NP: Noun Phrase <br>
     * VP: Verb Phrase <br> <br>
     *
     * the S -> S, Con S rule is selected with %chance {@code this.conjunction}
     *
     * @param firstCapital whether or not to capitalize the first letter of the sentence
     * @param detail       the level of detail we're currently at
     * @return             the Sentence we just generated
     */
    private String generateSentence(boolean firstCapital, int detail){
        // randomly chose between S -> S, Con S and S -> NP VP choosing S -> S, Con S this.conjunction% of the time
        // if we don't have anymore detail to spare the S -> NP VP rule will be chosen every time
        if (percentageChance(this.conjunction) && detail < maxDetail){
            // because this branches every %this.conjunctions it scales exponentially
            // we should treat the detail increase not as linear but as exponential
            // multiplying it by the percentage to branch each time the branch is encountered
            // we also add 1 to round up. ensuring that if detail is zero or some small value
            // the detail value will at least be increased linearly as a backup until the threshold is overcome.
            var estimatedDetailIncrease = (int)(detail  * (this.conjunction + 1)) + 1;

            // S -> S, Con S
            return this.generateSentence(firstCapital, estimatedDetailIncrease) + ", " +
                    this.randomDictionaryWord(Dictionary.CONJUNCTIONS_INDEX) +
                    " " + this.generateSentence(false, estimatedDetailIncrease);
        }else{
            // S -> NP VP
            var nounPhrase = this.generateNounPhrase(detail + 1);
            //capitalize the first letter our the generated NP text if needed
            if (firstCapital){
                nounPhrase = nounPhrase.substring(0,1).toUpperCase() + nounPhrase.substring(1);
            }
            return nounPhrase + " " + this.generateVerbPhrase((int)(detail  * (this.conjunction + 1)));
        }
    }

    /**
     * This method represents the Noun Phrase grammar rule. <br><br>
     *
     * NP -> PN <br>
     * NP -> A [Adj] CN [PP] <br> <br>
     *
     * NP: Noun Phrase <br>
     * PN: Proper Noun <br>
     * A: Article <br>
     * Adj: Adjective <br>
     * CN: Common Noun <br>
     * PP: Prepositional Phrase <br> <br>
     *
     * NP -> PN is taken {@code this.properProportion}% of the time
     * and is taken unconditionally if there is no more detail to spare <br> <br>
     *
     * [Adj] is added {@code this.descriptiveness}% of the time and <br>
     * [PP] is added {@code this.preposition}% of the time<br>
     *
     *
     * @param detail the level of detail we're currently at
     * @return       the generated noun phrase
     */
    private String generateNounPhrase(int detail){
        // randomly choose between NP -> PN and NP -> A [Adj] CN [PP] choosing NP -> PN this.properProportion% of the time
        // if we don't have anymore detail to spare choose NP -> PN unconditionally
        if (percentageChance(this.properProportion) || detail >= maxDetail){
            return this.randomDictionaryWord(Dictionary.PROPER_NOUNS_INDEX);
        }else{
            // we know we have detail to spare so we dont have to check when adding our prepositional phrase
            return this.randomDictionaryWord(Dictionary.ARTICLES_INDEX) + " " +
                    (this.percentageChance(this.descriptiveness) ? this.randomDictionaryWord(Dictionary.ADJECTIVES_INDEX) + " " : "") +
                    this.randomDictionaryWord(Dictionary.COMMON_NOUNS_INDEX) +
                    (this.percentageChance(this.preposition) ? " " +
                            this.generatePrepositionalPhrase(detail + 1): "");
        }
    }

    /**
     * This represents the prepositional phrase rule in the grammar.
     * PP -> P NP
     * PP: Prepositional Phrase
     * P: Preposition
     * NP: Noun Phrase
     *
     * @param detail  the level of detail we're currently at
     * @return        the generated prepositional phrase
     */
    private String generatePrepositionalPhrase(int detail){
        return randomDictionaryWord(Dictionary.PREPOSITIONS_INDEX) +
                " " + generateNounPhrase(detail + 1);
    }

    /**
     * This method represents the Verb Phrase in the grammar rule <br><br>
     *
     * VP -> [Adv] IV <br>
     * VP -> [Adv] TV NP <br>
     * VP -> TV IN [Adv] <br><br>
     *
     * VP: Verb Phrase <br>
     * Adv: Adverb <br>
     * IV: Intransitive Verb <br>
     * TV: Transitive Verb <br>
     * NP: Noun Phrase <br>
     * IN: Infinitive Verb <br><br>
     *
     * If we are out of detail only the paths VP -> [Adv] IV and VP -> TV IN [Adv] are taken. <br><br>
     *
     * [Adv] is added {@code this.descriptiveness}% of the time <br>
     *
     * @param detail the level of detail we're currently at
     * @return       the generated verb phrase
     */
    private String generateVerbPhrase(int detail){
        // Evenly select between all three rules evenly
        // unless we don't have more detail to spare
        // then only select between VP -> [Adv] IV and VP -> TV IN [Adv]
        switch (randInt(detail < maxDetail ? 3 : 2)){
            // VP -> [Adv] IV
            case 0:
                return (percentageChance(this.descriptiveness) ? this.randomDictionaryWord(Dictionary.ADVERBS_INDEX) + " " : "") +
                        this.randomDictionaryWord(Dictionary.INTRANSITIVE_VERBS_INDEX);
            // VP -> TV IN [Adv]
            case 1:
                return this.randomDictionaryWord(Dictionary.TRANSITIVE_VERBS_INDEX) + " " +
                        this.randomDictionaryWord(Dictionary.INFINITIVE_VERBS_INDEX) +
                        (percentageChance(this.descriptiveness) ? " " + this.randomDictionaryWord(Dictionary.ADVERBS_INDEX) : "");
            // VP -> [Adv] TV NP
            case 2:
                return (percentageChance(this.descriptiveness) ? this.randomDictionaryWord(Dictionary.ADVERBS_INDEX) + " " : "") +
                        this.randomDictionaryWord(Dictionary.TRANSITIVE_VERBS_INDEX) +
                        " " + this.generateNounPhrase(detail + 1);
            default:
                // hm wasn't expecting this to ever happen
                throw new RuntimeException("I cannot write a random int with bounds function properly IG (returned int is outside rang [0,3)");
        }
    }
}
