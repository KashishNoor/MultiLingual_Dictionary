package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dal.WordDAO;
import dto.Word;

public class GetWordByWordFromDBTesting {

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
        Word word1 = new Word("example1", "نمونہ", "مثال");
        Word word2 = new Word("example2", "نمونہ", "مثال");
        assertTrue(dao.addWordToDB(word));
    }

    @Test
    public void testGetWordByWordFromDB_ValidWord() {
        Word word = dao.getWordByWordFromDB("example");
        assertNotNull(word, "The word should not be null.");
        assertEquals("example", word.getWord());
        assertEquals("نمونہ", word.getUrduMeaning());
        assertEquals("مثال", word.getPersianMeaning());
    }

    @Test
    public void testGetWordByWordFromDB_NoMatchingWord() {
        Word word = dao.getWordByWordFromDB("nonexistentWord");
        assertNull(word, "The word should be null when no matching word is found.");
    }

    @Test
    public void testGetWordByWordFromDB_EmptyWord() {
        Word word = dao.getWordByWordFromDB("");
        assertNull(word, "The word should be null when an empty word is provided.");
    }

    @Test
    public void testGetWordByWordFromDB_NullWord() {
        Word word = dao.getWordByWordFromDB(null);
        assertNull(word, "The word should be null when a null word is provided.");
    }
}
