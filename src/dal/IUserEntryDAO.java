package dal;

import dto.User;

public interface IUserEntryDAO {
	boolean authenticateUser(User u);
	boolean addUserCredentials(User u);
}
