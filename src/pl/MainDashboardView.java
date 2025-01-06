package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import bl.IBLFacade;
import dto.Word;

public class MainDashboardView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton addWordButton, viewAllWordsButton, importFileButton, addCustomDictionaryButton, exitButton,
			posAndStemmerButton, viewCustomDictionaryButton, searchByRootButton, viewFavorite;
	private JTextArea searchWordField, searchMeaningField;
	private JToggleButton menuButton;
	private JComboBox<String> languageComboBox;
	private DefaultTableModel tableModel;
	private JList<String> suggestionList;
	private DefaultListModel<String> suggestionModel;

	private IBLFacade iblFacade;

	public MainDashboardView(IBLFacade iblFacade) {
		this.iblFacade = iblFacade;
		setTitle("Dictionary Dashboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(950, 650);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(new Color(240, 248, 255));
		JPanel leftPanel = new JPanel(new GridBagLayout());
		leftPanel.setBackground(new Color(38, 166, 154));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel titleLabel = new JLabel("Dictionary");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		leftPanel.add(titleLabel, gbc);

		JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>"
				+ "<p>Empowering words, enhancing learning!</p>" + "</div></html>");
		infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		infoLabel.setForeground(Color.WHITE);
		gbc.gridy = 1;
		leftPanel.add(infoLabel, gbc);
		addWordButton = createButton("Add Word");
		viewAllWordsButton = createButton("View All Words");
		importFileButton = createButton("Import File");
		addCustomDictionaryButton = createButton("Add Custom Dictionary");
		posAndStemmerButton = createButton("PosTagger/Stemmer");
		viewCustomDictionaryButton = createButton("View Custom Dictionary");
		searchByRootButton = createButton("Search by Root");
		viewFavorite = createButton("View Favorite");
		exitButton = createButton("Exit");

		gbc.gridy = 2;
		leftPanel.add(addWordButton, gbc);

		gbc.gridy = 3;
		leftPanel.add(viewAllWordsButton, gbc);

		gbc.gridy = 4;
		leftPanel.add(importFileButton, gbc);

		gbc.gridy = 5;
		leftPanel.add(addCustomDictionaryButton, gbc);

		gbc.gridy = 6;
		leftPanel.add(posAndStemmerButton, gbc);

		gbc.gridy = 7;
		leftPanel.add(viewCustomDictionaryButton, gbc);

		gbc.gridy = 8;
		leftPanel.add(searchByRootButton, gbc);

		gbc.gridy = 9;
		leftPanel.add(viewFavorite, gbc);

		gbc.gridy = 10;
		leftPanel.add(exitButton, gbc);
		JPanel rightPanel = new JPanel(new GridBagLayout());
		rightPanel.setBackground(Color.WHITE);
		GridBagConstraints gbcRight = new GridBagConstraints();
		gbcRight.insets = new Insets(10, 10, 10, 10);

		JLabel dashboardLabel = new JLabel("Search Dictionary");
		dashboardLabel.setFont(new Font("Arial", Font.BOLD, 26));
		gbcRight.gridx = 0;
		gbcRight.gridy = 0;
		gbcRight.gridwidth = 2;
		gbcRight.anchor = GridBagConstraints.CENTER;
		rightPanel.add(dashboardLabel, gbcRight);

		JLabel wordLabel = new JLabel("Word:");
		gbcRight.gridy = 1;
		gbcRight.gridx = 0;
		gbcRight.gridwidth = 1;
		gbcRight.anchor = GridBagConstraints.LINE_END;
		rightPanel.add(wordLabel, gbcRight);

		searchWordField = new JTextArea(1, 40);
		setupTextArea(searchWordField);
		JScrollPane wordScrollPane = new JScrollPane(searchWordField);
		gbcRight.gridx = 1;
		gbcRight.anchor = GridBagConstraints.LINE_START;
		rightPanel.add(wordScrollPane, gbcRight);

		JLabel meaningLabel = new JLabel("Meaning:");
		gbcRight.gridy = 2;
		gbcRight.gridx = 0;
		gbcRight.anchor = GridBagConstraints.LINE_END;
		rightPanel.add(meaningLabel, gbcRight);

		searchMeaningField = new JTextArea(3, 40);
		setupTextArea(searchMeaningField);
		JScrollPane meaningScrollPane = new JScrollPane(searchMeaningField);
		gbcRight.gridx = 1;
		gbcRight.anchor = GridBagConstraints.LINE_START;
		rightPanel.add(meaningScrollPane, gbcRight);

		JLabel languageLabel = new JLabel("Language:");
		gbcRight.gridy = 3;
		gbcRight.gridx = 0;
		gbcRight.anchor = GridBagConstraints.LINE_END;
		rightPanel.add(languageLabel, gbcRight);

		suggestionModel = new DefaultListModel<>();
		suggestionList = new JList<>(suggestionModel);
		suggestionList.setVisibleRowCount(5);
		suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tableModel = new DefaultTableModel(new String[] { "Suggestions" }, 0);
		JTable suggestionTable = new JTable(tableModel) {
			@Override
			public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (!isRowSelected(row)) {
					c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
				} else {
					c.setBackground(new Color(173, 216, 230));
				}
				return c;
			}
		};

		suggestionTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		suggestionTable.setRowHeight(25);
		suggestionTable.setShowGrid(true);
		suggestionTable.setGridColor(new Color(200, 200, 200));
		suggestionTable.setSelectionForeground(Color.BLACK);
		suggestionTable.setSelectionBackground(new Color(135, 206, 250));
		suggestionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		suggestionTable.getTableHeader().setBackground(new Color(38, 166, 154));
		suggestionTable.getTableHeader().setForeground(Color.WHITE);

		JScrollPane suggestionScrollPane = new JScrollPane(suggestionTable);
		suggestionScrollPane.setPreferredSize(new Dimension(400, 200));

		gbcRight.gridx = 1;
		gbcRight.gridy = 6;
		gbcRight.gridwidth = 2;
		gbcRight.fill = GridBagConstraints.BOTH;
		rightPanel.add(suggestionScrollPane, gbcRight);

		searchWordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String input = searchWordField.getText().trim();
				if (input.isEmpty()) {
					suggestionModel.clear();
					tableModel.setRowCount(0);

				} else {
					new Thread(() -> {
						List<String> suggestions = iblFacade.getSuggestions(input);
						SwingUtilities.invokeLater(() -> {
							updateSuggestionList(suggestions);
						});
					}).start();
				}
			}
		});

		suggestionTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = suggestionTable.getSelectedRow();
				if (row != -1) {
					String selectedSuggestion = (String) suggestionTable.getValueAt(row, 0);
					searchWordField.setText(selectedSuggestion);
				}
			}
		});

		languageComboBox = new JComboBox<>(new String[] { "Urdu Meaning", "Persian Meaning" });
		languageComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		languageComboBox.setBackground(Color.WHITE);
		languageComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					clearFields();
				}
			}
		});

		JLabel searchHistoryLabel = new JLabel("<html><u>Search History</u></html>");
		searchHistoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		searchHistoryLabel.setForeground(new Color(0, 102, 204)); // Bright blue color
		searchHistoryLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		searchHistoryLabel.setHorizontalAlignment(JLabel.CENTER);

		searchHistoryLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				new SearchHistoryView(iblFacade);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				searchHistoryLabel.setForeground(new Color(51, 153, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				searchHistoryLabel.setForeground(new Color(0, 102, 204));
			}
		});

		gbcRight.gridy = 5;
		gbcRight.gridx = 1;
		gbcRight.anchor = GridBagConstraints.CENTER;
		rightPanel.add(searchHistoryLabel, gbcRight);
		gbcRight.gridy = 3;
		gbcRight.gridx = 1;
		gbcRight.anchor = GridBagConstraints.LINE_START;
		rightPanel.add(languageComboBox, gbcRight);
		JButton searchButton = createButton("Search");
		gbcRight.gridy = 4;
		gbcRight.gridx = 1;
		gbcRight.gridwidth = 2;
		gbcRight.anchor = GridBagConstraints.CENTER;
		rightPanel.add(searchButton, gbcRight);

		addWordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AddWordView(iblFacade);
				dispose();
			}
		});
		viewAllWordsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ViewAllWords(iblFacade);
				dispose();
			}
		});
		searchButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				searchWord(searchWordField.getText(), searchMeaningField.getText());
			}
		});
		importFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ImportFileView(iblFacade);
				dispose();
			}
		});
		viewCustomDictionaryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ViewAllDictionaries(iblFacade);
				dispose();
			}
		});
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		posAndStemmerButton.addActionListener(e -> {
			dispose();
			new PartofSpeechView(iblFacade);
		});
		addCustomDictionaryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new AddCustomDictionaryView(iblFacade);
			}
		});

		searchByRootButton.addActionListener(e -> {
			new SearchByRootView(iblFacade);
			dispose();
		});

		viewFavorite.addActionListener(e -> {
			new ViewFavoriteWords(iblFacade);
			dispose();
		});
		menuButton = new JToggleButton("MENU");
		menuButton.setFont(new Font("Arial", Font.BOLD, 18));
		menuButton.setFocusPainted(false);
		menuButton.setBackground(new Color(0, 150, 136));
		menuButton.setForeground(Color.WHITE);
		menuButton.setBorderPainted(false);
		menuButton.addActionListener(e -> {
			if (menuButton.isSelected()) {
				mainPanel.add(leftPanel, BorderLayout.WEST);
			} else {
				mainPanel.remove(leftPanel);
			}
			mainPanel.revalidate();
			mainPanel.repaint();
		});
		JPanel menuPanel = new JPanel(new BorderLayout());
		menuPanel.setBackground(Color.WHITE);
		menuPanel.add(menuButton, BorderLayout.WEST);
		mainPanel.add(menuPanel, BorderLayout.NORTH);
		mainPanel.add(rightPanel, BorderLayout.CENTER);
		setContentPane(mainPanel);
		setVisible(true);
	}

	private void setupTextArea(JTextArea textArea) {
		textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
	}

	private JButton createButton(String text) {
		JButton button = new JButton(text);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setBackground(new Color(0, 150, 136));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		return button;
	}

	private void updateSuggestionList(List<String> suggestions) {
		tableModel.setRowCount(0);
		for (String suggestion : suggestions) {
			tableModel.addRow(new Object[] { suggestion }); // Add each suggestion as a row
		}
	}

	public void clearFields() {
		searchWordField.setText("");
		searchMeaningField.setText("");
		suggestionModel.clear();
		tableModel.setRowCount(0);

	}

	private void searchWord(String word, String meaning) {
		if (word.isEmpty() && meaning.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a word or meaning to search for.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String selectedLanguage = (String) languageComboBox.getSelectedItem();
		Word result = null;
		if (!word.isEmpty()) {
			result = iblFacade.getWordByWord(word);
		} else if (!meaning.isEmpty()) {
			if (selectedLanguage.equals("Urdu Meaning"))
				result = iblFacade.getWordByUrduMeaning(meaning);
			else
				result = iblFacade.getWordByPersianMeaning(meaning);
		}

		if (result != null) {
			searchWordField.setText(result.getWord());
			if (selectedLanguage.equals("Urdu Meaning")) {
				searchMeaningField.setText(result.getUrduMeaning());
				iblFacade.addSearchHistory(result.getWord(), result.getUrduMeaning());
			} else {
				searchMeaningField.setText(result.getPersianMeaning());
				iblFacade.addSearchHistory(result.getWord(), result.getPersianMeaning());
			}
		} else {
			promptForScrapingOrSegmentation(word, selectedLanguage);
		}
	}

	private void promptForScrapingOrSegmentation(String wordInput, String language) {
		String[] options = { "Scrape Meaning", "Perform Segmentation", "Cancel" };
		int choice = JOptionPane.showOptionDialog(this,
				"No matching result found. Would you like to scrape the meaning or perform word segmentation?",
				"Scrape or Segment Word", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
				options[0]);

		if (choice == 0) {
			CompletableFuture<String> meanings1 = iblFacade.urduMeaningScrapper(wordInput);
			CompletableFuture<String> meanings2 = iblFacade.persianMeaningScrapper(wordInput);

			meanings1.thenCombine(meanings2, (urduMeanings, persianMeanings) -> {
				SwingUtilities.invokeLater(() -> {
					if (!urduMeanings.isEmpty() && language.equals("Urdu Meaning")) {
						searchMeaningField.setText(urduMeanings);
						iblFacade.addSearchHistory(wordInput, urduMeanings);
					} else if (!persianMeanings.isEmpty() && language.equals("Persian Meaning")) {
						searchMeaningField.setText(persianMeanings);
						iblFacade.addSearchHistory(wordInput, persianMeanings);
					} else {
						JOptionPane.showMessageDialog(this, "No meaning found through scraping.", "Scrape Result",
								JOptionPane.INFORMATION_MESSAGE);
					}
					String urduMeaning = urduMeanings.isEmpty() ? "" : urduMeanings;
					String persianMeaning = persianMeanings.isEmpty() ? "" : persianMeanings;
					iblFacade.addWord(new Word(wordInput, urduMeaning, persianMeaning));
				});
				return null;
			}).exceptionally(ex -> {
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(this, "An error occurred while scraping meanings: " + ex.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				});
				return null;
			});

		} else if (choice == 1) {
			CompletableFuture<List<String>> segmentedWordsFuture = CompletableFuture
					.supplyAsync(() -> iblFacade.segmentWordWithDiacritics(wordInput));

			segmentedWordsFuture.thenAccept(segmentedWords -> {
				SwingUtilities.invokeLater(() -> {
					if (segmentedWords.isEmpty()) {
						JOptionPane.showMessageDialog(this, "No segmented words were found.", "Segmentation Result",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						String segmentedWordsText = String.join(", ", segmentedWords);
						JOptionPane.showMessageDialog(this, "Segmented Words: " + segmentedWordsText,
								"Segmentation Result", JOptionPane.INFORMATION_MESSAGE);
						String majorSegment = identifyMajorSegment(segmentedWords);
						Word majorSegmentWordObj = iblFacade.getWordByWord(majorSegment);

						if (majorSegmentWordObj != null) {
							String majorMeaning = language.equals("Urdu Meaning") ? majorSegmentWordObj.getUrduMeaning()
									: majorSegmentWordObj.getPersianMeaning();
							searchMeaningField.setText(majorMeaning);
							iblFacade.addSearchHistory(wordInput, majorMeaning);
						} else {
							JOptionPane.showMessageDialog(this,
									"No meaning found for the major segment: " + majorSegment, "Segmented Meaning",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				});
			}).exceptionally(ex -> {
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(this,
							"An error occurred while performing word segmentation: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				});
				return null;
			});
		} else {
			JOptionPane.showMessageDialog(this, "Operation canceled.", "Info", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private String identifyMajorSegment(List<String> segmentedWords) {
		return segmentedWords.stream().max((word1, word2) -> Integer.compare(word1.length(), word2.length()))
				.orElse(segmentedWords.get(0));
	}

}