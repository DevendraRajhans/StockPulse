package com.stockpulse.advisor;

import com.stockpulse.entity.enums.TriggerReason;

import java.math.BigDecimal;

public interface CommerceAdvisor {
    
    PricingRecommendation generatePricingRecommendation(
            Long productId, 
            TriggerReason triggerReason);
    
    ReorderRecommendation generateReorderRecommendation(
            Long productId, 
            TriggerReason triggerReason);
    
    class PricingRecommendation {
        private BigDecimal recommendedPrice;
        private String direction;
        private Double confidence;
        private String reasoning;
        private TriggerReason triggerReason;
        
        public PricingRecommendation() {}
        
        public PricingRecommendation(BigDecimal recommendedPrice, String direction, 
                                   Double confidence, String reasoning, TriggerReason triggerReason) {
            this.recommendedPrice = recommendedPrice;
            this.direction = direction;
            this.confidence = confidence;
            this.reasoning = reasoning;
            this.triggerReason = triggerReason;
        }
        
        // Getters and setters
        public BigDecimal getRecommendedPrice() { return recommendedPrice; }
        public void setRecommendedPrice(BigDecimal recommendedPrice) { this.recommendedPrice = recommendedPrice; }
        
        public String getDirection() { return direction; }
        public void setDirection(String direction) { this.direction = direction; }
        
        public Double getConfidence() { return confidence; }
        public void setConfidence(Double confidence) { this.confidence = confidence; }
        
        public String getReasoning() { return reasoning; }
        public void setReasoning(String reasoning) { this.reasoning = reasoning; }
        
        public TriggerReason getTriggerReason() { return triggerReason; }
        public void setTriggerReason(TriggerReason triggerReason) { this.triggerReason = triggerReason; }
    }
    
    class ReorderRecommendation {
        private Integer recommendedQuantity;
        private Double confidence;
        private String reasoning;
        private TriggerReason triggerReason;
        
        public ReorderRecommendation() {}
        
        public ReorderRecommendation(Integer recommendedQuantity, Double confidence, 
                                   String reasoning, TriggerReason triggerReason) {
            this.recommendedQuantity = recommendedQuantity;
            this.confidence = confidence;
            this.reasoning = reasoning;
            this.triggerReason = triggerReason;
        }
        
        // Getters and setters
        public Integer getRecommendedQuantity() { return recommendedQuantity; }
        public void setRecommendedQuantity(Integer recommendedQuantity) { this.recommendedQuantity = recommendedQuantity; }
        
        public Double getConfidence() { return confidence; }
        public void setConfidence(Double confidence) { this.confidence = confidence; }
        
        public String getReasoning() { return reasoning; }
        public void setReasoning(String reasoning) { this.reasoning = reasoning; }
        
        public TriggerReason getTriggerReason() { return triggerReason; }
        public void setTriggerReason(TriggerReason triggerReason) { this.triggerReason = triggerReason; }
    }
}