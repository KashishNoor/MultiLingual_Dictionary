package pl;

import java.awt.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import bl.IBLFacade;
import java.util.List;

public class PartofSpeechView extends JFrame {

	private static final long serialVersionUID = -8409774912937491363L;
	private JTable wordsTable;
    private JButton backButton;
    private JButton getPosAndStemButton; 
    private JTextField arabicWordField; 
    private IBLFacade iblFacade;
    private DefaultTableModel tableModel;
    private JList<String> suggestionList;
    private DefaultListModel<String> suggestionModel; 


    public PartofSpeechView(IBLFacade iblFacade) {
        this.iblFacade = iblFacade;
        setTitle("Part of Speech View");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(38, 166, 154));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" + "<h1>All Words</h1>"
                + "<p>Here you can POS, Voweled and Stem words.</p>" + "</div></html>");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        leftPanel.add(infoLabel);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 10);

        JLabel titleLabel = new JLabel("   ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(titleLabel, gbc);

        // Column names for the table
        String[] columnNames = { "Voweled Words", "Stem Words", "Part of Speech", "Root Words" };
        tableModel = new DefaultTableModel(columnNames, 0);
        wordsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(wordsTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        wordsTable.setFillsViewportHeight(true);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        rightPanel.add(scrollPane, gbc); 

        JLabel arabicWordLabel = new JLabel("Enter Arabic Word:");
        gbc.gridx = 0;
        gbc.gridy = 2; 
        gbc.gridwidth = 1;
        rightPanel.add(arabicWordLabel, gbc);

        arabicWordField = new JTextField(20); 
        gbc.gridx = 1;
        rightPanel.add(arabicWordField, gbc);

        
        suggestionModel = new DefaultListModel<>();
        suggestionList = new JList<>(suggestionModel);
        suggestionList.setVisibleRowCount(5); 
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane suggestionScrollPane = new JScrollPane(suggestionList);
        suggestionScrollPane.setPreferredSize(new Dimension(200, 100)); 

        gbc.gridx = 0;
        gbc.gridy = 5; 
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        rightPanel.add(suggestionScrollPane, gbc);

        
        arabicWordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = arabicWordField.getText().trim();
                if (!input.isEmpty()) {
                    List<String> suggestions = iblFacade.getSuggestions(input); 
                    updateSuggestionList(suggestions);
                } else {
                    suggestionModel.clear(); 
                }
            }
        });

        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    String selectedWord = suggestionList.getSelectedValue();
                    arabicWordField.setText(selectedWord); 
                    suggestionModel.clear(); 
                }
            }
        });

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE); 

        getPosAndStemButton = new JButton("Get POS and Stem");
        getPosAndStemButton.setBackground(new Color(0, 150, 136));
        getPosAndStemButton.setForeground(Color.WHITE);
        getPosAndStemButton.setFocusPainted(false);
        buttonPanel.add(getPosAndStemButton); 

        backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(0, 150, 136));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        buttonPanel.add(backButton);
        gbc.gridx = 0;
        gbc.gridy = 4; 
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(buttonPanel, gbc);
        
        getPosAndStemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String arabicWord = arabicWordField.getText().trim();
                if (!arabicWord.isEmpty()) {
                    List<String[]> posDetailsList = iblFacade.getPosDetails(arabicWord);
                    if (!posDetailsList.isEmpty()) {
                        updateTableModel(posDetailsList);
                    }
                    else
                    { 
                      JOptionPane.showMessageDialog(PartofSpeechView.this,"No part of speech details found for the word '"
                    + arabicWord + "'.", "No Details Found",JOptionPane.INFORMATION_MESSAGE);
                    }
                } 
                else
                {
                    JOptionPane.showMessageDialog(PartofSpeechView.this,"Please enter an Arabic word.", 
                    		"Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
            
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToDashboard();
            }
        });

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        setVisible(true);
    }
    
    private void updateTableModel(List<String[]> posDetailsList) {     
        tableModel.setRowCount(0);
        for (String[] details : posDetailsList) {
            tableModel.addRow(details);
        }
    }
    
    private void updateSuggestionList(List<String> suggestions) {
        suggestionModel.clear();
        for (String suggestion : suggestions) {
            suggestionModel.addElement(suggestion); 
        }
    }

    private void backToDashboard() {
        dispose();
        new MainDashboardView(iblFacade);
    }
}