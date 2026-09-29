package com.stockpulse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "min_stock_level", nullable = false)
    private Integer minStockLevel;

    @Column(name = "demand_velocity", nullable = false)
    private Double demandVelocity;

    @Column(nullable = false)
    private String category;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    // Constructors
    public Product() {}

    public Product(String name, String sku, BigDecimal price, BigDecimal cost, 
                   Integer quantity, Integer minStockLevel, Double demandVelocity, 
                   String category) {
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.cost = cost;
        this.quantity = quantity;
        this.minStockLevel = minStockLevel;
        this.demandVelocity = demandVelocity;
        this.category = category;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    public Double getDemandVelocity() {
        return demandVelocity;
    }

    public void setDemandVelocity(Double demandVelocity) {
        this.demandVelocity = demandVelocity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}