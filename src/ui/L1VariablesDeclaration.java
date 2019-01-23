package ui;

import codes.Hamming;
import codes.MDPC;

import javax.swing.*;

/**
 * Lowest layer of the GUI.
 * Contains only variable declarations.
 *
 * @author 150009974
 * @version 2.2
 */
class L1VariablesDeclaration extends JFrame {

    //<editor-fold desc="Constants">
    static final int FIRST_R = 2;
    static final int MAJOR_PARSE_ERROR = -1;
    static final int MINOR_PARSE_ERROR = -2;
    static final boolean USE_USER_MESSAGE = true;

    static final int GAP = 20;
    static final int MAIN_COMPONENT_HEIGHT = 35;
    static final int MAIN_COMPONENT_WIDTH = 130;
    static final int ANOVA_STAT_COLUMN_WIDTH = 200;
    static final int ANOVA_DISTANCE_COLUMN_WIDTH = 230;
    static final int ANOVA_DISTANCE_WOPADDING_COLUMN_WIDTH = 270;

    static final int TAB_LABEL_WIDTH = 80;
    static final int TAB_TEXTFIELD_WIDTH = 550;
    static final int TAB_LABEL_HEIGHT = 40;
    static final int TAB_TEXTFIELD_HEIGHT = 40;

    static final int HAMMING_DISTANCE = 0;
    static final int HAMMING_DISTANCE_NO_PADDING = 1;
    static final int MDPC_DISTANCE = 2;
    static final int MDPC_DISTANCE_NO_PADDING = 3;
    //</editor-fold>

    //<editor-fold desc="Input derived fields">
    int maxR;
    int timesToRun;
    int numberOfTabs;
    double errorProbability;
    int messageLength;
    double probabilityOfOne;
    String message;
    //</editor-fold>

    JTabbedPane tabs;
    //<editor-fold desc="Components on the L5UserInteraction Tab">
    JPanel mainPanel;
    JLabel rMaxLabel;
    JTextField rMaxTextField;
    JLabel probabilityOfOneLabel;
    JTextField ratioTextField;
    JLabel errorProbabilityLabel;
    JTextField errorProbabilityTextField;
    JLabel messageLengthLabel;
    JTextField messageLengthTextField;
    JButton goButton;
    JLabel nRunsLabel;
    JTextField nRunsTextField;
    JCheckBox doMDPCCheckBox;
    JLabel messageLabel;
    JTextArea messageTextArea;
    JScrollPane messageTextAreaScrollPane;
    JLabel hammingOnlyAnovaLabel;
    JTable hammingOnlyAnovaTable;
    JScrollPane hammingOnlyAnovaTableScrollPane;
    JLabel hammingMdpcAnovaLabel;
    JTable hammingMdpcAnovaTable;
    JScrollPane hammingMdpcAnovaTableScrollPane;
    //</editor-fold>

    //<editor-fold desc="Arrays of components in tabs">
    JPanel[] rPanel;
    JLabel[] hammingLabel;
    JLabel[] mdpcLabel;
    JLabel[] encodedLabel;
    JTextField[] hammingEncodedTextField;
    JTextField[] mdpcEncodedTextField;
    JLabel[] corruptedLabel;
    JTextField[] hammingCorruptedTextField;
    JTextField[] mdpcCorruptedTextField;
    JLabel[] decodedLabel;
    JTextField[] hammingDecodedTextField;
    JTextField[] mdpcDecodedTextField;
    JLabel[] statisticsLabel;
    JTable[] shortTable;
    JScrollPane[] shortTableScrollPane;
    JTable[] longTable;
    JScrollPane[] longTableScrollPane;
    //</editor-fold>
    //<editor-fold desc="Arrays of statistics for tabs">
    Hamming[] hamming;
    MDPC[] mdpc;
    int[][] sumDistances;
    int[][] best;
    //</editor-fold>

    //<editor-fold desc="Components on the custom tab">
    JPanel customPanel;
    JLabel rValuesLabel;
    JTextField rValuesTextField;
    JLabel customHammingLabel;
    JLabel customMdpcLabel;
    JLabel customEncodedLabel;
    JTextField customHammingEncodedTextField;
    JTextField customMdpcEncodedTextField;
    JLabel customCorruptedLabel;
    JTextField customHammingCorruptedTextField;
    JTextField customMdpcCorruptedTextField;
    JLabel customDecodedLabel;
    JTextField customHammingDecodedTextField;
    JTextField customMdpcDecodedTextField;
    JLabel customStatisticsLabel;
    JTable customShortTable;
    JScrollPane customShortTableScrollPane;
    JTable customLongTable;
    JScrollPane customLongTableScrollPane;
    //</editor-fold>
    //<editor-fold desc="Statistics for custom tab">
    Hamming customHamming;
    MDPC customMdpc;
    int[] rValues;
    int[] customSumDistances;
    int[] customBest;
    //</editor-fold>

}
