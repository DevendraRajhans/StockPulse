package com.stockpulse.service;

import com.stockpulse.entity.PricingSuggestion;
import com.stockpulse.entity.ReorderSuggestion;
import com.stockpulse.entity.Product;
import com.stockpulse.entity.enums.SuggestionStatus;
import com.stockpulse.entity.enums.TriggerReason;
import com.stockpulse.exception.SuggestionNotFoundException;
import com.stockpulse.repository.PricingSuggestionRepository;
import com.stockpulse.repository.ReorderSuggestionRepository;
import com.stockpulse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SuggestionService {
    
    private static final Logger logger = LoggerFactory.getLogger(SuggestionService.class);
    public List<PricingSuggestion> getPendingPricingSuggestions() {
        List<PricingSuggestion> suggestions = pricingSuggestionRepository.findByStatusOrderByCreatedAtDesc(SuggestionStatus.PENDING);
        for (PricingSuggestion suggestion : suggestions) {
            productRepository.findById(suggestion.getProductId()).ifPresent(product -> {
                suggestion.setCurrentPrice(product.getPrice());
            });
        }
        return suggestions;
    }
    
    public List<ReorderSuggestion> getPendingReorderSuggestions() {
        List<ReorderSuggestion> suggestions = reorderSuggestionRepository.findByStatusOrderByCreatedAtDesc(SuggestionStatus.PENDING);
        for (ReorderSuggestion suggestion : suggestions) {
            productRepository.findById(suggestion.getProductId()).ifPresent(product -> {
                suggestion.setCurrentStock(product.getQuantity());
            });
        }
        return suggestions;
    }
    
    public List<PricingSuggestion> getAllPricingSuggestions() {
        return pricingSuggestionRepository.findAll();
    }
    
    public List<ReorderSuggestion> getAllReorderSuggestions() {
        return reorderSuggestionRepository.findAll();
    }
    
    public PricingSuggestion createPricingSuggestion(Long productId, BigDecimal currentPrice, 
            BigDecimal recommendedPrice, String direction, Double confidence, String reasoning, 
            TriggerReason triggerReason, Boolean hasRiskFlag) {
        
        // Check for existing PENDING suggestion for this product and trigger
        List<PricingSuggestion> existingSuggestions = pricingSuggestionRepository
                .findByProductIdAndTriggerReasonAndStatus(productId, triggerReason, SuggestionStatus.PENDING);
        
        if (!existingSuggestions.isEmpty()) {
            logger.info("PENDING pricing suggestion already exists for product {} with trigger {}, returning existing", 
                       productId, triggerReason);
            return existingSuggestions.get(0);
        }
        
        PricingSuggestion suggestion = new PricingSuggestion();
        suggestion.setProductId(productId);
        suggestion.setRecommendedPrice(recommendedPrice);
        suggestion.setDirection(direction);
        suggestion.setConfidence(confidence);
        suggestion.setReasoning(reasoning);
        suggestion.setStatus(SuggestionStatus.PENDING);
        suggestion.setTriggerReason(triggerReason);
        suggestion.setCreatedAt(LocalDateTime.now());
        suggestion.setHasRiskFlag(hasRiskFlag != null ? hasRiskFlag : false);
        
        return pricingSuggestionRepository.save(suggestion);
    }
    
    public ReorderSuggestion createReorderSuggestion(Long productId, Integer currentStock,
            Integer recommendedQuantity, Double confidence, String reasoning, 
            TriggerReason triggerReason, Boolean hasRiskFlag) {
        
        // Check for existing PENDING suggestion for this product and trigger
        List<ReorderSuggestion> existingSuggestions = reorderSuggestionRepository
                .findByProductIdAndTriggerReasonAndStatus(productId, triggerReason, SuggestionStatus.PENDING);
        
        if (!existingSuggestions.isEmpty()) {
            logger.info("PENDING reorder suggestion already exists for product {} with trigger {}, returning existing", 
                       productId, triggerReason);
            return existingSuggestions.get(0);
        }
        
        ReorderSuggestion suggestion = new ReorderSuggestion();
        suggestion.setProductId(productId);
        suggestion.setRecommendedQuantity(recommendedQuantity);
        suggestion.setConfidence(confidence);
        suggestion.setReasoning(reasoning);
        suggestion.setStatus(SuggestionStatus.PENDING);
        suggestion.setTriggerReason(triggerReason);
        suggestion.setCreatedAt(LocalDateTime.now());
        suggestion.setHasRiskFlag(hasRiskFlag != null ? hasRiskFlag : false);
        
        return reorderSuggestionRepository.save(suggestion);
    }
    
    public PricingSuggestion approvePricingSuggestion(Long suggestionId, String approvedBy) {
        PricingSuggestion suggestion = pricingSuggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new SuggestionNotFoundException("Pricing suggestion not found: " + suggestionId));
        
        // Validate state transition
        if (suggestion.getStatus() != SuggestionStatus.PENDING) {
            throw new IllegalStateException("Cannot approve suggestion with status: " + suggestion.getStatus());
        }
        
        // Update suggestion status
        suggestion.setStatus(SuggestionStatus.APPROVED);
        suggestion.setApprovedAt(LocalDateTime.now());
        if (approvedBy != null) {
            suggestion.setApprovedBy(approvedBy);
        }
        
        // Apply the recommendation to the product
        Product product = productRepository.findById(suggestion.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + suggestion.getProductId()));
        product.setPrice(suggestion.getRecommendedPrice());
        product.setLastUpdated(LocalDateTime.now());
        productRepository.save(product);
        
        logger.info("Approved pricing suggestion {} for product {} - price changed to {}", 
                   suggestionId, suggestion.getProductId(), suggestion.getRecommendedPrice());
        
        return pricingSuggestionRepository.save(suggestion);
    }
    
    public PricingSuggestion rejectPricingSuggestion(Long suggestionId, String rejectedBy) {
        PricingSuggestion suggestion = pricingSuggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new SuggestionNotFoundException("Pricing suggestion not found: " + suggestionId));
        
        // Validate state transition
        if (suggestion.getStatus() != SuggestionStatus.PENDING) {
            throw new IllegalStateException("Cannot reject suggestion with status: " + suggestion.getStatus());
        }
        
        // Update suggestion status
        suggestion.setStatus(SuggestionStatus.REJECTED);
        
        logger.info("Rejected pricing suggestion {} for product {}", suggestionId, suggestion.getProductId());
        
        return pricingSuggestionRepository.save(suggestion);
    }
    
    public ReorderSuggestion approveReorderSuggestion(Long suggestionId, String approvedBy) {
        ReorderSuggestion suggestion = reorderSuggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new SuggestionNotFoundException("Reorder suggestion not found: " + suggestionId));
        
        // Validate state transition
        if (suggestion.getStatus() != SuggestionStatus.PENDING) {
            throw new IllegalStateException("Cannot approve suggestion with status: " + suggestion.getStatus());
        }
        
        // Update suggestion status
        suggestion.setStatus(SuggestionStatus.APPROVED);
        suggestion.setApprovedAt(LocalDateTime.now());
        if (approvedBy != null) {
            suggestion.setApprovedBy(approvedBy);
        }
        
        // For demo purposes, we'll just log the reorder recommendation
        // In a real system, this would trigger procurement processes
        logger.info("Approved reorder suggestion {} for product {} - quantity: {}", 
                   suggestionId, suggestion.getProductId(), suggestion.getRecommendedQuantity());
        
        return reorderSuggestionRepository.save(suggestion);
    }
    
    public ReorderSuggestion rejectReorderSuggestion(Long suggestionId, String rejectedBy) {
        ReorderSuggestion suggestion = reorderSuggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new SuggestionNotFoundException("Reorder suggestion not found: " + suggestionId));
        
        // Validate state transition
        if (suggestion.getStatus() != SuggestionStatus.PENDING) {
            throw new IllegalStateException("Cannot reject suggestion with status: " + suggestion.getStatus());
        }
        
        // Update suggestion status
        suggestion.setStatus(SuggestionStatus.REJECTED);
        
        logger.info("Rejected reorder suggestion {} for product {}", suggestionId, suggestion.getProductId());
        
        return reorderSuggestionRepository.save(suggestion);
    }
    
    @Autowired
    private PricingSuggestionRepository pricingSuggestionRepository;
    
    @Autowired
    private ReorderSuggestionRepository reorderSuggestionRepository;
    
    @Autowired
    private ProductRepository productRepository;
}