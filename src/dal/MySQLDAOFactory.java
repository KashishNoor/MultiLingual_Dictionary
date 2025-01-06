package dal;

public class MySQLDAOFactory implements IDAOFactory{

	@Override
	public IWordDAO createWordDao() throws Exception {
		return new WordDAO();
	}

	@Override
	public IUserEntryDAO createUserDao() {
		return new UserEntryDAO();
	}

	@Override
	public IWebScrappingDAO createWebScrappingDao() {
		return new WebScrappingDAO();
	}

	@Override
	public ICustomDictionaryDAO createCustomDictionaryDao() throws Exception {
		return new CustomDictionaryDAO();
	}

}
