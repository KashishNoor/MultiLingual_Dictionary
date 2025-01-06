package bl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dal.IDALFacade;

public class WebScrappingBO implements IWebScrappingBO {

    private IDALFacade iDalFacade;
    private ExecutorService executorService;

    public WebScrappingBO(IDALFacade iDalFacade) {
        this.iDalFacade = iDalFacade;
        this.executorService = Executors.newFixedThreadPool(5);
    }

    @Override
    public CompletableFuture<String> urduMeaningScrapper(String word) {
        return CompletableFuture.supplyAsync(() -> iDalFacade.urduMeaningScrapper(word), executorService);
    }

    @Override
    public CompletableFuture<String> persianMeaningScrapper(String word) {
        return CompletableFuture.supplyAsync(() -> iDalFacade.persianMeaningScrapper(word), executorService);
    }
    public void shutdown() {
        executorService.shutdown();
    }
}
