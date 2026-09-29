package com.stockpulse.event;

import com.stockpulse.service.EventProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryChangeEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryChangeEventListener.class);
    
    @Autowired
    private EventProcessingService eventProcessingService;
    
    @EventListener
    public void handleInventoryChangeEvent(InventoryChangeEvent event) {
        logger.info("Received inventory change event for product ID: {}, trigger: {}", 
                   event.getProductId(), event.getTriggerReason());
        
        // Delegate to EventProcessingService for async processing
        eventProcessingService.processInventoryChangeEvent(event);
    }
}