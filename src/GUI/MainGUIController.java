package GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import GUI.SimpleJavaFXGUI;


public class MainGUIController {
    @FXML
    private SimpleJavaFXGUI simpleJavaFXGUIController;

    @FXML
    private TrafficLightController trafficLightController;

    // Access methods and handle events using the injected controllers

    @FXML
    private void initialize() {
        // Initialize the controllers or perform any necessary setup
    }
}
