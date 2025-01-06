package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import bl.IBLFacade;
//import dto.SearchHistory;
import dto.Word;

public class SearchHistoryView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable historyTable;
    private JButton backButton;
    private IBLFacade iblFacade;
    private DefaultTableModel tableModel;

    public SearchHistoryView(IBLFacade iblFacade) {
        this.iblFacade = iblFacade;
        setTitle("Search History");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        

    
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

       
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(38, 166, 154));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h1>Search History</h1>" +
                "<p>View all your recent searches and their meanings.</p>" +
                "</div></html>");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        leftPanel.add(infoLabel);

   
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 10);

        
        JLabel titleLabel = new JLabel("Search History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(titleLabel, gbc);

       
        String[] columnNames = { "Search Term", "Meaning" };
        tableModel = new DefaultTableModel(columnNames, 0);
        historyTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        historyTable.setFillsViewportHeight(true);

   
        loadSearchHistory();

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(scrollPane, gbc);

        // Back button
        backButton = new JButton("Back to Dashboard");
        backButton.setBackground(new Color(0, 150, 136));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(backButton, gbc);

        backButton.addActionListener(e -> backToDashboard());
        

      
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void loadSearchHistory() {
      
        tableModel.setRowCount(0);

      
        List<Word> searchHistory = iblFacade.getSearchHistory(); 
        if (searchHistory == null || searchHistory.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No search history available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            new MainDashboardView(iblFacade);
            dispose();
        }

        
        for (Word word : searchHistory) {
            String meaning = word.getUrduMeaning() != null ? word.getUrduMeaning() : word.getPersianMeaning();
            tableModel.addRow(new Object[]{word.getWord(), meaning});
        }
    }
    private void backToDashboard() {
      
        dispose();
        
        MainDashboardView dashboard = new MainDashboardView(iblFacade);
        dashboard.setVisible(true);
    }

}
