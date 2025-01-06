package dal;

import java.io.File;
import java.util.List;

import dto.Dictionary;
import dto.User;
import dto.Word;

public class DALFacade implements IDALFacade {
	private IWordDAO wordDAO;
	private IUserEntryDAO userDAO;
	private IWebScrappingDAO webScrappingDAO;
	private ICustomDictionaryDAO customDictionaryDAO;

	public DALFacade(IWordDAO wordDAO, IUserEntryDAO userDAO, IWebScrappingDAO webScrappingDAO,
			ICustomDictionaryDAO customDictionaryDAO) {
		this.wordDAO = wordDAO;
		this.userDAO = userDAO;
		this.webScrappingDAO = webScrappingDAO;
		this.customDictionaryDAO = customDictionaryDAO;
	}

	@Override
	public boolean addWordToDB(Word w) {
		return wordDAO.addWordToDB(w);
	}

	@Override
	public boolean authenticateUser(User u) {
		return userDAO.authenticateUser(u);
	}

	@Override
	public boolean addUserCredentials(User u) {
		return userDAO.addUserCredentials(u);
	}

	@Override
	public boolean updateWordInDB(Word w) {
		return wordDAO.updateWordInDB(w);
	}

	@Override
	public boolean removeWordFromDB(String word) {
		return wordDAO.removeWordFromDB(word);
	}

	@Override
	public List<Word> getAllWordsFromDB() {
		return wordDAO.getAllWordsFromDB();
	}

	@Override
	public boolean saveOrUpdateWord(Word w) {
		return wordDAO.saveOrUpdateWord(w);
	}

	@Override
	public List<Word> importFile(File file) {
		return wordDAO.importFile(file);
	}

	@Override
	public Word getWordByUrduMeaningFromDB(String urduMeaning) {
		return wordDAO.getWordByUrduMeaningFromDB(urduMeaning);
	}

	@Override
	public Word getWordByPersianMeaningFromDB(String persianMeaning) {
		return wordDAO.getWordByPersianMeaningFromDB(persianMeaning);
	}

	@Override
	public Word getWordByWordFromDB(String word) {
		return wordDAO.getWordByWordFromDB(word);
	}

	@Override
	public String urduMeaningScrapper(String word) {
		return webScrappingDAO.urduMeaningScrapper(word);
	}

	@Override
	public String persianMeaningScrapper(String word) {
		return webScrappingDAO.persianMeaningScrapper(word);
	}

	@Override
	public boolean savePosDetails(String arabicWord, String voweled, String stem, String pos, String root) {
		return wordDAO.savePosDetails(arabicWord, voweled, stem, pos, root);
	}

	@Override
	public List<String> getSuggestions(String input) {
		return wordDAO.getSuggestions(input);
	}

	@Override
	public boolean wordExists(String word) {
		return wordDAO.wordExists(word);
	}

	@Override
	public boolean addCustomDictionary(String fileName, String filePath) {
		return customDictionaryDAO.addCustomDictionary(fileName, filePath);
	}

	@Override
	public List<Dictionary> getAllDictionaries() {
		return customDictionaryDAO.getAllDictionaries();
	}

	@Override
	public List<Word> getWordsByDictionary(String dictionaryName) {
		return customDictionaryDAO.getWordsByDictionary(dictionaryName);
	}

	@Override
	public List<Word> getSearchHistoryFromDB() {
		return wordDAO.getSearchHistoryFromDB();
	}

	@Override
	public boolean saveSearchHistory(Word w) {
		return wordDAO.saveSearchHistory(w);
	}

	@Override
	public void addWordToSearchHistory(String searchTerm, String meaning) {
		wordDAO.addWordToSearchHistory(searchTerm, meaning);

	}

	@Override
	public boolean markAsFavorite(String wordname) {
		return wordDAO.markAsFavorite(wordname);
	}

	@Override
	public List<Word> getWordsByRoot(String root) {
		return wordDAO.getWordsByRoot(root);
	}

	@Override
	public List<String> getRootSuggestions(String prefix) {
		return wordDAO.getRootSuggestions(prefix);
	}

	@Override
	public List<Word> getFavoriteWords() {
		return wordDAO.getFavoriteWords();
	}

	@Override

	public List<String> segmentWordWithDiacritics(String word) {
		return wordDAO.segmentWordWithDiacritics(word);

	}

	@Override
	public boolean deleteFavoriteWord(String word) {
		return wordDAO.deleteFavoriteWord(word);
	}

	@Override
	public boolean isWordInFavourites(String word) {
		return wordDAO.isWordInFavourites(word);
	}

	@Override
	public List<String[]> getPosDetails(String arabicWord) {
		// TODO Auto-generated method stub
		return wordDAO.getPosDetails(arabicWord);
	}

	@Override
	public void savePosDetails(String arabicWord, List<String[]> posDetailsList) {
		// TODO Auto-generated method 
           wordDAO.savePosDetails(arabicWord, posDetailsList);
		
	}

	@Override
	public boolean deleteDictionary(String dictionaryName) {
		// TODO Auto-generated method stub
		return customDictionaryDAO.deleteDictionary(dictionaryName);
	}
	

}