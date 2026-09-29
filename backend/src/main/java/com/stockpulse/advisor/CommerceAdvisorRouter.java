package com.stockpulse.advisor;

import com.stockpulse.entity.enums.TriggerReason;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class CommerceAdvisorRouter implements CommerceAdvisor {
    
    public enum Strategy {
        RULE,
        AI
    }
    
    private final AtomicReference<Strategy> currentStrategy = new AtomicReference<>(Strategy.RULE);
    
    @Autowired
    private RuleBasedCommerceAdvisor ruleBasedAdvisor;
    
    @Autowired
    private AICommerceAdvisor aiAdvisor;
    
    public void setStrategy(Strategy strategy) {
        this.currentStrategy.set(strategy);
    }
    
    public Strategy getCurrentStrategy() {
        return this.currentStrategy.get();
    }
    
    @Override
    public PricingRecommendation generatePricingRecommendation(Long productId, TriggerReason triggerReason) {
        Strategy strategy = currentStrategy.get();
        switch (strategy) {
            case AI:
                return aiAdvisor.generatePricingRecommendation(productId, triggerReason);
            case RULE:
            default:
                return ruleBasedAdvisor.generatePricingRecommendation(productId, triggerReason);
        }
    }
    
    @Override
    public ReorderRecommendation generateReorderRecommendation(Long productId, TriggerReason triggerReason) {
        Strategy strategy = currentStrategy.get();
        switch (strategy) {
            case AI:
                return aiAdvisor.generateReorderRecommendation(productId, triggerReason);
            case RULE:
            default:
                return ruleBasedAdvisor.generateReorderRecommendation(productId, triggerReason);
        }
    }
}