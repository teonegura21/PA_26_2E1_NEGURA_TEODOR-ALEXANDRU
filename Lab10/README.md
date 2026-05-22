# Lab 10: Java Networking Multiplayer Quiz Game

## Project Overview

This project implements a multiplayer quiz game with a server-client architecture using Java sockets, ThreadPoolExecutor, and multi-threading. It includes bot players, time-controlled questions, and a virtual thread demonstration.

## Project Structure

```
Lab10/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── lab10/
        │       ├── common/
        │       │   ├── Question.java
        │       │   ├── Protocol.java
        │       │   ├── PlayerResult.java
        │       │   └── GameConfig.java
        │       ├── server/
        │       │   ├── GameServer.java
        │       │   ├── ClientThread.java
        │       │   ├── QuestionLoader.java
        │       │   ├── BotPlayer.java
        │       │   └── VirtualThreadServer.java
        │       └── client/
        │           ├── GameClient.java
        │           └── ServerListener.java
        └── resources/
            └── questions.json
```

## Requirements

- Java 21 or later
- Maven 3.6+

## Compilation

```bash
cd /home/teodor/Documents/PA_26_2E1_NEGURA_TEODOR-ALEXANDRU/Lab10
mvn clean compile
```

## Running the Server

### Standard ThreadPool Server
```bash
mvn compile exec:java -Dexec.mainClass="lab10.server.GameServer"
```
Or with custom port:
```bash
mvn compile exec:java -Dexec.mainClass="lab10.server.GameServer" -Dexec.args="12345"
```

### Virtual Thread Server (Advanced)
```bash
mvn compile exec:java -Dexec.mainClass="lab10.server.VirtualThreadServer"
```

## Running the Client

```bash
mvn compile exec:java -Dexec.mainClass="lab10.client.GameClient"
```
Or with custom host/port:
```bash
mvn compile exec:java -Dexec.mainClass="lab10.client.GameClient" -Dexec.args="localhost 12345"
```

## How to Play

1. Start the server
2. Start one or more clients
3. Each client enters their name when prompted
4. In the server console, type:
   - `addbot` to add a bot player
   - `start` to begin the game
   - `status` to see current players
   - `stop` to shut down the server
5. During the game, clients answer questions by typing the option number (0-3)
6. After all questions, the scoreboard is displayed

## Features

### Compulsory
- GameServer with ServerSocket and command processing
- ClientThread for each client connection
- GameClient that reads from keyboard and sends to server
- "stop" command shuts down server gracefully

### Homework
- JSON file for questions and answers
- OOP model (Question, PlayerResult, Protocol, GameConfig)
- ThreadPoolExecutor for client handling
- Real-time communication with two client threads (input + listener)
- Time-controlled questions (15 seconds per question)
- Winner determined by correctness, ties broken by response time

### Advanced
- Bot players with configurable correctness probability
- Virtual thread server for high concurrency demonstration
- Graceful shutdown with shutdown hooks

## Protocol

Messages are formatted as: `COMMAND|payload`

| Command | Description |
|---------|-------------|
| JOIN | Player joins with name |
| START | Game starts |
| QUESTION | New question sent |
| ANSWER | Player answer (number) |
| RESULT | Correct/Wrong result |
| SCORE | Scoreboard update |
| END | Game ended |
| STOP | Server stopped |
| PING | Keep-alive / test |
| WAIT | Waiting state |
| ERROR | Error message |

## License

Academic project for Lab 10.
