package com.example.stockexchange.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;





@Service
@EnableScheduling
public class StockPriceLoaderScheduler {

    @Value("${stockExchange.priceLoad.scheduler.files}")
    private List<String> files;

    @Value("${stockExchange.priceLoad.scheduler.urls}")
    private List<String> urls;

    private StockPriceService stockPriceService;



    @Autowired
    public StockPriceLoaderScheduler(StockPriceService stockPriceService)  {
        this.stockPriceService=stockPriceService;
    }


    @Scheduled(fixedDelayString = "${stockExchange.priceLoad.scheduler.delay:20000}")
    public void updateStocksPrice() {
        try {
            stockPriceService.updateStockPriceFromExternal(files,urls);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
