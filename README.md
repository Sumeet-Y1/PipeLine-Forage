# 🚀 PipelineForge

> Enterprise-grade Async Task Processing Platform with Full DevOps Automation

PipelineForge is a cloud-native microservices platform for distributed task processing, demonstrating production-ready DevOps practices. The system handles asynchronous job execution with real-time status tracking, automated scaling, and comprehensive observability.

## 🏗️ Architecture
```
┌─────────────┐      ┌──────────────┐      ┌─────────────┐
│   Client    │─────▶│     ALB      │─────▶│ API Service │
└─────────────┘      └──────────────┘      └──────┬──────┘
                                                   │
                                                   ▼
                                            ┌─────────────┐
                                            │   AWS SQS   │
                                            └──────┬──────┘
                                                   │
                     ┌─────────────────────────────┼─────────────────┐
                     ▼                             ▼                 ▼
              ┌─────────────┐            ┌─────────────┐    ┌─────────────┐
              │Worker Service│            │  RDS MySQL  │    │   AWS S3    │
              └─────────────┘            └─────────────┘    └─────────────┘
                     │
                     ▼
              ┌─────────────┐
              │ CloudWatch  │
              └─────────────┘
```

## ✨ Features

- **Microservices Architecture**: Decoupled API and Worker services
- **Async Processing**: SQS-based message queue for reliable task handling
- **Infrastructure as Code**: Complete AWS infrastructure managed via Terraform
- **CI/CD Automation**: GitHub Actions pipeline with automated testing and deployment
- **Containerization**: Docker-based deployments on AWS ECS Fargate
- **Auto-scaling**: ECS services scale based on CPU and memory metrics
- **Observability**: CloudWatch dashboards, custom metrics, and alerting
- **Security**: AWS Secrets Manager, IAM roles, VPC isolation

## 🛠️ Tech Stack

**Backend**
- Java 17
- Spring Boot 3.x
- Spring Cloud AWS (SQS)
- Spring Data JPA
- MySQL

**Infrastructure & DevOps**
- Docker & Docker Compose
- Terraform
- GitHub Actions
- AWS ECS Fargate
- AWS ECR
- AWS Application Load Balancer
- Amazon SQS
- Amazon RDS (MySQL)
- Amazon S3

**Monitoring**
- AWS CloudWatch Logs
- CloudWatch Metrics & Dashboards
- CloudWatch Alarms

## 📋 Prerequisites

- AWS Account with CLI configured
- Terraform >= 1.0
- Docker & Docker Compose
- Java 17+
- Maven

## 🚀 Quick Start

### Local Development

1. **Clone the repository**
```bash
git clone https://github.com/YOUR_USERNAME/PipelineForge.git
cd PipelineForge
```

2. **Start services locally**
```bash
docker-compose up -d
```

3. **Test the API**
```bash
# Submit a task
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"type": "DATA_PROCESSING", "payload": "sample data"}'

# Check task status
curl http://localhost:8080/tasks/{task-id}
```

### AWS Deployment

1. **Configure AWS credentials**
```bash
aws configure
```

2. **Initialize Terraform**
```bash
cd terraform
terraform init
```

3. **Deploy infrastructure**
```bash
terraform plan
terraform apply
```

4. **Build and push Docker images**
```bash
# Images are automatically built and deployed via GitHub Actions
# Or manually:
./scripts/deploy.sh
```

## 📊 Monitoring

Access CloudWatch dashboards for:
- Service health metrics (CPU, Memory, Request Count)
- Task processing metrics
- Error rates and logs
- Custom business metrics

## 🔒 Security

- Secrets managed via AWS Secrets Manager
- IAM roles for service authentication
- VPC with public/private subnet isolation
- Security groups restricting network access
- No hardcoded credentials

## 📈 CI/CD Pipeline

GitHub Actions workflow automatically:
1. Runs unit and integration tests
2. Builds Docker images
3. Pushes to Amazon ECR
4. Updates ECS services
5. Sends deployment notifications

## 🧹 Cleanup

To destroy all AWS resources:
```bash
cd terraform
terraform destroy
```

## 📝 API Documentation

### Submit Task
```http
POST /tasks
Content-Type: application/json

{
  "type": "DATA_PROCESSING",
  "payload": "your data here"
}
```

### Get Task Status
```http
GET /tasks/{taskId}
```

### List All Tasks
```http
GET /tasks
```

## 🎯 Project Goals

This project demonstrates:
- Production-ready microservices architecture
- Complete infrastructure automation
- Modern DevOps practices
- Cloud-native development
- Scalable and resilient system design

## 📄 License

MIT License

## 👤 Author

Your Name
- GitHub: [@Sumeet-Y1](https://github.com/Sumeet-Y1)
- LinkedIn: [Sumeet Y](https://www.linkedin.com/in/sumeet-backenddev/)

---

⭐ Star this repo if you find it helpful!

