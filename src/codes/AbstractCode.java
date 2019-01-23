package codes;

/**
 * This is the base for an error correcting code.
 * An instantiatable child class must implement methods that
 * add parity checks to the data and
 * fix corrupted data using the received parity.
 *
 * @author 150009974
 * @version 2.2
 */
public abstract class AbstractCode {

    /**
     * Matrix for parity generation.
     * The input word is multiplied by this matrix to obtain the parity bits.
     * The word and these bits are then attached to form the codeword.
     * This matrix is also used to check the correctness of a given codeword.
     */
    Word[] X;

    public int getDataLength() {
        return X.length;
    }
    public int getCodeLength() {
        if (X.length == 0) return 0;
        return X.length+X[0].length();
    }

    Word calculateParity(Word w) {
        return w.multiply(X);
    }

    public abstract Word fix(Word wordToFix, Word estimatedParity);
}
