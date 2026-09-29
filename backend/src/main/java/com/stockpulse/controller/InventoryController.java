package com.stockpulse.controller;

import com.stockpulse.entity.Product;
import com.stockpulse.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "http://localhost:5173")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    @PostMapping("/{productId}/sale")
    public Product recordSale(@PathVariable Long productId) {
        // Simulate a sale by reducing stock by 1
        return inventoryService.updateStock(productId, -1);
    }
    
    @PostMapping("/{productId}/adjust")
    public Product adjustStock(@PathVariable Long productId, @RequestParam int quantityChange) {
        // Adjust stock by the specified amount
        return inventoryService.updateStock(productId, quantityChange);
    }
}