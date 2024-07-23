package com.railansantana.e_commerce.services.stock;

import com.railansantana.e_commerce.domain.Product;
import com.railansantana.e_commerce.domain.Stock;
import com.railansantana.e_commerce.repository.StockRepository;
import com.railansantana.e_commerce.services.Exceptions.DatabaseException;
import com.railansantana.e_commerce.services.Exceptions.ResourceNotFoundException;
import com.railansantana.e_commerce.services.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public Stock save(String productId, int quantity) {
        Product product = productService.findById(productId);
        // Create a new Stock object with the product
        Stock objStock = new Stock(null, product, quantity);
        // Fetch all existing stocks
        List<Stock> savedStockList = stockRepository.findAll();
        // Check if the product is already in any of the stocks
        boolean stockExists = savedStockList.stream()
                .anyMatch(stock -> stock.getProduct().getId().equals(product.getId()));
        if (stockExists) {
            throw new DatabaseException("Stock already exists");
        }
        objStock = stockRepository.save(objStock);
        // Save and return the new stock
        product.setStock(objStock);
        productService.update(productId, product);
        return objStock;
    }

    public void addQuantityProducts(String productId, String stockId, int quantity) {
        Stock stock = findById(stockId);
        Product prod = productService.findById(productId);

        // Check if the product is already in any of the stocks
        boolean stockExists = stock.getProduct().getId().equals(productId);

        if (!stockExists) {
            throw new DatabaseException("Product don't already exists from stock");
        }
        stock.updateStock(quantity);
        stockRepository.save(stock);
    }

    public void removeQuantityProductFromStock(String productId, String stockId, int quantity) {
        Stock stock = findById(stockId);
        Product prod = productService.findById(productId);

        // Check if the product is already in any of the stocks
        boolean stockExists = stock.getProduct().getId().equals(productId);

        if (!stockExists) {
            throw new DatabaseException("Product not exists from stock");
        }
        stock.removeStock(quantity);
        stockRepository.save(stock);
    }

    public void deleteStock(String stockId) {
        Stock stock = findById(stockId);
        stockRepository.delete(stock);
    }

}
