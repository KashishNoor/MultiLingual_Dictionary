package pl;

import java.awt.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import bl.IBLFacade;
import dto.Word;

import java.util.List;

public class SearchByRootView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable rootWordsTable;
    private JButton backButton;
    private JButton searchByRootButton;
    private JTextField rootWordField;
    private IBLFacade iblFacade;
    private DefaultTableModel tableModel;
    private JList<String> suggestionList;
    private DefaultListModel<String> suggestionModel;

    public SearchByRootView(IBLFacade iblFacade) {
        this.iblFacade = iblFacade;
        setTitle("Search by Root View");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(38, 166, 154));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" + 
                                      "<h1>Search by Root</h1>" +
                                      "<p>Find all words associated with a root word.</p>" + 
                                      "</div></html>");
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
        
        String[] columnNames = { "Word", "Urdu Meaning", "Persian Meaning" };
        tableModel = new DefaultTableModel(columnNames, 0);
        rootWordsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(rootWordsTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        rootWordsTable.setFillsViewportHeight(true);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        rightPanel.add(scrollPane, gbc);

        JLabel rootWordLabel = new JLabel("Enter Root Word:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        rightPanel.add(rootWordLabel, gbc);

        rootWordField = new JTextField(20);
        gbc.gridx = 1;
        rightPanel.add(rootWordField, gbc);

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

        rootWordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = rootWordField.getText().trim();
                if (!input.isEmpty()) {
                   List<String> suggestions = iblFacade.getRootSuggestions(input); 
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
                    String selectedRoot = suggestionList.getSelectedValue();
                    rootWordField.setText(selectedRoot);
                    suggestionModel.clear();
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        searchByRootButton = new JButton("Search by Root");
        searchByRootButton.setBackground(new Color(0, 150, 136));
        searchByRootButton.setForeground(Color.WHITE);
        searchByRootButton.setFocusPainted(false);
        buttonPanel.add(searchByRootButton);

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

        searchByRootButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String rootWord = rootWordField.getText().trim();
                if (!rootWord.isEmpty()) {
                   List<Word> rootWordsList = iblFacade.getWordsByRoot(rootWord); //getWordsByRoot
                    if (!rootWordsList.isEmpty()) {
                        updateTableModel(rootWordsList);
                    } else {
                        JOptionPane.showMessageDialog(SearchByRootView.this,
                                "No words found for the root '" + rootWord + "'.",
                               "No Results Found",
                               JOptionPane.INFORMATION_MESSAGE);
                   }
                } else {
                   JOptionPane.showMessageDialog(SearchByRootView.this,
                            "Please enter a root word.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
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

    private void updateTableModel(List<Word> rootWordsList) {
        tableModel.setRowCount(0);  

        for (Word word : rootWordsList) {
            Object[] row = new Object[3]; 
            row[0] = word.getWord();  
            row[1] = word.getUrduMeaning(); 
            row[2] = word.getPersianMeaning();  

            tableModel.addRow(row);
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