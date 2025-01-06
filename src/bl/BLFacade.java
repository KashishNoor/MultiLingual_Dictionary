package bl;

import java.io.File;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import dto.Dictionary;
import dto.User;
import dto.Word;

public class BLFacade implements IBLFacade {

	private IWordBO wordBo;
	private IUserEntryBO userEntryBo;
	private IWebScrappingBO webScrappingBo;
	private ICustomDictionaryBO customDictionaryBo;

	public BLFacade(IWordBO wordBo, IUserEntryBO userEntryBo, IWebScrappingBO webScrappingBo,
			ICustomDictionaryBO customDictionaryBo) {
		this.wordBo = wordBo;
		this.userEntryBo = userEntryBo;
		this.webScrappingBo = webScrappingBo;
		this.customDictionaryBo = customDictionaryBo;
	}

	@Override
	public String addWord(Word w) {
		return wordBo.addWord(w);
	}

	@Override
	public String updateWordMeaning(Word w) {
		return wordBo.updateWordMeaning(w);
	}

	@Override
	public String removeWord(String word) {
		return wordBo.removeWord(word);
	}

	@Override
	public String loginUser(User u) {
		return userEntryBo.loginUser(u);
	}

	@Override
	public String registerUser(User u) {
		return userEntryBo.registerUser(u);
	}

	@Override
	public List<Word> getAllWords() {
		return wordBo.getAllWords();
	}

	@Override
	public List<Word> importFile(File file) {
		return wordBo.importFile(file);
	}

	@Override
	public Word getWordByWord(String word) {
		return wordBo.getWordByWord(word);
	}

	@Override
	public Word getWordByUrduMeaning(String urduMeaning) {
		return wordBo.getWordByUrduMeaning(urduMeaning);
	}

	@Override
	public Word getWordByPersianMeaning(String persianMeaning) {
		return wordBo.getWordByPersianMeaning(persianMeaning);

	}

	@Override
	public CompletableFuture<String> urduMeaningScrapper(String word) {
		return webScrappingBo.urduMeaningScrapper(word);
	}

	@Override
	public CompletableFuture<String> persianMeaningScrapper(String word) {
		return webScrappingBo.persianMeaningScrapper(word);
	}

	public List<String[]> getPosDetails(String arabicWord) {
		return wordBo.getPosDetails(arabicWord);
	}

	@Override
	public List<String> getSuggestions(String input) {
		return wordBo.getSuggestions(input);
	}

	@Override
	public boolean addCustomDictionary(String fileName, String filePathName) {
		return customDictionaryBo.addCustomDictionary(fileName, filePathName);
	}

	@Override
	public List<Word> getWordsByDictionary(String dictionaryName) {
		return customDictionaryBo.getWordsByDictionary(dictionaryName);
	}

	@Override
	public List<Dictionary> getAllDictionaries() {
		return customDictionaryBo.getAllDictionaries();
	}
	@Override
	public List<Word> getSearchHistory() {
	    return wordBo.getSearchHistory();  
	}


	@Override
	public void addSearchHistory(String searchTerm, String meaning) {
	    wordBo.addSearchHistory(searchTerm, meaning); 
	}
	
	@Override
	public boolean markAsFavorite(String wordname) {

		return wordBo.markAsFavorite(wordname);
	}

	@Override
	public List<Word> getWordsByRoot(String root) {
		return wordBo.getWordsByRoot(root);
	}

	@Override
	public List<String> getRootSuggestions(String prefix) {
		
		return wordBo.getRootSuggestions(prefix);
	}

	@Override
	public List<Word> getFavoriteWords() {
		return wordBo.getFavoriteWords();
	}
	@Override
	public List<String> segmentWordWithDiacritics(String word){
		return wordBo.segmentWordWithDiacritics(word);
	}

	@Override
	public boolean deleteFavoriteWord(String word) {
	
		return wordBo.deleteFavoriteWord(word);
	}

	@Override
	public boolean isWordInFavourites(String word) {
		return wordBo.isWordInFavourites(word);
	}
	

	@Override
	public void savePosDetails(String arabicWord, List<String[]> posDetailsList) {
		 wordBo.savePosDetails(arabicWord, posDetailsList);
		
	}
		
}