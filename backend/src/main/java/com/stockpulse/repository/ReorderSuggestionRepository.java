package com.stockpulse.repository;

import com.stockpulse.entity.ReorderSuggestion;
import com.stockpulse.entity.enums.SuggestionStatus;
import com.stockpulse.entity.enums.TriggerReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReorderSuggestionRepository extends JpaRepository<ReorderSuggestion, Long> {
    List<ReorderSuggestion> findByProductIdAndStatusOrderByCreatedAtDesc(Long productId, SuggestionStatus status);
    List<ReorderSuggestion> findByStatusOrderByCreatedAtDesc(SuggestionStatus status);
    List<ReorderSuggestion> findByProductIdAndStatusAndTriggerReason(
            Long productId, SuggestionStatus status, TriggerReason triggerReason);
    List<ReorderSuggestion> findByProductIdAndTriggerReasonAndStatus(
            Long productId, TriggerReason triggerReason, SuggestionStatus status);
}