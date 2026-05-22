# Lab 11 - Java Persistence API (JPA)

## Overview

This project continues the **multiplayer quiz game** from **Lab 10** by adding a **persistent database layer** using **JPA (Java Persistence API)** with **Spring Data JPA** and **Hibernate** as the JPA provider. All quiz-related data (players, questions, games, results) is now stored in an **H2 relational database** instead of being kept in memory.

The project is built with **Spring Boot 3.2.5** and uses **Java 17**.

---

## How Lab 11 Complements Lab 10

| Lab 10 (Networking) | Lab 11 (JPA Persistence) |
|---|---|
| Players existed only in memory during server runtime | Players are now **persisted** to the database |
| Questions were loaded from a JSON file at startup | Questions are stored in the **DB** with relationships |
| Game results were lost when server stopped | Game results are **permanently saved** and queryable |
| No history or statistics | Full **audit trail** and **searchable results** |
| No relationships between data | Proper **one-to-many** and **many-to-many** JPA relationships |
 relational.

**In short**: Lab 10 was the "live game server". Lab 11 adds the "database backend" that makes data durable, queryable, and
---

## Project Structure

```
Lab11/
|-- pom.xml                                    # Maven config with Spring Boot, JPA, H2, JMH
|-- src/
|   |-- main/
|   |   |-- java/lab11/
|   |   |   |-- QuizJpaApplication.java        # Spring Boot main class + demo runner
|   |   |   |-- entity/                        # JPA Entity classes (Compulsory + Homework)
|   |   |   |   |-- Player.java
|   |   |   |   |-- Question.java
|   |   |   |   |-- Game.java
|   |   |   |   |-- GameResult.java
|   |   |   |   |-- AuditLog.java
|   |   |   |-- repository/                    # Spring Data JPA Repositories (Compulsory + Homework)
|   |   |   |   |-- PlayerRepository.java
|   |   |   |   |-- QuestionRepository.java
|   |   |   |   |-- GameRepository.java
|   |   |   |   |-- GameResultRepository.java
|   |   |   |   |-- AuditLogRepository.java
|   |   |   |-- service/                       # Business logic + Criteria API (Homework + Advanced)
|   |   |   |   |-- QuizService.java
|   |   |   |   |-- GameResultSpecification.java
|   |   |   |-- audit/                         # Auditing mechanism (Homework)
|   |   |   |   |-- AuditService.java
|   |   |   |-- controller/                    # REST API (Advanced - exposes all features)
|   |   |   |   |-- QuizController.java
|   |   |-- resources/
|   |   |   |-- application.properties         # DB config, JPA settings, logging config
|   |-- test/java/lab11/
|   |   |-- QuizServiceTest.java               # Integration tests (Compulsory)
|   |   |-- AppTest.java                       # Basic sanity test
```

---

## Requirement Mapping

Every item from the lab sheet is mapped to the exact file and line where it is implemented.

---

### COMPULSORY (1 point)

#### 1. Use a JPA implementation (Hibernate) with Spring Data JPA
**Where**: `pom.xml` lines 25-28
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
Spring Boot auto-configures **Hibernate** as the JPA provider. No manual `EntityManagerFactory` setup is needed -- Spring Boot handles it.

**Also in**: `application.properties` lines 8-11
```properties
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

#### 2. Define entity classes for your model (at least one) in a dedicated package
**Where**: `src/main/java/lab11/entity/` -- 5 entity classes

| Entity | File | Description |
|---|---|---|
| Player | `Player.java` | Quiz players with username/password |
| Question | `Question.java` | Quiz questions with options |
| Game | `Game.java` | A quiz game session |
| GameResult | `GameResult.java` | Score/results of a player in a game |
| AuditLog | `AuditLog.java` | Audit trail for DB changes |

Each entity uses JPA annotations: `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`, etc.

**Example** (`Player.java`):
```java
@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    // ...
}
```

---

#### 3. Define repository classes for your entities (at least one)
**Where**: `src/main/java/lab11/repository/` -- 5 repositories

All repositories extend `JpaRepository<Entity, Long>` which provides CRUD operations out of the box.

**Example** (`PlayerRepository.java`):
```java
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUsername(String username);
}
```

| Repository | Extends | Custom Methods |
|---|---|---|
| PlayerRepository | JpaRepository | `findByUsername(String)` |
| QuestionRepository | JpaRepository | `findByTextContaining(String)` -- **JPQL query** |
| GameRepository | JpaRepository | (default CRUD only) |
| GameResultRepository | JpaRepository + JpaSpecificationExecutor | `findByPlayerId`, `findByGameId`, `updateScoreById` |
| AuditLogRepository | JpaRepository | (default CRUD only) |

---

#### 4. Test your application
**Where**: `src/test/java/lab11/QuizServiceTest.java`

Contains **7 integration tests** using `@SpringBootTest`:
- `testCreatePlayer()` -- creates a player, verifies ID is generated
- `testCreateQuestion()` -- creates a question, verifies text
- `testCreateGameAndResults()` -- creates game with questions and a result
- `testFindPlayerById()` -- tests JPQL read query
- `testFindQuestionsByKeyword()` -- tests JPQL search
- `testUpdateGameResultScore()` -- tests transactional modifying query
- `testSearchResultsWithCriteria()` -- tests Criteria API dynamic search
- `testAuditLogCreated()` -- verifies auditing works

Run tests with:
```bash
mvn test
```

Also, `QuizJpaApplication.java` contains a `CommandLineRunner` demo that runs automatically when the app starts, creating sample data and exercising all major features.

---

### HOMEWORK (2 points)

#### 1. Create ALL entity classes and repositories
**Status**: COMPLETE -- all 5 entities and 5 repositories are implemented.

See Compulsory section above for the full list.

---

#### 2. Implement properly the one-to-many and many-to-many relationships

**One-to-Many relationships**:

| Relationship | Owner Side | Mapped By | File |
|---|---|---|---|
| Player -> GameResult | GameResult | `player` | `Player.java` line 21, `GameResult.java` line 14 |
| Game -> GameResult | GameResult | `game` | `Game.java` line 26, `GameResult.java` line 18 |
| Game -> GameResult (cascade) | Game | `results` | `Game.java` line 26 |

`Player.java`:
```java
@OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private List<GameResult> results = new ArrayList<>();
```

`GameResult.java`:
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "player_id", nullable = false)
private Player player;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "game_id", nullable = false)
private Game game;
```

**Many-to-Many relationship**:

| Relationship | Join Table | File |
|---|---|---|
| Game <-> Question | `game_questions` | `Game.java` lines 18-24, `Question.java` line 26 |

`Game.java`:
```java
@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
@JoinTable(
    name = "game_questions",
    joinColumns = @JoinColumn(name = "game_id"),
    inverseJoinColumns = @JoinColumn(name = "question_id")
)
private List<Question> questions = new ArrayList<>();
```

`Question.java`:
```java
@ManyToMany(mappedBy = "questions", fetch = FetchType.LAZY)
private List<Game> games = new ArrayList<>();
```

Helper methods for bidirectional sync:
- `Game.addQuestion(Question)` / `Game.removeQuestion(Question)`
- `Player.addResult(GameResult)` / `Player.removeResult(GameResult)`

---

#### 3. Use at least one read query based on a specific JPQL string and one transactional, modifying query

**JPQL Read Query**:
**Where**: `QuestionRepository.java` line 14
```java
@Query("SELECT q FROM Question q WHERE q.text LIKE %:keyword%")
List<Question> findByTextContaining(@Param("keyword") String keyword);
```
Used in: `QuizService.findQuestionsByKeyword()` (line 80-92)

**Another JPQL Read Query**:
**Where**: `GameResultRepository.java` lines 17-18
```java
@Query("SELECT gr FROM GameResult gr WHERE gr.player.id = :playerId")
List<GameResult> findByPlayerId(@Param("playerId") Long playerId);
```

**Transactional Modifying Query**:
**Where**: `GameResultRepository.java` lines 23-26
```java
@Modifying
@Transactional
@Query("UPDATE GameResult gr SET gr.score = :newScore WHERE gr.id = :id")
int updateScoreById(@Param("id") Long id, @Param("newScore") int newScore);
```
Used in: `QuizService.updateGameResultScore()` (line 164-177)

---

#### 4. Log the exceptions and the execution time of your JPQL statements (both on screen and in a file)

**Where**: `QuizService.java` -- every query method has timing + exception logging

**Example pattern** (from `findQuestionsByKeyword()`, lines 80-92):
```java
long start = System.currentTimeMillis();
try {
    List<Question> result = questionRepository.findByTextContaining(keyword);
    long duration = System.currentTimeMillis() - start;
    logger.info("JPQL findQuestionsByKeyword executed in {} ms", duration);
    return result;
} catch (Exception e) {
    long duration = System.currentTimeMillis() - start;
    logger.error("JPQL findQuestionsByKeyword failed after {} ms: {}", duration, e.getMessage());
    throw e;
}
```

**Logging configuration** (`application.properties` lines 22-28):
```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
logging.level.lab11=DEBUG

# Log to file
logging.file.name=logs/quiz-jpa.log
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

Logs appear:
- **On screen**: console output from Spring Boot
- **In file**: `Lab11/logs/quiz-jpa.log`

---

#### 5. Implement an auditing mechanism for tracking changes in your database

**Where**: `src/main/java/lab11/audit/AuditService.java`

```java
@Service
public class AuditService {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logChange(String entityName, String action, String details) {
        AuditLog log = new AuditLog(entityName, action, details);
        auditLogRepository.save(log);
        logger.info("AUDIT: entity={}, action={}, details={}", entityName, action, details);
    }
}
```

**Entity**: `AuditLog.java` -- stores entity name, action (CREATE/UPDATE), details, timestamp.

**Usage**: Every create/update operation in `QuizService` calls `auditService.logChange()`:
- `createPlayer()` -> logs "Player CREATE"
- `createQuestion()` -> logs "Question CREATE"
- `createGame()` -> logs "Game CREATE"
- `createGameResult()` -> logs "GameResult CREATE"
- `updateGameResultScore()` -> logs "GameResult UPDATE"

**Test**: `QuizServiceTest.testAuditLogCreated()` verifies audit entries are created.

---

### ADVANCED (2 points)

#### 1. Use Criteria API / Specification to implement a dynamic search for results with multiple optional filters

**Where**: `GameResultSpecification.java` + `QuizService.searchResults()` + `QuizController.searchResults()`

**GameResultSpecification.java** defines 4 optional filters:
```java
public static Specification<GameResult> hasPlayerUsername(String username)
public static Specification<GameResult> hasGameName(String gameName)
public static Specification<GameResult> scoreGreaterThanOrEqual(Integer minScore)
public static Specification<GameResult> playedAfter(LocalDateTime after)
```

Each filter returns `cb.conjunction()` (no-op) when the parameter is null, so they can be **combined dynamically**.

**QuizService.java** (lines 181-197):
```java
public List<GameResult> searchResults(String playerUsername, String gameName,
                                       Integer minScore, LocalDateTime playedAfter) {
    Specification<GameResult> spec = Specification.where(
        GameResultSpecification.hasPlayerUsername(playerUsername))
        .and(GameResultSpecification.hasGameName(gameName))
        .and(GameResultSpecification.scoreGreaterThanOrEqual(minScore))
        .and(GameResultSpecification.playedAfter(playedAfter));
    return gameResultRepository.findAll(spec);
}
```

**QuizController.java** (lines 95-107) exposes it as REST:
```java
@GetMapping("/results/search")
public ResponseEntity<List<GameResult>> searchResults(
        @RequestParam(required = false) String playerUsername,
        @RequestParam(required = false) String gameName,
        @RequestParam(required = false) Integer minScore,
        @RequestParam(required = false) String playedAfter) { ... }
```

**Example usage**:
```bash
curl "http://localhost:8080/api/results/search?playerUsername=alice&minScore=2"
```

**Note**: Joins use `JoinType.LEFT` to avoid excluding results when a filter is not applied.

---

#### 2. Implement second-level caching and query caching + JMH benchmarking

**Status**: PARTIALLY IMPLEMENTED

**Where**: `pom.xml` lines 51-61 -- JMH dependencies are included:
```xml
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-core</artifactId>
    <version>1.37</version>
</dependency>
```

**Where**: `application.properties` lines 14-15 -- cache config (currently disabled to avoid provider classpath issues):
```properties
spring.jpa.properties.hibernate.cache.use_second_level_cache=false
spring.jpa.properties.hibernate.cache.use_query_cache=false
```

To **enable** second-level caching:
1. Add Ehcache or Caffeine dependency to `pom.xml`
2. Change the properties above to `true`
3. Add `@Cacheable` or `@Cache` annotations to entities
4. Create JMH benchmark classes in `src/test/java/lab11/benchmark/`

The infrastructure is in place but caching is disabled by default because the cache provider classes were not on the classpath during initial build.

---

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.8+

### 1. Compile
```bash
cd /home/teodor/Documents/PA_26_2E1_NEGURA_TEODOR-ALEXANDRU/Lab11
mvn clean compile
```

### 2. Run the application
```bash
mvn spring-boot:run
```

The application will:
1. Start an embedded **H2 database**
2. Auto-create tables from JPA entities (`ddl-auto=create-drop`)
3. Run the **CommandLineRunner demo** which creates sample players, questions, a game, and results
4. Print audit logs and query timings to console
5. Start a **REST API server** on port 8080

### 3. Access the application
- **REST API**: http://localhost:8080/api
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:quizdb`
  - User: `sa`
  - Password: (empty)

### 4. Run tests
```bash
mvn test
```

---

## REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/players` | Create a player (body: `{"username":"...","password":"..."}`) |
| GET | `/api/players` | List all players |
| POST | `/api/questions` | Create a question (body: `{"text":"...","correctAnswer":"...","options":["..."]}`) |
| GET | `/api/questions` | List all questions |
| GET | `/api/questions/search?keyword=...` | Search questions by keyword (JPQL) |
| POST | `/api/games` | Create a game (body: `{"name":"...","questionIds":[1,2,3]}`) |
| GET | `/api/games` | List all games |
| POST | `/api/results` | Create a result (body: `{"playerId":1,"gameId":1,"score":3,"totalQuestions":5}`) |
| GET | `/api/results/player/{playerId}` | Get results by player (JPQL) |
| GET | `/api/results/game/{gameId}` | Get results by game (JPQL) |
| PUT | `/api/results/{id}/score` | Update score (modifying JPQL) |
| GET | `/api/results/search` | Dynamic search with Criteria API (optional params: playerUsername, gameName, minScore, playedAfter) |
| GET | `/api/audit` | Get all audit logs |

---

## Example API Calls

```bash
# Create a player
curl -X POST http://localhost:8080/api/players \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"secret"}'

# Create a question
curl -X POST http://localhost:8080/api/questions \
  -H "Content-Type: application/json" \
  -d '{"text":"What is Java?","correctAnswer":"A language","options":["A language","A car","A city"]}'

# Search questions by keyword (JPQL)
curl "http://localhost:8080/api/questions/search?keyword=Java"

# Create a game with questions
curl -X POST http://localhost:8080/api/games \
  -H "Content-Type: application/json" \
  -d '{"name":"Programming Quiz","questionIds":[1,2]}'

# Create a result
curl -X POST http://localhost:8080/api/results \
  -H "Content-Type: application/json" \
  -d '{"playerId":1,"gameId":1,"score":3,"totalQuestions":5}'

# Update score (modifying query)
curl -X PUT http://localhost:8080/api/results/1/score \
  -H "Content-Type: application/json" \
  -d '{"score":5}'

# Dynamic search with Criteria API
curl "http://localhost:8080/api/results/search?playerUsername=alice&minScore=2"

# View audit logs
curl http://localhost:8080/api/audit
```

---

## Technologies Used

| Technology | Purpose |
|---|---|
| **Spring Boot 3.2.5** | Application framework |
| **Spring Data JPA** | Simplifies repository layer |
| **Hibernate** | JPA provider (ORM) |
| **H2 Database** | Embedded relational database |
| **SLF4J + Logback** | Logging (console + file) |
| **JMH** | Benchmarking library (Advanced) |
| **JUnit 5** | Testing |

---

## Data Flow Diagram

```
+------------+     HTTP      +------------------+     JPA      +--------+
|   Client   | <-----------> | QuizController   | <----------> |   DB   |
+------------+               +------------------+              |  (H2)  |
                             |                    |              +--------+
                             | QuizService        |
                             | - JPQL queries     |
                             | - Criteria search  |
                             | - Audit logging    |
                             |                    |
                             | Repositories       |
                             | - PlayerRepository |
                             | - QuestionRepo     |
                             | - GameResultRepo   |
                             +--------------------+
```

---

## Common Issues

**LazyInitializationException**: This happens when accessing lazy-loaded collections outside a transaction. The demo in `QuizJpaApplication` re-fetches entities from the database before accessing their collections to avoid this.

**H2 Console not accessible**: Make sure `spring.h2.console.enabled=true` is set in `application.properties`.

**Port 8080 already in use**: Change the port with `--server.port=8081` or add `server.port=8081` to `application.properties`.
