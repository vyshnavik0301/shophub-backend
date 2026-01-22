package com.shophub.api.controller;

import com.shophub.api.model.Order;
import com.shophub.api.security.SecurityUtils;
import com.shophub.api.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrdersByUser() {
        UUID userId = SecurityUtils.getCurrentUserId();
        List<Order> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping("/orders/{orderId}/status")
    public ResponseEntity<Map<String, String>> getOrderStatus(@PathVariable UUID orderId) {
        return ResponseEntity.ok(Map.of("status", orderService.getOrderStatus(orderId).name()));
    }
}
