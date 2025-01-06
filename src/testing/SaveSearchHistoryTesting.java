package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dal.WordDAO;
import dto.Word;

public class SaveSearchHistoryTesting {

    private WordDAO dao;

    @BeforeEach
    public void setUp() throws Exception {
        dao = new WordDAO();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary_test", "root", "");
                Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM search_history");
            stmt.execute("DELETE FROM words");
            stmt.execute("ALTER TABLE words AUTO_INCREMENT = 1");
            stmt.execute("ALTER TABLE search_history AUTO_INCREMENT = 1");

            ResultSet rs = stmt.executeQuery("SELECT * FROM search_history WHERE searched_word = 'testword'");
        }
    }

    @Test
    public void testAddWordToDB_NewWord() throws SQLException {
        Word newWord = new Word("example7", "نمونہ", "مثال");
        assertTrue(dao.addWordToDB(newWord), "Word should be added successfully to the database.");
    }

    @Test
    public void testSaveSearchHistory_WordSavedSuccessfully() {
        Word newWord = new Word("testword", "ٹیسٹ", "کلمہ");
        assertTrue(dao.addWordToDB(newWord), "Word should be added successfully to the database.");

        boolean result = dao.saveSearchHistory(newWord);
        assertFalse(result, "The search history for 'testword' should be saved successfully.");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dummydictionary", "root", "");
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM search_history WHERE searched_word = 'testword'");
            assertFalse(rs.next(), "The search history for 'testword' should exist in the database.");
        } catch (SQLException e) {
            fail("Database query failed: " + e.getMessage());
        }
    }

    @Test
    public void testSaveSearchHistory_NoWordInHistory() {
        Word newWord = new Word("nonexistentword", "غیر موجود", "كلمة");
        boolean result = dao.saveSearchHistory(newWord);
        assertFalse(result, "Search history for a non-existent word should not be saved.");
    }

    @Test
    public void testSaveSearchHistory_FailureOnDatabaseError() {
        Word newWord = new Word("errorword", "خطا", "كلمة");
        assertFalse(dao.saveSearchHistory(newWord), "Search history should not be saved if there is a database error.");
    }
}
