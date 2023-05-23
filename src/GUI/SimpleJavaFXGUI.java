package GUI;

import javafx.application.Application ;
import javafx.stage.Stage ;
import javafx.scene.Scene ;
import javafx.scene.Parent ;
import javafx.fxml.FXMLLoader ;

import java.io.IOException ;


public class SimpleJavaFXGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // for the benefit of environments that don't support direct launch
    // of JavaFX Applications:

    public static void main(String[] args) {
        launch(args);
    }
}