package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TrafficLightController {
    @FXML
    private Circle redCircle;
    @FXML
    private Circle yellowCircle;
    @FXML
    private Circle greenCircle;
    @FXML
    private GridPane gridPane;

    private int gridSize = 5; // Change this value to adjust the grid size

    public void initialize() {
        setupGrid();
    }

    private void setupGrid() {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Circle circle = new Circle(20, Color.GRAY);
                gridPane.add(circle, col, row);
            }
        }
    }

    public void setRedLight() {
        redCircle.setFill(Color.RED);
        yellowCircle.setFill(Color.GRAY);
        greenCircle.setFill(Color.GRAY);
    }

    public void setYellowLight() {
        redCircle.setFill(Color.GRAY);
        yellowCircle.setFill(Color.YELLOW);
        greenCircle.setFill(Color.GRAY);
    }

    public void setGreenLight() {
        redCircle.setFill(Color.GRAY);
        yellowCircle.setFill(Color.GRAY);
        greenCircle.setFill(Color.GREEN);
    }
}
