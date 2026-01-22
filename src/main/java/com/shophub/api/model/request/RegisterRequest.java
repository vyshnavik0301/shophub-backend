package com.shophub.api.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request body for POST /api/auth/register.
 */
public record RegisterRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 255)
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 255)
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 1, max = 255)
        String password
) {}
