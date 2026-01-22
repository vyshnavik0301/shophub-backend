package com.shophub.api.service;

import com.shophub.api.exception.BadRequestException;
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
        var opt = inventoryRepository.findByProductId(productId);
        if (opt.isEmpty()) {
            // TODO: Product has no Inventory record; skip reduce (treat as unlimited). Create Inventory for products that need stock control.
            return;
        }
        Inventory inventory = opt.get();
        if (inventory.getStock() < quantity) {
            throw new BadRequestException("Insufficient stock available");
        }
        inventory.setStock(inventory.getStock() - quantity);
        inventoryRepository.save(inventory);
    }
}
