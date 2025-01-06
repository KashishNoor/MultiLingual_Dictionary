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
import javax.swing.JTextField;
import bl.IBLFacade;

public class RemoveWordView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField wordField;
    private JButton removeButton;
    private JButton clearButton;
    private IBLFacade iblFacade;

    public RemoveWordView(IBLFacade iblFacade) {
        this.iblFacade = iblFacade;
        setTitle("Remove Word");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(38, 166, 154));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<h1>Remove Word</h1>"
                + "<p>Delete an existing word from the dictionary</p></div></html>");
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        leftPanel.add(titleLabel);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 10);

        JLabel wordLabel = new JLabel("Word to Remove:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        rightPanel.add(wordLabel, gbc);

        wordField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        rightPanel.add(wordField, gbc);

        removeButton = new JButton("Remove Word");
        removeButton.setBackground(new Color(244, 67, 54));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(removeButton, gbc);

        clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(33, 150, 243));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        rightPanel.add(clearButton, gbc);

        JLabel backLabel = new JLabel("<html><u>Back to Dashboard</u></html>");
        backLabel.setForeground(new Color(0, 120, 215));
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backToDashboard();
            }
        });

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        rightPanel.add(backLabel, gbc);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeWord();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
    }

    private void removeWord() {
        String word = wordField.getText().trim();

        if (word.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a word to remove.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            String result = iblFacade.removeWord(word);
            JOptionPane.showMessageDialog(this, result, "Remove Word", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        }
    }

    private void clearFields() {
        wordField.setText("");
    }

    private void backToDashboard() {
        dispose();
        new MainDashboardView(iblFacade);
    }
}
