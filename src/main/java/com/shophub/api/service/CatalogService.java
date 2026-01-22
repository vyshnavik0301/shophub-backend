package com.shophub.api.service;

import com.shophub.api.exception.ResourceNotFoundException;
import com.shophub.api.model.Product;
import com.shophub.api.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CatalogService {

    private final ProductRepository productRepository;

    public CatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
    }

    @Transactional(readOnly = true)
    public List<Product> search(String query) {
        if (query == null || query.isBlank()) {
            return productRepository.findAll();
        }
        return productRepository.findBySearchQuery(query.trim());
    }

    @Transactional(readOnly = true)
    public List<Product> filter(String category, Double minPrice, Double maxPrice) {
        String cat = (category == null || category.isBlank()) ? null : category.trim();
        return productRepository.filterProducts(cat, minPrice, maxPrice);
    }
}
