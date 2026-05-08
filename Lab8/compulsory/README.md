# Compulsory (1p)

## Requirements Met

1. **Main Frame**: `MazeApplication.java` creates a JavaFX stage with BorderPane layout
2. **Configuration Panel**: `ConfigPanel.java` - Top panel with:
   - Labels: "Rows:" and "Cols:"
   - Input components: Two Spinner<Integer> controls (5-50 range)
   - Button: "Draw Grid" to create the maze grid
3. **Canvas**: `MazeCanvas.java` - Custom Canvas in the center:
   - Draws cells as squares with configurable size
   - Draws walls as black lines around each square
   - Start cell (top-left) highlighted in green
   - End cell (bottom-right) highlighted in red
4. **Control Panel**: `ControlPanel.java` - Bottom panel with:
   - "Create (Random)" button: Randomly removes ~30% of walls
   - "Reset" button: Restores all walls
   - "Exit" button: Closes the application

## How to Run

```bash
mvn javafx:run
```

This single command runs the entire application including all features.
