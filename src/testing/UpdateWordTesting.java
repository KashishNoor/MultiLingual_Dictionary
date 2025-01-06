package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;
import dto.Word;

public class UpdateWordTesting {

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

          
            stmt.execute("INSERT INTO words (id, word) VALUES (1, 'example')");
            stmt.execute("INSERT INTO urdumeaning (word_id, meaning) VALUES (1, 'نمونہ')");
            stmt.execute("INSERT INTO persianmeaning (word_id, meaning) VALUES (1, 'مثال')");
        }
    }

    @Test
    public void testUpdateWordInDB_ValidWord() throws SQLException {
        Word word = new Word("example", "نیا معنی", "جدید مثال");
        assertTrue(dao.updateWordInDB(word), "The word should be updated successfully.");
    }

    @Test
    public void testUpdateWordInDB_NonExistentWord() throws SQLException {
        Word word = new Word("nonexistent", "معنی", "مثال");
        assertFalse(dao.updateWordInDB(word), "Updating a non-existent word should return false.");
    }

    @Test
    public void testUpdateWordInDB_NullWord() {
        assertThrows(IllegalArgumentException.class, () -> dao.updateWordInDB(null),
                "Updating a null word should throw IllegalArgumentException.");
    }

    @Test
    public void testUpdateWordInDB_NullMeanings() throws SQLException {
        Word word = new Word("example", null, null);
        assertTrue(dao.updateWordInDB(word), "The word with null meanings should be updated successfully.");
    }

    @Test
    public void testUpdateWordInDB_EmptyMeanings() throws SQLException {
        Word word = new Word("example", "", "");
        assertTrue(dao.updateWordInDB(word), "The word with empty meanings should be updated successfully.");
    }

    @Test
    public void testUpdateWordInDB_RollbackOnError() throws SQLException {
        Word word = new Word("example", "نمونہ", "مثال");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dummydictionary", "root", "");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE urdumeaning"); 
        } catch (SQLException e) {
        }

        assertFalse(dao.updateWordInDB(word), "The update should fail and changes should be rolled back.");
    }
}
