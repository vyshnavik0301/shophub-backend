package com.shophub.api.service;

import com.shophub.api.model.Order;
import com.shophub.api.model.Payment;
import com.shophub.api.model.enums.PaymentMethod;
import com.shophub.api.model.enums.PaymentStatus;
import com.shophub.api.repository.OrderRepository;
import com.shophub.api.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Payment mockPayment(UUID orderId) {
        return mockPayment(orderId, PaymentMethod.CREDIT_CARD);
    }

    @Transactional
    public Payment mockPayment(UUID orderId, PaymentMethod method) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setMethod(method != null ? method : PaymentMethod.CREDIT_CARD);
        payment.setStatus(PaymentStatus.COMPLETED);

        return paymentRepository.save(payment);
    }
}
