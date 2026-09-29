package com.stockpulse.service;

import com.stockpulse.advisor.CommerceAdvisor;
import com.stockpulse.advisor.CommerceAdvisorRouter;
import com.stockpulse.entity.Product;
import com.stockpulse.entity.enums.TriggerReason;
import com.stockpulse.event.InventoryChangeEvent;
import com.stockpulse.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EventProcessingService {
    
    private static final Logger logger = LoggerFactory.getLogger(EventProcessingService.class);
    
    @Autowired
    private CommerceAdvisorRouter commerceAdvisorRouter;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private SuggestionService suggestionService;
    
    @Async
    public void processInventoryChangeEvent(InventoryChangeEvent event) {
        try {
            Long productId = event.getProductId();
            TriggerReason triggerReason = event.getTriggerReason();
            
            logger.info("Processing inventory change event for product ID: {}, trigger: {}", 
                       productId, triggerReason);
            
            // Check if product exists
            Product product = productRepository.findById(productId).orElse(null);
            if (product == null) {
                logger.warn("Product not found for ID: {}", productId);
                return;
            }
            
            // Generate pricing recommendation using the configured advisor
            CommerceAdvisor.PricingRecommendation pricingRec = 
                commerceAdvisorRouter.generatePricingRecommendation(productId, triggerReason);
            
            logger.info("Generated pricing recommendation for product {}: {} -> {}, Reason: {}", 
                       product.getName(), product.getPrice(), pricingRec.getRecommendedPrice(), 
                       pricingRec.getReasoning());
            
            // Save pricing recommendation as PENDING suggestion
            suggestionService.createPricingSuggestion(
                productId,
                product.getPrice(),
                pricingRec.getRecommendedPrice(),
                pricingRec.getDirection(),
                pricingRec.getConfidence(),
                pricingRec.getReasoning(),
                triggerReason,
                false // TODO: Implement risk flag detection
            );
            
            // Generate reorder recommendation using the configured advisor
            CommerceAdvisor.ReorderRecommendation reorderRec = 
                commerceAdvisorRouter.generateReorderRecommendation(productId, triggerReason);
            
            logger.info("Generated reorder recommendation for product {}: {} units, Reason: {}", 
                       product.getName(), reorderRec.getRecommendedQuantity(), 
                       reorderRec.getReasoning());
            
            // Save reorder recommendation as PENDING suggestion
            suggestionService.createReorderSuggestion(
                productId,
                product.getQuantity(),
                reorderRec.getRecommendedQuantity(),
                reorderRec.getConfidence(),
                reorderRec.getReasoning(),
                triggerReason,
                false // TODO: Implement risk flag detection
            );
            
        } catch (Exception e) {
            logger.error("Error processing inventory change event for product ID: " + 
                        event.getProductId(), e);
        }
    }
}