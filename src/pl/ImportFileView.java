package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import bl.IBLFacade;
import dto.Word;
public class ImportFileView extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton importButton;
    private JButton backButton;
    private IBLFacade iblFacade;
    private JLabel titleLabel, subtitleLabel;
    private JTable wordsTable;
    private DefaultTableModel tableModel;

    public ImportFileView(IBLFacade iblFacade) {
        this.iblFacade = iblFacade;
        setTitle("Dictionary Management System - Import");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(38, 166, 154));
        leftPanel.setLayout(new BorderLayout());

        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" + "<h1>Dictionary Import</h1>"
                + "<p>Import your dictionary data from text or CSV files.</p>" + "</div></html>");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        leftPanel.add(infoLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        titleLabel = new JLabel("Import Dictionary Data");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(titleLabel, gbc);

        subtitleLabel = new JLabel("Choose a file to import:");
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        rightPanel.add(subtitleLabel, gbc);

        importButton = new JButton("Import File");
        importButton.setPreferredSize(new Dimension(150, 40));
        importButton.setBackground(new Color(0, 150, 136));
        importButton.setForeground(Color.WHITE);
        importButton.setFocusPainted(false);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        rightPanel.add(importButton, gbc);

        String[] columnNames = { "Word", "Urdu Meaning", "Persian Meaning" };
        tableModel = new DefaultTableModel(columnNames, 0);
        wordsTable = new JTable(tableModel);

        wordsTable.setBackground(new Color(144, 238, 144));
        wordsTable.setForeground(Color.BLACK);

        wordsTable.getTableHeader().setBackground(new Color(0, 150, 136));
        wordsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(wordsTable);
        scrollPane.setPreferredSize(new Dimension(600, 300)); // Adjust table size
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rightPanel.add(scrollPane, gbc);

        // Add backButton at the bottom
        backButton = new JButton("Back to Dashboard");
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.setBackground(new Color(244, 67, 54));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.PAGE_END; // Align at the bottom
        rightPanel.add(backButton, gbc);

        importButton.addActionListener(e -> browseAndImportFile());
        backButton.addActionListener(e -> backToDashboard());

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void browseAndImportFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text and CSV Files", "txt", "csv");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            new Thread(() -> {
                try {
                    List<Word> importedWords = iblFacade.importFile(selectedFile);
                    if (importedWords == null || importedWords.isEmpty()) {
                        throw new Exception("Empty file");
                    }

                    SwingUtilities.invokeLater(() -> {
                        tableModel.setRowCount(0);
                        for (Word word : importedWords) {
                            tableModel.addRow(new Object[] { word.getWord(), word.getUrduMeaning(), word.getPersianMeaning() });
                        }

                        JOptionPane.showMessageDialog(ImportFileView.this, "Data imported successfully!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    });

                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(ImportFileView.this, "Failed to import data: " + e.getMessage(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        }
    }

    private void backToDashboard() {
        dispose();
        new MainDashboardView(iblFacade);
    }
}
