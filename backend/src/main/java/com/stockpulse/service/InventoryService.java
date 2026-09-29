package com.stockpulse.service;

import com.stockpulse.entity.InventorySnapshot;
import com.stockpulse.entity.Product;
import com.stockpulse.entity.enums.TriggerReason;
import com.stockpulse.event.InventoryChangeEvent;
import com.stockpulse.repository.InventorySnapshotRepository;
import com.stockpulse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private InventorySnapshotRepository inventorySnapshotRepository;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public Product updateStock(Long productId, int quantityChange) {
        // 1. Find the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        
        // 2. Validate the resulting stock does not become invalid
        int newQuantity = product.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new RuntimeException("Cannot reduce stock below zero. Current: " + product.getQuantity() + ", Change: " + quantityChange);
        }
        
        // 3. Update quantity and lastUpdated
        int previousQuantity = product.getQuantity();
        product.setQuantity(newQuantity);
        product.setLastUpdated(LocalDateTime.now());
        
        // 4. Save the product
        product = productRepository.save(product);
        
        // 5. Create an InventorySnapshot
        InventorySnapshot snapshot = new InventorySnapshot(
                productId, 
                newQuantity, 
                quantityChange < 0 ? "SALE" : "ADJUSTMENT");
        
        // 6. Save the snapshot
        inventorySnapshotRepository.save(snapshot);
        
        // 7. Detect relevant triggers
        List<TriggerReason> triggers = detectTriggers(product);
        
        // 8. Publish InventoryChangeEvent for each trigger
        for (TriggerReason trigger : triggers) {
            InventoryChangeEvent event = new InventoryChangeEvent(
                    this, 
                    productId, 
                    previousQuantity, 
                    newQuantity, 
                    trigger, 
                    LocalDateTime.now());
            eventPublisher.publishEvent(event);
        }
        
        return product;
    }
    
    private List<TriggerReason> detectTriggers(Product product) {
        List<TriggerReason> triggers = new ArrayList<>();
        
        // INVENTORY_LOW: current quantity < minStockLevel
        if (product.getQuantity() < product.getMinStockLevel()) {
            triggers.add(TriggerReason.INVENTORY_LOW);
        }
        
        // DEMAND_SPIKE: demand velocity > 2 × category average
        double categoryAverage = calculateCategoryAverageDemandVelocity(product.getCategory());
        if (product.getDemandVelocity() > 2 * categoryAverage) {
            triggers.add(TriggerReason.DEMAND_SPIKE);
        }
        
        return triggers;
    }
    
    private double calculateCategoryAverageDemandVelocity(String category) {
        var productsInCategory = productRepository.findByCategory(category);
        if (productsInCategory.isEmpty()) {
            return 0.0;
        }
        
        double sum = productsInCategory.stream()
                .mapToDouble(Product::getDemandVelocity)
                .sum();
                
        return sum / productsInCategory.size();
    }
}