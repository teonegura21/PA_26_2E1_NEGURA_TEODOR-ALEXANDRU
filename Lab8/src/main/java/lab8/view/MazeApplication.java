package lab8.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lab8.controller.MazeController;

/* COMPULSORY - Main JavaFX Application */
public class MazeApplication extends Application {
    private MazeController controller;
    private MazeCanvas mazeCanvas;
    private ConfigPanel configPanel;
    private ControlPanel controlPanel;
    
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Create canvas
        mazeCanvas = new MazeCanvas();
        
        // Wrap canvas in a Pane so it respects BorderPane layout
        Pane canvasWrapper = new Pane(mazeCanvas);
        
        // Create controller
        controller = new MazeController(mazeCanvas);
        
        // Create panels
        configPanel = new ConfigPanel(controller);
        controlPanel = new ControlPanel(controller);
        
        // Layout
        root.setTop(configPanel);
        root.setCenter(canvasWrapper);
        root.setBottom(controlPanel);
        
        // Create default maze
        controller.createMaze(15, 15);
        
        Scene scene = new Scene(root, 900, 700);
        primaryStage.setTitle("Lab 8 - Maze Generator (JavaFX)");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Bind canvas size to wrapper size
        mazeCanvas.widthProperty().bind(canvasWrapper.widthProperty());
        mazeCanvas.heightProperty().bind(canvasWrapper.heightProperty());
        
        // Redraw when canvas size changes
        mazeCanvas.widthProperty().addListener((obs, oldVal, newVal) -> mazeCanvas.draw());
        mazeCanvas.heightProperty().addListener((obs, oldVal, newVal) -> mazeCanvas.draw());
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
