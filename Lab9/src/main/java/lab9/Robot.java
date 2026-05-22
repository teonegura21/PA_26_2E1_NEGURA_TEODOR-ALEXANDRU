package lab9;

import java.util.*;

public class Robot implements Runnable {
    private final String id;
    private final Maze maze;
    private final SharedMemory sharedMemory;
    private int row;
    private int col;
    private volatile long moveDelay = 700;
    private volatile boolean paused = false;
    private volatile boolean running = true;
    private final Random random = new Random();
    private final Stack<Cell> dfsStack = new Stack<>();
    private final Set<String> localVisited = new HashSet<>();
    private static final int PROXIMITY_RANGE = 3;

    public Robot(String id, Maze maze, SharedMemory sharedMemory, int startRow, int startCol) {
        this.id = id;
        this.maze = maze;
        this.sharedMemory = sharedMemory;
        this.row = startRow;
        this.col = startCol;
        this.dfsStack.push(maze.getCell(startRow, startCol));
        this.localVisited.add(startRow + "," + startCol);
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
        sharedMemory.tryOccupy(row, col, id);
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

            if (sharedMemory.isGameOver()) break;

            // Proximity sensor: check if bunny is close
            int[] bunnyPos = sharedMemory.getBunnyPosition();
            if (bunnyPos[0] >= 0) {
                int dist = Math.abs(row - bunnyPos[0]) + Math.abs(col - bunnyPos[1]);
                if (dist <= PROXIMITY_RANGE) {
                    sharedMemory.setProximityAlert(true, bunnyPos[0], bunnyPos[1]);
                }
            }

            // If proximity alert, move towards bunny using BFS shortest path
            if (sharedMemory.isProximityAlert()) {
                int[] alert = sharedMemory.getAlertPosition();
                Cell next = bfsNextStep(row, col, alert[0], alert[1]);
                if (next != null) {
                    moveTo(next);
                    if (row == bunnyPos[0] && col == bunnyPos[1]) {
                        sharedMemory.setBunnyCaught(true);
                        break;
                    }
                    try {
                        Thread.sleep(moveDelay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    continue;
                }
            }

            // DFS-based systematic exploration with shared memory
            Cell current = maze.getCell(row, col);
            List<Cell> neighbors = maze.getNeighbors(current);
            List<Cell> candidates = new ArrayList<>();
            for (Cell n : neighbors) {
                String key = n.getRow() + "," + n.getCol();
                if (!localVisited.contains(key) && !sharedMemory.isVisitedByRobots(n.getRow(), n.getCol())) {
                    candidates.add(n);
                }
            }

            if (!candidates.isEmpty()) {
                Cell next = candidates.get(random.nextInt(candidates.size()));
                dfsStack.push(current);
                moveTo(next);
                localVisited.add(next.getRow() + "," + next.getCol());
                sharedMemory.setVisitedByRobots(next.getRow(), next.getCol());
            } else if (!dfsStack.isEmpty()) {
                Cell back = dfsStack.pop();
                moveTo(back);
            } else {
                // Random move if stuck
                if (!neighbors.isEmpty()) {
                    Cell next = neighbors.get(random.nextInt(neighbors.size()));
                    moveTo(next);
                }
            }

            // Check catch
            int[] bp = sharedMemory.getBunnyPosition();
            if (bp[0] == row && bp[1] == col) {
                sharedMemory.setBunnyCaught(true);
                break;
            }

            try {
                Thread.sleep(moveDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        sharedMemory.releasePosition(row, col, id);
    }

    private void moveTo(Cell target) {
        int oldRow = row;
        int oldCol = col;
        if (!sharedMemory.tryOccupy(target.getRow(), target.getCol(), id)) {
            return;
        }
        sharedMemory.releasePosition(oldRow, oldCol, id);
        row = target.getRow();
        col = target.getCol();
    }

    private Cell bfsNextStep(int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow == toRow && fromCol == toCol) return null;
        Queue<int[]> queue = new LinkedList<>();
        Map<String, int[]> parent = new HashMap<>();
        queue.add(new int[]{fromRow, fromCol});
        parent.put(fromRow + "," + fromCol, null);
        int[][] dirs = {{-1,0},{0,1},{1,0},{0,-1}};
        Direction[] dirVals = {Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            Cell curCell = maze.getCell(cur[0], cur[1]);
            for (int i = 0; i < 4; i++) {
                if (curCell.hasWall(dirVals[i])) continue;
                int nr = cur[0] + dirs[i][0];
                int nc = cur[1] + dirs[i][1];
                if (nr < 0 || nr >= maze.getRows() || nc < 0 || nc >= maze.getCols()) continue;
                String key = nr + "," + nc;
                if (parent.containsKey(key)) continue;
                parent.put(key, cur);
                if (nr == toRow && nc == toCol) {
                    // Backtrack to find first step
                    int[] step = new int[]{nr, nc};
                    while (true) {
                        int[] p = parent.get(step[0] + "," + step[1]);
                        if (p == null) return null;
                        if (p[0] == fromRow && p[1] == fromCol) {
                            return maze.getCell(step[0], step[1]);
                        }
                        step = p;
                    }
                }
                queue.add(new int[]{nr, nc});
            }
        }
        return null;
    }
}
