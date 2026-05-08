package lab8.util;

import lab8.model.Cell;
import lab8.model.Maze;

import java.util.*;

/* HOMEWORK - Validation with BFS */
public class MazeValidator {
    
    public static class ValidationResult {
        private final boolean valid;
        private final List<Cell> path;
        
        public ValidationResult(boolean valid, List<Cell> path) {
            this.valid = valid;
            this.path = path;
        }
        
        public boolean isValid() { return valid; }
        public List<Cell> getPath() { return path; }
    }
    
    public static ValidationResult validate(Maze maze) {
        Cell start = maze.getStartCell();
        Cell end = maze.getEndCell();
        
        Queue<Cell> queue = new LinkedList<>();
        Map<Cell, Cell> parentMap = new HashMap<>();
        Set<Cell> visited = new HashSet<>();
        
        queue.add(start);
        visited.add(start);
        parentMap.put(start, null);
        
        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            
            if (current == end) {
                return new ValidationResult(true, reconstructPath(parentMap, end));
            }
            
            List<Cell> neighbors = getReachableNeighbors(maze, current);
            for (Cell neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        
        return new ValidationResult(false, new ArrayList<>());
    }
    
    private static List<Cell> getReachableNeighbors(Maze maze, Cell cell) {
        List<Cell> reachable = new ArrayList<>();
        int r = cell.getRow();
        int c = cell.getCol();
        
        if (!cell.hasTopWall() && r > 0) {
            reachable.add(maze.getCell(r - 1, c));
        }
        if (!cell.hasBottomWall() && r < maze.getRows() - 1) {
            reachable.add(maze.getCell(r + 1, c));
        }
        if (!cell.hasLeftWall() && c > 0) {
            reachable.add(maze.getCell(r, c - 1));
        }
        if (!cell.hasRightWall() && c < maze.getCols() - 1) {
            reachable.add(maze.getCell(r, c + 1));
        }
        
        return reachable;
    }
    
    private static List<Cell> reconstructPath(Map<Cell, Cell> parentMap, Cell end) {
        List<Cell> path = new ArrayList<>();
        Cell current = end;
        while (current != null) {
            path.add(0, current);
            current = parentMap.get(current);
        }
        return path;
    }
    
    /* BONUS - Verify maze is a perfect maze (spanning tree) */
    public static boolean isPerfectMaze(Maze maze) {
        // Check all cells are reachable
        ValidationResult result = validate(maze);
        if (!result.isValid()) return false;
        
        // Check exactly rows*cols - 1 internal walls removed
        // In a grid with all walls: internal walls = rows*(cols-1) + (rows-1)*cols
        // In a perfect maze: we need exactly rows*cols - 1 passages
        // So removed walls = (total possible passages) - (rows*cols - 1)
        int expectedRemovedWalls = (maze.getRows() * (maze.getCols() - 1)) + 
                                   ((maze.getRows() - 1) * maze.getCols()) - 
                                   (maze.getRows() * maze.getCols() - 1);
        
        // Alternative: count passages (missing walls)
        int passageCount = 0;
        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {
                Cell cell = maze.getCell(r, c);
                // Only count right and bottom to avoid double counting
                if (c < maze.getCols() - 1 && !cell.hasRightWall()) passageCount++;
                if (r < maze.getRows() - 1 && !cell.hasBottomWall()) passageCount++;
            }
        }
        
        return passageCount == maze.getRows() * maze.getCols() - 1;
    }
}
