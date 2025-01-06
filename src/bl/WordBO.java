package bl;

import dal.IDALFacade;
import dto.Word;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WordBO implements IWordBO {
    private IDALFacade iDalFacade;
    private List<Word> searchHistory = new ArrayList<>(); 
    
    public WordBO(IDALFacade iDalFacade) {
        this.iDalFacade = iDalFacade;
    }

    public String addWord(Word w) {
        if (iDalFacade.addWordToDB(w)) {
            return " added to dictionary successfully.";
        }
        return " failed to be added to dictionary.";
    }

    public String updateWordMeaning(Word w) {
        if (iDalFacade.updateWordInDB(w)) {
            return "updated successfully.";
        }
        return "word not found in dictionary.";
    }

    public String removeWord(String word) {
        if (iDalFacade.removeWordFromDB(word)) {
            return "Word '" + word + "' deleted successfully.";
        }
        return "Failed to delete word '" + word + "'. It may not exist.";
    }

    public List<Word> importFile(File file) {
        return iDalFacade.importFile(file);
    }

    public List<Word> getAllWords() {
        return iDalFacade.getAllWordsFromDB();
    }

    public Word getWordByUrduMeaning(String urduMeaning) {
        return iDalFacade.getWordByUrduMeaningFromDB(urduMeaning);
    }

    public Word getWordByPersianMeaning(String persianMeaning) {
        return iDalFacade.getWordByPersianMeaningFromDB(persianMeaning);
    }

    public Word getWordByWord(String word) {
        return iDalFacade.getWordByWordFromDB(word);
    }


    public List<String[]> getPosDetails(String arabicWord) {
    	return iDalFacade.getPosDetails(arabicWord);
    }
  
    @Override
	public void savePosDetails(String arabicWord, List<String[]> posDetailsList) {
		// TODO Auto-generated method 
    	iDalFacade.savePosDetails(arabicWord, posDetailsList);
		
	}
    @Override
    public List<String> getSuggestions(String input) {
        return iDalFacade.getSuggestions(input);
    }

    @Override
    public void addSearchHistory(String searchTerm, String meaning) {
        Word word = new Word(searchTerm, meaning, meaning); 
        searchHistory.add(word); 
        iDalFacade.addWordToSearchHistory(searchTerm, meaning);
    }

    @Override
    public List<Word> getSearchHistory() {
        return iDalFacade.getSearchHistoryFromDB();
    }
    
 		
 	public boolean markAsFavorite(String wordname) {
 		return iDalFacade.markAsFavorite(wordname);
 	}
 		
    public List<Word> getWordsByRoot(String root)
 	{
 	    return iDalFacade.getWordsByRoot(root);	 
 	}
    
 	public List<String> getRootSuggestions(String prefix)
 	{
 		return iDalFacade.getRootSuggestions(prefix);
 	}

 	public List<Word> getFavoriteWords()
 	{
 		return iDalFacade.getFavoriteWords();
 	}
 	 @Override
     public List<String> segmentWordWithDiacritics(String word) {
         try {
             return iDalFacade.segmentWordWithDiacritics(word);
         } catch (Exception e) {
             System.err.println("Error during segmentation in BL: " + e.getMessage());
             return null;
         }
}
 	@Override
	public boolean deleteFavoriteWord(String word) {
		return iDalFacade.deleteFavoriteWord(word);
	}

 	@Override
 	public boolean isWordInFavourites(String word) {
 		return iDalFacade.isWordInFavourites(word);
 	}
}