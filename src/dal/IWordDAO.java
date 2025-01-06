package dal;

import java.io.File;
import java.util.List;

import dto.Word;

public interface IWordDAO {
	boolean addWordToDB(Word w);
    boolean updateWordInDB(Word w);
    boolean removeWordFromDB(String word);
    List<Word> getAllWordsFromDB();
    boolean saveOrUpdateWord(Word w);
    List<Word> importFile(File file);
    Word getWordByUrduMeaningFromDB(String urduMeaning);
    Word getWordByPersianMeaningFromDB(String persianMeaning);
    Word getWordByWordFromDB(String word);
	List<String> getSuggestions(String input);
	boolean wordExists(String word);
    boolean saveSearchHistory(Word word); 
    List<Word> getSearchHistoryFromDB();    
	void addWordToSearchHistory(String searchTerm,String meaning);
	boolean markAsFavorite(String wordname);
	List<Word> getWordsByRoot(String root);
	List<String> getRootSuggestions(String prefix);
	List<Word> getFavoriteWords();
	boolean savePosDetails(String arabicWord, String voweled, String stem, String pos, String root);
	List<String> segmentWordWithDiacritics(String word);
	boolean deleteFavoriteWord(String word);
	boolean isWordInFavourites(String word);
	List<String[]> getPosDetails(String arabicWord);
	void savePosDetails(String arabicWord, List<String[]> posDetailsList);
    
}