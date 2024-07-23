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
    private Product product;
    private Integer quantity;


    public void updateStock(int quantity) {
     setQuantity(getQuantity() + quantity);
    }

    public void removeStock(int quantity) {
        if(!verifyStock(quantity)) {
           throw new ResourceNotFoundException("quantity out of stock");
        }
       setQuantity(getQuantity() - quantity);
    }

    public Boolean verifyStock(int quantity) {
        return getQuantity() >= quantity;
    }

}
