package lab9;

import java.util.*;

public class GameManager implements Runnable {
    private final Maze maze;
    private final SharedMemory sharedMemory;
    private final Bunny bunny;
    private final List<Robot> robots;
    private final List<Thread> threads;
    private volatile boolean running = true;
    private final long timeLimitMs;
    private final long startTime;

    public GameManager(Maze maze, SharedMemory sharedMemory, Bunny bunny, List<Robot> robots, long timeLimitMs) {
        this.maze = maze;
        this.sharedMemory = sharedMemory;
        this.bunny = bunny;
        this.robots = robots;
        this.timeLimitMs = timeLimitMs;
        this.startTime = System.currentTimeMillis();
        this.threads = new ArrayList<>();
    }

    public void start() {
        Thread bunnyThread = new Thread(bunny);
        bunnyThread.setName("Bunny");
        threads.add(bunnyThread);
        bunnyThread.start();

        for (Robot robot : robots) {
            Thread t = new Thread(robot);
            t.setName(robot.getId());
            threads.add(t);
            t.start();
        }

        Thread managerThread = new Thread(this);
        managerThread.setDaemon(true);
        managerThread.setName("GameManager");
        managerThread.start();
    }

    public void stopAll() {
        running = false;
        bunny.stop();
        for (Robot robot : robots) {
            robot.stop();
        }
        for (Thread t : threads) {
            t.interrupt();
        }
    }

    @Override
    public void run() {
        while (running) {
            long elapsed = System.currentTimeMillis() - startTime;
            if (timeLimitMs > 0 && elapsed > timeLimitMs) {
                System.out.println("\n[GameManager] Time limit exceeded! Stopping game.");
                sharedMemory.setBunnyCaught(true); // Force end
                stopAll();
                break;
            }

            if (sharedMemory.isGameOver()) {
                stopAll();
                break;
            }

            sharedMemory.incrementTick();
            render(elapsed);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // Final render
        long elapsed = System.currentTimeMillis() - startTime;
        render(elapsed);
        if (sharedMemory.isBunnyEscaped()) {
            System.out.println("\n=== BUNNY ESCAPED! ===");
        } else if (sharedMemory.isBunnyCaught()) {
            System.out.println("\n=== BUNNY CAUGHT! ===");
        }
    }

    private void render(long elapsedMs) {
        StringBuilder sb = new StringBuilder();
        sb.append("\033[H\033[2J"); // Clear screen (ANSI)
        sb.append("=== Lab 9 Concurrency Maze Game ===\n");
        sb.append("Time: ").append(elapsedMs / 1000).append("s / ").append(timeLimitMs / 1000).append("s\n");
        sb.append("Tick: ").append(sharedMemory.getGameTick()).append("\n");
        sb.append("Bunny: ").append(bunny.getRow()).append(",").append(bunny.getCol());
        if (sharedMemory.isProximityAlert()) {
            sb.append(" [ALERT!]");
        }
        sb.append("\nRobots:\n");
        for (Robot r : robots) {
            sb.append("  ").append(r.getId()).append(" -> ").append(r.getRow()).append(",").append(r.getCol()).append("\n");
        }
        sb.append("\n");

        int rows = maze.getRows();
        int cols = maze.getCols();

        // Build char grid for cells
        char[][] display = new char[rows * 2 + 1][cols * 4 + 1];
        for (int i = 0; i < display.length; i++) {
            Arrays.fill(display[i], ' ');
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = maze.getCell(r, c);
                int dr = r * 2 + 1;
                int dc = c * 4 + 2;

                // Walls
                if (cell.hasWall(Direction.UP)) {
                    for (int k = 0; k < 5; k++) display[r * 2][c * 4 + k] = '#';
                }
                if (cell.hasWall(Direction.LEFT)) {
                    display[dr][c * 4] = '#';
                    display[dr - 1][c * 4] = '#';
                    display[dr + 1][c * 4] = '#';
                }
                if (cell.hasWall(Direction.RIGHT)) {
                    display[dr][c * 4 + 4] = '#';
                    display[dr - 1][c * 4 + 4] = '#';
                    display[dr + 1][c * 4 + 4] = '#';
                }
                if (cell.hasWall(Direction.DOWN)) {
                    for (int k = 0; k < 5; k++) display[r * 2 + 2][c * 4 + k] = '#';
                }

                // Corners
                display[r * 2][c * 4] = '#';
                display[r * 2][c * 4 + 4] = '#';
                display[r * 2 + 2][c * 4] = '#';
                display[r * 2 + 2][c * 4 + 4] = '#';

                // Entities
                char ch = '.';
                if (maze.isExit(r, c)) ch = 'E';
                if (bunny.getRow() == r && bunny.getCol() == c) ch = 'B';
                for (Robot robot : robots) {
                    if (robot.getRow() == r && robot.getCol() == c) {
                        ch = 'R';
                        break;
                    }
                }
                display[dr][dc] = ch;
            }
        }

        for (char[] rowChars : display) {
            sb.append(rowChars).append("\n");
        }

        System.out.print(sb.toString());
    }
}
