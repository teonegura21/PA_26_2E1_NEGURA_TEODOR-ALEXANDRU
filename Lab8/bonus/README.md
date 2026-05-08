# Bonus (2p)

## Additional Requirements Met

### 1. Recursive Backtracking Algorithm
- **File**: `MazeGenerator.java`
- **Algorithm**: Depth-First Search (DFS) with stack
- **Process**:
  1. Start at cell (0,0), mark as visited
  2. While stack not empty:
     - Peek at current cell
     - If unvisited neighbors exist: pick random neighbor, remove walls, push to stack
     - If no unvisited neighbors: pop from stack (backtrack)
  3. Result: Perfect maze

### 2. Animated Generation
- **File**: `AnimationController.java`
- **Feature**: Visual step-by-step maze generation
- **Implementation**:
  - JavaFX Timeline with KeyFrames
  - Adjustable speed via Slider (1-100 maps to 500ms-10ms per step)
  - Current cell highlighted in light blue during generation
  - "Stop" button to interrupt generation

### 3. Proof of Validity

**Theorem**: The recursive backtracking algorithm produces a perfect maze.

**Proof**:

1. **Spanning Tree Property**: 
   - The grid graph has V = rows × cols vertices
   - Each wall removal creates exactly one edge
   - Algorithm stops when all cells are visited
   - Total edges removed = V - 1 (standard spanning tree property)
   - Therefore: |E| = |V| - 1

2. **Connected Component**:
   - DFS visits every cell exactly once
   - Every cell is reachable from the start
   - Exactly one connected component

3. **No Isolated Areas**:
   - By construction, every cell is connected to the spanning tree
   - No cell is left unvisited

4. **Unique Path**:
   - Spanning tree has no cycles
   - Between any two vertices in a tree, there is exactly one path
   - Therefore: unique path between any two cells

**Runtime Verification** (`MazeValidator.isPerfectMaze()`):
- BFS confirms all cells reachable from start
- Count passages (removed walls) = rows × cols - 1
- Both conditions must hold for a perfect maze

### 4. Speed Control
- Slider in Control Panel (1 = slow, 100 = fast)
- Maps to delay: 510 - (speed × 5) milliseconds
- Range: 10ms to 505ms per step

## Files Added/Modified for Bonus

- `controller/MazeGenerator.java` - NEW
- `controller/AnimationController.java` - NEW
- `view/ControlPanel.java` - MODIFIED (speed slider, generate/stop buttons)
- `controller/MazeController.java` - MODIFIED (generatePerfectMaze method)

## Mathematical Properties

For an R×C maze:
- Total cells: R × C
- Required passages for perfect maze: R × C - 1
- Internal walls in full grid: R(C-1) + C(R-1) = 2RC - R - C
- Walls remaining in perfect maze: (2RC - R - C) - (RC - 1) = RC - R - C + 1
