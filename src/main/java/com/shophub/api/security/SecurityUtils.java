package com.shophub.api.security;

import com.shophub.api.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * Utility class for security-related operations.
 */
public class SecurityUtils {

    /**
     * Get the current authenticated user ID from SecurityContext.
     * 
     * @return User ID of the authenticated user
     * @throws IllegalStateException if user is not authenticated or user details are invalid
     */
    public static UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        
        if (!(authentication.getPrincipal() instanceof ShopHubUserDetails)) {
            throw new IllegalStateException("Invalid authentication principal");
        }
        
        ShopHubUserDetails userDetails = (ShopHubUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        return user.getUserId();
    }

    /**
     * Get the current authenticated user from SecurityContext.
     * 
     * @return User entity of the authenticated user
     * @throws IllegalStateException if user is not authenticated or user details are invalid
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        
        if (!(authentication.getPrincipal() instanceof ShopHubUserDetails)) {
            throw new IllegalStateException("Invalid authentication principal");
        }
        
        ShopHubUserDetails userDetails = (ShopHubUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
