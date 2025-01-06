package pl;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import bl.IBLFacade;
import dto.Word;

public class ViewSpecificDictionary extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable wordsTable;
	private JButton backButton;
	private IBLFacade iblFacade;
	private DefaultTableModel tableModel;
	private String dictionaryName;
	private JFrame parentFrame;

	public ViewSpecificDictionary(IBLFacade iblFacade, String dictionaryName, JFrame parentFrame) {
		this.iblFacade = iblFacade;
		this.dictionaryName = dictionaryName;
		this.parentFrame = parentFrame;

		setTitle("View Dictionary - " + dictionaryName);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(new Color(240, 248, 255));

		// Left Panel
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(new Color(38, 166, 154));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" + "<h1>" + dictionaryName + "</h1>"
				+ "<p>View all words in the selected dictionary.</p>" + "</div></html>");
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

		leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		leftPanel.add(infoLabel);

		// Right Panel
		JPanel rightPanel = new JPanel(new GridBagLayout());
		rightPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 5, 10, 10);

		JLabel titleLabel = new JLabel("Words in Dictionary: " + dictionaryName);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		rightPanel.add(titleLabel, gbc);

		String[] columnNames = { "Word", "Urdu Meaning", "Persian Meaning" };
		tableModel = new DefaultTableModel(columnNames, 0);
		wordsTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(wordsTable);
		scrollPane.setPreferredSize(new Dimension(600, 300));
		wordsTable.setFillsViewportHeight(true);
		List<Word> words = iblFacade.getWordsByDictionary(dictionaryName);
		for (Word word : words) {
			String[] rowData = { word.getWord(), word.getUrduMeaning(), word.getPersianMeaning() };
			tableModel.addRow(rowData);
		}

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		rightPanel.add(scrollPane, gbc);
		backButton = new JButton("Back to Dictionaries");
		backButton.setBackground(new Color(0, 150, 136));
		backButton.setForeground(Color.WHITE);
		backButton.setFocusPainted(false);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(20, 0, 10, 0);
		gbc.anchor = GridBagConstraints.CENTER;
		rightPanel.add(backButton, gbc);

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				backToParentView();
			}
		});

		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(rightPanel, BorderLayout.CENTER);

		setContentPane(mainPanel);
		setVisible(true);
	}

	private void backToParentView() {
		dispose();
		parentFrame.setVisible(true); 
	}

	public void refreshTable() {
		tableModel.setRowCount(0); 

		List<Word> words = iblFacade.getWordsByDictionary(dictionaryName);
		for (Word word : words) {
			String[] rowData = { word.getWord(), word.getUrduMeaning(), word.getPersianMeaning() };
			tableModel.addRow(rowData);
		}
	}
}
