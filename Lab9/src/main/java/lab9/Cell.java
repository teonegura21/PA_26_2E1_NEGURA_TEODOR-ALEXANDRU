package lab9;

import java.util.concurrent.locks.ReentrantLock;

public class Cell {
    private final int row;
    private final int col;
    private final boolean[] walls; // top, right, bottom, left
    private volatile boolean visitedByRobot;
    private volatile int lastVisitTick;
    private final ReentrantLock lock = new ReentrantLock();

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.walls = new boolean[]{true, true, true, true};
        this.visitedByRobot = false;
        this.lastVisitTick = -1;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public boolean hasWall(Direction dir) {
        return walls[dir.ordinal()];
    }

    public void removeWall(Direction dir) {
        walls[dir.ordinal()] = false;
    }

    public void setVisitedByRobot(boolean visited, int tick) {
        this.visitedByRobot = visited;
        this.lastVisitTick = tick;
    }

    public boolean isVisitedByRobot() { return visitedByRobot; }
    public int getLastVisitTick() { return lastVisitTick; }

    public ReentrantLock getLock() { return lock; }

    public void lock() { lock.lock(); }
    public void unlock() { lock.unlock(); }
}
