package com.example.stockexchange.dl.dao;
import com.example.stockexchange.dto.StockPrice;
import lombok.Data;
import javax.persistence.*;



@Entity(name="StockPriceEntity")
@Table(name = "stock_price")
@Data
public class StockPriceEntity {


    @Id
    @Column(name = "NAME")
    private String name;


    @Column(name = "PRICE")
    private Double price;

    public StockPriceEntity() {

    }

    public StockPriceEntity(String name, Double price) {
        this.name = name;
        this.price = price;
    }


    public StockPrice convertToDto(){
        StockPrice stockPrice = new StockPrice(getName(),getPrice());
        return stockPrice;
    }
}
