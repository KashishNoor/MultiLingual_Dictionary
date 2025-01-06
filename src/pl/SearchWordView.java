package pl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import bl.IBLFacade;
import dto.Word;

public class SearchWordView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JComboBox<String> meaningComboBox;
    private JTextField wordField;
    private JTextField meaningField;
    private JButton searchButton;
    private JButton clearButton;
    private IBLFacade iblFacade;

    public SearchWordView(IBLFacade iblFacade) {
        this.iblFacade = iblFacade;
        setTitle("Search Word in Dictionary");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(38, 166, 154));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<h1>Search Words</h1>"
                + "<p>Find words and their meanings in the dictionary.</p>"
                + "</div></html>");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        leftPanel.add(infoLabel);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Search Word in Dictionary");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(titleLabel, gbc);

        JLabel wordLabel = new JLabel("Word:");
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        rightPanel.add(wordLabel, gbc);

        wordField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        rightPanel.add(wordField, gbc);

        JLabel meaningLabel = new JLabel("Meaning:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        rightPanel.add(meaningLabel, gbc);

        meaningField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        rightPanel.add(meaningField, gbc);

        meaningComboBox = new JComboBox<>(new String[]{"Urdu", "Persian"});
        meaningComboBox.setBackground(Color.WHITE);
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        rightPanel.add(meaningComboBox, gbc);

        searchButton = new JButton("Search");
        searchButton.setBackground(new Color(0, 150, 136));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(searchButton, gbc);

        clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(244, 67, 54));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        gbc.gridy = 5;
        rightPanel.add(clearButton, gbc);

        JLabel backLabel = new JLabel("<html><u>Back to Dashboard</u></html>");
        backLabel.setForeground(new Color(0, 120, 215));
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        gbc.anchor = GridBagConstraints.SOUTH;
        rightPanel.add(backLabel, gbc);

        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backToDashboard();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchWord();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        setVisible(true);
    }

    private void searchWord() {
        String wordInput = wordField.getText().trim();
        String meaningInput = meaningField.getText().trim();
        String meaningSelection = (String) meaningComboBox.getSelectedItem();

        Word foundWord = null;

        if (!wordInput.isEmpty()) {
            foundWord = iblFacade.getWordByWord(wordInput);
            if (foundWord != null) {
                displayResult(foundWord, meaningSelection);
            } else {
                JOptionPane.showMessageDialog(this, "No matching result found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (!meaningInput.isEmpty()) {
            switch (meaningSelection) {
                case "Urdu":
                    foundWord = iblFacade.getWordByUrduMeaning(meaningInput);
                    break;
                case "Persian":
                    foundWord = iblFacade.getWordByPersianMeaning(meaningInput);
                    break;
            }
            if (foundWord != null) {
                wordField.setText(foundWord.getWord());
            } else {
                JOptionPane.showMessageDialog(this, "No matching result found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a word or meaning to search.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayResult(Word foundWord, String meaningSelection) {
        switch (meaningSelection) {
            case "Urdu":
                meaningField.setText(foundWord.getUrduMeaning());
                break;
            case "Persian":
                meaningField.setText(foundWord.getPersianMeaning());
                break;
        }
    }

    private void clearFields() {
        wordField.setText("");
        meaningField.setText("");
    }

    private void backToDashboard() {
        dispose();
        new MainDashboardView(iblFacade);
    }
}
