package com.railansantana.e_commerce.repository;

import com.railansantana.e_commerce.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByNameIgnoreCase(String name);
    List<Product> findByNameContainsIgnoreCase(String name);
}
