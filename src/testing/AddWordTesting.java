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

public class AddWordTesting {

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
             stmt.execute("ALTER TABLE persianmeaning AUTO_INCREMENT = 1"); }
    }

    @Test
    public void testAddWordToDB_NewWord() throws SQLException {
        Word word = new Word("example", "نمونہ", "مثال");
        assertTrue(dao.addWordToDB(word));
    }

    @Test
    public void testAddWordToDB_DuplicateWord() throws SQLException {
        Word word = new Word("example", "نمونہ", "مثال");
        dao.addWordToDB(word);
        assertTrue(dao.addWordToDB(word));
    }

    @Test
    public void testAddWordToDB_NullWord() {
        assertThrows(IllegalArgumentException.class, ()
        		-> dao.addWordToDB(new Word(null, "meaning", "meaning")));
    }

    @Test
    public void testAddWordToDB_EmptyWord() {
        assertThrows(IllegalArgumentException.class, () 
        		-> dao.addWordToDB(new Word("", "meaning", "meaning")));
    }

    @Test
    public void testAddWordToDB_NullMeanings() {
        assertThrows(IllegalArgumentException.class, ()
        		-> dao.addWordToDB(new Word("word", null, null)));
    }

    @Test
    public void testAddWordToDB_PartialMeaning() {
        Word word = new Word("partial", "معنی", null);
        assertTrue(dao.addWordToDB(word));
    }    
}
