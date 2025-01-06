package bl;

import java.util.List;

import dal.IDALFacade;
import dto.Dictionary;
import dto.Word;

public class CustomDictionaryBO implements ICustomDictionaryBO {

	private IDALFacade iDalFacade;
	public CustomDictionaryBO(IDALFacade iDalFacade)
	{
		this.iDalFacade = iDalFacade;
	}
	@Override
	public boolean addCustomDictionary(String fileName, String filePathName) {
		return iDalFacade.addCustomDictionary(fileName, filePathName);
	}
	@Override
	public List<Word> getWordsByDictionary(String dictionaryName) {
		return iDalFacade.getWordsByDictionary(dictionaryName);
	}
	@Override 
	public List<Dictionary> getAllDictionaries()
	{
		return iDalFacade.getAllDictionaries();
	}

}
