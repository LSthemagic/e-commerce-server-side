package com.railansantana.e_commerce.services.order;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.railansantana.e_commerce.domain.Order;
import com.railansantana.e_commerce.domain.Payment;
import com.railansantana.e_commerce.domain.Product;
import com.railansantana.e_commerce.domain.User;
import com.railansantana.e_commerce.domain.enums.OrderStatus;
import com.railansantana.e_commerce.repository.OrderRepository;
import com.railansantana.e_commerce.services.Exceptions.ResourceNotFoundException;
import com.railansantana.e_commerce.services.product.ProductService;
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

    private User getCurrentUser(String id) {
        User obj = userService.findById(id);
        if (obj == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return obj;
    }

    public List<Order> getOrders(String userId) {
        User user = getCurrentUser(userId);
        return user.getOrders();
    }

    public void createOrder(String userId, Order order) {
        User user = getCurrentUser(userId);
        order.setClient(user);
        user.getOrders().add(order);
        orderRepository.save(order);
        userService.save(user);
    }

    public void addProductToOrder(String userId, String orderId, String productId) throws MPException, MPApiException {
        try {


            User user = getCurrentUser(userId);
            System.err.println("product id" + productId);
            Product product = productService.findById(productId);
            Optional<Order> optionalOrder = user.getOrders().stream().filter(order -> order.getId().equals(orderId)).findFirst();

            if (optionalOrder.isEmpty()) {
                throw new ResourceNotFoundException("Order not found");
            }
            optionalOrder.get().addProduct(product);
            optionalOrder.get().calculateTotalPrice();
            optionalOrder.get().finalizeOrder(OrderStatus.WAITING_PAYMENT);


            Preference p = payment.configMercadoPago(product, 2);
            System.err.println(payment.convertObjectToJson(p.getResponse()));
            userService.save(user);
            orderRepository.save(optionalOrder.get());
        } catch (MPApiException e) {
            logger.error("Error during payment configuration: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void removeProductFromOrder(String userId, String orderId, String productId) {
        Product product = productService.findById(productId);
        User user = getCurrentUser(userId);
        Optional<Order> optionalOrder = user.getOrders().stream().filter(order -> order.getId().equals(orderId)).findFirst();

        if (optionalOrder.isEmpty()) {
            throw new ResourceNotFoundException("Order not found");
        }

//        Order order = optionalOrder.get();
        optionalOrder.get().removeProduct(product);
        optionalOrder.get().calculateTotalPrice();
        userService.save(user);
        orderRepository.save(optionalOrder.get());

    }

    public void removeOrder(String userId, String orderId) {
        User user = getCurrentUser(userId);
        user.getOrders().removeIf(order -> order.getId().equals(orderId));
        userService.save(user);
        orderRepository.deleteById(orderId);
    }
}
