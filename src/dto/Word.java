package dto;

import java.util.ArrayList;
import java.util.List;

public class Word {
	private String word;
	private String urduMeaning;
	private String persianMeaning;
    private String rootWord;
    private static List<Word> searchHistory = new ArrayList<>();
    
    public Word(String word, String urduMeaning, String persianMeaning, String rootWord) {
        this.word = word;
        this.urduMeaning = urduMeaning;
        this.persianMeaning = persianMeaning;
        this.rootWord = rootWord;
    }
	 
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getUrduMeaning() {
		return urduMeaning;
	}
	public String getPersianMeaning() {
		return persianMeaning;
	}
	public void setUrduMeaning(String urduMeaning) {
		this.urduMeaning = urduMeaning;
	}
	public void setPersianMeaning(String persianMeaning) {
		this.persianMeaning = persianMeaning;
	}
	public String getRootWord() {
	        return rootWord;
	}
    public void setRootWord(String rootWord) {
	        this.rootWord = rootWord;
	}
	public Word(String word, String urduMeaning, String persianMeaning) {
		this.word = word;
		this.urduMeaning = urduMeaning;
		this.persianMeaning = persianMeaning;
	}
	public static void addToSearchHistory(Word word) {
	        searchHistory.add(word);
	}
	public static List<Word> getSearchHistory() {
	        return searchHistory;
	}

}