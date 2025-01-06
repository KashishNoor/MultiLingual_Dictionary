package dal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import co.ConfigReader;
import dto.Dictionary;
import dto.Word;

public class CustomDictionaryDAO implements ICustomDictionaryDAO {

	private static ConfigReader reader = new ConfigReader();
	private static final String URL = reader.getUrl();
	private static final String USER = reader.getUser();
	private static final String PASSWORD = reader.getPassword();

	private static final String INSERT_DICTIONARY_QUERY = "INSERT INTO custom_dictionaries (dictionary_name) VALUES (?)";
	private static final String GET_DICTIONARY_ID_QUERY = "SELECT dictionary_id FROM custom_dictionaries WHERE dictionary_name = ?";
	private static final String GET_WORD_ID_QUERY = "SELECT id FROM words WHERE word = ?";
	private static final String INSERT_DICTIONARY_WORD = "INSERT INTO dictionary_words (dictionary_id, word_id) VALUES (?, ?)";
	private static final String GET_ALL_DICTIONARIES_QUERY = "SELECT dictionary_name, created_at FROM custom_dictionaries";
	private static final String GET_WORDS_BY_DICTIONARY_QUERY = "SELECT dw.word_id, w.word, um.meaning AS urdu_meaning, pm.meaning AS persian_meaning "
			+ "FROM dictionary_words dw " + "JOIN custom_dictionaries cd ON dw.dictionary_id = cd.dictionary_id "
			+ "JOIN words w ON dw.word_id = w.id " + "LEFT JOIN urdumeaning um ON dw.word_id = um.word_id "
			+ "LEFT JOIN persianmeaning pm ON dw.word_id = pm.word_id " + "WHERE cd.dictionary_name = ?";
	private static final String DELETE_DICTIONARY_SQL = "DELETE FROM custom_dictionaries WHERE dictionary_name = ?";
	private WordDAO wordDAO;

	public CustomDictionaryDAO() throws Exception {
		this.wordDAO = new WordDAO();
	}
	@Override
	public boolean addCustomDictionary(String dictionaryName, String filePath) {
	    Connection connection = null;
	    PreparedStatement insertDictionaryStatement = null;
	    try {
	        connection = DriverManager.getConnection(URL, USER, PASSWORD);
	        insertDictionaryStatement = connection.prepareStatement(INSERT_DICTIONARY_QUERY, Statement.RETURN_GENERATED_KEYS);
	        insertDictionaryStatement.setString(1, dictionaryName);
	        int rowsAffected = insertDictionaryStatement.executeUpdate();
	        if (rowsAffected > 0) {
	            try (ResultSet generatedKeys = insertDictionaryStatement.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    int dictionaryId = generatedKeys.getInt(1);
	                    List<Word> words = readFile(filePath);
	                    if (words == null || words.isEmpty()) {
	                        return false; // Return false if no words are found
	                    }
	                    for (Word word : words) {
	                        int wordId = getWordId(connection, word.getWord());
	                        if (wordId != -1) {
	                            try (PreparedStatement insertWordStatement = connection.prepareStatement(INSERT_DICTIONARY_WORD)) {
	                                insertWordStatement.setInt(1, dictionaryId);
	                                insertWordStatement.setInt(2, wordId);
	                                insertWordStatement.executeUpdate();
	                            }
	                        } else {
	                            System.err.println("Word not found in 'words' table: " + word.getWord());
	                        }
	                    }
	                    return true;
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (insertDictionaryStatement != null) insertDictionaryStatement.close();
	            if (connection != null) connection.close();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	    return false;
	}

	private int getDictionaryId(Connection connection, String dictionaryName) {
		try (PreparedStatement getDictionaryIdStatement = connection.prepareStatement(GET_DICTIONARY_ID_QUERY)) {
			getDictionaryIdStatement.setString(1, dictionaryName);
			try (ResultSet resultSet = getDictionaryIdStatement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt("dictionary_id");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private int getWordId(Connection connection, String wordName) {
		try (PreparedStatement getWordIdStatement = connection.prepareStatement(GET_WORD_ID_QUERY)) {
			getWordIdStatement.setString(1, wordName);
			try (ResultSet resultSet = getWordIdStatement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt("id");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private List<Word> readFile(String filePath) {
		List<Word> words = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String text;
			while ((text = reader.readLine()) != null) {
				String[] wordTokens = text.split("\\s+");
				for (String token : wordTokens) {
					Word word = wordDAO.getWordByWordFromDB(token);
					if (word != null) {
						words.add(word);
					} else {
						List<String> segments = wordDAO.segmentWordWithDiacritics(token);
						for (String segment : segments) {
							Word segmentWord = wordDAO.getWordByWordFromDB(segment);
							if (segmentWord != null) {
								words.add(segmentWord);
							} else {
								System.err.println("Segment not found in 'words' table: " + segment);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return words;
	}

	@Override
	public List<Dictionary> getAllDictionaries() {
		List<Dictionary> dictionaries = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_DICTIONARIES_QUERY);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				dictionaries
						.add(new Dictionary(resultSet.getString("dictionary_name"), resultSet.getDate("created_at")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dictionaries;
	}

	@Override
	public List<Word> getWordsByDictionary(String dictionaryName) {
		List<Word> words = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(GET_WORDS_BY_DICTIONARY_QUERY)) {

			preparedStatement.setString(1, dictionaryName);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					String wordName = resultSet.getString("word");
					String urduMeaning = resultSet.getString("urdu_meaning");
					String persianMeaning = resultSet.getString("persian_meaning");
					words.add(new Word(wordName, urduMeaning, persianMeaning));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching words for dictionary: " + dictionaryName);
			e.printStackTrace();
		}
		return words;
	}

	@Override
	public boolean deleteDictionary(String dictionaryName) {

		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = connection.prepareStatement(DELETE_DICTIONARY_SQL)) {
			stmt.setString(1, dictionaryName);
			int affectedRows = stmt.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}