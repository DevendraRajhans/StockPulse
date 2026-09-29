package com.stockpulse.entity;

import com.stockpulse.entity.enums.SuggestionStatus;
import com.stockpulse.entity.enums.TriggerReason;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pricing_suggestions")
public class PricingSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(precision = 10, scale = 2)
    private BigDecimal recommendedPrice;

    @Column(length = 50)
    private String direction;

    @Column
    private Double confidence;

    @Column(length = 1000)
    private String reasoning;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuggestionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TriggerReason triggerReason;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private String approvedBy;

    @Column
    private LocalDateTime approvedAt;

    @Column
    private Boolean hasRiskFlag;

    @Transient
    private BigDecimal currentPrice;

    // Constructors
    public PricingSuggestion() {}

    public PricingSuggestion(Long productId, BigDecimal recommendedPrice, String direction, 
                           Double confidence, String reasoning, TriggerReason triggerReason) {
        this.productId = productId;
        this.recommendedPrice = recommendedPrice;
        this.direction = direction;
        this.confidence = confidence;
        this.reasoning = reasoning;
        this.triggerReason = triggerReason;
        this.status = SuggestionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.hasRiskFlag = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getRecommendedPrice() {
        return recommendedPrice;
    }

    public void setRecommendedPrice(BigDecimal recommendedPrice) {
        this.recommendedPrice = recommendedPrice;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    public SuggestionStatus getStatus() {
        return status;
    }

    public void setStatus(SuggestionStatus status) {
        this.status = status;
    }

    public TriggerReason getTriggerReason() {
        return triggerReason;
    }

    public void setTriggerReason(TriggerReason triggerReason) {
        this.triggerReason = triggerReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Boolean getHasRiskFlag() {
        return hasRiskFlag;
    }

    public void setHasRiskFlag(Boolean hasRiskFlag) {
        this.hasRiskFlag = hasRiskFlag;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }
}