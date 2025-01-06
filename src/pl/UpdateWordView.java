package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bl.IBLFacade;
import dto.Word;

public class UpdateWordView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField wordField;
    private JTextArea urduMeaningArea;
    private JTextArea persianMeaningArea;
    private JButton updateButton;
    private JButton clearButton;
    private IBLFacade iblFacade;

    public UpdateWordView(IBLFacade iblFacade) {
        this.iblFacade = iblFacade;

        setTitle("Update Word in Dictionary");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(38, 166, 154));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<h1>Update Word</h1>"
                + "<p>Modify existing words and their meanings in the dictionary</p></div></html>");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        leftPanel.add(infoLabel);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 10);

        JLabel wordLabel = new JLabel("Word:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        rightPanel.add(wordLabel, gbc);

        wordField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        rightPanel.add(wordField, gbc);

        JLabel urduMeaningLabel = new JLabel("New Urdu Meaning:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        rightPanel.add(urduMeaningLabel, gbc);

        urduMeaningArea = new JTextArea(5, 20);
        urduMeaningArea.setLineWrap(true);
        urduMeaningArea.setWrapStyleWord(true);
        JScrollPane urduScrollPane = new JScrollPane(urduMeaningArea);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        rightPanel.add(urduScrollPane, gbc);

        JLabel persianMeaningLabel = new JLabel("New Persian Meaning:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        rightPanel.add(persianMeaningLabel, gbc);

        persianMeaningArea = new JTextArea(5, 20);
        persianMeaningArea.setLineWrap(true);
        persianMeaningArea.setWrapStyleWord(true);
        JScrollPane persianScrollPane = new JScrollPane(persianMeaningArea);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        rightPanel.add(persianScrollPane, gbc);

        updateButton = new JButton("Update Word");
        updateButton.setBackground(new Color(0, 150, 136));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(updateButton, gbc);

        clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(244, 67, 54));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        gbc.gridy = 4;
        rightPanel.add(clearButton, gbc);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateWord();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        JLabel backLabel = new JLabel("<html><u>Back to Dashboard</u></html>");
        backLabel.setForeground(new Color(0, 120, 215));
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backToDashboard();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        gbc.anchor = GridBagConstraints.SOUTH;
        rightPanel.add(backLabel, gbc);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void updateWord() {
        String word = wordField.getText().trim();
        String urduMeaning = urduMeaningArea.getText().trim();
        String persianMeaning = persianMeaningArea.getText().trim();

        if (word.isEmpty() || urduMeaning.isEmpty() || persianMeaning.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the word and its meanings in both Urdu and Persian.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Word updatedWord = new Word(word, urduMeaning, persianMeaning);

            String result = iblFacade.updateWordMeaning(updatedWord);
            JOptionPane.showMessageDialog(this, result, "Update Result", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        }
    }

    private void clearFields() {
        wordField.setText("");
        urduMeaningArea.setText("");
        persianMeaningArea.setText("");
    }

    private void backToDashboard() {
        dispose();
        new MainDashboardView(iblFacade);
    }
}
