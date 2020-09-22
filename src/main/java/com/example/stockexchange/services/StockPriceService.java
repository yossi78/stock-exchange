package com.example.stockexchange.services;

import com.example.stockexchange.dl.dao.StockPriceEntity;
import com.example.stockexchange.dl.repositories.StockPriceRepository;
import com.example.stockexchange.dto.StockPrice;
import com.example.stockexchange.utils.FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




@Service
public class StockPriceService {


    private StockPriceRepository stockPriceRepository;
    private RestTemplate restTemplate;



    @Autowired
    public StockPriceService(StockPriceRepository stockPriceRepository, RestTemplate restTemplate) {
        this.stockPriceRepository = stockPriceRepository;
        this.restTemplate = restTemplate;
    }



    public void updateStockPriceFromExternal(List<String> filePathes, List<String> urls) throws IOException {
        List<StockPrice> newStockPrices = new ArrayList<>();
        for(String current:filePathes){
            newStockPrices = readStocksFromFile(current);
            mergeByLowerPrice(newStockPrices);
        }
        for(String current:urls){
            newStockPrices = readStocksFromUrl(current);
            mergeByLowerPrice(newStockPrices);
        }
    }

    private List<StockPrice> readStocksFromUrl(String url) {
        List<StockPrice> results  = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<StockPrice[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, StockPrice[].class);
        results = List.of(responseEntity.getBody());
        return results;
    }


    private List<StockPrice> fetchStockPricesFromDB() {
        List<StockPriceEntity> stockPricesEntities = stockPriceRepository.findAll();
        List<StockPrice> results = entityToDto(stockPricesEntities);
        return results;
    }

    private List<StockPrice> readStocksFromFile(String filePath) throws IOException {
        Boolean isFirst=true;
        StockPrice temp = new StockPrice();
        List<StockPrice> results = new ArrayList<>();
        List<String> lines = FileUtil.fileToLines(filePath);
        for(String current:lines){
            if (filePath.contains("csv") && isFirst){
                isFirst=false;
                continue;
            }
            if(filePath.contains("csv")){
                temp =parseStringToStockPrice(current,",");
            }else{
                current=current.replace("},","}");
                current=current.replace("[","");
                current=current.replace("]","");
                if(current.length()<1){
                    continue;
                }
                temp =(StockPrice) parseStringToObject(current,StockPrice.class);
            }
            results.add(temp);
        }
        return results;
    }

    private StockPrice  parseStringToStockPrice(String str,String seperator) throws IOException {
        StockPrice stockPrice  =new StockPrice();
        List<String> list = List.of(str.split(seperator));
        stockPrice.setName(list.get(0));
        String priceStr=list.get(2);
        Double price = (Double) parseStringToObject(priceStr,Double.class);
        stockPrice.setPrice(price);
        return stockPrice;
    }


    private void mergeByLowerPrice(List<StockPrice> newStockPrices) {
        for(StockPrice current: newStockPrices){
            StockPriceEntity stockPriceEntityFromDB = stockPriceRepository.findById(current.getName()).orElse(null);
            if(stockPriceEntityFromDB==null){
                stockPriceRepository.save(current.convertToDao());
                continue;
            }
            else if(current.getPrice()<stockPriceEntityFromDB.getPrice()){
                stockPriceRepository.save(current.convertToDao());
            }
        }

    }


    public Double getPrice(String stockName) {
        StockPriceEntity stockPriceEntity = stockPriceRepository.findById(stockName).orElse(null);
        StockPrice stockPrice = stockPriceEntity.convertToDto();
        return stockPrice.getPrice();
    }


    private List<StockPriceEntity> dtoToEntity(List<StockPrice> stockPrices){
        List<StockPriceEntity> results = new ArrayList<>();
        for(StockPrice current:stockPrices){
            results.add(current.convertToDao());
        }
        return results;
    }


    private List<StockPrice> entityToDto(List<StockPriceEntity> stockPriceEntities){
        List<StockPrice> results = new ArrayList<>();
        for(StockPriceEntity current:stockPriceEntities){
            results.add(current.convertToDto());
        }
        return results;
    }


    
    public Object parseStringToObject(String str,Class T) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = mapper.readValue(str,T);
        return result;
    }

    public List<StockPrice> getAllLowestPrices() {
        List<StockPriceEntity> entities = stockPriceRepository.getAllLowestPrices();
        List<StockPrice> stockPrices = entityToDto(entities);
        return stockPrices;
    }
}
