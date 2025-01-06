package bl;

import java.io.File;
import java.util.List;

import dto.Word;

public interface IWordBO {
	String addWord(Word w);
	String updateWordMeaning(Word w);
	String removeWord(String word);
	List<Word> getAllWords();
	List<Word> importFile(File file);
	Word getWordByWord(String word);
	Word getWordByUrduMeaning(String urduMeaning);
	Word getWordByPersianMeaning(String persianMeaning);
	List<String[]> getPosDetails(String arabicWord);
	List<String> getSuggestions(String input);
	List<Word> getSearchHistory();  
    void addSearchHistory(String searchTerm, String meaning);
    public boolean markAsFavorite(String wordd);
    public List<Word> getWordsByRoot(String root);
	List<String> getRootSuggestions(String prefix);
	 public List<Word> getFavoriteWords();
	 public List<String> segmentWordWithDiacritics(String word);
	boolean deleteFavoriteWord(String word);
	boolean isWordInFavourites(String word);
	void savePosDetails(String arabicWord, List<String[]> posDetailsList);

}