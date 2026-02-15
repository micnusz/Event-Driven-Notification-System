# Event-Driven Notification System

Event driven notification system created with Spring Boot, Kafka, Docker, and PostgreSQL.

![Demo](docs/demo.gif)

## 🚀 Features

- **Event-driven architecture** with Apache Kafka
- **Real-time WebSocket notifications** (broadcast + user-specific)
- **Dead Letter Queue** for failed events with retry mechanism
- **Idempotency** at API and Kafka level
- **Polymorphic event payloads** (6 event types)
- **Visual demo UI** for testing

- ## 🏗️ Architecture
````mermaid
flowchart LR

    subgraph Client
        C1[Web UI]
        C2[External API Client]
    end

    subgraph Application
        API[REST API<br/>Idempotent Endpoint]
        Producer[Kafka Producer]
        Consumer[Kafka Consumer<br/>Retry + Backoff]
        WS[WebSocket Broker]
    end

    subgraph Kafka Cluster
        T1[(Topic: events)]
        T2[(Topic: events-dlt)]
    end

    subgraph Database
        DB[(PostgreSQL)]
    end

    C1 --> API
    C2 --> API

    API -->|Check idempotency| DB
    API --> Producer
    Producer --> T1

    T1 --> Consumer

    Consumer -->|Persist event| DB
    Consumer -->|Emit notification| WS

    Consumer -->|3 failed attempts| T2

    WS --> C1

````

## 🛠️ Tech Stack

- Java 17
- Spring Boot 3.x
- Apache Kafka 3.7
- PostgreSQL 16
- WebSocket (STOMP)
- Docker Compose

## 📋 Quick Start
```bash
# 1. Start infrastructure
docker-compose up -d

# 2. Run application
mvn spring-boot:run

# 3. Open demo UI
open http://localhost:8080
```

## 🎯 Architecture
```
POST /api/events
    ↓
Kafka Producer (topic: events)
    ↓
Kafka Consumer (retry 3x)
    ↓
├─ PostgreSQL (persistence)
└─ WebSocket (real-time notifications)
```

## 📊 Event Types

- `TASK_ASSIGNED` - Task assignment
- `COMMENT_ADDED` - Comment on task
- `TASK_COMPLETED` - Task completion
- `REMINDER` - Time-based reminder
- `ALERT` - User alert
- `SYSTEM_ALERT` - System-wide broadcast

## 🧪 Testing

Send event:
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "type": "TASK_ASSIGNED",
    "recipientId": "user-123",
    "idempotencyKey": "8f4b3c2e-6d2a-4a1f-9e12-123456789hwq",
    "payload": {
      "payloadType": "TASK_ASSIGNED",
      "taskName": "Prepare quarterly report",
      "taskDescription": "Compile financial data and prepare Q1 report",
      "assignedBy": "manager-456",
      "dueDate": "2026-03-01"
    }
  }'
```

Watch it appear in real-time at http://localhost:8080!

## 🔍 Monitoring

- **Demo UI**: http://localhost:8080
- **Adminer (DB)**: http://localhost:8083
- **Kafka topics**: 
```bash
  docker exec -it edns-backend-kafka-1 /opt/kafka/bin/kafka-topics.sh \
    --bootstrap-server localhost:9092 --list
```

## 💡 Key Concepts

### Idempotency
Duplicate requests with same `idempotencyKey` return same `eventId` without creating new events.

### Dead Letter Queue
Events that fail after 3 retry attempts are moved to `events-dlt` topic for manual investigation.

### Real-time Updates
UI automatically loads historical events from DB and receives new events via WebSocket.

## 📝 License

MIT
