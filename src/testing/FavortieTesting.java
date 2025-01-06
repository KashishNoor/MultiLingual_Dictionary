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

public class FavortieTesting {

    private WordDAO dao;

    @BeforeEach
    public void setUp() throws Exception {
        dao = new WordDAO();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary_test", "root", "");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM persianmeaning");
            stmt.execute("DELETE FROM urdumeaning");
            stmt.execute("DELETE FROM words");
            stmt.execute("DELETE FROM favourites");
            
            stmt.execute("ALTER TABLE words AUTO_INCREMENT = 1");
            stmt.execute("ALTER TABLE urdumeaning AUTO_INCREMENT = 1");
            stmt.execute("ALTER TABLE persianmeaning AUTO_INCREMENT = 1");
            stmt.execute("ALTER TABLE favourites AUTO_INCREMENT = 1");
         
        }
    }

    @Test
    public void testAddWordToDB_NewWord() throws SQLException {
        Word word = new Word("example", "نمونہ", "مثال");
        assertTrue(dao.addWordToDB(word));
    }

    @Test
    public void testMarkAsFavorite_ValidWord() {
        Word word = new Word("example", "نمونہ", "مثال");
        dao.addWordToDB(word); 
        boolean result = dao.markAsFavorite("example");
        assertTrue(result, "Word should be marked as favorite.");
    }

    @Test
    public void testMarkAsFavorite_NonExistentWord() {
        boolean result = dao.markAsFavorite("nonexistentword");
        assertFalse(result, "Word should not be marked as favorite because it doesn't exist in the database.");
    }

 
}
