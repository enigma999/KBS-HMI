package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import SerialCom.SerialComm;
import GUI.Orders;

import java.io.IOException;

public class SimpleJavaFXGUI extends Application {
    private Orders orders;
    private SerialComm serialComm;


    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HomeGUI.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        orders = new Orders(); // Create an instance of the Orders class
        serialComm = new SerialCom.SerialComm("COM1"); // Replace "COM1" with your desired COM port
        Thread serialThread = new Thread(serialComm);
        serialThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void BackGUI(MouseEvent mouseEvent) throws IOException {
        setScreen(mouseEvent, "HomeGUI.fxml");
    }

    public void CreateOrder(MouseEvent mouseEvent) {
        orders.CreateOrder(mouseEvent); // Call the CreateOrder method from the Orders class
    }

    public void ChangeOrder(MouseEvent mouseEvent) {
        orders.ChangeOrder(mouseEvent); // Call the ChangeOrder method from the Orders class
    }

    public void ViewOrders(MouseEvent mouseEvent) throws IOException {
        orders.ViewOrders(mouseEvent); // Call the ViewOrders method from the Orders class
    }

    public void SaveOrder(MouseEvent mouseEvent) {
        orders.SaveOrder(mouseEvent); // Call the SaveOrder method from the Orders class
    }

    public static void setScreen(MouseEvent mouseEvent, String page) throws IOException {
        Parent root = FXMLLoader.load(SimpleJavaFXGUI.class.getResource(page));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
