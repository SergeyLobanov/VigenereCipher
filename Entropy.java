package lab1;

import java.io.*;
import java.util.*;

/**
 * Created by Сергей on 11.03.2016.
 */
public class Entropy {
    //here small cyrillic characters is begin
    static final int START_CYRILLIC = 1072;
    private String text;

    private boolean withSpace;
    private boolean crossedBigrams = true;
    private boolean isOneLetter = true;

    private double[] freqArray;
    private double[][] freqBigrams;
    private static Map<String, Double> langLetterFreq;

    public Entropy(String text){
        this.text = text;
        this.withSpace = true;
    }

    //to get language frequencies
    public Entropy(String text, boolean isBig) {
        this(text);
        if (isBig) {
            String clearText = filtration(text, "");
            this.freqArray = countFrequency(clearText);
            // addition measuring for athene transpositions enciphering
            this.freqBigrams = countFrequency(clearText, crossedBigrams);
            langLetterFreq = sortMapsByValue();
        }
    }

    public void setWithSpace(boolean withSpace) {
        this.withSpace = withSpace;
    }

    public void setCrossedBigrams(boolean crossedBigrams) {
        this.crossedBigrams = crossedBigrams;
    }

    public void setIsOneLetter(boolean isOneLetter) {
        this.isOneLetter = isOneLetter;
    }

    public String getText() {
        return this.text;
    }

    public void getCustomisation() {
        System.out.println("space: " + withSpace + " crossed: " + crossedBigrams + " one letter: " + isOneLetter);
    }

    public void setDefaultSettings() {
        this.withSpace = false;
        this.crossedBigrams = true;
        this.isOneLetter = true;
    }

    public Map<String, Double> getLangLetterFreq() {
        return langLetterFreq;
    }

    public void calculateFrequencies() {
        String clearText = filtration(text, (withSpace ? " " : ""));
        this.freqArray = countFrequency(clearText);
        this.freqBigrams = countFrequency(clearText, crossedBigrams);
    }

    //to get entropy for text with or without spaces and initialize frequency arrays
    public void calculateEntropy() {
        String clearText = filtration(text, (withSpace ? " " : ""));
        this.freqArray = countFrequency(clearText);
        this.freqBigrams = countFrequency(clearText, crossedBigrams);
        System.out.println("Entropy for one letter " + (withSpace ? "with spaces " : "without spaces ") + "H1 = " + entropy(freqArray));
        System.out.println("Entropy for bigrams " + (withSpace ? "with spaces " : "without spaces ")
                + (crossedBigrams ? "(bigrams cross) " : "(bigrams uncross) ")+ "H2 = " + entropy(freqBigrams));
        System.out.println();
    }

    // +text filtration
    public static String filtration(String text, String filterTo) {
        return text.replaceAll("[^а-яА-Я]+", filterTo).replaceAll("ё", "е").toLowerCase();
    }

    // +count frequency of each letter in the text
    public static double [] countFrequency(String text) {
        double freqArray [] = new double[33];

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' ') {
                freqArray[text.charAt(i) - START_CYRILLIC]++;
            }
            else freqArray[32]++;
        }

        for (int i = 0; i < 33; i++) {
            freqArray[i] /= text.length();
        }

        return freqArray;
    }

    // +two cases counting: for counting joint and disjoint bigrams
    private double [][] countFrequency(String text, boolean crossedBigrams) {
        double freqBArray [][] = new double[33][33];

        for (int i = 0; i < text.length() - 2; ) { //crossing bigrams depends on crossedBigrams variable
            if (text.charAt(i) != ' ' && text.charAt(i+1) != ' ') {
                freqBArray[text.charAt(i) - START_CYRILLIC][text.charAt(i+1) - START_CYRILLIC]++;
            }
            else if (text.charAt(i) != ' ' && text.charAt(i+1) == ' ') {
                freqBArray[text.charAt(i) - START_CYRILLIC][32]++;
            }
            else {
                freqBArray[32][text.charAt(i+1) - START_CYRILLIC]++;
            }
            if (crossedBigrams) i++;
            else i+=2;
        }

        for (int i = 0; i < 33; i++) {
            for (int j = 0; j < 33; j++) {
                if (crossedBigrams) {
                    freqBArray[i][j] /= text.length() - 1;
                }
                else {
                    freqBArray[i][j] /= text.length()/2; //for counting disjoint bigrams
                }
            }
        }

        return freqBArray;
    }

    // +binding frequency value with corresponding character
    private HashMap<String, Double> bindFreqLetter(double[] freqArray) {
        HashMap<String, Double> corrFreq = new HashMap<String, Double>();

        for (int i = 0; i < 32; i++) {
            corrFreq.put(((char)(i + START_CYRILLIC)) + "", freqArray[i]);
        }

        if (freqArray[32] != 0) {
            corrFreq.put("_", freqArray[32]);
        }
        //this.letterMap = corrFreq;
        return corrFreq;
    }

    // +binding frequency value with corresponding character pairs (bigrams)
    private HashMap<String, Double> bindFreqLetter(double[][] freqArray) {
        HashMap<String, Double> corrFreq = new HashMap<String, Double>();

        for (int i = 0; i < freqArray.length; i++) {
            for (int j = 0; j < freqArray[0].length; j++) {
                if (i != 32 && j != 32) {
                    corrFreq.put(((char)(i + START_CYRILLIC)) + "" + (char)(j + START_CYRILLIC), freqArray[i][j]);
                }
                else if (i != 32) {
                    corrFreq.put(((char)(i + START_CYRILLIC)) + "_", freqArray[i][j]);
                }
                else if (j != 32) {
                    corrFreq.put("_" + (char)(j + START_CYRILLIC), freqArray[i][j]);
                }
                else corrFreq.put("__", freqArray[i][j]);
            }
        }
        //this.bigramsMap = corrFreq;
        return corrFreq;
    }

    // +sorting map by frequency
    private <String, Double extends Comparable<? super java.lang.Double>> HashMap<java.lang.String, java.lang.Double>
    sortMapsByValue() {

        List<Map.Entry<java.lang.String, java.lang.Double>> list =
                new LinkedList<>( isOneLetter ? bindFreqLetter(freqArray).entrySet() : bindFreqLetter(freqBigrams).entrySet() );

        Collections.sort( list, (o1, o2) -> (o2.getValue()).compareTo( o1.getValue() ));

        /*
        // without lambda
        Collections.sort( list, new Comparator<Map.Entry<java.lang.String, java.lang.Double>>()
        {
            @Override
            public int compare(Map.Entry<java.lang.String, java.lang.Double> o1, Map.Entry<java.lang.String, java.lang.Double> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );*/

        Map<java.lang.String, java.lang.Double> result = new LinkedHashMap<>();
        for (Map.Entry<java.lang.String, java.lang.Double> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }


        return (HashMap<java.lang.String, java.lang.Double>)result;
    }

    public void printSortedFreq() {
        Map<String, Double> sortedMap = sortMapsByValue();
        for(Map.Entry<String, Double> entry : sortedMap.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
        System.out.println();
    }

    //addition method for first bigrams printing
    public void printFirstNSortedFreq(int N) {
        Map<String, Double> sortedMap = sortMapsByValue();
        int k = 1;
        for(Map.Entry<String, Double> entry : sortedMap.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
            if (k == N) {
                break;
            }
            k++;
        }
        System.out.println();
    }

    // +H1 counting
    private static double entropy(double [] freqArray) {
        double entr = 0;

        for(double frEl : freqArray){
            if (frEl != 0) {
                entr -= frEl * Math.log(frEl) / Math.log(2);
            }
        }

        return entr;
    }

    // +H2 counting
    private static double entropy(double [][] freqArray) {
        double entr = 0;

        for (double[] iArr : freqArray) {
            for (double ijElement : iArr) {
                if(ijElement != 0) {
                    entr -= ijElement * Math.log(ijElement) / Math.log(2);
                }
            }
        }

        return entr/2;
    }


    public static char getMaxFreqNElement(int elementN) {
        String freqNLetter = null;

        Iterator<String> iterator = langLetterFreq.keySet().iterator();
        int i = 0;
        while (i < elementN && iterator.hasNext()) {
            freqNLetter = iterator.next();
            i++;
        }

        return freqNLetter != null ? freqNLetter.charAt(0) : 'W';
    }

    public static char getMaxFreqElement(double [] freqArray) {
        double maxElem = 0;
        int indexOfMaxElem = 0;

        for (int i = 0; i < freqArray.length; i++) {
            if (maxElem < freqArray[i]) {
                maxElem = freqArray[i];
                indexOfMaxElem = i;
            }
        }

        return (char)(indexOfMaxElem + START_CYRILLIC);
    }

    // index of corresponding
    public static double calcIndex(String text) {
        int numLetters[] = new int[32];
        double corrIndex = 0;

        for (int i = 0; i < text.length(); i++) {
            numLetters[text.charAt(i) - START_CYRILLIC]++;
        }

        int nextLength = text.length();

        for (int i = 0; i < 32; i++){
            corrIndex += (numLetters[i])/(nextLength*(nextLength - 1.0))*(numLetters[i] - 1);
        }

        return corrIndex;
    }

    // +read data from file
    public static String readFile(String fileName) throws IOException {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "Cp1251"))) {
            //try statement with resources, using AutoClosable
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        }
    }

    // writing text to file
    public static void createTextFile(String fileName, String text) {
        File file = new File(fileName);
        try {
            //if file don't exist create new file
            if(!file.exists()){
                file.createNewFile();
            }

            try (PrintWriter out = new PrintWriter(file.getAbsoluteFile())) {
                //text writing
                out.print(text);
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
