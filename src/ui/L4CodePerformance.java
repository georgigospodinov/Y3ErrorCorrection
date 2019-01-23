package ui;

import codes.AbstractCode;
import codes.Hamming;
import codes.MDPC;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.inference.OneWayAnova;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.util.LinkedList;

import static codes.ErrorCorrection.*;
import static codes.Word.distance;
import static ui.Randomnizer.corrupt;
import static ui.Randomnizer.invert;

/**
 * The functions here perform the main operations as required by the practical.
 * These include encoding, corrupting, decoding, and analysing.
 *
 * @author 150009974
 * @version 2.1
 */
class L4CodePerformance extends L3ComponentCreation {

    private void setMessage(boolean useUserMessage) {
        if (!useUserMessage) return;
        message = messageTextArea.getText();
        messageLength = message.length();
    }

    private void genMessage() {
        message = Randomnizer.nextMessage(messageLength, probabilityOfOne);
        messageTextArea.setText(message);
        messageLengthTextField.setText("" + messageLength);
    }

    void runExperiments(boolean useUserMessage) {
        clearTabs();
        runForEachTab(useUserMessage);
        runCustom(useUserMessage);
        writeMainAnalysis();
    }

    void runForEachTab(boolean useUserMessage) {
        setMessage(useUserMessage);

        // Create Tab.
        for (int i = 0; i < numberOfTabs; i++) {
            addElementsToTab(i);
            writeInShortTable(shortTable[i], hamming[i], "Hamming");
            writeInShortTable(shortTable[i], mdpc[i], "MDPC");
        }

        // Run algorithms.
        for (int runId = 1; runId <= timesToRun; runId++) {
            if (!useUserMessage) genMessage();

            for (int i = 0; i < numberOfTabs; i++)
                encodeCorruptDecode(runId, i);

        }

        // Write results.
        for (int i = 0; i < numberOfTabs; i++)
            writeSummary(longTable[i], sumDistances[i], best[i]);

    }

    void runCustom(boolean useUserMessage) {
        setMessage(useUserMessage);

        // Remove any previous rows.
        DefaultTableModel model = (DefaultTableModel) customShortTable.getModel();
        int rc = model.getRowCount();
        for (int i = 0; i < rc; i++) model.removeRow(0);
        model = (DefaultTableModel) customLongTable.getModel();
        rc = model.getRowCount();
        for (int i = 0; i < rc; i++) model.removeRow(0);
        prepareCountersCustom();

        writeInShortTable(customShortTable, customHamming, "Hamming");
        writeInShortTable(customShortTable, customMdpc, "MDPC");

        for (int i = 0; i < timesToRun; i++) {
            if (!useUserMessage) genMessage();
            encodeCorruptDecode(i);
        }
        writeSummary(customLongTable, customSumDistances, customBest);
    }

    private void writeInShortTable(JTable t, AbstractCode code, String title) {
        int dataLength = code.getDataLength(), codeLength = code.getCodeLength();
        if (dataLength == 0) dataLength = 1;
        if (codeLength == 0) codeLength = 1;
        DefaultTableModel dtm = (DefaultTableModel) t.getModel();
        double infoRate = 1.0 * dataLength / codeLength;
        dtm.addRow(new Object[]{title, dataLength, codeLength, infoRate, infoRate * errorProbability});
    }

    /**
     * Calls the main encodeCorruptDecode method with the parameters of tab with the given index.
     *
     * @param runId the id of the run
     * @param index the index of the tab
     */
    private void encodeCorruptDecode(int runId, int index) {
        encodeCorruptDecode(hamming[index], mdpc[index], runId, longTable[index],
                hammingEncodedTextField[index], hammingCorruptedTextField[index], hammingDecodedTextField[index],
                mdpcEncodedTextField[index], mdpcCorruptedTextField[index], mdpcDecodedTextField[index],
                best[index], sumDistances[index]);
    }

    /**
     * Calls the main encodeCorruptDecode method with the parameters of the 'custom tab'.
     *
     * @param runId the id of the run
     */
    private void encodeCorruptDecode(int runId) {
        encodeCorruptDecode(customHamming, customMdpc, runId, customLongTable,
                customHammingEncodedTextField, customHammingCorruptedTextField, customHammingDecodedTextField,
                customMdpcEncodedTextField, customMdpcCorruptedTextField, customMdpcDecodedTextField,
                customBest, customSumDistances);
    }

    /**
     * The main encodeCorruptDecode method.
     * Performs encoding, corrupting, and decoding of the {@link L1VariablesDeclaration#message} with the given parameters.
     * This method also writes the results to the respective table,
     * determines if there is a new 'best',
     * and adds the result to the 'sums'.
     *
     * @param h            the instance of the Hamming code to use
     * @param m            the instance of the MDPC code to use
     * @param runId        the id of the run
     * @param table        the table to use
     * @param hencodedTF   the {@link JTextField} to write the Hamming encoded string to
     * @param hcorruptedTF the {@link JTextField} to write the Hamming corrupted string to
     * @param hdecodedTF   the {@link JTextField} to write the Hamming decoded string to
     * @param mencodedTF   the {@link JTextField} to write the MDPC encoded string to
     * @param mcorruptedTF the {@link JTextField} to write the MDPC corrupted string to
     * @param mdecodedTF   the {@link JTextField} to write the MDPC decoded string to
     * @param bests        the array of best values to update
     * @param sums         the array of sums to update
     */
    private void encodeCorruptDecode(Hamming h, MDPC m, int runId, JTable table,
                                     JTextField hencodedTF, JTextField hcorruptedTF, JTextField hdecodedTF,
                                     JTextField mencodedTF, JTextField mcorruptedTF, JTextField mdecodedTF,
                                     int[] bests, int[] sums) {

        //<editor-fold desc="Apply Hamming">
        String encoded = encode(message, h);
        hencodedTF.setText(encoded);
        String corrupted = corrupt(encoded, errorProbability);
        hcorruptedTF.setText(corrupted);
        String decoded = decode(errorProbability > 0.5 ? invert(corrupted) : corrupted, h);
        hdecodedTF.setText(decoded);
        int hdist = distance(decoded, zeroExtend(message, h.getDataLength()));
        int hdistnp = distance(decoded.substring(decoded.length() - messageLength), message);
        //</editor-fold>

        //<editor-fold desc="Apply MDPC">
        int mdist = -1, mdistnp = -1;
        if (doMDPCCheckBox.isSelected()) {
            encoded = encode(message, m);
            mencodedTF.setText(encoded);
            corrupted = corrupt(encoded, errorProbability);
            mcorruptedTF.setText(corrupted);
            decoded = decode(errorProbability > 0.5 ? invert(corrupted) : corrupted, m);
            mdecodedTF.setText(decoded);
            mdist = distance(decoded, zeroExtend(message, m.getDataLength()));
            mdistnp = distance(decoded.substring(decoded.length() - messageLength), message);
        }
        //</editor-fold>

        //<editor-fold desc="Save results">
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{
                runId,
                hdist,  // (Hamming) distance
                hdistnp,  // (Hamming) distance No Padding
                mdist,  // (MDPC) distance
                mdistnp // (MDPC) distance No Padding
        });

        if (hdist < bests[HAMMING_DISTANCE])
            bests[HAMMING_DISTANCE] = hdist;
        if (hdistnp < bests[HAMMING_DISTANCE_NO_PADDING])
            bests[HAMMING_DISTANCE_NO_PADDING] = hdistnp;
        if (mdist < bests[MDPC_DISTANCE])
            bests[MDPC_DISTANCE] = mdist;
        if (mdistnp < bests[MDPC_DISTANCE_NO_PADDING])
            bests[MDPC_DISTANCE_NO_PADDING] = mdistnp;

        sums[HAMMING_DISTANCE] += hdist;
        sums[HAMMING_DISTANCE_NO_PADDING] += hdistnp;
        sums[MDPC_DISTANCE] += mdist;
        sums[MDPC_DISTANCE_NO_PADDING] += mdistnp;
        //</editor-fold>
    }

    private void writeSummary(JTable t, int[] sums, int[] bests) {
        DefaultTableModel model = (DefaultTableModel) t.getModel();
        model.addRow(new Object[]{
                "average",
                1.0 * sums[HAMMING_DISTANCE] / timesToRun,
                1.0 * sums[HAMMING_DISTANCE_NO_PADDING] / timesToRun,
                1.0 * sums[MDPC_DISTANCE] / timesToRun,
                1.0 * sums[MDPC_DISTANCE_NO_PADDING] / timesToRun,
        });

        model.addRow(new Object[]{
                "best distances",
                bests[HAMMING_DISTANCE],
                bests[HAMMING_DISTANCE_NO_PADDING],
                bests[MDPC_DISTANCE],
                bests[MDPC_DISTANCE_NO_PADDING],
        });
    }

    void writeMainAnalysis() {

        if (timesToRun < 2) return;
        for (int i = 1; i < 3; i++) {
            writeAnova(getSamplesFromIndex(i), hammingOnlyAnovaTable, i);
            writeAnova(getSamplesCombined(i), hammingMdpcAnovaTable, i);
        }

    }

    private void writeAnova(LinkedList<double[]> values, JTable anovaTable, int indexToWrite) {

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(8);  // up to 8 digits after the floating point

        anovaTable.setValueAt(timesToRun, 0, indexToWrite);  // Power
        OneWayAnova anova = new OneWayAnova();
        anovaTable.setValueAt((anova.anovaPValue(values)), 1, indexToWrite);
        anovaTable.setValueAt(df.format(anova.anovaFValue(values)), 2, indexToWrite);
        double significanceLevel;
        boolean significant = false;
        for (significanceLevel = 0.01; significanceLevel <= 0.2 && !significant; significanceLevel += 0.01)
            significant = anova.anovaTest(values, significanceLevel);
        significanceLevel -= 0.01;
        anovaTable.setValueAt(significant ? df.format(significanceLevel) : "Not Significant", 3, indexToWrite);

        double[] stdMean = getStandardDeviationAndMean(values);

        String confidenceInterval = "[" + df.format(stdMean[1] - 2 * stdMean[0]) + " ; " + df.format(stdMean[1] + 2 * stdMean[0]) + "]";
        anovaTable.setValueAt(confidenceInterval, 4, indexToWrite);

    }

    private LinkedList<double[]> getSamplesFromIndex(int index) {
        LinkedList<double[]> samples = new LinkedList<>();
        for (JTable longT : longTable) {
            double[] values = new double[timesToRun];
            for (int i = 0; i < values.length; i++)
                values[i] = (int) longT.getValueAt(i, index);
            samples.addLast(values);
        }

        return samples;
    }

    /**
     * Get the combined samples for Hamming and MDPC.
     *
     * @param hammingIndex the index of the column with hamming results
     *                     MDPC results are at this index +2
     * @return a {@link LinkedList} with two elements
     * one has all the hamming data
     * the other - all the MDPC data
     */
    private LinkedList<double[]> getSamplesCombined(int hammingIndex) {
        double[] hammingVals = new double[longTable.length * timesToRun];
        double[] mdpcVals = new double[longTable.length * timesToRun];
        for (int i = 0; i < longTable.length; i++) {
            JTable longT = longTable[i];
            for (int j = 0; j < timesToRun; j++) {
                hammingVals[i * timesToRun + j] = (int) longT.getValueAt(j, hammingIndex);
                mdpcVals[i * timesToRun + j] = (int) longT.getValueAt(j, hammingIndex + 2);
            }
        }

        LinkedList<double[]> samples = new LinkedList<>();
        samples.addLast(hammingVals);
        samples.addLast(mdpcVals);

        return samples;
    }

    private double[] getStandardDeviationAndMean(LinkedList<double[]> samples) {
        double[] joined = new double[samples.size() * timesToRun];
        for (int i = 0; i < samples.size(); i++) {
            double[] sample = samples.get(i);
            System.arraycopy(sample, 0, joined, i * timesToRun, timesToRun);
        }

        double stdDev = new StandardDeviation(false).evaluate(joined);
        double mean = new Mean().evaluate(joined);
        return new double[]{stdDev, mean};
    }

}
