package lab1;

import java.io.IOException;
import java.util.Map;

import static lab1.Entropy.*;
import static lab1.Vigenere.*;

/**
 * Created by Сергей on 20.03.2016.
 */
public class VigenereTest {
    public static void main(String[] args) throws IOException {
        //text reading
        String bigText = readFile("src/lab1/plaintexts/Don.txt"); //large file for frequencies analyse, first task
        //String bigText = readFile("src/lab1/plaintexts/LT.txt");
/*
        //first part of task
        Entropy ent = new Entropy(bigText); // text for entropy measuring

        ent.calculateFrequencies(); //for text with spaces and crossed bigrams
        System.out.println("Frequencies for text with spaces");
        ent.printSortedFreq(); //sorted list with spaces
        ent.calculateEntropy();
        // Entropy for one letter with spaces H1 = 4.355412528401781
        // Entropy for bigrams with spaces (bigrams cross) H2 = 3.9475244164121484

        System.out.println("Frequencies for bigrams");
        ent.setIsOneLetter(false);
        ent.printSortedFreq();


        ent.setIsOneLetter(true);
        ent.setCrossedBigrams(false); //for uncrossed bigrams
        ent.calculateEntropy();
        // Entropy for bigrams with spaces (bigrams uncross) H2 = 3.9474649499586527

        ent.setWithSpace(false); // for text without spaces
        ent.calculateFrequencies();

        ent.setCrossedBigrams(true);
        ent.calculateEntropy();
        // Entropy for one letter without spaces H1 = 4.448884063325011
        // Entropy for bigrams without spaces (bigrams cross) H2 = 4.130979877400885


        System.out.println("Frequencies for text without spaces"); // for text with spaces
        ent.printSortedFreq(); //list without spaces
*/

        // second part of task

        //Entropy object for measuring letter frequency in the language
        Entropy lang = new Entropy(bigText, true);
        //Map<String, Double> language = lang.getLangLetterFreq();
        //lang.printSortedFreq(); //frequency of letter on big text


        double index = calcIndex(filtration(bigText, ""));
        System.out.println("Index value for language: " + index + "\n");


        // text for enciphering
        String shortText = readFile("src/lab1/plaintexts/DonSnippet.txt");
        double indexP = calcIndex(filtration(shortText, ""));
        System.out.println("Index value for plaintext: " + indexP);

        Vigenere vigenere = new Vigenere(shortText);
        String ciphertext2 = vigenere.enciphering("ши");
        System.out.println("Index value for text encrypted by key with length 2: " + calcIndex(ciphertext2));
        String ciphertext3 = vigenere.enciphering("шиф");
        System.out.println("Index value for text encrypted by key with length 3: " + calcIndex(ciphertext3));
        String ciphertext4 = vigenere.enciphering("шифр");
        System.out.println("Index value for text encrypted by key with length 4: " + calcIndex(ciphertext4));
        String ciphertext5 = vigenere.enciphering("шифро");
        System.out.println("Index value for text encrypted by key with length 5: " + calcIndex(ciphertext5));
        String ciphertext10 = vigenere.enciphering("шифроватьм");
        System.out.println("Index value for text encrypted by key with length 10: " + calcIndex(ciphertext10));
        String ciphertext20 = vigenere.enciphering("шифроватьмынебросима");
        System.out.println("Index value for text encrypted by key with length 20: " + calcIndex(ciphertext20));

/*
        // methods verification on known ciphertext
        String key1 = findKey(ciphertext20, 20);
        System.out.println("Key length for cipher: " + findKeyLength(ciphertext20));
        System.out.println(key1);
        //String plaintext = decrypt(ciphertext20, key1);
        String plaintext = decrypt(ciphertext20, "шифроватьмынебросима");
        System.out.println(plaintext);
 */

        // deciphering for text from task
        String cipherTask = filtration(readFile("src/lab1/var/cipher.txt"), "");
        //String cipherTask = readFile("src/lab1/var/12.txt");
        int keyLength = findKeyLength(cipherTask);
        System.out.println("Key length for cipher: " + keyLength);
        System.out.println("Index value for cipher from task: " + calcIndex(filtration(cipherTask, "")));

        String keys = findKey(cipherTask, keyLength);
        System.out.println("Possible keys");
        System.out.println(keys);
        String key = keys.split(" |\n")[1];
        System.out.println("First variant of decryption by the most possible key " + key);
        String plaintext = deciphering(cipherTask, key);
        System.out.println(plaintext);

        System.out.println("You need to look on possible keys and choose the most probable combination");
        String plaintext1 = deciphering(cipherTask, "родинабезразличия");
        //String plaintext1 = deciphering(cipherTask, "чугунныенебеса");
        System.out.println("Corrected variant of decryption");
        System.out.println(plaintext1);

/*
        // writing ciphers in files
        createTextFile("src/lab1/ciphertexts/ciphertext2.txt", ciphertext2);
        createTextFile("src/lab1/ciphertexts/ciphertext3.txt", ciphertext3);
        createTextFile("src/lab1/ciphertexts/ciphertext4.txt", ciphertext4);
        createTextFile("src/lab1/ciphertexts/ciphertext5.txt", ciphertext5);
        createTextFile("src/lab1/ciphertexts/ciphertext10.txt", ciphertext10);
        createTextFile("src/lab1/ciphertexts/ciphertext20.txt", ciphertext20);
*/
    }
}
