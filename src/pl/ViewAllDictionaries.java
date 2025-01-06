package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import bl.IBLFacade;
//import dto.Dictionary;
import dto.Dictionary;

public class ViewAllDictionaries extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JTable dictionariesTable;
	private JButton backButton;
	private IBLFacade iblFacade;
	private DefaultTableModel tableModel;

	public ViewAllDictionaries(IBLFacade iblFacade) {
		this.iblFacade = iblFacade;
		setTitle("View All Dictionaries");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(new Color(240, 248, 255));
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(new Color(38, 166, 154));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" + "<h1>All Dictionaries</h1>"
				+ "<p>Here you can view all dictionaries created in the system.</p>" + "</div></html>");
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

		leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		leftPanel.add(infoLabel);
		JPanel rightPanel = new JPanel(new GridBagLayout());
		rightPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 5, 10, 10);

		JLabel titleLabel = new JLabel("All Dictionaries");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		rightPanel.add(titleLabel, gbc);

		String[] columnNames = { "Dictionary Name", "Created At" };
		tableModel = new DefaultTableModel(columnNames, 0);
		dictionariesTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(dictionariesTable);
		scrollPane.setPreferredSize(new Dimension(600, 300));
		dictionariesTable.setFillsViewportHeight(true);

		List<Dictionary> allDictionaries = iblFacade.getAllDictionaries();
		for (Dictionary dictionary : allDictionaries) {
			String[] rowData = { dictionary.getName(), dictionary.getCreatedAt().toString(),};
			tableModel.addRow(rowData);
		}

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		rightPanel.add(scrollPane, gbc);

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
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				backToDashboard();
			}
		});

		dictionariesTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int selectedRow = dictionariesTable.getSelectedRow();
				if (selectedRow != -1) {
					String selectedDictionaryName = (String) dictionariesTable.getValueAt(selectedRow, 0);
					openViewSpecificDictionaryView(selectedDictionaryName);
				}
			}
		});

		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(rightPanel, BorderLayout.CENTER);

		setContentPane(mainPanel);
		setVisible(true);
	}

	private void openViewSpecificDictionaryView(String dictionaryName) {
		dispose();
		new ViewSpecificDictionary(iblFacade, dictionaryName, this);
		
	}

	private void backToDashboard() {
		dispose();
		new MainDashboardView(iblFacade);
	}

	public void refreshTable() {
		tableModel.setRowCount(0);

		List<Dictionary> allDictionaries = iblFacade.getAllDictionaries();
		for (Dictionary dictionary : allDictionaries) {
			String[] rowData = { dictionary.getName(), dictionary.getCreatedAt().toString(), };
			tableModel.addRow(rowData);
		}
	}
}
