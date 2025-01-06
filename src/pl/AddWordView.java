package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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

public class AddWordView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField wordField;
    private JTextArea urduMeaningArea;
    private JTextArea persianMeaningArea;
    private JButton addButton;
    private JButton clearButton;
    private IBLFacade iblFacade;

    public AddWordView(IBLFacade iblFacade) {
        this.iblFacade = iblFacade;
        setTitle("Add Word to Dictionary");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(38, 166, 154));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<h1>Add New Words</h1>"
                + "<p>Enhance your vocabulary by adding new words</p><p> and their meanings.</p>"
                + "</div></html>");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        leftPanel.add(infoLabel);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Add Word to Dictionary");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(titleLabel, gbc);

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
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        gbc.anchor = GridBagConstraints.SOUTH;
        rightPanel.add(backLabel, gbc);

        JLabel wordLabel = new JLabel("Word:");
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(10, 20, 10, 10); 
        rightPanel.add(wordLabel, gbc);

        wordField = new JTextField(20);
        wordField.setPreferredSize(new Dimension(200, 30)); 
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        rightPanel.add(wordField, gbc);

        JLabel urduMeaningLabel = new JLabel("Urdu Meaning:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(10, 20, 10, 10); 
        rightPanel.add(urduMeaningLabel, gbc);

        String urduPlaceholder = "Enter Urdu meaning...";
        urduMeaningArea = new JTextArea(urduPlaceholder, 3, 20);
        urduMeaningArea.setLineWrap(true);
        urduMeaningArea.setWrapStyleWord(true);
        urduMeaningArea.setForeground(Color.GRAY);
        urduMeaningArea.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (urduMeaningArea.getText().equals(urduPlaceholder)) {
                    urduMeaningArea.setText("");
                    urduMeaningArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (urduMeaningArea.getText().isEmpty()) {
                    urduMeaningArea.setForeground(Color.GRAY);
                    urduMeaningArea.setText(urduPlaceholder);
                }
            }
        });
        JScrollPane urduScrollPane = new JScrollPane(urduMeaningArea);
        gbc.gridx = 1;
        rightPanel.add(urduScrollPane, gbc);
        JLabel persianMeaningLabel = new JLabel("Persian Meaning:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(10, 20, 10, 10); 
        rightPanel.add(persianMeaningLabel, gbc);

        String persianPlaceholder = "Enter Persian meaning...";
        persianMeaningArea = new JTextArea(persianPlaceholder, 3, 20);
        persianMeaningArea.setLineWrap(true);
        persianMeaningArea.setWrapStyleWord(true);
        persianMeaningArea.setForeground(Color.GRAY);
        persianMeaningArea.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (persianMeaningArea.getText().equals(persianPlaceholder)) {
                    persianMeaningArea.setText("");
                    persianMeaningArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (persianMeaningArea.getText().isEmpty()) {
                    persianMeaningArea.setForeground(Color.GRAY);
                    persianMeaningArea.setText(persianPlaceholder);
                }
            }
        });
        JScrollPane persianScrollPane = new JScrollPane(persianMeaningArea);
        gbc.gridx = 1;
        rightPanel.add(persianScrollPane, gbc);

        addButton = new JButton("Add Word");
        addButton.setBackground(new Color(0, 150, 136));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(addButton, gbc);

        clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(244, 67, 54));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        gbc.gridy = 5;
        rightPanel.add(clearButton, gbc);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addWord();
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

    private void addWord() {
        String word = wordField.getText().trim();
        String urduMeaning = urduMeaningArea.getText().trim();
        String persianMeaning = persianMeaningArea.getText().trim();

        if (word.isEmpty() || urduMeaning.isEmpty() || persianMeaning.isEmpty() || urduMeaning.equals("Enter Urdu meaning...") || persianMeaning.equals("Enter Persian meaning...")) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = iblFacade.addWord(new Word(word, urduMeaning, persianMeaning));
        JOptionPane.showMessageDialog(this,word +  result, "Add Word", JOptionPane.INFORMATION_MESSAGE);
        clearFields();
    }

    private void clearFields() {
        wordField.setText("");
        urduMeaningArea.setText("Enter Urdu meaning...");
        urduMeaningArea.setForeground(Color.GRAY);
        persianMeaningArea.setText("Enter Persian meaning...");
        persianMeaningArea.setForeground(Color.GRAY);
    }

    private void backToDashboard() {
        dispose();
        new MainDashboardView(iblFacade);
    }
}
