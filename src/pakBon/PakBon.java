package pakBon;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import database.*;

public class PakBon {
    private int orderId;
    private String content = "";
    private Order order = new Order();
    public PakBon(int orderId) throws SQLException {
        this.orderId = orderId;
        addLine("Naam: " + order.getCustomerInfo(orderId).get(0));
        addLine("Adress: " + order.getCustomerInfo(orderId).get(1));
        addLine("Datum: " + order.getOrderDate(orderId)[1]);
        addLine("OrderNr: " + orderId);
        addLine("");

        int[] orderLineIds = order.getOrderLines(orderId);
        for (int orderLineId : orderLineIds) {
            ArrayList<ArrayList<String>> orderInfo = order.getOrderInfo(orderLineId);
            for (ArrayList<String> orderlines : orderInfo) {
                int i = 0;
                String name = "";
                for (String orderAttribute : orderlines) {
                    if (i == 0) {
                        name = "Naam: ";
                    } else if (i==1) {
                        name = "Hoeveelheid: ";
                    } else {
                        name = "Prijs: €";
                    }
                    addLine(name + orderAttribute);
                    i++;
                }
                content += "\n";

            }
        }

        createTextFile(orderId, content);

    }


    private void addLine(String line) {
        content += line + "\n";

    }

    public static void createTextFile(int fileName, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(fileName)));
            writer.write(content);
            writer.close();
            System.out.println("Text file created successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws SQLException {
        Connectie connectie = new Connectie();
        connectie.connect();

        new PakBon(1);
    }
}
