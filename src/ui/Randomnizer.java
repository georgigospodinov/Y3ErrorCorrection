package ui;

import java.util.Random;

/**
 * Contains static methods used for generating random values.
 *
 * @author 150009974
 */
class Randomnizer {
    
    private static final Random RNG = new Random();
    
    static String nextMessage(long length, double probabilityOf1) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < length; i ++)
            sb.append(RNG.nextDouble() < probabilityOf1 ? '1' : '0');
        return sb.toString();
    }

    static String corrupt(String message, double corruptChance) {
        StringBuilder sb = new StringBuilder(message);
        for (int i = 0; i < message.length(); i++) {
            if (RNG.nextDouble() < corruptChance)
                sb.setCharAt(i, message.charAt(i) == '1' ? '0' : '1');
        }
        return sb.toString();
    }

    static String invert(String message) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < message.length(); i++)
            sb.append('1'-message.charAt(i));

        return sb.toString();
    }

}
