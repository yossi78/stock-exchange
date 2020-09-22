package com.example.stockexchange.dto;


import com.example.stockexchange.dl.dao.StockPriceEntity;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;




@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockPrice {

    @JsonAlias({"Symbol", "name"})
    private String name;


    @JsonAlias({"Price", "price"})
    private Double price;


    public StockPrice() {
    }

    public StockPrice(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public StockPriceEntity convertToDao (){
        StockPriceEntity stockPriceEntity = new StockPriceEntity(getName(),getPrice());
        return stockPriceEntity;
    }


}
