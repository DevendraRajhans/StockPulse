const API_BASE_URL = 'http://localhost:8080/api';

// Helper function for API calls
const apiCall = async (endpoint, options = {}) => {
  const url = `${API_BASE_URL}${endpoint}`;
  const config = {
    headers: {
      'Content-Type': 'application/json',
    },
    ...options,
  };

  try {
    const response = await fetch(url, config);
    
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`HTTP ${response.status}: ${errorText || response.statusText}`);
    }
    
    if (response.status === 204) {
      return null; // No content
    }
    
    return await response.json();
  } catch (error) {
    console.error(`API call failed for ${url}:`, error);
    throw error;
  }
};

// Product API
export const getProducts = () => {
  return apiCall('/products');
};

// Suggestion API
export const getPendingSuggestions = async () => {
  try {
    // Fetch pricing and reorder suggestions separately
    const [pricing, reorder] = await Promise.all([
      apiCall('/suggestions/pricing/pending'),
      apiCall('/suggestions/reorder/pending')
    ]);
    
    return { pricing, reorder };
  } catch (error) {
    console.error('Failed to fetch pending suggestions:', error);
    return { pricing: [], reorder: [] };
  }
};

// Sale Simulation
export const simulateSale = (productId) => {
  return apiCall(`/inventory/${productId}/sale`, {
    method: 'POST',
  });
};

// Pricing Suggestion Actions
export const approvePricing = (suggestionId) => {
  return apiCall(`/suggestions/pricing/${suggestionId}/approve`, {
    method: 'POST',
  });
};

export const rejectPricing = (suggestionId) => {
  return apiCall(`/suggestions/pricing/${suggestionId}/reject`, {
    method: 'POST',
  });
};

// Reorder Suggestion Actions
export const approveReorder = (suggestionId) => {
  return apiCall(`/suggestions/reorder/${suggestionId}/approve`, {
    method: 'POST',
  });
};

export const rejectReorder = (suggestionId) => {
  return apiCall(`/suggestions/reorder/${suggestionId}/reject`, {
    method: 'POST',
  });
};