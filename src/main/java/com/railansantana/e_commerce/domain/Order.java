package com.railansantana.e_commerce.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.railansantana.e_commerce.domain.enums.OrderStatus;
import com.railansantana.e_commerce.services.Exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Order {
    @Id
    private String id;

    @JsonIgnore
    @DBRef
    private User client;

    @DBRef
    private List<Product> listProducts = new ArrayList<>();

    private Double totalPrice;
    private OrderStatus status;
    private int quantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant createdAt;

    public void addProduct(Product product, int quantity) {
        listProducts.add(new Product(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getImage(),
                product.getStock())
        );
        this.quantity += quantity;
    }

    public void updateQuantityOrderProduct(int quantity){
        this.quantity += quantity;
        calculateTotalPrice(quantity);
    }

    public void removeProduct(Product product) {
        listProducts.removeIf(x -> x.getId().equals(product.getId()));
        calculateTotalPrice(getQuantity());
    }

    public void calculateTotalPrice(Integer quantityProducts) {
        this.totalPrice = listProducts.stream().mapToDouble(x -> x.getPrice() * quantityProducts).sum();
    }

    public void finalizeOrder(OrderStatus status) {
        setStatus(status);
        setCreatedAt(Instant.now());
    }
}
