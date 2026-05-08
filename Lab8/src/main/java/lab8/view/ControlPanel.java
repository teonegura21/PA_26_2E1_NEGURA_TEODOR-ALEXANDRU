package lab8.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import lab8.controller.MazeController;

import java.io.File;

/* COMPULSORY/HOMEWORK/BONUS - Control panel (bottom) */
public class ControlPanel extends HBox {
    private MazeController controller;
    private Slider speedSlider;
    private Button generateButton;
    private Button stopButton;
    private Label speedLabel;
    
    public ControlPanel(MazeController controller) {
        this.controller = controller;
        initialize();
    }
    
    private void initialize() {
        setAlignment(Pos.CENTER);
        setSpacing(10);
        setPadding(new Insets(10));
        
        // Create button (random walls) - COMPULSORY
        Button createButton = new Button("Create (Random)");
        createButton.setOnAction(e -> controller.createRandomMaze());
        
        // Generate Perfect Maze - BONUS
        generateButton = new Button("Generate Perfect");
        generateButton.setOnAction(e -> controller.generatePerfectMaze());
        
        // Stop Generation - BONUS
        stopButton = new Button("Stop");
        stopButton.setOnAction(e -> controller.stopGeneration());
        
        // Reset button - COMPULSORY
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> {
            controller.resetMaze();
            controller.clearPath();
        });
        
        // Validate button - HOMEWORK
        Button validateButton = new Button("Validate");
        validateButton.setOnAction(e -> controller.validateMaze());
        
        // Export PNG - HOMEWORK
        Button exportButton = new Button("Export PNG");
        exportButton.setOnAction(e -> exportPng());
        
        // Save button - HOMEWORK
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveMaze());
        
        // Load button - HOMEWORK
        Button loadButton = new Button("Load");
        loadButton.setOnAction(e -> loadMaze());
        
        // Exit button - COMPULSORY
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> System.exit(0));
        
        // Speed slider - BONUS
        speedLabel = new Label("Anim Speed:");
        speedSlider = new Slider(1, 100, 50);
        speedSlider.setPrefWidth(150);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(25);
        speedSlider.setTooltip(new javafx.scene.control.Tooltip(
            "Animation speed: 1 = Slow (500ms/step), 100 = Fast (10ms/step)"
        ));
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            controller.getAnimationController().setSpeed(newVal.doubleValue());
        });
        
        getChildren().addAll(
            createButton, generateButton, stopButton, resetButton,
            validateButton, exportButton, saveButton, loadButton,
            speedLabel, speedSlider, exitButton
        );
    }
    
    private void exportPng() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Maze to PNG");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PNG Files", "*.png")
        );
        File file = fileChooser.showSaveDialog(getScene().getWindow());
        if (file != null) {
            try {
                controller.exportToPng(file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void saveMaze() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Maze");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Maze Files", "*.maze")
        );
        File file = fileChooser.showSaveDialog(getScene().getWindow());
        if (file != null) {
            try {
                controller.saveMaze(file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void loadMaze() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Maze");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Maze Files", "*.maze")
        );
        File file = fileChooser.showOpenDialog(getScene().getWindow());
        if (file != null) {
            try {
                controller.loadMaze(file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
