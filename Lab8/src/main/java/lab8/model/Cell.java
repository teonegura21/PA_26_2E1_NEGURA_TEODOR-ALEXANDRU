package lab8.model;

import java.io.Serializable;

/* COMPULSORY - Model */
public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final int row;
    private final int col;
    private boolean topWall = true;
    private boolean rightWall = true;
    private boolean bottomWall = true;
    private boolean leftWall = true;
    private boolean visited = false;
    
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public int getRow() { return row; }
    public int getCol() { return col; }
    
    public boolean hasTopWall() { return topWall; }
    public boolean hasRightWall() { return rightWall; }
    public boolean hasBottomWall() { return bottomWall; }
    public boolean hasLeftWall() { return leftWall; }
    
    public void setTopWall(boolean topWall) { this.topWall = topWall; }
    public void setRightWall(boolean rightWall) { this.rightWall = rightWall; }
    public void setBottomWall(boolean bottomWall) { this.bottomWall = bottomWall; }
    public void setLeftWall(boolean leftWall) { this.leftWall = leftWall; }
    
    public boolean isVisited() { return visited; }
    public void setVisited(boolean visited) { this.visited = visited; }
    
    public void resetWalls() {
        topWall = true;
        rightWall = true;
        bottomWall = true;
        leftWall = true;
    }
    
    public int getWallCount() {
        int count = 0;
        if (topWall) count++;
        if (rightWall) count++;
        if (bottomWall) count++;
        if (leftWall) count++;
        return count;
    }
}
