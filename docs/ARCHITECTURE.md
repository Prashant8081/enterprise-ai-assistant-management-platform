# Architecture

## Product Idea

Enterprise AI Assistant Management Platform lets an organization create multiple secure AI assistants for different departments. Each assistant has its own instructions, documents, retrieval context, audit trail, and tenant boundary.

## Main Request Flow

1. User logs in through `/api/auth/login`.
2. The platform issues a signed JWT containing tenant and role claims.
3. API Gateway validates the token and routes requests to the platform service.
4. The platform service enforces role-based access using Spring Security method rules.
5. Documents are chunked, embedded, and stored in PostgreSQL with pgvector.
6. Chat requests retrieve the most similar chunks for the assistant and tenant.
7. The answer is generated from grounded context.
8. Audit data is stored in PostgreSQL and published to Kafka.

## Why This Looks Professional

- Tenant isolation is part of every important query.
- JWT security is stateless and gateway-aware.
- Flyway owns schema changes.
- PostgreSQL stores both business data and vector embeddings.
- Kafka receives audit events for downstream analytics.
- Open Session in View is disabled.
- Controllers use DTOs instead of exposing entities.
- Local RAG mode makes the project runnable without external keys.
