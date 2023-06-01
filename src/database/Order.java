package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

// getOrderStockID(), input is int(orderid), hij returnt een array met de stockitemids die bij de order horen.
// getOrderLines(), input is int(orderid), hij returnt een array met de orderlineids die bij de order horen.
// getOrderInfo(), input is int(orderid), hij return een array met de items, hoeveelheid en prijs die bij de order passen.
// getCustomerInfo(), input is int(orderid), returnt naam en adres.
// getOrderDate(), input is int(orderid), returnt orderid en orderdatum.
// printPakBon(), input is int(orderid), print de pakbon in de terminal.
// getPakBon(), input is int(orderid), returnt een array met alle informatie van de pakbon.
// isPicked(), input is int(orderid), geen output, zorgt dat de tabel is picked wordt bijgewerkt.
public class Order extends Connectie {
    public int getNextOrderID() throws SQLException {
        int nextOrderID = 0;
        if (!this.isConnected())
            this.connect();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT MAX(OrderID) FROM ordertabel");

        if (resultSet.next()) {
            nextOrderID = resultSet.getInt(1) + 1;
        }

        resultSet.close();
        statement.close();

        return nextOrderID;
    }

    public int[] getOrderStockID(int orderid) throws SQLException {
        if (!this.isConnected())
            this.connect();

        ArrayList<ArrayList<String>> result = queryResult("SELECT StockItemID FROM orderline WHERE OrderID = " + orderid);
        int[] orderArray = new int[result.size()];

        for (int i = 0; i < result.size(); i++) {
            ArrayList<String> innerList = result.get(i);
            String value = innerList.get(0);
            orderArray[i] = Integer.parseInt(value);
        }

        return orderArray;
    }

    public int[] getOrderLines(int orderid) throws SQLException {
        if (!this.isConnected())
            this.connect();
        ArrayList<ArrayList<String>> result = queryResult("select orderlineID from orderline where OrderID = " + orderid);
        int[] orderArray = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            ArrayList<String> innerList = result.get(i);
            String value = innerList.get(0);
            orderArray[i] = Integer.parseInt(value);
        }

        return orderArray;
    }

    public ArrayList<ArrayList<String>> getOrderInfo(int orderid) throws SQLException {
        if (!this.isConnected())
            this.connect();

        int[] orderLineIDs = getOrderLines(orderid);

        ArrayList<ArrayList<String>> orderInfo = new ArrayList<>();

        if (orderLineIDs.length == 0) {
            // Handle the case when no order lines are found
            System.out.println("No order lines found for order ID: " + orderid);
        } else {
            for (int orderLineID : orderLineIDs) {
                ArrayList<ArrayList<String>> result = queryResult("SELECT description, quantity, unitprice FROM orderline WHERE OrderLineID = " + orderLineID);

                orderInfo.addAll(result);
            }
        }

        return orderInfo;
    }

    public ArrayList<String> getCustomerInfo(int orderid) throws SQLException {
        if (!this.isConnected())
            this.connect();

        ArrayList<ArrayList<String>> result = queryResult("select CustomerID from ordertabel where OrderID = " + orderid);
        String customerId = result.get(0).get(0);
        ArrayList<ArrayList<String>> result2 = queryResult("select CustomerName from customer where CustomerID = " + customerId);
        return result2.get(0);
    }

    public String[] getOrderDate(int orderid) throws SQLException {
        if (!this.isConnected())
            this.connect();

        ArrayList<ArrayList<String>> orderDateResult = queryResult("SELECT OrderDate FROM ordertabel WHERE OrderID = " + orderid);
        String orderDate = orderDateResult.get(0).get(0);

        String[] orderDateArray = new String[2];
        orderDateArray[0] = String.valueOf(orderid);
        orderDateArray[1] = orderDate;

        return orderDateArray;
    }

    public void printPakBon(int orderid) throws SQLException {
        ArrayList<ArrayList<String>> orderInfo = getOrderInfo(orderid);
        ArrayList<String> customerInfo = getCustomerInfo(orderid);
        String[] orderDateArray = getOrderDate(orderid);

        System.out.println("OrderID en Datum: [" + orderid + ", " + orderDateArray[1] + "]");
        System.out.println("Klant: " + customerInfo);
        System.out.println("Producten, Aantal en Prijs: " + orderInfo);
    }

    public ArrayList<ArrayList<String>> getPakBon(int orderid) throws SQLException {
        ArrayList<ArrayList<String>> orderAndCustomerInfo = new ArrayList<>();

        ArrayList<ArrayList<String>> orderInfo = getOrderInfo(orderid);
        ArrayList<String> customerInfo = getCustomerInfo(orderid);
        String[] orderDateArray = getOrderDate(orderid);

        orderAndCustomerInfo.addAll(orderInfo);
        orderAndCustomerInfo.add(customerInfo);
        orderAndCustomerInfo.add(new ArrayList<>(Arrays.asList(orderDateArray)));

        return orderAndCustomerInfo;
    }

    public void isPicked(int orderid) throws SQLException {
        if (!this.isConnected())
            this.connect();

        String updateQuery = "UPDATE ordertabel SET picked = 1 WHERE OrderID = " + orderid;

        Statement statement = connection.createStatement();
        int rowsAffected = statement.executeUpdate(updateQuery);
        statement.close();
    }

    public void updateOrder(int orderID, int[] quantities) throws SQLException {

        if (!this.isConnected())
            this.connect();

        int[] orderLineIDs = getOrderLines(orderID);

        if (orderLineIDs.length != quantities.length) {
            throw new IllegalArgumentException("Number of quantities does not match the number of order lines.");
        }

        for (int i = 0; i < orderLineIDs.length; i++) {
            int orderLineID = orderLineIDs[i];
            int quantity = quantities[i];

            PreparedStatement statement = connection.prepareStatement("UPDATE orderline SET quantity = ? WHERE OrderlineID = ?");
            statement.setInt(1, quantity);
            statement.setInt(2, orderLineID);
            statement.executeUpdate();
            statement.close();
        }
    }

    private void insertOrderTable(int orderID, LocalDate date, int customerID) throws SQLException {
        PreparedStatement orderStatement = connection.prepareStatement("INSERT INTO ordertabel (OrderID, OrderDate, Picked, CustomerID) VALUES (?, ?, 0, ?)");
        orderStatement.setInt(1, orderID);
        orderStatement.setDate(2, java.sql.Date.valueOf(date));
        orderStatement.setInt(3, customerID);
        orderStatement.executeUpdate();
    }



    public boolean orderExists(int orderID) throws SQLException {
        if (!this.isConnected())
            this.connect();

        PreparedStatement statement = connection.prepareStatement("SELECT OrderID FROM ordertabel WHERE OrderID = ?");
        statement.setInt(1, orderID);
        ResultSet result = statement.executeQuery();
        boolean exists = result.next();
        statement.close();
        return exists;
    }

    private ArrayList<String> getInfoStockitem(int stockitemID) throws SQLException {
        if (!this.isConnected())
            this.connect();
        ArrayList<ArrayList<String>> result = queryResult("select stockitemname, unitprice from stockitem where StockItemID = " + stockitemID);
        return result.get(0);
    }

    public int insertOrder(LocalDate date, int[] stockitemIDs, int[] quantities, int customerID) throws SQLException {
        if (!this.isConnected())
            this.connect();

        int newOrderID = getNextOrderID();

        for (int i = 0; i < stockitemIDs.length; i++) {
            int stockitemID = stockitemIDs[i];
            int quantity = quantities[i];

            ArrayList<String> stockitemInfo = getInfoStockitem(stockitemID);
            String stockitemName = stockitemInfo.get(0);
            double unitPrice = Double.parseDouble(stockitemInfo.get(1));

            insertOrderLine(newOrderID, stockitemID, stockitemName, quantity, unitPrice);
        }

        insertOrderTable(newOrderID, date, customerID);

        return newOrderID;
    }

    private void insertOrderLine(int orderID, int stockitemID, String description, int quantity, double unitPrice) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO orderline (orderid, stockitemid, description, quantity, unitprice) VALUES (?, ?, ?, ?, ?)");
        statement.setInt(1, orderID);
        statement.setInt(2, stockitemID);
        statement.setString(3, description);
        statement.setInt(4, quantity);
        statement.setDouble(5, unitPrice);
        statement.executeUpdate();
    }
}
