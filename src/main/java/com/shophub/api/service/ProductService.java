package com.shophub.api.service;

import com.shophub.api.exception.BadRequestException;
import com.shophub.api.exception.ResourceNotFoundException;
import com.shophub.api.model.Category;
import com.shophub.api.model.Product;
import com.shophub.api.repository.CategoryRepository;
import com.shophub.api.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Product createProduct(Product product) {
        UUID categoryId = product.getCategory() != null ? product.getCategory().getCategoryId() : null;
        if (categoryId == null) {
            throw new BadRequestException("Category is required");
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
        product.setCategory(category);
        product.setProductId(null);
        product.setInventory(null);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }
}
