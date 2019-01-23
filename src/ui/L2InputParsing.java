package ui;

import javax.swing.*;

/**
 * Second layer of the GUI.
 * Defines input parsing functions.
 * These functions read the user inputs from the different fields and
 * do any necessary transformations.
 *
 * @author 150009974
 * @version 2.0
 */
class L2InputParsing extends L1VariablesDeclaration {

    private double parseFraction(String input) {
        input = input.trim();
        String[] parts = input.split(":");
        if (parts.length != 2) parts = input.split("/");
        if (parts.length != 2) throw new NumberFormatException();

        double numerator = Double.parseDouble(parts[0]);
        double denominator = Double.parseDouble(parts[1]);
        return numerator / denominator;
    }

    private void showErrorMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg,
                "Input parse error.", JOptionPane.ERROR_MESSAGE);
    }

    int parseInputs() {
        //<editor-fold desc="Max r">
        try {
            maxR = Integer.parseInt(rMaxTextField.getText().trim());
            if (maxR < FIRST_R) throw new NumberFormatException();
            numberOfTabs = maxR - FIRST_R + 1;
        }
        catch (NumberFormatException nfe) {
            showErrorMsg("Could not parse max r as an integer greater than 1.");
            return MAJOR_PARSE_ERROR;
        }
        //</editor-fold>

        //<editor-fold desc="Probability of 1">
        try {
            probabilityOfOne = Double.parseDouble(ratioTextField.getText().trim());
            if (probabilityOfOne < 0 || probabilityOfOne > 1)
                throw new NumberFormatException();
        }
        catch (NumberFormatException nfe) {
            try {
                probabilityOfOne = parseFraction(ratioTextField.getText());
            }
            catch (NumberFormatException nfe2) {
                showErrorMsg("Could not parse error ratio as a non-negative floating point number less than 1 or equal.");
                return MAJOR_PARSE_ERROR;
            }
        }
        //</editor-fold>

        //<editor-fold desc="Error Probability">
        try {
            errorProbability = Double.parseDouble(errorProbabilityTextField.getText().trim());
            if (errorProbability >= 1)
                errorProbability /= 100.0;  //assume percent
            if (errorProbability < 0 || errorProbability > 1)
                throw new NumberFormatException();
        }
        catch (NumberFormatException nfe) {
            try {
                errorProbability = parseFraction(errorProbabilityTextField.getText());
            }
            catch (NumberFormatException nfe2) {
                showErrorMsg("Could not parse error probability as a non-negative floating point number less than 1 or equal.");
                return MAJOR_PARSE_ERROR;
            }
        }
        //</editor-fold>

        //<editor-fold desc="Message Length">
        try {
            messageLength = Integer.parseInt(messageLengthTextField.getText().trim());
            if (messageLength < 1) throw new NumberFormatException();
        }
        catch (NumberFormatException nfe) {
            showErrorMsg("Could not parse message length as a natural number.");
            return MAJOR_PARSE_ERROR;
        }
        //</editor-fold>

        //<editor-fold desc="Times to run">
        try {
            timesToRun = Integer.parseInt(nRunsTextField.getText());
            if (timesToRun < 1) throw new NumberFormatException();
        }
        catch (NumberFormatException nfe) {
            showErrorMsg("Could not parse number of runs as a natural number.");
            return MAJOR_PARSE_ERROR;
        }
        //</editor-fold>

        return parseRValues();
    }

    private int parseRValues() {
        String input = rValuesTextField.getText();
        String[] dims = input.split(",");
        rValues = new int[dims.length];
        for (int i = 0; i < dims.length; i++) {
            try {
                rValues[i] = Integer.parseInt(dims[i].trim());
            }
            catch (NumberFormatException nfe) {
                rValues = new int[0];
                return MINOR_PARSE_ERROR;
            }
        }

        return 0;
    }

}
