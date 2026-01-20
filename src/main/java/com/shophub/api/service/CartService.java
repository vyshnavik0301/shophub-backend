package com.shophub.api.service;

import com.shophub.api.model.Cart;
import com.shophub.api.model.CartItem;
import com.shophub.api.model.Product;
import com.shophub.api.model.User;
import com.shophub.api.repository.CartItemRepository;
import com.shophub.api.repository.CartRepository;
import com.shophub.api.repository.ProductRepository;
import com.shophub.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Optional<Cart> getCartByUserId(UUID userId) {
        return cartRepository.findByUserId(userId);
    }

    @Transactional
    public CartItem addItemToCart(UUID userId, UUID productId, int quantity) {
        // Get or create cart for user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setTotalAmount(0.0);
                    return cartRepository.save(newCart);
                });

        // Get product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Create cart item
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setPriceAtTime(product.getPrice());

        // Update cart total
        cart.setTotalAmount(cart.getTotalAmount() + (product.getPrice() * quantity));

        cartRepository.save(cart);
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeItemFromCart(UUID cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        Cart cart = cartItem.getCart();
        double itemTotal = cartItem.getPriceAtTime() * cartItem.getQuantity();
        cart.setTotalAmount(cart.getTotalAmount() - itemTotal);

        cartItemRepository.delete(cartItem);
        cartRepository.save(cart);
    }
}
