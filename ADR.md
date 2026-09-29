# Architecture Decision Records (ADRs)

## 1. Async Event Processing

**Context**: Inventory changes need to trigger AI analysis without blocking the HTTP response.

**Decision**: Use Spring's `@Async` with `ApplicationEventPublisher` for non-blocking processing.

**Tradeoffs**: 
- Pro: Fast API responses
- Pro: Scalable event processing
- Con: Increased system complexity
- Con: Potential for event loss if not properly handled

## 2. Strategy Pattern for Commerce Advisors

**Context**: Need to support both rule-based and AI-powered recommendation engines.

**Decision**: Implement strategy pattern with `CommerceAdvisor` interface and runtime switching.

**Tradeoffs**:
- Pro: Easy to swap between AI and rule-based advisors
- Pro: Clean separation of concerns
- Pro: Extensible for future advisor types
- Con: Slight overhead of polymorphism

## 3. Real LLM with Rule-Based Fallback

**Context**: AI recommendations are critical but LLMs can fail, timeout, or return invalid data.

**Decision**: Always use real LLM API with automatic fallback to rule-based system on failure.

**Tradeoffs**:
- Pro: Leverages cutting-edge AI when available
- Pro: Guaranteed recommendation generation
- Pro: Transparent fallback mechanism
- Con: Requires robust error handling
- Con: More complex validation logic

## 4. Human-in-the-Loop Approval

**Context**: AI recommendations should not automatically modify product data.

**Decision**: All recommendations are created as PENDING suggestions requiring explicit human approval.

**Tradeoffs**:
- Pro: Business control over AI decisions
- Pro: Risk mitigation for bad recommendations
- Pro: Compliance with business policies
- Con: Additional step in workflow
- Con: Manual intervention required

## 5. Duplicate Prevention and Idempotency

**Context**: Multiple events might trigger identical recommendations.

**Decision**: Check for existing PENDING suggestions before creating new ones. Validate state transitions.

**Tradeoffs**:
- Pro: Prevents duplicate work
- Pro: Ensures data consistency
- Pro: Better user experience
- Con: Additional database queries
- Con: More complex suggestion lifecycle management

## 6. Business Guardrails

**Context**: AI might generate unreasonable pricing or reorder quantities.

**Decision**: Implement lightweight validation to flag but not modify extreme recommendations.

**Tradeoffs**:
- Pro: Maintains human oversight
- Pro: Preserves AI intent
- Pro: Clear risk communication
- Con: Does not prevent risky changes
- Con: Relies on human judgment

## 7. H2 Database Choice

**Context**: Need a simple database for hackathon demonstration.

**Decision**: Use H2 in-memory database with automatic schema generation.

**Tradeoffs**:
- Pro: Zero setup
- Pro: Fast development
- Pro: Easy to reset
- Con: Data lost on restart
- Con: Not production suitable

## 8. Exclusion of Complex Infrastructure

**Context**: Limited time in hackathon environment.

**Decision**: Deliberately exclude Kafka, Redis, Docker, authentication, payments, microservices.

**Tradeoffs**:
- Pro: Rapid development
- Pro: Focus on core functionality
- Pro: Simplified deployment
- Con: Less production-ready
- Con: Limited scalability options

## 9. Separate Pricing and Reorder Suggestions

**Context**: Pricing and reorder decisions have different lifecycles and approval processes.

**Decision**: Create separate entities and repositories for pricing and reorder suggestions.

**Tradeoffs**:
- Pro: Clear separation of concerns
- Pro: Independent approval workflows
- Pro: Better data modeling
- Con: More entities and repositories
- Con: Slightly more complex querying

## 10. Environment Variable Configuration

**Context**: Need to securely handle LLM API credentials.

**Decision**: Use environment variables with Spring's property placeholder syntax.

**Tradeoffs**:
- Pro: Secure credential handling
- Pro: Environment-specific configuration
- Pro: Industry standard practice
- Con: Requires environment setup
- Con: Potential for misconfiguration