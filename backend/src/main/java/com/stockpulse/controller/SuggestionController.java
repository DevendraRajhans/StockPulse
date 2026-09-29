package com.stockpulse.controller;

import com.stockpulse.entity.PricingSuggestion;
import com.stockpulse.entity.ReorderSuggestion;
import com.stockpulse.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend requests
public class SuggestionController {
    @Autowired
    private SuggestionService suggestionService;
    
    @GetMapping("/pending")
    public ResponseEntity<List<?>> getPendingSuggestions() {
        List<PricingSuggestion> pricingSuggestions = suggestionService.getPendingPricingSuggestions();
        List<ReorderSuggestion> reorderSuggestions = suggestionService.getPendingReorderSuggestions();
        
        // Combine both lists - in a real app you might want to return them separately
        // or create a unified suggestion DTO
        return ResponseEntity.ok(List.of(pricingSuggestions, reorderSuggestions));
    }
    
    /**
     * Get all pending pricing suggestions
     */
    @GetMapping("/pricing/pending")
    public ResponseEntity<List<PricingSuggestion>> getPendingPricingSuggestions() {
        List<PricingSuggestion> suggestions = suggestionService.getPendingPricingSuggestions();
        return ResponseEntity.ok(suggestions);
    }
    
    /**
     * Get all pending reorder suggestions
     */
    @GetMapping("/reorder/pending")
    public ResponseEntity<List<ReorderSuggestion>> getPendingReorderSuggestions() {
        List<ReorderSuggestion> suggestions = suggestionService.getPendingReorderSuggestions();
        return ResponseEntity.ok(suggestions);
    }
    
    /**
     * Approve a pricing suggestion
     */
    @PostMapping("/pricing/{id}/approve")
    public ResponseEntity<?> approvePricingSuggestion(@PathVariable("id") Long id) {
        try {
            // In a real implementation, we would get the authenticated user
            // For demo purposes, we'll use a placeholder
            PricingSuggestion suggestion = suggestionService.approvePricingSuggestion(id, "demo-user");
            return ResponseEntity.ok(suggestion);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Reject a pricing suggestion
     */
    @PostMapping("/pricing/{id}/reject")
    public ResponseEntity<?> rejectPricingSuggestion(@PathVariable("id") Long id) {
        try {
            // In a real implementation, we would get the authenticated user
            // For demo purposes, we'll use a placeholder
            PricingSuggestion suggestion = suggestionService.rejectPricingSuggestion(id, "demo-user");
            return ResponseEntity.ok(suggestion);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Approve a reorder suggestion
     */
    @PostMapping("/reorder/{id}/approve")
    public ResponseEntity<?> approveReorderSuggestion(@PathVariable("id") Long id) {
        try {
            // In a real implementation, we would get the authenticated user
            // For demo purposes, we'll use a placeholder
            ReorderSuggestion suggestion = suggestionService.approveReorderSuggestion(id, "demo-user");
            return ResponseEntity.ok(suggestion);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Reject a reorder suggestion
     */
    @PostMapping("/reorder/{id}/reject")
    public ResponseEntity<?> rejectReorderSuggestion(@PathVariable("id") Long id) {
        try {
            // In a real implementation, we would get the authenticated user
            // For demo purposes, we'll use a placeholder
            ReorderSuggestion suggestion = suggestionService.rejectReorderSuggestion(id, "demo-user");
            return ResponseEntity.ok(suggestion);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}