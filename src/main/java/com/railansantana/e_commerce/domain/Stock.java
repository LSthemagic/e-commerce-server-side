package com.railansantana.e_commerce.domain;

import com.railansantana.e_commerce.services.Exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
public class Stock implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    @DBRef
    private List<Product> product = new ArrayList<>();


    public void addStock(int quantity, Product prod) {
       prod.setQuantity(quantity + prod.getQuantity());
    }

    public void removeStock(int quantity, Product prod) {
        if(!verifyStock(quantity, prod)) {
           throw new ResourceNotFoundException("quantity out of stock");
        }
        prod.setQuantity(prod.getQuantity() - quantity);
    }

    public Boolean verifyStock(int quantity, Product prod) {
        return prod.getQuantity() >= quantity;
    }

}
