package com.railansantana.e_commerce.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railansantana.e_commerce.dtos.auth.ResponseFullDataDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String name;
    private String email;
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant createdAt;
    private String address;
    private String roles;

    @DBRef
    private List<Order> orders = new ArrayList<>();

    public User(String id, String name, String email, String password, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = Instant.now();
        this.address = address;
    }

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = Instant.now();
    }

    public User(ResponseFullDataDTO user) {
        setName(user.name());
        setEmail(user.email());
        setAddress(user.address());
        getOrders().add((Order) user.orders());
    }

    public void updateAddress(String address) {
        setAddress(address);
    }
}
