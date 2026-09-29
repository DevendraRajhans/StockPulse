INSERT INTO products (name, sku, price, cost, quantity, min_stock_level, demand_velocity, category, last_updated) VALUES
('Premium Headphones', 'HP-001', 199.99, 120.00, 25, 10, 2.5, 'Electronics', NOW()),
('Wireless Mouse', 'WM-002', 29.99, 15.00, 50, 20, 8.0, 'Electronics', NOW()),
('Mechanical Keyboard', 'KB-003', 89.99, 50.00, 15, 5, 1.2, 'Electronics', NOW()),
('Gaming Monitor', 'GM-004', 299.99, 200.00, 3, 5, 0.8, 'Electronics', NOW()),
('USB-C Hub', 'UH-005', 39.99, 20.00, 30, 15, 3.0, 'Electronics', NOW());

INSERT INTO inventory_snapshots (product_id, quantity, event_type, timestamp) VALUES
(1, 25, 'STOCK_INITIALIZED', NOW()),
(2, 50, 'STOCK_INITIALIZED', NOW()),
(3, 15, 'STOCK_INITIALIZED', NOW()),
(4, 3, 'STOCK_INITIALIZED', NOW()),
(5, 30, 'STOCK_INITIALIZED', NOW());