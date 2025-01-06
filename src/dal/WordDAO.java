package dal;

import java.io.BufferedReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.qcri.farasa.segmenter.Farasa;

import co.ConfigReader;
import dto.Word;

public class WordDAO implements IWordDAO {
	private static final Logger logger = LogManager.getLogger(WordDAO.class);

	private static ConfigReader reader = new ConfigReader();
	private static final String URL = reader.getUrl();
	private static final String USER = reader.getUser();
	private static final String PASSWORD = reader.getPassword();
	private final Farasa farasaSegmenter;

	public WordDAO() throws Exception {

		farasaSegmenter = new Farasa();
	}

	private static final String INSERT_WORD_SQL = "INSERT INTO words (word) VALUES (?)";
	private static final String INSERT_URDU_MEANING_SQL = "INSERT INTO urduMeaning (word_id, meaning) VALUES (?, ?)";
	private static final String INSERT_PERSIAN_MEANING_SQL = "INSERT INTO persianMeaning (word_id, meaning) VALUES (?, ?)";
	private static final String UPDATE_URDU_MEANING_SQL = "UPDATE urduMeaning SET meaning = ? WHERE word_id = ?";
	private static final String UPDATE_PERSIAN_MEANING_SQL = "UPDATE persianMeaning SET meaning = ? WHERE word_id = ?";
	private static final String DELETE_WORD_SQL = "DELETE FROM words WHERE word = ?";
	private static final String GET_ALL_WORDS_SQL = "SELECT w.word, u.meaning as urduMeaning, p.meaning as persianMeaning FROM words w LEFT JOIN urduMeaning u ON w.id = u.word_id LEFT JOIN persianMeaning p ON w.id = p.word_id";
	private static final String WORD_EXIST_SQL = "SELECT COUNT(*) FROM words WHERE word = ?";
	private static final String GET_WORD_ID_SQL = "SELECT id FROM words WHERE word = ?";
	private static final String GET_WORD_BY_URDU_MEANING_SQL = "SELECT w.word, u.meaning as urduMeaning, p.meaning as persianMeaning FROM words w LEFT JOIN urduMeaning u ON w.id = u.word_id LEFT JOIN persianMeaning p ON w.id = p.word_id WHERE u.meaning = ?";
	private static final String GET_WORD_BY_PERSIAN_MEANING_SQL = "SELECT w.word, u.meaning as urduMeaning, p.meaning as persianMeaning FROM words w LEFT JOIN urduMeaning u ON w.id = u.word_id LEFT JOIN persianMeaning p ON w.id = p.word_id WHERE p.meaning = ?";
	private static final String GET_WORD_BY_WORD_SQL = "SELECT w.word, u.meaning as urduMeaning, p.meaning as persianMeaning FROM words w LEFT JOIN urduMeaning u ON w.id = u.word_id LEFT JOIN persianMeaning p ON w.id = p.word_id WHERE w.word = ?";
	private static final String INSERT_SEARCH_HISTORY_SQL = "INSERT INTO search_history (searched_word, meaning) VALUES (?, ?)";
	// private static final String GET_SEARCH_HISTORY_SQL = "SELECT searched_word,
	// meaning, timestamp FROM search_history ORDER BY timestamp DESC LIMIT ?";
	private static final String INSERT_PART_OF_SPEECH_SQL = "INSERT INTO PartOfSpeech (wordid, voweled,stem,pos,root) VALUES (?, ? ,?, ?, ?)";
	private static final String INSERT_FAVORITES_SQL = "INSERT INTO Favourites (wordid, wordname) VALUES (? ,?)";
	private static final String GET_WORDS_BY_ROOT_SQL = "SELECT w.word, u.meaning AS urduMeaning, p.meaning AS persianMeaning "
			+ "FROM words w " + "LEFT JOIN PartOfSpeech pos ON w.id = pos.wordid "
			+ "LEFT JOIN urduMeaning u ON w.id = u.word_id " + "LEFT JOIN persianMeaning p ON w.id = p.word_id "
			+ "WHERE pos.root LIKE ?";
	private static final String GET_SEARCH_HISTORY_SQL = "SELECT searched_word, meaning, timestamp FROM search_history ORDER BY timestamp DESC LIMIT 20";
	private static final String FAVOURITE_WORD_EXIST_SQL = "SELECT COUNT(*) FROM favourites WHERE wordname = ?";
	private static final String GET_ROOT_FROM_POS = "SELECT root FROM PartOfSpeech WHERE root LIKE ?";
	private static final String DELETE_FROM_FVRT = "DELETE FROM Favourites WHERE wordname = ? ;";
	private static final String GET_FVRT_WORDS = "SELECT f.wordname AS word, " + "       um.meaning AS urduMeaning, "
			+ "       pm.meaning AS persianMeaning " + "FROM favourites f " + "JOIN words w ON f.wordid = w.id "
			+ "LEFT JOIN urdumeaning um ON w.id = um.word_id " + "LEFT JOIN persianmeaning pm ON w.id = pm.word_id";

	private static final String SELECT_FOR_SUGGESTIONS = "SELECT word FROM Words WHERE word LIKE ?";
	private static final String INSERT_INTO_HISTORY = "INSERT INTO search_history (searched_word, meaning) VALUES (?, ?)";

	@Override
	public List<String> getSuggestions(String prefix) {
		List<String> suggestions = new ArrayList<>();

		logger.info("getSuggestions method called with prefix: {}", prefix);

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement preparedStatement = conn.prepareStatement(SELECT_FOR_SUGGESTIONS)) {

			preparedStatement.setString(1, prefix + "%");
			logger.debug("Prepared statement created with query: {}", SELECT_FOR_SUGGESTIONS);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				suggestions.add(resultSet.getString("word"));
			}
		} catch (SQLException e) {
			logger.error("SQL exception occurred while fetching suggestions", e);
		}
		logger.info("getSuggestions method completed, returning {} suggestions", suggestions.size());
		return suggestions;
	}

	@Override
	public boolean saveSearchHistory(Word word) {
		logger.info("saveSearchHistory method called for word: {}", word.getWord());

		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = connection.prepareStatement(INSERT_SEARCH_HISTORY_SQL)) {

			stmt.setString(1, word.getWord());
			stmt.setString(2, "word");
			logger.debug("Prepared statement created with SQL: {}", INSERT_SEARCH_HISTORY_SQL);

			int rowsAffected = stmt.executeUpdate();
			logger.info("Rows affected: {}", rowsAffected);

			return rowsAffected > 0;

		} catch (SQLException e) {
			logger.error("SQL exception occurred while saving search history for word: {}", word.getWord(), e);
			return false;
		}
	}

	@Override
	public void addWordToSearchHistory(String searchTerm, String meaning) {
		logger.info("addWordToSearchHistory method called with searchTerm: {}, meaning: {}", searchTerm, meaning);

		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); // 1
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_HISTORY)) { // 1
			logger.debug("Database connection established and prepared statement created.");

			preparedStatement.setString(1, searchTerm); // 2
			preparedStatement.setString(2, meaning); // 2
			logger.debug("Set parameters for prepared statement: searchTerm = {}, meaning = {}", searchTerm, meaning);

			preparedStatement.executeUpdate(); // 2
			logger.info("Search history added successfully for searchTerm: {}", searchTerm);
		} catch (SQLException e) {
			logger.error("SQL exception occurred while adding word to search history: searchTerm = {}, meaning = {}",
					searchTerm, meaning, e);
		}
	}

	@Override
	public boolean markAsFavorite(String wordname) {
		logger.info("markAsFavorite method called for word: {}", wordname);
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			logger.debug("Connection established to the database.");
			int wordId = getWordId(connection, wordname);
			if (wordId == -1) {
				logger.warn("Word not found in database: {}", wordname);
				return false;
			}

			preparedStatement = connection.prepareStatement(INSERT_FAVORITES_SQL);
			preparedStatement.setInt(1, wordId);
			preparedStatement.setString(2, wordname);
			logger.debug("Prepared statement created with SQL: {}", INSERT_FAVORITES_SQL);

			int rowsAffected = preparedStatement.executeUpdate();
			logger.info("Rows affected by the insert operation: {}", rowsAffected);

			return rowsAffected > 0;

		} catch (SQLException e) {
			logger.error("SQL exception occurred while marking word as favorite: {}", wordname, e);
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
					logger.debug("PreparedStatement closed.");
				}
				if (connection != null) {
					connection.close();
					logger.debug("Database connection closed.");
				}
			} catch (SQLException e) {
				logger.error("SQL exception occurred while closing resources.", e);
			}
		}

		return false;
	}

	@Override
	public List<Word> getWordsByRoot(String root) { // 1
		List<Word> wordList = new ArrayList<>(); // 1
		logger.info("Entering getWordsByRoot method with root: {}", root);
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); // 2
				PreparedStatement preparedStatement = connection.prepareStatement(GET_WORDS_BY_ROOT_SQL)) { // 2
			preparedStatement.setString(1, "%" + root + "%"); // 3
			try (ResultSet resultSet = preparedStatement.executeQuery()) { // 4
				while (resultSet.next()) { // 5
					String word = resultSet.getString("word"); // 6
					String urduMeaning = resultSet.getString("urduMeaning"); // 6
					String persianMeaning = resultSet.getString("persianMeaning"); // 6
					Word wordObj = new Word(word, urduMeaning, persianMeaning); // 6
					wordList.add(wordObj);
				}
			}
		} catch (SQLException e) { // 7
			logger.error("Error while getting words by root: {}", root, e);
		}

		logger.info("Exiting getWordsByRoot method, found {} words", wordList.size());
		return wordList; // 8
	}

	@Override
	public List<Word> getFavoriteWords() { // 1
		List<Word> favoriteWords = new ArrayList<>(); // 1
		logger.info("Entering getFavoriteWords method"); // Log method entry

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); // 2
				PreparedStatement stmt = conn.prepareStatement(GET_FVRT_WORDS)) { // 2
			ResultSet rs = stmt.executeQuery(); // 3
			while (rs.next()) { // 4
				String wordname = rs.getString("word"); // 5
				String urduMeaning = rs.getString("urduMeaning"); // 5
				String persianMeaning = rs.getString("persianMeaning"); // 5
				Word word = new Word(wordname, persianMeaning, urduMeaning); // 5
				favoriteWords.add(word); // 5
			}
		} catch (SQLException e) { // 6
			logger.error("Error while getting favorite words", e);
		}

		logger.info("Exiting getFavoriteWords method, found {} favorite words", favoriteWords.size());
		return favoriteWords; // 7
	}

	@Override
	public List<String> getRootSuggestions(String prefix) {
		if (prefix == null || prefix.trim().isEmpty()) {
			logger.warn("Prefix is null or empty, returning empty suggestions list.");
			return new ArrayList<>();
		}

		List<String> suggestions = new ArrayList<>();
		logger.info("Fetching root suggestions for prefix: {}", prefix);

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement preparedStatement = conn.prepareStatement(GET_ROOT_FROM_POS)) {

			preparedStatement.setString(1, prefix + "%");
			logger.debug("Prepared SQL query with prefix: {}", prefix);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					suggestions.add(resultSet.getString("root"));
					logger.debug("Added root suggestion: {}", resultSet.getString("root"));
				}
			}
		} catch (SQLException e) {
			logger.error("SQL Exception while fetching root suggestions for prefix: {}", prefix, e);
		}

		logger.info("Exiting getRootSuggestions method for prefix: {}", prefix);
		return suggestions;
	}

	public boolean deleteFavoriteWord(String word) {
		logger.info("Attempting to delete favorite word: {}", word);

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(DELETE_FROM_FVRT)) {

			stmt.setString(1, word);
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				logger.info("Successfully deleted favorite word: {}", word);
				return true;
			} else {
				logger.warn("No rows affected when attempting to delete favorite word: {}", word);
				return false;
			}
		} catch (SQLException e) {
			logger.error("SQLException occurred while attempting to delete favorite word: {}", word, e);
			return false;
		}
	}

	@Override
	public List<String> segmentWordWithDiacritics(String word) {
		logger.info("Segmenting word with diacritics: {}", word);

		Map<Integer, Character> diacriticalMarks = new HashMap<>();
		StringBuilder strippedWord = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (isDiacriticalMark(c)) {
				diacriticalMarks.put(strippedWord.length() - 1, c);
			} else {
				strippedWord.append(c);
			}
		}

		try {
			List<String> segmentedWords = farasaSegmenter.segmentLine(strippedWord.toString());
			List<String> segmentedWithDiacritics = reapplyDiacritics(segmentedWords, diacriticalMarks);
			logger.info("Successfully segmented word: {}", word); // Log success
			return splitByCommasAndPlus(segmentedWithDiacritics);
		} catch (Exception e) {
			logger.error("Error during segmentation of word: {}. Error message: {}", word, e.getMessage(), e);
			return null;
		}
	}

	private boolean isDiacriticalMark(char c) {
		return (c >= 0x064B && c <= 0x0652);
	}

	private List<String> reapplyDiacritics(List<String> segmentedWords, Map<Integer, Character> diacriticalMarks) {
		List<String> result = new ArrayList<>();
		StringBuilder currentSegment = new StringBuilder();
		int positionInOriginal = 0;

		for (String segment : segmentedWords) {
			currentSegment.setLength(0);
			for (int i = 0; i < segment.length(); i++) {
				char c = segment.charAt(i);
				currentSegment.append(c);
				if (diacriticalMarks.containsKey(positionInOriginal)) {
					currentSegment.append(diacriticalMarks.get(positionInOriginal));
				}
				positionInOriginal++;
			}

			result.add(currentSegment.toString());
		}

		return result;
	}

	private List<String> splitByCommasAndPlus(List<String> segmentedWords) {
		List<String> result = new ArrayList<>();
		for (String segment : segmentedWords) {
			String[] parts = segment.split("[,+]");
			result.addAll(Arrays.asList(parts));
		}

		return result;
	}

	@Override
	public boolean isWordInFavourites(String word) {
		logger.info("Checking if word is in favourites: {}", word);
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(FAVOURITE_WORD_EXIST_SQL)) {

			stmt.setString(1, word);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					boolean isFavourite = rs.getInt(1) > 0;
					logger.info("Word {} is {} in favourites", word, isFavourite ? "already" : "not");
					return isFavourite;
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException occurred while checking if word {} is in favourites", word, e);
		}
		return false;
	}

	public List<String[]> getPosDetails(String arabicWord) {
		logger.info("Fetching POS details for word: {}", arabicWord);
		List<String[]> posDetailsList = new ArrayList<>();

		if (wordExists(arabicWord) && arabicWord != null && !arabicWord.isEmpty()) {
			try {
				File jarFile = new File("/path/to/your/AlKhalil-2.1.21.jar");
				try (URLClassLoader classLoader = new URLClassLoader(new URL[] { jarFile.toURI().toURL() })) {
					logger.info("Loading AlKhalil POS Tagger from JAR file: {}", jarFile.getAbsolutePath());
					Class<?> posTaggerClass = classLoader.loadClass("AlKhalil2.AnalyzedWords");
					Object posTaggerInstance = posTaggerClass.getDeclaredConstructor().newInstance();
					Method tagMethod = posTaggerClass.getMethod("analyzedWords", String.class);
					LinkedList<?> posTaggedResult = (LinkedList<?>) tagMethod.invoke(posTaggerInstance, arabicWord);

					logger.info("Successfully tagged word {} with POS details", arabicWord);
					for (Object result : posTaggedResult) {
						String voweledWord = (String) result.getClass().getMethod("getVoweledWord").invoke(result);
						String stem = (String) result.getClass().getMethod("getStem").invoke(result);
						String pos = (String) result.getClass().getMethod("getWordType").invoke(result);
						String root = (String) result.getClass().getMethod("getWordRoot").invoke(result);
						posDetailsList.add(new String[] { voweledWord, stem, pos, root });
					}
					savePosDetails(arabicWord, posDetailsList);
				}
			} catch (Exception e) {
				logger.error("Error while fetching POS details for word: {}", arabicWord, e);
			}
		} else {
			logger.warn("Word {} does not exist or is invalid for POS tagging", arabicWord);
		}

		return posDetailsList;
	}

	public boolean savePosDetails(String arabicWord, String voweled, String stem, String pos, String root) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			logger.info("Saving POS details for word: {}", arabicWord);
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			int wordId = getWordId(connection, arabicWord);

			if (wordId == -1) {
				logger.warn("Word ID not found for: {}. POS details will not be saved.", arabicWord);
				return false;
			}

			preparedStatement = connection.prepareStatement(INSERT_PART_OF_SPEECH_SQL);
			preparedStatement.setInt(1, wordId);
			preparedStatement.setString(2, voweled);
			preparedStatement.setString(3, stem);
			preparedStatement.setString(4, pos);
			preparedStatement.setString(5, root);
			preparedStatement.executeUpdate();

			logger.info("POS details for word {} successfully saved.", arabicWord);
			return true;
		} catch (SQLException e) {
			logger.error("SQLException occurred while saving POS details for word: {}", arabicWord, e);
			return false;
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("SQLException occurred while closing resources for saving POS details of word: {}",
						arabicWord, e);
			}
		}
	}

	public void savePosDetails(String arabicWord, List<String[]> posDetailsList) {
		logger.info("Saving POS details for word: {}. Number of POS entries: {}", arabicWord, posDetailsList.size());

		for (String[] posDetails : posDetailsList) {
			String voweled = posDetails[0];
			String stem = posDetails[1];
			String pos = posDetails[2];
			String root = posDetails[3];
			boolean isSaved = savePosDetails(arabicWord, voweled, stem, pos, root);
			if (isSaved) {
				logger.info("Successfully saved POS detail for word: {} - POS: {}", arabicWord, pos);
			} else {
				logger.error("Failed to save POS detail for word: {} - POS: {}", arabicWord, pos);
			}
		}
	}

	@Override
	public boolean addWordToDB(Word word) {
		Connection connection = null;
		PreparedStatement wordStmt = null;
		PreparedStatement urduStmt = null;
		PreparedStatement persianStmt = null;
		try {
			logger.info("Adding word to database: {}", word.getWord());
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			connection.setAutoCommit(false);

			wordStmt = connection.prepareStatement(INSERT_WORD_SQL, Statement.RETURN_GENERATED_KEYS);
			wordStmt.setString(1, word.getWord());
			wordStmt.executeUpdate();
			ResultSet generatedKeys = wordStmt.getGeneratedKeys();
			int wordId;
			if (generatedKeys.next()) {
				wordId = generatedKeys.getInt(1);
				logger.info("Word inserted successfully with ID: {}", wordId);
			} else {
				throw new SQLException("Failed to insert word, no ID obtained.");
			}

			urduStmt = connection.prepareStatement(INSERT_URDU_MEANING_SQL);
			urduStmt.setInt(1, wordId);
			urduStmt.setString(2, word.getUrduMeaning());
			urduStmt.executeUpdate();
			logger.info("Urdu meaning inserted for word: {}", word.getWord());

			persianStmt = connection.prepareStatement(INSERT_PERSIAN_MEANING_SQL);
			persianStmt.setInt(1, wordId);
			persianStmt.setString(2, word.getPersianMeaning());
			persianStmt.executeUpdate();
			logger.info("Persian meaning inserted for word: {}", word.getWord());

			connection.commit();
			logger.info("Transaction committed successfully.");
			return true;
		} catch (SQLException e) {
			logger.error("Error while adding word to database: {}", e.getMessage(), e);
			try {
				if (connection != null) {
					connection.rollback();
					logger.warn("Transaction rolled back due to an error.");
				}
			} catch (SQLException ex) {
				logger.error("Error during rollback: {}", ex.getMessage(), ex);
			}
			return false;
		} finally {
			try {
				if (wordStmt != null)
					wordStmt.close();
				if (urduStmt != null)
					urduStmt.close();
				if (persianStmt != null)
					persianStmt.close();
				if (connection != null)
					connection.close();
				logger.info("Database resources cleaned up.");
			} catch (SQLException e) {
				logger.error("Error closing database resources: {}", e.getMessage(), e);
			}
		}
	}

	@Override
	public boolean updateWordInDB(Word w) {
		Connection connection = null;
		PreparedStatement urduStmt = null;
		PreparedStatement persianStmt = null;
		logger.info("Updating word in database: {}", w.getWord());

		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			connection.setAutoCommit(false);
			int wordId = getWordId(connection, w.getWord());
			if (wordId == -1) {
				logger.warn("Word not found in database: {}", w.getWord());
				return false;
			}

			urduStmt = connection.prepareStatement(UPDATE_URDU_MEANING_SQL);
			urduStmt.setString(1, w.getUrduMeaning());
			urduStmt.setInt(2, wordId);
			logger.debug("Executing update for Urdu meaning: {}", w.getUrduMeaning());
			urduStmt.executeUpdate();
			persianStmt = connection.prepareStatement(UPDATE_PERSIAN_MEANING_SQL);
			persianStmt.setString(1, w.getPersianMeaning());
			persianStmt.setInt(2, wordId);
			logger.debug("Executing update for Persian meaning: {}", w.getPersianMeaning());
			persianStmt.executeUpdate();
			connection.commit();
			logger.info("Successfully updated word: {}", w.getWord());
			return true;

		} catch (SQLException e) {
			logger.error("SQLException occurred while updating word: {}", w.getWord(), e);
			try {
				if (connection != null)
					connection.rollback();
			} catch (SQLException ex) {
				logger.error("SQLException occurred during rollback: {}", ex.getMessage(), ex);
			}
			return false;
		} finally {
			try {
				if (urduStmt != null)
					urduStmt.close();
				if (persianStmt != null)
					persianStmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				logger.error("SQLException occurred during resource cleanup: {}", e.getMessage(), e);
			}
		}
	}

	@Override
	public List<Word> getAllWordsFromDB() {
		List<Word> wordList = new ArrayList<>();
		logger.info("Fetching all words from database.");

		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_WORDS_SQL);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				String word = resultSet.getString("word");
				String urduMeaning = resultSet.getString("urduMeaning");
				String persianMeaning = resultSet.getString("persianMeaning");
				Word wordObj = new Word(word, urduMeaning, persianMeaning);
				wordList.add(wordObj);
				logger.debug("Fetched word: {}, Urdu: {}, Persian: {}", word, urduMeaning, persianMeaning);
			}

		} catch (SQLException e) {
			logger.error("SQLException occurred while fetching words from database", e);
		}

		logger.info("Returning list of all words."); // Log method exit
		return wordList;
	}

	private int getWordId(Connection connection, String word) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(GET_WORD_ID_SQL)) {
			stmt.setString(1, word);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("id");
				}
			}
		}
		return -1;
	}

	@Override
	public boolean removeWordFromDB(String word) {
		logger.info("Attempting to remove word from database: {}", word);
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_WORD_SQL)) {

			preparedStatement.setString(1, word);
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				logger.info("Successfully removed word: {}", word);
				return true;
			} else {
				logger.warn("No word found to remove: {}", word);
				return false;
			}

		} catch (SQLException e) {
			logger.error("SQLException occurred while removing word: {}", word, e);
			return false;
		}
	}

	@Override
	public boolean saveOrUpdateWord(Word w) {
		logger.info("Attempting to save or update word: {}", w.getWord());
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
			if (wordExists(w.getWord())) {
				logger.info("Word exists, updating: {}", w.getWord());
				return updateWordInDB(w);
			} else {
				logger.info("Word does not exist, adding: {}", w.getWord());
				return addWordToDB(w);
			}
		} catch (Exception e) {
			logger.error("Error while saving or updating word: {}", w.getWord(), e);
		}
		return false;
	}

	@Override
	public boolean wordExists(String word) {
		logger.info("Checking if word exists: {}", word);
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(WORD_EXIST_SQL)) {
			stmt.setString(1, word);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					boolean exists = rs.getInt(1) > 0;
					logger.info("Word {} exists: {}", word, exists);
					return exists;
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException occurred while checking if word exists: {}", word, e);
		}
		return false;
	}

	@Override
	public List<Word> importFile(File file) {
		logger.info("Importing file: {}", file.getName());
		List<Word> importedWords = new ArrayList<>();
		try {
			importedWords = processFile(file);
			logger.info("Imported {} words from file: {}", importedWords.size(), file.getName());
		} catch (Exception e) {
			logger.error("Error while importing file: {}", file.getName(), e);
		}
		return importedWords;
	}

	private List<Word> processFile(File file) {
		logger.info("Processing file: {}", file.getName());
		List<Word> importedWords = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",");
				if (data.length == 3) {
					Word word = new Word(data[0].trim(), data[1].trim(), data[2].trim());
					logger.info("Processing word: {}", word.getWord());
					if (saveOrUpdateWord(word)) {
						importedWords.add(word);
						logger.info("Successfully processed word: {}", word.getWord());
					} else {
						logger.warn("Failed to save or update word: {}", word.getWord());
					}
				}
			}
		} catch (IOException e) {
			logger.error("IOException occurred while processing file: {}", file.getName(), e);
		}
		return importedWords;
	}

	@Override
	public Word getWordByUrduMeaningFromDB(String urduMeaning) {
		logger.info("Fetching word by Urdu meaning: {}", urduMeaning);
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = connection.prepareStatement(GET_WORD_BY_URDU_MEANING_SQL)) {

			stmt.setString(1, urduMeaning);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					String word = rs.getString("word");
					String urdu = rs.getString("urduMeaning");
					String persian = rs.getString("persianMeaning");
					logger.info("Found word by Urdu meaning: {} -> {}", urduMeaning, word);
					return new Word(word, urdu, persian);
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException occurred while fetching word by Urdu meaning: {}", urduMeaning, e);
		}
		return null;
	}

	@Override
	public Word getWordByPersianMeaningFromDB(String persianMeaning) {
		logger.info("Fetching word by Persian meaning: {}", persianMeaning);

		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = connection.prepareStatement(GET_WORD_BY_PERSIAN_MEANING_SQL)) {

			stmt.setString(1, persianMeaning);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					String word = rs.getString("word");
					String urdu = rs.getString("urduMeaning");
					String persian = rs.getString("persianMeaning");

					logger.info("Found word by Persian meaning: {} - Word: {}, Urdu: {}, Persian: {}", persianMeaning,
							word, urdu, persian);
					return new Word(word, urdu, persian);
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException occurred while fetching word by Persian meaning: {}", persianMeaning, e);
		}
		return null;
	}

	@Override
	public Word getWordByWordFromDB(String word) {
		logger.info("Fetching word by word: {}", word);

		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = connection.prepareStatement(GET_WORD_BY_WORD_SQL)) {

			stmt.setString(1, word);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					String fetchedWord = rs.getString("word");
					String urduMeaning = rs.getString("urduMeaning");
					String persianMeaning = rs.getString("persianMeaning");

					logger.info("Found word by word: {} - Word: {}, Urdu: {}, Persian: {}", word, fetchedWord,
							urduMeaning, persianMeaning);
					return new Word(fetchedWord, urduMeaning, persianMeaning);
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException occurred while fetching word by word: {}", word, e);
		}
		return null;
	}

	@Override
	public List<Word> getSearchHistoryFromDB() {
		logger.info("Fetching search history from database.");

		List<Word> searchHistory = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(GET_SEARCH_HISTORY_SQL);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				String searchedWord = resultSet.getString("searched_word");
				String meaning = resultSet.getString("meaning");
				Timestamp timestamp = resultSet.getTimestamp("timestamp");
				Word word = new Word(searchedWord, meaning, timestamp.toString());
				searchHistory.add(word);

				logger.debug("Fetched search history entry: Word: {}, Meaning: {}, Timestamp: {}", searchedWord,
						meaning, timestamp);
			}
		} catch (SQLException e) {
			logger.error("SQLException occurred while fetching search history from database.", e);
		}

		logger.info("Returning search history of size: {}", searchHistory.size());
		return searchHistory;
	}

}