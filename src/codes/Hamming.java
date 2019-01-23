package codes;

import static codes.Word.next;
import static java.lang.Math.pow;

/**
 * Implementation of the Hamming Code.
 *
 * @author 150009974
 * @version 3.0
 */
public final class Hamming extends AbstractCode {

    public Hamming(int r) {
        int dimension = (int) (pow(2, r) - 1) - r;

        int[] checkBits = new int[r];
        for (int i = 0; i < r - 1; i++) checkBits[i] = 0;
        checkBits[r - 1] = 1;

        if (r > 1) checkBits[r - 2] = 1;

        X = new Word[dimension];
        for (int i = 0; i < dimension; i++) {
            X[i] = new Word(checkBits, 0);
            checkBits = next(checkBits);
        }

    }


    @Override
    public Word fix(Word wordToFix, Word estimatedParity) {
        for (int i = 0; i < X.length; i++)
            if (estimatedParity.equals(X[i]))
                return wordToFix.flip(i);

        return wordToFix;
    }

}
