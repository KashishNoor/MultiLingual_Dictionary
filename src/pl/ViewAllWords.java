package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import bl.IBLFacade;
import dto.Word;

public class ViewAllWords extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable wordsTable;
    private JButton backButton;
    private JComboBox<String> filterComboBox;
    private IBLFacade iblFacade;
    private DefaultTableModel tableModel;

    public ViewAllWords(IBLFacade iblFacade) {
        this.iblFacade = iblFacade;
        setTitle("View All Words");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(38, 166, 154));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" + "<h1>All Words</h1>"
                + "<p>Here you can view all words in the dictionary.</p>" + "</div></html>");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        leftPanel.add(infoLabel);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 10);

        JLabel titleLabel = new JLabel("All Words in Dictionary");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(titleLabel, gbc);

        // Add ComboBox for Filter
        String[] filters = { "All Words", "Favorite Words" };
        filterComboBox = new JComboBox<>(filters);
        filterComboBox.addActionListener(e -> refreshTable());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(filterComboBox, gbc);

        String[] columnNames = { "Word", "Urdu Meaning", "Persian Meaning" };
        tableModel = new DefaultTableModel(columnNames, 0);
        wordsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(wordsTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        wordsTable.setFillsViewportHeight(true);

        refreshTable();

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(scrollPane, gbc);

        backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(0, 150, 136));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(backButton, gbc);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToDashboard();
            }
        });

        wordsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = wordsTable.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedWord = (String) wordsTable.getValueAt(selectedRow, 0);
                    String selectedUrduMeaning = (String) wordsTable.getValueAt(selectedRow, 1);
                    String selectedPersianMeaning = (String) wordsTable.getValueAt(selectedRow, 2);
                    openViewSpecificWordView(selectedWord, selectedUrduMeaning, selectedPersianMeaning);
                }
            }
        });

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void openViewSpecificWordView(String word, String urduMeaning, String persianMeaning) {
        new ViewSpecificWord(iblFacade, new Word(word, urduMeaning, persianMeaning), this);
    }

    private void backToDashboard() {
        dispose();
        new MainDashboardView(iblFacade);
    }

    public void refreshTable() {
      
        tableModel.setRowCount(0);

       
        Thread loadingThread = new Thread(() -> {
            List<Word> words;
            if (filterComboBox.getSelectedItem().equals("All Words")) {
                words = iblFacade.getAllWords(); 
            } else {
                words = iblFacade.getFavoriteWords(); 
            }

          
            SwingUtilities.invokeLater(() -> {
                for (Word word : words) {
                    String[] rowData = { word.getWord(), word.getUrduMeaning(), word.getPersianMeaning() };
                    tableModel.addRow(rowData); 
                }
            });
        });

        loadingThread.start(); 
    }
}
