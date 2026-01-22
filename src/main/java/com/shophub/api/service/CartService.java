package com.shophub.api.service;

import com.shophub.api.exception.BadRequestException;
import com.shophub.api.exception.ResourceNotFoundException;
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

    /**
     * Returns the cart or throws if the user has no cart.
     */
    public Cart getCart(UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));
    }

    /**
     * Add item to cart. If product already exists, increase quantity and set price to current.
     */
    @Transactional
    public CartItem addItemToCart(UUID userId, UUID productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setTotalAmount(0.0);
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Optional<CartItem> existing = cartItemRepository.findByCartIdAndProductId(cart.getCartId(), productId);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            double oldLine = item.getPriceAtTime() * item.getQuantity();
            item.setQuantity(item.getQuantity() + quantity);
            item.setPriceAtTime(product.getPrice()); // use current price for the line
            double newLine = item.getPriceAtTime() * item.getQuantity();
            cart.setTotalAmount(cart.getTotalAmount() - oldLine + newLine);
            cartRepository.save(cart);
            return cartItemRepository.save(item);
        }

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setPriceAtTime(product.getPrice());
        cart.setTotalAmount(cart.getTotalAmount() + (product.getPrice() * quantity));
        cartRepository.save(cart);
        return cartItemRepository.save(cartItem);
    }

    /**
     * Update cart item quantity. If quantity is 0, remove the item.
     */
    @Transactional
    public CartItem updateItem(UUID userId, UUID itemId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (!item.getCart().getCartId().equals(cart.getCartId())) {
            throw new BadRequestException("Cart item does not belong to user's cart");
        }
        if (quantity <= 0) {
            removeItemFromCart(userId, itemId);
            return null; // removed
        }
        double oldLine = item.getPriceAtTime() * item.getQuantity();
        item.setQuantity(quantity);
        double newLine = item.getPriceAtTime() * item.getQuantity();
        cart.setTotalAmount(cart.getTotalAmount() - oldLine + newLine);
        cartRepository.save(cart);
        return cartItemRepository.save(item);
    }

    @Transactional
    public void removeItemFromCart(UUID userId, UUID itemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (!item.getCart().getCartId().equals(cart.getCartId())) {
            throw new BadRequestException("Cart item does not belong to user's cart");
        }
        double itemTotal = item.getPriceAtTime() * item.getQuantity();
        cart.setTotalAmount(cart.getTotalAmount() - itemTotal);
        cartItemRepository.delete(item);
        cartRepository.save(cart);
    }

}
