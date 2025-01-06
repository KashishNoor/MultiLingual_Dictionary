package testing;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.UserEntryDAO;
import dto.User;

public class UserEntryDAOTest {

    private UserEntryDAO dao = new UserEntryDAO();

    @BeforeEach
    public void setUp() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary_test", "root", "password");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS users");
            stmt.execute("CREATE TABLE users (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255));");
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary_test", "root", "password");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS users");
        }
    }

    @Test
    public void testAddUserCredentials_NewUser() throws SQLException {
        assertTrue(dao.addUserCredentials(new User("newUser", "password123")));
    }

    @Test
    public void testAddUserCredentials_ExistingUser() throws SQLException {
        dao.addUserCredentials(new User("newUser", "password123"));
        assertFalse(dao.addUserCredentials(new User("newUser", "password123")));
    }

    @Test
    public void testAuthenticateUser_ValidCredentials() throws SQLException {
        dao.addUserCredentials(new User("user", "pass"));
        assertTrue(dao.authenticateUser(new User("user", "pass")));
    }

    @Test
    public void testAuthenticateUser_InvalidPassword() throws SQLException {
        dao.addUserCredentials(new User("user", "pass"));
        assertFalse(dao.authenticateUser(new User("user", "wrongpass")));
    }

    @Test
    public void testAuthenticateUser_InvalidUsername() throws SQLException {
        assertFalse(dao.authenticateUser(new User("nobody", "pass")));
    }

    @Test
    public void testCheckIfUserExists_ExistingUser() throws SQLException {
        dao.addUserCredentials(new User("user", "pass"));
        assertTrue(dao.checkIfUserExists("user"));
    }

    @Test
    public void testCheckIfUserExists_NonExistingUser() throws SQLException {
        assertFalse(dao.checkIfUserExists("ghost"));
    }
}