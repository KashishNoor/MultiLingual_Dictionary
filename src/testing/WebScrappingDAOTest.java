package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dal.WebScrappingDAO;

public class WebScrappingDAOTest {

    private WebScrappingDAO dao = new WebScrappingDAO();

    @Test
    public void testUrduMeaningScraper_ExistingWord() {
        String result = dao.urduMeaningScrapper("سلام");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        System.out.println("Urdu meaning of 'سلام': " + result);
    }

    @Test
    public void testUrduMeaningScraper_NonExistingWord() {
        String result = dao.urduMeaningScrapper("zzzzzzz");
        assertNull(result);
    }

    @Test
    public void testPersianMeaningScraper_ExistingWord() {
        String result = dao.persianMeaningScrapper("سلام");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        System.out.println("Persian meaning of 'سلام': " + result);
    }

    @Test
    public void testPersianMeaningScraper_NonExistingWord() {
        String result = dao.persianMeaningScrapper("zzzzzzz");
        assertNull(result);
    }
}