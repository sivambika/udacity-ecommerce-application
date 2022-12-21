package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/submit/{username}")
    public ResponseEntity<UserOrder> submit(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            logger.error("User not found for username, {}.", username);
            return ResponseEntity.notFound().build();
        }
        UserOrder order = UserOrder.createFromCart(user.getCart());
        orderRepository.save(order);
        logger.info("Order successfully submitted for username, {}, order id, {}", username, order.getId());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/history/{username}")
    public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            logger.error("User not found for username, {}.", username);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderRepository.findByUser(user));
    }
}
