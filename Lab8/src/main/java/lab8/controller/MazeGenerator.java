package lab8.controller;

import lab8.model.Cell;
import lab8.model.Maze;

import java.util.*;

/* BONUS - Recursive Backtracking (DFS) Maze Generator */
public class MazeGenerator {
    private Maze maze;
    private Random random = new Random();
    
    public MazeGenerator(Maze maze) {
        this.maze = maze;
    }
    
    public List<GenerationStep> generateSteps() {
        List<GenerationStep> steps = new ArrayList<>();
        Stack<Cell> stack = new Stack<>();
        
        Cell start = maze.getCell(0, 0);
        start.setVisited(true);
        stack.push(start);
        
        while (!stack.isEmpty()) {
            Cell current = stack.peek();
            List<Cell> unvisited = maze.getUnvisitedNeighbors(current);
            
            if (unvisited.isEmpty()) {
                stack.pop();
                steps.add(new GenerationStep(current, null, GenerationStep.Type.BACKTRACK));
            } else {
                Cell next = unvisited.get(random.nextInt(unvisited.size()));
                removeWalls(current, next);
                next.setVisited(true);
                stack.push(next);
                steps.add(new GenerationStep(current, next, GenerationStep.Type.VISIT));
            }
        }
        
        return steps;
    }
    
    private void removeWalls(Cell current, Cell next) {
        int dr = next.getRow() - current.getRow();
        int dc = next.getCol() - current.getCol();
        
        if (dr == -1) {
            current.setTopWall(false);
            next.setBottomWall(false);
        } else if (dr == 1) {
            current.setBottomWall(false);
            next.setTopWall(false);
        } else if (dc == -1) {
            current.setLeftWall(false);
            next.setRightWall(false);
        } else if (dc == 1) {
            current.setRightWall(false);
            next.setLeftWall(false);
        }
    }
    
    /* BONUS - Step data for animation */
    public static class GenerationStep {
        public enum Type { VISIT, BACKTRACK }
        
        private final Cell current;
        private final Cell next;
        private final Type type;
        
        public GenerationStep(Cell current, Cell next, Type type) {
            this.current = current;
            this.next = next;
            this.type = type;
        }
        
        public Cell getCurrent() { return current; }
        public Cell getNext() { return next; }
        public Type getType() { return type; }
    }
}
