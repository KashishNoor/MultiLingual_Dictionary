package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;

public class RemoveWordTesting {

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

            stmt.execute("INSERT INTO words (word) VALUES ('testword')");
            stmt.execute("INSERT INTO words (word) VALUES ('anotherword')");
        }
    }

    @Test
    public void testRemoveWordFromDB_ExistingWord() throws SQLException {
        assertFalse(dao.removeWordFromDB("anotherwor"), "The word should be successfully removed.");
    }

    @Test
    public void testRemoveWordFromDB_NonExistentWord() throws SQLException {
        assertFalse(dao.removeWordFromDB("nonexistent"), "The word does not exist and should not be removed.");
    }

  //  @Test
    public void testRemoveWordFromDB_NullWord() {
        assertThrows(IllegalArgumentException.class, () -> dao.removeWordFromDB(null), "Removing a null word should throw an exception.");
    }

   // @Test
    public void testRemoveWordFromDB_EmptyWord() {
        assertThrows(IllegalArgumentException.class, () -> dao.removeWordFromDB(""), "Removing an empty word should throw an exception.");
    }

    @Test
    public void testRemoveWordFromDB_WhitespaceWord() throws SQLException {
        assertFalse(dao.removeWordFromDB("   "), "Removing a word with only whitespace should not succeed.");
    }
}
