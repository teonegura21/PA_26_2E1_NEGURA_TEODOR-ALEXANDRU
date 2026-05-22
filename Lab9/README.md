# Lab 9 - Java Concurrency Maze Game

## Description
A concurrent maze game where a bunny tries to escape a maze while multiple robots try to catch it.

## Features
- **Maze generation**: Recursive backtracking (perfect maze).
- **Concurrency**: Bunny and each robot run in separate threads.
- **Synchronization**: Cell-level locks and shared memory with read-write locks.
- **Shared memory**: Robots collaboratively mark visited cells and share bunny proximity alerts.
- **Systematic exploration**: Robots use DFS with backtracking; when bunny is sensed nearby, they switch to BFS shortest-path chase.
- **Keyboard controls**: Speed up/slow down/pause/resume bunny and robots.
- **Daemon manager**: Displays game state periodically and enforces a time limit.

## Compile and Run

### Maven
```bash
cd /home/teodor/Documents/PA_26_2E1_NEGURA_TEODOR-ALEXANDRU/Lab9
mvn compile exec:java
```

### Plain Java
```bash
cd /home/teodor/Documents/PA_26_2E1_NEGURA_TEODOR-ALEXANDRU/Lab9/src/main/java
javac lab9/*.java
java lab9.Main
```

## Keyboard Commands (while running)
- `+` / `-` : speed up / slow down bunny
- `r+` / `r-` : speed up / slow down all robots
- `p` : pause all
- `c` : resume all
- `q` : quit
