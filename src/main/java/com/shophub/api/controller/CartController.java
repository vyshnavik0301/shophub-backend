package com.shophub.api.controller;

import com.shophub.api.model.Cart;
import com.shophub.api.model.CartItem;
import com.shophub.api.security.SecurityUtils;
import com.shophub.api.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<Cart> getCart() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    /** Request body: { "productId": "uuid", "quantity": 1 } */
    @PostMapping("/items")
    public ResponseEntity<CartItem> addItem(
            @Valid @RequestBody AddCartItemRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        CartItem item = cartService.addItemToCart(userId, request.productId(), request.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    /** Request body: { "quantity": 2 }. If quantity is 0, item is removed. */
    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartItem> updateItem(
            @PathVariable UUID itemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        CartItem item = cartService.updateItem(userId, itemId, request.quantity());
        if (item == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable UUID itemId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        cartService.removeItemFromCart(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    public record AddCartItemRequest(
            @NotNull(message = "Product ID is required") UUID productId,
            @Min(value = 1, message = "Quantity must be at least 1") int quantity
    ) {}

    public record UpdateCartItemRequest(
            @Min(value = 0, message = "Quantity must be 0 or greater") int quantity
    ) {}
}
