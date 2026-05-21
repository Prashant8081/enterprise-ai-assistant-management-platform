# Deployment Guide

## Best Strategy for Recruiters

For recruiters, the best result is a public URL plus a clean GitHub repository. Kubernetes is impressive, but many free Kubernetes options are harder to keep stable than simple app hosting.

Recommended path:

1. Use Docker Compose locally for development.
2. Add Kubernetes manifests to show cloud-native knowledge.
3. Deploy a public demo on a simple free container platform.
4. Mention Kubernetes support in the README and interview.

## Recommended Free Options

### Best public demo: Koyeb or Render

Use this when you want recruiters to click a URL and see the API is alive.

Pros:
- Docker support
- GitHub deployment
- easier than managing a Kubernetes cluster
- good enough for recruiter demos

Tradeoff:
- free instances may sleep or have resource limits
- running PostgreSQL, Kafka, and Redis together may exceed free limits

Best recruiter setup:
- deploy only `platform-service` and `api-gateway`
- use managed/free PostgreSQL if available
- keep Kafka optional or disabled for the demo
- explain full Kafka/Kubernetes setup in GitHub docs

### Best Kubernetes learning: Oracle Cloud Always Free

Use this when you want real Kubernetes experience.

Pros:
- generous Always Free compute allocation in many regions
- OKE Basic Cluster is listed as free
- good interview story for Kubernetes, deployments, services, probes, HPA, and persistent volumes

Tradeoff:
- account creation and free capacity can be inconsistent
- you must be careful to stay inside Always Free limits
- Kubernetes has more operational work than simple app hosting

### Best local Kubernetes demo: Docker Desktop Kubernetes, Minikube, or Kind

Use this when you want to learn Kubernetes safely without cloud billing risk.

Pros:
- no public cloud cost
- fast for learning
- good for screenshots and interview walkthroughs

Tradeoff:
- recruiters cannot access it publicly unless you record a demo or expose it through a tunnel

## Kubernetes Deploy Flow

1. Build Docker images.
2. Push images to a registry such as GitHub Container Registry.
3. Replace image names in `k8s/base/*.yaml`.
4. Apply manifests:

```powershell
kubectl apply -k k8s/base
```

5. Check pods:

```powershell
kubectl get pods -n enterprise-ai
```

6. Check service:

```powershell
kubectl get svc -n enterprise-ai
```

## What to Say in Interview

Use this explanation:

```text
I containerized both Spring Boot services and added Kubernetes manifests for gateway, platform service, PostgreSQL with pgvector, Kafka, and Redis. I used ConfigMaps for non-sensitive configuration, Secrets for credentials, readiness/liveness probes for health checks, Services for internal networking, a LoadBalancer/Ingress for external access, and HPA for autoscaling examples. For a real production deployment, I would move PostgreSQL and Kafka to managed cloud services, keep only stateless Spring Boot services in Kubernetes, and use CI/CD to publish images to a registry.
```

## Honest Production Note

The included PostgreSQL and Kafka Kubernetes manifests are useful for learning and demos. In production, do not run database and Kafka this casually unless you also add backups, monitoring, storage classes, anti-affinity, security policies, and disaster recovery.
