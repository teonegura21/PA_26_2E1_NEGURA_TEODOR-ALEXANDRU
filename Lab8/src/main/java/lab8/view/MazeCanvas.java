package lab8.view;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;
import lab8.model.Cell;
import lab8.model.Maze;

import java.util.ArrayList;
import java.util.List;

/* COMPULSORY/HOMEWORK/BONUS - Custom canvas for maze drawing and interaction */
public class MazeCanvas extends Canvas {
    private Maze maze;
    private List<Cell> path = new ArrayList<>();
    private double cellSize;
    private double offsetX;
    private double offsetY;
    private static final double WALL_THICKNESS = 2.5;
    private static final double HOVER_THRESHOLD = 10;
    private static final double PATH_THICKNESS = 6;
    
    private int hoveredWallRow = -1;
    private int hoveredWallCol = -1;
    private int hoveredWallSide = -1;
    
    // Animation for path
    private Timeline pathAnimation;
    private double pathProgress = 0;
    
    // Callback for when walls are modified (to trigger re-validation)
    private Runnable onWallModified;
    
    public MazeCanvas() {
        super();
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        this.setOnMouseMoved(this::handleMouseMoved);
        this.setOnMouseClicked(this::handleMouseClicked);
        this.setOnMouseExited(e -> {
            hoveredWallRow = -1;
            hoveredWallCol = -1;
            hoveredWallSide = -1;
            draw();
        });
    }
    
    public void setOnWallModified(Runnable callback) {
        this.onWallModified = callback;
    }
    
    public void setMaze(Maze maze) {
        this.maze = maze;
        this.path.clear();
        stopPathAnimation();
        calculateCellSize();
    }
    
    public void setPath(List<Cell> path) {
        this.path = path != null ? new ArrayList<>(path) : new ArrayList<>();
        if (!this.path.isEmpty()) {
            startPathAnimation();
        }
    }
    
    private void startPathAnimation() {
        stopPathAnimation();
        pathProgress = 0;
        
        pathAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(this.pathProgressProperty(), 0)),
            new KeyFrame(Duration.millis(1500), new KeyValue(this.pathProgressProperty(), 1))
        );
        pathAnimation.setCycleCount(1);
        pathAnimation.play();
    }
    
    private void stopPathAnimation() {
        if (pathAnimation != null) {
            pathAnimation.stop();
        }
        pathProgress = 1;
    }
    
    private DoubleProperty pathProgressProperty() {
        return new SimpleDoubleProperty() {
            @Override
            public double get() { return pathProgress; }
            @Override
            public void set(double value) { 
                pathProgress = value; 
                draw();
            }
        };
    }
    
    private void calculateCellSize() {
        if (maze == null) return;
        double availableWidth = getWidth() - 60;
        double availableHeight = getHeight() - 60;
        cellSize = Math.min(availableWidth / maze.getCols(), availableHeight / maze.getRows());
        offsetX = (getWidth() - cellSize * maze.getCols()) / 2;
        offsetY = (getHeight() - cellSize * maze.getRows()) / 2;
    }
    
    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        
        if (maze == null) return;
        
        calculateCellSize();
        
        // Draw background gradient
        gc.setFill(Color.web("#1a1a2e"));
        gc.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw cells
        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {
                drawCell(gc, maze.getCell(r, c));
            }
        }
        
        // Draw path overlay
        drawPath(gc);
        
        // Draw hover highlight
        drawHoverHighlight(gc);
    }
    
    private void drawCell(GraphicsContext gc, Cell cell) {
        int r = cell.getRow();
        int c = cell.getCol();
        double x = offsetX + c * cellSize;
        double y = offsetY + r * cellSize;
        
        // Cell background with gradient
        if (cell.isVisited()) {
            // Animated visited cells during generation - cyan/blue glow
            LinearGradient gradient = new LinearGradient(
                x, y, x + cellSize, y + cellSize,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#00d4ff")),
                new Stop(1, Color.web("#0099cc"))
            );
            gc.setFill(gradient);
            gc.fillRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
        } else {
            gc.setFill(Color.web("#16213e"));
            gc.fillRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
        }
        
        // Start cell - vibrant green with glow
        if (r == 0 && c == 0) {
            gc.setFill(Color.web("#00ff88"));
            gc.fillRoundRect(x + 4, y + 4, cellSize - 8, cellSize - 8, 8, 8);
            gc.setStroke(Color.web("#00ff88"));
            gc.setLineWidth(2);
            gc.strokeRoundRect(x + 4, y + 4, cellSize - 8, cellSize - 8, 8, 8);
            
            // Label
            gc.setFill(Color.BLACK);
            gc.setFont(javafx.scene.text.Font.font("Arial", cellSize * 0.3));
            gc.fillText("S", x + cellSize/2 - 5, y + cellSize/2 + 5);
        } 
        // End cell - vibrant red with glow
        else if (r == maze.getRows() - 1 && c == maze.getCols() - 1) {
            gc.setFill(Color.web("#ff3366"));
            gc.fillRoundRect(x + 4, y + 4, cellSize - 8, cellSize - 8, 8, 8);
            gc.setStroke(Color.web("#ff3366"));
            gc.setLineWidth(2);
            gc.strokeRoundRect(x + 4, y + 4, cellSize - 8, cellSize - 8, 8, 8);
            
            // Label
            gc.setFill(Color.WHITE);
            gc.setFont(javafx.scene.text.Font.font("Arial", cellSize * 0.3));
            gc.fillText("E", x + cellSize/2 - 5, y + cellSize/2 + 5);
        }
        
        // Draw walls with neon effect
        gc.setStroke(Color.web("#00f2ff"));
        gc.setLineWidth(WALL_THICKNESS);
        gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        
        if (cell.hasTopWall()) {
            gc.strokeLine(x, y, x + cellSize, y);
        }
        if (cell.hasRightWall()) {
            gc.strokeLine(x + cellSize, y, x + cellSize, y + cellSize);
        }
        if (cell.hasBottomWall()) {
            gc.strokeLine(x, y + cellSize, x + cellSize, y + cellSize);
        }
        if (cell.hasLeftWall()) {
            gc.strokeLine(x, y, x, y + cellSize);
        }
    }
    
    private void drawPath(GraphicsContext gc) {
        if (path == null || path.size() < 2) return;
        
        int maxIndex = (int) ((path.size() - 1) * pathProgress);
        
        // Draw glow effect behind path
        gc.setStroke(Color.web("#ff00ff", 0.3));
        gc.setLineWidth(PATH_THICKNESS + 8);
        gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        gc.setLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);
        
        drawPathSegment(gc, maxIndex);
        
        // Draw main path - bright yellow/green
        gc.setStroke(Color.web("#ffff00"));
        gc.setLineWidth(PATH_THICKNESS);
        gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        gc.setLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);
        
        drawPathSegment(gc, maxIndex);
        
        // Draw white center line
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        drawPathSegment(gc, maxIndex);
        
        // Draw animated dots at path vertices
        if (maxIndex > 0) {
            gc.setFill(Color.web("#ff00ff"));
            for (int i = 0; i <= maxIndex && i < path.size(); i++) {
                Cell cell = path.get(i);
                double cx = offsetX + cell.getCol() * cellSize + cellSize / 2;
                double cy = offsetY + cell.getRow() * cellSize + cellSize / 2;
                gc.fillOval(cx - 4, cy - 4, 8, 8);
            }
        }
    }
    
    private void drawPathSegment(GraphicsContext gc, int maxIndex) {
        if (path.size() < 2) return;
        
        Cell start = path.get(0);
        double startX = offsetX + start.getCol() * cellSize + cellSize / 2;
        double startY = offsetY + start.getRow() * cellSize + cellSize / 2;
        
        gc.beginPath();
        gc.moveTo(startX, startY);
        
        for (int i = 1; i <= maxIndex && i < path.size(); i++) {
            Cell cell = path.get(i);
            double x = offsetX + cell.getCol() * cellSize + cellSize / 2;
            double y = offsetY + cell.getRow() * cellSize + cellSize / 2;
            gc.lineTo(x, y);
        }
        gc.stroke();
    }
    
    private void drawHoverHighlight(GraphicsContext gc) {
        if (hoveredWallRow < 0 || hoveredWallCol < 0 || hoveredWallSide < 0) return;
        
        double x = offsetX + hoveredWallCol * cellSize;
        double y = offsetY + hoveredWallRow * cellSize;
        
        // Pulsing red highlight
        gc.setStroke(Color.web("#ff0040"));
        gc.setLineWidth(WALL_THICKNESS + 4);
        gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        
        switch (hoveredWallSide) {
            case 0: // top
                gc.strokeLine(x - 2, y, x + cellSize + 2, y);
                break;
            case 1: // right
                gc.strokeLine(x + cellSize, y - 2, x + cellSize, y + cellSize + 2);
                break;
            case 2: // bottom
                gc.strokeLine(x - 2, y + cellSize, x + cellSize + 2, y + cellSize);
                break;
            case 3: // left
                gc.strokeLine(x, y - 2, x, y + cellSize + 2);
                break;
        }
    }
    
    private void handleMouseMoved(MouseEvent event) {
        if (maze == null) return;
        
        double mouseX = event.getX();
        double mouseY = event.getY();
        
        int[] wallInfo = findNearestWall(mouseX, mouseY);
        
        if (wallInfo != null) {
            if (wallInfo[0] != hoveredWallRow || 
                wallInfo[1] != hoveredWallCol || 
                wallInfo[2] != hoveredWallSide) {
                
                hoveredWallRow = wallInfo[0];
                hoveredWallCol = wallInfo[1];
                hoveredWallSide = wallInfo[2];
                draw();
            }
        } else if (hoveredWallRow >= 0) {
            hoveredWallRow = -1;
            hoveredWallCol = -1;
            hoveredWallSide = -1;
            draw();
        }
    }
    
    private void handleMouseClicked(MouseEvent event) {
        if (maze == null) return;
        
        double mouseX = event.getX();
        double mouseY = event.getY();
        
        int[] wallInfo = findNearestWall(mouseX, mouseY);
        if (wallInfo == null) return;
        
        int r = wallInfo[0];
        int c = wallInfo[1];
        int side = wallInfo[2];
        
        Cell cell = maze.getCell(r, c);
        toggleWall(cell, side);
        path.clear();
        stopPathAnimation();
        draw();
        
        // Trigger re-validation to find new shortest path
        if (onWallModified != null) {
            onWallModified.run();
        }
    }
    
    private int[] findNearestWall(double mouseX, double mouseY) {
        if (maze == null) return null;
        
        double minDistance = Double.MAX_VALUE;
        int[] result = null;
        
        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {
                double x = offsetX + c * cellSize;
                double y = offsetY + r * cellSize;
                Cell cell = maze.getCell(r, c);
                
                // Check each wall
                if (cell.hasTopWall()) {
                    double dist = distanceToLineSegment(mouseX, mouseY, x, y, x + cellSize, y);
                    if (dist < minDistance && dist < HOVER_THRESHOLD) {
                        minDistance = dist;
                        result = new int[]{r, c, 0};
                    }
                }
                if (cell.hasRightWall()) {
                    double dist = distanceToLineSegment(mouseX, mouseY, x + cellSize, y, x + cellSize, y + cellSize);
                    if (dist < minDistance && dist < HOVER_THRESHOLD) {
                        minDistance = dist;
                        result = new int[]{r, c, 1};
                    }
                }
                if (cell.hasBottomWall()) {
                    double dist = distanceToLineSegment(mouseX, mouseY, x, y + cellSize, x + cellSize, y + cellSize);
                    if (dist < minDistance && dist < HOVER_THRESHOLD) {
                        minDistance = dist;
                        result = new int[]{r, c, 2};
                    }
                }
                if (cell.hasLeftWall()) {
                    double dist = distanceToLineSegment(mouseX, mouseY, x, y, x, y + cellSize);
                    if (dist < minDistance && dist < HOVER_THRESHOLD) {
                        minDistance = dist;
                        result = new int[]{r, c, 3};
                    }
                }
            }
        }
        
        return result;
    }
    
    private double distanceToLineSegment(double px, double py, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double lengthSq = dx * dx + dy * dy;
        
        if (lengthSq == 0) return Math.hypot(px - x1, py - y1);
        
        double t = Math.max(0, Math.min(1, ((px - x1) * dx + (py - y1) * dy) / lengthSq));
        double projectionX = x1 + t * dx;
        double projectionY = y1 + t * dy;
        
        return Math.hypot(px - projectionX, py - projectionY);
    }
    
    private void toggleWall(Cell cell, int side) {
        int r = cell.getRow();
        int c = cell.getCol();
        
        switch (side) {
            case 0: // top
                if (r > 0) {
                    boolean newState = !cell.hasTopWall();
                    cell.setTopWall(newState);
                    maze.getCell(r - 1, c).setBottomWall(newState);
                }
                break;
            case 1: // right
                if (c < maze.getCols() - 1) {
                    boolean newState = !cell.hasRightWall();
                    cell.setRightWall(newState);
                    maze.getCell(r, c + 1).setLeftWall(newState);
                }
                break;
            case 2: // bottom
                if (r < maze.getRows() - 1) {
                    boolean newState = !cell.hasBottomWall();
                    cell.setBottomWall(newState);
                    maze.getCell(r + 1, c).setTopWall(newState);
                }
                break;
            case 3: // left
                if (c > 0) {
                    boolean newState = !cell.hasLeftWall();
                    cell.setLeftWall(newState);
                    maze.getCell(r, c - 1).setRightWall(newState);
                }
                break;
        }
    }
    
    @Override
    public boolean isResizable() {
        return true;
    }
    
    @Override
    public double prefWidth(double height) {
        return getWidth();
    }
    
    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}
