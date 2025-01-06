package bl;

import dal.IDALFacade;
import dto.User;

public class UserEntryBO implements IUserEntryBO{
	IDALFacade iDalFacade;
	public UserEntryBO(IDALFacade iDalFacade)
	{
		this.iDalFacade = iDalFacade;
	}
	public String loginUser(User u)
	{
		if(iDalFacade.authenticateUser(u))
		{
			return "Successful";
		}
		return "Unsuccessful";
	}
	public String registerUser(User u)
	{
		if(iDalFacade.addUserCredentials(u))
		{
			return "Successful";
		}
		return "User Already exists";
	}

}
