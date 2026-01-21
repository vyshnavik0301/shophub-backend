package com.shophub.api.service;

import com.shophub.api.model.*;
import com.shophub.api.model.enums.OrderStatus;
import com.shophub.api.model.enums.PaymentMethod;
import com.shophub.api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PaymentService paymentService;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        CartRepository cartRepository,
                        CartItemRepository cartItemRepository,
                        PaymentService paymentService,
                        UserRepository userRepository,
                        InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.paymentService = paymentService;
        this.userRepository = userRepository;
        this.inventoryService = inventoryService;
    }

    /**
     * Checkout: convert cart to order, reduce product stock, process mock payment, clear cart.
     */
    @Transactional
    public Order checkout(UUID userId, String shippingAddress, String contactPhone, String contactEmail, PaymentMethod paymentMethod) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Reduce stock before creating order (fail fast if insufficient)
        for (CartItem ci : cartItems) {
            inventoryService.reduceStock(ci.getProduct().getProductId(), ci.getQuantity());
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(cart.getTotalAmount());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(shippingAddress);
        order.setContactPhone(contactPhone);
        order.setContactEmail(contactEmail);
        order = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getPriceAtTime());
            orderItems.add(orderItemRepository.save(orderItem));
        }
        order.setOrderItems(orderItems);

        paymentService.mockPayment(order.getOrderId(), paymentMethod);

        cartItemRepository.deleteAll(cartItems);
        cart.setTotalAmount(0.0);
        cartRepository.save(cart);

        return order;
    }

    public List<Order> getOrdersByUser(UUID userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<Order> getOrderById(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    public Optional<OrderStatus> getOrderStatus(UUID orderId) {
        return orderRepository.findById(orderId).map(Order::getStatus);
    }
}
