package com.stockpulse.repository;

import com.stockpulse.entity.PricingSuggestion;
import com.stockpulse.entity.enums.SuggestionStatus;
import com.stockpulse.entity.enums.TriggerReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricingSuggestionRepository extends JpaRepository<PricingSuggestion, Long> {
    List<PricingSuggestion> findByProductIdAndStatusOrderByCreatedAtDesc(Long productId, SuggestionStatus status);
    List<PricingSuggestion> findByStatusOrderByCreatedAtDesc(SuggestionStatus status);
    List<PricingSuggestion> findByProductIdAndStatusAndTriggerReason(
            Long productId, SuggestionStatus status, TriggerReason triggerReason);
    List<PricingSuggestion> findByProductIdAndTriggerReasonAndStatus(
            Long productId, TriggerReason triggerReason, SuggestionStatus status);
}