package com.shophub.api.repository;

import com.shophub.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") UUID categoryId);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Product> findBySearchQuery(@Param("q") String q);

    @Query("SELECT p FROM Product p WHERE (:categoryName IS NULL OR :categoryName = '' OR p.category.name = :categoryName) AND (:min IS NULL OR p.price >= :min) AND (:max IS NULL OR p.price <= :max)")
    List<Product> filterProducts(@Param("categoryName") String categoryName, @Param("min") Double min, @Param("max") Double max);
}
