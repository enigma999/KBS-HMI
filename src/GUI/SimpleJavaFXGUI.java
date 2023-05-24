package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
//BackGUI refereert terug naar de main GUI pagina
//CreateOrder moet een order maken in de stijl van de database
//ChangeOrder moet een order updaten naar de nieuwe order
//ViewOrders moet een lijst geven van niet voltooide orders
//TSPTest moet laten zien welke tsp oplossing in welk geval beter is en wat de runtime is
//SaveOrder moet de aangepaste of gecreeerde order opslaan naar de database
//Setscreen past de pagina grootte aan naar wat wij willen van het scherm.

public class SimpleJavaFXGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);
    }

    // for the benefit of environments that don't support direct launch
    // of JavaFX Applications:

    public static void main(String[] args) {
        launch(args);
    }

    public void BackGUI(MouseEvent mouseEvent) throws IOException {
        setScreen(mouseEvent,"GUI.fxml");
    }


    public void CreateOrder(MouseEvent mouseEvent) {
        //hier moet met een method van marit een order de database in gestuurd worden
    }

    public void ChangeOrder(MouseEvent mouseEvent) {
        //hier moet een van de methods van marit een order aangepast kunnen worden.
    }

    public void ViewOrders(MouseEvent mouseEvent) throws IOException {
        setScreen(mouseEvent,"OrderWeergave.fxml");
    }

    public void TSPTest(MouseEvent mouseEvent) throws IOException {
        setScreen(mouseEvent,"TSPTest.fxml");
    }

    public void SaveOrder(MouseEvent mouseEvent) {
        //hier moet er een order in de database kunnen worden opgeslagen
    }

    private void setScreen(MouseEvent mouseEvent, String page) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource(page));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
