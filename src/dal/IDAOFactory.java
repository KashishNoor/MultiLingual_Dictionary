package dal;

public interface IDAOFactory {
	IWordDAO createWordDao() throws Exception;
	IUserEntryDAO createUserDao();
	IWebScrappingDAO createWebScrappingDao();
	ICustomDictionaryDAO createCustomDictionaryDao() throws Exception;

}
