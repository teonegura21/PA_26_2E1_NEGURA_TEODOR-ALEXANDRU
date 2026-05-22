package lab9;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SharedMemory {
    private final Maze maze;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private volatile int bunnyRow = -1;
    private volatile int bunnyCol = -1;
    private volatile boolean bunnyCaught = false;
    private volatile boolean bunnyEscaped = false;
    private final Set<String> robotPositions = new HashSet<>();
    private final boolean[][] visitedByRobots;
    private volatile int gameTick = 0;
    private volatile boolean proximityAlert = false;
    private volatile int alertRow = -1;
    private volatile int alertCol = -1;

    public SharedMemory(Maze maze) {
        this.maze = maze;
        this.visitedByRobots = new boolean[maze.getRows()][maze.getCols()];
    }

    public void updateBunnyPosition(int row, int col) {
        lock.writeLock().lock();
        try {
            bunnyRow = row;
            bunnyCol = col;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int[] getBunnyPosition() {
        lock.readLock().lock();
        try {
            return new int[]{bunnyRow, bunnyCol};
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setBunnyCaught(boolean caught) {
        lock.writeLock().lock();
        try {
            bunnyCaught = caught;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isBunnyCaught() {
        lock.readLock().lock();
        try {
            return bunnyCaught;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setBunnyEscaped(boolean escaped) {
        lock.writeLock().lock();
        try {
            bunnyEscaped = escaped;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isBunnyEscaped() {
        lock.readLock().lock();
        try {
            return bunnyEscaped;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isGameOver() {
        lock.readLock().lock();
        try {
            return bunnyCaught || bunnyEscaped;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean tryOccupy(int row, int col, String robotId) {
        lock.writeLock().lock();
        try {
            String key = row + "," + col;
            if (robotPositions.contains(key)) {
                return false;
            }
            robotPositions.add(key);
            visitedByRobots[row][col] = true;
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void releasePosition(int row, int col, String robotId) {
        lock.writeLock().lock();
        try {
            robotPositions.remove(row + "," + col);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isVisitedByRobots(int row, int col) {
        lock.readLock().lock();
        try {
            return visitedByRobots[row][col];
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setVisitedByRobots(int row, int col) {
        lock.writeLock().lock();
        try {
            visitedByRobots[row][col] = true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void incrementTick() {
        lock.writeLock().lock();
        try {
            gameTick++;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int getGameTick() {
        lock.readLock().lock();
        try {
            return gameTick;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setProximityAlert(boolean alert, int row, int col) {
        lock.writeLock().lock();
        try {
            proximityAlert = alert;
            alertRow = row;
            alertCol = col;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isProximityAlert() {
        lock.readLock().lock();
        try {
            return proximityAlert;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int[] getAlertPosition() {
        lock.readLock().lock();
        try {
            return new int[]{alertRow, alertCol};
        } finally {
            lock.readLock().unlock();
        }
    }

    public Maze getMaze() {
        return maze;
    }
}
