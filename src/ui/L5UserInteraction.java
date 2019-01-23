package ui;

import javax.swing.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.awt.EventQueue.invokeLater;

/**
 * The top layer of the GUI.
 * Functions here add Listeners to the components and run the GUI.
 *
 * @author 150009974
 * @version 2.1
 */
public class L5UserInteraction extends L4CodePerformance {

    private MouseAdapter goButtonMouseAdapter =  //<editor-fold>
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() != MouseEvent.BUTTON1) return;

                    if (parseInputs() == MAJOR_PARSE_ERROR) return;
                    setUpCodesCustom();

                    runExperiments(!USE_USER_MESSAGE);
                }
            };
    //</editor-fold>

    private KeyAdapter nRunsKeyAdapter =  //<editor-fold>
            new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() != KeyEvent.VK_ENTER) return;

                    if (parseInputs() == MAJOR_PARSE_ERROR) return;

                    clearTabs();
                    runForEachTab(USE_USER_MESSAGE);
                    // does not run custom
                    writeMainAnalysis();
                }
            };
    //</editor-fold>

    private KeyAdapter messageKeyAdapter =  //<editor-fold>
            new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() != KeyEvent.VK_ENTER) return;

                    if (parseInputs() == MAJOR_PARSE_ERROR) return;
                    setUpCodesCustom();

                    runExperiments(USE_USER_MESSAGE);
                }
            };
    //</editor-fold>

    private KeyAdapter rValuesKeyAdapter = //<editor-fold>
            new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() != KeyEvent.VK_ENTER) return;

                    if (parseInputs() == MAJOR_PARSE_ERROR ||
                            parseInputs() == MINOR_PARSE_ERROR) return;

                    setUpCodesCustom();
                    runCustom(USE_USER_MESSAGE);
                }
            };
    //</editor-fold>

    private void addListeners() {
        goButton.addMouseListener(goButtonMouseAdapter);
        nRunsTextField.addKeyListener(nRunsKeyAdapter);
        messageTextArea.addKeyListener(messageKeyAdapter);
        rValuesTextField.addKeyListener(rValuesKeyAdapter);
    }

    private L5UserInteraction() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addMainTab();
        addCustomTab();
        addListeners();
        getContentPane().add(tabs);
        setTitle("P2 - Error Correction");
        pack();

        // The size must fit the right most and bottom most components.
        setSize(hammingOnlyAnovaTableScrollPane.getX() + hammingOnlyAnovaTableScrollPane.getWidth(),
                customLongTableScrollPane.getY() + customLongTableScrollPane.getHeight() + 4 * GAP);
        setLocationRelativeTo(null);  // centers the window
    }

    public static void main(String args[]) {
        // Set the Nimbus look and feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (info.getName().equals("Nimbus")) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (Exception ex) {
            System.err.println("Failed to set Nimbus look and feel. Elements may be misaligned.");
        }

        invokeLater(() -> new L5UserInteraction().setVisible(true));
    }
}
