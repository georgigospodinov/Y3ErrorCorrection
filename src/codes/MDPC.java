package codes;

import java.security.InvalidParameterException;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;

import static codes.Word.distance;
import static codes.Word.next;

/**
 * Implementation of Multi-Dimensional Parity Check.
 *
 * @author 150009974
 * @version 2.4
 */
public class MDPC extends AbstractCode {

    /* NOTE: eye(size) and ones(size) are almost always called with (r-1)
     * Only in the reduced table generation is there a call to eye
     * when nothing is subtracted from the parameter.
     *
     * In my implementation I subtract one in the eye(size) and ones(size) methods
     * and add one to the call in generateReduced() & generateMy().
     */

    /**
     * Creates an Identity matrix with the given size.
     * The main diagonal is composed of 1s, all other entries are 0s.
     *
     * @param size the size of the matrix to create.
     * @return the created Identity matrix.
     */
    private static Word[] eye(int size) {
        size--;
        int[] bits = new int[size];
        for (int i = 0; i < size; i++)
            bits[i] = 0;

        Word[] I = new Word[size];
        for (int i = size - 1; i >= 0; i--) {
            bits = next(bits);
            I[i] = new Word(bits, size);
        }

        return I;
    }

    private static Word[] ones(int size) {
        size--;
        Word[] ones = new Word[size];

        for (int i = 0; i < size; i++)
            ones[i] = new Word("1");

        return ones;
    }

    private static Word[] concat(Word[] matrixLeft, Word[] matrixRight) {

        assert (matrixLeft.length == matrixRight.length);
        Word[] m = new Word[matrixLeft.length];
        for (int i = 0; i < m.length; i++)
            m[i] = matrixLeft[i].concat(matrixRight[i]);

        return m;
    }

    /**
     * Performs the Kronecker Multiplication on the two matrices.
     * http://www.siam.org/books/textbooks/OT91sample.pdf
     *
     * @param matrixLeft  the matrix whose bits are multiplied by the other matrix
     * @param matrixRight the matrix that gets multiplied by the bits of the other matrix
     * @return the Kronecker product of the two matrices
     */
    private static Word[] kron(Word[] matrixLeft, Word[] matrixRight) {
        if (matrixRight.length == 0) return matrixLeft;

        Word[] product = new Word[matrixLeft.length * matrixRight.length];

        for (int i = 0; i < matrixLeft.length; i++)
            for (int j = 0; j < matrixRight.length; j++)
                product[i * matrixRight.length + j] = matrixRight[j].kroneckerMultiply(matrixLeft[i]);

        return product;
    }

    // Equivalent to 'r' in http://ieeexplore.ieee.org/document/7818772/?part=1
    private int[] dimensionLengths;

    /**
     * The following functions are left only for demonstration.
     * They cannot be used with the encoding method in {@link ErrorCorrection#encode(String, AbstractCode)}.
     * That code adds the data word after the multiplication with the matrix.
     * These two methods generate matrices that already include these bits.
     */
    //<editor-fold desc="The two implementations described in the paper.">
    private void generateReduced() {
        X = ones(2);  // start with a single 1

        for (int i = 0; i < dimensionLengths.length; i++) {
            X = kron(eye(dimensionLengths[i]), X);
            int x = 1;
            for (int j = 0; j < i; j++) x *= (dimensionLengths[j] - 1);
            Word[] Gtemp = kron(ones(dimensionLengths[i]), eye(x + 1));
            X = concat(X, Gtemp);
        }
    }

    private void generateReliable() {
        X = ones(2);  // start with a single 1
        for (int dimensionLength : dimensionLengths) {
            Word[] Gtemp = concat(eye(dimensionLength), ones(dimensionLength));
            X = kron(Gtemp, X);
        }
    }
    //</editor-fold>

    private void generateMy() {
        X = ones(dimensionLengths[0]);  // start with a number of ones, equal to r[0]
        for (int i = 1; i < dimensionLengths.length; i++) {
            if (dimensionLengths[i] == 1) continue;

            Word[] Ir = eye(dimensionLengths[i]);
            Word[] Ig = eye(X.length + 1);
            Word[] Ones = ones(dimensionLengths[i]);
            X = concat(kron(Ir, X), concat(kron(Ones, Ig), kron(Ones, X)));
        }

    }

    public MDPC(int[] dimensionLengths) {
        this.dimensionLengths = new int[dimensionLengths.length];
        System.arraycopy(dimensionLengths, 0, this.dimensionLengths, 0, dimensionLengths.length);
        generateMy();
    }

    private static class IndexDistance implements Comparable {

        private int index, distance;

        IndexDistance(int index, int distance) {
            this.index = index;
            this.distance = distance;
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof IndexDistance))
                throw new InvalidParameterException("IndexDistance can only be compared to IndexDistance");

            return Integer.compare(this.distance, ((IndexDistance) o).distance);
        }

    }

    private LinkedHashSet<Integer> findIndex(Word parity, LinkedHashSet<Integer> toSkip, int depth, int minDist) {

        // Order indexes by their FDM (minimum distance from a word in X).
        PriorityQueue<IndexDistance> queue = new PriorityQueue<>();
        for (int i = 0; i < X.length; i++)
            if (!toSkip.contains(i)) {
                int dist = distance(parity, X[i]);
                if (dist == 0) {  // Is this the fix we need?
                    LinkedHashSet<Integer> indexes = new LinkedHashSet<>();
                    indexes.add(i);
                    return indexes;
                }
                else if (dist <= minDist)  // Only enqueue fixes that can do better than the previous.
                    queue.add(new IndexDistance(i, dist));
            }

        // If no fix can do better than the previous,
        if (queue.isEmpty())
            // no need to continue in this branch.
            return null;

        // If we are at the bottom of the recursion,
        if (depth == this.dimensionLengths.length / 2)
            // the input has more errors than can be fixed.
            return null;

        // Increase the depth for the recursive calls.
        depth++;

        // Try each fix and recurse.
        LinkedHashSet<Integer> workingCopy = new LinkedHashSet<>(toSkip);
        while (!queue.isEmpty()) {
            IndexDistance id = queue.poll();
            parity.add(X[id.index]);
            workingCopy.add(id.index);
            LinkedHashSet<Integer> indexes = findIndex(parity, workingCopy, depth, id.distance);
            if (indexes != null) {
                indexes.add(id.index);
                return indexes;
            }

            // This index did not work, reverse back the word.
            parity.add(X[id.index]);
        }

        return null;

    }

    @Override
    public Word fix(Word wordToFix, Word estimatedParity) {
        LinkedHashSet<Integer> set = findIndex(estimatedParity, new LinkedHashSet<>(), 1, estimatedParity.length());
        if (set != null)
            for (int i : set)
                wordToFix.flip(i);
        return wordToFix;
    }

}
