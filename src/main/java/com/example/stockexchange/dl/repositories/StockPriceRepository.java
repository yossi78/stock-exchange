package com.example.stockexchange.dl.repositories;


import com.example.stockexchange.dl.dao.StockPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


//         CREATE TABLE `stock_price` (
//        `NAME` varchar(245) NOT NULL,
//        `PRICE`  DOUBLE(40,4) NOT NULL,
//        `CREATION_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
//        `UPDATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
//        PRIMARY KEY (`NAME`)
//        )




public interface StockPriceRepository  extends JpaRepository<StockPriceEntity, String> {


    @Query(nativeQuery=true, value="SELECT * FROM stock_price WHERE price =  ( SELECT MIN(price) FROM stock_price )")
    public List<StockPriceEntity> getAllLowestPrices();





}