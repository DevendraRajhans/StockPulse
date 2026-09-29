# StockPulse

StockPulse is an AI-powered commerce recommendation engine for e-commerce merchandising. It monitors inventory and sales data, detects triggers, generates intelligent pricing and reorder recommendations using real LLMs, and requires human approval before applying changes.

## Architecture

StockPulse follows an event-driven, asynchronous architecture:

1. **Inventory/Sales Events**: Changes trigger detection
2. **Async Processing**: Events are processed asynchronously 
3. **AI Analysis**: Real LLM evaluates inventory data and market conditions
4. **Recommendations**: Pricing and reorder suggestions generated
5. **Human Approval**: All changes require explicit human approval
6. **Execution**: Approved recommendations are applied to products

## Tech Stack

### Backend
- Java 17+
- Spring Boot 3.x
- Spring Web MVC
- Spring Data JPA
- H2 In-Memory Database
- Maven

### Frontend
- React 18+
- Vite
- Plain CSS (No heavy UI libraries)

### AI/LLM
- Real LLM integration with fallback to rule-based system
- Configurable strategy switching

## How It Works

### Core Flow
```
Inventory/Order Change
        ↓
Trigger Detection
        ↓
Async Event
        ↓
Commerce Advisor (AI/Rules)
       ↙        ↘
   Pricing    Reorder
       ↘        ↙
      PENDING Suggestions
          ↓
   Human Approval (Required)
      ↙        ↘
 APPROVE      REJECT
    ↓            ↓
Apply Change  No Change
```

### Trigger Types
1. **INVENTORY_LOW**: Current stock < minimum stock level
2. **DEMAND_SPIKE**: Demand velocity > 2× category average

### AI Guardrails
- Flags price changes > 50% as risky
- Flags reorder quantities > 10,000 as risky
- Automatically falls back to rule-based system on AI failure

## Setup

### Prerequisites
- Java 17+ JDK
- Node.js 16+
- Maven 3.8+

### Backend Setup

1. Set environment variable for LLM API key:
   ```bash
   export LLM_API_KEY="your-api-key-here"
   ```

2. Navigate to backend directory:
   ```bash
   cd backend
   ```

3. Build and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. Backend will start on http://localhost:8080

### Frontend Setup

1. Navigate to frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start development server:
   ```bash
   npm run dev
   ```

4. Frontend will start on http://localhost:5173

## API Endpoints

### Products
- `GET /api/products` - Get all products

### Inventory
- `POST /api/inventory/{productId}/sale` - Simulate a sale
- `POST /api/inventory/{productId}/adjust?quantityChange=N` - Adjust stock by N units

### Advisor Strategy
- `GET /api/advisor/strategy` - Get current advisor strategy
- `PUT /api/advisor/strategy?strategy=RULE|AI` - Switch advisor strategy

### Suggestions
- `GET /api/suggestions/pending` - Get all pending suggestions
- `GET /api/suggestions/pricing/pending` - Get pending pricing suggestions
- `GET /api/suggestions/reorder/pending` - Get pending reorder suggestions
- `POST /api/suggestions/pricing/{id}/approve` - Approve pricing suggestion
- `POST /api/suggestions/pricing/{id}/reject` - Reject pricing suggestion
- `POST /api/suggestions/reorder/{id}/approve` - Approve reorder suggestion
- `POST /api/suggestions/reorder/{id}/reject` - Reject reorder suggestion

## Demo Walkthrough

1. Start both backend and frontend
2. Open dashboard at http://localhost:5173
3. View products - note Gaming Monitor (stock=3, min=5) and Wireless Mouse (demand=8.0)
4. Simulate sale for Gaming Monitor - triggers INVENTORY_LOW
5. Simulate sale for Wireless Mouse - triggers DEMAND_SPIKE
6. View pending recommendations in the dashboard
7. Review AI-generated pricing/reorder suggestions
8. Approve/reject recommendations
9. Observe product prices change only after approval

## AI Fallback

If the AI/LLM service is unavailable, times out, or returns invalid responses, StockPulse automatically falls back to rule-based recommendations:

### Rule Baseline
- **Pricing**: 
  - stock < reorder threshold → +10% price
  - demand velocity > 2× category average → +5% price
  - otherwise HOLD
- **Reorder**: quantity = (threshold × 3) - current stock, minimum 1

## Security Notes

- Never commit API keys
- LLM API key should be provided via environment variable
- CORS configured for frontend communication
