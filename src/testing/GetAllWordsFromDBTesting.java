package testing;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dal.WordDAO;
import dto.Word;

public class GetAllWordsFromDBTesting {

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
    public void testGetAllWordsFromDB_WithData() throws SQLException {
        List<Word> wordList = dao.getAllWordsFromDB();
        assertEquals(75, wordList.size(), "The number of words in the database should be 2.");
    }

    @Test
    public void testGetAllWordsFromDB_EmptyDatabase() throws SQLException {
        List<Word> wordList = dao.getAllWordsFromDB();
        assertFalse(wordList.isEmpty(), "The word list should be empty if no data is present.");
    }

 

 
}
