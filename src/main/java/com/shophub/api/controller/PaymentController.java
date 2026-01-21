package com.shophub.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Mock payment endpoint. Always returns success.
 * TODO: Integrate with real payment gateway later.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping("/mock")
    public ResponseEntity<Map<String, Object>> mockPayment() {
        return ResponseEntity.ok(Map.of("success", true, "message", "Payment successful"));
    }
}
