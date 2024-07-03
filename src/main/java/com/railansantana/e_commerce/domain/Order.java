package com.railansantana.e_commerce.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railansantana.e_commerce.domain.enums.OrderStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Order {
    @Id
    private String id;
    private User client;
    @DBRef
    private final List<Product> listProducts = new ArrayList<Product>();
    private Double totalPrice;
    private OrderStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant createdAt;

    public void addProduct(Product product) {
        listProducts.add(product);
    }

    public void removeProduct(Product product) {
        listProducts.removeIf((x)-> x.getId().equals(product.getId()));
    }

    public void calculateTotalPrice() {
        this.totalPrice = listProducts.stream().
                mapToDouble((x)-> x.getPrice()).sum();
    }

    public void finalizeOrder(OrderStatus status) {
        setStatus(status);
        setCreatedAt(Instant.now());
    }

}
