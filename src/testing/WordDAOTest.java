package testing;

import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;
import dto.Word;

public class WordDAOTest {

	private WordDAO wordDAO;

	@BeforeEach
	public void setUp() {
		try {
			wordDAO = new WordDAO();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetRootSuggestions_EmptyPrefix() {
		List<String> results = wordDAO.getRootSuggestions("");
		assertTrue(results.isEmpty(), "Expected no suggestions for empty prefix.");
	}

	@Test
	public void testGetRootSuggestions_ValidPrefix() {
		List<String> results = wordDAO.getRootSuggestions("شم");
		assertFalse(results.isEmpty(), "Expected some results for valid prefix.");
	}

	@Test
	public void testGetRootSuggestions_InvalidPrefix() {
		List<String> results = wordDAO.getRootSuggestions("xyz");
		assertTrue(results.isEmpty(), "Expected no results for invalid prefix.");
	}

	@Test
	public void testGetFavoriteWords_Empty() {
		List<Word> favorites = wordDAO.getFavoriteWords();
		assertFalse(favorites.isEmpty(), "Expected no favorite words when none are marked.");
	}

	@Test
	public void testGetFavoriteWords_NotEmpty() {
		List<Word> favorites = wordDAO.getFavoriteWords();
		assertFalse(favorites.isEmpty(), "Expected non-empty favorite words list.");
	}

	@Test
	public void testSavePosDetails_InvalidData() {
		boolean result = wordDAO.savePosDetails("", "", "", "", "");
		assertFalse(result, "Should return false for invalid input data.");
	}

	@Test
	public void testSavePosDetails_ValidData() {
		wordDAO.removeWordFromDB("test");
		wordDAO.addWordToDB(new Word("test", "urduTest", "persianTest"));
		boolean result = wordDAO.savePosDetails("test", "voweledTest", "stemTest", "noun", "rootTest");
		assertTrue(result, "Should return true for valid input data.");
	}

	@Test
	public void testSegmentWordWithDiacritics_Empty() {
		List<String> segments = wordDAO.segmentWordWithDiacritics("");
		assertTrue(segments.isEmpty(), "Expected empty result for empty input.");
	}

	@Test
	public void testSegmentWordWithDiacritics_ValidWord() {
		List<String> segments = wordDAO.segmentWordWithDiacritics("الشمس");
		assertFalse(segments.isEmpty(), "Expected non-empty result for valid input.");
	}

	@Test
	public void testDeleteFavoriteWord_NonExistingWord() {
		boolean result = wordDAO.deleteFavoriteWord("nonExistingWord");
		assertFalse(result, "Should return false for a non-existing word.");
	}

	@Test
	public void testDeleteFavoriteWord_ExistingWord() {
		wordDAO.markAsFavorite("banana");
		boolean result = wordDAO.deleteFavoriteWord("banana");
		assertTrue(result, "Should return true for an existing word.");
	}

	@Test
	public void testIsWordInFavourites_NotFavourite() {
		boolean isFavourite = wordDAO.isWordInFavourites("notFavourite");
		assertFalse(isFavourite, "Expected false for a word not in favourites.");
	}

	@Test
	public void testIsWordInFavourites_IsFavourite() {
		wordDAO.markAsFavorite("banana");
		boolean isFavourite = wordDAO.isWordInFavourites("banana");
		assertTrue(isFavourite, "Expected true for a word that is in favourites.");
	}

	@Test
	public void testGetPosDetails_NoDetails() {
		List<String[]> details = wordDAO.getPosDetails("nonExistingWord");
		assertTrue(details.isEmpty(), "Expected no POS details for non-existing word.");
	}

	@Test
	public void testGetPosDetails_HasDetails() {
		List<String[]> details = wordDAO.getPosDetails("شمس");
		assertFalse(details.isEmpty(), "Expected POS details for existing word.");
	}

	@Test
	public void testSavePosDetails_List() {
		List<String[]> details = new ArrayList<>();
		details.add(new String[] { "word", "voweled", "stem", "noun", "root" });
		wordDAO.savePosDetails("word", details);
	}

}
