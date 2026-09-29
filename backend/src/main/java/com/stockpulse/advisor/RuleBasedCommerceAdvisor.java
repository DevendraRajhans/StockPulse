package com.stockpulse.advisor;

import com.stockpulse.entity.Product;
import com.stockpulse.entity.enums.TriggerReason;
import com.stockpulse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RuleBasedCommerceAdvisor implements CommerceAdvisor {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public PricingRecommendation generatePricingRecommendation(Long productId, TriggerReason triggerReason) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        
        BigDecimal recommendedPrice = product.getPrice();
        String direction = "HOLD";
        Double confidence = 0.8;
        String reasoning = "";
        
        switch (triggerReason) {
            case INVENTORY_LOW:
                // Recommend approximately +10% price
                recommendedPrice = product.getPrice().multiply(BigDecimal.valueOf(1.10))
                        .setScale(2, RoundingMode.HALF_UP);
                direction = "INCREASE";
                reasoning = String.format(
                    "Stock (%d) is below minimum level (%d). Increasing price by 10%% to balance supply and demand.",
                    product.getQuantity(), product.getMinStockLevel());
                break;
                
            case DEMAND_SPIKE:
                // Check if demand velocity is greater than 2x category average
                double categoryAverage = calculateCategoryAverageDemandVelocity(product.getCategory());
                if (product.getDemandVelocity() > 2 * categoryAverage) {
                    // Recommend approximately +5% price
                    recommendedPrice = product.getPrice().multiply(BigDecimal.valueOf(1.05))
                            .setScale(2, RoundingMode.HALF_UP);
                    direction = "INCREASE";
                    reasoning = String.format(
                        "Demand velocity (%.1f) is more than 2x category average (%.1f). Increasing price by 5%% to optimize revenue.",
                        product.getDemandVelocity(), categoryAverage);
                } else {
                    reasoning = String.format(
                        "No significant demand spike detected. Current demand (%.1f) vs category average (%.1f).",
                        product.getDemandVelocity(), categoryAverage);
                }
                break;
                
            default:
                reasoning = "No specific trigger detected. Maintaining current price.";
                confidence = 0.5;
                break;
        }
        
        return new PricingRecommendation(recommendedPrice, direction, confidence, reasoning, triggerReason);
    }
    
    @Override
    public ReorderRecommendation generateReorderRecommendation(Long productId, TriggerReason triggerReason) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        
        // Reorder quantity = (minimum stock level × 3) - current stock
        int recommendedQuantity = (product.getMinStockLevel() * 3) - product.getQuantity();
        
        // Minimum recommended quantity = 1
        if (recommendedQuantity < 1) {
            recommendedQuantity = 1;
        }
        
        String reasoning = String.format(
            "Calculated reorder quantity: (minStockLevel %d × 3) - currentStock %d = %d. Minimum order quantity enforced.",
            product.getMinStockLevel(), product.getQuantity(), recommendedQuantity);
            
        Double confidence = 0.9;
        
        return new ReorderRecommendation(recommendedQuantity, confidence, reasoning, triggerReason);
    }
    
    private double calculateCategoryAverageDemandVelocity(String category) {
        List<Product> productsInCategory = productRepository.findByCategory(category);
        if (productsInCategory.isEmpty()) {
            return 0.0;
        }
        
        double sum = productsInCategory.stream()
                .mapToDouble(Product::getDemandVelocity)
                .sum();
                
        return sum / productsInCategory.size();
    }
}