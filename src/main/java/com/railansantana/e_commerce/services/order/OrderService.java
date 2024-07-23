package com.railansantana.e_commerce.services.order;

import com.railansantana.e_commerce.domain.*;
import com.railansantana.e_commerce.domain.enums.OrderStatus;
import com.railansantana.e_commerce.dtos.auth.ResponseFullDataDTO;
import com.railansantana.e_commerce.repository.OrderRepository;
import com.railansantana.e_commerce.services.Exceptions.ResourceNotFoundException;
import com.railansantana.e_commerce.services.product.ProductService;
import com.railansantana.e_commerce.services.stock.StockService;
import com.railansantana.e_commerce.services.user.DataUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final Payment payment;
    private final OrderRepository orderRepository;
    private final DataUserService userService;
    private final ProductService productService;
    private final StockService stockService;



    public List<Order> getOrders(String userId) {
        ResponseFullDataDTO user = userService.findById(userId);
        return user.orders();
    }

    public void createOrder(String userId, Order order) {
        User user = userService.findByIdAux(userId);
        order.setClient(user);
        user.getOrders().add(order);
        orderRepository.save(order);
        userService.save(user);
    }

    public void addProductToOrder(String userId, String orderId, String productId, int quantity) {
        User user = userService.findByIdAux(userId);
        Product product = productService.findById(productId);
        Optional<Order> optionalOrder = user.getOrders().stream().filter(order -> order.getId().equals(orderId)).findFirst();

        if (optionalOrder.isEmpty()) {
            throw new ResourceNotFoundException("Order not found");
        }
        boolean isProductExists = user.getOrders().stream().anyMatch(x-> x.getListProducts().contains(product));
        if (isProductExists) {
            optionalOrder.get().updateQuantityOrderProduct(quantity);
            userService.save(user);
            orderRepository.save(optionalOrder.get());
            return;
        }

        optionalOrder.get().addProduct(product, quantity);
        optionalOrder.get().calculateTotalPrice(quantity);
        optionalOrder.get().finalizeOrder(OrderStatus.WAITING_PAYMENT);
        Product p = productService.update(productId, product);
        stockService.removeQuantityProductFromStock(p.getId(), p.getStock().getId(), quantity);

        userService.save(user);
        orderRepository.save(optionalOrder.get());
    }

    public void removeProductFromOrder(String userId, String orderId, String productId, Integer quantity) {
        Product product = productService.findById(productId);
        User user = userService.findByIdAux(userId);
        Optional<Order> optionalOrder = user.getOrders().stream().filter(order -> order.getId().equals(orderId)).findFirst();

        if (optionalOrder.isEmpty()) {
            throw new ResourceNotFoundException("Order not found");
        }

//        Order order = optionalOrder.get();
        optionalOrder.get().removeProduct(product);
        optionalOrder.get().calculateTotalPrice(quantity);
        userService.save(user);
        orderRepository.save(optionalOrder.get());

    }

    public void removeOrder(String userId, String orderId) {
        User user = userService.findByIdAux(userId);
        user.getOrders().removeIf(order -> order.getId().equals(orderId));
        userService.save(user);
        orderRepository.deleteById(orderId);
    }
}
