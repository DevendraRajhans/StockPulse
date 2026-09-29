import React, { useState } from 'react';

const SimulateSale = ({ products, onSaleSimulation }) => {
  const [selectedProductId, setSelectedProductId] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (selectedProductId && window.confirm('Confirm simulation of sale?')) {
      onSaleSimulation(selectedProductId);
      setSelectedProductId('');
    }
  };

  return (
    <div className="card">
      <h2>Simulate Sale</h2>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: '15px' }}>
          <label htmlFor="productSelect" style={{ display: 'block', marginBottom: '5px' }}>
            Select Product:
          </label>
          <select
            id="productSelect"
            value={selectedProductId}
            onChange={(e) => setSelectedProductId(e.target.value)}
            style={{ 
              width: '100%', 
              padding: '8px', 
              borderRadius: '4px', 
              border: '1px solid #ccc' 
            }}
          >
            <option value="">Choose a product</option>
            {products.map((product) => (
              <option key={product.id} value={product.id}>
                {product.sku} - {product.name}
              </option>
            ))}
          </select>
        </div>
        <button 
          type="submit" 
          className="btn btn-primary"
          disabled={!selectedProductId}
        >
          Simulate Sale
        </button>
      </form>
    </div>
  );
};

export default SimulateSale;