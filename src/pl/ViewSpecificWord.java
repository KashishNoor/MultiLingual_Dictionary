package pl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import bl.IBLFacade;
import dto.Word;

public class ViewSpecificWord extends JFrame {
    private static final long serialVersionUID = 1L;
    private JLabel wordLabel;
    private JTextArea urduMeaningArea, persianMeaningArea, rootWordArea;
    private JButton updateButton, removeButton, favoriteButton;
    private IBLFacade iblFacade;
    private ViewAllWords view;
    private boolean isFavorite;

    public ViewSpecificWord(IBLFacade iblFacade, Word word, ViewAllWords view) {
        this.iblFacade = iblFacade;
        this.view = view;
        this.isFavorite = iblFacade.isWordInFavourites(word.getWord());

        setTitle("View Specific Word");
        setSize(600, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel wordPanel = new JPanel(new GridBagLayout());
        wordPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        wordLabel = new JLabel("Word: " + word.getWord());
        wordLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        wordPanel.add(wordLabel, gbc);

        addLabeledTextArea(wordPanel, gbc, "Urdu Meaning:", urduMeaningArea = new JTextArea(word.getUrduMeaning()), 1);
        addLabeledTextArea(wordPanel, gbc, "Persian Meaning:", persianMeaningArea = new JTextArea(word.getPersianMeaning()), 2);
        addLabeledTextArea(wordPanel, gbc, "Root Word:", rootWordArea = new JTextArea(), 3);

        updateButton = createButton("Update", new Color(0, 150, 136), e -> updateWord(word.getWord()));
        removeButton = createButton("Remove", new Color(244, 67, 54), e -> removeWord(word.getWord()));
        favoriteButton = createButton(isFavorite ? "Remove from Favorite" : "Add to Favorite", new Color(33, 150, 243), e -> toggleFavorite(word.getWord()));

        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.gridx = 0;
        wordPanel.add(updateButton, gbc);
        gbc.gridx = 1;
        wordPanel.add(removeButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        wordPanel.add(favoriteButton, gbc);

        mainPanel.add(wordPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void addLabeledTextArea(JPanel panel, GridBagConstraints gbc, String labelText, JTextArea textArea, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(300, 80));
        gbc.gridx = 1;
        panel.add(scrollPane, gbc);
    }

    private JButton createButton(String text, Color color, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(actionListener);
        return button;
    }

    private void updateWord(String word) {
        String newUrduMeaning = urduMeaningArea.getText().trim();
        String newPersianMeaning = persianMeaningArea.getText().trim();
        String newRootWord = rootWordArea.getText().trim();

        if (newUrduMeaning.isEmpty() || newPersianMeaning.isEmpty() || newRootWord.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Urdu, Persian, and Root meanings.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Word updatedWord = new Word(word, newUrduMeaning, newPersianMeaning, newRootWord);
            String result = iblFacade.updateWordMeaning(updatedWord);
            JOptionPane.showMessageDialog(this, result, "Update Word", JOptionPane.INFORMATION_MESSAGE);
            view.refreshTable();
            dispose();
        }
    }

    private void removeWord(String word) {
        String result = iblFacade.removeWord(word);
        JOptionPane.showMessageDialog(this, result, "Remove Word", JOptionPane.INFORMATION_MESSAGE);
        view.refreshTable(); 
        dispose();
    }

    private void toggleFavorite(String word) {
        boolean success;
        if (isFavorite) {
            success = iblFacade.deleteFavoriteWord(word);
            favoriteButton.setText("Add to Favorite");
        } else {
            success = iblFacade.markAsFavorite(word);
            favoriteButton.setText("Remove from Favorite");
        }

        if (success) {
            JOptionPane.showMessageDialog(this, isFavorite ? "Word removed from favorites." : "Word added to favorites.", "Favorite Status", JOptionPane.INFORMATION_MESSAGE);
            isFavorite = !isFavorite;
            view.refreshTable(); 
        } else {
            JOptionPane.showMessageDialog(this, "Operation failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
