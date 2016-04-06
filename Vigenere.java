package lab1;


import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lab1.Entropy.*;
import static lab1.Entropy.START_CYRILLIC;


/**
 * Created by Сергей on 07.03.2016.
 */
public class Vigenere {
    static final int START_CYRILLIC = 1072;
    private String plaintext;
    private String ciphertext;

    Vigenere(String plaintext) {
        this.plaintext = filtration(plaintext, "");
    }

    // here is enciphering by Vigenere
    public String enciphering(String key) {
        StringBuilder ciphertext = new StringBuilder();
        char currentY;

        for(int i = 0; i < plaintext.length();i++)
        {
            currentY = (char)((plaintext.charAt(i) + key.charAt(i % key.length()) - 2 * START_CYRILLIC) % 32 + START_CYRILLIC);

            ciphertext.append(currentY);
        }

        return ciphertext.toString();
    }

    // finding length of key for text
    public static int findKeyLength(String ciphertext) {
        int [] matchForR = new int[31];

        for (int r = 6; r < matchForR.length; r++) {
            for (int i = 0; i < ciphertext.length() - r; i++) {
                if(ciphertext.charAt(i) == ciphertext.charAt(i + r)) {
                    matchForR[r]++;
                }
            }
        }
        int keyLength = 0;
        int biggestMatching = 0;

        for (int i = 6; i < matchForR.length; i++) {
            //System.out.println("Kronecker symbol for " + i + " is " + matchForR[i]);
            if (biggestMatching < matchForR[i]) {
                biggestMatching = matchForR[i];
                keyLength = i;
            }
        }

        return keyLength;
    }

    public static String findKey(String ciphertext, int keyLength) {
        StringBuilder [] divText = new StringBuilder[keyLength];
        //String [] divText = new String[keyLength];

        // create new StringBuilder objects
        for (int i = 0; i < keyLength; i++) {
            //divText[i] = new String();
            divText[i] = new StringBuilder();
        }

        // dividing text on subtexts by key length period
        for (int i = 0; i < ciphertext.length(); i++) {
            //divText[i % keyLength] = new StringBuilder(divText[i % keyLength]).append(ciphertext.charAt(i)).toString();
            divText[i % keyLength].append(ciphertext.charAt(i));
        }

        // bringing StringBuilder object to String
        String [] divStringText = new String[keyLength];//= divText;  //new String[keyLength];
        for (int i = 0; i < divText.length; i++) {
            divStringText[i] = divText[i].toString();
        }

        //StringBuilder key = new StringBuilder();
        // creating few variants of keys for different values of the most frequency element
        StringBuilder key = new StringBuilder(getMaxFreqNElement(1) + " ");
        StringBuilder key1 = new StringBuilder(getMaxFreqNElement(2) + " ");
        StringBuilder key2 = new StringBuilder(getMaxFreqNElement(3) + " ");
        StringBuilder key3 = new StringBuilder(getMaxFreqNElement(4) + " ");

        char shiftedLetter;
        String tempY;
        for (int i = 0; i < keyLength; i++) {
            tempY = divStringText[i];
            shiftedLetter = getMaxFreqElement(countFrequency(tempY));
            //System.out.println("sl = " + (int)shiftedLetter + shiftedLetter + " getMaxFreqElement() " + (int)getMaxFreqElement()
            //+" div = " + (int)(char)((shiftedLetter - getMaxFreqElement() + 32) % 32 + START_CYRILLIC) + (char)((shiftedLetter - getMaxFreqElement()+32) % 32 + START_CYRILLIC));
            key.append((char)((shiftedLetter - getMaxFreqNElement(1)+32) % 32 + START_CYRILLIC));
            shiftedLetter = getMaxFreqElement(countFrequency(tempY));
            //System.out.println("sl = " + (int)shiftedLetter + shiftedLetter + " getMaxFreqElement() " + (int)getMaxFreqNElement(2)
            //        +" div = " + (int)(char)((shiftedLetter - getMaxFreqNElement(2) + 32) % 32 + START_CYRILLIC) + (char)((shiftedLetter - getMaxFreqElement()+32) % 32 + START_CYRILLIC));
            key1.append((char)((shiftedLetter - getMaxFreqNElement(2)+32) % 32 + START_CYRILLIC));
            shiftedLetter = getMaxFreqElement(countFrequency(tempY));
            //System.out.println("sl = " + (int)shiftedLetter + shiftedLetter + " getMaxFreqElement() " + (int)getMaxFreqNElement(3)
            //        +" div = " + (int)(char)((shiftedLetter - getMaxFreqNElement(2) + 32) % 32 + START_CYRILLIC) + (char)((shiftedLetter - getMaxFreqElement()+32) % 32 + START_CYRILLIC));
            key2.append((char)((shiftedLetter - getMaxFreqNElement(3)+32) % 32 + START_CYRILLIC));
            shiftedLetter = getMaxFreqElement(countFrequency(tempY));
            //System.out.println("sl = " + (int)shiftedLetter + shiftedLetter + " getMaxFreqElement() " + (int)getMaxFreqNElement(4)
            //       +" div = " + (int)(char)((shiftedLetter - getMaxFreqNElement(2) + 32) % 32 + START_CYRILLIC) + (char)((shiftedLetter - getMaxFreqElement()+32) % 32 + START_CYRILLIC));
            key3.append((char)((shiftedLetter - getMaxFreqNElement(4)+32) % 32 + START_CYRILLIC));
        }

        return key.toString() + "\n" + key1.toString() + "\n" + key2.toString()+ "\n" + key3.toString();
    }


    public static String deciphering(String ciphertext, String key)
    {
        StringBuilder text = new StringBuilder();
        for(int i = 0; i < ciphertext.length();i++)
        {
            char c = (char)(((ciphertext.charAt(i)  - key.charAt(i % key.length()) + 32) % 32) + START_CYRILLIC);

            text.append(c);
        }
        return text.toString();
    }

}
