package com.stockpulse.event;

import com.stockpulse.entity.enums.TriggerReason;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class InventoryChangeEvent extends ApplicationEvent {
    private final Long productId;
    private final int previousQuantity;
    private final int newQuantity;
    private final TriggerReason triggerReason;
    private final LocalDateTime eventTimestamp;
    
    public InventoryChangeEvent(Object source, Long productId, int previousQuantity, 
                              int newQuantity, TriggerReason triggerReason, LocalDateTime eventTimestamp) {
        super(source);
        this.productId = productId;
        this.previousQuantity = previousQuantity;
        this.newQuantity = newQuantity;
        this.triggerReason = triggerReason;
        this.eventTimestamp = eventTimestamp;
    }
    
    // Getters
    public Long getProductId() {
        return productId;
    }
    
    public int getPreviousQuantity() {
        return previousQuantity;
    }
    
    public int getNewQuantity() {
        return newQuantity;
    }
    
    public TriggerReason getTriggerReason() {
        return triggerReason;
    }
    
    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }
}