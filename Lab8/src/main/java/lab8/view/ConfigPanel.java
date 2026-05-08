package lab8.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import lab8.controller.MazeController;

/* COMPULSORY - Configuration panel (top) */
public class ConfigPanel extends HBox {
    private MazeController controller;
    private Spinner<Integer> rowsSpinner;
    private Spinner<Integer> colsSpinner;
    private Button drawButton;
    
    public ConfigPanel(MazeController controller) {
        this.controller = controller;
        initialize();
    }
    
    private void initialize() {
        setAlignment(Pos.CENTER);
        setSpacing(15);
        setPadding(new Insets(10));
        
        // Label and input for rows
        Label rowsLabel = new Label("Rows:");
        rowsSpinner = new Spinner<>(5, 50, 15);
        rowsSpinner.setPrefWidth(80);
        
        // Label and input for cols
        Label colsLabel = new Label("Cols:");
        colsSpinner = new Spinner<>(5, 50, 15);
        colsSpinner.setPrefWidth(80);
        
        // Draw button
        drawButton = new Button("Draw Grid");
        drawButton.setStyle("-fx-font-weight: bold;");
        drawButton.setOnAction(e -> {
            int rows = rowsSpinner.getValue();
            int cols = colsSpinner.getValue();
            controller.createMaze(rows, cols);
        });
        
        getChildren().addAll(rowsLabel, rowsSpinner, colsLabel, colsSpinner, drawButton);
    }
    
    public int getRows() { return rowsSpinner.getValue(); }
    public int getCols() { return colsSpinner.getValue(); }
}
