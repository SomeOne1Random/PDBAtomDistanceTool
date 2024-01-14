import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.gui.jmol.StructureAlignmentJmol;
import org.biojava.nbio.structure.io.PDBFileReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PDBRenderer {
    private static StructureAlignmentJmol jmolPanel;
    private static JLabel selectedFileLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("PDB Renderer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // File Selection Panel
        JPanel fileSelectionPanel = new JPanel();
        JButton selectFileButton = new JButton("Select PDB File");
        selectedFileLabel = new JLabel("No file selected");
        selectFileButton.addActionListener(new FileSelectionAction());
        fileSelectionPanel.add(selectFileButton);
        fileSelectionPanel.add(selectedFileLabel);
        frame.add(fileSelectionPanel, BorderLayout.NORTH);

        // Jmol Panel
        jmolPanel = new StructureAlignmentJmol();
        frame.add(jmolPanel, BorderLayout.CENTER);

        // Task Buttons Panel
        JPanel taskButtonsPanel = new JPanel();
        JButton spinButton = new JButton("Toggle Spin");
        spinButton.addActionListener(e -> jmolPanel.evalString("spin on"));
        taskButtonsPanel.add(spinButton);
        frame.add(taskButtonsPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    private static class FileSelectionAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                selectedFileLabel.setText("Selected: " + selectedFile.getName());
                loadPDBFile(selectedFile);
            }
        }

        private void loadPDBFile(File file) {
            try {
                PDBFileReader pdbReader = new PDBFileReader();
                Structure structure = pdbReader.getStructure(file.getAbsolutePath());
                jmolPanel.setStructure(structure);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}