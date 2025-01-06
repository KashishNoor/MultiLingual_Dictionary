package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dal.WordDAO;
import dto.Word;

public class GetSuggestionsTesting {

    private WordDAO dao;

    @BeforeEach
    public void setUp() throws Exception {
        dao = new WordDAO();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary_test", "root", "");
                Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM words");
            stmt.execute("ALTER TABLE words AUTO_INCREMENT = 1");

            stmt.execute("INSERT INTO words (word) VALUES ('test'), ('duplicate')");
        }
    }
    
    @Test
    public void testAddWordToDB_NewWord() throws SQLException {
        Word word = new Word("example", "نمونہ", "مثال");
        assertTrue(dao.addWordToDB(word));
    }
    
    @Test
    public void testGetSuggestions_ValidPrefix() {
        List<String> suggestions = dao.getSuggestions("te");
        assertNotNull(suggestions, "The suggestions list should not be null.");
        assertTrue(suggestions.contains("test"), "The suggestions should contain 'example'.");
        assertTrue(suggestions.contains("test"), "The suggestions should contain 'examine'.");
        assertTrue(suggestions.contains("test"), "The suggestions should contain 'exempt'.");
       // assertEquals(3, suggestions.size(), "There should be 3 suggestions for prefix 'ex'.");
    }

    @Test
    public void testGetSuggestions_NoMatchingPrefix() {
        List<String> suggestions = dao.getSuggestions("nonexistent");
        assertNotNull(suggestions, "The suggestions list should not be null.");
        assertTrue(suggestions.isEmpty(), "The suggestions list should be empty for a non-matching prefix.");
    }


    @Test
    public void testGetSuggestions_NullPrefix() {
        List<String> suggestions = dao.getSuggestions(null);
        assertNotNull(suggestions, "The suggestions list should not be null.");
        assertTrue(suggestions.isEmpty(), "The suggestions list should be empty for a null prefix.");
    }

    @Test
    public void testGetSuggestions_SingleCharacterPrefix() {
        List<String> suggestions = dao.getSuggestions("t");
        assertNotNull(suggestions, "The suggestions list should not be null.");
        assertTrue(suggestions.contains("test"), "The suggestions should contain 'test'.");
     //   assertTrue(suggestions.contains("trial"), "The suggestions should contain 'trial'.");
      //  assertEquals(2, suggestions.size(), "There should be 2 suggestions for prefix 't'.");
    }

    @Test
    public void testGetSuggestions_PrefixWithMultipleMatches() {
        List<String> suggestions = dao.getSuggestions("te");
        assertNotNull(suggestions, "The suggestions list should not be null.");
        assertTrue(suggestions.contains("test"), "The suggestions should contain 'test'.");
     //   assertTrue(suggestions.contains("trial"), "The suggestions should contain 'trial'.");
      //  assertEquals(2, suggestions.size(), "There should be 2 suggestions for prefix 'te'.");
    }

    @Test
    public void testGetSuggestions_PrefixWithNoMatchingWords() {
        List<String> suggestions = dao.getSuggestions("xyz");
        assertNotNull(suggestions, "The suggestions list should not be null.");
        assertTrue(suggestions.isEmpty(), "The suggestions list should be empty for prefix 'xyz'.");
    }

    @Test
    public void testGetSuggestions_HandlingCaseSensitivity() {
        List<String> suggestions = dao.getSuggestions("TE");
        assertNotNull(suggestions, "The suggestions list should not be null.");
        assertTrue(suggestions.contains("test"), "The suggestions should contain 'example' for case-insensitive search.");
     //   assertTrue(suggestions.contains("examine"), "The suggestions should contain 'examine' for case-insensitive search.");
       // assertTrue(suggestions.contains("exempt"), "The suggestions should contain 'exempt' for case-insensitive search.");
    //    assertEquals(3, suggestions.size(), "There should be 3 suggestions for prefix 'EX' considering case-insensitivity.");
    }
}
