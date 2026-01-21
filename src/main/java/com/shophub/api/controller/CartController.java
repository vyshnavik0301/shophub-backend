package com.shophub.api.controller;

import com.shophub.api.model.Cart;
import com.shophub.api.model.CartItem;
import com.shophub.api.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(@PathVariable UUID userId) {
        return cartService.getCartByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Request body: { "productId": "uuid", "quantity": 1 } */
    @PostMapping("/items")
    public ResponseEntity<CartItem> addItem(
            @PathVariable UUID userId,
            @RequestBody AddCartItemRequest request) {
        try {
            if (request == null || request.productId() == null || request.quantity() < 1) {
                return ResponseEntity.badRequest().build();
            }
            CartItem item = cartService.addItemToCart(userId, request.productId(), request.quantity());
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /** Request body: { "quantity": 2 }. If quantity is 0, item is removed. */
    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartItem> updateItem(
            @PathVariable UUID userId,
            @PathVariable UUID itemId,
            @RequestBody UpdateCartItemRequest request) {
        try {
            if (request == null) {
                return ResponseEntity.badRequest().build();
            }
            CartItem item = cartService.updateItem(userId, itemId, request.quantity());
            if (item == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable UUID userId,
            @PathVariable UUID itemId) {
        try {
            cartService.removeItemFromCart(userId, itemId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public record AddCartItemRequest(UUID productId, int quantity) {}
    public record UpdateCartItemRequest(int quantity) {}
}
