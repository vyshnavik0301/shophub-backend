package com.shophub.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "inventory_id")
    private UUID inventoryId;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "reserved_stock", nullable = false)
    private int reservedStock;

    @Column(name = "low_stock_threshold", nullable = false)
    private int lowStockThreshold;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;
}
