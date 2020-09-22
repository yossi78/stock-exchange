package com.example.stockexchange.api;
import com.example.stockexchange.dl.dao.StockPriceEntity;
import com.example.stockexchange.dto.StockPrice;
import com.example.stockexchange.services.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/v1/stocks/prices")
public class StockPriceController {



    private StockPriceService stockPriceService;


    @Autowired
    public StockPriceController(StockPriceService stockPriceService) {
        this.stockPriceService = stockPriceService;
    }



    @GetMapping("/{stockName}")
    public ResponseEntity GetLowestPrice(@PathVariable String stockName) {
        try {
            Double lowestPrice = stockPriceService.getPrice(stockName);
            return ResponseEntity.ok().body(lowestPrice);
        }catch (Exception e){
           System.out.println("Failed to find stock price");
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @GetMapping(value = "/lowest")
    public ResponseEntity GetAllLowestPrices() {
        try {
            List<StockPrice> stockPrices = stockPriceService.getAllLowestPrices();
            return new ResponseEntity(stockPrices, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getCause(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



    @GetMapping(value = "/health")
    public ResponseEntity healthCheck() {
        try {
            return new ResponseEntity("Stock Exchange Service Health is OK", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getCause(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
