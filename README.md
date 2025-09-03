# echo_at_time

A Spring Boot application that accepts messages with time delays and echoes them back after the specified time using Redis for persistence.

## Features

- TCP server accepting connections on port 10101
- Message scheduling with Redis persistence
- Input validation and error handling
- Configurable delay periods (up to 24 hours)

## Prerequisites

- Java 8 or higher
- Redis server
- Maven 3.x

## Redis Setup

### Option 1: Local Installation
Install Redis locally and run with default configuration.

### Option 2: Docker (Recommended)
Start Redis using the provided script:
```bash
./redis.sh start
```

To stop and clean up:
```bash
./redis.sh finish
```

## Building and Running

### Option 1: IDE
Run `EchoAtTimeApplication.java` from your IDE.

### Option 2: Maven
```bash
mvn clean package
java -jar target/echo_at_time-0.0.5-SNAPSHOT.jar
```

## Usage

### Connect to Server
Use netcat to connect to the server:
```bash
nc 127.0.0.1 10101
```

### Send Messages
The server accepts messages in the following format:
```
message:your text here;time:delay_in_milliseconds
```

**Examples:**
```
message:Hello World;time:5000
message:Reminder to check email;time:60000
```

### Message Format

| Parameter | Description | Limitations |
|-----------|-------------|-------------|
| message | Text content to echo back | 1-1000 characters |
| time | Delay in milliseconds before echoing | 0-86400000 (24 hours) |

## Output

The server will:
1. Acknowledge receipt of valid messages
2. Store them in Redis with the calculated echo time
3. Display messages back to the client when their time arrives

## Architecture

- **Spring Boot 2.3.0** - Application framework
- **Redis** - Message persistence and scheduling
- **TCP Sockets** - Client communication
- **Scheduled polling** - Message delivery (100ms intervals)

## Testing

Run the test suite:
```bash
mvn test
```

## Known Limitations

- Single client connection at a time
- No authentication or authorization
- No SSL/TLS encryption
- Limited error handling for network issues