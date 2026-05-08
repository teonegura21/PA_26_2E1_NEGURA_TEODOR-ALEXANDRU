package lab8.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* COMPULSORY - Model */
public class Maze implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int rows;
    private int cols;
    private Cell[][] grid;
    private Cell startCell;
    private Cell endCell;
    
    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        initializeGrid();
    }
    
    private void initializeGrid() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Cell(r, c);
            }
        }
        startCell = grid[0][0];
        endCell = grid[rows - 1][cols - 1];
    }
    
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public Cell getCell(int row, int col) { return grid[row][col]; }
    public Cell getStartCell() { return startCell; }
    public Cell getEndCell() { return endCell; }
    
    public List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int r = cell.getRow();
        int c = cell.getCol();
        
        if (r > 0) neighbors.add(grid[r - 1][c]);
        if (r < rows - 1) neighbors.add(grid[r + 1][c]);
        if (c > 0) neighbors.add(grid[r][c - 1]);
        if (c < cols - 1) neighbors.add(grid[r][c + 1]);
        
        return neighbors;
    }
    
    public List<Cell> getUnvisitedNeighbors(Cell cell) {
        List<Cell> unvisited = new ArrayList<>();
        for (Cell neighbor : getNeighbors(cell)) {
            if (!neighbor.isVisited()) {
                unvisited.add(neighbor);
            }
        }
        return unvisited;
    }
    
    public void resetVisited() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c].setVisited(false);
            }
        }
    }
    
    public void resetAllWalls() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c].resetWalls();
            }
        }
    }
    
    public int getTotalWallCount() {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                count += grid[r][c].getWallCount();
            }
        }
        return count / 2; // Each internal wall counted twice
    }
    
    public List<Cell> getAllCells() {
        List<Cell> cells = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells.add(grid[r][c]);
            }
        }
        return cells;
    }
}
