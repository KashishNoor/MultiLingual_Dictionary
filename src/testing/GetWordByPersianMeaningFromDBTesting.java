package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dal.WordDAO;
import dto.Word;

public class GetWordByPersianMeaningFromDBTesting {

    private WordDAO dao;

    @BeforeEach
    public void setUp() throws Exception {
        dao = new WordDAO();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary_test", "root", "");
             Statement stmt = conn.createStatement()) {
        	 stmt.execute("DELETE FROM persianmeaning");
             stmt.execute("DELETE FROM urdumeaning");
             stmt.execute("DELETE FROM words");

             stmt.execute("ALTER TABLE words AUTO_INCREMENT = 1");
             stmt.execute("ALTER TABLE urdumeaning AUTO_INCREMENT = 1");
             stmt.execute("ALTER TABLE persianmeaning AUTO_INCREMENT = 1");
        }
    }

    @Test
    public void testAddWordToDB_NewWord() throws SQLException {
        Word word = new Word("example", "نمونہ", "مثال");
        assertTrue(dao.addWordToDB(word));
    }

    @Test
    public void testGetWordByPersianMeaningFromDB_ValidPersianMeaning() {
        Word word = dao.getWordByPersianMeaningFromDB("مثال");
        assertNotNull(word, "The word should not be null.");
        assertEquals("example", word.getWord());
        assertEquals("نمونہ", word.getUrduMeaning());
        assertEquals("مثال", word.getPersianMeaning());
    }

    @Test
    public void testGetWordByPersianMeaningFromDB_NoMatchingPersianMeaning() {
        Word word = dao.getWordByPersianMeaningFromDB("nonexistentMeaning");
        assertNull(word, "The word should be null when no matching Persian meaning is found.");
    }

    @Test
    public void testGetWordByPersianMeaningFromDB_EmptyPersianMeaning() {
        Word word = dao.getWordByPersianMeaningFromDB("");
        assertNull(word, "The word should be null when an empty Persian meaning is provided.");
    }

    @Test
    public void testGetWordByPersianMeaningFromDB_NullPersianMeaning() {
        Word word = dao.getWordByPersianMeaningFromDB(null);
        assertNull(word, "The word should be null when a null Persian meaning is provided.");
    }
}
