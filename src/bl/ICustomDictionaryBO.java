package bl;

import java.util.List;

import dto.Dictionary;
import dto.Word;

public interface ICustomDictionaryBO {
	boolean addCustomDictionary(String fileName, String filePathName);
	List<Word> getWordsByDictionary(String dictionaryName);
	List<Dictionary> getAllDictionaries();

}
