package com.railansantana.e_commerce.repository;

import com.railansantana.e_commerce.domain.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends MongoRepository<Stock, String> {
}
