package lab9;

import java.util.*;

public class Bunny implements Runnable {
    private final String id;
    private final Maze maze;
    private final SharedMemory sharedMemory;
    private int row;
    private int col;
    private volatile long moveDelay = 500;
    private volatile boolean paused = false;
    private volatile boolean running = true;
    private final Random random = new Random();

    public Bunny(String id, Maze maze, SharedMemory sharedMemory, int startRow, int startCol) {
        this.id = id;
        this.maze = maze;
        this.sharedMemory = sharedMemory;
        this.row = startRow;
        this.col = startCol;
    }

    public void setMoveDelay(long delay) {
        this.moveDelay = Math.max(50, delay);
    }

    public long getMoveDelay() {
        return moveDelay;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public void stop() {
        running = false;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public String getId() { return id; }

    @Override
    public void run() {
        sharedMemory.updateBunnyPosition(row, col);
        while (running && !sharedMemory.isGameOver()) {
            if (paused) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                continue;
            }

            Cell current = maze.getCell(row, col);
            List<Cell> neighbors = maze.getNeighbors(current);
            if (neighbors.isEmpty()) {
                try {
                    Thread.sleep(moveDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                continue;
            }

            // Prefer moving towards exit
            Cell best = null;
            int bestDist = Integer.MAX_VALUE;
            for (Cell n : neighbors) {
                int dist = Math.abs(n.getRow() - maze.getExitRow()) + Math.abs(n.getCol() - maze.getExitCol());
                if (dist < bestDist) {
                    bestDist = dist;
                    best = n;
                }
            }
            if (best == null || random.nextDouble() < 0.3) {
                best = neighbors.get(random.nextInt(neighbors.size()));
            }

            Cell target = maze.getCell(best.getRow(), best.getCol());
            if (target == null) continue;
            target.lock();
            try {
                if (sharedMemory.isGameOver()) break;
                // Check if a robot occupies this cell
                if (!sharedMemory.tryOccupy(target.getRow(), target.getCol(), "bunny")) {
                    continue;
                }
                sharedMemory.releasePosition(row, col, "bunny");
                row = target.getRow();
                col = target.getCol();
                sharedMemory.updateBunnyPosition(row, col);
                if (maze.isExit(row, col)) {
                    sharedMemory.setBunnyEscaped(true);
                    break;
                }
            } finally {
                target.unlock();
            }

            try {
                Thread.sleep(moveDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
