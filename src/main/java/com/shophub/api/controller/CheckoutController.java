package com.shophub.api.controller;

import com.shophub.api.model.Order;
import com.shophub.api.model.enums.PaymentMethod;
import com.shophub.api.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}")
public class CheckoutController {

    private final OrderService orderService;

    public CheckoutController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Checkout: request body { "shippingAddress", "contactPhone", "contactEmail", "paymentMethod" }.
     * paymentMethod: CREDIT_CARD, DEBIT_CARD, PAYPAL, BANK_TRANSFER, CASH_ON_DELIVERY
     */
    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(
            @PathVariable UUID userId,
            @RequestBody CheckoutRequest request) {
        try {
            if (request == null) {
                return ResponseEntity.badRequest().build();
            }
            PaymentMethod method = null;
            if (request.paymentMethod() != null && !request.paymentMethod().isBlank()) {
                try {
                    method = PaymentMethod.valueOf(request.paymentMethod().trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().build();
                }
            }
            Order order = orderService.checkout(
                    userId,
                    request.shippingAddress(),
                    request.contactPhone(),
                    request.contactEmail(),
                    method);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public record CheckoutRequest(String shippingAddress, String contactPhone, String contactEmail, String paymentMethod) {}
}
