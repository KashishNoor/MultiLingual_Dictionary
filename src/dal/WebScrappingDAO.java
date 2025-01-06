package dal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebScrappingDAO implements IWebScrappingDAO {
	private static final int MAX_RETRIES = 5;

	public String urduMeaningScrapper(String word) {
		return scrappingHelper("ur", word, 0);
	}

	public String persianMeaningScrapper(String word) {
		return scrappingHelper("fa", word, 0);
	}

	private String scrappingHelper(String lang, String word, int retryCount) {
		String url = "https://www.almaany.com/" + lang + "/dict/ar-" + lang + "/" + word + "/";
		try {
			Document doc = Jsoup.connect(url).userAgent(getRandomUserAgent()).get();
			Element firstRow = doc.select("table.results tbody tr").first();
			if (firstRow != null) {
				Element meaningElement = firstRow.select("td").get(1);
				String meaning = meaningElement.text().trim();
				meaning = meaning.replaceAll("\\(.?\\)", "").replaceAll("\\d+", "").replaceAll("\\[.?\\]", "").trim();
				return meaning;
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (retryCount < MAX_RETRIES) {
				System.out.println("Retry #" + (retryCount + 1));
				return scrappingHelper(lang, word, retryCount + 1);
			} else {
				System.out.println("Maximum retry limit reached.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Non-IO exception occurred.");
		}
		return null;
	}

	private static String getRandomUserAgent() {
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(WebScrappingDAO.class.getResourceAsStream("/user-agent.txt")))) {
			List<String> lines = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			Random random = new Random();
			return lines.get(random.nextInt(lines.size()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Mozilla/5.0"; 

	}
}