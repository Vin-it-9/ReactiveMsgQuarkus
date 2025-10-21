# Real-Time Metrics Alert System with Quarkus & Kafka

A reactive microservice built with Quarkus that monitors metric streams from Apache Kafka and triggers real-time alerts via Server-Sent Events (SSE) when values exceed configured thresholds.

## 🚀 Features

- **Real-time Metric Processing**: Consumes metrics from Kafka topics
- **Dynamic Range Configuration**: Set custom thresholds per user via REST API
- **Live Alert Streaming**: WebSocket/SSE streaming of alerts to web dashboard
- **Reactive Architecture**: Built with SmallRye Reactive Messaging
- **Zero-Config Development**: Auto-generates test metrics every 2 seconds

## 🏗️ Architecture

```
MetricProducer → Kafka (metrics topic) → MetricProcessor → In-Memory Channel → WebSocket/SSE → Browser
                                              ↓
                                        RangeService (checks thresholds)
                                              ↓
                                         Alert Generation
```

## 📋 Prerequisites

- **Java 21** or higher
- **Apache Maven 3.9+**
- **Apache Kafka** (KRaft mode - no Zookeeper needed)
- **Docker** (optional - for Dev Services)

## 🛠️ Technology Stack

- **Quarkus 3.27.0** - Supersonic Subatomic Java Framework
- **Apache Kafka** - Event streaming platform
- **SmallRye Reactive Messaging** - Reactive messaging framework
- **Jackson** - JSON serialization
- **Tailwind CSS** - Frontend styling



## 📡 API Endpoints

### Set Range Configuration

**POST** `/ranges/{userId}`

```bash
curl -X POST http://localhost:8080/ranges/user1 \
  -H "Content-Type: application/json" \
  -d '{
    "metricType": "cpu",
    "minValue": 60.0,
    "maxValue": 80.0
  }'
```

### Get Range Configuration

**GET** `/ranges/{userId}`

```bash
curl http://localhost:8080/ranges/user1
```

### Get All Ranges

**GET** `/ranges`

```bash
curl http://localhost:8080/ranges
```

### Delete Range

**DELETE** `/ranges/{userId}`

```bash
curl -X DELETE http://localhost:8080/ranges/user1
```

### Stream Alerts (SSE)

**GET** `/alerts/stream`

```bash
curl -N http://localhost:8080/alerts/stream
```

## 🎯 Usage

### 1. Open the Dashboard

Navigate to `http://localhost:8080` in your browser

### 2. Configure a Range

- Enter **User ID** (e.g., `user1`)
- Select **Metric Type** (cpu, memory, disk, network)
- Set **Min/Max Values** for the acceptable range
- Click **Set Range**

### 3. Monitor Alerts

Alerts will automatically stream to the dashboard when metrics exceed configured ranges.

### Metric Types Generated

- `cpu` - CPU usage percentage (60-100%)
- `memory` - Memory usage percentage (60-100%)
- `disk` - Disk usage percentage (60-100%)
- `network` - Network usage percentage (60-100%)

## ⚙️ Configuration

### application.properties

```properties
# Kafka Connection
quarkus.kafka.devservices.enabled=false
kafka.bootstrap.servers=localhost:9092

# Kafka Consumer
mp.messaging.incoming.metrics-in.connector=smallrye-kafka
mp.messaging.incoming.metrics-in.topic=metrics
mp.messaging.incoming.metrics-in.group.id=metric-processor-group
mp.messaging.incoming.metrics-in.value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
mp.messaging.incoming.metrics-in.auto.offset.reset=earliest

# Kafka Producer
mp.messaging.outgoing.metrics-out.connector=smallrye-kafka
mp.messaging.outgoing.metrics-out.topic=metrics
mp.messaging.outgoing.metrics-out.value.serializer=org.apache.kafka.common.serialization.StringSerializer
mp.messaging.outgoing.metrics-out.merge=true

# Application
quarkus.http.port=8080
```

## 📁 Project Structure

```
reactivemsgquarkus/
├── src/main/java/
│   ├── controller/
│   │   ├── RangeResource.java          # REST API for range management
│   │   └── websocket/
│   │       └── AlertWebSocket.java     # SSE endpoint for alerts
│   ├── kafka/
│   │   └── MetricProducer.java         # Produces test metrics
│   ├── processor/
│   │   └── MetricProcessor.java        # Processes metrics & generates alerts
│   ├── model/
│   │   ├── Alert.java                  # Alert model
│   │   ├── MetricData.java             # Metric model
│   │   └── Range.java                  # Range configuration model
│   ├── service/
│   │   └── RangeService.java           # Range management service
│   └── lifecycle/
│       └── AppStartup.java             # Application startup logic
├── src/main/resources/
│   ├── application.properties          # Configuration
│   └── META-INF/resources/
│       └── index.html                  # Dashboard UI
└── pom.xml
```

## 🧪 Testing

### Test Kafka Consumer Manually

```bash
cd C:\kafka\bin\windows
kafka-console-consumer.bat --topic metrics --bootstrap-server localhost:9092 --from-beginning
```

### Test Kafka Producer Manually

```bash
kafka-console-producer.bat --topic metrics --bootstrap-server localhost:9092
```

Then type:
```json
{"id":"test-1","metricType":"cpu","value":85.0,"unit":"%"}
```

## 🐛 Troubleshooting

### Kafka Not Running

Verify Kafka is running:
```bash
kafka-broker-api-versions.bat --bootstrap-server localhost:9092
```

### Consumer Not Reading Messages

Reset consumer group offset:
```bash
kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group metric-processor-group --reset-offsets --to-earliest --topic metrics --execute
```

### Check Topics

List all topics:
```bash
kafka-topics.bat --bootstrap-server localhost:9092 --list
```

Describe topic:
```bash
kafka-topics.bat --bootstrap-server localhost:9092 --describe --topic metrics
```

## 📊 Metrics Generated

The application automatically generates random metrics every 2 seconds:

```json
{
  "id": "metric-1",
  "metricType": "cpu",
  "value": 84.5,
  "unit": "%",
  "timestamp": "2025-10-21T12:30:45Z"
}
```
