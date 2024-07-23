package com.railansantana.e_commerce.repository;

import com.railansantana.e_commerce.domain.Order;
import com.railansantana.e_commerce.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    Optional<Product> findByListProducts_Id(String productId);
}
