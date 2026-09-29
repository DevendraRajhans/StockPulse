import React, { useState, useEffect } from 'react';
import ProductTable from './components/ProductTable';
import RecommendationPanel from './components/RecommendationPanel';
import SimulateSale from './components/SimulateSale';
import Header from './components/Header';
import { getProducts, getPendingSuggestions, simulateSale } from './services/api';

function App() {
  const [products, setProducts] = useState([]);
  const [suggestions, setSuggestions] = useState({ pricing: [], reorder: [] });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [productsData, suggestionsData] = await Promise.all([
        getProducts(),
        getPendingSuggestions()
      ]);
      setProducts(productsData);
      setSuggestions(suggestionsData);
    } catch (err) {
      setError('Failed to load data: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleSaleSimulation = async (productId) => {
    try {
      await simulateSale(productId);
      await fetchData(); // Refresh data after simulation
    } catch (err) {
      setError('Failed to simulate sale: ' + err.message);
    }
  };

  const handleSuggestionAction = async () => {
    // Refresh suggestions after approval/rejection
    await fetchData();
  };

  useEffect(() => {
    fetchData();
  }, []);

  if (loading && products.length === 0) {
    return <div className="container">Loading...</div>;
  }

  return (
    <div className="container">
      <Header />
      
      {error && (
        <div className="card" style={{ backgroundColor: '#f8d7da', borderColor: '#f5c6cb', color: '#721c24' }}>
          Error: {error}
        </div>
      )}
      
      <SimulateSale 
        products={products} 
        onSaleSimulation={handleSaleSimulation}
      />
      
      <ProductTable 
        products={products} 
        onSaleSimulation={handleSaleSimulation}
      />
      
      <RecommendationPanel 
        suggestions={suggestions} 
        onAction={handleSuggestionAction}
      />
    </div>
  );
}

export default App;