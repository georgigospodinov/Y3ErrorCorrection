package ui;

import codes.Hamming;
import codes.MDPC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * The functions here create tabs in the GUI
 * and fill them with components.
 *
 * @author 150009974
 * @version 2.1
 */
class L3ComponentCreation extends L2InputParsing {

    void addMainTab() {

        createMainComponents();

        addMainComponents();

        tabs = new JTabbedPane();
        tabs.addTab("Main", mainPanel);
    }

    private void createMainComponents() {
        rMaxLabel = new JLabel("Max r:");
        rMaxLabel.setBounds(  // top left corner of window
                0, 0,
                MAIN_COMPONENT_WIDTH, MAIN_COMPONENT_HEIGHT);

        rMaxTextField = new JTextField("6");
        rMaxTextField.setBounds(  // right of rMaxLabel
                rMaxLabel.getX() + rMaxLabel.getWidth(), rMaxLabel.getY(),
                MAIN_COMPONENT_WIDTH, MAIN_COMPONENT_HEIGHT);

        probabilityOfOneLabel = new JLabel("Probability of 1:");
        probabilityOfOneLabel.setBounds(  // below rMaxLabel
                rMaxLabel.getX(), rMaxLabel.getY() + rMaxLabel.getHeight(),
                MAIN_COMPONENT_WIDTH, MAIN_COMPONENT_HEIGHT);

        ratioTextField = new JTextField("0.5");
        ratioTextField.setBounds(  // right of probabilityOfOneLabel
                probabilityOfOneLabel.getX() + probabilityOfOneLabel.getWidth(), probabilityOfOneLabel.getY(),
                MAIN_COMPONENT_WIDTH, MAIN_COMPONENT_HEIGHT);

        errorProbabilityLabel = new JLabel("Error probability:");
        errorProbabilityLabel.setBounds(  // right of rMaxTextField
                rMaxTextField.getX() + rMaxTextField.getWidth() + GAP, rMaxTextField.getY(),
                MAIN_COMPONENT_WIDTH, MAIN_COMPONENT_HEIGHT);

        errorProbabilityTextField = new JTextField("0.05");
        errorProbabilityTextField.setBounds(  // right of errorProbabilityLabel
                errorProbabilityLabel.getX() + errorProbabilityLabel.getWidth(), errorProbabilityLabel.getY(),
                MAIN_COMPONENT_WIDTH, MAIN_COMPONENT_HEIGHT);

        messageLengthLabel = new JLabel("Message Length:");
        messageLengthLabel.setBounds(  // below errorProbabilityLabel
                errorProbabilityLabel.getX(), errorProbabilityLabel.getY() + errorProbabilityLabel.getHeight(),
                MAIN_COMPONENT_WIDTH, MAIN_COMPONENT_HEIGHT);

        messageLengthTextField = new JTextField("500");
        messageLengthTextField.setBounds(  // right of messageLengthLabel
                messageLengthLabel.getX() + messageLengthLabel.getWidth(), messageLengthLabel.getY(),
                MAIN_COMPONENT_WIDTH, MAIN_COMPONENT_HEIGHT);

        goButton = new JButton("GO");
        goButton.setBounds(  // below probabilityOfOneLabel
                probabilityOfOneLabel.getX(), probabilityOfOneLabel.getY() + probabilityOfOneLabel.getHeight() + GAP,
                50, 50);

        nRunsLabel = new JLabel("Number of runs to perform:");
        nRunsLabel.setBounds(  // right of goButton
                goButton.getX() + goButton.getWidth(), goButton.getY(),
                2 * MAIN_COMPONENT_WIDTH, MAIN_COMPONENT_HEIGHT);

        nRunsTextField = new JTextField("100");
        nRunsTextField.setBounds(  // right of nRunsLabel
                nRunsLabel.getX() + nRunsLabel.getWidth(), nRunsLabel.getY(),
                MAIN_COMPONENT_WIDTH, MAIN_COMPONENT_HEIGHT);

        doMDPCCheckBox = new JCheckBox("Do MDPC?");
        doMDPCCheckBox.setBounds(  // below goButton
                goButton.getX(), goButton.getY() + goButton.getHeight(),
                MAIN_COMPONENT_WIDTH, MAIN_COMPONENT_HEIGHT);
        doMDPCCheckBox.setSelected(true);

        messageLabel = new JLabel("Message:");
        messageLabel.setBounds(  // below doMDPCCheckBox
                doMDPCCheckBox.getX(), doMDPCCheckBox.getY() + doMDPCCheckBox.getHeight(),
                MAIN_COMPONENT_WIDTH, MAIN_COMPONENT_HEIGHT);

        messageTextArea = new JTextArea("110101", 5, 20);
        messageTextArea.setLineWrap(true);
        messageTextAreaScrollPane = new JScrollPane(messageTextArea);
        int errorProbabilityEnd = errorProbabilityTextField.getX() + errorProbabilityTextField.getWidth();
        messageTextAreaScrollPane.setBounds(  // below messageLabel
                messageLabel.getX(), messageLabel.getY() + messageLabel.getHeight(),
                errorProbabilityEnd - messageLabel.getX(),  // up to the end of error probability
                500);

        hammingOnlyAnovaLabel = new JLabel("ANOVA results between Hamming Encodings.");
        hammingOnlyAnovaLabel.setBounds(  // right of errorProbabilityTextField
                errorProbabilityEnd + 3 * GAP, errorProbabilityTextField.getY(),
                3 * MAIN_COMPONENT_WIDTH, MAIN_COMPONENT_HEIGHT);

        hammingOnlyAnovaTable =  //<editor-fold desc="Table model">
                new JTable(new DefaultTableModel(
                        new Object[][]{
                                new Object[]{"Power"},
                                new Object[]{"p-value"},
                                new Object[]{"f-value"},
                                new Object[]{"Significance level"},
                                new Object[]{"Confidence Interval"}
                        },
                        new String[]{
                                "Hamming Only",
                                "Distance from error",
                                "Distance from error w/o padding"
                        }
                ) {
                    Class[] types = new Class[]{String.class, Object.class, Object.class};

                    public Class getColumnClass(int columnIndex) {
                        return types[columnIndex];
                    }
                });
        hammingOnlyAnovaTable.getColumnModel().getColumn(0).setPreferredWidth(ANOVA_STAT_COLUMN_WIDTH);
        hammingOnlyAnovaTable.getColumnModel().getColumn(0).setMaxWidth(ANOVA_STAT_COLUMN_WIDTH);
        hammingOnlyAnovaTable.getColumnModel().getColumn(1).setPreferredWidth(ANOVA_DISTANCE_COLUMN_WIDTH);
        hammingOnlyAnovaTable.getColumnModel().getColumn(1).setMaxWidth(ANOVA_DISTANCE_COLUMN_WIDTH);
        hammingOnlyAnovaTable.getColumnModel().getColumn(2).setPreferredWidth(ANOVA_DISTANCE_WOPADDING_COLUMN_WIDTH);
        hammingOnlyAnovaTable.getColumnModel().getColumn(2).setMaxWidth(ANOVA_DISTANCE_WOPADDING_COLUMN_WIDTH);

        //</editor-fold>
        hammingOnlyAnovaTable.setColumnSelectionAllowed(true);
        hammingOnlyAnovaTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        hammingOnlyAnovaTableScrollPane = new JScrollPane(hammingOnlyAnovaTable);
        hammingOnlyAnovaTableScrollPane.setBounds(  // below hammingOnlyAnovaLabel
                hammingOnlyAnovaLabel.getX(), hammingOnlyAnovaLabel.getY() + hammingOnlyAnovaLabel.getHeight(),
                ANOVA_STAT_COLUMN_WIDTH + 2 * ANOVA_DISTANCE_COLUMN_WIDTH,
                (hammingOnlyAnovaTable.getRowCount() + 2) * hammingOnlyAnovaTable.getRowHeight());

        hammingMdpcAnovaLabel = new JLabel("ANOVA results between Hamming and MDPC.");
        hammingMdpcAnovaLabel.setBounds(  // below hammingOnlyAnovaTableScrollPane
                hammingOnlyAnovaTableScrollPane.getX(),
                hammingOnlyAnovaTableScrollPane.getY() + GAP + hammingOnlyAnovaTableScrollPane.getHeight(),
                3 * MAIN_COMPONENT_WIDTH, MAIN_COMPONENT_HEIGHT);

        hammingMdpcAnovaTable = //<editor-fold desc="Table model">
                new JTable(new DefaultTableModel(
                        new Object[][]{
                                new Object[]{"Power"},
                                new Object[]{"p-value"},
                                new Object[]{"f-value"},
                                new Object[]{"Significance level"},
                                new Object[]{"Confidence Interval"}
                        },
                        new String[]{
                                "Hamming Only",
                                "Distance from error",
                                "Distance from error w/o padding"
                        }
                ) {
                    Class[] types = new Class[]{String.class, Object.class, Object.class};

                    public Class getColumnClass(int columnIndex) {
                        return types[columnIndex];
                    }
                });
        hammingMdpcAnovaTable.getColumnModel().getColumn(0).setPreferredWidth(ANOVA_STAT_COLUMN_WIDTH);
        hammingMdpcAnovaTable.getColumnModel().getColumn(0).setMaxWidth(ANOVA_STAT_COLUMN_WIDTH);
        hammingMdpcAnovaTable.getColumnModel().getColumn(1).setPreferredWidth(ANOVA_DISTANCE_COLUMN_WIDTH);
        hammingMdpcAnovaTable.getColumnModel().getColumn(1).setMaxWidth(ANOVA_DISTANCE_COLUMN_WIDTH);
        hammingMdpcAnovaTable.getColumnModel().getColumn(2).setPreferredWidth(ANOVA_DISTANCE_WOPADDING_COLUMN_WIDTH);
        hammingMdpcAnovaTable.getColumnModel().getColumn(2).setMaxWidth(ANOVA_DISTANCE_WOPADDING_COLUMN_WIDTH);
        //</editor-fold>
        hammingMdpcAnovaTable.setColumnSelectionAllowed(true);
        hammingMdpcAnovaTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        hammingMdpcAnovaTableScrollPane = new JScrollPane(hammingMdpcAnovaTable);
        hammingMdpcAnovaTableScrollPane.setBounds(  // below hammingMdpcAnovaLabel
                hammingMdpcAnovaLabel.getX(), hammingMdpcAnovaLabel.getY() + hammingMdpcAnovaLabel.getHeight(),
                ANOVA_STAT_COLUMN_WIDTH + 2 * ANOVA_DISTANCE_COLUMN_WIDTH,
                (hammingMdpcAnovaTable.getRowCount() + 2) * hammingMdpcAnovaTable.getRowHeight());
    }

    private void addMainComponents() {
        mainPanel = new JPanel(null);
        mainPanel.add(rMaxLabel);
        mainPanel.add(rMaxTextField);
        mainPanel.add(probabilityOfOneLabel);
        mainPanel.add(ratioTextField);
        mainPanel.add(errorProbabilityLabel);
        mainPanel.add(errorProbabilityTextField);
        mainPanel.add(messageLengthLabel);
        mainPanel.add(messageLengthTextField);
        mainPanel.add(goButton);
        mainPanel.add(nRunsLabel);
        mainPanel.add(nRunsTextField);
        mainPanel.add(doMDPCCheckBox);
        mainPanel.add(messageLabel);
        mainPanel.add(messageTextAreaScrollPane);
        mainPanel.add(hammingOnlyAnovaLabel);
        mainPanel.add(hammingOnlyAnovaTableScrollPane);
        mainPanel.add(hammingMdpcAnovaLabel);
        mainPanel.add(hammingMdpcAnovaTableScrollPane);
    }

    void addCustomTab() {

        createCustomComponents();

        addCustomComponents();

        tabs.addTab("Custom r", customPanel);

        setUpCodesCustom();
        prepareCountersCustom();

    }

    private void createCustomComponents() {
        rValuesLabel = new JLabel("    r = ");
        rValuesLabel.setBounds(  // top left
                0, 0,
                TAB_LABEL_WIDTH, TAB_LABEL_HEIGHT);

        rValuesTextField = new JTextField("3,4,5,6");
        rValuesTextField.setBounds(  // right of rValuesLabel
                rValuesLabel.getX() + rValuesLabel.getWidth(), rValuesLabel.getY(),
                TAB_TEXTFIELD_WIDTH, TAB_TEXTFIELD_HEIGHT);

        customEncodedLabel = new JLabel("Encoded:");
        customEncodedLabel.setBounds(  // below rValuesLabel
                rValuesLabel.getX(), rValuesLabel.getY() + rValuesLabel.getHeight() + TAB_LABEL_HEIGHT,
                TAB_LABEL_WIDTH, TAB_LABEL_HEIGHT);

        customCorruptedLabel = new JLabel("Corrupted");
        customCorruptedLabel.setBounds(  // below customEncodedLabel
                customEncodedLabel.getX(), customEncodedLabel.getY() + customEncodedLabel.getHeight(),
                TAB_LABEL_WIDTH, TAB_LABEL_HEIGHT);

        customDecodedLabel = new JLabel("Decoded");
        customDecodedLabel.setBounds(  // below customCorruptedLabel
                customCorruptedLabel.getX(), customCorruptedLabel.getY() + customCorruptedLabel.getHeight(),
                TAB_LABEL_WIDTH, TAB_LABEL_HEIGHT);

        customStatisticsLabel = new JLabel("Statistics (weight of difference, information rate (take padding into account)):");
        customStatisticsLabel.setBounds(  // below customDecodedLabel
                customDecodedLabel.getX(), customDecodedLabel.getY() + customDecodedLabel.getHeight(),
                7 * TAB_LABEL_WIDTH, TAB_LABEL_HEIGHT);

        customHammingEncodedTextField = new JTextField();
        customHammingEncodedTextField.setBounds(  // right of customEncodedLabel
                customEncodedLabel.getX() + customEncodedLabel.getWidth(), customEncodedLabel.getY(),
                TAB_TEXTFIELD_WIDTH, TAB_TEXTFIELD_HEIGHT);

        customHammingCorruptedTextField = new JTextField();
        customHammingCorruptedTextField.setBounds(  // right of customCorruptedLabel
                customCorruptedLabel.getX() + customCorruptedLabel.getWidth(), customCorruptedLabel.getY(),
                TAB_TEXTFIELD_WIDTH, TAB_TEXTFIELD_HEIGHT);

        customHammingDecodedTextField = new JTextField();
        customHammingDecodedTextField.setBounds(  // right of customDecodedLabel
                customDecodedLabel.getX() + customDecodedLabel.getWidth(), customDecodedLabel.getY(),
                TAB_TEXTFIELD_WIDTH, TAB_TEXTFIELD_HEIGHT);

        customHammingLabel = new JLabel("Hamming:");
        customHammingLabel.setBounds(  // above middle of customHammingEncodedTextField
                customHammingEncodedTextField.getX() + customHammingEncodedTextField.getWidth() / 2 - TAB_LABEL_WIDTH / 2,
                customHammingEncodedTextField.getY() - TAB_LABEL_HEIGHT,
                TAB_LABEL_WIDTH, TAB_LABEL_HEIGHT);

        customMdpcEncodedTextField = new JTextField();
        customMdpcEncodedTextField.setBounds(  // right of customHammingEncodedTextField
                customHammingEncodedTextField.getX() + customHammingEncodedTextField.getWidth() + GAP, customHammingEncodedTextField.getY(),
                TAB_TEXTFIELD_WIDTH, TAB_TEXTFIELD_HEIGHT);

        customMdpcCorruptedTextField = new JTextField();
        customMdpcCorruptedTextField.setBounds(  // right of customHammingCorruptedTextField
                customHammingCorruptedTextField.getX() + customHammingCorruptedTextField.getWidth() + GAP, customHammingCorruptedTextField.getY(),
                TAB_TEXTFIELD_WIDTH, TAB_TEXTFIELD_HEIGHT);

        customMdpcDecodedTextField = new JTextField();
        customMdpcDecodedTextField.setBounds(  // right of the customHammingDecodedTextField
                customHammingDecodedTextField.getX() + customHammingDecodedTextField.getWidth() + GAP, customHammingDecodedTextField.getY(),
                TAB_TEXTFIELD_WIDTH, TAB_TEXTFIELD_HEIGHT);

        customMdpcLabel = new JLabel("Multi-Dimensional Parity Check:");
        customMdpcLabel.setBounds(  // above middle of customMdpcEncodedTextField
                customMdpcEncodedTextField.getX() + customMdpcEncodedTextField.getWidth() / 2 - TAB_LABEL_WIDTH / 2,
                customMdpcEncodedTextField.getY() - TAB_LABEL_HEIGHT,
                3 * TAB_LABEL_WIDTH, TAB_LABEL_HEIGHT);

        customShortTable =  //<editor-fold desc="Table design">
                new JTable(new DefaultTableModel(
                        new Object[][]{},
                        new String[]{  // Headers
                                "Code", "Number of data bits", "Code length", "Information rate", "Chance of error in data bits"
                        }
                ) {
                    Class[] types = new Class[]{
                            Integer.class, Integer.class, Integer.class, Double.class, Double.class
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types[columnIndex];
                    }
                });
        //</editor-fold>
        customShortTable.setColumnSelectionAllowed(true);
        customShortTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        customShortTableScrollPane = new JScrollPane(customShortTable);
        customShortTableScrollPane.setBounds(  // below customStatisticsLabel
                customStatisticsLabel.getX(), customStatisticsLabel.getY() + customStatisticsLabel.getHeight(),
                hammingOnlyAnovaTableScrollPane.getX() + hammingOnlyAnovaTableScrollPane.getWidth(), 70);

        customLongTable =  //<editor-fold desc="Table model">
                new JTable(new DefaultTableModel(
                        new Object[][]{},
                        new String[]{
                                "Run ID", "(Hamming) distance", "(Hamming) distance w/o padding", "(MDPC) distance", "(MDPC) distance w/o padding"
                        }
                ) {
                    Class[] types = new Class[]{
                            String.class, Integer.class, Integer.class, Integer.class, Integer.class
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types[columnIndex];
                    }
                });
        //</editor-fold>
        customLongTable.setColumnSelectionAllowed(true);
        customLongTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        customLongTableScrollPane = new JScrollPane(customLongTable);
        customLongTableScrollPane.setBounds(  // below customShortTableScrollPane
                customShortTableScrollPane.getX(), customShortTableScrollPane.getY() + customShortTableScrollPane.getHeight(),
                hammingOnlyAnovaTableScrollPane.getX() + hammingOnlyAnovaTableScrollPane.getWidth(), 400);
    }

    private void addCustomComponents() {
        customPanel = new JPanel(null);
        customPanel.add(customEncodedLabel);
        customPanel.add(customCorruptedLabel);
        customPanel.add(customDecodedLabel);
        customPanel.add(customStatisticsLabel);
        customPanel.add(customHammingLabel);
        customPanel.add(customHammingEncodedTextField);
        customPanel.add(customHammingDecodedTextField);
        customPanel.add(customHammingCorruptedTextField);
        customPanel.add(customMdpcLabel);
        customPanel.add(customMdpcEncodedTextField);
        customPanel.add(customMdpcCorruptedTextField);
        customPanel.add(customMdpcDecodedTextField);
        customPanel.add(customShortTableScrollPane);
        customPanel.add(customLongTableScrollPane);
        customPanel.add(rValuesLabel);
        customPanel.add(rValuesTextField);
    }

    void setUpCodesCustom() {

        if (rValues == null) return;
        customHamming = new Hamming(rValues.length);
        customMdpc = new MDPC(rValues);
    }

    void prepareCountersCustom() {
        customBest = new int[MDPC_DISTANCE_NO_PADDING + 1];
        customBest[HAMMING_DISTANCE] = Integer.MAX_VALUE;
        customBest[HAMMING_DISTANCE_NO_PADDING] = Integer.MAX_VALUE;
        customBest[MDPC_DISTANCE] = Integer.MAX_VALUE;
        customBest[MDPC_DISTANCE_NO_PADDING] = Integer.MAX_VALUE;

        customSumDistances = new int[MDPC_DISTANCE_NO_PADDING + 1];
        customSumDistances[HAMMING_DISTANCE] = 0;
        customSumDistances[HAMMING_DISTANCE_NO_PADDING] = 0;
        customSumDistances[MDPC_DISTANCE] = 0;
        customSumDistances[MDPC_DISTANCE_NO_PADDING] = 0;
    }

    void clearTabs() {
        tabs.removeAll();
        tabs.addTab("Main", mainPanel);
        tabs.addTab("Custom r", customPanel);

        encodedLabel = new JLabel[numberOfTabs];
        corruptedLabel = new JLabel[numberOfTabs];
        decodedLabel = new JLabel[numberOfTabs];
        statisticsLabel = new JLabel[numberOfTabs];

        hammingLabel = new JLabel[numberOfTabs];
        hammingEncodedTextField = new JTextField[numberOfTabs];
        hammingCorruptedTextField = new JTextField[numberOfTabs];
        hammingDecodedTextField = new JTextField[numberOfTabs];

        mdpcLabel = new JLabel[numberOfTabs];
        mdpcEncodedTextField = new JTextField[numberOfTabs];
        mdpcCorruptedTextField = new JTextField[numberOfTabs];
        mdpcDecodedTextField = new JTextField[numberOfTabs];

        shortTable = new JTable[numberOfTabs];
        shortTableScrollPane = new JScrollPane[numberOfTabs];
        longTable = new JTable[numberOfTabs];
        longTableScrollPane = new JScrollPane[numberOfTabs];

        rPanel = new JPanel[numberOfTabs];

        hamming = new Hamming[numberOfTabs];
        mdpc = new MDPC[numberOfTabs];
        sumDistances = new int[numberOfTabs][MDPC_DISTANCE_NO_PADDING + 1];
        best = new int[numberOfTabs][MDPC_DISTANCE_NO_PADDING + 1];

    }

    void addElementsToTab(int index) {

        createComponents(index);

        addComponentsToPanel(index);

        tabs.addTab("r = " + (index + FIRST_R), rPanel[index]);

        setUpCodes(index);
        prepareCounters(index);

    }

    private void createComponents(int index) {
        encodedLabel[index] = new JLabel("Encoded");
        encodedLabel[index].setBounds(  // top left, leaving an empty line
                0, TAB_LABEL_HEIGHT,
                TAB_LABEL_WIDTH, TAB_LABEL_HEIGHT);

        corruptedLabel[index] = new JLabel("Corrupted");
        corruptedLabel[index].setBounds(  // below encodedLabel
                encodedLabel[index].getX(), encodedLabel[index].getY() + encodedLabel[index].getHeight(),
                TAB_LABEL_WIDTH, TAB_LABEL_HEIGHT);

        decodedLabel[index] = new JLabel("Decoded");
        decodedLabel[index].setBounds(  // below corruptedLabel
                corruptedLabel[index].getX(), corruptedLabel[index].getY() + corruptedLabel[index].getHeight(),
                TAB_LABEL_WIDTH, TAB_LABEL_HEIGHT);

        statisticsLabel[index] = new JLabel("Statistics (weight of difference, information rate (take padding into account)):");
        statisticsLabel[index].setBounds(  // below decodedLabel
                decodedLabel[index].getX(), decodedLabel[index].getY() + decodedLabel[index].getHeight(),
                7 * TAB_LABEL_WIDTH, TAB_LABEL_HEIGHT);

        hammingEncodedTextField[index] = new JTextField();
        hammingEncodedTextField[index].setBounds(  // right of encodedLabel
                encodedLabel[index].getX() + encodedLabel[index].getWidth(), encodedLabel[index].getY(),
                TAB_TEXTFIELD_WIDTH, TAB_TEXTFIELD_HEIGHT);

        hammingCorruptedTextField[index] = new JTextField();
        hammingCorruptedTextField[index].setBounds(  // right of corruptedLabel
                corruptedLabel[index].getX() + corruptedLabel[index].getWidth(), corruptedLabel[index].getY(),
                TAB_TEXTFIELD_WIDTH, TAB_TEXTFIELD_HEIGHT);

        hammingDecodedTextField[index] = new JTextField();
        hammingDecodedTextField[index].setBounds(  // right of decodedLabel
                decodedLabel[index].getX() + decodedLabel[index].getWidth(), decodedLabel[index].getY(),
                TAB_TEXTFIELD_WIDTH, TAB_TEXTFIELD_HEIGHT);

        hammingLabel[index] = new JLabel("Hamming:");
        hammingLabel[index].setBounds(  // above middle of hammingEncodedTextField
                hammingEncodedTextField[index].getX() + hammingEncodedTextField[index].getWidth() / 2 - TAB_LABEL_WIDTH / 2,
                hammingEncodedTextField[index].getY() - TAB_LABEL_HEIGHT,
                TAB_LABEL_WIDTH, TAB_LABEL_HEIGHT);

        mdpcEncodedTextField[index] = new JTextField();
        mdpcEncodedTextField[index].setBounds(  // right of hammingEncodedTextField
                hammingEncodedTextField[index].getX() + hammingEncodedTextField[index].getWidth() + GAP, hammingEncodedTextField[index].getY(),
                TAB_TEXTFIELD_WIDTH, TAB_TEXTFIELD_HEIGHT);

        mdpcCorruptedTextField[index] = new JTextField();
        mdpcCorruptedTextField[index].setBounds(  // right of hammingCorruptedTextField
                hammingCorruptedTextField[index].getX() + hammingCorruptedTextField[index].getWidth() + GAP, hammingCorruptedTextField[index].getY(),
                TAB_TEXTFIELD_WIDTH, TAB_TEXTFIELD_HEIGHT);

        mdpcDecodedTextField[index] = new JTextField();
        mdpcDecodedTextField[index].setBounds(  // right of the hammingDecodedTextField
                hammingDecodedTextField[index].getX() + hammingDecodedTextField[index].getWidth() + GAP, hammingDecodedTextField[index].getY(),
                TAB_TEXTFIELD_WIDTH, TAB_TEXTFIELD_HEIGHT);

        mdpcLabel[index] = new JLabel("Multi-Dimensional Parity Check:");
        mdpcLabel[index].setBounds(  // above middle of mdpcEncodedTextField
                mdpcEncodedTextField[index].getX() + mdpcEncodedTextField[index].getWidth() / 2 - TAB_LABEL_WIDTH / 2,
                mdpcEncodedTextField[index].getY() - TAB_LABEL_HEIGHT,
                3 * TAB_LABEL_WIDTH, TAB_LABEL_HEIGHT);

        shortTable[index] =  //<editor-fold desc="Table design">
                new JTable(new DefaultTableModel(
                        new Object[][]{},
                        new String[]{  // Headers
                                "Code", "Number of data bits", "Code length", "Information rate", "Chance of error in data bits"
                        }
                ) {
                    Class[] types = new Class[]{
                            Integer.class, Integer.class, Integer.class, Double.class, Double.class
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types[columnIndex];
                    }
                });
        //</editor-fold>
        shortTable[index].setColumnSelectionAllowed(true);
        shortTable[index].setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        shortTableScrollPane[index] = new JScrollPane(shortTable[index]);
        shortTableScrollPane[index].setBounds(  // below statisticsLabel
                statisticsLabel[index].getX(), statisticsLabel[index].getY() + statisticsLabel[index].getHeight(),
                hammingOnlyAnovaTableScrollPane.getX() + hammingOnlyAnovaTableScrollPane.getWidth(), 70);

        longTable[index] =  //<editor-fold desc="Table model">
                new JTable(new DefaultTableModel(
                        new Object[][]{},
                        new String[]{
                                "Run ID", "(Hamming) distance", "(Hamming) distance w/o padding", "(MDPC) distance", "(MDPC) distance w/o padding"
                        }
                ) {
                    Class[] types = new Class[]{
                            String.class, Integer.class, Integer.class, Integer.class, Integer.class
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types[columnIndex];
                    }
                });
        //</editor-fold>
        longTable[index].setColumnSelectionAllowed(true);
        longTable[index].setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        longTableScrollPane[index] = new JScrollPane(longTable[index]);
        longTableScrollPane[index].setBounds(  // below shortTableScrollPane
                shortTableScrollPane[index].getX(), shortTableScrollPane[index].getY() + shortTableScrollPane[index].getHeight(),
                hammingOnlyAnovaTableScrollPane.getX() + hammingOnlyAnovaTableScrollPane.getWidth(), 400);
    }

    private void addComponentsToPanel(int index) {
        rPanel[index] = new JPanel(null);
        rPanel[index].add(encodedLabel[index]);
        rPanel[index].add(corruptedLabel[index]);
        rPanel[index].add(decodedLabel[index]);
        rPanel[index].add(statisticsLabel[index]);
        rPanel[index].add(hammingLabel[index]);
        rPanel[index].add(hammingEncodedTextField[index]);
        rPanel[index].add(hammingDecodedTextField[index]);
        rPanel[index].add(hammingCorruptedTextField[index]);
        rPanel[index].add(mdpcLabel[index]);
        rPanel[index].add(mdpcEncodedTextField[index]);
        rPanel[index].add(mdpcCorruptedTextField[index]);
        rPanel[index].add(mdpcDecodedTextField[index]);
        rPanel[index].add(shortTableScrollPane[index]);
        rPanel[index].add(longTableScrollPane[index]);
    }

    private void setUpCodes(int index) {
        hamming[index] = new Hamming(index + FIRST_R);

        int[] dimensions = new int[index + 1];
        for (int i = 0; i < dimensions.length; i++) dimensions[i] = FIRST_R + i;
        mdpc[index] = new MDPC(dimensions);
    }

    private void prepareCounters(int index) {
        best[index][HAMMING_DISTANCE] = Integer.MAX_VALUE;
        best[index][HAMMING_DISTANCE_NO_PADDING] = Integer.MAX_VALUE;
        best[index][MDPC_DISTANCE] = Integer.MAX_VALUE;
        best[index][MDPC_DISTANCE_NO_PADDING] = Integer.MAX_VALUE;

        sumDistances[index][HAMMING_DISTANCE] = 0;
        sumDistances[index][HAMMING_DISTANCE_NO_PADDING] = 0;
        sumDistances[index][MDPC_DISTANCE] = 0;
        sumDistances[index][MDPC_DISTANCE_NO_PADDING] = 0;
    }
}
