package com.shophub.api.service;

import com.shophub.api.model.Inventory;
import com.shophub.api.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public void reduceStock(UUID productId, int quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product"));

        if (inventory.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock available");
        }

        inventory.setStock(inventory.getStock() - quantity);
        inventoryRepository.save(inventory);
    }
}
