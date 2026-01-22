package com.shophub.api.controller;

import com.shophub.api.exception.BadRequestException;
import com.shophub.api.model.Order;
import com.shophub.api.model.enums.PaymentMethod;
import com.shophub.api.security.SecurityUtils;
import com.shophub.api.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
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
            @Valid @RequestBody CheckoutRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        PaymentMethod method = null;
        if (request.paymentMethod() != null && !request.paymentMethod().isBlank()) {
            try {
                method = PaymentMethod.valueOf(request.paymentMethod().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid payment method: " + request.paymentMethod());
            }
        }
        Order order = orderService.checkout(
                userId,
                request.shippingAddress(),
                request.contactPhone(),
                request.contactEmail(),
                method);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    public record CheckoutRequest(
            @NotBlank(message = "Shipping address is required") String shippingAddress,
            String contactPhone,
            @NotBlank(message = "Contact email is required")
            @Email(message = "Invalid contact email format") String contactEmail,
            String paymentMethod
    ) {}
}
