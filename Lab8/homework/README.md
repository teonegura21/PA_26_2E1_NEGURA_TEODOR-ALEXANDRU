# Homework (2p)

## Additional Requirements Met

### 1. Manual Wall Editing
- **File**: `MazeCanvas.java`
- **Feature**: Click on any wall to toggle it on/off
- **Implementation**: 
  - Distance calculation from mouse to wall line segments
  - 8-pixel hover threshold for precise clicking
  - Red highlight on hover to indicate clickable wall
  - Bidirectional wall toggle (updates both adjacent cells)

### 2. Maze Validation
- **File**: `MazeValidator.java`
- **Feature**: BFS validation from start (0,0) to end (rows-1, cols-1)
- **Visual**: Green line drawn over the maze showing the valid path
- **Implementation**: Breadth-first search with parent tracking to reconstruct path

### 3. PNG Export
- **File**: `MazeExporter.java`
- **Feature**: Export current maze canvas to PNG image file
- **Implementation**: JavaFX Canvas.snapshot() → WritableImage → ImageIO

### 4. Object Serialization
- **File**: `MazeSerializer.java`
- **Feature**: Save/Load maze state to .maze files
- **Implementation**: ObjectOutputStream / ObjectInputStream
- **Usage**: "Save" and "Load" buttons in Control Panel

## Files Added/Modified for Homework

- `util/MazeValidator.java` - NEW
- `util/MazeExporter.java` - NEW
- `util/MazeSerializer.java` - NEW
- `view/MazeCanvas.java` - MODIFIED (wall clicking, hover)
- `view/ControlPanel.java` - MODIFIED (new buttons)
- `controller/MazeController.java` - MODIFIED (new actions)
