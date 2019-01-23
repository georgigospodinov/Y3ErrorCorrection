package codes;

import java.util.BitSet;

import static java.lang.Math.pow;

/**
 * Represents a word as sequence of bits.
 * Builds on top of the {@link BitSet} class to add needed functionality.
 *
 * @author 150009974
 * @version 2.1
 */
public class Word {

    private static final int NOT_FOUND = -1;

    private int nDataBits, nCheckBits;
    private BitSet bits;

    private Word(int numberOfDataBits, int numberOfCheckBits) {
        nDataBits = numberOfDataBits;
        nCheckBits = numberOfCheckBits;
        bits = new BitSet(numberOfDataBits + numberOfCheckBits);
    }

    Word(int[] bits, int numberOfDataBits) {
        this(numberOfDataBits, bits.length - numberOfDataBits);
        for (int i = 0; i < bits.length; i++)
            if (bits[i] == 1) this.bits.set(i);
    }

    Word(String bits, int numberOfDataBits) {
        this(numberOfDataBits, bits.length() - numberOfDataBits);

        for (int i = 0; i < bits.length(); i++)
            if (bits.charAt(i) == '1') this.bits.set(i);
    }

    Word(String bits) {
        this(bits, 0);
    }

    private Word(BitSet bs, int numberOfDataBits, int numberOfCheckBits) {
        this(numberOfDataBits, numberOfCheckBits);
        bits = bs;
    }

    Word flip(int index) {
        bits.flip(index);
        return this;
    }

    /**
     * Adds the given word to this one.
     * This method modifies this word.
     *
     * @param other word to add
     * @return this (after being modified)
     */
    Word add(Word other) {
        if (other == null) return this;

        bits.xor(other.bits);
        return this;
    }

    private Word multiply(boolean bit) {
        BitSet product = new BitSet();
        if (bit) product.or(bits);
        return new Word(product, nDataBits, nCheckBits);
    }

    /**
     * Calculates the product of this word with the given matrix.
     * A new instance of this class is created to store the product.
     *
     * @param matrix the matrix to multiply by
     * @return a new word instance that is the product of the multiplication
     */
    Word multiply(Word[] matrix) {
        if (matrix.length == 0) return new Word(0, 0);

        // Each row is multiplied by one bit from this word.
        Word m = matrix[0].multiply(bits.get(0));
        for (int i = 1; i < nDataBits + nCheckBits; i++)
            m.add(matrix[i].multiply(bits.get(i)));

        return m;
    }

    /**
     * Consecutively multiplies this word by each bit of the given word
     * and concatenates the results, thus forming a new row in the Kronecker product.
     *
     * @param other the word to multiply by
     * @return the concatenation of the products
     */
    Word kroneckerMultiply(Word other) {
        Word current = this.multiply(other.bits.get(0));
        for (int i = 1; i < other.nDataBits + other.nCheckBits; i++)
            current = current.concat(this.multiply(other.bits.get(i)));

        return current;
    }

    Word concat(Word other) {
        return new Word(this.toString() + other.toString(), this.nDataBits);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < nDataBits + nCheckBits; i++)
            sb.append(bits.get(i) ? '1' : '0');

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Word &&
                this.toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        int value = 0;
        for (int i = 0; i < nDataBits + nCheckBits; i++)
            value += bits.get(i) ? pow(2, nDataBits + nCheckBits - 1 - i) : 0;

        return value;
    }

    public int length() {
        return nDataBits + nCheckBits;
    }

    private static int findFirstOne(int[] bits) {
        for (int i = 0; i < bits.length; i++)
            if (bits[i] == 1) return i;

        return NOT_FOUND;
    }

    static int[] next(int[] current) {

        // Bottom of recursion.
        if (current.length == 1)
            // Either there is no more or add 1.
            return current[0] == 1 ? null : new int[]{1};

        int position = findFirstOne(current);

        // If 0s only, add 1 at the end and return.
        if (position == NOT_FOUND) {
            current[current.length - 1] = 1;
            return current;
        }

        // If the 1 is not leading, move it to the left.
        if (position != 0) {
            current[position - 1] = 1;
            current[position] = 0;
            return current;
        }

        /* 1 is leading
         * recurse with all the bits to the right
         * move the 1 to the left of the sub-first-one
         */
        int[] workingCopy = new int[current.length - 1];
        System.arraycopy(current, 1, workingCopy, 0, workingCopy.length);
        workingCopy = next(workingCopy);
        if (workingCopy == null) return null;

        System.arraycopy(workingCopy, 0, current, 1, workingCopy.length);
        current[0] = 0;
        current[findFirstOne(current) - 1] = 1;

        return current;
    }

    public static int distance(String s1, String s2) {
        Word w1 = new Word(s1);
        Word w2 = new Word(s2);
        w1.bits.xor(w2.bits);
        return w1.bits.cardinality();
    }

    public static int distance(Word w1, Word w2) {
        w1.bits.xor(w2.bits);
        int d = w1.bits.cardinality();
        w1.bits.xor(w2.bits);
        return d;
    }

}
