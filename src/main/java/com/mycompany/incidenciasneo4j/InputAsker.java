package com.mycompany.incidenciasneo4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Class to ask input to user.
 * 
 * @author mfontana
 */
public class InputAsker {

    /**
     * Request String
     *
     * @param message String to show to user
     * @return String written by the user
     */
    public static String askString(String message) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer = "";
        do {
            try {
                System.out.println(message);
                answer = br.readLine();
                if (answer.equals("")) {
                    System.out.println("You must write something.");
                }
            } catch (IOException ex) {
                System.out.println("Error input / output.");
            }
        } while (answer.equals(""));
        return answer;
    }

    /**
     * Request String, it can only be optionA or optionB
     *
     * @param message String to show to user
     * @param optionA String option a accepted.
     * @param optionB String option b acceptedn
     * @return String (optionA or optionB)
     */
    public static String askString(String message, String optionA, String optionB) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer = "";
        do {
            answer = askString(message);
            if (!answer.equalsIgnoreCase(optionA) && !answer.equalsIgnoreCase(optionB)) {
                System.out.println("Wrong answer! Write " + optionA + " or " + optionB + " please");
            }
        } while (!answer.equalsIgnoreCase(optionA) && !answer.equalsIgnoreCase(optionB));
        return answer;
    }

    /**
     * Request String from a ArrayList of String accepteds
     *
     * @param message String to show user
     * @param wordsAccepted ArrayList of String accepted
     * @return String written by user, within the accepted words
     */
    public static String askString(String message, ArrayList<String> wordsAccepted) {
        String answer;
        boolean wordOk;
        do {
            for (String word : wordsAccepted) {
                System.out.println(word);
            }
            answer = InputAsker.askString(message);
            wordOk = wordIsOk(answer, wordsAccepted);
            if (!wordOk) {
                System.out.println("Wrong answer!");
            }
        } while (!wordOk);
        return answer;
    }

    /**
     * Returns true if the word exists in wordsAccepted.
     * 
     * @param word to find
     * @param wordsAccepted ArrayList of String accepted
     * @return boolean true if word is in wordsAccepted
     */
    private static boolean wordIsOk(String word, ArrayList<String> wordsAccepted) {
        for (String w : wordsAccepted) {
            if (w.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Request int
     *
     * @param message
     * @return int
     */
    public static int askInt(String message) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int num = 0;
        boolean error;
        do {
            try {
                System.out.println(message);
                num = Integer.parseInt(br.readLine());
                error = false;
            } catch (IOException ex) {
                System.out.println("Error input / output.");
                error = true;
            } catch (NumberFormatException ex) {
                System.out.println("Please, write integer number.");
                error = true;
            }
        } while (error);
        return num;
    }

    /**
     * Request int in the interval between min and max (inclusives)
     *
     * @param message
     * @param min
     * @param max
     * @return int
     */
    public static int askInt(String message, int min, int max) {
        int num;
        do {
            num = askInt(message);
            if (num < min || num > max) {
                System.out.println("Error, the number must be between " + min + " and " + max);
            }
        } while (num < min || num > max);
        return num;
    }

}
