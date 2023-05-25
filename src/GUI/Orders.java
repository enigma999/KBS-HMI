package GUI;

import javafx.scene.input.MouseEvent;
import java.io.IOException;

public class Orders {
    private SerialCom.SerialComm serialComm;


    public void CreateOrder(MouseEvent mouseEvent) {
        // Implement the logic to create an order and send it to the database
    }

    public void ChangeOrder(MouseEvent mouseEvent) {
        // Implement the logic to update an existing order
    }

    public void ViewOrders(MouseEvent mouseEvent) throws IOException {
        SimpleJavaFXGUI.setScreen(mouseEvent, "OrderWeergave.fxml");
    }

    public void SaveOrder(MouseEvent mouseEvent) {
        // Implement the logic to save the modified or newly created order to the database
    }
    public void noodstop(MouseEvent mouseEvent) {
        serialComm.noodstop();
    }

    public void besturingAutomatisch(MouseEvent mouseEvent) {
        serialComm.besturing(true);
    }

    public void besturingHandmatig(MouseEvent mouseEvent) {
        serialComm.besturing(false);
    }

}
