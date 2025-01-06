package pl;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import bl.IBLFacade;
import dto.Word;

public class ViewFavoriteWords extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTable favoriteWordsTable;
    private JButton backButton;
    private JButton deleteButton; 
    private IBLFacade iblFacade;
    private DefaultTableModel tableModel;

    public ViewFavoriteWords(IBLFacade iblFacade) {
        this.iblFacade = iblFacade;

        setTitle("View Favorite Words");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        initUIComponents();
        refreshTable();
    }
    private void initUIComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(38, 166, 154));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h1>Favorite Words</h1>" +
                "<p>Here you can view all your favorite words.</p>" +
                "</div></html>");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        leftPanel.add(infoLabel);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 10);

        JLabel titleLabel = new JLabel("Favorite Words in Dictionary");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(titleLabel, gbc);

        String[] columnNames = {"Word", "Urdu Meaning", "Persian Meaning"};
        tableModel = new DefaultTableModel(columnNames, 0);
        favoriteWordsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(favoriteWordsTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        favoriteWordsTable.setFillsViewportHeight(true);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        rightPanel.add(scrollPane, gbc);

        deleteButton = new JButton("Delete Favorite");
        deleteButton.setBackground(new Color(255, 99, 71));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(deleteButton, gbc);

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

        backButton.addActionListener(e -> backToDashboard());
        deleteButton.addActionListener(e -> deleteFavoriteWord());

        return rightPanel;
    }

    // Method to delete selected favorite word
    private void deleteFavoriteWord() {
        int selectedRow = favoriteWordsTable.getSelectedRow();
        if (selectedRow >= 0) {
            String word = (String) tableModel.getValueAt(selectedRow, 0);

            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this word from favorites?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                try {
                    iblFacade.deleteFavoriteWord(word);
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Word deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting word: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a word to delete.",
                    "No Word Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void refreshTable() {
        try {
            updateFavoriteWordsTableModel();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error refreshing favorite words: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFavoriteWordsTableModel() {
        tableModel.setRowCount(0); 

        List<Word> favoriteWords = iblFacade.getFavoriteWords();
        if (favoriteWords.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No favorite words found.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Word word : favoriteWords) {
                tableModel.addRow(new Object[]{
                        word.getWord(),
                        word.getUrduMeaning(),
                        word.getPersianMeaning()
                });
            }
        }
    }

    private void backToDashboard() {
        dispose();
        new MainDashboardView(iblFacade);
    }
}