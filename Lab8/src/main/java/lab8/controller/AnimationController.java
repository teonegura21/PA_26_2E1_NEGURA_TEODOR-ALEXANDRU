package lab8.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import lab8.model.Cell;

import java.util.*;

/* BONUS - Animation controller for maze generation with vibrant effects */
public class AnimationController {
    private MazeController mazeController;
    private Timeline timeline;
    private double speed = 50; // milliseconds per step
    private boolean running = false;
    private Set<Cell> visitedCells = new HashSet<>();
    private Set<Cell> backtrackedCells = new HashSet<>();
    private Cell currentCell = null;
    
    public AnimationController(MazeController mazeController) {
        this.mazeController = mazeController;
    }
    
    public void setSpeed(double speed) {
        // Map 1-100 to 500ms-10ms
        this.speed = 510 - (speed * 5);
    }
    
    public void animateGeneration(List<MazeGenerator.GenerationStep> steps, Runnable onComplete) {
        stop();
        running = true;
        visitedCells.clear();
        backtrackedCells.clear();
        currentCell = null;
        
        timeline = new Timeline();
        
        for (int i = 0; i < steps.size(); i++) {
            MazeGenerator.GenerationStep step = steps.get(i);
            
            KeyFrame keyFrame = new KeyFrame(Duration.millis(speed * i), event -> {
                if (!running) return;
                
                Cell prevCurrent = currentCell;
                currentCell = step.getCurrent();
                Cell next = step.getNext();
                
                if (step.getType() == MazeGenerator.GenerationStep.Type.BACKTRACK) {
                    // Mark as backtracked (will be shown in different color)
                    if (prevCurrent != null) {
                        backtrackedCells.add(prevCurrent);
                    }
                } else {
                    // Mark visited cells
                    visitedCells.add(currentCell);
                    if (next != null) {
                        visitedCells.add(next);
                    }
                }
                
                Platform.runLater(() -> {
                    if (mazeController.getMaze() != null) {
                        // Reset all visited flags
                        mazeController.getMaze().resetVisited();
                        
                        // Mark visited cells
                        for (Cell cell : visitedCells) {
                            cell.setVisited(true);
                        }
                        
                        mazeController.updateCanvas();
                    }
                });
            });
            
            timeline.getKeyFrames().add(keyFrame);
        }
        
        timeline.setOnFinished(event -> {
            running = false;
            currentCell = null;
            if (mazeController.getMaze() != null) {
                mazeController.getMaze().resetVisited();
            }
            if (onComplete != null) {
                onComplete.run();
            }
        });
        
        timeline.play();
    }
    
    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
        running = false;
        visitedCells.clear();
        backtrackedCells.clear();
        currentCell = null;
    }
    
    public boolean isRunning() { return running; }
    public Cell getCurrentCell() { return currentCell; }
    public Set<Cell> getVisitedCells() { return new HashSet<>(visitedCells); }
    public Set<Cell> getBacktrackedCells() { return new HashSet<>(backtrackedCells); }
}
