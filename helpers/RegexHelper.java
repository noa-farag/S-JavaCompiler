package ex5.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Helper class for regex with static methods.
 * will be called without an instance of the class.
 * @auther noa.farag, noya.ashkenazi
 */
public class RegexHelper {

    /**
     * A method that checks if a word matches a regex phrase.
     *
     * @param regexPhrase the regex phrase.
     * @param word        the word to check.
     * @return true if the word matches the regex phrase, false otherwise.
     */
    public static boolean regexMatches(String regexPhrase, String word) {
        Pattern pattern = Pattern.compile(regexPhrase);
        Matcher matcher = pattern.matcher(word);
        return matcher.matches();
    }

    /**
     * A method that checks if a word contains a regex phrase.
     *
     * @param regexPhrase the regex phrase.
     * @param word        the word to check.
     * @return true if the word contains the regex phrase, false otherwise.
     */
    public static boolean regexFind(String regexPhrase, String word) {
        Pattern pattern = Pattern.compile(regexPhrase);
        Matcher matcher = pattern.matcher(word);
        return matcher.find();
    }
}