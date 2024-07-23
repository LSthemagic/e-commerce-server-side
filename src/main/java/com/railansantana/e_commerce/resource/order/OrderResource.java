package com.railansantana.e_commerce.resource.order;

import com.railansantana.e_commerce.domain.Order;
import com.railansantana.e_commerce.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderResource {
    private final OrderService orderService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getOrders(@PathVariable String userId) {
        return ResponseEntity.ok().body(orderService.getOrders(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Void> createOrder(@PathVariable String userId, @RequestBody Order order) {
        orderService.createOrder(userId, order);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/{userId}/{orderId}/{productId}/{quantity}")
    public ResponseEntity<Void> addProductToOrder(@PathVariable String userId, @PathVariable String orderId, @PathVariable String productId, @PathVariable int quantity)  {
        orderService.addProductToOrder(userId, orderId, productId, quantity);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/{userId}/{orderId}/{productId}/{quantity}")
    public ResponseEntity<Void> removeProductFromOrder(@PathVariable String userId, @PathVariable String orderId, @PathVariable String productId, @PathVariable Integer quantity) {
        orderService.removeProductFromOrder(userId, orderId, productId, quantity);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteOrder/{userId}/{orderId}")
    public ResponseEntity<Void> removeOrder(@PathVariable String userId, @PathVariable String orderId) {
        orderService.removeOrder(userId, orderId);
        return ResponseEntity.noContent().build();
    }
}
