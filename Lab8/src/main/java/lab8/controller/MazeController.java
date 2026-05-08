package lab8.controller;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lab8.model.Cell;
import lab8.model.Maze;
import lab8.util.MazeExporter;
import lab8.util.MazeSerializer;
import lab8.util.MazeValidator;
import lab8.view.MazeCanvas;

import java.io.File;
import java.io.IOException;
import java.util.*;

/* COMPULSORY/HOMEWORK/BONUS - Main Controller */
public class MazeController {
    private Maze maze;
    private MazeCanvas mazeCanvas;
    private AnimationController animationController;
    private List<Cell> currentPath = new ArrayList<>();
    private boolean isGenerating = false;
    
    public MazeController(MazeCanvas mazeCanvas) {
        this.mazeCanvas = mazeCanvas;
        this.animationController = new AnimationController(this);
        
        // Auto-revalidate when walls are modified
        this.mazeCanvas.setOnWallModified(() -> {
            if (maze != null) {
                validateMaze();
            }
        });
    }
    
    public void createMaze(int rows, int cols) {
        this.maze = new Maze(rows, cols);
        currentPath.clear();
        mazeCanvas.setMaze(maze);
        mazeCanvas.draw();
    }
    
    public void createRandomMaze() {
        if (maze == null) return;
        maze.resetAllWalls();
        Random random = new Random();
        
        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {
                Cell cell = maze.getCell(r, c);
                if (random.nextDouble() < 0.3) {
                    if (r > 0 && random.nextBoolean()) {
                        cell.setTopWall(false);
                        maze.getCell(r - 1, c).setBottomWall(false);
                    }
                    if (c < maze.getCols() - 1 && random.nextBoolean()) {
                        cell.setRightWall(false);
                        maze.getCell(r, c + 1).setLeftWall(false);
                    }
                }
            }
        }
        currentPath.clear();
        mazeCanvas.draw();
    }
    
    /* BONUS - Generate perfect maze with animation */
    public void generatePerfectMaze() {
        if (maze == null || isGenerating) return;
        isGenerating = true;
        maze.resetAllWalls();
        maze.resetVisited();
        currentPath.clear();
        
        MazeGenerator generator = new MazeGenerator(maze);
        List<MazeGenerator.GenerationStep> steps = generator.generateSteps();
        
        animationController.animateGeneration(steps, () -> {
            isGenerating = false;
            Platform.runLater(() -> {
                mazeCanvas.draw();
                // Auto-validate after generation completes
                validateMaze();
            });
        });
    }
    
    public void stopGeneration() {
        animationController.stop();
        isGenerating = false;
    }
    
    /* HOMEWORK - Validate and show path */
    public void validateMaze() {
        if (maze == null) return;
        MazeValidator.ValidationResult result = MazeValidator.validate(maze);
        currentPath = result.getPath();
        mazeCanvas.setPath(currentPath);
        mazeCanvas.draw();
    }
    
    public void clearPath() {
        currentPath.clear();
        mazeCanvas.setPath(currentPath);
        mazeCanvas.draw();
    }
    
    /* HOMEWORK - Export to PNG */
    public void exportToPng(File file) throws IOException {
        if (mazeCanvas != null) {
            MazeExporter.exportToPng(mazeCanvas, file);
        }
    }
    
    /* HOMEWORK - Save/Load */
    public void saveMaze(File file) throws IOException {
        if (maze != null) {
            MazeSerializer.save(maze, file);
        }
    }
    
    public void loadMaze(File file) throws IOException, ClassNotFoundException {
        this.maze = MazeSerializer.load(file);
        currentPath.clear();
        mazeCanvas.setMaze(maze);
        mazeCanvas.draw();
    }
    
    public void resetMaze() {
        if (maze != null) {
            maze.resetAllWalls();
            maze.resetVisited();
            currentPath.clear();
            mazeCanvas.draw();
        }
    }
    
    public Maze getMaze() { return maze; }
    public boolean isGenerating() { return isGenerating; }
    public AnimationController getAnimationController() { return animationController; }
    
    public void updateCanvas() {
        mazeCanvas.draw();
    }
}
