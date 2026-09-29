import React from 'react';
import { approvePricing, rejectPricing, approveReorder, rejectReorder } from '../services/api';

const RecommendationPanel = ({ suggestions, onAction }) => {
  const handleApprovePricing = async (id) => {
    try {
      await approvePricing(id);
      onAction();
    } catch (err) {
      alert('Failed to approve pricing suggestion: ' + err.message);
    }
  };

  const handleRejectPricing = async (id) => {
    try {
      await rejectPricing(id);
      onAction();
    } catch (err) {
      alert('Failed to reject pricing suggestion: ' + err.message);
    }
  };

  const handleApproveReorder = async (id) => {
    try {
      await approveReorder(id);
      onAction();
    } catch (err) {
      alert('Failed to approve reorder suggestion: ' + err.message);
    }
  };

  const handleRejectReorder = async (id) => {
    try {
      await rejectReorder(id);
      onAction();
    } catch (err) {
      alert('Failed to reject reorder suggestion: ' + err.message);
    }
  };

  const renderPricingSuggestion = (suggestion) => (
    <div key={suggestion.id} className="card" style={{ marginBottom: '15px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h3>Pricing Recommendation</h3>
          <p><strong>Type:</strong> {suggestion.triggerReason}</p>
          <p><strong>Current Price:</strong> ${suggestion.currentPrice?.toFixed(2)}</p>
          <p><strong>Recommended Price:</strong> ${suggestion.recommendedPrice?.toFixed(2)}</p>
          <p><strong>Direction:</strong> {suggestion.direction}</p>
          <p><strong>Confidence:</strong> {(suggestion.confidence * 100).toFixed(1)}%</p>
          <p><strong>Reasoning:</strong> {suggestion.reasoning}</p>
          {suggestion.hasRiskFlag && (
            <p className="risk-flag">⚠️ Risk Alert: Unusual price change detected</p>
          )}
        </div>
        <div>
          <button 
            className="btn btn-success"
            onClick={() => handleApprovePricing(suggestion.id)}
            style={{ marginRight: '10px' }}
          >
            Approve
          </button>
          <button 
            className="btn btn-danger"
            onClick={() => handleRejectPricing(suggestion.id)}
          >
            Reject
          </button>
        </div>
      </div>
    </div>
  );

  const renderReorderSuggestion = (suggestion) => (
    <div key={suggestion.id} className="card" style={{ marginBottom: '15px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h3>Reorder Recommendation</h3>
          <p><strong>Type:</strong> {suggestion.triggerReason}</p>
          <p><strong>Current Stock:</strong> {suggestion.currentStock}</p>
          <p><strong>Recommended Quantity:</strong> {suggestion.recommendedQuantity}</p>
          <p><strong>Confidence:</strong> {(suggestion.confidence * 100).toFixed(1)}%</p>
          <p><strong>Reasoning:</strong> {suggestion.reasoning}</p>
          {suggestion.hasRiskFlag && (
            <p className="risk-flag">⚠️ Risk Alert: Unusual reorder quantity detected</p>
          )}
        </div>
        <div>
          <button 
            className="btn btn-success"
            onClick={() => handleApproveReorder(suggestion.id)}
            style={{ marginRight: '10px' }}
          >
            Approve
          </button>
          <button 
            className="btn btn-danger"
            onClick={() => handleRejectReorder(suggestion.id)}
          >
            Reject
          </button>
        </div>
      </div>
    </div>
  );

  const hasPendingSuggestions = suggestions.pricing?.length > 0 || suggestions.reorder?.length > 0;

  return (
    <div className="card">
      <h2>Pending Recommendations</h2>
      
      {!hasPendingSuggestions && (
        <p>No pending recommendations. Simulate sales to generate AI-powered suggestions.</p>
      )}
      
      {suggestions.pricing?.map(renderPricingSuggestion)}
      {suggestions.reorder?.map(renderReorderSuggestion)}
    </div>
  );
};

export default RecommendationPanel;