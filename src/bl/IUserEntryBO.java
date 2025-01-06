package bl;

import dto.User;

public interface IUserEntryBO {
	String loginUser(User u);
	String registerUser(User u);
}


