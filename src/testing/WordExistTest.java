package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dal.WordDAO;
import dto.Word;

public class WordExistTest {

    private WordDAO dao;

    @BeforeEach
    public void setUp() throws Exception {
        dao = new WordDAO();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary_test", "root", "");
                Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM words");
            stmt.execute("ALTER TABLE words AUTO_INCREMENT = 1");
            stmt.execute("INSERT INTO words (word) VALUES ('existingword')");
        }
    }

    @Test
    public void testAddWordToDB_NewWord() throws SQLException {
        Word newWord = new Word("example", "نمونہ", "مثال");
        assertTrue(dao.addWordToDB(newWord), "Word should be added successfully to the database.");
        assertTrue(dao.wordExists("example"), "The word 'example' should exist in the database.");
    }

    @Test
    public void testAddWordToDB_ExistingWord() throws SQLException {
        Word newWord = new Word("existingword", "موجود", "كلمة");
        assertTrue(dao.addWordToDB(newWord), "Word should be added successfully to the database.");
        assertTrue(dao.wordExists("existingword"), "The word 'existingword' should exist in the database.");
    }

    @Test
    public void testWordExists_NonExistentWord() {
        assertFalse(dao.wordExists("nonexistentword"), "The word 'nonexistentword' should not exist in the database.");
    }
}
