# Enterprise AI Assistant Management Platform

A secure, multi-tenant Spring Boot platform for creating and managing enterprise AI assistants over internal knowledge.

This starter is intentionally runnable without paid LLM keys. It uses PostgreSQL with `pgvector`, JWT security, Kafka audit events, Spring Cloud Gateway, and a local deterministic RAG response generator. A Spring AI response generator is included as an extension point for plugging in a real model provider later.

## Tech Stack

- Java 21
- Spring Boot 3.5.x
- Spring Security with stateless JWT
- Spring Cloud Gateway
- Spring Data JPA and Hibernate
- PostgreSQL with pgvector
- Flyway migrations
- Kafka audit publishing
- Spring AI extension point
- Docker Compose
- Maven multi-module project

## Modules

- `api-gateway`: gateway running on port `8080`, validates JWT and routes `/api/**`.
- `platform-service`: core APIs running on port `8081`, owns auth, tenants, assistants, documents, RAG retrieval, chat, and audit.

## Run Locally

Start infrastructure:

```powershell
docker compose up -d
```

Start the platform service:

```powershell
mvn -pl platform-service spring-boot:run
```

Start the API gateway in another terminal:

```powershell
mvn -pl api-gateway spring-boot:run
```

Default login:

```text
email: admin@enterprise.local
password: Admin@12345
```

## IntelliJ

1. Open the folder `enterprise-ai-assistant-management-platform`.
2. Let IntelliJ import it as a Maven project.
3. Run `docker compose up -d` from the terminal.
4. Run `PlatformServiceApplication`.
5. Run `ApiGatewayApplication`.
6. Call APIs through `http://localhost:8080`.

## Example API Flow

Login:

```powershell
$login = Invoke-RestMethod -Method Post http://localhost:8080/api/auth/login `
  -ContentType "application/json" `
  -Body '{"email":"admin@enterprise.local","password":"Admin@12345"}'
$token = $login.token
```

Create an assistant:

```powershell
$assistant = Invoke-RestMethod -Method Post http://localhost:8080/api/assistants `
  -Headers @{ Authorization = "Bearer $token" } `
  -ContentType "application/json" `
  -Body '{"name":"HR Policy Assistant","systemPrompt":"You are a careful HR policy assistant. Always answer with grounded company-policy context.","modelName":"local-rag-v1"}'
```

Add knowledge:

```powershell
Invoke-RestMethod -Method Post "http://localhost:8080/api/assistants/$($assistant.id)/documents" `
  -Headers @{ Authorization = "Bearer $token" } `
  -ContentType "application/json" `
  -Body '{"title":"Leave Policy","sourceType":"TEXT","content":"Employees can take 18 paid leaves per year. Sick leave requires manager notification. Carry forward is limited to 5 days."}'
```

Ask a question:

```powershell
Invoke-RestMethod -Method Post "http://localhost:8080/api/assistants/$($assistant.id)/chat" `
  -Headers @{ Authorization = "Bearer $token" } `
  -ContentType "application/json" `
  -Body '{"message":"How many paid leaves can employees take?"}'
```

## Production Upgrade Path

- Replace the local hash embedding service with a real Spring AI embedding model.
- Add `spring-ai-starter-model-openai`, Azure OpenAI, Ollama, or another Spring AI provider.
- Add object storage for original PDF/DOCX files.
- Add Apache Tika for rich document extraction.
- Move secrets to a vault or deployment secret manager.
- Add Testcontainers integration tests.
- Add Grafana dashboards over Actuator/Micrometer metrics.

## Deployment

This repository includes Dockerfiles, Kubernetes manifests, and GitHub Actions workflows.

- Kubernetes manifests: `k8s/base`
- Kubernetes guide: `k8s/README.md`
- Deployment strategy guide: `docs/DEPLOYMENT.md`
- CI workflow: `.github/workflows/ci.yml`
- Docker image publishing workflow: `.github/workflows/docker-publish.yml`
