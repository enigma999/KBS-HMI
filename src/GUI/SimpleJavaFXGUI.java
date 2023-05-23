package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


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

    public void BackGUI(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }


    public void CreateOrder(MouseEvent mouseEvent) {
    }

    public void ChangeOrder(MouseEvent mouseEvent) {
    }

    public void ViewOrders(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("OrderWeergave.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    public void TSPTest(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TSPTest.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    public void SaveOrder(MouseEvent mouseEvent) {
    }
}