package dal;

import java.util.List;

import dto.Dictionary;
import dto.Word;

public interface ICustomDictionaryDAO {
	boolean addCustomDictionary(String fileName, String filePath);
	List<Dictionary> getAllDictionaries();
	List<Word> getWordsByDictionary(String dictionaryName);
	boolean deleteDictionary(String dictionaryName);
}
