package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;
import dto.Word;

public class AddSearchHistoryTest {

    private WordDAO dao;
    
    @BeforeEach
    public void setUp() throws Exception {
        dao = new WordDAO();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary_test", "root", "");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM persianmeaning");
            stmt.execute("DELETE FROM urdumeaning");
            stmt.execute("DELETE FROM words");
            stmt.execute("DELETE FROM search_history");
            
            stmt.execute("ALTER TABLE words AUTO_INCREMENT = 1");
            stmt.execute("ALTER TABLE urdumeaning AUTO_INCREMENT = 1");
            stmt.execute("ALTER TABLE persianmeaning AUTO_INCREMENT = 1");
            stmt.execute("ALTER TABLE search_history AUTO_INCREMENT = 1");
        }
    }

    @Test
    public void testAddWordToDB_NewWord() throws SQLException {
        Word word = new Word("example", "نمونہ", "مثال");
        assertTrue(dao.addWordToDB(word));
    }
    
    @Test
    public void testAddWordToSearchHistory_ValidInput() throws SQLException {
       
        Word word = new Word("example", "نمونہ", "مثال");
        dao.addWordToDB(word);
        String searchTerm = "example";
        String meaning = "نمونہ";
        
        dao.addWordToSearchHistory(searchTerm, meaning);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dummydictionary", "root", "");
        	     PreparedStatement stmt = conn.prepareStatement("SELECT * FROM search_history WHERE searched_word = ? AND meaning = ?")) {
        	    stmt.setString(1, searchTerm);
        	    stmt.setString(2, meaning);
        	    ResultSet rs = stmt.executeQuery();  
        	    assertTrue(rs.next(), "The word should exist in the search history.");
        	} catch (SQLException e) {
        	    fail("Database query failed: " + e.getMessage());
        	}

    }

    @Test
    public void testAddWordToSearchHistory_NullSearchTerm() {
        assertThrows(IllegalArgumentException.class, () -> dao.addWordToSearchHistory(null, "meaning"));
    }

    @Test
    public void testAddWordToSearchHistory_EmptySearchTerm() {
        assertThrows(IllegalArgumentException.class, () -> dao.addWordToSearchHistory("", "meaning"));
    }

    @Test
    public void testAddWordToSearchHistory_NullMeaning() {
        assertThrows(IllegalArgumentException.class, () -> dao.addWordToSearchHistory("searchTerm", null));
    }

    @Test
    public void testAddWordToSearchHistory_EmptyMeaning() {
        assertThrows(IllegalArgumentException.class, () -> dao.addWordToSearchHistory("searchTerm", ""));
    }
}
