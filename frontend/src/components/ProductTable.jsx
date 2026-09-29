import React from 'react';

const ProductTable = ({ products, onSaleSimulation }) => {
  const handleSaleClick = (productId) => {
    if (window.confirm('Simulate a sale for this product?')) {
      onSaleSimulation(productId);
    }
  };

  const getStockStatus = (quantity, minStockLevel) => {
    if (quantity < minStockLevel) {
      return <span className="badge badge-danger">LOW STOCK</span>;
    } else if (quantity <= minStockLevel * 1.5) {
      return <span className="badge badge-warning">WARNING</span>;
    }
    return <span className="badge badge-success">OK</span>;
  };

  return (
    <div className="card">
      <h2>Product Inventory</h2>
      <table className="table">
        <thead>
          <tr>
            <th>SKU</th>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Min Stock</th>
            <th>Demand Velocity</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <tr key={product.id}>
              <td>{product.sku}</td>
              <td>{product.name}</td>
              <td>{product.category}</td>
              <td>${product.price.toFixed(2)}</td>
              <td>{product.quantity}</td>
              <td>{product.minStockLevel}</td>
              <td>{product.demandVelocity?.toFixed(2) || '0.00'}</td>
              <td>{getStockStatus(product.quantity, product.minStockLevel)}</td>
              <td>
                <button 
                  className="btn btn-primary"
                  onClick={() => handleSaleClick(product.id)}
                >
                  Simulate Sale
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ProductTable;