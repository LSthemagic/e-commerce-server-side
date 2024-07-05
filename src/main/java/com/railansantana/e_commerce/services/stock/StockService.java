package com.railansantana.e_commerce.services.stock;

import com.railansantana.e_commerce.domain.Product;
import com.railansantana.e_commerce.domain.Stock;
import com.railansantana.e_commerce.repository.StockRepository;
import com.railansantana.e_commerce.services.Exceptions.DatabaseException;
import com.railansantana.e_commerce.services.Exceptions.ResourceNotFoundException;
import com.railansantana.e_commerce.services.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final ProductService productService;

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    public Stock findById(String id) {
        Optional<Stock> stock = stockRepository.findById(id);
        if (stock.isEmpty()) {
            throw new ResourceNotFoundException("Stock not found");
        }
        return stock.get();
    }

    public Stock save(String productId) {
        Product product = productService.findById(productId);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found");
        }

        // Create a new Stock object with the product
        Stock objStock = new Stock(null, Collections.singletonList(product));

        // Fetch all existing stocks
        List<Stock> savedStockList = stockRepository.findAll();

        // Check if the product is already in any of the stocks
        boolean stockExists = savedStockList.stream()
                .anyMatch(stock -> stock.getProduct().stream()
                        .anyMatch(p -> p.getId().equals(productId)));

        if (stockExists) {
            throw new DatabaseException("Stock already exists");
        }

        // Save and return the new stock
        return stockRepository.save(objStock);
    }

    public void addProductToStock(String productId, String stockId) {
        Stock stock = findById(stockId);
        Product prod = productService.findById(productId);

        // Check if the product is already in any of the stocks
        boolean stockExists = stock.getProduct().stream()
                        .anyMatch(p -> p.getId().equals(productId));

        if (stockExists) {
            throw new DatabaseException("Product already exists from stock");
        }
        stock.getProduct().add(prod);
        stock.addStock(prod.getQuantity(), prod);
        stockRepository.save(stock);
    }

    public void removeProductFromStock(String productId, String stockId, int quantity) {
        Stock stock = findById(stockId);
        Product prod = productService.findById(productId);
        stock.removeStock(quantity,prod);
        productService.update(productId, prod);
        stockRepository.save(stock);

    }

    public void deleteStock(String stockId) {
        Stock stock = findById(stockId);
        stockRepository.delete(stock);
    }

}
