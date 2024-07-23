package com.railansantana.e_commerce.services.product;

import com.railansantana.e_commerce.domain.Product;
import com.railansantana.e_commerce.repository.ProductRepository;
import com.railansantana.e_commerce.services.Exceptions.DatabaseException;
import com.railansantana.e_commerce.services.Exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product findById(String id) {
        Optional <Product> objProduct = productRepository.findById(id);
        if (objProduct.isEmpty()){
            throw new ResourceNotFoundException("Product not found");
        }
        return  objProduct.get();
    }

    public List<Product> findAll() {
      return productRepository.findAll();
    }

    public Product save(Product product) {
        Optional <Product> objProduct = productRepository.findByNameIgnoreCase(product.getName());
        if (objProduct.isPresent()){
            throw new DatabaseException("Product already exists");
        }
        return productRepository.save(product);
    }

    public void delete(String id) {
        Product obj = findById(id);
        if (obj == null){
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public Product update(String id, Product product) {
        Product obj = findById(id);
        if (obj == null){
            throw new ResourceNotFoundException("Product not found");
        }
        updateData(obj, product);
        return productRepository.save(obj);
    }

    private void updateData(Product obj, Product product) {
        obj.setName(product.getName());
        obj.setDescription(product.getDescription());
        obj.setPrice(product.getPrice());
        obj.setImage(product.getImage());
        obj.setCategory(product.getCategory());
        obj.setStock(product.getStock());
    }

    public List<Product> findByName(String name){
        return productRepository.findByNameContainsIgnoreCase(name);
    }

}
