package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.CustomDictionaryDAO;
import dto.Word;
import dto.Dictionary;

public class CustomDictionaryDAOTest {

    private CustomDictionaryDAO dao;

    @BeforeEach
    public void setUp() throws Exception {
        dao = new CustomDictionaryDAO(); // Assume constructor does not throw, handle it if needed.
        /*try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary_test", "root", "password");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS custom_dictionaries, dictionary_words, words");
            stmt.execute("CREATE TABLE custom_dictionaries (dictionary_id INT AUTO_INCREMENT PRIMARY KEY, dictionary_name VARCHAR(255) NOT NULL)");
            stmt.execute("CREATE TABLE words (id INT AUTO_INCREMENT PRIMARY KEY, word VARCHAR(255) NOT NULL)");
            stmt.execute("CREATE TABLE dictionary_words (dictionary_id INT, word_id INT, PRIMARY KEY(dictionary_id, word_id))");
            stmt.execute("INSERT INTO words (word) VALUES ('testWord1'), ('testWord2')");
        }*/
    }
    public void tearDown() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary_test", "root", "password");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS custom_dictionaries, dictionary_words, words");
        }
    }
    @Test
    public void testAddCustomDictionary_WithValidWords() throws SQLException {
        assertTrue(dao.addCustomDictionary("TestDict", "C:\\Users\\Cp9-30\\Desktop\\5th sem\\New Text Document.txt"));
    }
    @Test
    public void testAddCustomDictionary_NoWordsFound() throws SQLException {
        assertFalse(dao.addCustomDictionary("EmptyDict", "C:\\Users\\Cp9-30\\Desktop\\5th sem\\blankFile.txt"));
    }
    @Test
    public void testGetAllDictionaries() throws SQLException {
        dao.addCustomDictionary("TestDict", "C:\\Users\\Cp9-30\\Desktop\\5th sem\\New Text Document.txt");
        List<Dictionary> dictionaries = dao.getAllDictionaries();
        assertFalse(dictionaries.isEmpty());
    }
    @Test
    public void testGetWordsByDictionary_ExistingDictionary() throws SQLException {
        dao.addCustomDictionary("TestDict", "C:\\Users\\Cp9-30\\Desktop\\5th sem\\New Text Document.txt");
        List<Word> words = dao.getWordsByDictionary("TestDict");
        assertFalse(words.isEmpty());
    }
    @Test
    public void testDeleteDictionary_ExistingDictionary() throws SQLException {
        dao.addCustomDictionary("TestDict", "C:\\Users\\Cp9-30\\Desktop\\5th sem\\New Text Document.txt");
        assertTrue(dao.deleteDictionary("TestDict"));
    }
    @Test
    public void testDeleteDictionary_NonExistingDictionary() throws SQLException {
        assertFalse(dao.deleteDictionary("NonExistentDict"));
    }
}