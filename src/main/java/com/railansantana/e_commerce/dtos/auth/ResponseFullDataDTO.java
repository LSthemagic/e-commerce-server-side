package com.railansantana.e_commerce.dtos.auth;

import com.railansantana.e_commerce.domain.Order;

import java.time.Instant;
import java.util.List;

public record ResponseFullDataDTO(String id, String name, String email, List<Order> orders, String token,
                                  String address, Instant createdAt) {
}

