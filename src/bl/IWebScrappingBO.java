package bl;


import java.util.concurrent.CompletableFuture;

public interface IWebScrappingBO {
	CompletableFuture<String> urduMeaningScrapper(String word);
	CompletableFuture<String> persianMeaningScrapper(String word);

}
