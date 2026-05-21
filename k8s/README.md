# Kubernetes Deployment

This folder contains a recruiter-friendly Kubernetes deployment for the Enterprise AI Assistant Management Platform.

It includes:

- `api-gateway` Deployment and LoadBalancer Service
- `platform-service` Deployment and ClusterIP Service
- PostgreSQL with pgvector as a StatefulSet
- Kafka single-broker development Deployment
- Redis Deployment
- ConfigMap and Secret
- Ingress example
- HPA examples

## Important Notes

The included PostgreSQL and Kafka manifests are suitable for learning, demos, and small proof-of-concept deployments. For production, use managed PostgreSQL and managed Kafka where possible.

Before deploying, replace these image placeholders:

```text
ghcr.io/YOUR_GITHUB_USERNAME/eaamp-api-gateway:latest
ghcr.io/YOUR_GITHUB_USERNAME/eaamp-platform-service:latest
```

## Build Images Locally

```powershell
docker build -f api-gateway/Dockerfile -t eaamp-api-gateway:local .
docker build -f platform-service/Dockerfile -t eaamp-platform-service:local .
```

If you use local images with Docker Desktop Kubernetes, change the image names in:

```text
k8s/base/api-gateway.yaml
k8s/base/platform-service.yaml
```

## Deploy

```powershell
kubectl apply -k k8s/base
```

Check status:

```powershell
kubectl get pods -n enterprise-ai
kubectl get svc -n enterprise-ai
```

Local access without a cloud load balancer:

```powershell
kubectl port-forward -n enterprise-ai service/api-gateway 8080:80
```

Then call:

```text
http://localhost:8080/api/auth/login
```

## Default Login

```text
email: admin@enterprise.local
password: Admin@12345
```
