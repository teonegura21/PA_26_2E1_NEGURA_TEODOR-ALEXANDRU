package lab9;

import java.util.*;

public class Maze {
    private final int rows;
    private final int cols;
    private final Cell[][] grid;
    private final int exitRow;
    private final int exitCol;

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Cell(r, c);
            }
        }
        generateMaze();
        this.exitRow = rows - 1;
        this.exitCol = cols - 1;
    }

    private void generateMaze() {
        Stack<Cell> stack = new Stack<>();
        boolean[][] visited = new boolean[rows][cols];
        Random rand = new Random();
        Cell current = grid[0][0];
        visited[0][0] = true;
        stack.push(current);

        while (!stack.isEmpty()) {
            current = stack.peek();
            List<int[]> neighbors = new ArrayList<>();
            int r = current.getRow();
            int c = current.getCol();

            if (r > 0 && !visited[r - 1][c]) neighbors.add(new int[]{r - 1, c, 0}); // UP
            if (c < cols - 1 && !visited[r][c + 1]) neighbors.add(new int[]{r, c + 1, 1}); // RIGHT
            if (r < rows - 1 && !visited[r + 1][c]) neighbors.add(new int[]{r + 1, c, 2}); // DOWN
            if (c > 0 && !visited[r][c - 1]) neighbors.add(new int[]{r, c - 1, 3}); // LEFT

            if (neighbors.isEmpty()) {
                stack.pop();
            } else {
                int[] next = neighbors.get(rand.nextInt(neighbors.size()));
                int nr = next[0];
                int nc = next[1];
                int dir = next[2];
                Direction d = Direction.values()[dir];
                Direction opposite = Direction.values()[(dir + 2) % 4];
                current.removeWall(d);
                grid[nr][nc].removeWall(opposite);
                visited[nr][nc] = true;
                stack.push(grid[nr][nc]);
            }
        }
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return null;
        return grid[row][col];
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int getExitRow() { return exitRow; }
    public int getExitCol() { return exitCol; }

    public boolean isExit(int row, int col) {
        return row == exitRow && col == exitCol;
    }

    public List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int r = cell.getRow();
        int c = cell.getCol();
        if (!cell.hasWall(Direction.UP)) neighbors.add(grid[r - 1][c]);
        if (!cell.hasWall(Direction.RIGHT)) neighbors.add(grid[r][c + 1]);
        if (!cell.hasWall(Direction.DOWN)) neighbors.add(grid[r + 1][c]);
        if (!cell.hasWall(Direction.LEFT)) neighbors.add(grid[r][c - 1]);
        return neighbors;
    }
}
