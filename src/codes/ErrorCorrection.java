package codes;

/**
 * The static methods in this class perform the encoding and decoding of data.
 * These methods are called in {@link ui.L4CodePerformance}.
 *
 * @author 150009974
 * @version 2.2
 */
public class ErrorCorrection {

    /**
     * Creates a new string with length divisible by n.
     * The returned string is the same as the given but contains preceding zeros.
     * This prepares it for encoding/decoding.
     *
     * @param str String to extend.
     * @return Zero-extended string
     */
    public static String zeroExtend(String str, int n) {
        if (n == 0) return str;

        int remainder = str.length() % n;
        if (remainder != 0) {
            // Add leading zeros.
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < n - remainder; i++)
                sb.append('0');

            str = sb.append(str).toString();
        }

        return str;
    }

    /**
     * Encodes a given string using the given encoding technique.
     * The input string is extended with zeros so that its length is divisible
     * by the number of rows in the encoding matrix. The string is then split into chunks of that size.
     * The parity for each chunk is obtained from the encoding.
     * The chunk and the parity are attached to the output string.
     *
     * @param input    the string to encode
     * @param encoding the encoding technique to use
     * @return the encoded string
     */
    public static String encode(String input, AbstractCode encoding) {

        int dataLength = encoding.getDataLength();
        if (dataLength == 0) dataLength = input.length();
        input = zeroExtend(input, dataLength);

        StringBuilder code = new StringBuilder("");
        int chunks = input.length() / dataLength;
        for (int i = 0; i < chunks; i++) {
            Word current = new Word(input.substring(i * dataLength, (i + 1) * dataLength));
            code.append(current).append(encoding.calculateParity(current));
        }

        return code.toString();
    }

    public static String decode(String coded, AbstractCode encoding) {

        int dataLength = encoding.getDataLength();
        if (dataLength == 0) dataLength = coded.length();
        int codeLength = encoding.getCodeLength();
        if (codeLength == 0) codeLength = dataLength;

        coded = zeroExtend(coded, codeLength);
        StringBuilder output = new StringBuilder("");
        int chunks = coded.length() / codeLength;

        for (int i = 0; i < chunks; i++) {
            String dataPart = coded.substring(i * codeLength, i * codeLength + dataLength);
            Word data = new Word(dataPart, dataLength);

            String parityPart = coded.substring(i * codeLength + dataLength, (i + 1) * codeLength);
            Word parityReceived = new Word(parityPart, 0);

            // For the received data, calculate the expected parity and add it to the received parity
            Word parityCalculated = encoding.calculateParity(data).add(parityReceived);

            output.append(encoding.fix(data, parityCalculated));
        }

        return output.toString();
    }
}
