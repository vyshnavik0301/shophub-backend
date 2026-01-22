package com.shophub.api.service;

import com.shophub.api.exception.BadRequestException;
import com.shophub.api.exception.ResourceNotFoundException;
import com.shophub.api.model.User;
import com.shophub.api.model.enums.UserRole;
import com.shophub.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Mock authentication service. No JWT or real password hashing yet.
 * TODO: Add password encoding and JWT tokens later.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User register(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Email already registered");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password); // TODO: encode password
        user.setRole(UserRole.CUSTOMER);
        return userRepository.save(user);
    }

    /**
     * Mock login: validates email/password and returns user. No real auth.
     */
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // TODO: validate password with encoder
        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Invalid credentials");
        }
        return user;
    }

    public User getCurrentUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
