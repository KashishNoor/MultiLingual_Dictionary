package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import co.ConfigReader;
import dto.User;

public class UserEntryDAO implements IUserEntryDAO{
	private static ConfigReader reader = new ConfigReader();
	private static final String URL = reader.getUrl();
	private static final String USER = reader.getUser();
	private static final String PASSWORD = reader.getPassword();
    public boolean authenticateUser(User u) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        boolean isAuthenticated = false;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, u.getUsername());
            pstmt.setString(2, u.getPassword());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    isAuthenticated = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAuthenticated;
    }
    public boolean addUserCredentials(User u) {
    	if(checkIfUserExists(u.getUsername()))
    	{
    		return false;
    	}
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        boolean isUserAdded = false;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, u.getUsername());
            pstmt.setString(2, u.getPassword());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                isUserAdded = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUserAdded;
    }
    public boolean checkIfUserExists(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        boolean userExists = false;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userExists = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userExists;
    }
}